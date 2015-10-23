package org.acv.bynal.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;

//org.acv.bynal.views.ProgressView
public class ProgressView extends BaseView implements OnClickListener {
	private TextView progress_title, progress_tv_per, progress_total;
	private ProgressBar progress_progressbar;

	public ProgressView(Context context) {
		super(context);
		init(R.layout.progress);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.progress);

	}

	public void setCurrent(final int current) {
		post(new Runnable() {

			@Override
			public void run() {
				progress_tv_per.setText(current + "%");
				progress_total.setText(current + "/" + 100);
				progress_progressbar.setProgress(current);
			}
		});
	}

	@Override
	public void init(int res) {
		super.init(res);
		try {
			progress_title = (TextView) findViewById(R.id.progress_title);
			progress_tv_per = (TextView) findViewById(R.id.progress_tv_per);
			progress_total = (TextView) findViewById(R.id.progress_total);
			progress_progressbar = (ProgressBar) findViewById(R.id.progress_progressbar);
			progress_progressbar.setEnabled(false);
			progress_progressbar.setMax(100);
		} catch (Exception exception) {

		}
	}

	@Override
	public void refresh() {
		super.refresh();

	}

	@Override
	public void onClick(View v) {
	}
}