package org.acv.bynal.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.ProjectTagItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class ProjectDetailTagDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ListView projectlisttagview;
	boolean actionChangeTag = false;
	private BaseAdapter baseAdapter;
	List<BaseItem> baseItems = new ArrayList<BaseItem>();
	AccountDB accountDB = new AccountDB(getActivity());
	final String token_user = accountDB.getToken();
	TextView txt_tag;
	LinearLayout viewcontent;

	public ProjectDetailTagDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_tag_m_main));
		onLoad();

		findViewById(R.id.tagpost_footer_btn).setOnClickListener(this);
		txt_tag = (TextView) findViewById(R.id.tagpost_footer_text);
		sendData = new HashMap<String, String>();
		sendData.put("project_id", Integer.toString(ProjectDetailActivity.projectId));
		viewcontent = (LinearLayout) findViewById(R.id.tagpost_footer);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new APICaller(getActivity()).callApi("/project/tag", true, callbackAPI, sendData);
			}
		}, 500);

	}

	private void onLoad() {
		projectlisttagview = (ListView) findViewById(R.id.listview_tag);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectTagItemView projectTagItemView = new ProjectTagItemView(context);
				projectTagItemView.addProjectManagerFragment(getActivity());
				return projectTagItemView;
			}
		};

		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				baseItems.clear();
				try {
					viewcontent.setVisibility(View.VISIBLE);
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						// CommonAndroid.showDialog(getActivity(), "data:" +
						// jsonObject.toString() , null);
						if (jsonObject.getString("status").equalsIgnoreCase("1")) {
							if (jsonObject != null) {
								try {
									JSONArray array = jsonObject.getJSONArray("array_data");
									for (int i = (array.length() - 1); i >= 0; i--) {
										baseItems.add(new BaseItem(array.getJSONObject(i)));
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							baseAdapter.clear();
							baseAdapter.addAllItems(baseItems);
							baseAdapter.notifyDataSetChanged();
							if (actionChangeTag) {
								if (!jsonObject.getBoolean("is_login")) {
									CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_session_login), null);
									actionChangeTag = false;
								} else {
									if (jsonObject.has("add")) {
										if (jsonObject.getString("add").equalsIgnoreCase("false")) { // add
																										// err
											CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.tag_add_error), null);
											actionChangeTag = false;
										}
									}

								}
							}
						}
					}

				} catch (JSONException e) {
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				viewcontent.setVisibility(View.VISIBLE);
				CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
			}
		};
		projectlisttagview.setAdapter(baseAdapter);
		projectlisttagview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final String tag_id_delete = baseItems.get(position).getString("id");
				// CommonAndroid.showDialog(getActivity(),"3333333333" +
				// position +"::" + tag_id_delete, null);
				if (ByUtils.isBlank(token_user)) {
					CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
				} else {
					CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.tag_delete_confirm), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// delete tag
							actionChangeTag = true;
							sendData = new HashMap<String, String>();
							sendData.put("project_id", Integer.toString(ProjectDetailActivity.projectId));
							sendData.put("remove_tag", tag_id_delete);
							sendData.put("add_tag", "");
							sendData.put("token", token_user);
							new APICaller(getActivity()).callApi("/project/tag", true, callbackAPI, sendData);
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Cancel
						}
					});

				}

			}
		});

	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_tag_header);
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
		headerOption.setResHeader(R.string.header_project_detail_tag);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_tag_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_tag;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tagpost_footer_btn:
			InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
			if (ByUtils.isBlank(token_user)) {
				CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
			} else {
				if (getTextStr(R.id.tagpost_footer_text).equalsIgnoreCase("")) {
					CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.tag_add_error), null);
				} else {
					// add tag
					// onLoad();
					actionChangeTag = true;
					sendData = new HashMap<String, String>();
					sendData.put("project_id", Integer.toString(ProjectDetailActivity.projectId));
					sendData.put("token", token_user);
					sendData.put("add_tag", getTextStr(R.id.tagpost_footer_text));
					new APICaller(getActivity()).callApi("/project/tag", true, callbackAPI, sendData);
					txt_tag.setText("");
				}
			}
			break;
		}

	}

	private Context getActivity() {
		return getContext();
	}

	public void actionDeleteTag(final BaseItem data) {
		onLoad();
		final String tag_id_delete = data.getString("id");
		// int type = v.getId();
		// switch (type) {
		// case R.id.tag_delete:
		if (ByUtils.isBlank(token_user)) {
			CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
		} else {
			CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.tag_delete_confirm), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// delete tag
					actionChangeTag = true;
					sendData = new HashMap<String, String>();
					sendData.put("project_id", Integer.toString(ProjectDetailActivity.projectId));
					sendData.put("remove_tag", tag_id_delete);
					sendData.put("add_tag", "");
					sendData.put("token", token_user);
					new APICaller(getActivity()).callApi("/project/tag", true, callbackAPI, sendData);
				}
			}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Cancel
				}
			});

		}

		// break;
		// }

	}
}