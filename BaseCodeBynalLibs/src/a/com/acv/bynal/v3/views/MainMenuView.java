package a.com.acv.bynal.v3.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.menu.MenuItemView;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.example.ttest.views.MenuLeft;

/**
 * a.com.acv.bynal.v3.views.MainMenuView
 * 
 * @author teemo
 * 
 */
public class MainMenuView extends MenuLeft {
	private AccountDB accountDB;

	public MainMenuView(Context context) {
		super(context);
		accountDB = new AccountDB(getContext());
	}

	public MainMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		accountDB = new AccountDB(getContext());
	}

	private Menu menu;

	@Override
	public void addViewContent(FrameLayout frameLayout) {
		super.addViewContent(frameLayout);
		try {
			frameLayout.addView(menu = new Menu(getContext()));
		} catch (Exception exception) {
			Log.e("AAAAA", "AAAAA", exception);
		}
	}

	private class MenuData {
		int resImg;
		int resStr;
	}

	private class Menu extends BaseView {
		private LinearLayout menu_content;

		public Menu(Context context) {
			super(context);
			init(R.layout.v3_menu_left);
			menu_content = (LinearLayout) findViewById(R.id.menu_content);

			findViewById(R.id.menuslide_header_img).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseTabActivity.senBroadCastHeaderMenu(v.getContext(), "home");
				}
			});
			int GROUP[][] = new int[][] { //
			{ R.drawable.menu_home_m1_xml, R.string.menu_home_Login },//
					{ R.drawable.menu_home_m2_xml, R.string.menu_home_project_manager },//
					{ R.drawable.menu_home_m3_xml, R.string.menu_home_message },//
					{ R.drawable.menu_home_m4_xml, R.string.menu_home_news_event },//
					{ R.drawable.menu_home_m5_xml, R.string.menu_home_more },//
					{ R.drawable.menu_home_m7_xml, R.string.menu_home_logout } //
			};//

			int resId[] = new int[] {//
			R.id.menu_m1,//
					R.id.menu_m2,//
					R.id.menu_m3,//
					R.id.menu_m4,//
					R.id.menu_m5,//
					R.id.menu_m6,//
			};

			for (int i = 0; i < resId.length; i++) {
				MainMenuItemView itemView = (MainMenuItemView) findViewById(resId[i]);
				try {
					itemView.setData(GROUP[i]);
					itemView.refresh();
				} catch (Exception exception) {
				}
				// itemView.refresh();
			}

			reload();
		}

		public void reload() {
			try {
				MainMenuItemView menu_m1 = (MainMenuItemView) findViewById(R.id.menu_m1);
				MainMenuItemView menu_m6 = (MainMenuItemView) findViewById(R.id.menu_m6);
				menu_m1.refresh();
				if (ByUtils.isBlank(accountDB.getToken())) {
					menu_m6.setVisibility(View.GONE);
				} else {
					menu_m6.setVisibility(View.VISIBLE);
				}
			} catch (Exception exception) {

			}
		}
	}

	public void reload() {
		// if(accountDB.)
		try {
			menu.reload();
		} catch (Exception exception) {

		}
	}
}