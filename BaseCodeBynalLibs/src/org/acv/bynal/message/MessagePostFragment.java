package org.acv.bynal.message;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseFragment;

@SuppressLint("ResourceAsColor")
public class MessagePostFragment extends BaseFragment {
	private ListView messagepost_listview;
	private BaseAdapter baseAdapter;
	Bitmap avata_default;
	Bitmap postmesage_avatar1 = null;
	Bitmap postmesage_avatar2 = null;
	public static long onloadImg = 0;
	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		avata_default = BitmapFactory.decodeResource(getResources(), R.drawable.default_user2_circle2);
		onloadImg = System.currentTimeMillis();
		loadPost();
	}

	public MessagePostFragment() {
		super();
	}

	@Override
	public void setUpFragment(View view) {
		messagepost_listview = (ListView) view.findViewById(R.id.messagepost_listview);
		messagepost_listview.setAdapter((baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale);
				view.startAnimation(animation);
				return view;
			}

			@Override
			public BaseView getView(Context context, Object data) {
				return new PostMessageItemView(context);
			}
		}));

		view.findViewById(R.id.messagepost_footer_btn).setOnClickListener(this);

	}

	private void loadPost() {
		String id = getData().toString();
		APICaller apiCaller = new APICaller(getActivity());
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("user_id", id);
		sendData.put("token", new AccountDB(getActivity()).getToken());
		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				updateScreen(false, response);
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), message + "", null);
			}
		};
		apiCaller.callApi("/messages/post", true, callbackAPI, sendData);
	}

	@Override
	public void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter(ByUtils.ACTION_REFRESH_SCREEN_MESSAGE);
		getActivity().registerReceiver(broadcastReceiver, filter);

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			loadPost();
		}

	};

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	@Override
	public int layoytResurce() {
		return R.layout.messagepost;
	}

	@Override
	public void onClick(View v) {

		if (ByUtils.isBlank(getTextStr(R.id.messagepost_footer_text))) {

			CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_input_message), null);
			return;
		}
		APICaller apiCaller = new APICaller(getActivity());
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("user_id", getData().toString());
		sendData.put("message", getTextStr(R.id.messagepost_footer_text));
		sendData.put("token", new AccountDB(getActivity()).getToken());
		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				updateScreen(true, response);
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), message + "", null);
			}
		};

		apiCaller.callApi("/messages/post", true, callbackAPI, sendData);
		setTextStr(R.id.messagepost_footer_text, "");
	}

	private void updateScreen(boolean isSendMessage, String response) {
		if (isSendMessage) {

		}

		BaseItem baseItem = new BaseItem(response);
		String status = baseItem.getString("status");

		if ("1".equals(status)) {
			if (baseItem.getObject("array_data") != null) {
				JSONArray jsonArray = (JSONArray) baseItem.getObject("array_data");
				List<Object> list = new ArrayList<Object>();

				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						list.add(new BaseItem(jsonArray.getJSONObject(i)));
					} catch (JSONException e) {
					}
				}
				baseAdapter.clear();
				baseAdapter.addAllItems(list);
				baseAdapter.notifyDataSetChanged();
			}
		}
	}

	private class PostMessageItemView extends BaseView {

		public PostMessageItemView(Context context) {
			super(context);
			init(R.layout.postmessageitem);
		}

		@Override
		public void refresh() {
			super.refresh();
			BaseItem baseItem = (BaseItem) getData();
			setTextStr(R.id.postmesage_textview1, baseItem.getString("user_name"));
			setTextStr(R.id.postmesage_textview2, baseItem.getString("date"));
			setTextStr(R.id.postmesage_textview3, baseItem.getString("message"));

			String url = baseItem.getString("user_img");

//			ImageLoaderUtils.getInstance(getContext()).DisplayImage(url, (ImageView) findViewById(R.id.postmesage_avatar_img),
//					BitmapFactory.decodeResource(getResources(), R.drawable.default_user2_circle2));
//			ImageLoaderUtils.getInstance(getContext()).DisplayImage(url, (ImageView) findViewById(R.id.postmesage_avatar_img2),
//					BitmapFactory.decodeResource(getResources(), R.drawable.default_user2_circle2));

			ImageView mesage_main_icon = (ImageView) findViewById(R.id.postmesage_avatar_img);
			ImageView mesage_main_icon2 = (ImageView) findViewById(R.id.postmesage_avatar_img2);
			
			String user_id = baseItem.getString("user_id");
			String myId = new AccountDB(getContext()).getUserId();

			if (!user_id.equals(myId)) {
				findViewById(R.id.postmesage_avatar1).setVisibility(View.VISIBLE);
				findViewById(R.id.postmesage_avatar2).setVisibility(View.GONE);

				findViewById(R.id.v3_message_note1).setBackgroundResource(R.drawable.v3_message_note1);
				/**
				 * 
				 */
				setTextColor(R.id.postmesage_textview1, R.color.v3_3333333);
				setTextColor(R.id.postmesage_textview2, R.color.v3_3333333);
				setTextColor(R.id.postmesage_textview3, R.color.v3_3333333);
				if(postmesage_avatar1 == null){
					//postmesage_avatar1 = MessageActivity.imageLoader.getBitmap2(url);
					Bitmap avataload = MessageActivity.imageLoader.DisplayImageAvata(url, mesage_main_icon, avata_default);
					if(avataload != null){
						postmesage_avatar1 = avataload;
					}
				}
				if(postmesage_avatar1 != null){
					mesage_main_icon.setImageBitmap(postmesage_avatar1);
				}
				
				
			} else {
				findViewById(R.id.postmesage_avatar1).setVisibility(View.GONE);
				findViewById(R.id.postmesage_avatar2).setVisibility(View.VISIBLE);
				findViewById(R.id.v3_message_note1).setBackgroundResource(R.drawable.v3_message_note2);
				/**
				 * 
				 */
				setTextColor(R.id.postmesage_textview1, android.R.color.white);
				setTextColor(R.id.postmesage_textview2, android.R.color.white);
				setTextColor(R.id.postmesage_textview3, android.R.color.white);
//				MessageActivity.imageLoader.DisplayImageSetDefault(url, mesage_main_icon2, R.drawable.default_user2_circle2);
				if(postmesage_avatar2 == null){
					Bitmap avataload = MessageActivity.imageLoader.DisplayImageAvata(url, mesage_main_icon2, avata_default);
					if(avataload != null){
						postmesage_avatar2 = avataload;
					}
				}
				if(postmesage_avatar2 != null){
					mesage_main_icon2.setImageBitmap(postmesage_avatar2);
				}
			}
		}

		private void setTextColor(int resText, int color) {
			((TextView) findViewById(resText)).setTextColor(getContext().getResources().getColor(color));
		}
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		if (type_message == 1) {
			headerOption.setShowButtonRight(false);
		} else {
			headerOption.setShowButtonRight(true);
		}
		return headerOption;
	}

	private int type_message = 0;

	public void setMessageType(int type_message) {
		this.type_message = type_message;
	}

}