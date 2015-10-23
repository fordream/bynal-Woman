package org.acv.bynal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.MessageService;
import org.acv.bynal.activity.MessageService.STATUSSERVICE;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.LoadingView;
import org.acv.bynal.views.MessageItemView;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.lazyload.ImageLoader2;
import com.acv.libs.nbase.BaseFragment;

@SuppressLint("NewApi")
public class MessageFragment extends BaseFragment {
	ListView messagelistview;
	private LoadingView message_loadingview;
	public static ImageLoader2 imageLoader; 
	public MessageFragment() {
		super();
	}

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		imageLoader=new ImageLoader2(getActivity());
		MessageService.updateMessageRoom(getActivity());
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			STATUSSERVICE status = (STATUSSERVICE) intent.getSerializableExtra(MessageService.KEY_STATUS);
			if (status == STATUSSERVICE.ERROR) {
				// onError(intent.getStringExtra(MessageService.KEY_MESSAGE));
				message_loadingview.endProgressBar(intent.getStringExtra(MessageService.KEY_MESSAGE));
			} else {
				List<BaseItem> baseItems = new ArrayList<BaseItem>();
				try {
					String response = intent.getStringExtra(MessageService.KEY_VALUES);
					JSONObject jsonObject = new JSONObject(response);

					if (jsonObject.getString("status").equals("1")) {
						JSONArray array = jsonObject.getJSONArray("array_data");
						for (int i = 0; i < array.length(); i++) {
							baseItems.add(new BaseItem(array.getJSONObject(i)));
						}
						message_loadingview.endProgressBar("");
					} else {
						if (!ByUtils.isLogin(jsonObject)) {
							relaseTooken();
						} else {
							// onError(jsonObject.getString("message"));
							message_loadingview.endProgressBar(jsonObject.getString("message"));
						}
					}
				} catch (Exception e) {
					message_loadingview.endProgressBar(getString(R.string.error_message_connect_server_fail));
				}

				baseAdapter.clear();
				baseAdapter.addAllItems(baseItems);
				baseAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		getView().findViewById(R.id.mesage_block).setVisibility(View.GONE);
		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(MessageService.ACTION_UPDATE_ROOM_LIST));
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void callMessageActivity(String id) {
		MainHomeActivity.saveBimap();
		BaseTabActivity.senBroadCastMessage(getActivity(), id);
	}

	@Override
	public void setUpFragment(final View view) {

		ImageLoaderUtils.getInstance(getActivity()).clear();
		message_loadingview = (LoadingView) view.findViewById(R.id.message_loadingview);
		// message_loadingview.endProgressBar("");
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		view.findViewById(R.id.mesage_block).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_message_item_shooser_close);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						view.findViewById(R.id.mesage_block).setVisibility(View.GONE);
					}
				});
				view.findViewById(R.id.mesage_block_content).startAnimation(animation);

			}
		});
		messagelistview = (ListView) view.findViewById(R.id.messagelistview);
		baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale);
				view.startAnimation(animation);
				
				return view;
			}

			@Override
			public BaseView getView(Context context, Object data) {
				final MessageItemView messageItemView = new MessageItemView(context);
				messageItemView.addMessageFragment(MessageFragment.this);
				return messageItemView;
			}
		};

		messagelistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BaseItem baseItem = (BaseItem) parent.getItemAtPosition(position);
				String user_id = baseItem.getString("user_id");

				String block_flg = baseItem.getString("block_flg");
				String spam_flg = baseItem.getString("spam_flg");
				String hide_flg = baseItem.getString("hide_flg");

				if ("0".equals(block_flg) && "0".equals(spam_flg) && "0".equals(hide_flg)) {
					callMessageActivity(user_id);
				}
			}
		});
		messagelistview.setAdapter(baseAdapter);

	}

	@Override
	public int layoytResurce() {
		return R.layout.message;
	}

	private BaseAdapter baseAdapter;

	public void showController(int top, final BaseItem data) {

		if (top < 0)
			top = 0;

		int height = (int) getResources().getDimension(R.dimen.dimen_50dp);
		top = top + height;

		getView().findViewById(R.id.mesage_block).setVisibility(View.VISIBLE);
		View view = getView().findViewById(R.id.mesage_block_content);
		view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.v3_message_item_shooser));
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		layoutParams.topMargin = top;

		view.setLayoutParams(layoutParams);

		String block_flg = data.getString("block_flg");
		String spam_flg = data.getString("spam_flg");
		String hide_flg = data.getString("hide_flg");

		if (!"0".equals(block_flg)) {
			getView().findViewById(R.id.item1_img).setBackgroundResource(R.drawable.message_menu_1);
		} else {
			getView().findViewById(R.id.item1_img).setBackgroundResource(R.drawable.message_menu_1_close);
		}

		if (!"0".equals(hide_flg)) {
			getView().findViewById(R.id.item2_img).setBackgroundResource(R.drawable.message_menu_2);
		} else {
			getView().findViewById(R.id.item2_img).setBackgroundResource(R.drawable.message_menu_2_close);
		}

		if (!"0".equals(spam_flg)) {
			getView().findViewById(R.id.item3_img).setBackgroundResource(R.drawable.message_menu_3);
		} else {
			getView().findViewById(R.id.item3_img).setBackgroundResource(R.drawable.message_menu_3_close);
		}

		setTextStr(R.id.item1, !block_flg.equals("1") ? getString(R.string.block) : getString(R.string.unblock));
		setTextStr(R.id.item2, !hide_flg.equals("1") ? getString(R.string.hide) : getString(R.string.unhide));
		setTextStr(R.id.item3, !spam_flg.equals("1") ? getString(R.string.spam) : getString(R.string.unspam));

		getView().findViewById(R.id.item1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onChange(data, 0);
			}
		});

		getView().findViewById(R.id.item2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onChange(data, 1);
			}
		});

		getView().findViewById(R.id.item3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onChange(data, 2);
			}

		});
	}

	private void onChange(BaseItem data, int index) {
		getView().findViewById(R.id.mesage_block).setVisibility(View.GONE);
		String block_flg = data.getString("block_flg");
		String spam_flg = data.getString("spam_flg");
		String hide_flg = data.getString("hide_flg");

		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("token", new AccountDB(getActivity()).getToken());
		sendData.put("user_id", data.getString("user_id"));
		sendData.put("room", data.getString("room_no"));

		// block_flg
		if (index == 0) {
			sendData.put("type", "0");
			sendData.put("value", block_flg.equals("1") ? "0" : "1");
		} else if (index == 1) {
			// spam_flg
			sendData.put("type", "1");
			sendData.put("value", hide_flg.equals("1") ? "0" : "1");
		} else if (index == 2) {
			// spam_flg
			sendData.put("type", "2");
			sendData.put("value", spam_flg.equals("1") ? "0" : "1");
		}

		APICaller apiCaller = new APICaller(getActivity());

		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<BaseItem> baseItems = new ArrayList<BaseItem>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						JSONArray array = jsonObject.getJSONArray("array_data");
						for (int i = 0; i < array.length(); i++) {
							baseItems.add(new BaseItem(array.getJSONObject(i)));
						}
					} else {
						onError(jsonObject.getString("message"));
					}

				} catch (Exception e) {
				}

				baseAdapter.clear();
				baseAdapter.addAllItems(baseItems);
				baseAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), message + "", null);
			}
		};

		apiCaller.callApi("/messages/status", true, callbackAPI, sendData);
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		//
		headerOption.setResHeader(R.string.menu_home_message);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}