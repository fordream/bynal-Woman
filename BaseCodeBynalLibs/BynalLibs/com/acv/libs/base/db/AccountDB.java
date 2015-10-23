package com.acv.libs.base.db;

import org.json.JSONObject;

import android.content.Context;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.LogUtils;

public class AccountDB extends BaseDB {

	public AccountDB(Context context) {
		super(context);
	}

	public String getToken() {
		String str = getData();
		try {
			return new BaseItem(new JSONObject(str)).getString("token");
		} catch (Exception exception) {
			return null;
		}
	}

	public String getName() {
		String str = getData();
		try {
			if (!getNameUserId().equals("")) {
				return getNameUserId();
			}
			return new BaseItem(new JSONObject(str)).getString("user_name");
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public void save(String str) {
		super.save(str);
		saveName(null, "");
	}

	public String getUserId() {
		String str = getData();
		try {
			return new BaseItem(new JSONObject(str)).getString("user_id");
		} catch (Exception exception) {
			return null;
		}
	}

	
	public String getEmail(){
		String str = getData();
		try {
		//	return new JSONObject(str).getJSONObject("log").getJSONObject("post").getString("user");
			return new JSONObject(str).getString("mail_address");
		} catch (Exception exception) {
			return null;
		}
	}
	public String getNameUserId() {
		return getData("OMG");
	}

	public void saveName(String idUser, String name) {
		save("OMG", name);
	}
}