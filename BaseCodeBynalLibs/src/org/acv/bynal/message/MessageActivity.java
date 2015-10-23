package org.acv.bynal.message;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.main.activity.MainHomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.lazyload.ImageLoader2;
import com.acv.libs.nbase.BaseActivity;

public class MessageActivity extends BaseActivity {
	public static Bitmap bitmap;
	public static ImageLoader2 imageLoader; 
	public static final void startActivity(Context context, String id) {

		Intent intent = new Intent(context, MessageActivity.class);
		intent.putExtra("id", id);
		if (context instanceof Activity) {
			((Activity) context).startActivityForResult(intent, ByUtils.REQUEST_HOME);
		} else if (context instanceof BaseActivity) {
			((BaseActivity) context).startActivityForResult(intent, ByUtils.REQUEST_HOME);
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		imageLoader=new ImageLoader2(this);
		if (bitmap != null) {
			ImageView imageView = (ImageView) findViewById(R.id.base_background);
			imageView.setImageBitmap(bitmap);
			bitmap = null;

		}
		AccountDB accountDB = new AccountDB(this);

		if (accountDB.getToken() == null) {
			finish();
		} else {
			String id = getIntent().getStringExtra("id");
			int type_message = getIntent().getIntExtra("type_message", 0);
			MessagePostFragment messagePostFragment = new MessagePostFragment();
			messagePostFragment.setMessageType(type_message);
			messagePostFragment.setData(id);
			changeFragemt(messagePostFragment);
			openFragmentListener(findViewById(R.id.mFrament), messagePostFragment.getAnimationAction());
		}

	}

	@Override
	public void configMenuleft() {

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	public void gotoHome() {
		setResult(ByUtils.REQUEST_HOME);
		finish();
	}
}