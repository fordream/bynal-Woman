package a.com.acv.bynal.v3.views;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.json.JSONObject;

import a.com.acv.bynal.v3.database.ProjectTable;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;

public class V3RegisterView extends BaseView implements View.OnClickListener {

	public V3RegisterView(Context context) {
		super(context);

		init(R.layout.v3registerview);
	}

	public V3RegisterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.v3registerview);
	}

	@Override
	public void init(int res) {
		super.init(res);
		findViewById(R.id.login_main_register_btn1_1).setOnClickListener(this);
		findViewById(R.id.login_main_register_btn2).setOnClickListener(this);
		findViewById(R.id.login_main_register_btn3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		// register

		if (R.id.login_main_register_btn1_1 == v.getId()) {
			String name = getTextStr(R.id.login_main_register_edt1);
			final String email = getTextStr(R.id.login_main_register_edt2);
			final String password = getTextStr(R.id.login_main_register_edt3);
			String repassword = getTextStr(R.id.login_main_register_edt4);

			if (ByUtils.isBlank(name) || ByUtils.isBlank(email)) {

				String message = getResources().getString(R.string.error_message_input_name);
				if (ByUtils.isBlank(name)) {
					message = getResources().getString(R.string.error_message_input_name);
				} else {
					message = getResources().getString(R.string.error_message_input_email);
				}
				showDialogMessage(message);

			} else {
				if (ByUtils.isBlank(password)) {
					String message = getResources().getString(R.string.error_message_input_password);
					showDialogMessage(message);
				} else if (!password.equals(repassword)) {
					String message = getResources().getString(R.string.error_message_check_passwordandrepassword);
					showDialogMessage(message);
				} else {
					Map<String, String> sendData = new HashMap<String, String>();
					sendData.put("type", "0");
					sendData.put("id", "xxx");
					sendData.put("user", name);
					sendData.put("email", email);
					sendData.put("name", name);
					sendData.put("password", password);
					sendData.put("repassword", repassword);

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
							} catch (Exception e) {
								showDialogMessage(getResources().getString(R.string.error_message_register_fail));
							}
						}

						@Override
						public void onError(String message) {
							showDialogMessage(getResources().getString(R.string.error_message_register_fail));
						}
					};
					apiCaller.callApi("/user/register", true, callbackAPI, sendData);

				}
			}
		} else if (R.id.login_main_register_btn2 == v.getId()) {
			((MainHomeActivity) getActivity()).loginFacebook();
		} else if (R.id.login_main_register_btn3 == v.getId()) {
			((MainHomeActivity) getActivity()).loginTwitter();
		}
	}

	@Override
	public void refresh() {
		setText("", R.id.login_main_register_edt1);
		setText("", R.id.login_main_register_edt2);
		setText("", R.id.login_main_register_edt3);
		setText("", R.id.login_main_register_edt4);
	}
}