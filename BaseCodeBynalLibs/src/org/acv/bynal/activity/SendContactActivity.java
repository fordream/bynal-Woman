package org.acv.bynal.activity;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.fragment.ShareContactFragment;

import android.os.Bundle;
import app.bynal.woman.R;

import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;

public class SendContactActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareContactFragment contactFragment = new ShareContactFragment();
		contactFragment.setData(new String[] { getIntent().getStringExtra("id"), getIntent().getStringExtra("title"), getIntent().getStringExtra("url") });
		changeFragemt(contactFragment);
		openFragmentListener(findViewById(R.id.mFrament), contactFragment.getAnimationAction());
	}

	@Override
	public void configMenuleft() {

	}

	public void gotoHome() {
		setResult(ByUtils.REQUEST_HOME);
		finish();
	}

}