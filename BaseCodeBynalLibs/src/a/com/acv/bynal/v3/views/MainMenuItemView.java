package a.com.acv.bynal.v3.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;

/**
 * a.com.acv.bynal.v3.views.MainMenuItemView
 * 
 * @author teemo
 * 
 */
public class MainMenuItemView extends BaseView implements View.OnClickListener {
	private LinearLayout menu_header_items;

	public MainMenuItemView(Context context) {
		super(context);
		init(R.layout.menuleftitem_header);
	}

	public MainMenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.menuleftitem_header);
	}

	@Override
	public void init(int res) {
		super.init(res);
		menu_header_items = (LinearLayout) findViewById(R.id.menu_header_items);
	}

	@Override
	public void refresh() {
		super.refresh();
		if (getData() != null) {
			final int[] daa = (int[]) getData();
			View view = findViewById(R.id.menu_img_header);
			TextView textView = (TextView) findViewById(R.id.menutextview_header);
			view.setBackgroundResource(daa[0]);
			textView.setText(daa[1]);

			if (daa[0] == R.drawable.menu_home_m1_xml) {
				AccountDB accountDB = new AccountDB(getContext());
				if (!ByUtils.isBlank(accountDB.getToken())) {
					textView.setText(accountDB.getName());
				}
			}
			setOnTouchListener(new OnTouchListener() {
				private boolean start = true;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						start = true;
						// PRESSED
						Log.e("AAAAA", "PRESSED");
						findViewById(R.id.menu_header).setEnabled(false);
						findViewById(R.id.menu_img_header).setEnabled(false);
						return true; // if you want to handle the touch event

					case MotionEvent.ACTION_MOVE:
						Log.e("AAAAA", "ACTION_MOVIE " + event.getY() + " : " + v.getHeight());
						Log.e("AAAAA", "ACTION_MOVIE");
						if (event.getY() < 0 || event.getY() > v.getHeight()) {
							start = false;
							findViewById(R.id.menu_header).setEnabled(true);
							findViewById(R.id.menu_img_header).setEnabled(true);
						} else {
							if (start) {
								findViewById(R.id.menu_header).setEnabled(false);
								findViewById(R.id.menu_img_header).setEnabled(false);
							}
						}
						return true;
					case MotionEvent.ACTION_UP:

						// RELEASED
						Log.e("AAAAA", "RELEASED");
						findViewById(R.id.menu_header).setEnabled(true);
						findViewById(R.id.menu_img_header).setEnabled(true);

						if (start) {
							if (daa[0] == R.drawable.menu_home_m5_xml) {
								onClick(null);
							} else {
								int mid = ((int[]) getData())[1];
								if (mid == R.string.menu_home_Login && !ByUtils.isBlank(new AccountDB(getContext()).getToken())) {
									mid = R.string.menu_home_profile;
								}

								BaseTabActivity.senBroadCast(getContext(), mid);
							}
						}
						start = true;
						return true; // if you want to handle the touch event
					case MotionEvent.ACTION_CANCEL:
						// RELEASED

						Log.e("ACTION_CANCEL", "ACTION_CANCEL");
						findViewById(R.id.menu_header).setEnabled(true);
						findViewById(R.id.menu_img_header).setEnabled(true);
						start = true;
						return true; // if you want to handle the touch event
					}
					return false;
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		if (v == null)
			v = this;
		int CHILD[][] = new int[][] { //
		{ R.drawable.menu_child_m1_xml, R.string.menu_home_contact_us }, //
				{ R.drawable.menu_child_m2_xml, R.string.menu_home_about },//
				{ R.drawable.menu_child_m3_xml, R.string.menu_home_help }, //
				{ R.drawable.menu_child_m4_xml, R.string.menu_home_x1 }, //
				{ R.drawable.menu_child_m5_xml, R.string.menu_home_x2 },//
				{ R.drawable.menu_child_m6_xml, R.string.menu_home_x3 },//
		};//

		if (menu_header_items.getChildCount() == 0) {
			v.findViewById(R.id.menu_header_img).setBackgroundResource(R.drawable.menu_m2);
			addChild(0, CHILD);
		} else if (menu_header_items.getChildCount() == CHILD.length) {
			v.findViewById(R.id.menu_header_img).setBackgroundResource(R.drawable.menu_m1);
			removeChild();
		}
	}

	private Handler handler = new Handler();

	private void removeChild() {
		if (menu_header_items.getChildCount() == 0) {
			return;
		}

		final View view = menu_header_items.getChildAt(menu_header_items.getChildCount() - 1);

		menu_header_items.removeView(view);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				removeChild();
			}
		}, 20);

	}

	private void addChild(final int position, final int[][] cHILD) {

		if (position >= cHILD.length) {
			return;
		}

		MainMenuItem2View mainMenuItem2View = new MainMenuItem2View(getContext());
		mainMenuItem2View.setData(cHILD[position]);
		mainMenuItem2View.refresh();
		menu_header_items.addView(mainMenuItem2View);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				addChild(position + 1, cHILD);
			}
		}, 20);
	}
}
