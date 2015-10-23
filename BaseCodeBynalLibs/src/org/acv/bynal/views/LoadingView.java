package org.acv.bynal.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;

public class LoadingView extends BaseView implements OnClickListener {
	private TextView textView;
	private ProgressBar progressBar;
	
	public LoadingView(Context context) {
		super(context);
		init(R.layout.loading);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.loading);
	}

	@Override
	public void init(int res) {
		super.init(res);
		textView = (TextView) findViewById(R.id.loading_error);
		progressBar = (ProgressBar) findViewById(R.id.loading_progress);
		textView.setText("");
		
//		findViewById(R.id.loading_main).setOnClickListener(null);
	}

	@Override
	public void refresh() {
		super.refresh();

	}

	@Override
	public void onClick(View v) {
	}

	public void endProgressBar(String text) {
		progressBar.setVisibility(View.GONE);
		findViewById(R.id.loading_main).setVisibility(View.GONE);
		textView.setText(text);
	}
	
	public void startProgressBar(){
		progressBar.setVisibility(View.VISIBLE);
		findViewById(R.id.loading_main).setVisibility(View.VISIBLE);
		textView.setText("");
	}

}