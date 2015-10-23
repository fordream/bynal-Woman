package a.com.acv.bynal.v3.views;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.dialog.ForgotPasswordDialog;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;

public class V3LoginView extends BaseView implements View.OnClickListener {
	SharedPreferences preferences;

	public V3LoginView(Context context) {
		super(context);
		init(R.layout.v3loginview);
	}

	public V3LoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.v3loginview);
	}

	private CheckBox checkBoxLogin;

	@Override
	public void init(int res) {
		super.init(res);
		preferences = getContext().getSharedPreferences("V3Login", 0);

		findViewById(R.id.login_main_login_forgotpassword).setOnClickListener(this);
		findViewById(R.id.login_main_login_btnlogin).setOnClickListener(this);
		findViewById(R.id.login_main_login_facebook).setOnClickListener(this);
		findViewById(R.id.login_main_login_twitter).setOnClickListener(this);

		checkBoxLogin = (CheckBox) findViewById(R.id.login_main_login_checkbox);
		findViewById(R.id.login_main_login_text5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxLogin.setChecked(!checkBoxLogin.isChecked());
			}
		});
	}

	@Override
	public void onClick(View v) {
		CommonAndroid.hiddenKeyBoard((Activity) getActivity());
		if (v.getId() == R.id.login_main_login_forgotpassword) {

			new ForgotPasswordDialog(getActivity(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((BaseTabActivity) ((MainHomeActivity) getActivity()).getParent()).refreshMenuAndgotoHome();
				}
			}).show();
		} else if (v.getId() == R.id.login_main_login_btnlogin) {
			final String email = getTextStr(R.id.login_main_login_email);
			final String password = getTextStr(R.id.login_main_login_password);

			if (ByUtils.isBlank(email) || ByUtils.isBlank(password)) {
				String message = getResources().getString(R.string.error_message_input_email);
				if (ByUtils.isBlank(email)) {
					message = getResources().getString(R.string.error_message_input_email);
				} else {
					message = getResources().getString(R.string.error_message_input_password);
				}
				showDialogMessage(message);
			} else {
				Map<String, String> sendData = new HashMap<String, String>();
				sendData.put("type", "0");
				sendData.put("id", "xxx");
				sendData.put("user", email);
				sendData.put("password", password);

				APICaller apiCaller = new APICaller(getActivity());

				ICallbackAPI callbackAPI = new ICallbackAPI() {

					@Override
					public void onSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.getString("status").equals("1")) {
								AccountDB accountDB = new AccountDB(getActivity());
								accountDB.save("email", email);
								accountDB.save("password", password);
								accountDB.save(response);
								GcmBroadcastReceiver.register(getActivity());
								((BaseTabActivity) ((MainHomeActivity) getActivity()).getParent()).refreshMenuAndgotoHome();
							} else {
								showDialogMessage(jsonObject.getString("message"));
							}

							// if (checkBoxLogin.isChecked()) {
							// save to account db
							preferences.edit().putBoolean("check", checkBoxLogin.isChecked()).putString("use", email).putString("password", password).commit();
							// }
						} catch (Exception e) {
							showDialogMessage(getResources().getString(R.string.error_message_login_fail));
						}
					}

					@Override
					public void onError(String message) {
						showDialogMessage(getResources().getString(R.string.error_message_login_fail));
					}
				};
				apiCaller.callApi("/user/login", true, callbackAPI, sendData);
			}
		} else if (v.getId() == R.id.login_main_login_facebook) {
			((MainHomeActivity) getContext()).loginFacebook();
		} else if (v.getId() == R.id.login_main_login_twitter) {
			((MainHomeActivity) getContext()).loginTwitter();
		}
	}

	@Override
	public void refresh() {
		checkBoxLogin.setChecked(preferences.getBoolean("check", false));
		if (preferences.getBoolean("check", false)) {
			setText(preferences.getString("use", ""), R.id.login_main_login_email);
			setText(preferences.getString("password", ""), R.id.login_main_login_password);
		} else {
			setText("", R.id.login_main_login_email);
			setText("", R.id.login_main_login_password);
		}
	}
}