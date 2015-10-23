package a.com.acv.bynal.v3.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.nbase.MbaseAdialog;

public class ComfirmContactUsDialog extends MbaseAdialog implements android.view.View.OnClickListener, ICallbackAPI {
	private TextView contactus_email, contactus_title, contactus_descripntion, contactus_btn;

	public ComfirmContactUsDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();

		contactus_email = (TextView) findViewById(R.id.contactus_email);
		contactus_title = (TextView) findViewById(R.id.contactus_title);
		contactus_descripntion = (TextView) findViewById(R.id.contactus_descripntion);
		contactus_btn = (TextView) findViewById(R.id.contactus_btn);

		contactus_btn.setOnClickListener(this);

		/**
		 * setdata
		 * 
		 */

		contactus_email.setText(email);
		contactus_title.setText(title);
		contactus_descripntion.setText(description);

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
		findViewById(R.id.mfragment).startAnimation(animation);

	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
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
		headerOption.setResHeader(R.string.comfirm_sendcontactus_title);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_center);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
				if (isOnClick && clickListener != null) {
					clickListener.onClick(null, 0);
				}
			}
		});
		findViewById(R.id.mfragment).startAnimation(animation);

	}

	@Override
	public int getLayout() {
		return R.layout.contactus_cofirm;
	}

	@Override
	public void onClick(View v) {
		// final String email = getTextStr(R.id.contactus_email);
		// final String title = getTextStr(R.id.contactus_title);
		// final String description = getTextStr(R.id.contactus_descripntion);

		APICaller apiCaller = new APICaller(getActivity());
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("title", title);
		sendData.put("email", email);
		sendData.put("body", description);
		apiCaller.callApi("/user/contact", true, this, sendData);

	}

	private Context getActivity() {
		return getContext();
	}

	@Override
	public void onError(String message) {
		showDialogMessage(getContext().getString(R.string.error_message_connect_server_fail));
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
						// goto home
						close(false);
					}
				});
			} else {
				showDialogMessage(jsonObject.getString("message"));
				// onError(jsonObject.getString("message"));
			}
		} catch (Exception e) {
			// onError("Can not send message");
			showDialogMessage(getContext().getString(R.string.error_message_connect_server_fail));
		}
	}

	private String email, title, description;

	public void setData(String email, String title, String description) {
		this.email = email;
		this.description = description;
		this.title = title;

	}
}