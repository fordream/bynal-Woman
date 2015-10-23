package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class ForgotPasswordDialog extends MbaseAdialog implements android.view.View.OnClickListener {

	public ForgotPasswordDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		findViewById(R.id.forgotpassword_btn).setOnClickListener(this);

		openPopActivity(findViewById(R.id.forgotpassword_m_main));
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.forgotpassword_header);
		HeaderOption headerOption = new HeaderOption(getContext(), TYPEHEADER.NORMAL) {
			@Override
			public void onClickButtonLeft() {
				super.onClickButtonLeft();
				// dismiss();
				close(false);
			}

			@Override
			public void onClickButtonRight() {
				super.onClickButtonRight();
				// dismiss();
				// clickListener.onClick(null, 0);
				close(true);
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.forgot_header);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {

		closePopActivity(getContext(), findViewById(R.id.forgotpassword_m_main), new AnimationAction() {

			@Override
			public void onAnimationEnd() {
				dismiss();
				if (isOnClick) {
					clickListener.onClick(null, 0);
				}
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.forgotpassworddialog;
	}

	@Override
	public void onClick(View v) {
		String email = getTextStr(R.id.forgotpassword_edit);

		if (ByUtils.isBlank(email)) {
			showDialogMessage(getActivity().getString(R.string.error_message_input_email));
		} else {
			APICaller apiCaller = new APICaller(getActivity());
			Map<String, String> sendData = new HashMap<String, String>();
			sendData.put("email", email);
			apiCaller.callApi("/user/forgotpassword", true, new ICallbackAPI() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.getString("status").equals("1")) {
							CommonAndroid.showDialog(getActivity(), jsonObject.getString("message"), new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dismiss();
								}
							});
						} else {
							showDialogMessage(jsonObject.getString("message"));
						}
					} catch (Exception e) {
						showDialogMessage(getActivity().getString(R.string.error_message_connect_server_fail));
					}
				}

				@Override
				public void onError(String message) {
					showDialogMessage(getActivity().getString(R.string.error_message_connect_server_fail));
				}
			}, sendData);
		}
	}

	private Context getActivity() {
		return getContext();
	}
}