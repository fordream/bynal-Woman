package a.com.acv.bynal.v3.views;

import java.lang.reflect.Field;
import java.util.List;

import a.com.acv.bynal.v3.FixedSpeedScroller;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.BaseViewPagerAdapter;
import com.acv.libs.nbase.ACVbaseApplication;

public class V3HomeSession1View extends ViewPager {

	public V3HomeSession1View(Context context) {
		super(context);
		init();
	}

	public V3HomeSession1View(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ACVbaseApplication getACVApplication() {
		return (ACVbaseApplication) getContext().getApplicationContext();
	}

	public void init() {

		setAdapter(adapter);

//		try {
//			Field mScroller = this.getClass().getDeclaredField("mScroller");
//			mScroller.setAccessible(true);
//			Scroller scroll = new FixedSpeedScroller(getContext());
//			// Field scrollDuration =
//			// scroll.getClass().getDeclaredField("mDuration");
//			// scrollDuration.setAccessible(true);
//			// scrollDuration.set(scroll, 3000);
//			mScroller.set(this, scroll);
//		} catch (Exception e) {
//		}
		
		setViewPagerScrollSpeed();
	}

	private BaseViewPagerAdapter adapter = new BaseViewPagerAdapter() {
		public Object instantiateItem(View collection, int position) {
			View view = (View) super.instantiateItem(collection, position);
			// Animation animation =
			AnimationUtils.loadAnimation(collection.getContext(), R.anim.scale);
			// view.startAnimation(animation);
			return view;
		}

		@Override
		public BaseView getView(Context context, Object data) {
			return new V3homesesion1itemView(context);
		}
	};

	private void setViewPagerScrollSpeed() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(this.getContext());
			mScroller.set(this, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	public void setDataList(List<Object> list) {
		adapter.clear();
		adapter.setListData(list);
		adapter.notifyDataSetChanged();
		// setAdapter(adapter);
	}

}