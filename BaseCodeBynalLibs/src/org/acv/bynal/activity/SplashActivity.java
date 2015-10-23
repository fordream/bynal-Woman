package org.acv.bynal.activity;

import org.acv.bynal.fragment.SplashFragment;

import a.com.acv.bynal.v3.service.V3BynalService;
import a.com.acv.bynal.v3.service.V3BynalService.V3BynalServiceACTION;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.CalendarUtils;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.MBaseActivity;
import com.acv.libs.base.tab.BaseTabActivity;

public class SplashActivity extends MBaseActivity {
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if (!isFinishing()) {
				startActivity(new Intent(SplashActivity.this, BaseTabActivity.class));
				finish();
				overridePendingTransition(R.anim.a_bot_to_top_in,R.anim.a_nothing);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.a_bot_to_top_in,R.anim.a_nothing);
		setContentView(R.layout.mainhome);

		Intent intent = new Intent(this, V3BynalService.class);
		intent.putExtra("V3BynalServiceACTION", V3BynalServiceACTION.EVENTNEWS);
		startService(intent);

		SplashFragment fragment = new SplashFragment();
		fragment.mHandle = handler;
		changeFragemt(R.id.mainhomeframe, fragment);
		
//		long id = CalendarUtils.insertCalendar(this);
//		CommonAndroid.toast(this, id + "");
	}

}