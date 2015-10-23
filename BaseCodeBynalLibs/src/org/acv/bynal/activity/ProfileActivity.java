package org.acv.bynal.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.camera.Base64;
import org.acv.bynal.camera.CropActivity;
import org.acv.bynal.dialog.ChangePasswordDialog;
import org.acv.bynal.dialog.UnRegisterDialog;
import org.acv.bynal.fragment.ChangePassFragment;
import org.acv.bynal.fragment.ProfileFragment;
//import org.acv.bynal.fragment.UnRegisterFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import app.bynal.woman.R;

import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;

public class ProfileActivity extends BaseActivity {
	public static ProfileFragment profilePage;
	public ChangePassFragment changePass;
//	public UnRegisterFragment unRegister;
	public static final int PICK_FROM_CAMERA = 2001;
	public static final int CROP_FROM_CAMERA = 2002;
	public static final int PICK_FROM_FILE = 2003;
	public static Uri mImageCaptureUri;
	// public static byte[] byteArrayImgProfile;
	// public static String uploadImg = "123";
	public static Bitmap croppedImage;
	static Context contex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		profilePage = new ProfileFragment();
		changePass = new ChangePassFragment();
//		unRegister = new UnRegisterFragment();
		changeFragemt(profilePage);
		openFragmentListener(findViewById(R.id.mFrament), profilePage.getAnimationAction());
		contex = this;
		// HeaderOption headerOption = new HeaderOption(this,
		// TYPEHEADER.NORMAL);
		//
		// headerOption.setShowButtonLeft(true);
		// headerOption.setShowButtonRight(true);
		// headerOption.setResHeader(R.string.cancelled);
		// headerOption.setResDrawableLeft(R.drawable.back_xml);
		// headerOption.setResDrawableRight(R.drawable.menu_xml);
		// getHeaderView().setheaderOption(headerOption);
		configMenuRight();
	}

	@Override
	public void configMenuleft() {
		findViewById(R.id.menuslide_header).setVisibility(View.GONE);
		findViewById(R.id.menu_main_content).setBackgroundResource(R.drawable.bg_menuright);
		addmenu(new MenuItem(getResources().getString(R.string.menu_profile_changepass), R.drawable.profile_menu_change_password, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_profile_unregister), R.drawable.profile_menu_unregister, true));
	}

	@Override
	public void onItemClick(int position, MenuItem item) {
		super.onItemClick(position, item);
		// Toast.makeText(this, item.getName() + "::item", Toast.LENGTH_SHORT)
		// .show();
		if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_profile_changepass))) {
			// changeFragemt(changePass);
			new ChangePasswordDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
		}
		if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_profile_unregister))) {
			// changeFragemt(unRegister);
			new UnRegisterDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
		}
		// startActivityForResult(new Intent(this, ProfileActivity.class),
		// ByUtils.REQUEST_PROFILE);
	}

	// Event handler for radio buttons
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
				// profilePage.updataRadioButtonClicked(view);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
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
						startActivityForResult(PageCrop, ProfileActivity.CROP_FROM_CAMERA);
					}
				} else {
					AccountDB support_info = new AccountDB(this);
					String picturePath_tmp = support_info.getData("img_profile");
					String picturePath = picturePath_tmp.substring("file://".length());
					Bitmap photo = getScaledBitmap(picturePath, 200, 200);
					Uri uri = saveBitmaptoUri(photo);
					Intent PageCrop = new Intent(this, CropActivity.class);
					PageCrop.putExtra("profileImgUri", uri.toString());
					startActivityForResult(PageCrop, ProfileActivity.CROP_FROM_CAMERA);
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
				startActivityForResult(PageCrop, ProfileActivity.CROP_FROM_CAMERA);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void uploadAvatar(Bitmap bmp) {
		if (bmp != null) {
			croppedImage = bmp;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] byte_arr = stream.toByteArray();
			String image_str = Base64.encodeBytes(byte_arr);
			profilePage.CallAPIUploadAvata(image_str);
		}

	}

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

	public static void gotoHome() {
		((Activity) contex).setResult(ByUtils.RESPONSE_RELEASETOKEN);
		((Activity) contex).finish();
	}

	public static void gotoNewPage(Intent mIntent) {
		((Activity) contex).startActivityForResult(mIntent, ByUtils.REQUEST_PROJECT_DETAIL);
	}
}