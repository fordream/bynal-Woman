package org.acv.bynal.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.menu.MenuItemView;

public class MenuSlideView extends BaseView implements OnClickListener,
		OnItemClickListener {
	private OnItemChooser onItemClickListener;

	public static final long TIMECALLMENU = 500;
	public ListView menu_list;
	public LinearLayout menu_main;
	public LinearLayout menu_click;
	private MenuAdapter adapter = new MenuAdapter();
	private LinearLayout menu_main_content;
	private boolean isExpanded = false;
	private int screenWidth;
	private int widthLayout = 0;
	private boolean onRunAnimation = false;
	public boolean onSubmenu = false;
	public static boolean isRight = false;

	public MenuSlideView(Context context) {
		super(context);
		init();
	}

	public MenuSlideView(Context context, boolean isLeft) {
		super(context);
		this.isLeft = isLeft;
		init();
	}

	public MenuSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.menuslide, this);

		menu_list = (ListView) findViewById(R.id.menu_list);
		menu_main = (LinearLayout) findViewById(R.id.menu_main);
		menu_click = (LinearLayout) findViewById(R.id.menu_click);
		menu_main_content = (LinearLayout) findViewById(R.id.menu_main_content);
		menu_click.setOnClickListener(this);
		menu_list.setOnItemClickListener(this);
		menu_list.setAdapter(adapter);

		if (isLeft) {
			((LinearLayout) findViewById(R.id.menu_main))
					.setGravity(Gravity.LEFT);
		} else {
			((LinearLayout) findViewById(R.id.menu_main))
					.setGravity(Gravity.RIGHT);
		}
		/**
		 * hidden menu
		 */

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		widthLayout = (int) (0.7 * screenWidth);
		widthLayout = (int) getResources().getDimension(R.dimen.dimen_214dp);
		LayoutParams layoutParams = new LayoutParams(widthLayout,
				LayoutParams.MATCH_PARENT);
		menu_main_content.setLayoutParams(layoutParams);
		callMenu(0);

		// resize(findViewById(R.id.menuslide_header),
		// LayoutParams.MATCH_PARENT,
		// 50);
		// resize(findViewById(R.id.menuslide_header_img), 119, 15);
	}

	public void setOnItemChooser(OnItemChooser onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void callMenu(final long time) {

		if (onRunAnimation) {
			return;
		}

		if (time == 0) {
			menu_click.setVisibility(GONE);
			menu_list.setVisibility(GONE);
		}

		TranslateAnimation animation = null;
		AnimationListener animationListener = null;
		if (isExpanded) {
			animation = new TranslateAnimation(-widthLayout, 0, 0, 0);
			if (!isLeft) {
				animation = new TranslateAnimation(widthLayout, 0, 0, 0);
			}
			animationListener = new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if (time == 0) {
						menu_click.setVisibility(GONE);
						menu_list.setVisibility(GONE);
					} else {
						onRunAnimation = true;
						menu_click.setVisibility(VISIBLE);
						menu_list.setVisibility(VISIBLE);
					}

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					onRunAnimation = false;
				}
			};
		} else {
			animation = new TranslateAnimation(0, -widthLayout, 0, 0);
			if (!isLeft) {
				animation = new TranslateAnimation(0, widthLayout, 0, 0);
			}
			animationListener = new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if (time == 0) {
						menu_click.setVisibility(GONE);
						menu_list.setVisibility(GONE);
					} else {
						onRunAnimation = true;
					}

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					onRunAnimation = false;
					menu_click.setVisibility(GONE);
					menu_list.setVisibility(GONE);
				}
			};
		}

		isExpanded = !isExpanded;
		animation.setAnimationListener(animationListener);
		animation.setFillAfter(true);
		animation.setDuration(time);
		startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		callMenu(TIMECALLMENU);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (!onRunAnimation) {
			if (!onSubmenu) {
				callMenu(TIMECALLMENU);
			}

			if (onItemClickListener != null) {
				onItemClickListener.onItemClick(position,
						(MenuItem) adapter.getItem(position));
			}
		}

	}

	public void addmenu(MenuItem item) {
		if (item != null) {
			adapter.add(item);
		}
	}

	public void clear() {
		adapter.clear();
		adapter.notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	/**/
	private class MenuAdapter extends BaseAdapter {
		List<MenuItem> items = new ArrayList<MenuItem>();

		@Override
		public int getCount() {
			return items.size();
		}

		public void clear() {
			items.clear();
		}

		public void add(MenuItem item) {
			items.add(item);
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new MenuItemView(parent.getContext());
			}

			((MenuItemView) convertView).setData(items.get(position));

			return convertView;
		}
	}

	public interface OnItemChooser {
		public void onItemClick(int position, MenuItem item);
	}

	private boolean isLeft = false;
}