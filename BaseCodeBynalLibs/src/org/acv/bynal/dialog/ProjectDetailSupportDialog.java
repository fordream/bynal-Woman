package org.acv.bynal.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectDetailSupportItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import a.com.acv.bynal.v3.dialog.A47_Comfirm;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.MbaseAdialog;

public class ProjectDetailSupportDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ListView pDetailSupportview;
	private BaseAdapter baseAdapter;
	LinearLayout support_info_block;

	public ProjectDetailSupportDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View header2 = getLayoutInflater().inflate(R.layout.projectdetail_support_header, null);
		support_info_block = (LinearLayout) header2.findViewById(R.id.support_info);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_support_m_main));
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<BaseItem> baseItems = new ArrayList<BaseItem>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						// CommonAndroid.showDialog(getActivity(), "data:" +
						// jsonObject.toString() , null);
						if (jsonObject.getString("status").equalsIgnoreCase("1")) {
							JSONArray array = jsonObject.getJSONArray("array_data");
							for (int i = 0; i < array.length(); i++) {
								baseItems.add(new BaseItem(array.getJSONObject(i)));
							}
							support_info_block.setVisibility(View.VISIBLE);
							String project_info = jsonObject.getString("project");
							JSONObject jsonObject_pjInfo = new JSONObject(project_info);
							((TextView) findViewById(R.id.support_point_txtname)).setText(jsonObject_pjInfo.getString("supported"));
							((TextView) findViewById(R.id.support_flag_txtname)).setText(jsonObject_pjInfo.getString("total_money_support"));
							((TextView) findViewById(R.id.support_percent_txtname)).setText(jsonObject_pjInfo.getString("percent"));
							((TextView) findViewById(R.id.support_timer_txtname)).setText(jsonObject_pjInfo.getString("total_date"));
							((TextView) findViewById(R.id.support_number_txtname)).setText(jsonObject_pjInfo.getString("person"));

						}
					}
					baseAdapter.clear();
					baseAdapter.addAllItems(baseItems);
					baseAdapter.notifyDataSetChanged();

				} catch (Exception e) {
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
			}
		};
		sendData = new HashMap<String, String>();
		sendData.put("id", Integer.toString(ProjectDetailActivity.projectId));
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new APICaller(getActivity()).callApi("/project/listSupport", true, callbackAPI, sendData);
			}
		}, 500);
		pDetailSupportview = (ListView) findViewById(R.id.listview_support);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectDetailSupportItemView projectDetailSupportItemView = new ProjectDetailSupportItemView(context);
				// projectDetailSupportItemView.addProjectManagerFragment(ProjectDetailSupportFragment.this);
				return projectDetailSupportItemView;
			}
		};
		pDetailSupportview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					BaseItem baseItem = (BaseItem) parent.getItemAtPosition(position);
					AccountDB support_info = new AccountDB(getActivity());
					support_info.save("support_info_value", baseItem.getString("value"));
					support_info.save("support_info_desc", baseItem.getString("desc"));
					support_info.save("support_info_offer_date", baseItem.getString("offer_date"));

					support_info.save("support_info_projectid", baseItem.getString("project_id"));
					support_info.save("support_info_returnid", baseItem.getString("id"));

					// close(true);
					if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
						if (ProjectDetailActivity.projectTotalDate > 0) {

							if (ByUtils.isBlank(new AccountDB(getContext()).getToken())) {
								CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_message_pleaselogin), null);
								return;
							}
							new A47_Comfirm(getContext(), null, false) {
								public void gotoHome() {
									ProjectDetailSupportDialog.this.gotoHome();
								};

								public void openItem(final JSONObject jsonObject) {
									new ProjectDetailSupportDetailDialog(getmContext(), new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									}, ProjectDetailSupportDialog.this, jsonObject).show();
								};
							}.show();

						} else {
							CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.project_detail_support_err_projectended), null);
						}

					} else {
						CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_project_unpublic), null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pDetailSupportview.addHeaderView(header2);
		pDetailSupportview.setAdapter(baseAdapter);
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_support_header);
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
				// Intent mIntent = new Intent(getActivity(),
				// BaseTabActivity.class);
				// ProjectDetailActivity.gotoNewPage(mIntent);
				// BaseTabActivity.gotoHome(getActivity());
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_support);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_support_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_support;
	}

	@Override
	public void onClick(View v) {

	}

	private Context getActivity() {
		return getContext();
	}

	/*
	 * public void viewSupportDetail(View v, BaseItem data) { if
	 * ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
	 * AccountDB support_info = new AccountDB(getActivity());
	 * support_info.save("support_info_value", data.getString("value"));
	 * support_info.save("support_info_desc", data.getString("desc"));
	 * support_info.save("support_info_offer_date",
	 * data.getString("offer_date")); ((BaseActivity)
	 * getActivity()).changeFragemt(new ProjectDetailSupportdetailFragment()); }
	 * else { CommonAndroid.showDialog(getActivity(),
	 * getActivity().getResources().getString(R.string.error_project_unpublic),
	 * null); }
	 * 
	 * }
	 */
}