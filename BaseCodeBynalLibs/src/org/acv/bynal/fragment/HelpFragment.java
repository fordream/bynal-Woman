package org.acv.bynal.fragment;

import org.acv.bynal.views.HeaderView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.nbase.BaseFragment;

public class HelpFragment extends BaseFragment {

	public HelpFragment() {
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
		view2.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//				CommonAndroid.showDialog(getActivity(), url + "", null);
//				super.onPageStarted(view, url, favicon);
//			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals("http://www.contacts.com/")) {
					((BaseTabActivity) getActivity().getParent()).gotoContactUs();
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});

		view2.loadUrl("file:///android_asset/more-final/13_help.html");

	}

	@Override
	public int layoytResurce() {
		return R.layout.av3help;
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.menu_home_help);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}
