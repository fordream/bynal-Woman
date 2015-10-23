package com.acv.libs.base;

import a.com.acv.crash.CrashExceptionHandler;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MBaseActivity extends FragmentActivity {
	// private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		CrashExceptionHandler.onCreate(this);
	}

	/**
	 * R.id.mainhomeframe
	 * 
	 * @param res
	 * @param rFragment
	 */
	public void changeFragemt(int res, final LibsBaseFragment rFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(res, rFragment);
		ft.commit();
		// HeaderOption headerOption = rFragment.getHeaderOption();

	}
}
