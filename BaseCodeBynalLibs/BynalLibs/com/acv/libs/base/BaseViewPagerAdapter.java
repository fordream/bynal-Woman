package com.acv.libs.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class BaseViewPagerAdapter extends PagerAdapter {
	List<Object> list = new ArrayList<Object>();

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		Object data = list.get(position);
		BaseView view = getView(collection.getContext(), data);
		view.setData(data);
		view.refresh();
		((ViewPager) collection).addView(view);
		return view;
	}

	public BaseView getView(Context context, Object data) {
		return new BaseView(context);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public void clear() {
		list.clear();
	}

	public void setListData(List<Object> list2) {
		list.addAll(list2);
	}

}