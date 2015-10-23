package org.acv.bynal.fragment;

import java.util.HashMap;
import java.util.Map;


//import org.acv.bynal.activity.ProfileActivity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.BaseFragment;

public class ChangePassFragment extends BaseFragment implements OnClickListener {

	public ChangePassFragment() {
	}

	@Override
	public void setUpFragment(final View view) {
		view.findViewById(R.id.profile_user_btnchangepass).setOnClickListener(
				this);
//		resizeAndTextSize(view.findViewById(R.id.profile_user_btnchangepass),
//				300, 50, 25);


	}

	@Override
	public int layoytResurce() {
		return R.layout.user_changepass;
	}

	@Override
	public HeaderOption getHeaderOption() {
		TYPEHEADER type = TYPEHEADER.NORMAL;
		HeaderOption headerOption = new HeaderOption(getActivity(), type) {
			@Override
			public void onClickButtonLeft() {
//				((BaseActivity) getActivity()).changeFragemt(ProfileActivity.profilePage);
			}

			@Override
			public void onClickButtonRight() {
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
			}
		};
		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResHeader(R.string.header_profile_changepass);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.profile_user_btnchangepass) {
			final String password1 = getTextStr(R.id.profile_user_changepass);
			final String password2 = getTextStr(R.id.profile_user_changepass_conf);
 
			if (ByUtils.isBlank(password1) || ByUtils.isBlank(password2)) {
				CommonAndroid.showDialog(getActivity(),
						getResources().getString(R.string.change_pass_is_blank), null);
			}else if(!password1.equalsIgnoreCase(password2)){
				CommonAndroid.showDialog(getActivity(),
						getResources().getString(R.string.change_pass_passconfirm_err), null);
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
								CommonAndroid.showDialog(getActivity(),
										getResources().getString(R.string.change_pass_ok), new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
//												((BaseActivity) getActivity()).changeFragemt(ProfileActivity.profilePage);
											}
										}
								);
							} else {
								CommonAndroid.showDialog(getActivity(),
										jsonObject.getString("message"), null);
							}
						} catch (Exception e) {
							CommonAndroid.showDialog(getActivity(),
									getResources().getString(R.string.change_pass_err), null);
						}
					}

					@Override
					public void onError(String message) {
						CommonAndroid.showDialog(getActivity(), message + "",
								null);
					}
				};
				apiCaller.callApi("/user/changepassword", true, callbackAPI, sendData);
			}
		} 
		
	}
}