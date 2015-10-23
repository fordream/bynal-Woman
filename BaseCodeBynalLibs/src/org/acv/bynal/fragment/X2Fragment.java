package org.acv.bynal.fragment;

import org.acv.bynal.views.HeaderView;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import app.bynal.woman.R;

import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.nbase.BaseFragment;

public class X2Fragment extends BaseFragment {

	public X2Fragment() {
	}

	@Override
	public void setUpFragment(View view) {
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());

	}

	@Override
	public void onResume() {
		super.onResume();
		WebView view2 = (WebView) getView().findViewById(R.id.webView1);
		view2.getSettings().setJavaScriptEnabled(true);
		view2.setLongClickable(false);
		view2.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		view2.loadUrl("file:///android_asset/more-final/15_trade-law.html");
	}

	@Override
	public int layoytResurce() {
		return R.layout.av3abount;
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.menu_home_x2);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}
