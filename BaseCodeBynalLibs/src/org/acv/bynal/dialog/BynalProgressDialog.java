package org.acv.bynal.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import app.bynal.woman.R;

public class BynalProgressDialog extends ProgressDialog {

	public BynalProgressDialog(Context context) {
		super(context);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
	}
}