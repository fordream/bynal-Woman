package org.acv.bynal.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.camera.Base64;
import org.acv.bynal.dialog.MenuRightDialog;
import org.acv.bynal.dialog.ProfileCameraMenuDialogDialog;
import org.acv.bynal.dialog.TvChooserDialog;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.BankInforView;
import org.acv.bynal.views.HeaderView;
import org.json.JSONException;
import org.json.JSONObject;

import a.com.acv.bynal.v3.dialog.A47_1Dialog;
import a.com.acv.bynal.v3.dialog.A47_Comfirm;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.LogUtils;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.BaseFragment;

public class ProfileFragment extends BaseFragment {

	private BankInforView user_profile_bank_infor;
	View profile;
	static String token_user;
	Boolean getProfile = false;
	String user_name_new;
	static Map<String, String> sendDataUploadImg = new HashMap<String, String>();
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackNewAPI;
	static ICallbackAPI callbackAPIUploadImg;
	private ImageView imgProfile;
	// private Spinner spinner_year;
	private TextView txt_year;
	List<String> listyear = new ArrayList<String>();
	String[] items_year = null;
	int maxYear = 2010;
	int minYear = 1950;
	String birthday_year2 = "";
	LinearLayout profile_main;

	public static Uri mImageCaptureUri;
	public static Bitmap croppedImage;
	private RelativeLayout viewdialog;
	Animation fadeOut2, fadeIn2;
	private boolean showDialogshare = false;
	private boolean onLoad = true;

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		loadPage();
	}

	public ProfileFragment() {
	}

	private void loadPage() {
		APICaller apiCaller = new APICaller(MainHomeActivity.homeActivity);
		callbackNewAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						setData(null);
						setData(jsonObject);
						parserData();
					} else if (jsonObject.getString("status").equals("0")) {
						// token timeout
						CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_message_session_login), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								relaseTooken();
							}
						});

					}
				} catch (JSONException e) {
					CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_connect_server), null);
			}
		};

		callbackAPIUploadImg = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						imgProfile.setImageBitmap(croppedImage);
					}
				} catch (JSONException e) {
				}
			}

			@Override
			public void onError(String message) {
			}
		};
		profile_main.setVisibility(View.INVISIBLE);
		sendData = new HashMap<String, String>();
		AccountDB accountDB = new AccountDB(MainHomeActivity.homeActivity);
		token_user = accountDB.getToken();
		if (ByUtils.isBlank(token_user)) {
			CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_message_pleaselogin), null);
		} else {
			sendData.put("token", token_user);
			apiCaller.callApi("/user/getprofile", true, callbackNewAPI, sendData);
		}

	}

	@Override
	public void setUpFragment(final View view) {
		view.findViewById(R.id.user_profile_add_address).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new A47_Comfirm(getActivity(), null, true) {
					public void gotoHome() {
						BaseTabActivity.senBroadCastHeaderMenu(MainHomeActivity.homeActivity, "home");
					};

					public void dismiss() {
						super.dismiss();
						loadPage();
					};
				}.show();

			}
		});

		user_profile_bank_infor = (BankInforView) view.findViewById(R.id.user_profile_bank_infor);
		
		user_profile_bank_infor.setVisibility(View.GONE);
		viewdialog = (RelativeLayout) view.findViewById(R.id.camera_block);
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		profile = view;
		view.findViewById(R.id.profile_user_btnupdateprofile).setOnClickListener(this);
		view.findViewById(R.id.prof_img_upload).setOnClickListener(this);
		imgProfile = (ImageView) view.findViewById(R.id.prof_img_upload);
		profile_main = (LinearLayout) view.findViewById(R.id.profile_main);
		txt_year = (TextView) view.findViewById(R.id.birthday_year);
		Calendar cal = Calendar.getInstance();
		maxYear = cal.get(Calendar.YEAR);
		for (int i = maxYear; i >= minYear; i--) {
			listyear.add("" + i);
		}
		items_year = new String[listyear.size()];
		for (int i = 0; i < listyear.size(); i++) {
			items_year[i] = listyear.get(i);
		}

		view.findViewById(R.id.birthday_year).setOnClickListener(this);
		view.findViewById(R.id.camera_block).setOnClickListener(this);

		view.findViewById(R.id.check_sex_male).setOnClickListener(this);
		view.findViewById(R.id.check_sex_female).setOnClickListener(this);
		view.findViewById(R.id.check_mail_delivery_flg_0).setOnClickListener(this);
		view.findViewById(R.id.check_mail_delivery_flg_1).setOnClickListener(this);

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
		final ScrollView scrollView = (ScrollView) view.findViewById(R.id.profile_main_s);
		scrollView.setOnTouchListener(new View.OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}

		});
	}

	public void updataRadioButtonClicked(int view, boolean check1) {

		// Is the button now checked?
		boolean checked = check1;// ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		RadioButton sex_male;
		RadioButton sex_female;
		sex_male = (RadioButton) profile.findViewById(R.id.sex_male);
		sex_female = (RadioButton) profile.findViewById(R.id.sex_female);
		RadioButton mail_delivery_flg_0;
		RadioButton mail_delivery_flg_1;
		mail_delivery_flg_0 = (RadioButton) profile.findViewById(R.id.mail_delivery_flg_0);
		mail_delivery_flg_1 = (RadioButton) profile.findViewById(R.id.mail_delivery_flg_1);

		switch (view) {
		case R.id.check_sex_male:
			if (checked) {
				check_sex_male = true;
				check_sex_female = false;
				sex_male.setChecked(true);
				sex_female.setChecked(false);
				sex_male.setBackgroundResource(R.drawable.radio_button_check);
				sex_female.setBackgroundResource(R.drawable.radio_button_uncheck);
			}
			break;
		case R.id.check_sex_female:
			if (checked) {
				check_sex_male = false;
				check_sex_female = true;
				sex_male.setChecked(false);
				sex_female.setChecked(true);
				sex_male.setBackgroundResource(R.drawable.radio_button_uncheck);
				sex_female.setBackgroundResource(R.drawable.radio_button_check);
			}
			break;
		case R.id.check_mail_delivery_flg_0:
			if (checked) {
				check_mail_delivery_flg_0 = true;
				check_mail_delivery_flg_1 = false;
				mail_delivery_flg_0.setChecked(true);
				mail_delivery_flg_1.setChecked(false);
				mail_delivery_flg_0.setBackgroundResource(R.drawable.radio_button_check);
				mail_delivery_flg_1.setBackgroundResource(R.drawable.radio_button_uncheck);
			}
			break;
		case R.id.check_mail_delivery_flg_1:
			if (checked) {
				check_mail_delivery_flg_0 = false;
				check_mail_delivery_flg_1 = true;
				mail_delivery_flg_0.setChecked(false);
				mail_delivery_flg_1.setChecked(true);
				mail_delivery_flg_0.setBackgroundResource(R.drawable.radio_button_uncheck);
				mail_delivery_flg_1.setBackgroundResource(R.drawable.radio_button_check);
			}
			break;
		}
	}

	public static void CallAPIUploadAvata(String avatar) {
		sendDataUploadImg = new HashMap<String, String>();
		sendDataUploadImg.put("token", token_user);
		sendDataUploadImg.put("file", avatar);
		new APICaller(MainHomeActivity.homeActivity).callApi("/user/updateavatar", true, callbackAPIUploadImg, sendDataUploadImg);
	}

	private String delivery_id = null;

	public void parserData() {
		if (getData() instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) getData();
			try {
				delivery_id = jsonObject.getString("delivery_id");
				String user_name = jsonObject.getString("user_name");
				if (user_name.equalsIgnoreCase("null")) {
					user_name = "";
				}
				String user_name_yomi = jsonObject.getString("user_name_yomi");
				if (user_name_yomi.equalsIgnoreCase("null")) {
					user_name_yomi = "";
				}
				// String user_image_no = jsonObject.getString("user_image_no");
				String biography = jsonObject.getString("biography");
				if (biography.equalsIgnoreCase("null")) {
					biography = "";
				}
				String sex = jsonObject.getString("sex");
				String birthday_year = jsonObject.getString("birthday_year");
				if (birthday_year.equalsIgnoreCase("null")) {
					birthday_year = "";
				}
				String hometown = jsonObject.getString("hometown");
				if (hometown.equalsIgnoreCase("null")) {
					hometown = "";
				}
				String job = jsonObject.getString("job");
				if (job.equalsIgnoreCase("null")) {
					job = "";
				}
				String history1 = jsonObject.getString("history1");
				if (history1.equalsIgnoreCase("null")) {
					history1 = "";
				}
				String history2 = jsonObject.getString("history2");
				if (history2.equalsIgnoreCase("null")) {
					history2 = "";
				}
				String history3 = jsonObject.getString("history3");
				if (history3.equalsIgnoreCase("null")) {
					history3 = "";
				}
				String user_url1 = jsonObject.getString("user_url1");
				if (user_url1.equalsIgnoreCase("null")) {
					user_url1 = "";
				}
				String user_url2 = jsonObject.getString("user_url2");
				if (user_url2.equalsIgnoreCase("null")) {
					user_url2 = "";
				}
				String user_url3 = jsonObject.getString("user_url3");
				if (user_url3.equalsIgnoreCase("null")) {
					user_url3 = "";
				}
				String mail_delivery_flg = jsonObject.getString("mail_delivery_flg");
				String prof_img_upload = jsonObject.getString("prof_img_upload");
				String name = jsonObject.getString("delivery_name"); // name
				if (name.equalsIgnoreCase("null")) {
					name = "";
				}
				String zip1 = jsonObject.getString("delivery_zip1");// zip
				if (zip1.equalsIgnoreCase("null")) {
					zip1 = "";
				}
				String zip2 = jsonObject.getString("delivery_zip2");// zip
				if (zip2.equalsIgnoreCase("null")) {
					zip2 = "";
				}
				String phone = jsonObject.getString("delivery_phone");// phone
				if (phone.equalsIgnoreCase("null")) {
					phone = "";
				}
				String address = jsonObject.getString("delivery_address");
				if (address.equalsIgnoreCase("null")) {
					address = "";
				}

				user_profile_bank_infor.update(jsonObject);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String currentDateandTime = sdf.format(new Date());

				ImageLoaderUtils.getInstance(MainHomeActivity.homeActivity).DisplayImage(prof_img_upload + "?" + currentDateandTime, (ImageView) profile.findViewById(R.id.prof_img_upload));

				((TextView) profile.findViewById(R.id.user_name)).setText(user_name);
				((TextView) profile.findViewById(R.id.user_name_yomi)).setText(user_name_yomi);
				((TextView) profile.findViewById(R.id.biography)).setText(biography);

				RadioButton sex_male;
				RadioButton sex_female;
				sex_male = (RadioButton) profile.findViewById(R.id.sex_male);
				sex_female = (RadioButton) profile.findViewById(R.id.sex_female);
				if (sex.equalsIgnoreCase(getResources().getString(R.string.profile_sex_man))) {
					sex_male.setChecked(true);
					sex_female.setChecked(false);
					sex_male.setBackgroundResource(R.drawable.radio_button_check);
					sex_female.setBackgroundResource(R.drawable.radio_button_uncheck);
				} else {
					sex_male.setChecked(false);
					sex_female.setChecked(true);
					sex_male.setBackgroundResource(R.drawable.radio_button_uncheck);
					sex_female.setBackgroundResource(R.drawable.radio_button_check);
				}

				if (!birthday_year.equalsIgnoreCase("")) {
					((TextView) profile.findViewById(R.id.birthday_year)).setText(birthday_year);
				}
				((TextView) profile.findViewById(R.id.hometown)).setText(hometown);
				((TextView) profile.findViewById(R.id.job)).setText(job);
				((TextView) profile.findViewById(R.id.history1)).setText(history1);
				((TextView) profile.findViewById(R.id.history2)).setText(history2);
				((TextView) profile.findViewById(R.id.history3)).setText(history3);
				((TextView) profile.findViewById(R.id.user_url1)).setText(user_url1);
				((TextView) profile.findViewById(R.id.user_url2)).setText(user_url2);
				((TextView) profile.findViewById(R.id.user_url3)).setText(user_url3);
				RadioButton mail_delivery_flg_0;
				RadioButton mail_delivery_flg_1;
				mail_delivery_flg_0 = (RadioButton) profile.findViewById(R.id.mail_delivery_flg_0);
				mail_delivery_flg_1 = (RadioButton) profile.findViewById(R.id.mail_delivery_flg_1);
				if (mail_delivery_flg.equalsIgnoreCase("0")) {
					mail_delivery_flg_0.setChecked(true);
					mail_delivery_flg_1.setChecked(false);
					mail_delivery_flg_0.setBackgroundResource(R.drawable.radio_button_check);
					mail_delivery_flg_1.setBackgroundResource(R.drawable.radio_button_uncheck);
				} else {
					mail_delivery_flg_0.setChecked(false);
					mail_delivery_flg_1.setChecked(true);
					mail_delivery_flg_0.setBackgroundResource(R.drawable.radio_button_uncheck);
					mail_delivery_flg_1.setBackgroundResource(R.drawable.radio_button_check);
				}

				((TextView) profile.findViewById(R.id.delivery_name)).setText(name);
				((TextView) profile.findViewById(R.id.delivery_zip1)).setText(zip1);
				((TextView) profile.findViewById(R.id.delivery_zip2)).setText(zip2);
				((TextView) profile.findViewById(R.id.delivery_address)).setText(address);
				((TextView) profile.findViewById(R.id.delivery_phone)).setText(phone);
				getProfile = true;
				if (onLoad) {
					Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
					profile_main.startAnimation(animation);

					onLoad = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_data), null);
			}

		} else {
		}
	}

	@Override
	public int layoytResurce() {
		return R.layout.user_profile;
	}

	@Override
	public HeaderOption getHeaderOption() {
		TYPEHEADER type = TYPEHEADER.NORMAL;
		HeaderOption headerOption = new HeaderOption(MainHomeActivity.homeActivity, type) {
			@Override
			public void onClickButtonLeft() {
				BaseTabActivity.senBroadCastHeaderMenu(MainHomeActivity.homeActivity, "menu");
			}

			@Override
			public void onClickButtonRight() {
				new MenuRightDialog(MainHomeActivity.homeActivity, new DialogInterface.OnClickListener() {
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
		headerOption.setResHeader(R.string.header_profile);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.menuright_xml);
		headerOption.setResDrawableRight2(R.drawable.home_xml);
		return headerOption;

	}

	private Button button;
	private boolean check_sex_male = false;
	private boolean check_sex_female = false;
	private boolean check_mail_delivery_flg_0 = false;
	private boolean check_mail_delivery_flg_1 = false;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.check_sex_male) {
			updataRadioButtonClicked(R.id.check_sex_male, true);
		} else if (v.getId() == R.id.check_sex_female) {
			updataRadioButtonClicked(R.id.check_sex_female, true);
		} else if (v.getId() == R.id.check_mail_delivery_flg_0) {
			updataRadioButtonClicked(R.id.check_mail_delivery_flg_0, true);
		} else if (v.getId() == R.id.check_mail_delivery_flg_1) {
			updataRadioButtonClicked(R.id.check_mail_delivery_flg_1, true);
		} else if (v.getId() == R.id.birthday_year) {
			new TvChooserDialog(MainHomeActivity.homeActivity, items_year) {
				@Override
				public void onItemClick(int item, String text) {
					try {
						((TextView) profile.findViewById(R.id.birthday_year)).setText(items_year[item]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.show();
		} else if (v.getId() == R.id.prof_img_upload && getProfile) {
			final String[] items = new String[] { getResources().getString(R.string.profile_changeimg_from_camera), getResources().getString(R.string.profile_changeimg_from_gallery) };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainHomeActivity.homeActivity, android.R.layout.select_dialog_item, items);
			AlertDialog.Builder builder = new AlertDialog.Builder(MainHomeActivity.homeActivity);

			showController();
		} else if (v.getId() == R.id.profile_user_btnupdateprofile && getProfile) {
			final String token = token_user;
			final String user_name = getTextStr(R.id.user_name);
			final String user_name_yomi = getTextStr(R.id.user_name_yomi);
			final String biography = getTextStr(R.id.biography);
			String sex = "";// getTextStr(R.id.sex);
			// final String birthday_year = getTextStr(R.id.birthday_year);
			final String hometown = getTextStr(R.id.hometown);
			final String job = getTextStr(R.id.job);
			final String history1 = getTextStr(R.id.history1);
			final String history2 = getTextStr(R.id.history2);
			final String history3 = getTextStr(R.id.history3);
			final String user_url1 = getTextStr(R.id.user_url1);
			final String user_url2 = getTextStr(R.id.user_url2);
			final String user_url3 = getTextStr(R.id.user_url3);
			String mail_delivery_flg = "";// getTextStr(R.id.mail_delivery_flg);
			final String delivery_name = getTextStr(R.id.delivery_name);
			final String delivery_zip1 = getTextStr(R.id.delivery_zip1);
			final String delivery_zip2 = getTextStr(R.id.delivery_zip2);
			final String delivery_address = getTextStr(R.id.delivery_address);
			final String delivery_phone = getTextStr(R.id.delivery_phone);

			RadioButton sex_male;
			sex_male = (RadioButton) profile.findViewById(R.id.sex_male);
			RadioButton mail_delivery_flg_0;
			mail_delivery_flg_0 = (RadioButton) profile.findViewById(R.id.mail_delivery_flg_0);

			if (sex_male.isChecked()) {
				sex = getResources().getString(R.string.profile_sex_man);
			} else {
				sex = getResources().getString(R.string.profile_sex_woman);
			}
			if (mail_delivery_flg_0.isChecked()) {
				mail_delivery_flg = "0";
			} else {
				mail_delivery_flg = "1";
			}

			Map<String, String> sendData = new HashMap<String, String>();
			sendData.put("token", token);
			sendData.put("user_name", user_name);
			sendData.put("user_name_yomi", user_name_yomi);
			sendData.put("biography", biography);
			sendData.put("sex", sex);
			sendData.put("birthday_year", birthday_year2);
			sendData.put("hometown", hometown);
			sendData.put("job", job);
			sendData.put("history1", history1);
			sendData.put("history2", history2);
			sendData.put("history3", history3);
			sendData.put("user_url1", user_url1);
			sendData.put("user_url2", user_url2);
			sendData.put("user_url3", user_url3);

			sendData.put("delivery_id", ByUtils.isBlank(delivery_id) ? "" : delivery_id);

			sendData.put("mail_delivery_flg", mail_delivery_flg);
			sendData.put("delivery_name", delivery_name);
			sendData.put("delivery_zip1", delivery_zip1);
			sendData.put("delivery_zip2", delivery_zip2);
			sendData.put("delivery_address", delivery_address);
			sendData.put("delivery_phone", delivery_phone);
			sendData.put("card_number", user_profile_bank_infor.getTextStr(R.id.bank_infor_card_no));
			sendData.put("card_expire", user_profile_bank_infor.getTextStr(R.id.bank_infor_card_expire));
			sendData.put("card_code", user_profile_bank_infor.getTextStr(R.id.bank_infor_card_securitycode));

			user_profile_bank_infor.put(sendData);
			user_name_new = user_name;
			checkprofileValidate(user_name, user_name_yomi, biography, sex, birthday_year2, hometown, job, history1, history2, history3, user_url1, user_url2, user_url3, mail_delivery_flg,
					delivery_name, delivery_zip1, delivery_zip2, delivery_address, delivery_phone);
			APICaller apiCallerUpload = new APICaller(MainHomeActivity.homeActivity);

			ICallbackAPI callbackAPIUpload = new ICallbackAPI() {

				@Override
				public void onSuccess(String response) {
					try {

						Log.e("Error", response);
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.getString("status").equals("1")) {
							CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.profile_update_ok), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									AccountDB accountDB = new AccountDB(MainHomeActivity.homeActivity);
									accountDB.saveName(accountDB.getUserId(), user_name_new);
								}
							});

						} else {
							String err = jsonObject.getString("message");
							err = errReplace(err);
							CommonAndroid.showDialog(MainHomeActivity.homeActivity, err.replaceAll("/n", ""), null);
						}
					} catch (JSONException e) {
						CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_data), null);
					} catch (Exception e) {
						CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_data), null);
					}
				}

				@Override
				public void onError(String message) {
					Log.e("Error", "omg");
					CommonAndroid.showDialog(MainHomeActivity.homeActivity, getResources().getString(R.string.error_connect_server), null);
				}
			};
			if (Message_err_validate.equalsIgnoreCase("")) {
				apiCallerUpload.callApi("/user/updateprofile", true, callbackAPIUpload, sendData);
			} else {
				Log.e("validate", "false");
				CommonAndroid.showDialog(MainHomeActivity.homeActivity, Message_err_validate, null);
			}
		} else if (v.getId() == R.id.camera_block) {
			if (showDialogshare) {
				viewdialog.startAnimation(fadeIn2);
			}
		}
	}

	String Message_err_validate = "";

	private void checkprofileValidate(String user_name, String user_name_yomi, String biography, String sex, String birthday_year2, String hometown, String job, String history1, String history2,
			String history3, String user_url1, String user_url2, String user_url3, String mail_delivery_flg, String delivery_name, String delivery_zip1, String delivery_zip2, String delivery_address,
			String delivery_phone) {
		Message_err_validate = "";
		if (!delivery_name.equalsIgnoreCase("") || !delivery_zip1.equalsIgnoreCase("") || !delivery_zip1.equalsIgnoreCase("") || !delivery_zip2.equalsIgnoreCase("")
				|| !delivery_address.equalsIgnoreCase("") || !delivery_phone.equalsIgnoreCase("")) {

		}

		if (user_name.equalsIgnoreCase("")) {
			Message_err_validate += getResources().getString(R.string.validate_user_name_null);
			// ((TextView)
			// profile.findViewById(R.id.user_name)).setBackgroundResource(R.drawable.textfield_search_empty_pressed);
		}

		if (delivery_name.equalsIgnoreCase("")) {
			Message_err_validate += getResources().getString(R.string.validate_deli_name);
		}
		if (delivery_zip1.equalsIgnoreCase("") || delivery_zip2.equalsIgnoreCase("")) {
			Message_err_validate += getResources().getString(R.string.validate_deli_zip);
		}
		if (delivery_address.equalsIgnoreCase("")) {
			Message_err_validate += getResources().getString(R.string.validate_deli_address);
		}
		if (delivery_phone.equalsIgnoreCase("")) {
			Message_err_validate += getResources().getString(R.string.validate_deli_phone);
		}
		/*
		 * if(checklenght(user_name, "Shift_JIS" ,20 )){ Message_err_validate +=
		 * getResources().getString(R.string.validate_user_name_len); }
		 * if(checklenght(user_name_yomi, "Shift_JIS", 20)){
		 * Message_err_validate +=
		 * getResources().getString(R.string.validate_user_name_yomi_len); }
		 * if(checklenght(biography,null, 64)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_biography_len); }
		 * if(checklenght(hometown,null, 12)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_hometown_len); }
		 * if(checklenght(job,null, 12)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_job_len); }
		 * if(checklenght(history1,null, 50)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_history1_len); }
		 * if(checklenght(history2,null, 50)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_history2_len); }
		 * if(checklenght(history3,null, 50)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_history3_len); }
		 * if(checklenght(user_url1,null, 120)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_user_url1_len); }
		 * if(checklenght(user_url2,null, 120)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_user_url2_len); }
		 * if(checklenght(user_url3,null, 120)){ Message_err_validate +=
		 * getResources().getString(R.string.validate_user_url3_len); }
		 */
		// check match

	}

	private boolean checklenght(String s, String encode, int len) {
		String encode1 = "UTF-8";
		if (encode != null) {
			encode1 = encode;
		}
		try {
			final String s_temp = new String(s.getBytes(), encode1);
			if (s_temp.length() > len) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("utf8", "conversion", e);
			return true;
		}
		return false;
	}

	public boolean checkMatch(String s) {
		return true;
	}

	public static String errReplace(String string) {
		String message = string;
		try {
			if (string == null || string.length() == 0) {
				return "\"\"";
			}

			char c = 0;
			int i;
			int len = string.length();
			StringBuilder sb = new StringBuilder(len + 4);
			String t;
			String temp = "/";
			for (i = 0; i < len; i += 1) {
				c = string.charAt(i);
				switch (c) {
				case '\\':
					sb.append('\n');
					sb.append(temp);
					break;
				case '\n':
					sb.append("\n");
					break;
				default:
					if (c < ' ') {
						t = "000" + Integer.toHexString(c);
						sb.append("\\u" + t.substring(t.length() - 4));
					} else {
						sb.append(c);
					}
				}
			}
			message = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public static void uploadAvatar(Bitmap bmp) {
		if (bmp != null) {
			croppedImage = bmp;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] byte_arr = stream.toByteArray();
			String image_str = Base64.encodeBytes(byte_arr);
			CallAPIUploadAvata(image_str);
		}

	}

	public void showController() {

		new ProfileCameraMenuDialogDialog(getActivity(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
					AccountDB profile_imginfo = new AccountDB(MainHomeActivity.homeActivity);
					Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
					profile_imginfo.save("img_profile", fileUri.toString());
					try {
						intent.putExtra("return-data", true);

						MainHomeActivity.homeActivity.startActivityForResult(intent, MainHomeActivity.PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else if (which == 1) {
					Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					MainHomeActivity.homeActivity.startActivityForResult(i, MainHomeActivity.PICK_FROM_FILE);
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
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//
		// mImageCaptureUri = Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory(), "tmp_avatar_" +
		// String.valueOf(System.currentTimeMillis()) + ".jpg"));
		//
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// mImageCaptureUri);
		// AccountDB profile_imginfo = new
		// AccountDB(MainHomeActivity.homeActivity);
		// Uri fileUri = Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory(), "tmp_avatar_" +
		// String.valueOf(System.currentTimeMillis()) + ".jpg"));
		// profile_imginfo.save("img_profile", fileUri.toString());
		// try {
		// intent.putExtra("return-data", true);
		//
		// MainHomeActivity.homeActivity.startActivityForResult(intent,
		// MainHomeActivity.PICK_FROM_CAMERA);
		// } catch (ActivityNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		// });
		//
		// getView().findViewById(R.id.camera002).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// viewdialog.startAnimation(fadeIn2);
		//
		// Intent i = new Intent(Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// MainHomeActivity.homeActivity.startActivityForResult(i,
		// MainHomeActivity.PICK_FROM_FILE);
		// }
		// });

	}

}
