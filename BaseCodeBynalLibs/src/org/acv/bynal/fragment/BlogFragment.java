package org.acv.bynal.fragment;

import java.util.List;

import org.acv.bynal.views.HeaderView;

import a.com.acv.bynal.v3.database.DBOperations;
import a.com.acv.bynal.v3.database.NewAndEventTable;
import a.com.acv.bynal.v3.database.Table;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import app.bynal.woman.R;

import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseFragment;

public class BlogFragment extends BaseFragment {

	public BlogFragment() {
	}

	@Override
	public void setUpFragment(View view) {
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());

	}
	
	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		WebView view2 = (WebView) getView().findViewById(R.id.webView1);
		view2.getSettings().setJavaScriptEnabled(true);
		view2.setLongClickable(false);
		view2.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		view2.loadUrl(ByUtils.BASESERVER_BLOG);
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public int layoytResurce() {
		return R.layout.av3abount;
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.menu_home_blog);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}
