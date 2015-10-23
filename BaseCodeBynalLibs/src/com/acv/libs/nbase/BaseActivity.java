package com.acv.libs.nbase;

import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.MenuSlideView;
import org.acv.bynal.views.MenuSlideView.OnItemChooser;

import a.com.acv.crash.CrashExceptionHandler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.util.ByUtils;

public abstract class BaseActivity extends FragmentActivity implements OnItemChooser {
	private FrameLayout mFrameLayout;
	private MenuSlideView menuSlideView;

	// private FacebookUtils facebookUtils;
	// private TwitterJS twitterJS;
	@Override
	public void finish() {
		// BynalAnimationUtils.closeActivity(this, findViewById(R.id.mFrament),
		// new AnimationAction() {
		// @Override
		// public void onAnimationEnd() {
		// findViewById(R.id.mFrament).setVisibility(View.GONE);
		// _finish();
		// }
		// });
		super.finish();
		overridePendingTransition(0, R.anim.a_top_to_bot_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		MainHomeActivity.saveBimap(findViewById(R.id.mFrament));
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivity(Intent intent) {
		startActivityForResult(intent, ByUtils.REQUEST_HOME);
	}

	// private void _finish() {
	// super.finish();
	// overridePendingTransition(0, R.anim.a_top_to_bot_out);
	// }

	public void refresh() {

	}

	public void refreshMenu() {
	}

	public void clearMenu() {
		menuSlideView.clear();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CrashExceptionHandler.onCreate(this);
		
		setContentView(R.layout.base);
		overridePendingTransition(R.anim.a_bot_to_top_in, R.anim.a_nothing);
		ImageView imageView = (ImageView) findViewById(R.id.base_background);
		imageView.setImageBitmap(MainHomeActivity.mBitmap);

		mFrameLayout = (FrameLayout) findViewById(R.id.mFrament);
		menuSlideView = new MenuSlideView(this);
		if (!(this instanceof MessageActivity))
			mFrameLayout.addView(menuSlideView);
		menuSlideView.setOnItemChooser(this);

		configMenuleft();

		if (menuSlideView != null) {
			menuSlideView.notifyDataSetChanged();
		}
	}

	public void enableMenuLeft() {
	}

	public void setTextheader(int str) {
		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
		headerView.setText(str);
	}

	public void setTextheader(String str) {
		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
		headerView.setText(str);
	}

	public HeaderView getHeaderView() {
		return (HeaderView) findViewById(R.id.headerView1);
	}

	/**
	 * add fragment
	 * 
	 */

	private BaseFragment currBaseFragment;
	private Handler handler = new Handler();

	public void changeFragemt(final BaseFragment rFragment) {
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		getHeaderView().reloadChooser();
		rFragment.setMParentFragment(currBaseFragment);
		currBaseFragment = rFragment;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.main_content, rFragment);
		ft.commit();

		HeaderOption headerOption = rFragment.getHeaderOption();
		getHeaderView().setheaderOption(headerOption);
		getHeaderView().refresh();
		// }
		// }, MenuSlideView.TIMECALLMENU);

	}

	/*
	 * MENU LEFT
	 */
	public void onItemClickmenuLeft(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == ByUtils.REQUEST_HOME && requestCode == ByUtils.REQUEST_HOME) {
			setResult(ByUtils.REQUEST_HOME);
			finish();
		}
	}

	public abstract void configMenuleft();

	public void addmenu(MenuItem item) {
		if (item != null) {
			if (menuSlideView != null) {
				menuSlideView.addmenu(item);
			}
		}
	}

	public void showMenuLeft(boolean show) {
		if (menuSlideView != null) {
			menuSlideView.callMenu(MenuSlideView.TIMECALLMENU);
		}
	}

	public void showMenuSubRight(boolean show) {
		menuSlideView.onSubmenu = show;
		if (!show) {
			menuSlideView.callMenu(0);
		}
	}

	public void configMenuRight() {
		menuSlideView.isRight = true;
	}

	public void showMenuRight(boolean show) {
		if (!show) {
			menuSlideView.menu_click.setVisibility(View.GONE);
			menuSlideView.menu_list.setVisibility(View.GONE);
			menuSlideView.menu_main.setVisibility(View.GONE);
		} else {
			menuSlideView.menu_click.setVisibility(View.VISIBLE);
			menuSlideView.menu_list.setVisibility(View.VISIBLE);
			menuSlideView.menu_main.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(int position, MenuItem item) {

	}

	public void setChoosePosition(int i) {
		if (currBaseFragment != null) {
			currBaseFragment.setChoosePosition(i);
		}
	}

	public void openFragmentListener(View mMain, final AnimationAction animationAction) {
		if (mMain == null)
			return;
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.a_nothing);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animationAction != null)
					animationAction.onAnimationEnd();
			}
		});
		mMain.startAnimation(animation);
	}
}