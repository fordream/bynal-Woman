package org.acv.bynal.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectDetailReportdetailItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class ProjectDetailReportdetailDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ListView pDetailReportdetailview;
	String projectId = "0";
	String project_report_no = "";
	private WebView credit;
	/** Layout of credit. */
	private RelativeLayout rl;
	private boolean actionAddComment = false;
	private BaseAdapter baseAdapter;
	ProjectDetailReportDialog projectDetailReportDialog;
	List<BaseItem> baseItems = new ArrayList<BaseItem>();
	Animation fadeOut, fadeIn;
	private LinearLayout header;
	private RelativeLayout footerform;
	public ProjectDetailReportdetailDialog(Context context, OnClickListener clickListener, ProjectDetailReportDialog projectDetailReportDialog) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.projectDetailReportDialog = projectDetailReportDialog;
	}

	private ProgressDialog progressDialog;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_reportdetail_m_main));
		credit = new WebView(getActivity());
		credit.getSettings().setPluginState(PluginState.ON);
		credit.getSettings().setAllowFileAccess(true);
		credit.setHorizontalScrollBarEnabled(false);
		credit.setBackgroundColor(0x00000000);
		credit.setBackgroundResource(R.drawable.transfer);
		credit.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

		// credit.setWebViewClient(new WebViewClient() {
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// view.setBackgroundColor(0x00000000);
		// if (Build.VERSION.SDK_INT >= 11)
		// view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		// }
		// });
		// rl = (RelativeLayout) findViewById(R.id.reportdetail_desc_txt);
		// rl.addView(credit);
		View header2 = getLayoutInflater().inflate(R.layout.projectdetail_reportheaderdetail, null);
		rl = (RelativeLayout) header2.findViewById(R.id.reportdetail_desc_txt);
		rl.addView(credit);
		projectId = Integer.toString(ProjectDetailActivity.projectId);
		project_report_no = ProjectDetailActivity.project_report_no;
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				baseItems.clear();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						// CommonAndroid.showDialog(getActivity(), "data:" +
						// jsonObject.toString() , null);
						if (jsonObject != null) {
							if (actionAddComment) {
								actionAddComment = false;
								if (jsonObject.getString("is_login").equalsIgnoreCase("false")) {
									CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
								}
							}
							// ((TextView)
							// findViewById(R.id.reportdetail_title_txt)).setText(jsonObject.getString("report_title"));

							// ((TextView)
							// findViewById(R.id.reportdetail_postdate_txt)).setText(getActivity().getString(R.string.report_postdate)
							// + jsonObject.getString("post_date"));
							String title = jsonObject.getString("report_title");
							String date_post = getActivity().getString(R.string.report_postdate) + jsonObject.getString("post_date");
							updataDetail(jsonObject.getString("report_body"), title, date_post);
							try {
								JSONArray array = jsonObject.getJSONArray("comment_data");
								int k = 0;
								for (int i = 0; i < array.length(); i++) {
									k++;
									if (k < 10) {
										array.getJSONObject(i).put("key_value", "0" + k);
									} else {
										array.getJSONObject(i).put("key_value", k);
									}
									array.getJSONObject(i).put("key_value2", i);
									baseItems.add(new BaseItem(array.getJSONObject(i)));
								}
								if (array.length() == 0) {
									baseItems.add(null);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								baseItems.add(null);
								e.printStackTrace();
							}

						}

					}
					
				} catch (Exception e) {
					footerform.setVisibility(View.VISIBLE);
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
					progressDialog.dismiss();
				}
			}

			@Override
			public void onError(String message) {
				footerform.setVisibility(View.VISIBLE);
				CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
				progressDialog.dismiss();
			}
		};
		sendData = new HashMap<String, String>();
		sendData.put("p_id", projectId);
		sendData.put("r_no", project_report_no);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				progressDialog = new BynalProgressDialog(getActivity());
				progressDialog.show();
				new APICaller(getActivity()).callApi("/project/detailRepost", false, callbackAPI, sendData);
			}
		}, 500);
		pDetailReportdetailview = (ListView) findViewById(R.id.listview_reportdetail);
		header2.setBackgroundColor(0x00000000);
		pDetailReportdetailview.addHeaderView(header2);
		pDetailReportdetailview.setBackgroundColor(0x00000000);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectDetailReportdetailItemView projectDetailReportdetailItemView = new ProjectDetailReportdetailItemView(context);
				// projectDetailReportdetailItemView.addProjectDetailFragment(ProjectDetailReportdetailFragment.this);
				return projectDetailReportdetailItemView;
			}
		};
		pDetailReportdetailview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		pDetailReportdetailview.setAdapter(baseAdapter);
		findViewById(R.id.reportdetail_footer_btn).setOnClickListener(this);
		
		header = (LinearLayout) findViewById(R.id.reportdetail_top);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_zoom_in);/*v3_slide_down_search*/
		fadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
