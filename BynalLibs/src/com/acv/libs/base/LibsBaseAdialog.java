package com.acv.libs.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.bynal.libs.R;

public abstract class LibsBaseAdialog extends Dialog {

	public DialogInterface.OnClickListener clickListener;

	public void setText(int registerfacebooktwitterEdt1, String name) {
		((TextView) findViewById(registerfacebooktwitterEdt1)).setText(name);
	}

	public String getTextStr(int res) {
		return ((TextView) findViewById(res)).getText().toString().trim();
	}

	private Context mContext;

	public Context getmContext() {
		return mContext;
	}

	public LibsBaseAdialog(Context context, DialogInterface.OnClickListener clickListener) {
		// super(context);
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		this.clickListener = clickListener;
	}

	public LibsBaseAdialog(Context context) {
		super(context, R.style.PauseDialog);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		if (getLayout() != 0)
			setContentView(getLayout());
	}

	public abstract int getLayout();

	public void showDialogMessage(String message) {
		CommonAndroid.showDialog(getContext(), message, null);
	}
}