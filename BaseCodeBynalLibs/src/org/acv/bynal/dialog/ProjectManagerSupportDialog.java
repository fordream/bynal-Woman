package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.activity.ProjectmanagerActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.fragment.ProjectManagerFragment;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ProjectManagerSupportDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	String token_user;
	LinearLayout pm_support_detail;

	public ProjectManagerSupportDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		pm_support_detail = (LinearLayout) findViewById(R.id.pm_support_detail);
		openPopActivity(findViewById(R.id.pm_support_m_main));
		onLoad();
	}

	private void onLoad() {
		AccountDB accountDB = new AccountDB(getActivity());
		token_user = accountDB.getToken();
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					pm_support_detail.setVisibility(View.VISIBLE);
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						if (jsonObject.getString("now_support_point") != null) {
							String monny = " " + getActivity().getResources().getString(R.string.header_project_support_detail_monny);
							String now_support_point = jsonObject.getString("now_support_point") + monny;
							String pending = jsonObject.getString("pending") + monny;
							String system_per = jsonObject.getString("system_per") + monny;
							String calc_paid = jsonObject.getString("calc_paid") + monny;

							((TextView) findViewById(R.id.now_support_point)).setText(now_support_point);
							((TextView) findViewById(R.id.pending)).setText(pending);
							((TextView) findViewById(R.id.system_per)).setText(system_per);
							((TextView) findViewById(R.id.calc_paid)).setText(calc_paid);
						} else {

						}
					}

				} catch (JSONException e) {
					pm_support_detail.setVisibility(View.VISIBLE);
					Log.e("PM", "err::" + e.getMessage());
				}
			}

			@Override
			public void onError(String message) {
				pm_support_detail.setVisibility(View.VISIBLE);
			}
		};
		sendData = new HashMap<String, String>();
		sendData.put("project_id", ProjectManagerFragment.project_id);
		sendData.put("token", token_user);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new APICaller(getActivity()).callApi("/project/supportDetail", true, callbackAPI, sendData);
			}
		}, 500);

	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pm_support_header);
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
		headerOption.setResHeader(R.string.header_project_support_detail);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pm_support_m_main), new AnimationAction() {

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
		return R.layout.projectmanager_support;
	}

	@Override
	public void onClick(View v) {

	}

	private Context getActivity() {
		return getContext();
	}
}