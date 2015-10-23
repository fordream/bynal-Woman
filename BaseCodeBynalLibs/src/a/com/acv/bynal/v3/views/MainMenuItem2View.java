package a.com.acv.bynal.v3.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
public class MainMenuItem2View extends BaseView {

	public MainMenuItem2View(Context context) {
		super(context);
		init(R.layout.menuleftitem_content);
	}

	public MainMenuItem2View(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.menuleftitem_content);
	}

	@Override
	public void refresh() {
		super.refresh();
		if (getData() != null) {
			int[] daa = (int[]) getData();
			View view = findViewById(R.id.ImageView04);
			TextView textView = (TextView) findViewById(R.id.BaseTextView04);
			view.setBackgroundResource(daa[0]);
			textView.setText(daa[1]);
		}

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// PRESSED
					Log.e("AAAAA", "PRESSED");
					findViewById(R.id.menu_sup_1).setEnabled(false);
					findViewById(R.id.ImageView04).setEnabled(false);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					// RELEASED
					Log.e("AAAAA", "RELEASED");
					findViewById(R.id.menu_sup_1).setEnabled(true);
					findViewById(R.id.ImageView04).setEnabled(true);
					
					 int mid = ((int[]) getData())[1];
					 BaseTabActivity.senBroadCast(getContext(), mid);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_CANCEL:
					// RELEASED
					Log.e("ACTION_CANCEL", "ACTION_CANCEL");
					findViewById(R.id.menu_sup_1).setEnabled(true);
					findViewById(R.id.ImageView04).setEnabled(true);
					return true; // if you want to handle the touch event
				}
				return false;
			}
		});

	}
}