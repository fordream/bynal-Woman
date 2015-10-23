package com.acv.libs.base.comunicate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

@SuppressLint("CommitPrefEdits")
public abstract class FacebookUtils {
	public static final String TAG = "FacebookUtils";
	private Activity activity;

	public FacebookUtils(Activity activity) {
		this.activity = activity;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getActiveSession() != null)
			Session.getActiveSession().onActivityResult(activity, requestCode,
					resultCode, data);
	}

	public void login() {
		Session.openActiveSession(activity, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {
								@Override
								public void onCompleted(final GraphUser user,
										Response response) {
									if (user != null) {
										SharedPreferences preferences = activity.getPreferences(0);
										Editor editor = preferences.edit();
										editor.putString("face-id", user.getId());
										editor.putString("face-name", user.getName());
										editor.commit();
										loginFaceSucess(user);
									}
								}
							}).executeAsync();
				}
			}
		});
	}

	public abstract void loginFaceSucess(final GraphUser user) ;

	public String getId() {
		SharedPreferences preferences = activity.getPreferences(0);
		return preferences.getString("face-id", "");
	}

	public String getName() {
		SharedPreferences preferences = activity.getPreferences(0);
		return preferences.getString("face-name", "");
	}

	private ProgressDialog initProgressDialog() {
		return ProgressDialog.show(activity, "loading", "loading");
	}

	public void logout() {
		Session session = Session.getActiveSession();
		if (session == null) {
			session = new Session(activity);
			Session.setActiveSession(session);
		}

		if (session != null) {
			session.closeAndClearTokenInformation();
		}
	}

}