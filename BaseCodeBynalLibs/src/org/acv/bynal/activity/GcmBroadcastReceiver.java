/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acv.bynal.activity;

import java.io.IOException;

import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.push.PUSHTYPE;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.LogUtils;
import com.acv.libs.base.RestClient;
import com.acv.libs.base.RestClient.RequestMethod;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.SaveCallback;

/**
 * Handling of GCM messages.
 */
public class GcmBroadcastReceiver extends ParsePushBroadcastReceiver /*BroadcastReceiver*/ {
	static final String TAG = "GCMDemo";
	public static final int NOTIFICATION_ID = 1;
//	private boolean useBrodcast = false;
//	private static final String ACTION = "org.acv.bynal.push.mPaserPushBroadcastReciver";

	@Override
    protected Notification getNotification(Context context, Intent intent)
    {
		LogUtils.e(TAG,"aaaaaaaaaaa");
		Bundle extras = intent.getExtras();
        String jsonData = extras.getString("com.parse.Data");
        String title = "Bynal Woman";
        String message = "";
        String customdata = "";
        PUSHTYPE pushtype = PUSHTYPE.none;;
        String userid_from = "";//Message
        String project_id = "";// project
        if (jsonData != null) {
            try {
            	JSONObject data = new JSONObject(jsonData);
                message = data.getString("alert");
                customdata = data.getString("customdata");
                JSONObject custom_data = new JSONObject(customdata);
                if(custom_data != null){
                	if(custom_data.has("type")){
                		pushtype = getPustType(custom_data.getString("type"));
                		LogUtils.e("MESSGAE", message + pushtype);
                	}
                	if(custom_data.has("userid_from")){
                		userid_from = custom_data.getString("userid_from");
                	}
                	if(custom_data.has("project_id")){
                		project_id = custom_data.getString("project_id");
                	}
                }
            }
            catch(JSONException e) {
            	LogUtils.e(TAG, "Error parsing json data");
            }
        }
        else {
        	LogUtils.e(TAG, "cannot find notification data");
        }

        LogUtils.e(TAG,"customdata:" + customdata);
        LogUtils.e(TAG,"message:" + message);
        // Common intent
        /*Intent newIntent = new Intent(context, ProjectDetailActivity.class);
        String project_id = "217";
		newIntent.putExtra("projectIdDetail", Integer.parseInt(project_id ));
//        PendingIntent openPendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);
        PendingIntent openPendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        */
        AccountDB accountDB = new AccountDB(context);
		final String userId = accountDB.getUserId();
        int idPush = 0;
        PendingIntent contentIntent = null;
		if (pushtype == PUSHTYPE.message) {
			if (userId != null) {
				idPush = 1;
				Intent notificationIntent = new Intent(context, MessageActivity.class);

				notificationIntent.putExtra("id", userid_from);
				notificationIntent.putExtra("type_message", 1);
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				// send broadcast for update screen
				Intent refreshScreenMessage = new Intent(ByUtils.ACTION_REFRESH_SCREEN_MESSAGE);
				context.sendBroadcast(refreshScreenMessage);
				// use send broadcast
				// Intent notificationIntent = new Intent(ACTION);
				// notificationIntent.putExtra("message", message);
				// contentIntent = PendingIntent.getBroadcast(context, 0,
				// notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				// update message room
				MessageService.updateMessageRoom(context);
			}else{
				LogUtils.e("MESSGAE", "do not login");
			}
			
		} else if (pushtype == PUSHTYPE.favorite_pj) {
			idPush = 2;
			Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
			notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
			contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		} else if (pushtype == PUSHTYPE.new_pj) {
			idPush = 3;
			Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
			notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
			contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		} else if (pushtype == PUSHTYPE.report_pj) {
			idPush = 4;
			Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
			notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
			contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		} else if (pushtype == PUSHTYPE.support_pj) {
			if (userId != null) {
				idPush = 5;
//				Intent notificationIntent = new Intent(context, ProjectmanagerActivity.class);
//				notificationIntent.putExtra("projectIdDelivery", project_id);
				
				Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
				notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			}else{
				LogUtils.e("SUPPORT", "do not login");
			}
			

		} else if (pushtype == PUSHTYPE.none){
			idPush = 6;
			Intent notificationIntent = new Intent(context, SplashActivity.class);
			contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		}

		if (/*pushtype != PUSHTYPE.none &&*/ contentIntent != null && !"".equals(message)) {
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 300 milliseconds
			v.vibrate(300);
			NotificationCompat.Builder builder =
	                new NotificationCompat.Builder(context)
	                        .setSmallIcon(R.drawable.icon)
	                        .setContentTitle(title)
	                        .setContentText(message)
	                        .setContentIntent(contentIntent)
	                        .setDefaults(Notification.DEFAULT_ALL)
	                        .setAutoCancel(true)
	                        .setStyle(new NotificationCompat.BigTextStyle()
	                                .bigText(message));
	        return builder.build();
		}else{
			return null;
		}
        
    }
	
