/**
 * 
 */
package com.acv.libs.base.tab;

import org.acv.bynal.activity.GcmBroadcastReceiver;
import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.activity.SearchActivity;
import org.acv.bynal.activity.SplashActivity;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.views.MenuSlideView.OnItemChooser;

import a.com.acv.bynal.v3.dialog.MenuLeftDialog;
import a.com.acv.crash.CrashExceptionHandler;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.comunicate.FacebookUtils;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.util.ByUtils;
import com.example.ttest.views.AnimationView;
import com.facebook.model.GraphUser;

/**
 * class base for all activity
 * 
 * @author tvuong1pc
 * 
 */
public class BaseTabActivity extends TabActivity implements OnItemChooser {
	public static void senBroadCast(Context context, int idMenu) {
		Intent intent = new Intent(ACTION_MENU);
		intent.putExtra(ACTION_MENU, idMenu);
		context.sendBroadcast(intent);
	}

	public static void senBroadCastMessage(Context context, String id) {
		senBroadCastHeaderMenu(context, "message", id);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.a_bot_to_top_in, R.anim.a_top_to_bot_out);
	}

	public static void senBroadCastHeaderMenu(Context context, String idMenu) {
		Intent intent = new Intent(ACTION_MENU_HEADER);
		intent.putExtra(ACTION_MENU_HEADER, idMenu);
		context.sendBroadcast(intent);
	}

	public static void senBroadCastHeaderMenu(Context context, String idMenu, int id) {
		Intent intent = new Intent(ACTION_MENU_HEADER);
		intent.putExtra(ACTION_MENU_HEADER, idMenu);
		intent.putExtra("id", id);
		context.sendBroadcast(intent);
	}

	public static void senBroadCastHeaderMenu(Context context, String idMenu, String id) {
		Intent intent = new Intent(ACTION_MENU_HEADER);
		intent.putExtra(ACTION_MENU_HEADER, idMenu);
		intent.putExtra("_id", id);
		context.sendBroadcast(intent);
	}

	private static final String ACTION_MENU = "ACTION_MENU";
	private static final String ACTION_MENU_HEADER = "ACTION_MENU_HEADER";

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(ACTION_MENU)) {
				final int idResMenu = intent.getIntExtra(ACTION_MENU, -1);
				MenuItem item = new MenuItem(getString(idResMenu), 0, false);
				onItemClick(0, item);
			} else if (intent.getAction().equals(ACTION_MENU_HEADER)) {
				String actionContentc = intent.getStringExtra(ACTION_MENU_HEADER);
				if ("home".equals(actionContentc)) {
					refreshMenuAndgotoHome();
				} else if ("menu".equals(actionContentc)) {
					showMenuLeft(true);
				} else if ("search".equals(actionContentc)) {
					MainHomeActivity.saveBimap();
					startActivity(new Intent(getActivity(), SearchActivity.class));

				} else if ("projectdetail".equals(actionContentc)) {
					MainHomeActivity.saveBimap();
					int id = intent.getIntExtra("id", -1);
					Intent mIntent = new Intent(getContext(), ProjectDetailActivity.class);
					mIntent.putExtra("projectIdDetail", id);
					MainHomeActivity.saveBimap();
					startActivity(mIntent);
				} else if ("message".equals(actionContentc)) {
					String id = intent.getStringExtra("_id");
					Intent mIntent = new Intent(BaseTabActivity.this, MessageActivity.class);
					mIntent.putExtra("id", id);
					mIntent.putExtra("type_message", 0);
					startActivityForResult(mIntent, ByUtils.REQUEST_HOME);
				}
			}
		}
	};

	private AnimationView animationview;

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.a_bot_to_top_in, R.anim.a_nothing);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.a_bot_to_top_in, R.anim.a_nothing);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
		registerReceiver(broadcastReceiver, new IntentFilter(ACTION_MENU));
		registerReceiver(broadcastReceiver, new IntentFilter(ACTION_MENU_HEADER));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onBackPressed() {

		if (getTabHost().getCurrentTab() <= 0) {
			super.onBackPressed();
		} else {
			setCurrentTab(0);
		}
	}

	private void setCurrentTab(final int index) {
		SharedPreferences preferences = getSharedPreferences(ByUtils.SHAREREFERRENT_SETTING, 0);
		preferences.edit().putBoolean("LOGINTAB", true).commit();

		if (getTabHost().getCurrentTab() != index) {
			MainHomeActivity.setNeedUseAnimation(this, true);
			// Bitmap bitmap = MainHomeActivity.getDrawingCache();
			// animationview.setBimatMap(bitmap);
			// animationview.startMAnimation(null);
			getTabHost().setCurrentTab(index);
		}
	}

	// private static SharedPreferences preferences_shorcut;
	private static final String SHORCUT_DB = "shorcut_db";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// A47_1Dialog a47_1Dialog = new A47_1Dialog(getContext(), null);
		// a47_1Dialog.show();

