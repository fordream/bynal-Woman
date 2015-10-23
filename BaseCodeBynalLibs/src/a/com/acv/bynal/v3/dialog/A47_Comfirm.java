package a.com.acv.bynal.v3.dialog;

import java.util.HashMap;

import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.LogUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.nbase.MbaseAdialog;

public class A47_Comfirm extends MbaseAdialog {
	static TextView txtErr;
	@Override
	public int getLayout() {
		return R.layout.a47_comfirm;
	}

	public void openItem(JSONObject jsonObject) {

	}

//	private void addOrUpdate(JSONObject jsonObject) {
//		ListView listView = (ListView) findViewById(R.id.listview);
//		if (adapter == null) {
//			JSONArray array = new JSONArray();
//			adapter = new MAdapter(array);
//		}
//
//		adapter.addOrUpdate(jsonObject);
//
//		listView.setAdapter(adapter);
//
//		adapter.notifyDataSetChanged();
//	}

	private boolean isProfile = false;

	public A47_Comfirm(Context context, OnClickListener clickListener, boolean isProfile) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.isProfile = isProfile;
	}

	private MAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.a_bot_to_top_in);
		findViewById(R.id.a47_1_main).startAnimation(animation);
		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		txtErr = (TextView) findViewById(R.id.pj_err_message);
		findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				A47_1Dialog dialog = new A47_1Dialog(getContext(), null, null) {
					@Override
					public void compelete(JSONObject jsonObject) {
						super.compelete(jsonObject);
						callApi();
					}
				};
				dialog.show();
			}
		});

		callApi();

	}

	private void callApi() {
		final ListView listView = (ListView) findViewById(R.id.listview);

		APICaller apiCaller = new APICaller(getContext());
		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("token", new AccountDB(getContext()).getToken());
		apiCaller.callApi("/user/getdelivery", true, new ICallbackAPI() {

			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						txtErr.setVisibility(View.GONE);
						JSONArray jsonArray = jsonObject.getJSONArray("data_delivery");
						listView.setAdapter(adapter = new MAdapter(jsonArray));
					} else if (jsonObject.getString("status").equals("0")) {
						txtErr.setVisibility(View.VISIBLE);
						txtErr.setText(R.string.error_data);
//						CommonAndroid.showDialog(MainHomeActivity.homeActivity, jsonObject.getString("message"), null);
					}
				} catch (JSONException e) {
					CommonAndroid.showDialog(MainHomeActivity.homeActivity, getContext().getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(MainHomeActivity.homeActivity, getContext().getResources().getString(R.string.error_connect_server), null);
			}
		}, sendData);
	}

	private HeaderOption getHeaderOption() {
		HeaderOption headerOption = new HeaderOption(getContext(), TYPEHEADER.NORMAL) {
			@Override
			public void onClickButtonLeft() {
				super.onClickButtonLeft();
				close(false);
			}

			@Override
			public void onClickButtonRight() {
				super.onClickButtonRight();
				close(false);
				gotoHome();
			}
		};
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.a47_comfirm);
		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		return headerOption;
	}

	private void close(boolean b) {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.a_top_to_bot_out);
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
			}
		});
		findViewById(R.id.a47_1_main).startAnimation(animation);
	}

	private class MAdapter extends BaseAdapter {
		private JSONArray jsonArray;

		public MAdapter(JSONArray jsonArray) {
			this.jsonArray = jsonArray;
		}

//		private void addOrUpdate(JSONObject jsonObject) {
//			JSONObject object = null;
//			for (int i = 0; i < jsonArray.length(); i++) {
//				try {
//					if (jsonArray.getJSONObject(i).getString("delivery_id").equals(jsonObject.getString("delivery_id"))) {
//						object = jsonArray.getJSONObject(i);
//					}
//				} catch (Exception exception) {
//
//				}
//			}
//
//			if (object != null) {
//				try {
//					object.put("delivery_name", jsonObject.getString("delivery_name"));
//					object.put("delivery_zip1", jsonObject.getString("delivery_zip1"));
//					object.put("delivery_zip2", jsonObject.getString("delivery_zip2"));
//					object.put("delivery_address", jsonObject.getString("delivery_address"));
//					object.put("delivery_phone", jsonObject.getString("delivery_phone"));
//				} catch (Exception exception) {
//
//				}
//
//			} else {
//				try {
//					jsonArray.put(0, jsonObject);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}

		@Override
		public int getCount() {
			return jsonArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			try {
				return jsonArray.get(arg0);
			} catch (JSONException e) {
				return null;
			}
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.a47comfirm_item, null);
			}
			final JSONObject jsonObject = (JSONObject) getItem(position);
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isProfile) {
						A47_1Dialog dialog = new A47_1Dialog(getContext(), null, jsonObject) {
							public void compelete(JSONObject jsonObject) {
								callApi();
							};
						};
						dialog.show();
					} else {
						close(false);
						openItem(jsonObject);
					}
				}

			});

			if (jsonObject != null) {
				try {
					((TextView) convertView.findViewById(R.id.a47comfirm_item_1)).setText(jsonObject.getString("delivery_name"));
					((TextView) convertView.findViewById(R.id.a47comfirm_item_2)).setText(jsonObject.getString("delivery_zip1") + " - " + jsonObject.getString("delivery_zip2"));
					((TextView) convertView.findViewById(R.id.a47comfirm_item_3)).setText(jsonObject.getString("delivery_address"));
					((TextView) convertView.findViewById(R.id.a47comfirm_item_4)).setText(jsonObject.getString("delivery_phone"));
				} catch (Exception exception) {

				}
			}
			return convertView;
		}

	}
}