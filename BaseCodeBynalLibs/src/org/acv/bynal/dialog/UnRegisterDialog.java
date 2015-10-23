package org.acv.bynal.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

//import org.acv.bynal.activity.ProfileActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.nbase.MbaseAdialog;

public class UnRegisterDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	private WebView credit;
	/** Layout of credit. */
	private RelativeLayout rl;
	APICaller apiCaller;
	ICallbackAPI callbackAPI;
	Map<String, String> sendData = new HashMap<String, String>();
	private TextView unregister_button;

	public UnRegisterDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		findViewById(R.id.profile_user_btnunregister).setOnClickListener(this);
		openPopActivity(findViewById(R.id.unregister_m_main));
		unregister_button = (TextView) findViewById(R.id.profile_user_btnunregister);
		unregister_button.setVisibility(View.GONE);
		credit = new WebView(getActivity());
		try {
			progressDialog = ProgressDialog.show(getActivity(), "", "", true);
			progressDialog.show();
			String str = "";
			str += loadResouceHTMLFromAssets("webviewcontent.html");
			credit.loadDataWithBaseURL("file:///android_asset/", str, "text/html", "UTF-8", "");
			credit.setWebChromeClient(new WebChromeClient() {
			});

			credit.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return false;
				}

				public void onPageFinished(WebView view, String url) {
					progressDialog.dismiss();
					unregister_button.setVisibility(View.VISIBLE);
				}
			});

		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
		rl = (RelativeLayout) findViewById(R.id.credit_display);
		rl.addView(credit);

	}

	public String loadResouceHTMLFromAssets(String filename) {
		String tmp = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(filename)));
			String word;
			while ((word = br.readLine()) != null) {
				if (!word.equalsIgnoreCase("")) {
					tmp += word;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tmp;
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.unregister_header);
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
				// ProfileActivity.gotoHome();

				// gotoHome();
				// BaseTabActivity.gotoHome(getActivity());
				((BaseTabActivity) (MainHomeActivity.homeActivity).getParent()).refreshMenuAndgotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_profile_unregister);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.unregister_m_main), new AnimationAction() {

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
		return R.layout.user_unregister;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.profile_user_btnunregister) {
			AccountDB accountDB = new AccountDB(getActivity());
			String token = accountDB.getToken();
			sendData.put("token", token);
			apiCaller = new APICaller(getActivity());
			callbackAPI = new ICallbackAPI() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						// CommonAndroid.showDialog(getActivity(),"" +
						// jsonObject.toString(), null);
						if (jsonObject.getString("success").equals("1")) {
							// getActivity().setResult(ByUtils.RESPONSE_RELEASETOKEN);
							// getActivity().finish();
							close(false);
							// ProfileActivity.gotoHome();
						} else {
							CommonAndroid.showDialog(getActivity(), jsonObject.getString("message"), null);
						}
					} catch (Exception e) {
						try {
							JSONObject jsonObject2 = new JSONObject(response);
							CommonAndroid.showDialog(getActivity(), jsonObject2.getString("message"), null);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							// CommonAndroid.showDialog(getActivity(),
							// e.getMessage(), null);
							CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
						}
						e.printStackTrace();
					}
				}

				@Override
				public void onError(String message) {
					// CommonAndroid.showDialog(getActivity(), message + "",
					// null);
					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
				}
			};
			CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.unregister_confirm), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// leave
					apiCaller.callApi("/user/leave", true, callbackAPI, sendData);
				}
			}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Cancel
				}
			});

		}
	}

	private Context getActivity() {
		return getContext();
	}
}