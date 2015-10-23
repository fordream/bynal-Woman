package org.acv.bynal.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.nbase.ACVbaseApplication;

//org.acv.bynal.views.BaseBoldRadioButton
public class BaseBoldRadioButton extends RadioButton {

	public BaseBoldRadioButton(Context context) {
		super(context);
		try {
			setTypeface(((ACVbaseApplication) getContext().getApplicationContext()).getTypefaceBold());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);

		setTextColor(!checked ? Color.WHITE : getContext().getResources().getColor(R.color.unchecked));
	}

	public BaseBoldRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			setTypeface(((ACVbaseApplication) getContext().getApplicationContext()).getTypefaceBold());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}

	public BaseBoldRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try {
			setTypeface(((ACVbaseApplication) getContext().getApplicationContext()).getTypefaceBold());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}
}