	/*@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		String message = intent.getExtras().getString("message");

		if (getStatusPush(context) == ENABLE) {
			BaseItem baseItem = new BaseItem(message);
			String title = baseItem.getString("title");

			PUSHTYPE pushtype = getPustType(title);
			LogUtils.e("MESSGAE", message + pushtype);
			// all
			String description = baseItem.getString("description");

			// Message
			String userid_to = baseItem.getString("userid_to");
			String userid_from = baseItem.getString("userid_from");
			// project
			String project_id = baseItem.getString("project_id");
			// support_pj
			String username_support = baseItem.getString("username_support");

			// support_pj,favorite_pj,report_pj
			String userid_project = baseItem.getString("userid_project");

			// favorite_pj,report_pj
			String username_addfavorite = baseItem.getString("username_addfavorite");

			if (ACTION.equals(intent.getAction())) {
				// start activity

				return;
			}

			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.icon;
			long time = System.currentTimeMillis();

			String notificationTitle = getPustTypeStr(context,title);
			Notification notification = new Notification(icon, notificationTitle, time);
			notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;
			PendingIntent contentIntent = null;

			int idPush = 0;

			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 300 milliseconds
			v.vibrate(300);

			if (pushtype == PUSHTYPE.message) {
				idPush = 1;
				Intent notificationIntent = new Intent(context, MessageActivity.class);

				notificationIntent.putExtra("id", userid_from);
				notificationIntent.putExtra("type_message", 1);
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				// send broadcast for update screen
				Intent refreshScreenMessage = new Intent(ByUtils.ACTION_REFRESH_SCREEN_MESSAGE);
				context.sendBroadcast(refreshScreenMessage);
				// use send broadcast
				// Intent notificationIntent = new Intent(ACTION);
				// notificationIntent.putExtra("message", message);
				// contentIntent = PendingIntent.getBroadcast(context, 0,
				// notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				// update message room
				MessageService.updateMessageRoom(context);
			} else if (pushtype == PUSHTYPE.favorite_pj) {
				idPush = 2;
				Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
				notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

			} else if (pushtype == PUSHTYPE.new_pj) {
				idPush = 3;
				Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
				notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			} else if (pushtype == PUSHTYPE.report_pj) {
				idPush = 4;
				Intent notificationIntent = new Intent(context, ProjectDetailActivity.class);
				notificationIntent.putExtra("projectIdDetail", Integer.parseInt(project_id));
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

			} else if (pushtype == PUSHTYPE.support_pj) {
				idPush = 5;
				Intent notificationIntent = new Intent(context, ProjectmanagerActivity.class);
				notificationIntent.putExtra("projectIdDelivery", project_id);
				contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

			}

			if (pushtype != PUSHTYPE.none && contentIntent != null) {

				// if (useBrodcast) {
				//
				// Intent notificationIntent = new Intent(ACTION);
				// notificationIntent.putExtra("message", message);
				// contentIntent = PendingIntent.getBroadcast(context, 0,
				// notificationIntent,
				// PendingIntent.FLAG_UPDATE_CURRENT);
				// } else {
				//
				// }

				notification.setLatestEventInfo(context, notificationTitle, description, contentIntent);
				mNotificationManager.notify(idPush, notification);
			}
		}
	}*/

