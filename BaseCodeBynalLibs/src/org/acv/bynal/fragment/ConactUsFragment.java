package org.acv.bynal.fragment;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import a.com.acv.bynal.v3.dialog.ComfirmContactUsDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.acv.libs.nbase.BaseFragment;

public class ConactUsFragment extends BaseFragment implements ICallbackAPI {

	public ConactUsFragment() {
	}

	@Override
	public void setUpFragment(View view) {
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());

		/**
		 * 
		 */
		contactus_email = (TextView) view.findViewById(R.id.contactus_email);
		contactus_title = (TextView) view.findViewById(R.id.contactus_title);
		contactus_descripntion = (TextView) view.findViewById(R.id.contactus_descripntion);
		contactus_btn = (TextView) view.findViewById(R.id.contactus_btn);

		contactus_btn.setOnClickListener(this);
	}

	private TextView contactus_email, contactus_title, contactus_descripntion, contactus_btn;

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	private void reload() {
		// contactus_btn.setText(R.string.str_contactus_1);
		contactus_email.setText("");
		contactus_title.setText("");
		contactus_descripntion.setText("");
		
		AccountDB accountDB = new AccountDB(getActivity());
		if(ByUtils.isBlank(accountDB.getToken())){
			contactus_email.setEnabled(true);
		}else{
			contactus_email.setEnabled(false);
			contactus_email.setText(accountDB.getEmail());
		}
	}

	@Override
	public void onClick(View v) {
		CommonAndroid.hiddenKeyBoard(getActivity());
		final String email = getTextStr(R.id.contactus_email);
		final String title = getTextStr(R.id.contactus_title);
		final String description = getTextStr(R.id.contactus_descripntion);

		/**
		 * comfirm message
		 */
		String message = null;

		if (ByUtils.isBlank(email)) {
			message = getString(R.string.error_message_input_email);
		}

		if (ByUtils.isBlank(title)) {
			message = getString(R.string.error_message_input_title);
		}

		if (ByUtils.isBlank(description)) {
			message = getString(R.string.error_message_input_description);
		}

		if (message != null) {
			CommonAndroid.showDialog(getActivity(), message, null);
			return;
		}

		if (!ByUtils.isEmail(email)) {
			message = getString(R.string.error_message_input_email_check);
			CommonAndroid.showDialog(getActivity(), message, null);
			return;
		}

		// if
		// (contactus_btn.getText().toString().equals(getString(R.string.str_contactus_1)))
		// {
		// CommonAndroid.hiddenKeyBoard(getActivity());
		// contactus_email.setEnabled(false);
		// contactus_title.setEnabled(false);
		// contactus_descripntion.setEnabled(false);
		// contactus_btn.setText(R.string.str_contactus_2);
		// return;
		// }
		ComfirmContactUsDialog comfirmContactUsDialog = new ComfirmContactUsDialog(getActivity(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// goto home
			}
		});
		comfirmContactUsDialog.setData(email, title, description);
		comfirmContactUsDialog.show();
		// String messageComfirmSendContact =
		// getString(R.string.message_comfirm_sendcontactus);
		// CommonAndroid.showDialog(getActivity(), messageComfirmSendContact,
		// new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// APICaller apiCaller = new APICaller(getActivity());
		// Map<String, String> sendData = new HashMap<String, String>();
		// sendData.put("title", title);
		// sendData.put("email", email);
		// sendData.put("body", description);
		// apiCaller.callApi("/user/contact", true, ConactUsFragment.this,
		// sendData);
		// }
		// }, null);
	}

	@Override
	public int layoytResurce() {
		return R.layout.contactus;
	}

	@Override
	public void onError(String message) {
		showDialogMessage(getString(R.string.error_message_connect_server_fail));
	}

	@Override
	public void onSuccess(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("status").equals("1")) {
				// setText("", R.id.contactus_email);
				// setText("", R.id.contactus_title);
				// setText("", R.id.contactus_descripntion);

				CommonAndroid.showDialog(getActivity(), jsonObject.getString("message"), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						reload();
						// goto home
					}
				});
			} else {
				showDialogMessage(jsonObject.getString("message"));
				// onError(jsonObject.getString("message"));
			}
		} catch (Exception e) {
			// onError("Can not send message");
			showDialogMessage(getString(R.string.error_message_connect_server_fail));
		}
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.menu_home_contact_us);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}
