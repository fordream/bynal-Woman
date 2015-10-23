package org.acv.bynal.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;

public class MessageService extends Service {
	public static final String ACTION_UPDATE_ROOM_LIST = "org.acv.bynal.activity.MessageService.ACTION_UPDATE_ROOM_LIST";
	public static final String ACTION_UPDATE_CHAT = "org.acv.bynal.activity.MessageService.ACTION_UPDATE_CHAT";

	public static final String KEY_STATUS = "KEY_STATUS";
	public static final String KEY_MESSAGE = "KEY_MESSAGE";
	public static final String KEY_VALUES = "KEY_VALUES";

	public static void updateMessageRoom(Context context) {
		Intent service = new Intent(context, MessageService.class);
		service.putExtra(KEY_STATUS, MESSAGETYPE.LOADROOMLIST);
		context.startService(service);
	}

	public enum MESSAGETYPE {
		LOADROOMLIST, UPDATEROOMSTATUS, POSTMESSAGE, UPDATEMESSAGE
	}

	public enum STATUSSERVICE {
		SUCCCESS, ERROR
	}

	public MessageService() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			MESSAGETYPE type = (MESSAGETYPE) intent
					.getSerializableExtra(KEY_STATUS);
			if (type == MESSAGETYPE.LOADROOMLIST) {
				loadRoomList();
			}
		} catch (Exception exception) {

		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void postMessage(String user_id, String message) {
		APICaller apiCaller = new APICaller(this);
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("user_id", user_id);
		sendData.put("message", message);
		sendData.put("token", new AccountDB(this).getToken());
		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
			}

			@Override
			public void onError(String message) {
			}
		};

		apiCaller.callApi("/messages/post", false, callbackAPI, sendData);
	}

	private void getPostMessage(String user_id) {
		APICaller apiCaller = new APICaller(this);
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("user_id", user_id);
		sendData.put("token", new AccountDB(this).getToken());

		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
			}

			@Override
			public void onError(String message) {
			}
		};
		apiCaller.callApi("/messages/post", false, callbackAPI, sendData);
	}

	private void changeFlag(String user_id, String room_no, String type,
			String value) {
		APICaller apiCaller = new APICaller(this);
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("token", new AccountDB(this).getToken());
		sendData.put("user_id", user_id);
		sendData.put("room", room_no);
		sendData.put("type", type);
		sendData.put("value", value);

		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {

			}

			@Override
			public void onError(String message) {
			}
		};

		apiCaller.callApi("/messages/status", false, callbackAPI, sendData);
	}

	private void loadRoomList() {
		APICaller apiCaller = new APICaller(this);
		Map<String, String> sendData = new HashMap<String, String>();
		sendData.put("token", new AccountDB(this).getToken());
		ICallbackAPI callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				Intent intent = new Intent(ACTION_UPDATE_ROOM_LIST);
				intent.putExtra(KEY_STATUS, STATUSSERVICE.SUCCCESS);
				intent.putExtra(KEY_MESSAGE, "");
				intent.putExtra(KEY_VALUES, response);
				sendBroadcast(intent);
			}

			@Override
			public void onError(String message) {
				Intent intent = new Intent(ACTION_UPDATE_ROOM_LIST);
				intent.putExtra(KEY_STATUS, STATUSSERVICE.ERROR);
				intent.putExtra(KEY_MESSAGE, message);
				intent.putExtra(KEY_VALUES, "");
				sendBroadcast(intent);
			}
		};

		apiCaller.callApi("/messages/list", false, callbackAPI, sendData);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}