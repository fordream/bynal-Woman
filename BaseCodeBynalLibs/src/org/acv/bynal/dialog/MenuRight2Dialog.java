package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.fragment.ProjectManagerFragment;
import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class MenuRight2Dialog extends MbaseAdialog implements android.view.View.OnClickListener {

	public MenuRight2Dialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public int getAnimationOpen() {
		return R.anim.a_menu_right_in;
	}

	public int getAnimationClose() {
		return R.anim.a_menu_right_out;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.close_menu).setOnClickListener(this);
		findViewById(R.id.menu_right1).setOnClickListener(this);
		findViewById(R.id.menu_right2).setOnClickListener(this);
		findViewById(R.id.menu_right3).setOnClickListener(this);
		findViewById(R.id.menu_right4).setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		open();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.menuright_m_main), new AnimationAction() {

			@Override
			public void onAnimationEnd() {
				dismiss();
				if (isOnClick) {
					clickListener.onClick(null, 0);
				}
			}
		});
	}

	public void open() {
		openPopActivity(findViewById(R.id.menuright_m_main));
	}

	@Override
	public int getLayout() {
		return R.layout.menuright2_new;
	}

	private int TIMEDELAY = 500;

	@Override
	public void onClick(View v) {
		int type = v.getId();
		final Handler handler = new Handler();
		switch (type) {
		case R.id.menu_right1:
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(1);
				}
			}, TIMEDELAY);
			break;
		case R.id.menu_right2:
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(2);
				}
			}, TIMEDELAY);
			break;
		case R.id.menu_right3:
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(3);
				}
			}, TIMEDELAY);
			break;
		case R.id.menu_right4:
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(4);
				}
			}, TIMEDELAY);
			break;
		default:
			break;
		}
		close(true);

	}

	public void opendialog(int dialog) {
		switch (dialog) {
		case 1:
			ProjectManagerFragment.typeProject = "0";
			ProjectManagerFragment.CallAPIProjectList();
			break;
		case 2:
			ProjectManagerFragment.typeProject = "1";
			ProjectManagerFragment.CallAPIProjectList();
			break;
		case 3:
			ProjectManagerFragment.typeProject = "2";
			ProjectManagerFragment.CallAPIProjectList();
			break;
		case 4:
			ProjectManagerFragment.typeProject = "3";
			ProjectManagerFragment.CallAPIProjectList();
			break;
		default:
			break;
		}

	}

	private Context getActivity() {
		return getContext();
	}
}