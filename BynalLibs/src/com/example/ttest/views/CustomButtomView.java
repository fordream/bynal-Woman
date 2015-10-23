package com.example.ttest.views;

import java.util.StringTokenizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bynal.libs.R;

public class CustomButtomView extends RelativeLayout {
	/**
	 * 
	 */
	private String textbutton = "";
	private int bgbutton = 0;

	public String getTextbutton() {
		return textbutton;
	}

	public void setTextbutton(String textbutton) {
		this.textbutton = textbutton;
		invalidate();
		requestLayout();

	}

	public int getBgbutton() {
		return bgbutton;
	}

	public void setBgbutton(int bgbutton) {
		this.bgbutton = bgbutton;
		invalidate();
		requestLayout();
	}

	/**
	 * 
	 */

	private TextView textView;
	private Button button;

	public CustomButtomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public CustomButtomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		textView = new TextView(getContext());
		button = new Button(getContext());
		button.setTextColor(Color.BLUE);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		addView(textView);
		addView(button);

		textView.setLayoutParams(layoutParams);
		button.setLayoutParams(layoutParams);

		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);
		bgbutton = getIdResource(a.getString(R.styleable.CustomButton_bgbutton));
		String myText = a.getString(R.styleable.CustomButton_textbutton);

		int idResource = getIdResource(myText);
		if (idResource == 0) {
			if (myText != null)
				textbutton = myText;
		} else {
			getResources().getString(idResource);
		}

		button.setText(textbutton);
		button.setBackgroundResource(bgbutton);
		a.recycle();
	}

	private int getIdResource(String res) {
		try {
			if (isResource(res)) {
				StringTokenizer stringTokenizer = new StringTokenizer(res, "/");
				stringTokenizer.nextToken();// remove resource
				String deftype = stringTokenizer.nextToken().replace("@", "");
				String name = stringTokenizer.nextToken();
				// name = "xml_button_custom.xml"
				if (name.contains(".")) {
					name = name.substring(0, name.indexOf("."));
				}
				// deftype == "drawable";
				return getResources().getIdentifier(name, deftype, getContext().getPackageName());
			}
		} catch (Exception exception) {

		}
		return 0;
	}

	private boolean isResource(String str) {
		if (str == null)
			return false;
		return str.startsWith("res/");
	}
}