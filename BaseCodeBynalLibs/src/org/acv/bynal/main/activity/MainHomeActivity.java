package org.acv.bynal.main.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.camera.CropActivity;
import org.acv.bynal.camera.VideoActivity;
import org.acv.bynal.camera.uploadyoutube.VideoUtil;
import org.acv.bynal.dialog.RegisterFacebookTwitterDialog;
import org.acv.bynal.fragment.AboutFragment;
import org.acv.bynal.fragment.BlogFragment;
import org.acv.bynal.fragment.ConactUsFragment;
import org.acv.bynal.fragment.HelpFragment;
import org.acv.bynal.fragment.MessageFragment;
import org.acv.bynal.fragment.NewAndEventFragment;
import org.acv.bynal.fragment.ProfileFragment;
import org.acv.bynal.fragment.ProjectManagerFragment;
import org.acv.bynal.fragment.X1Fragment;
import org.acv.bynal.fragment.X2Fragment;
import org.acv.bynal.fragment.X3Fragment;

import a.com.acv.bynal.v3.V3HomeFragment;
import a.com.acv.bynal.v3.fragment.V3LoginGFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.RadioButton;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.MBaseActivity;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.comunicate.FacebookUtils;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseFragment;
import com.facebook.model.GraphUser;
import com.org.social.twitter.TwitterJS;

public class MainHomeActivity extends MBaseActivity {
	public static final int HOME = 1000;
	public static final int LOGIN = 1001;
	public static final int MESSAGE = 1002;
	public static final int MORE = 1003;
	public static final int CONTACTUS = 1004;
	public static final int ABOUT = 1005;
	public static final int HELP = 1006;
	public static final int X1 = 1007;
	public static final int X2 = 1008;
	public static final int X3 = 1009;
	public static final int NEWANDALERTS = 1010;
	public static final int PROFILE = 1011;
	public static final int PROJECTMABAGER = 1012;
	public static final int BLOG = 1013;
	public static MainHomeActivity homeActivity;
	public static Bitmap mBitmap;

	public static Bitmap getBimap(View child) {
		try {
			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache(true);
			Bitmap bm = Bitmap.createBitmap(child.getDrawingCache());
			child.setDrawingCacheEnabled(false);

			return bm;
		} catch (Exception exception) {
			return null;
		}
	}

	public static void saveBimap(View child) {
		try {
			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache(true);
			Bitmap bm = Bitmap.createBitmap(child.getDrawingCache());
			child.setDrawingCacheEnabled(false);
			mBitmap = bm;
		} catch (Exception exception) {

		} catch (OutOfMemoryError out){
			
		}
	}

	public static void saveBimap() {
		try {
			View child = homeActivity.findViewById(R.id.mainhomeframe);
			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache(true);
			child.buildDrawingCache(true);
			Bitmap bm = Bitmap.createBitmap(child.getDrawingCache());
			child.setDrawingCacheEnabled(false);
			mBitmap = bm;
		} catch (Exception exception) {

		} catch (OutOfMemoryError out){
			
		}
	}

	public static Bitmap getDrawingCache() {
		try {
			View child = homeActivity.findViewById(R.id.mainhomeframe);

			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache(true);
			File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsoluteFile().getAbsolutePath() + "/tem.png");
			FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
			child.getDrawingCache(true).compress(CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.close();
			child.setDrawingCacheEnabled(false);
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			mBitmap = bitmap;
			return bitmap;

		} catch (Exception exception) {
			return null;
		}
	}

	// public static Bitmap loadBitmapFromView(View v) {
	// Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
	// v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
	// Canvas c = new Canvas(b);
	// v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	// v.draw(c);
	// return b;
	// }

	public static boolean isNeedUseAnimation(Context data) {
		SharedPreferences preferences = data.getSharedPreferences("1", 0);
		return preferences.getBoolean("1", false);
	}

	public static void setNeedUseAnimation(Context data, boolean check) {
		SharedPreferences preferences = data.getSharedPreferences("1", 0);
		preferences.edit().putBoolean("1", check).commit();
	}

	/**
	 * ORTHER
	 */
	// fix
	public static final int PICK_FROM_CAMERA = 3001;
	public static final int CROP_FROM_CAMERA = 3002;
	public static final int PICK_FROM_FILE = 3003;
	public static Uri mImageCaptureUri;
	// public static byte[] byteArrayImgProfile;
	// public static String uploadImg = "123";
	public static Bitmap croppedImage;

	// TODO:upload video
	public static String typeProject = "0";
	public static String project_id = "0";
	public static final int CAPTURE_RETURN = 4001;
	public static final int GALLERY_RETURN = 4002;
	public static final int SUBMIT_RETURN = 4003;
	private String ytdDomain = null;
	private String assignmentId = null;
	private TextView domainHeader = null;
	public static final String YTD_DOMAIN = "ytd_domain";
	public static final String ASSIGNMENT_ID = "assignment_id";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("AAAAAAAAAA", requestCode + "==link==" + resultCode);
		facebookUtils.onActivityResult(requestCode, resultCode, data);
		boolean checkGetDataIntent = false;
		try {
			Bitmap photo_check = (Bitmap) data.getExtras().get("data");
			checkGetDataIntent = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			checkGetDataIntent = false;
		}

