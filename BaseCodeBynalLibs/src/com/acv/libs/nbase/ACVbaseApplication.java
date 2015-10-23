package com.acv.libs.nbase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.camera.Authorizer;
import org.acv.bynal.camera.VideoActivity;
import org.acv.bynal.camera.Authorizer.AuthorizationListener;
import org.acv.bynal.camera.ClientLoginAuthorizer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.DataStore;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.parse.Parse;

public class ACVbaseApplication extends Application {
	private ImageLoaderUtils imageLoaderUtils;
	private DataStore dataStore = DataStore.getInstance();

	@Override
	public void onCreate() {
		super.onCreate();
		imageLoaderUtils = ImageLoaderUtils.getInstance(this);
		dataStore.init(this);
		if (GcmBroadcastReceiver.getStatusPush((Context) this) == GcmBroadcastReceiver.ENABLE) {
			String Parse_app_key = this.getResources().getString(R.string.parse_app_key);
			String Parse_client_key = this.getResources().getString(R.string.parse_client_key);
			Parse.initialize(this, Parse_app_key, Parse_client_key);
		}
	}

	public void CallAPI() {

	}

	private Typeface typeface, typefaceb;

	public Typeface getTypeface() {
		if (typeface == null) {
			AssetManager assertManager = getAssets();
			typeface = Typeface.createFromAsset(assertManager, "meiryo.ttc");
		}
		return typeface;
	}

	public Typeface getTypefaceBold() {
		if (typefaceb == null) {
			AssetManager assertManager = getAssets();
			typefaceb = Typeface.createFromAsset(assertManager, "meiryob.ttc");
		}
		return typefaceb;
	}

	public void callApiHotProject(final ACVbaseApplicationCallBack applicationCallBack) {
		Map<String, String> sendData = new HashMap<String, String>();
		APICaller apiCaller = new APICaller(this);
		ICallbackAPI callbackHotAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<Object> lfragments = new ArrayList<Object>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						JSONArray array = jsonObject.getJSONArray("array_data");
						for (int i = 0; i < array.length(); i++) {
							lfragments.add(array.get(i));
						}
					} else {
					}
				} catch (Exception e) {
				}

				try {
					applicationCallBack.aCVbaseApplicationCallBack(lfragments);
				} catch (Exception exception) {

				}
			}

			@Override
			public void onError(String message) {
			}
		};

		apiCaller.callApi("/project/hot", false, callbackHotAPI, sendData);
	}

	public void callApiNewProject(final ACVbaseApplicationCallBack applicationCallBack) {
		Map<String, String> sendData = new HashMap<String, String>();
		APICaller apiCaller = new APICaller(this);
		ICallbackAPI callbackHotAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				List<Object> lfragments = new ArrayList<Object>();
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						JSONArray array = jsonObject.getJSONArray("array_data");
						for (int i = 0; i < array.length(); i++) {
							lfragments.add(array.get(i));
						}

					} else {
					}
				} catch (Exception e) {
				}

				try {
					applicationCallBack.aCVbaseApplicationCallBack(lfragments);
				} catch (Exception exception) {

				}
			}

			@Override
			public void onError(String message) {
			}
		};

		apiCaller.callApi("/project/listByFilter", false, callbackHotAPI, sendData);
	}

	public interface ACVbaseApplicationCallBack {
		public void aCVbaseApplicationCallBack(Object object);
	}

}