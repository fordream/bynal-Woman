package com.example.ttest.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bynal.libs.R;

public class MenuLeft extends LinearLayout {
	private IMenuLeftListener iMenuLeftListener;
	public void setiMenuLeftListener(IMenuLeftListener iMenuLeftListener) {
		this.iMenuLeftListener = iMenuLeftListener;
	}
	public MenuLeft(Context context) {
		super(context);
		init();
	}

	public boolean isOpenMenu() {
		RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
		return layoutParam.leftMargin == 0;
	}

	public void openMenu() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getMAnimation();
				RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
				int left = layoutParam.leftMargin;
				int add = (int) getResources().getDimension(R.dimen.dimen_50dp);
				if (left == 0) {
					_menu_main.setVisibility(View.VISIBLE);
					enableOpenOrClose = false;
					if(iMenuLeftListener!= null)iMenuLeftListener.onOpenedMenu();
					return;
				}

				enableOpenOrClose = true;
				left = left + add;

				if (left >= 0) {
					left = 0;
				}

				layoutParam.setMargins(left, 0, 0, 0);

				menuleft_listview.setLayoutParams(layoutParam);
				openMenu();
			}
		}, 10);
	}

	public interface ICloseMenuListener {
		public void closed();
	}

	public void closeMenu(final ICloseMenuListener menuListener) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getMAnimation();
				RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
				int left = layoutParam.leftMargin;
				int add = (int) getResources().getDimension(R.dimen.dimen_50dp);
				if (left == -widthList) {
					_menu_main.setVisibility(View.GONE);
					enableOpenOrClose = false;
					if(iMenuLeftListener!= null)iMenuLeftListener.onClosedMenu();
					if (menuListener != null) {
						menuListener.closed();
					}
					return;
				}

				enableOpenOrClose = true;
				if (left <= 0) {
					left = left - add;
				}

				if (left <= -widthList) {
					left = -widthList;
				}
				layoutParam.setMargins(left, 0, 0, 0);

				menuleft_listview.setLayoutParams(layoutParam);

				closeMenu(menuListener);
			}
		}, 10);
	}

	public MenuLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private View _menu_left;
	private FrameLayout menuleft_listview;
	private View _menu_main, _menu_main_background;

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu, this);
		_menu_left = findViewById(R.id._menu_left);
		_menu_main = findViewById(R.id._menu_main);
		_menu_main_background = findViewById(R.id._menu_main_background);

		_menu_main.setVisibility(View.GONE);
		menuleft_listview = (FrameLayout) findViewById(R.id.menuleft_listview);
		_menu_left.setOnTouchListener(onTouchMenuLeftListener);
		_menu_main.setOnTouchListener(onTouchMenuLeftListener);
		addViewContent(menuleft_listview);
		post(new Runnable() {
			@Override
			public void run() {
				RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
				widthList = layoutParam.width;
				layoutParam.setMargins(-widthList, 0, 0, 0);
				menuleft_listview.setLayoutParams(layoutParam);
				_menu_main_background.setBackgroundColor(Color.BLACK);
				getMAnimation();
			}
		});
	}

	public void addViewContent(FrameLayout frameLayout) {

	}

	@SuppressLint("NewApi")
	private void getMAnimation() {
		RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
		float f = (float) (layoutParam.leftMargin + widthList) / (float) widthList;
		if (f > 0.8) {
			f = 0.8f;
		}

		_menu_main_background.setAlpha(f);
	}

	private int widthList;

	private OnTouchListener onTouchMenuLeftListener = new OnTouchListener() {

		float startX = -1, startY = -1;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (enableOpenOrClose) {
				return false;
			}
			float x = event.getX();
			float y = event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if(iMenuLeftListener!= null)iMenuLeftListener.onStartMovieMenu();
				startX = x;
				startY = y;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if(iMenuLeftListener!= null)iMenuLeftListener.onMovieMenu();
				if (v == _menu_main) {
					movieToEnd(x, y);
					getMAnimation();
				} else {
					movie(x, y);
					getMAnimation();
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
				final int dx = (int) (x - startX);
				check(dx);
				startX = -1;
				startY = -1;
			}

			return true;
		}

		private void movie(float x, float y) {
			final int dx = (int) (x - startX);
			RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
			int left = -widthList + (dx >= 0 ? (dx >= widthList ? widthList : dx) : 0);
			layoutParam.setMargins(left, 0, 0, 0);
			menuleft_listview.setLayoutParams(layoutParam);
		}

		private void movieToEnd(float x, float y) {
			final int dx = (int) (x - startX);
			RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
			int left = 0;

			if (left + dx > 0)
				left = 0;
			else if (left + dx < -widthList) {
				left = -widthList;
			} else
				left = left + dx;
			layoutParam.setMargins(left, 0, 0, 0);
			menuleft_listview.setLayoutParams(layoutParam);
		}
	};
	private Handler handler = new Handler();

	private void check(final int dx) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getMAnimation();
				RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) menuleft_listview.getLayoutParams();
				int left = layoutParam.leftMargin;
				int add = (int) getResources().getDimension(R.dimen.dimen_10dp);
				if (left == 0 || left == -widthList) {
					if (left == -widthList) {
						_menu_main.setVisibility(View.GONE);
						if(iMenuLeftListener!= null)iMenuLeftListener.onClosedMenu();
					} else {
						if(iMenuLeftListener!= null)iMenuLeftListener.onOpenedMenu();
						_menu_main.setVisibility(View.VISIBLE);
					}

					enableOpenOrClose = false;
					return;
				}

				enableOpenOrClose = true;
				if (left < -widthList / 2) {
					// hidden
					left = left - add;
				} else {
					left = left + add;
					// show
				}

				if (left <= -widthList) {
					left = -widthList;
				}
				if (left >= 0) {
					left = 0;
				}
				layoutParam.setMargins(left, 0, 0, 0);

				menuleft_listview.setLayoutParams(layoutParam);

				check(0);
			}
		}, 10);
	}

	private boolean enableOpenOrClose = false;

}