package org.acv.bynal.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectmanagerActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.fragment.ProjectManagerFragment;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.views.DeliveryListItemView;
import org.acv.bynal.views.HeaderView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
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

public class ProjectManagerDeliveryDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	protected static ProjectManagerDeliveryDialog ProjectManagerDeliveryDialog = null;
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	String token_user;
	ListView deliverylistview;
	static TextView txtErr;

	public ProjectManagerDeliveryDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		// findViewById(R.id.profile_user_btnchangepass).setOnClickListener(this);
		openPopActivity(findViewById(R.id.pm_delivery_m_main));
		deliverylistview = (ListView) findViewById(R.id.listview_delivery);
		txtErr = (TextView) findViewById(R.id.pm_err_message);
		onLoad();
		ProjectManagerDeliveryDialog = this;
	}

	private BaseAdapter baseAdapter;

	private void onLoad() {
		AccountDB accountDB = new AccountDB(getActivity());
		token_user = accountDB.getToken();
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<BaseItem> baseItems = new ArrayList<BaseItem>();
				baseItems.clear();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						JSONArray array = jsonObject.getJSONArray("array_data");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								baseItems.add(new BaseItem(array.getJSONObject(i)));
							}
						} else {
							txtErr.setVisibility(View.VISIBLE);
							txtErr.setText(getContext().getResources().getString(R.string.error_pm_delivery_return_null));
						}
					}
					baseAdapter.clear();
					baseAdapter.addAllItems(baseItems);
					baseAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					txtErr.setVisibility(View.VISIBLE);
					txtErr.setText(getContext().getResources().getString(R.string.error_pm_delivery_return_null));
					Log.e("PM", "err::" + e.getMessage());
				}
			}

			@Override
			public void onError(String message) {

			}
		};
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final DeliveryListItemView deliveryListItemView = new DeliveryListItemView(context);
				deliveryListItemView.addProjectManagerFragment(ProjectManagerDeliveryDialog);// abc
				return deliveryListItemView;
			}
		};
		deliverylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BaseItem baseItem = (BaseItem) parent.getItemAtPosition(position);

			}
		});
		txtErr.setVisibility(View.GONE);
		deliverylistview.setAdapter(baseAdapter);
		sendData = new HashMap<String, String>();
		sendData.put("project_id", ProjectManagerFragment.project_id);
		sendData.put("token", token_user);
		new APICaller(getActivity()).callApi("/project/deliverylist", true, callbackAPI, sendData);
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pm_delivery_header);
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
		headerOption.setResHeader(R.string.header_project_delivery);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pm_delivery_m_main), new AnimationAction() {

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
		return R.layout.projectmanager_delivery;
	}

	@Override
	public void onClick(View v) {

	}

	private Context getActivity() {
		return getContext();
	}

	public void actionProjectDeliverySendMessage(View v, final BaseItem data) {
		String user_id = data.getString("user_id");
		// CommonAndroid.showDialog(getActivity(), "user_id:" + user_id, null);
		AccountDB accountDB = new AccountDB(getActivity());
		String user_id_login = accountDB.getUserId();
		// CommonAndroid.showDialog(getActivity(), "user_id:" + user_id +
		// "::user_id_login:" + user_id_login , null);
		if (!user_id.equalsIgnoreCase(user_id_login)) {
			// Intent mIntent = new Intent(getActivity(),
			// MessageActivity.class);
			// mIntent.putExtra("id", user_id);
			// getActivity().startActivityForResult(mIntent,
			// ByUtils.REQUEST_PROJECTMANAGER);
			// MessageActivity.startActivity(getActivity(), user_id);

			MessageActivity.bitmap = MainHomeActivity.getBimap(findViewById(R.id.pm_delivery_m_main));
			MessageActivity.startActivity(getmContext(), user_id);
		} else {
			CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.err_sendmessage_userid_invalid), null);
		}
	}
}