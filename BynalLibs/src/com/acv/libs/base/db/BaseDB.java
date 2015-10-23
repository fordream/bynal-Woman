package com.acv.libs.base.db;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseDB {
	private Context context;
	private SharedPreferences preferences;

	public BaseDB(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences(this.getClass().getName(), 0);
	}

	public void save(String str) {
		preferences.edit().putString(getClass().getName(), str).commit();
	}

	public void save(String key, String str) {
		preferences.edit().putString(key, str).commit();
	}

	public String getData(String key) {
		return preferences.getString(key, "");
	}

	public String getData() {
		try {
			return preferences.getString(getClass().getName(), "");
		} catch (Exception exception) {
			return "";
		}
	}

	public void clear() {
		preferences.edit().clear().commit();
	}
}