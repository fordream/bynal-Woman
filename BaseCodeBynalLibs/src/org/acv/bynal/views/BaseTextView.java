package org.acv.bynal.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.acv.libs.nbase.ACVbaseApplication;

//org.acv.bynal.views.BaseTextView
public class BaseTextView extends TextView {

	public BaseTextView(Context context) {
		super(context);
		try {
			setTypeface(((ACVbaseApplication) getContext()
					.getApplicationContext()).getTypeface());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}

	public BaseTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			setTypeface(((ACVbaseApplication) getContext()
					.getApplicationContext()).getTypeface());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}

	public BaseTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try {
			setTypeface(((ACVbaseApplication) getContext()
					.getApplicationContext()).getTypeface());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}
}