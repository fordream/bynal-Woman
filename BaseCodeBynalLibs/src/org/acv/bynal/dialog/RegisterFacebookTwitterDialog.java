package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;
import com.facebook.model.GraphUser;

public class RegisterFacebookTwitterDialog extends MbaseAdialog implements android.view.View.OnClickListener {

	public RegisterFacebookTwitterDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
	}

	private GraphUser user;
	private String id, name;

	public RegisterFacebookTwitterDialog(Context context, OnClickListener clickListener, GraphUser user) {
		super(context, clickListener);
		this.user = user;
	}

	public RegisterFacebookTwitterDialog(Context context, OnClickListener clickListener, String id, String name) {
		super(context, clickListener);
		this.id = id;
		this.name = name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();

		findViewById(R.id.registerfacebooktwitter_btn1).setOnClickListener(this);
		findViewById(R.id.registerfacebooktwitter_btn1_twwiter).setOnClickListener(this);
		// TextView textView =
		// (TextView)findViewById(R.id.registerfacebooktwitter_btn1);
		if (user == null) {
			setText(R.id.registerfacebooktwitter_edt1, name);
			findViewById(R.id.registerfacebooktwitter_btn1).setVisibility(View.GONE);
			// textView.setBackgroundResource(R.drawable.diablog_register_button_twitter);

			// textView.setTextAppearance(getContext(),
			// R.style.v3_button_twitter);
			// textView.setText(R.string.v3_register_twwiter);
		} else {
			setText(R.id.registerfacebooktwitter_edt1, user.getName());
			findViewById(R.id.registerfacebooktwitter_btn1_twwiter).setVisibility(View.GONE);
			// textView.setBackgroundResource(R.drawable.diablog_register_button_facebook);
			// textView.setTextAppearance(getContext(),
			// R.style.v3_button_facebook);
			// textView.setText(R.string.v3_register_facebook);
		}

		openPopActivity(findViewById(R.id.forgotpassword_m_main));

	}

	private void dismiss(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.forgotpassword_m_main), new AnimationAction() {

			@Override
			public void onAnimationEnd() {
				dismiss();
				if (isOnClick) {
					clickListener.onClick(null, 1);
				}
			}
		});
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.registerfacebook_header);
		HeaderOption headerOption = new HeaderOption(getContext(), TYPEHEADER.NORMAL) {
			@Override
			public void onClickButtonLeft() {
				super.onClickButtonLeft();
				dismiss(false);
			}

			@Override
			public void onClickButtonRight() {
				super.onClickButtonRight();

				dismiss(true);
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		if (user == null) {
			headerOption.setResHeader(R.string.twitter_register);
		} else {
			headerOption.setResHeader(R.string.facebook_register);
		}

		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	@Override
	public int getLayout() {
		return R.layout.registerfacebooktwitter;
	}

	@Override
	public void onClick(View v) {
		String name = getTextStr(R.id.registerfacebooktwitter_edt1);
		final String email = getTextStr(R.id.registerfacebooktwitter_edt2);
		final String password = getTextStr(R.id.registerfacebooktwitter_edt3);
		String repassword = getTextStr(R.id.registerfacebooktwitter_edt3);

		String message = null;

		if (ByUtils.isBlank(name)) {
			message = getContext().getResources().getString(R.string.error_message_input_name);
		}

		if (ByUtils.isBlank(email)) {
			message = getContext().getResources().getString(R.string.error_message_input_email);
		}

		if (ByUtils.isBlank(password)) {
			message = getContext().getResources().getString(R.string.error_message_input_password);
		}

		if (!password.equals(repassword)) {
			message = getContext().getResources().getString(R.string.error_message_check_passwordandrepassword);
		}

		if (message != null) {
			CommonAndroid.showDialog(getContext(), message, null);
			return;
		}

		Map<String, String> sendData = new HashMap<String, String>();
		if (user != null) {
			sendData.put("type", "2");
			sendData.put("id", user.getId());
		} else {
			sendData.put("type", "1");
			sendData.put("id", id);
		}

		sendData.put("user", name);
		sendData.put("email", email);
		sendData.put("name", name);
		sendData.put("password", password);
		sendData.put("repassword", repassword);

		APICaller apiCaller = new APICaller(getContext());
		ICallbackAPI callbackAPI = new ICallbackAPI() {

			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						AccountDB accountDB = new AccountDB(getContext());
						accountDB.save("email", email);
						accountDB.save("password", password);
						accountDB.save(response);
						GcmBroadcastReceiver.register(getContext());
						clickListener.onClick(null, 0);
						dismiss();
					} else {
						CommonAndroid.showDialog(getContext(), jsonObject.getString("message"), null);
					}
				} catch (Exception e) {
					CommonAndroid.showDialog(getContext(), getContext().getResources().getString(R.string.error_message_register_fail), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getContext(), getContext().getResources().getString(R.string.error_message_register_fail), null);
			}
		};
		apiCaller.callApi("/user/register", true, callbackAPI, sendData);
	}
}