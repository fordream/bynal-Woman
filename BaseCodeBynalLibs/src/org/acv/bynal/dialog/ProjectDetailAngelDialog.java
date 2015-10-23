package org.acv.bynal.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectDetailAngelItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.nbase.MbaseAdialog;

public class ProjectDetailAngelDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	GridView pDetailAngelview;
	public static long onloadImg = 0;
	TextView err_txt;
	public ProjectDetailAngelDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private BaseAdapter baseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_angel_m_main));
		err_txt = (TextView) findViewById(R.id.pj_err_message);
		err_txt.setVisibility(View.GONE);
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<BaseItem> baseItems = new ArrayList<BaseItem>();
				baseItems.clear();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						// CommonAndroid.showDialog(getActivity(), "data:" +
						// jsonObject.toString() , null);
						if (jsonObject != null) {
							if (jsonObject.getString("status").equalsIgnoreCase("1")) {
								JSONArray array = jsonObject.getJSONArray("array_data");
								for (int i = 0; i < array.length(); i++) {
									baseItems.add(new BaseItem(array.getJSONObject(i)));
								}
							}
						}
						baseAdapter.clear();
						baseAdapter.addAllItems(baseItems);
						baseAdapter.notifyDataSetChanged();
					}

				} catch (Exception e) {
					// Log.e("PD","err::" + e.getMessage());
//					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
					err_txt.setVisibility(View.VISIBLE);
					err_txt.setText( getActivity().getResources().getString(R.string.pj_detail_angel_null));
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
				new APICaller(getActivity()).callApi("/project/listAngel", true, callbackAPI, sendData);
			}
		}, 500);

		pDetailAngelview = (GridView) findViewById(R.id.gridview_angel);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectDetailAngelItemView projectDetailAngelItemView = new ProjectDetailAngelItemView(context);
				// projectDetailAngelItemView.addProjectManagerFragment(ProjectDetailAngelFragment.this);
				return projectDetailAngelItemView;
			}
		};
		pDetailAngelview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// BaseItem baseItem = (BaseItem) parent
				// .getItemAtPosition(position);
			}
		});
		pDetailAngelview.setAdapter(baseAdapter);
		onloadImg = System.currentTimeMillis();
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_angel_header);
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
				close(true);
				// Intent mIntent = new Intent(getActivity(),
				// BaseTabActivity.class);
				// ProjectDetailActivity.gotoNewPage(mIntent);
				//
				// BaseTabActivity.gotoHome(getActivity());
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_angel);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_angel_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_angel;
	}

	@Override
	public void onClick(View v) {

	}

	private Context getActivity() {
		return getContext();
	}
}