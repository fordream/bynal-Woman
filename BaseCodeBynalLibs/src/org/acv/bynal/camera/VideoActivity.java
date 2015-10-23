/* Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.acv.bynal.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;

import org.acv.bynal.camera.Authorizer.AuthorizationListener;
import org.acv.bynal.camera.uploadyoutube.Internal500ResumeException;
import org.acv.bynal.camera.uploadyoutube.ResumeInfo;
import org.acv.bynal.camera.uploadyoutube.VideoUtil;
import org.acv.bynal.camera.uploadyoutube.YouTubeAccountException;
import org.acv.bynal.camera.uploadyoutube.YouTubeManager;
import org.acv.bynal.fragment.ProjectManagerFragment;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.ProgressView;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import a.com.acv.crash.CrashExceptionHandler;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.BaseLoactionActivity;
import com.acv.libs.base.DataStore;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;

//import com.google.ytd.db.DbHelper;

public class VideoActivity extends BaseLoactionActivity {
	private static final String LOG_TAG = VideoActivity.class.getSimpleName();

	private static final int DIALOG_LEGAL = 0;

	private static final int MAX_RETRIES = 5;
	private static final int BACKOFF = 4; // base of exponential backoff

	private String ytdDomain = null;
	private String assignmentId = null;
	private Uri videoUri = null;
	private String clientLoginToken = null;
	private Date dateTaken = null;
	private Authorizer authorizer = null;

	private String tags = null;
	private SharedPreferences preferences = null;
	private double currentFileSize = 0;
	private double totalBytesUploaded = 0;
	private int numberOfRetries = 0;
	private VideoUtil util;

	private ProgressDialog progressdialog;
	private TextView submitButton;
	private ProgressView project_post_viewo_progress_view;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("AAAAAA", "fk");
		CrashExceptionHandler.onCreate(this);
		title = DataStore.getInstance().get("mypost_project_name", "");
		description = title;

		overridePendingTransition(R.anim.a_bot_to_top_in, R.anim.a_nothing);
		this.setContentView(R.layout.project_post_video);

		project_post_viewo_progress_view = (ProgressView) findViewById(R.id.project_post_viewo_progress_view);

		preferences = getSharedPreferences(VideoUtil.SHARE_DATA_PREF, Context.MODE_PRIVATE);
		this.authorizer = new ClientLoginAuthorizer(this);

		Intent intent = this.getIntent();
		this.videoUri = intent.getData();

		// if (videoUri == null && YouTubeManager.USEPATH) {
		// videoUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
		// }
		if (videoUri != null) {
			Log.e("videoUri", videoUri.toString());
		}
		this.ytdDomain = getString(R.string.default_ytd_domain);

		submitButton = (TextView) findViewById(R.id.submitButton);
		progressdialog = new ProgressDialog(this);

		// getAuthTokenWithPermission(YouTubeManager.YOUTUBENAME);
		util = new VideoUtil(VideoActivity.this);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (util.isNetworkAvaible()) {
					submitButton.setVisibility(View.GONE);
					project_post_viewo_progress_view.setVisibility(View.VISIBLE);
					getAuthTokenWithPermission2(YouTubeManager.YOUTUBENAME);
				} else {
					util.showAlertError(VideoActivity.this, getResources().getString(R.string.video_internet_err), getResources().getString(R.string.video_title_err));
				}
			}
		});

		findViewById(R.id.headerbutton1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		findViewById(R.id.headerbutton2).setVisibility(View.INVISIBLE);
		ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
		getThumb(thumbnail);
	}

	private void uploadToBynal() {
		String youTubeId = YouTubeManager.getYouTubeId();
		if (youTubeId != null && !youTubeId.equals("")) {
			String movie_url = "<iframe width='100%' height='315' src='http://www.youtube.com/embed/" + youTubeId + "' frameborder='0' allowfullscreen></iframe>";
			Log.e("movie_url", movie_url);
			Map<String, String> sendDataUploadVideo = new HashMap<String, String>();
			sendDataUploadVideo.put("token", ProjectManagerFragment.token_user);
			sendDataUploadVideo.put("project_id", DataStore.getInstance().get("mypost_project_id", "0"));
			sendDataUploadVideo.put("site_name", "Youtube");

			sendDataUploadVideo.put("movie_url", movie_url);

			ICallbackAPI callbackAPI = new ICallbackAPI() {
				@Override
				public void onSuccess(String response) {
					submitButton.setVisibility(View.GONE);
					project_post_viewo_progress_view.setVisibility(View.GONE);
					try {
						JSONObject jsonObject = new JSONObject(response);
						String status = jsonObject.getString("status");
						String message = jsonObject.getString("message");

						if ("1".equals(status) || "true".equals(status)) {
							if ("".equals(message) || message == null) {
								message = getString(R.string.video_up_youtube_done);
							}

							util.showAlertError(VideoActivity.this, message, null);
						} else {
							submitButton.setVisibility(View.VISIBLE);
							util.showAlertError(VideoActivity.this, message, null);
						}
					} catch (Exception exception) {
						onError(null);
					}
				}

				@Override
				public void onError(String message) {
					submitButton.setVisibility(View.VISIBLE);
					project_post_viewo_progress_view.setVisibility(View.GONE);
					util.showAlertError(VideoActivity.this, getString(R.string.error_message_connect_server_fail), null);
				}
			};
			new APICaller(MainHomeActivity.homeActivity).callApi("/project/movieUpload", true, callbackAPI, sendDataUploadVideo);
		} else {
			submitButton.setVisibility(View.VISIBLE);
			project_post_viewo_progress_view.setVisibility(View.GONE);
			util.showAlertError(VideoActivity.this, getString(R.string.error_message_connect_server_fail), null);
		}

		// setResult(RESULT_CANCELED);
		// finish();
		// overridePendingTransition(R.anim.a_nothing,
		// R.anim.a_top_to_bot_out);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog = new Dialog(VideoActivity.this);
		dialog.setTitle("Terms of Service");
		switch (id) {
		case DIALOG_LEGAL:
			dialog.setContentView(R.layout.project_post_videolegal);

			TextView legalText = (TextView) dialog.findViewById(R.id.legal);

			legalText.setText(VideoUtil.readFile(this, R.raw.legal).toString());

			dialog.findViewById(R.id.agree).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.findViewById(R.id.notagree).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});

			break;
		}

		return dialog;
	}

	@Override
	public void onRestart() {
		super.onRestart();
		hideKeyboard(this.getCurrentFocus());
	}

	private void requestDummyFocus() {
	}

	@SuppressLint("NewApi")
	private void hideKeyboard(View currentFocusView) {
		if (currentFocusView instanceof EditText) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
			requestDummyFocus();
		}
	}

	private ProgressDialog mDialog = null;

	public void upload(Uri videoUri) {
		this.mDialog = new ProgressDialog(this);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setMessage(getString(R.string.uploading));
		mDialog.setCancelable(false);
		mDialog = null;
		if (mDialog != null) {
			mDialog.show();
		}

		// TODO
		project_post_viewo_progress_view.setCurrent(0);
		progressdialog.dismiss();

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (mDialog != null)
					mDialog.dismiss();
				String videoId = msg.getData().getString("videoId");

				if (!VideoUtil.isNullOrEmpty(videoId)) {
					currentFileSize = 0;
					totalBytesUploaded = 0;
					uploadToBynal();
				} else {
					submitButton.setVisibility(View.VISIBLE);
					project_post_viewo_progress_view.setVisibility(View.GONE);

					String error = msg.getData().getString("error");
//					if (!VideoUtil.isNullOrEmpty(error)) {
//						util.showAlertError(VideoActivity.this, error, getResources().getString(R.string.video_title_err));
//					} else {
						util.showAlertError(VideoActivity.this, getResources().getString(R.string.video_up_youtube_err), getResources().getString(R.string.video_title_err));
//					}
				}
			}

		};

		asyncUpload(videoUri, handler);
	}

	public void asyncUpload(final Uri uri, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.setData(bundle);

				String videoId = null;
				int submitCount = 0;
				try {
					while (submitCount <= MAX_RETRIES && videoId == null) {
						try {
							submitCount++;
							videoId = startUpload(uri);
							assert videoId != null;
						} catch (Internal500ResumeException e500) {
							if (submitCount < MAX_RETRIES) {
								Log.w(LOG_TAG, e500.getMessage());
								Log.d(LOG_TAG, String.format("Upload retry :%d.", submitCount));
							} else {
								Log.d(LOG_TAG, "Giving up");
								Log.e(LOG_TAG, e500.getMessage());
								throw new IOException(e500.getMessage());
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
					return;
				} catch (YouTubeAccountException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
					return;
				} catch (SAXException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					bundle.putString("error", e.getMessage());
					handler.sendMessage(msg);
				}

				bundle.putString("videoId", videoId);
				handler.sendMessage(msg);
			}
		}).start();
	}

	private String startUpload(Uri uri) throws IOException, YouTubeAccountException, SAXException, ParserConfigurationException, Internal500ResumeException {
		File file = YouTubeManager.getFileFromUri(this, uri);
		if (videoUri != null && YouTubeManager.USEPATH && file == null) {
			String path = videoUri.toString();
			if (path.startsWith("file:///")) {
				file = new File(path.replace("file:///", ""));
			}else if(!path.startsWith("content://")){
				file = new File(path);
			}
		}

		if (this.clientLoginToken == null) {
			// The stored gmail account is not linked to YouTube
			throw new YouTubeAccountException(YouTubeManager.YOUTUBENAME + " is not linked to a YouTube account.");
		}
		String uploadUrl = uploadMetaData(file.getAbsolutePath(), true);

		Log.d(LOG_TAG, "uploadUrl=" + uploadUrl);
		Log.d(LOG_TAG, String.format("Client token : %s ", this.clientLoginToken));

		this.currentFileSize = file.length();
		this.totalBytesUploaded = 0;
		this.numberOfRetries = 0;

		int uploadChunk = 1024 * 1024 * 3; // 3MB

		int start = 0;
		int end = -1;

		String videoId = null;
		double fileSize = this.currentFileSize;
		while (fileSize > 0) {
			if (fileSize - uploadChunk > 0) {
				end = start + uploadChunk - 1;
			} else {
				end = start + (int) fileSize - 1;
			}
			Log.d(LOG_TAG, String.format("start=%s end=%s total=%s", start, end, file.length()));
			try {
				videoId = gdataUpload(file, uploadUrl, start, end);
				fileSize -= uploadChunk;
				start = end + 1;
				this.numberOfRetries = 0; // clear this counter as we had a
											// succesfull upload
			} catch (IOException e) {
				Log.d(LOG_TAG, "Error during upload : " + e.getMessage());
				ResumeInfo resumeInfo = null;
				do {
					if (!shouldResume()) {
						Log.d(LOG_TAG, String.format("Giving up uploading '%s'.", uploadUrl));
						throw e;
					}
					try {
						resumeInfo = resumeFileUpload(uploadUrl);
					} catch (IOException re) {
						// ignore
						Log.d(LOG_TAG, String.format("Failed retry attempt of : %s due to: '%s'.", uploadUrl, re.getMessage()));
					}
				} while (resumeInfo == null);
				Log.d(LOG_TAG, String.format("Resuming stalled upload to: %s.", uploadUrl));
				if (resumeInfo.getVideoId() != null) { // upload actually
														// complted
														// despite the exception
					videoId = resumeInfo.getVideoId();
					Log.d(LOG_TAG, String.format("No need to resume video ID '%s'.", videoId));
					break;
				} else {
					int nextByteToUpload = resumeInfo.getNextByteToUpload();
					Log.d(LOG_TAG, String.format("Next byte to upload is '%d'.", nextByteToUpload));
					this.totalBytesUploaded = nextByteToUpload; // possibly
																// rolling back
																// the
																// previosuly
																// saved value
					fileSize = this.currentFileSize - nextByteToUpload;
					start = nextByteToUpload;
				}
			}
		}

		if (videoId != null) {
			return videoId;
		}

		return null;
	}

	public String title = "BaseCodeLibs";
	public String description = "BaseCodeLibs-desc";

	private String uploadMetaData(String filePath, boolean retry) throws IOException {
		String uploadUrl = YouTubeManager.INITIAL_UPLOAD_URL;

		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/atom+xml");
		urlConnection.setRequestProperty("Slug", filePath);
		String atomData;

		String category = YouTubeManager.DEFAULT_VIDEO_CATEGORY;
		this.tags = YouTubeManager.DEFAULT_VIDEO_TAGS;

		if (this.videoLocation == null) {
			String template = VideoUtil.readFile(this, R.raw.gdata).toString();
			atomData = String.format(template, title, description, category, this.tags);
		} else {
			String template = VideoUtil.readFile(this, R.raw.gdata_geo).toString();
			atomData = String.format(template, title, description, category, this.tags, videoLocation.getLatitude(), videoLocation.getLongitude());
		}

		OutputStreamWriter outStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
		outStreamWriter.write(atomData);
		outStreamWriter.close();

		int responseCode = urlConnection.getResponseCode();
		if (responseCode < 200 || responseCode >= 300) {
			if ((responseCode + "").startsWith("4") && retry) {
				Log.d(LOG_TAG, "retrying to fetch auth token for " + YouTubeManager.YOUTUBENAME);
				this.clientLoginToken = authorizer.getFreshAuthToken(YouTubeManager.YOUTUBENAME, clientLoginToken);
				return uploadMetaData(filePath, false);
			} else {
				throw new IOException(String.format("response code='%s' (code %d)" + " for %s", urlConnection.getResponseMessage(), responseCode, urlConnection.getURL()));
			}
		}

		return urlConnection.getHeaderField("Location");
	}

	@Override
	public void onBackPressed() {
		if (project_post_viewo_progress_view.getVisibility() == View.VISIBLE) {
			return;
		} else {
		}

		super.onBackPressed();
		overridePendingTransition(R.anim.a_nothing, R.anim.a_top_to_bot_out);
	}

	private String gdataUpload(File file, String uploadUrl, int start, int end) throws IOException {
		int chunk = end - start + 1;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		FileInputStream fileStream = new FileInputStream(file);

		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		if (isFirstRequest()) {
			Log.d(LOG_TAG, String.format("Uploaded %d bytes so far, using POST method.", (int) totalBytesUploaded));
			urlConnection.setRequestMethod("POST");
		} else {
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("X-HTTP-Method-Override", "PUT");
			Log.d(LOG_TAG, String.format("Uploaded %d bytes so far, using POST with X-HTTP-Method-Override PUT method.", (int) totalBytesUploaded));
		}
		urlConnection.setDoOutput(true);
		urlConnection.setFixedLengthStreamingMode(chunk);
		urlConnection.setRequestProperty("Content-Type", "video/3gpp");
		urlConnection.setRequestProperty("Content-Range", String.format("bytes %d-%d/%d", start, end, file.length()));
		Log.d(LOG_TAG, urlConnection.getRequestProperty("Content-Range"));

		OutputStream outStreamWriter = urlConnection.getOutputStream();

		fileStream.skip(start);

		int bytesRead;
		int totalRead = 0;
		while ((bytesRead = fileStream.read(buffer, 0, bufferSize)) != -1) {
			outStreamWriter.write(buffer, 0, bytesRead);
			totalRead += bytesRead;
			this.totalBytesUploaded += bytesRead;

			final double percent = (totalBytesUploaded / currentFileSize) * 99;
			if (mDialog != null)
				mDialog.setProgress((int) percent);
			project_post_viewo_progress_view.setCurrent((int) percent);

			if (totalRead == (end - start + 1)) {
				break;
			}
		}

		outStreamWriter.close();
		int responseCode = urlConnection.getResponseCode();

		try {
			if (responseCode == 201) {
				String videoId = YouTubeManager.parseVideoId(urlConnection.getInputStream());
				preferences.edit().putString(VideoUtil.VIDEO_ID, videoId).commit();
				String latLng = null;
				if (this.videoLocation != null) {
					latLng = String.format("lat=%f lng=%f", this.videoLocation.getLatitude(), this.videoLocation.getLongitude());
				}

				YouTubeManager.submitToYtdDomain(this.ytdDomain, this.assignmentId, videoId, YouTubeManager.YOUTUBENAME, VideoActivity.this.clientLoginToken, title, description, this.dateTaken,
						latLng, this.tags);
				if (mDialog != null)
					mDialog.setProgress(100);
				return videoId;
			} else if (responseCode == 200) {
				Set<String> keySet = urlConnection.getHeaderFields().keySet();
				String keys = urlConnection.getHeaderFields().keySet().toString();
				Log.d(LOG_TAG, String.format("Headers keys %s.", keys));
				for (String key : keySet) {
					Log.d(LOG_TAG, String.format("Header key %s value %s.", key, urlConnection.getHeaderField(key)));
				}
				Log.w(LOG_TAG, "Received 200 response during resumable uploading");
				throw new IOException(String.format("Unexpected response code : responseCode=%d responseMessage=%s", responseCode, urlConnection.getResponseMessage()));
			} else {
				if ((responseCode + "").startsWith("5")) {
					String error = String.format("responseCode=%d responseMessage=%s", responseCode, urlConnection.getResponseMessage());
					Log.w(LOG_TAG, error);
					throw new IOException(error);
				} else if (responseCode == 308) {
					Log.d(LOG_TAG, String.format("responseCode=%d responseMessage=%s", responseCode, urlConnection.getResponseMessage()));
				} else {
					Log.w(LOG_TAG, String.format("Unexpected return code : %d %s while uploading :%s", responseCode, urlConnection.getResponseMessage(), uploadUrl));
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isFirstRequest() {
		return totalBytesUploaded == 0;
	}

	private ResumeInfo resumeFileUpload(String uploadUrl) throws IOException, ParserConfigurationException, SAXException, Internal500ResumeException {
		HttpURLConnection urlConnection = getGDataUrlConnection(uploadUrl);
		urlConnection.setRequestProperty("Content-Range", "bytes */*");
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("X-HTTP-Method-Override", "PUT");
		urlConnection.setFixedLengthStreamingMode(0);

		HttpURLConnection.setFollowRedirects(false);

		urlConnection.connect();
		int responseCode = urlConnection.getResponseCode();

		if (responseCode >= 300 && responseCode < 400) {
			int nextByteToUpload;
			String range = urlConnection.getHeaderField("Range");
			if (range == null) {
				Log.d(LOG_TAG, String.format("PUT to %s did not return 'Range' header.", uploadUrl));
				nextByteToUpload = 0;
			} else {
				Log.d(LOG_TAG, String.format("Range header is '%s'.", range));
				String[] parts = range.split("-");
				if (parts.length > 1) {
					nextByteToUpload = Integer.parseInt(parts[1]) + 1;
				} else {
					nextByteToUpload = 0;
				}
			}
			return new ResumeInfo(nextByteToUpload);
		} else if (responseCode >= 200 && responseCode < 300) {
			return new ResumeInfo(YouTubeManager.parseVideoId(urlConnection.getInputStream()));
		} else if (responseCode == 500) {
			throw new Internal500ResumeException(String.format("Unexpected response for PUT to %s: %s " + "(code %d)", uploadUrl, urlConnection.getResponseMessage(), responseCode));
		} else {
			throw new IOException(String.format("Unexpected response for PUT to %s: %s " + "(code %d)", uploadUrl, urlConnection.getResponseMessage(), responseCode));
		}
	}

	private boolean shouldResume() {
		this.numberOfRetries++;
		if (this.numberOfRetries > MAX_RETRIES) {
			return false;
		}
		try {
			int sleepSeconds = (int) Math.pow(BACKOFF, this.numberOfRetries);
			Log.d(LOG_TAG, String.format("Zzzzz for : %d sec.", sleepSeconds));
			Thread.currentThread().sleep(sleepSeconds * 1000);
			Log.d(LOG_TAG, String.format("Zzzzz for : %d sec done.", sleepSeconds));
		} catch (InterruptedException se) {
			se.printStackTrace();
			return false;
		}
		return true;
	}

	private HttpURLConnection getGDataUrlConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", String.format("GoogleLogin auth=\"%s\"", clientLoginToken));
		connection.setRequestProperty("GData-Version", "2");
		connection.setRequestProperty("X-GData-Client", this.getString(R.string.client_id));
		connection.setRequestProperty("X-GData-Key", String.format("key=%s", this.getString(R.string.dev_key)));
		return connection;
	}

	private void getAuthTokenWithPermission2(String accountName) {
		project_post_viewo_progress_view.setVisibility(View.VISIBLE);
		this.authorizer.fetchAuthToken(accountName, this, new AuthorizationListener<String>() {
			@Override
			public void onCanceled() {
			}

			@Override
			public void onError(Exception e) {
				submitButton.setVisibility(View.VISIBLE);
				project_post_viewo_progress_view.setVisibility(View.GONE);
				util.showAlertError(VideoActivity.this, e.getMessage(), getResources().getString(R.string.video_title_err));
			}

			@Override
			public void onSuccess(String result) {
				VideoActivity.this.clientLoginToken = result;
				upload(VideoActivity.this.videoUri);
			}
		});
	}

	private void getThumb(final ImageView thumbnail) {
		try {
			getVideoLocation();
		} catch (Exception exception) {

		}

		new AsyncTask<String, String, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				try {
					if (videoUri != null && YouTubeManager.USEPATH) {
						String path = videoUri.toString();
						if (path.startsWith("file:///")) {
							Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path.replace("file:///", ""), MediaStore.Images.Thumbnails.MINI_KIND);
							dateTaken = new Date(System.currentTimeMillis());
							return thumb;
						}else if(!path.startsWith("content://")){
							Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path , MediaStore.Images.Thumbnails.MINI_KIND);
							dateTaken = new Date(System.currentTimeMillis());
							return thumb;
						}
					}

					Cursor cursor = getContentResolver().query(videoUri, null, null, null, null);
					if (cursor.getCount() == 0) {

					} else {
						if (cursor.moveToFirst()) {
							long id = cursor.getLong(cursor.getColumnIndex(Video.VideoColumns._ID));
							dateTaken = new Date(cursor.getLong(cursor.getColumnIndex(Video.VideoColumns.DATE_TAKEN)));

							SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa");
							Configuration userConfig = new Configuration();
							Settings.System.getConfiguration(getContentResolver(), userConfig);
							Calendar cal = Calendar.getInstance(userConfig.locale);
							TimeZone tz = cal.getTimeZone();

							dateFormat.setTimeZone(tz);

							ContentResolver crThumb = getContentResolver();
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 1;
							return MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);

						}
					}
				} catch (Exception exception) {

				}

				return null;
			}

			protected void onPostExecute(Bitmap result) {

				if (result == null) {
					Toast.makeText(VideoActivity.this, getString(R.string.error_load_uri_video), Toast.LENGTH_LONG).show();
					finish();
					overridePendingTransition(R.anim.a_nothing, R.anim.a_top_to_bot_out);
				} else {
					thumbnail.setImageBitmap(result);
				}
			};
		}.execute("");

	}
}