	public static PUSHTYPE getPustType(String title) {

		if ("new_pj".equals(title)) {
			return PUSHTYPE.new_pj;
		} else if ("support_pj".equals(title)) {
			return PUSHTYPE.support_pj;
		} else if ("favorite_pj".equals(title)) {
			return PUSHTYPE.favorite_pj;
		} else if ("report_pj".equals(title)) {
			return PUSHTYPE.report_pj;
		} else if ("message".equals(title)) {
			return PUSHTYPE.message;
		}
		return PUSHTYPE.none;
	}

	public String getPustTypeStr(Context context, String title) {

		if ("new_pj".equals(title)) {
			return context.getResources().getString(R.string.notification_new_pj);
		} else if ("support_pj".equals(title)) {
			return context.getResources().getString(R.string.notification_support_pj);
		} else if ("favorite_pj".equals(title)) {
			return context.getResources().getString(R.string.notification_favorite_pj);
		} else if ("report_pj".equals(title)) {
			return context.getResources().getString(R.string.notification_report_pj);
		} else if ("message".equals(title)) {
			return context.getResources().getString(R.string.notification_message);
		}
		return "";
	}

	public static int getStatusPush(Context context) {
		final SharedPreferences preferences = context.getSharedPreferences(TAGA, 0);
		return preferences.getInt(TAGA, NORMAl);
	}

	public static void saveStatusPush(Context context, int eNABLE2) {
		final SharedPreferences preferences = context.getSharedPreferences(TAGA, 0);
		preferences.edit().putInt(TAGA, eNABLE2).commit();
	}

	public static int NORMAl = 0;
	public static int ENABLE = 1;
	public static int CANCEL = 2;
	static final String TAGA = "register-push";

	public static void register(final Context context) {

		// final SharedPreferences preferences = context.getSharedPreferences(
		// TAGA, 0);

		/*final String SENDER_ID = "609478506422";// "27284071298";
		final String SERVER = ByUtils.BASESERVER_REGISTER_PUSH + "/bynal_push/register/android";
		final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);*/
		AccountDB accountDB = new AccountDB(context);
		final String userId = accountDB.getUserId();

		String Parse_app_key = context.getResources().getString(R.string.parse_app_key);
		String Parse_client_key = context.getResources().getString(R.string.parse_client_key);
		Parse.initialize(context, Parse_app_key, Parse_client_key);
		if (userId != null) {
			ParseInstallation.getCurrentInstallation().put("userId", Integer.valueOf(userId));
		} else {
			ParseInstallation.getCurrentInstallation().put("userId", 0);
		}
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					LogUtils.e("GCM_Parse", "OK");
					return;
				}else{
					LogUtils.e("GCM_Parse", "Err" + e.getMessage());
					return;
				}
			}
		});
		
		/*new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... _params) {
				try {
					String regid = gcm.register(SENDER_ID);
					String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
					RestClient client = new RestClient(SERVER);
					client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
					client.addParam("device_id", deviceId);
					client.addParam("registration_id", regid);

					if (userId != null) {
						client.addParam("user_id", userId);
					} else {
						client.addParam("user_id", "");
					}

					try {
						client.execute(RequestMethod.POST);
						// if (client.getResponse().equals("200")
						// || client.getResponseCode() == 200) {
						// LogUtils.e("GCM_1", deviceId);
						// LogUtils.e("GCM_1", regid);
						// LogUtils.e("GCM_1", userId + "1");
						// LogUtils.e("GCM_1", client.getResponse());
						// preferences.edit().putBoolean(TAG, true)
						// .commit();
						// }

						return client.getResponse();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (IOException ex) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(String msg) {

				LogUtils.e("ABCBCBCBC", msg + "");
			}
		}.execute(null, null, null);*/
	}

}