//		header.startAnimation(fadeOut);
		footerform = (RelativeLayout) findViewById(R.id.reportdetail_footer);
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_reportdetail_header);
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
				projectDetailReportDialog.dismiss();
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_report);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_reportdetail_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_reportdetail;
	}

	@Override
	public void onClick(View v) {
		AccountDB accountDB = new AccountDB(getActivity());
		String token_user = accountDB.getToken();
		if (ByUtils.isBlank(token_user)) {
			CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
		} else {
			if (getTextStr(R.id.reportdetail_footer_text).equalsIgnoreCase("")) {
				CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.report_addcomment_err), null);
			} else {
				// add comment
				actionAddComment = true;
				sendData = new HashMap<String, String>();
				sendData.put("p_id", projectId);
				sendData.put("r_no", project_report_no);
				sendData.put("token", token_user);
				sendData.put("add_comment", getTextStr(R.id.reportdetail_footer_text));
				new APICaller(getActivity()).callApi("/project/detailRepost", true, callbackAPI, sendData);
				((TextView) findViewById(R.id.reportdetail_footer_text)).setText("");
			}
		}
	}

	private Context getActivity() {
		return getContext();
	}
 
	private void updataDetail(String data, String title, String date) {
		try {
			data = data.replaceAll("<div>&nbsp;</div>", "");
			data = data.replaceAll("&nbsp;", "");
			data = data.replaceAll("<br />", "<br/>");
			data = data.replaceAll("<br/>", "");
			data = data.replaceAll("<p></p>", "");
			data = data.replaceAll("\r\n", "\n");
			for (int i = 0; i < 10; i++) {
				data = data.replaceAll("\n\n", "\n");
			}
			data = data.replaceAll("\n", "<br />");
//			CommonAndroid.showDialog(getActivity(), data , null);
			String str = "";
			str += loadResouceHTMLFromAssets("project_detail_header2.html");
			str += "<font style=\"font-weight: bold;\">" + title + "</font><br />";
			str += date + "<br />";
			str += data + "<br />";
			str += "<hr /><br />";
			str += loadResouceHTMLFromAssets("project_detail_footer.html");
			credit.loadDataWithBaseURL("file:///android_asset/", str, "text/html", "UTF-8", "");
			credit.setWebChromeClient(new WebChromeClient() {
			});

			credit.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return false;
				}

				@SuppressLint("NewApi")
				public void onPageFinished(WebView view, String url) {
					view.setBackgroundColor(0x00000000);
					if (Build.VERSION.SDK_INT >= 11)
						view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

					// progressDialog.dismiss();
					baseAdapter.clear();
					baseAdapter.addAllItems(baseItems);
					baseAdapter.notifyDataSetChanged();
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							progressDialog.dismiss();
							pDetailReportdetailview.setVisibility(View.VISIBLE);
							footerform.setVisibility(View.VISIBLE);
						}
					}, 500);
				}
			});
			int secondsDelayed = 60;
	        new Handler().postDelayed(new Runnable()
	        {
                public void run()
                {
                	Log.e("TIME:report:", System.currentTimeMillis() + "");             
                    credit.stopLoading();
                }
	        }, secondsDelayed * 1000);
		} catch (Exception e) {
			progressDialog.dismiss();
			footerform.setVisibility(View.VISIBLE);
		} catch (OutOfMemoryError e) {
			progressDialog.dismiss();
			footerform.setVisibility(View.VISIBLE);
		}
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
				br.close(); // stop reading
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tmp;
	}

}