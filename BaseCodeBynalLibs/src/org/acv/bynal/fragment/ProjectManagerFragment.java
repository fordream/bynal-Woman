package org.acv.bynal.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.camera.uploadyoutube.VideoUtil;
import org.acv.bynal.camera.uploadyoutube.YouTubeManager;
import org.acv.bynal.dialog.MenuRight2Dialog;
import org.acv.bynal.dialog.ProjectManagerCameraMenuDialogDialog;
import org.acv.bynal.dialog.ProjectManagerDeliveryDialog;
import org.acv.bynal.dialog.ProjectManagerSupportDialog;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectListItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.DataStore;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseFragment;

public class ProjectManagerFragment extends BaseFragment {
	static Map<String, String> sendData = new HashMap<String, String>();
	static Map<String, String> sendDataUploadVideo = new HashMap<String, String>();
	static ICallbackAPI callbackAPI;
	static ICallbackAPI callbackAPIUploadVideo;
	ICallbackAPI callbackAPIDelete;
	Context context = null;
	public static String token_user;
	static ListView projectlistview;

	private static int total_pages = 1;
	private static int currentPage = 1;
	private static boolean isLoadMore = false;
	static TextView txtErr;
	static List<BaseItem> baseItems = new ArrayList<BaseItem>();

