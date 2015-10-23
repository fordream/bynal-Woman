package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.main.activity.MainHomeActivity;
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
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class ChangePasswordDialog extends MbaseAdialog implements android.view.View.OnClickListener {

	public ChangePasswordDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		findViewById(R.id.profile_user_btnchangepass).setOnClickListener(this);
		openPopActivity(findViewById(R.id.changepass_m_main));
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.changepassword_header);
		HeaderOption headerOption = new HeaderOption(getContext(), TYPEHEADER.NORMAL) {
			@Override
			public void onClickButtonLeft() {
				super.onClickButtonLeft();
				close(false);
			}

			@Override
			public void onClickButtonRight() {
				super.onClickButtonRight();
				close(true);
				((BaseTabActivity) (MainHomeActivity.homeActivity).getParent()).refreshMenuAndgotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_profile_changepass);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.changepass_m_main), new AnimationAction() {

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
		return R.layout.user_changepass;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.profile_user_btnchangepass) {
			final String password1 = getTextStr(R.id.profile_user_changepass);
			final String password2 = getTextStr(R.id.profile_user_changepass_conf);

			if (ByUtils.isBlank(password1) || ByUtils.isBlank(password2)) {
				CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.change_pass_is_blank), null);
			} else if (!password1.equalsIgnoreCase(password2)) {
				CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.change_pass_passconfirm_err), null);
			} else {
				AccountDB accountDB = new AccountDB(getActivity());
				String token = accountDB.getToken();
				Map<String, String> sendData = new HashMap<String, String>();
				sendData.put("token", token);
				sendData.put("password", password1);
				sendData.put("repassword", password2);

				APICaller apiCaller = new APICaller(getActivity());

				ICallbackAPI callbackAPI = new ICallbackAPI() {

					@Override
					public void onSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.getString("status").equals("1")) {
								CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.change_pass_ok), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// ((BaseActivity)
										// getActivity()).changeFragemt(ProfileActivity.profilePage);
										close(false);
									}
								});
							} else {
								CommonAndroid.showDialog(getActivity(), jsonObject.getString("message"), null);
							}
						} catch (Exception e) {
							CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.change_pass_err), null);
						}
					}

					@Override
					public void onError(String message) {
						// CommonAndroid.showDialog(getActivity(), message + "",
						// null);
						CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
					}
				};
				apiCaller.callApi("/user/changepassword", true, callbackAPI, sendData);
			}
		}
	}

	private Context getActivity() {
		return getContext();
	}
}