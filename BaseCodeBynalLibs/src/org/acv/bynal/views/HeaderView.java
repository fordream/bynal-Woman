package org.acv.bynal.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;

public class HeaderView extends BaseView {
	private ImageButton headerbutton1, headerbutton2, headerbutton3;

	public interface IHeaderCallBack {
		public void onClickLeft();

		public void onClickRight();
	}

	public HeaderView(Context context) {
		super(context);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.header, this);
		try {
			headerbutton3 = (ImageButton) findViewById(R.id.headerbutton3);
			headerbutton2 = (ImageButton) findViewById(R.id.headerbutton2);
			headerbutton1 = (ImageButton) findViewById(R.id.headerbutton1);
			headerbutton1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (headerOption != null)
						headerOption.onClickButtonLeft();
				}
			});

			headerbutton2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (headerOption != null)
						headerOption.onClickButtonRight();
				}
			});
			
			headerbutton3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (headerOption != null)
						headerOption.onClickButtonRight2();
				}
			});
		} catch (Exception exception) {
		}

	}

	public static final String KEYCHOOSER = "CHOOSER_POSITION";

	private void sendPosition(int i) {
		Intent intent = new Intent(KEYCHOOSER);
		intent.putExtra(KEYCHOOSER, i);
		getContext().sendBroadcast(intent);
	}

	public void setText(int str) {
		((TextView) findViewById(R.id.headertextView1)).setText(str);
	}

	private HeaderOption headerOption;

	public void setheaderOption(HeaderOption headerOption) {
		this.headerOption = headerOption;
		refresh();
	}

	public void refresh() {
		if (headerOption != null) {

			if (headerOption.getTypeHeader() == TYPEHEADER.CHECKBOX) {
				// findViewById(R.id.headerradiogroup).setVisibility(View.VISIBLE);
				findViewById(R.id.headertextView1).setVisibility(GONE);
			} else {
				// findViewById(R.id.headerradiogroup).setVisibility(View.GONE);
				findViewById(R.id.headertextView1).setVisibility(VISIBLE);
			}

			if (headerOption.getResHeader() != R.string.blank) {
				if (headerOption.getResHeader() == R.string.menu_home_message) {
					setText(R.string.header_message_room);
				} else {
					setText(headerOption.getResHeader());
				}
			}

			if (headerOption.getResHeader() == R.string.home) {
				findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
				findViewById(R.id.headertextView1).setVisibility(View.GONE);
			} else {
				findViewById(R.id.home_logo).setVisibility(View.GONE);
				if (headerOption.getTypeHeader() != TYPEHEADER.CHECKBOX) {
					findViewById(R.id.headertextView1).setVisibility(View.VISIBLE);
				}
			}

			headerbutton1.setImageResource(headerOption.getResDrawableLeft());
			headerbutton2.setImageResource(headerOption.getResDrawableRight());
			headerbutton3.setImageResource(headerOption.getResDrawableRight2());

			if (headerOption.isShowButtonLeft()) {
				findViewById(R.id.headerbutton1).setVisibility(VISIBLE);
			} else {
				findViewById(R.id.headerbutton1).setVisibility(GONE);
			}

			if (headerOption.isShowButtonRight()) {
				findViewById(R.id.headerbutton2).setVisibility(VISIBLE);
			} else {
				findViewById(R.id.headerbutton2).setVisibility(GONE);
			}

			if (headerOption.isShowButtonRight2()) {
				findViewById(R.id.headerbutton3).setVisibility(VISIBLE);
			} else {
				findViewById(R.id.headerbutton3).setVisibility(GONE);
			}
			// Animation animation = AnimationUtils.loadAnimation(getContext(),
			// R.anim.scale);
			// findViewById(R.id.headerbutton2).startAnimation(animation);
			// findViewById(R.id.headerbutton1).startAnimation(animation);
			// findViewById(R.id.home_logo).startAnimation(animation);
			// findViewById(R.id.headertextView1).startAnimation(animation);

		}
	}

	public void reloadChooser() {
		// RadioButton radioButton = (RadioButton)
		// findViewById(R.id.headerradiogroup1);
		// radioButton.setChecked(true);
	}

	public void setText(String str) {
		((TextView) findViewById(R.id.headertextView1)).setText(str);
	}
}