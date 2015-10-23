package a.com.acv.bynal.v3;

import java.util.ArrayList;
import java.util.List;

import a.com.acv.bynal.v3.views.V3LoginView;
import a.com.acv.bynal.v3.views.V3RegisterView;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.BaseViewPagerAdapter;

// a.com.acv.bynal.v3.NoScollViewPager
public class NoScollViewPager extends ViewPager {

	public NoScollViewPager(Context context) {
		super(context);
		init();
	}

	public NoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private BaseViewPagerAdapter basePagerAdapter = new BaseViewPagerAdapter() {
		@Override
		public BaseView getView(Context context, Object data) {
			if (data != null && data.toString().equals("1")) {
				return new V3LoginView(getContext());
			}

			return new V3RegisterView(getContext());
			// return baseView;
		}
	};

	private void init() {
		List<Object> list2 = new ArrayList<Object>();
		list2.add(1 + "");
		list2.add(2 + "");
		basePagerAdapter.setListData(list2);
		setAdapter(basePagerAdapter);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return true;
	}

}
