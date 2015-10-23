package com.org.social.twitter;

import android.content.Context;

import com.org.social.twitter.TwitterUtils.ITwitterUtils;

public abstract class TwitterJS {
	public static final String TAG = "TwitterJS";
	private Context mContext;

	public TwitterJS(Context example) {
		this.mContext = example;
		TwitterUtils.getInstance().onCreate(mContext);
		TwitterUtils.getInstance().setITwitterUtils(new ITwitterUtils() {
			@Override
			public void succesLoadProfile() {
				loginTwiterSucess();
			}
		});
	}

	public abstract void loginTwiterSucess();

	public boolean isLogin() {
		return TwitterUtils.getInstance().isLogin();
	}

	public void login() {
		TwitterUtils.getInstance().onCreateWeb(null);
	}

	public String getName() {
		return TwitterUtils.getInstance().getName();

	}

	public String getId() {
		return TwitterUtils.getInstance().getID();
	}
}