	// fix
	public static String typeProject = "0";
	public static String project_id = "0";
	static View views;
	private RelativeLayout viewdialog;
	Animation fadeOut2, fadeIn2;
	private boolean showDialogshare = false;

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		total_pages = 1;
		currentPage = 1;
		isLoadMore = false;
		typeProject = "0";
		project_id = "0";
		showDialogshare = false;
		loadPage();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void loadPage() {
		AccountDB accountDB = new AccountDB(getActivity());
		token_user = accountDB.getToken();
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						// "is_login":true
						if (jsonObject.getBoolean("is_login") == true) {
							total_pages = jsonObject.getInt("total_pages");
							currentPage = jsonObject.getInt("page");
							if (total_pages > 1 && total_pages > currentPage) {
								isLoadMore = true;
							}
							JSONArray array = jsonObject.getJSONArray("array_data");
							// Log.e("array_data_length","length==========::" +
							// array.length());
							for (int i = 0; i < array.length(); i++) {
								array.getJSONObject(i).put("key_value", i);
								baseItems.add(new BaseItem(array.getJSONObject(i)));
							}
						} else {
							// token timeout
							CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_session_login), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// getActivity().setResult(ByUtils.RESPONSE_RELEASETOKEN);
									// getActivity().finish();
									relaseTooken();
								}
							});
						}

					}
					baseAdapter.clear();
					baseAdapter.addAllItems(baseItems);
					baseAdapter.notifyDataSetChanged();
					projectlistview.setVisibility(View.VISIBLE);
					// Animation animation =
					// AnimationUtils.loadAnimation(context, R.anim.fadein);
					// projectlistview.startAnimation(animation);
					// BynalAnimationUtils.faceoutView(getActivity().getBaseContext(),projectlistview,
					// null);
				} catch (JSONException e) {
					if (baseItems.size() == 0) {
						int type = Integer.parseInt(typeProject);
						int Err_message = R.string.error_pm_favorite_return_null;
						switch (type) {
						case 0:
							Err_message = R.string.error_pm_favorite_return_null;
							break;
						case 1:
							Err_message = R.string.error_pm_angel_return_null;
							break;
						case 2:
							Err_message = R.string.error_pm_post_return_null;
							break;
						case 3:
							Err_message = R.string.error_pm_all_return_null;
							break;
						}
						txtErr.setVisibility(View.VISIBLE);
						setText(getResources().getString(Err_message), R.id.pm_err_message);
					}
					Log.e("PM", "err::" + e.getMessage());
				}
			}

			@Override
			public void onError(String message) {
				// Toast.makeText(getActivity(),getResources().getString(R.string.error_message_check_network),
				// Toast.LENGTH_LONG).show();
				CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
			}
		};

		callbackAPIUploadVideo = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					// CommonAndroid.showDialog(getActivity(),
					// jsonObject.toString(), null);
					if (jsonObject.getString("status").equals("true")) {
						// Log.e("aaaaaa","video========OKOKOK");
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.video_up_server_done), null);
					}
				} catch (JSONException e) {
					// CommonAndroid.showDialog(getActivity(), e.toString(),
					// null);
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				// CommonAndroid.showDialog(getActivity(), message, null);
				CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
			}
		};
		CallAPIProjectList();
	}

	// org.acv.bynal.camera.check.CommonCameraPreview cameraPreview;

	@Override
	public void setUpFragment(View view) {
		// cameraPreview = (CommonCameraPreview)
		// view.findViewById(R.id.camera_check);
		total_pages = 1;
		currentPage = 1;
		isLoadMore = false;
		typeProject = "0";
		project_id = "0";
		showDialogshare = false;

		viewdialog = (RelativeLayout) view.findViewById(R.id.camera_block);
		view.findViewById(R.id.camera_block).setOnClickListener(this);
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		txtErr = (TextView) view.findViewById(R.id.pm_err_message);
		projectlistview = (ListView) view.findViewById(R.id.listview_project_manager);
		views = view;
		projectManagerPage = this;
		fadeOut2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
		fadeOut2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showDialogshare = true;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				showDialogshare = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
		fadeIn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
		fadeIn2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showDialogshare = false;
				// viewdialog.setVisibility(View.GONE);
			}

			@SuppressLint("NewApi")
			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				showDialogshare = false;
				viewdialog.setVisibility(View.GONE);
				viewdialog.setTop(2800);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		total_pages = 1;
		currentPage = 1;
		isLoadMore = false;
		typeProject = "0";
		project_id = "0";
		showDialogshare = false;
		baseItems.clear();
	}

	private static BaseAdapter baseAdapter;

	@Override
	public int layoytResurce() {
		return R.layout.projectmanager;
	}

	@Override
	public void onClick(View v) {
		int type = v.getId();
		switch (type) {
		case R.id.camera_block:
			if (showDialogshare) {
				viewdialog.startAnimation(fadeIn2);
			}
			break;
		}
	}

	@Override
	public HeaderOption getHeaderOption() {
		/*
		 * TYPEHEADER type = TYPEHEADER.NORMAL; HeaderOption headerOption = new
		 * HeaderOption(getActivity(), type) {
		 * 
		 * @Override public void onClickButtonLeft() { typeProject = "0"; //
		 * getActivity().setResult(ByUtils.REQUEST_HOME); //
		 * getActivity().finish(); gotoHome(); }
		 * 
		 * @Override public void onClickButtonRight() { ((BaseActivity)
		 * getActivity()).showMenuLeft(true); ((BaseActivity)
		 * getActivity()).showMenuRight(true); } };
		 * headerOption.setShowButtonLeft(true);
		 * headerOption.setShowButtonRight(true);
		 * headerOption.setResHeader(R.string.header_project_favorite);
		 * headerOption.setResDrawableLeft(R.drawable.home_xml);
		 * headerOption.setResDrawableRight(R.drawable.menu_xml); return
		 * headerOption;
		 */

		TYPEHEADER type = TYPEHEADER.NORMAL;
		HeaderOption headerOption = new HeaderOption(getActivity(), type) {
			@Override
			public void onClickButtonLeft() {
				BaseTabActivity.senBroadCastHeaderMenu(getActivity(), "menu");
			}

			@Override
			public void onClickButtonRight() {
				new MenuRight2Dialog(getActivity(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
			}
			
			@Override
			public void onClickButtonRight2() {
				((BaseTabActivity) (MainHomeActivity.homeActivity).getParent()).refreshMenuAndgotoHome();
			}
		};
		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setShowButtonRight2(true);
		headerOption.setResHeader(R.string.header_project_favorite);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.menuright_xml);
		headerOption.setResDrawableRight2(R.drawable.home_xml);
		return headerOption;
	}

	public static ProjectManagerFragment projectManagerPage;

	public static void CallAPIProjectList() {
		// projectlistview.setVisibility(View.INVISIBLE);
		int txtHeader = 0;
		int type = Integer.parseInt(typeProject);
		switch (type) {
		case 0:
			txtHeader = R.string.header_project_favorite;
			break;
		case 1:
			txtHeader = R.string.header_project_support;
			break;
		case 2:
			txtHeader = R.string.header_project_post;
			break;
		case 3:
			txtHeader = R.string.header_project_all;
			break;
		}
		// ((BaseActivity)getActivity()).setTextheader(txtHeader);
		HeaderView headerView = (HeaderView) views.findViewById(R.id.headerView1);
		headerView.setText(txtHeader);
		baseAdapter = new BaseAdapter(MainHomeActivity.homeActivity, new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectListItemView ProjectManagerItemView = new ProjectListItemView(context);
				ProjectManagerItemView.addProjectManagerFragment(projectManagerPage);
				return ProjectManagerItemView;
			}
		};
		projectlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BaseItem baseItem = (BaseItem) parent.getItemAtPosition(position);
				String projectID = baseItem.getString("id");
				String public_flg = "0";
				try {
					public_flg = baseItem.getString("public_flg");
					if (public_flg.equalsIgnoreCase("1")) {
						Intent PageDetail = new Intent(MainHomeActivity.homeActivity, ProjectDetailActivity.class);
						PageDetail.putExtra("projectIdDetail", Integer.parseInt(projectID));
						(MainHomeActivity.homeActivity).startActivityForResult(PageDetail, ByUtils.REQUEST_HOME);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Intent PageDetail = new Intent(MainHomeActivity.homeActivity, ProjectDetailActivity.class);
					PageDetail.putExtra("projectIdDetail", Integer.parseInt(projectID));
					(MainHomeActivity.homeActivity).startActivityForResult(PageDetail, ByUtils.REQUEST_HOME);
				}
				// CommonAndroid.showDialog(getActivity(), "projectID::" +
				// public_flg,null);

			}
		});

		projectlistview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (totalItemCount > 0 && isLoadMore) {
					if (totalItemCount - 1 <= firstVisibleItem + visibleItemCount) {
						// CommonAndroid.showDialog(getActivity(), "load more:"
						// + (currentPage + 1), null);
						sendData = new HashMap<String, String>();
						sendData.put("type", typeProject);
						sendData.put("token", token_user);
						sendData.put("page", "" + (currentPage + 1));
						new APICaller(MainHomeActivity.homeActivity).callApi("/project/list", true, callbackAPI, sendData);
						isLoadMore = false;
					}
				}

			}

		});

		// setText("",R.id.pm_err_message);
		txtErr.setVisibility(View.GONE);
		projectlistview.setVisibility(View.INVISIBLE);
		baseItems.clear();
		total_pages = 1;
		currentPage = 1;
		projectlistview.setAdapter(baseAdapter);
		sendData = new HashMap<String, String>();
		sendData.put("type", typeProject);
		sendData.put("token", token_user);
		new APICaller(MainHomeActivity.homeActivity).callApi("/project/list", true, callbackAPI, sendData);

	}

	public void actionProjectPost(View v, final BaseItem data) {
		String id = data.getString("id");
		project_id = id;
		DataStore.getInstance().save("mypost_project_id", project_id);
		String project_name = data.getString("title");
		DataStore.getInstance().save("mypost_project_name", project_name + "");
		Log.e("project_id", project_id + "");
		int type = v.getId();
		int action = 0;
		Log.e("onclick==", "type==" + type);
		switch (type) {
		case R.id.pm_button_video:
			action = 0;
			final String[] items = new String[] { getResources().getString(R.string.project_changevideo_from_camera), getResources().getString(R.string.project_changevideo_from_gallery) };
			showController();
			break;
		case R.id.pm_button_preview:
			action = 1;
			Intent PageDetail = new Intent(getActivity(), ProjectDetailActivity.class);
			PageDetail.putExtra("projectIdDetail", Integer.parseInt(id));
			PageDetail.putExtra("projectIdDetailPreview", "1");
			getActivity().startActivityForResult(PageDetail, ByUtils.REQUEST_HOME);
			break;
		case R.id.pm_button_support:
			action = 2;
			// ((BaseActivity)getActivity()).changeFragemt(new
			// ProjectManagerSupportFragment());
			new ProjectManagerSupportDialog(getActivity(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case R.id.pm_button_delivery:
			action = 3;
			// ((BaseActivity)getActivity()).changeFragemt(new
			// ProjectManagerDeliveryFragment());
			new ProjectManagerDeliveryDialog(getActivity(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case R.id.pm_button_delete:
			action = 4;
			callbackAPIDelete = new ICallbackAPI() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.getString("status").equals("1")) {
							CallAPIProjectList();
						} else {
							CommonAndroid.showDialog(getActivity(), jsonObject.getString("message"), null);
						}
					} catch (JSONException e) {
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
					}
				}

				@Override
				public void onError(String message) {
					// CommonAndroid.showDialog(getActivity(), message + "",
					// null);
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
				}
			};
			CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.project_post_delete_confirm), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// delete project
					sendData = new HashMap<String, String>();
					sendData.put("project_id", project_id);
					sendData.put("token", token_user);
					new APICaller(getActivity()).callApi("/project/delete", true, callbackAPIDelete, sendData);
				}
			}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Cancel
				}
			});
			break;

		}
		// CommonAndroid.showDialog(getActivity(),action +
		// "==action::projectID==" + id, null);
	}

	public static void CallAPIUploadVideo(String youtubeID) {
		String movie_url = "<iframe width='100%' height='315' src='http://www.youtube.com/embed/" + youtubeID + "' frameborder='0' allowfullscreen></iframe>";
		sendDataUploadVideo = new HashMap<String, String>();
		sendDataUploadVideo.put("token", token_user);
		sendDataUploadVideo.put("project_id", project_id);
		sendDataUploadVideo.put("site_name", "Youtube");
		sendDataUploadVideo.put("movie_url", movie_url);
		new APICaller(MainHomeActivity.homeActivity).callApi("/project/movieUpload", true, callbackAPIUploadVideo, sendDataUploadVideo);
	}

	public void showController() {

		new ProjectManagerCameraMenuDialogDialog(getActivity(), new DialogInterface.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					VideoUtil util = new VideoUtil(getActivity());
					AccountDB profile_imginfo = new AccountDB(MainHomeActivity.homeActivity);
					profile_imginfo.save("video_project_post", "");
					if (util.checkCameraAvaible()) {
						// TODO
						Camera camera = null;
						try {
							camera = Camera.open(0);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							camera = Camera.open();
							e1.printStackTrace();
						}
						// if (cameraPreview.canOpen()) {
						if (camera != null) {
							// release camera
							try {
								camera.startPreview();
							} catch (Exception exception) {

							}
							try {
								camera.release();
							} catch (Exception exception) {

							}
							Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
							if (YouTubeManager.USEPATH) {
								File pathFileVideo = YouTubeManager.getFilePath();
								i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pathFileVideo));
								profile_imginfo = new AccountDB(MainHomeActivity.homeActivity);
//								Uri fileUri = Uri.fromFile(pathFileVideo);
//								profile_imginfo.save("video_project_post", fileUri.toString());
								
								//TODO:test
								ContentValues value =new ContentValues();
								value.put(MediaStore.Video.Media.TITLE,"VideoBynal");
								value.put(MediaStore.Video.Media.MIME_TYPE,"video/mp4");
								String videoFilePath = pathFileVideo.getAbsolutePath();
								value.put(MediaStore.Video.Media.DATA, videoFilePath);
								Uri photoUri = MainHomeActivity.homeActivity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, value);
								i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
								i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
							} else {
								SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
								String filename = timeStampFormat.format(new Date());
								ContentValues values = new ContentValues();
								values.put(Media.TITLE, filename);
								values.put(Media.DESCRIPTION, "Image from Bynal");
								Uri photoUri = MainHomeActivity.homeActivity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

								i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
								i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
								i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
							}
							try {
								i.putExtra("return-data", true);
								MainHomeActivity.homeActivity.startActivityForResult(i, MainHomeActivity.CAPTURE_RETURN);
							} catch (ActivityNotFoundException e) {
								e.printStackTrace();
							}

						} else {
							CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_camera), null);
						}
					} else {
						util.showAlertError(getResources().getString(R.string.video_camera_err), getResources().getString(R.string.video_title_err));
					}
				} else if (which == 1) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_PICK);
					intent.setType("video/*");

					List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
					if (list.size() <= 0) {
						// Log.d(LOG_TAG,
						// "no video picker intent on this hardware");
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.project_novideo_gallery), null);
						return;
					}

					MainHomeActivity.homeActivity.startActivityForResult(intent, MainHomeActivity.GALLERY_RETURN);
				}
			}
		}).show();

		// viewdialog.setVisibility(View.VISIBLE);
		// viewdialog.startAnimation(fadeOut2);
		//
		// View view = getView().findViewById(R.id.camera_block_content);
		// RelativeLayout.LayoutParams layoutParams =
		// (RelativeLayout.LayoutParams) view.getLayoutParams();
		//
		// view.setLayoutParams(layoutParams);
		//
		// getView().findViewById(R.id.camera001).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// viewdialog.startAnimation(fadeIn2);
		// VideoUtil util = new VideoUtil(getActivity());
		// AccountDB profile_imginfo = new
		// AccountDB(MainHomeActivity.homeActivity);
		// profile_imginfo.save("video_project_post", "");
		// if (util.checkCameraAvaible()) {
		// // TODO
		// Camera camera = Camera.open();
		// // if (cameraPreview.canOpen()) {
		// if (camera != null) {
		// // release camera
		// try {
		// camera.stopPreview();
		// } catch (Exception exception) {
		//
		// }
		// try {
		// camera.release();
		// } catch (Exception exception) {
		//
		// }
		// Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// if (YouTubeManager.USEPATH) {
		// File pathFileVideo = YouTubeManager.getFilePath();
		// i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pathFileVideo));
		// profile_imginfo = new AccountDB(MainHomeActivity.homeActivity);
		// Uri fileUri = Uri.fromFile(pathFileVideo);
		// profile_imginfo.save("video_project_post", fileUri.toString());
		// } else {
		// SimpleDateFormat timeStampFormat = new
		// SimpleDateFormat("yyyyMMddHHmmssSS");
		// String filename = timeStampFormat.format(new Date());
		// ContentValues values = new ContentValues();
		// values.put(Media.TITLE, filename);
		// values.put(Media.DESCRIPTION, "Image from Bynal");
		// Uri photoUri =
		// MainHomeActivity.homeActivity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
		// values);
		//
		// i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		// }
		// try {
		// i.putExtra("return-data", true);
		// MainHomeActivity.homeActivity.startActivityForResult(i,
		// MainHomeActivity.CAPTURE_RETURN);
		// } catch (ActivityNotFoundException e) {
		// e.printStackTrace();
		// }
		//
		// } else {
		// CommonAndroid.showDialog(getActivity(),
		// getActivity().getString(R.string.error_camera), null);
		// }
		// } else {
		// util.showAlertError(getResources().getString(R.string.video_camera_err),
		// getResources().getString(R.string.video_title_err));
		// }
		// }
		// });
		//
		// getView().findViewById(R.id.camera002).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// viewdialog.startAnimation(fadeIn2);
		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_PICK);
		// intent.setType("video/*");
		//
		// List<ResolveInfo> list =
		// getActivity().getPackageManager().queryIntentActivities(intent,
		// PackageManager.MATCH_DEFAULT_ONLY);
		// if (list.size() <= 0) {
		// // Log.d(LOG_TAG,
		// // "no video picker intent on this hardware");
		// CommonAndroid.showDialog(getActivity(),
		// getResources().getString(R.string.project_novideo_gallery), null);
		// return;
		// }
		//
		// MainHomeActivity.homeActivity.startActivityForResult(intent,
		// MainHomeActivity.GALLERY_RETURN);
		// }
		// });

	}
}