		try {
			if (requestCode == PICK_FROM_CAMERA) {
				if (checkGetDataIntent) {
					Bitmap photo = (Bitmap) data.getExtras().get("data");
					if (photo != null) {
						Uri uri = saveBitmaptoUri(photo);
						Intent PageCrop = new Intent(this, CropActivity.class);
						PageCrop.putExtra("profileImgUri", uri.toString());
						startActivityForResult(PageCrop, CROP_FROM_CAMERA);
					}
				} else {
					AccountDB support_info = new AccountDB(this);
					String picturePath_tmp = support_info.getData("img_profile");
					String picturePath = picturePath_tmp.substring("file://".length());
					Bitmap photo = getScaledBitmap(picturePath, 200, 200);
					Log.e("AAAAAAAAAA", "link" + picturePath);
					Uri uri = saveBitmaptoUri(photo);
					Intent PageCrop = new Intent(this, CropActivity.class);
					PageCrop.putExtra("profileImgUri", uri.toString());
					startActivityForResult(PageCrop, CROP_FROM_CAMERA);
				}

			} else if (requestCode == PICK_FROM_FILE) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				Bitmap photo = getScaledBitmap(picturePath, 200, 200);
				cursor.close();
				Uri uri = saveBitmaptoUri(photo);
				Intent PageCrop = new Intent(this, CropActivity.class);
				PageCrop.putExtra("profileImgUri", uri.toString());
				startActivityForResult(PageCrop, CROP_FROM_CAMERA);
			} else if ((requestCode == CAPTURE_RETURN) || (requestCode == GALLERY_RETURN)) {
				Intent intent = new Intent(this, VideoActivity.class);
				try{
					String check = data.getData().toString();
					intent.setData(data.getData());
					Log.e("video_data", "data video_1::" + check);
					startActivityForResult(intent, SUBMIT_RETURN);
				}catch(Exception e){
					AccountDB video_info = new AccountDB(this);
					String video_tmp = video_info.getData("video_project_post");
					if( !"".equals(video_tmp)){
						String video_tmpPath = video_tmp.substring("file:///".length());
						Uri UriVideo = Uri.parse(new File(video_tmpPath).toString());
						Log.e("video_data", "data video_2::" + UriVideo.toString());
						intent.setData(UriVideo);
						video_info.save("video_project_post", "");
						Bitmap thumb = ThumbnailUtils.createVideoThumbnail(video_tmpPath.replace("file:///", ""), MediaStore.Images.Thumbnails.MINI_KIND);
						if(thumb != null){
							startActivityForResult(intent, SUBMIT_RETURN);
						}
						
					}
					
				}
				
//				intent.putExtra(YTD_DOMAIN, ytdDomain);
//				if (!VideoUtil.isNullOrEmpty(assignmentId)) {
//					intent.putExtra(ASSIGNMENT_ID, assignmentId);
//				}
				
			} else if (requestCode == SUBMIT_RETURN) {
				String data_upload = (VideoUtil.jsonData).toString();
				Log.e("upload", "===========video::" + data_upload);
				// Toast.makeText(this, "upload done!",
				// Toast.LENGTH_LONG).show();
			} else if (resultCode == ByUtils.REQUEST_HOME && requestCode == ByUtils.REQUEST_HOME) {
				((BaseTabActivity) (MainHomeActivity.homeActivity).getParent()).refreshMenuAndgotoHome();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private BaseFragment rFragment;

	public void changeFragemt(BaseFragment rFragment) {
		this.rFragment = rFragment;
		super.changeFragemt(R.id.mainhomeframe, rFragment);
	}

	@Override
	protected void onResume() {
		super.onResume();
		homeActivity = this;
		if (isNeedUseAnimation(this)) {
			// BynalAnimationUtils.openActivityScale(this,
			// findViewById(R.id.mainhomeframe), rFragment == null ? null :
			// rFragment.getAnimationAction());
			openFragmentListener(findViewById(R.id.mainhomeframe), rFragment == null ? null : rFragment.getAnimationAction());
			setNeedUseAnimation(this, false);
		}
	}

	@Override
	public void onBackPressed() {
		getParent().onBackPressed();
		// super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		facebookUtils = new FacebookUtils(this) {
			@Override
			public void loginFaceSucess(final GraphUser user) {
				String id = user.getId();
				APICaller apiCaller = new APICaller(MainHomeActivity.this);
				Map<String, String> sendData = new HashMap<String, String>();
				sendData.put("type", "2");
				sendData.put("id", id);

				ICallbackAPI callbackAPI = new ICallbackAPI() {
					@Override
					public void onSuccess(String response) {
						BaseItem baseItem = new BaseItem(response);
						if (baseItem.getString("status") == null) {
							onError("Can not connect to server");
						} else if ("1".equals(baseItem.getString("status"))) {
							AccountDB accountDB = new AccountDB(MainHomeActivity.this);
							accountDB.save(response);
							GcmBroadcastReceiver.register(MainHomeActivity.this);
							((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
						} else {
							RegisterFacebookTwitterDialog dialog = new RegisterFacebookTwitterDialog(MainHomeActivity.this, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (which == 0)
										((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
									if (which == 1)
										((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
								}
							}, user);
							dialog.show();
						}
					}

					@Override
					public void onError(String message) {
						CommonAndroid.showDialog(MainHomeActivity.this, message, null);
					}
				};
				apiCaller.callApi("/user/login", true, callbackAPI, sendData);
			}
		};
		twitterJS = new TwitterJS(this) {
			@Override
			public void loginTwiterSucess() {

				String id = twitterJS.getId();
				APICaller apiCaller = new APICaller(MainHomeActivity.this);
				Map<String, String> sendData = new HashMap<String, String>();
				sendData.put("type", "1");
				sendData.put("id", id);

				ICallbackAPI callbackAPI = new ICallbackAPI() {
					@Override
					public void onSuccess(String response) {
						BaseItem baseItem = new BaseItem(response);
						if (baseItem.getString("status") == null) {
							onError("Can not connect to server");
						} else if ("1".equals(baseItem.getString("status"))) {
							AccountDB accountDB = new AccountDB(MainHomeActivity.this);
							accountDB.save(response);
							GcmBroadcastReceiver.register(MainHomeActivity.this);
							((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
						} else {
							RegisterFacebookTwitterDialog dialog = new RegisterFacebookTwitterDialog(MainHomeActivity.this, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (which == 0)
										((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
									if (which == 1)
										((BaseTabActivity) getParent()).refreshMenuAndgotoHome();
								}
							}, twitterJS.getId(), twitterJS.getName());
							dialog.show();
						}
					}

					@Override
					public void onError(String message) {
						CommonAndroid.showDialog(MainHomeActivity.this, message, null);
					}
				};
				apiCaller.callApi("/user/login", true, callbackAPI, sendData);
			}
		};

		int type = getIntent().getIntExtra("type", -1);
		setContentView(R.layout.mainhome);
		if (type == HOME) {
			changeFragemt(new V3HomeFragment());
		} else if (LOGIN == type) {
			// changeFragemt(new LoginFragment());

			changeFragemt(new V3LoginGFragment());
		} else if (MESSAGE == type) {
			changeFragemt(new MessageFragment());
		} else if (MORE == type) {
			changeFragemt(new ConactUsFragment());
		} else if (type == CONTACTUS) {
			changeFragemt(new ConactUsFragment());
		} else if (type == ABOUT) {
			changeFragemt(new AboutFragment());
		} else if (type == HELP) {
			changeFragemt(new HelpFragment());
		} else if (type == X1) {
			changeFragemt(new X1Fragment());
		} else if (type == X2) {
			changeFragemt(new X2Fragment());
		} else if (type == X3) {
			changeFragemt(new X3Fragment());
		} else if (type == NEWANDALERTS) {
			changeFragemt(new NewAndEventFragment());
		} else if (type == PROFILE) {
			changeFragemt(new ProfileFragment());
		} else if (type == PROJECTMABAGER) {
			changeFragemt(new ProjectManagerFragment());
		} else if (type == BLOG) {
			changeFragemt(new BlogFragment());
		}
	}

	/**
	 * facebook
	 */
	public void loginFacebook() {
		facebookUtils.login();
	}

	/**
	 * twitter
	 */

	public void loginTwitter() {
		twitterJS.login();
	}

	private FacebookUtils facebookUtils;
	private TwitterJS twitterJS;

	private Uri saveBitmaptoUri(Bitmap bmp) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		// String temp = null;
		File file = new File(extStorageDirectory, "temp.png");

		if (file.exists()) {
			file.delete();
			file = new File(extStorageDirectory, "temp.png");
		}

		try {
			outStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return Uri.fromFile(file);
	}

	private Bitmap getScaledBitmap(String picturePath, int width, int height) {
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, sizeOptions);

		int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

		sizeOptions.inJustDecodeBounds = false;
		sizeOptions.inSampleSize = inSampleSize;

		return BitmapFactory.decodeFile(picturePath, sizeOptions);
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// Event handler for radio buttons
	// public ProfileFragment profilePage;
	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.mail_delivery_flg_0:
		case R.id.mail_delivery_flg_1:
		case R.id.sex_male:
			;
		case R.id.sex_female:
			if (checked) {
				// profilePage.updataRadioButtonClicked((RadioButton) view);
			}
			break;
		}
	}

	public void openFragmentListener(View mMain, final AnimationAction animationAction) {
		if (mMain == null)
			return;
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.a_nothing);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animationAction != null)
					animationAction.onAnimationEnd();
			}
		});
		mMain.startAnimation(animation);
	}
}