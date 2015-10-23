package a.com.acv.bynal.v3.service;

import java.util.HashMap;
import java.util.Map;

import a.com.acv.bynal.v3.progress.ProgressNewAndEventCaller;
import a.com.acv.bynal.v3.progress.ProgressProjectCaller;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;

public class V3BynalService extends Service {
	public enum V3BynalServiceACTION {
		HOME_NEW_PROJECT, HOME_HOT_PROJECT, EVENTNEWS
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (intent != null) {
				V3BynalServiceACTION action = (V3BynalServiceACTION) intent.getSerializableExtra("V3BynalServiceACTION");

				if (action == V3BynalServiceACTION.HOME_NEW_PROJECT) {
					callProject(V3BynalServiceACTION.HOME_NEW_PROJECT);
				} else if (action == V3BynalServiceACTION.HOME_HOT_PROJECT) {
					callProject(V3BynalServiceACTION.HOME_HOT_PROJECT);
				} else if (action == V3BynalServiceACTION.EVENTNEWS) {
					APICaller apiCaller = new APICaller(this);
					apiCaller.callApi("/user/eventnews", false, callBackNewAndEvent, new HashMap<String, String>());
				}
			}
		} catch (Exception exception) {

		}
		return super.onStartCommand(intent, flags, startId);
	}

	private ICallbackAPI callBackNewAndEvent = new ICallbackAPI() {

		@Override
		public void onSuccess(final String response) {
			new ProgressNewAndEventCaller(V3BynalService.this, response).executeAsynCallBack();
		}

		@Override
		public void onError(String message) {
		}
	};

	private void callProject(final V3BynalServiceACTION action) {
		Map<String, String> sendData = new HashMap<String, String>();
		APICaller apiCaller = new APICaller(this);
		ICallbackAPI callbackHotAPI = new ICallbackAPI() {
			@Override
			public void onError(String message) {
			}

			@Override
			public void onSuccess(String response) {

				String project_type = "";

				if (action == V3BynalServiceACTION.HOME_NEW_PROJECT) {
					project_type = "home_project_new";
				} else if (action == V3BynalServiceACTION.HOME_HOT_PROJECT) {
					project_type = "home_project_hot";
				}

				new ProgressProjectCaller(V3BynalService.this, response, project_type).executeAsynCallBack();
			}
		};
		sendData.put("type", "1");
		sendData.put("value", "1");

		if (action == V3BynalServiceACTION.HOME_HOT_PROJECT) {
			apiCaller.callApi("/project/hot", false, callbackHotAPI, sendData);
		} else if (action == V3BynalServiceACTION.HOME_NEW_PROJECT) {
			apiCaller.callApi("/project/listByFilter", false, callbackHotAPI, sendData);
		}
	}
}