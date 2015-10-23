package org.acv.bynal.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectDetailCommentItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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

public class ProjectDetailCommentDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ListView pDetailCommentview;
	private int total_pages = 1;
	private int more_pages = 1;
	private boolean isLoadMore = false;
	public static long onloadImg = 0;
	List<BaseItem> baseItems = new ArrayList<BaseItem>();
	TextView err_txt;
	private BaseAdapter baseAdapter;

	public ProjectDetailCommentDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_comment_m_main));
		err_txt = (TextView) findViewById(R.id.pj_err_message);
		err_txt.setVisibility(View.GONE);
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				// List<BaseItem> baseItems = new ArrayList<BaseItem>();
				if (more_pages == 1) {
					baseItems.clear();
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						if (jsonObject != null) {
							// CommonAndroid.showDialog(getActivity(),
							// "index_pages:" + more_pages + ":data:" +
							// jsonObject.toString(), null);
							if (jsonObject.getString("status").equalsIgnoreCase("1")) {
								total_pages = jsonObject.getInt("total_pages");
								if (total_pages > 1) {
									isLoadMore = true;
									if (more_pages <= total_pages) {
										more_pages++;
									}
								}
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
					err_txt.setText( getActivity().getResources().getString(R.string.pj_detail_comment_null));
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
			}
		};
		sendData = new HashMap<String, String>();
		sendData.put("id", Integer.toString(ProjectDetailActivity.projectId));
		sendData.put("page", "1");
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new APICaller(getActivity()).callApi("/project/listComment", true, callbackAPI, sendData);
			}
		}, 500);
		pDetailCommentview = (ListView) findViewById(R.id.listview_comment);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectDetailCommentItemView projectDetailCommentItemView = new ProjectDetailCommentItemView(context);
				// projectDetailCommentItemView.addProjectManagerFragment(ProjectDetailCommentFragment.this);
				return projectDetailCommentItemView;
			}
		};
		pDetailCommentview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// BaseItem baseItem = (BaseItem) parent
				// .getItemAtPosition(position);
			}
		});
		pDetailCommentview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (totalItemCount > 0 && isLoadMore) {
					if (totalItemCount - 2 <= firstVisibleItem + visibleItemCount) {
						if (more_pages <= total_pages) {
							// CommonAndroid.showDialog(getActivity(),
							// "load more" + more_pages, null);
							isLoadMore = false;
							sendData = new HashMap<String, String>();
							sendData.put("id", Integer.toString(ProjectDetailActivity.projectId));
							sendData.put("page", Integer.toString(more_pages));
							new APICaller(getActivity()).callApi("/project/listComment", true, callbackAPI, sendData);
						}
					}
				}

			}

		});
		pDetailCommentview.setAdapter(baseAdapter);
		onloadImg = System.currentTimeMillis();
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_comment_header);
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
				//
				// BaseTabActivity.gotoHome(getActivity());
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_comment);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_comment_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_comment;
	}

	@Override
	public void onClick(View v) {

	}

	private Context getActivity() {
		return getContext();
	}
}