//		A47_Comfirm a47_Comfirm = new A47_Comfirm(getContext(), null);
//		a47_Comfirm.show();
		CrashExceptionHandler.onCreate(this);
		CrashExceptionHandler.sendCrash(this);

		setContentView(R.layout.tabexampleslide);
		animationview = (AnimationView) findViewById(R.id.animationview);

		MainHomeActivity.setNeedUseAnimation(this, true);
		getTabHost().setOnTabChangedListener(new AnimatedTabHostListener(this, getTabHost()));
		SharedPreferences preferences_shorcut = getSharedPreferences(SHORCUT_DB, MODE_PRIVATE);
		autoCreateShortCut(preferences_shorcut.getBoolean("create_shorcut", false), getContext());
		preferences_shorcut.edit().putBoolean("create_shorcut", true).commit();

		ImageLoaderUtils.getInstance(this).clear();
		addTab();
	}

	private void addTab() {
		// 0
		addTab(MainHomeActivity.class, "Home", "Home", MainHomeActivity.HOME);
		// 1
		addTab(MainHomeActivity.class, "Login", "Login", MainHomeActivity.LOGIN);
		// 2
		addTab(MainHomeActivity.class, "MESSAGE", "MESSAGE", MainHomeActivity.MESSAGE);
		// 3
		addTab(MainHomeActivity.class, "CONTACTUS", "CONTACTUS", MainHomeActivity.CONTACTUS);
		// 4
		addTab(MainHomeActivity.class, "ABOUT", "ABOUT", MainHomeActivity.ABOUT);
		// 5
		addTab(MainHomeActivity.class, "HELP", "HELP", MainHomeActivity.HELP);
		// 6
		addTab(MainHomeActivity.class, "X1", "X1", MainHomeActivity.X1);
		// 7
		addTab(MainHomeActivity.class, "X2", "X2", MainHomeActivity.X2);
		// 8
		addTab(MainHomeActivity.class, "X3" + "", "X3", MainHomeActivity.X3);
		// 9
		addTab(MainHomeActivity.class, "NEWANDALERTS", "NEWANDALERTS", MainHomeActivity.NEWANDALERTS);

		// 10
		addTab(MainHomeActivity.class, "PROFILE", "PROFILE", MainHomeActivity.PROFILE);

		// 11
		addTab(MainHomeActivity.class, "PROJECTMABAGER", "PROJECTMABAGER", MainHomeActivity.PROJECTMABAGER);
		
		// 12
		addTab(MainHomeActivity.class, "BLOG", "BLOG", MainHomeActivity.BLOG);

		if (GcmBroadcastReceiver.getStatusPush((Context) this) == GcmBroadcastReceiver.NORMAl) {
			CommonAndroid.showDialog(this, getString(R.string.message_push), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GcmBroadcastReceiver.saveStatusPush(BaseTabActivity.this, GcmBroadcastReceiver.ENABLE);
					GcmBroadcastReceiver.register(BaseTabActivity.this);
				}
			}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GcmBroadcastReceiver.saveStatusPush(BaseTabActivity.this, GcmBroadcastReceiver.CANCEL);
				}
			});
		} else if (GcmBroadcastReceiver.getStatusPush((Context) this) == GcmBroadcastReceiver.ENABLE) {
			GcmBroadcastReceiver.register(BaseTabActivity.this);
		}

	}

	protected void monItemClick(int position, MenuItem item) {
		onItemClick(position, item);
	}

	/**
	 * convert view from resource
	 * 
	 * @param res
	 * @return
	 */
	public <T extends View> T getView(int res) {
		@SuppressWarnings("unchecked")
		T view = (T) findViewById(res);
		return view;
	}

	public void addTab(Class<?> activity, String tabSpect, String indicator, int type) {
		TabHost tabHost = getTabHost();
		TabSpec firstTabSpec = tabHost.newTabSpec(tabSpect);
		Intent intent = new Intent(this, activity);
		intent.putExtra("type", type);
		firstTabSpec.setIndicator(indicator).setContent(intent);
		tabHost.addTab(firstTabSpec);
	}

	public void addTab(Class<?> activity, String tabSpect, View indicator) {
		TabHost tabHost = getTabHost();
		TabSpec firstTabSpec = tabHost.newTabSpec(tabSpect);
		firstTabSpec.setIndicator(indicator).setContent(new Intent(this, activity));
		tabHost.addTab(firstTabSpec);
	}

	protected Context getContext() {
		return this;
	}

	protected Activity getActivity() {
		return this;
	}

	public void refresh() {
	}

	public void enableMenuLeft() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ByUtils.REQUEST_HOME && resultCode == ByUtils.RESPONSE_RELEASETOKEN) {
			new AccountDB(this).clear();
			refreshMenuAndgotoHome();
		} else if (requestCode == ByUtils.REQUEST_HOME && resultCode == ByUtils.REQUEST_HOME) {
			refreshMenuAndgotoHome();
		}
	}

	public void showMenuLeft(boolean show) {
		CommonAndroid.hiddenKeyBoard(this);
		new MenuLeftDialog(getContext()).show();
	}

	@Override
	public void onItemClick(int position, MenuItem item) {
		AccountDB accountDB = new AccountDB(getActivity());
		if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_Login))) {
			setCurrentTab(1);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_message))) {
			if (ByUtils.isBlank(accountDB.getToken())) {
				if (getTabHost().getCurrentTab() == 1) {
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_pleaselogin), null);
				} else
					setCurrentTab(1);
			} else {
				setCurrentTab(2);
			}
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_contact_us))) {
			setCurrentTab(3);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_about))) {
			setCurrentTab(4);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_help))) {
			setCurrentTab(5);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_x1))) {
			setCurrentTab(6);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_x2))) {
			setCurrentTab(7);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_x3))) {
			setCurrentTab(8);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_news_event))) {
			setCurrentTab(9);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_logout))) {
			logout();
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_project_manager))) {
			if (ByUtils.isBlank(accountDB.getToken())) {
				if (getTabHost().getCurrentTab() == 1) {
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_pleaselogin), null);
				} else
					setCurrentTab(1);
			} else {

				setCurrentTab(11);
			}
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString((R.string.menu_home_profile)))) {
			setCurrentTab(10);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString((R.string.menu_home_more)))) {

		} else if ((item.getName()).equalsIgnoreCase(getResources().getString((R.string.menu_home_blog)))) {
			setCurrentTab(12);
		}
	}

	private void logout() {
		new AccountDB(this).clear();
		FacebookUtils facebookUtils = new FacebookUtils(this) {
			@Override
			public void loginFaceSucess(GraphUser user) {
			}
		};

		facebookUtils.logout();
		if (GcmBroadcastReceiver.getStatusPush((Context) this) == GcmBroadcastReceiver.ENABLE) {
			GcmBroadcastReceiver.register(BaseTabActivity.this);
		}
		refreshMenuAndgotoHome();
	}

	/**
	 * facebook
	 */

	/**
	 * twitter
	 */

	public void refreshMenuAndgotoHome() {
		refresh();
		setCurrentTab(0);

	}

	public void relaseTooken() {

		new AccountDB(this).clear();
		refresh();
		setCurrentTab(1);
	}

	public void openContactUs() {
		setCurrentTab(3);
	}

	public static void autoCreateShortCut_old(boolean hasShorcut, Context context) {
		if (!hasShorcut) {
			// Create Shortcut
			final String title = context.getResources().getString(R.string.app_name);
			final int icon = R.drawable.icon;

			final Class<?> cls = SplashActivity.class;

			final Intent shortcutIntent = new Intent();
			shortcutIntent.setClass(context, cls);
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			final Intent putShortCutIntent = new Intent();
			putShortCutIntent.putExtra("duplicate", false);
			putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon));
			putShortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			context.sendBroadcast(putShortCutIntent);
			// preferences_shorcut.edit().putBoolean("create_shorcut",
			// true).commit();
		}
	}

	private static void autoCreateShortCut(boolean hasShorcut, Context context) {
		if (!hasShorcut) {
			Intent intentShortcut = createIntent(SplashActivity.class, context);
			intentShortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			context.sendBroadcast(intentShortcut);
		}
	}

	private static Intent createIntent(Class<?> cls, Context context) {

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// -------------------------------------------------------------
		// sam sung 2x
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB && Build.BRAND.equals("samsung")) {
			// sam sung 2.x
			// shortcutIntent.addCategory("android.intent.category.LAUNCHER");
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && Build.BRAND.equals("samsung")) {
			// sam sung 4.x
			shortcutIntent.addCategory("android.intent.category.LAUNCHER");
			shortcutIntent.setPackage(context.getPackageName());
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		} else {
			// orther
			shortcutIntent.addCategory("android.intent.category.LAUNCHER");
			shortcutIntent.setPackage(context.getPackageName());
		}
		// -------------------------------------------------------
		shortcutIntent.setClass(context, cls);
		Intent intentShortcut = new Intent();
		intentShortcut.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
		String title = context.getResources().getString(R.string.app_name);
		intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		intentShortcut.putExtra("duplicate", false);

		final int icon = R.drawable.icon;

		intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon));
		return intentShortcut;
	}

	public void gotoContactUs() {
		setCurrentTab(3);
	}
}