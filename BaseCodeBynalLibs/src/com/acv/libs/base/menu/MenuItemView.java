package com.acv.libs.base.menu;

import org.acv.bynal.views.MenuSlideView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;

public class MenuItemView extends BaseView {
	public interface IHeaderCallBack {
		public void onClickLeft();

		public void onClickRight();
	}

	public MenuItemView(Context context) {
		super(context);
		init();
	}

	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (getContext() instanceof BaseTabActivity) {
			inflater.inflate(R.layout.menuitem, this);
		} else if (MenuSlideView.isRight) {
			inflater.inflate(R.layout.menuitem_right, this);
		} else {
			inflater.inflate(R.layout.menuitem, this);
		}

//		findViewById(R.id.menu_sup_1).setOnClickListener(clickListener);
//		findViewById(R.id.menu_sup_2).setOnClickListener(clickListener);
//		findViewById(R.id.menu_sup_3).setOnClickListener(clickListener);
//		findViewById(R.id.menu_sup_4).setOnClickListener(clickListener);
//		findViewById(R.id.menu_sup_5).setOnClickListener(clickListener);
//		findViewById(R.id.menu_sup_6).setOnClickListener(clickListener);
	}

//	private View.OnClickListener clickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			
//		}
//	};

	public void setData(MenuItem menuItem) {

		if (menuItem.getResImgge() != 0) {
			// findViewById(R.id.menu_img).setBackgroundResource(menuItem.getResImgge());
			if (MenuSlideView.isRight) {
				findViewById(R.id.menu_img).setBackgroundResource(menuItem.getResImgge());
			}
			findViewById(R.id.menu_img_header).setBackgroundResource(menuItem.getResImgge());
		}
		if (getContext() instanceof BaseTabActivity) {
			if (R.drawable.menu_home_m5 == menuItem.getResImgge()) {
				// findViewById(R.id.menuitem_background).setBackgroundResource(R.drawable.white);
			} else {
				// findViewById(R.id.menuitem_background).setBackgroundResource(R.drawable.transfer);
			}
		}
		if (MenuSlideView.isRight) {
			((TextView) findViewById(R.id.menutextView1)).setText(menuItem.getName());
		}
		((TextView) findViewById(R.id.menutextview_header)).setText(menuItem.getName());

		findViewById(R.id.menu_header).setVisibility(View.GONE);
		findViewById(R.id.menusub).setVisibility(View.GONE);
		if (menuItem.isHeader) {
			findViewById(R.id.menu_header).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.menusub).setVisibility(View.VISIBLE);
		}

		if ((menuItem.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_home_profile))) {

			AccountDB accountDB = new AccountDB(getContext());
			((TextView) findViewById(R.id.menutextView1)).setText(accountDB.getName());
			((TextView) findViewById(R.id.menutextview_header)).setText(accountDB.getName());
		}

	}

}