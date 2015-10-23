package com.acv.libs.base.callback;

import org.acv.bynal.dialog.BynalProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;

public class ExeCallBackOption {
	private Context context;
	private boolean showDialog;
	private int strResDialog;
	private ProgressDialog mConfig;

	public ExeCallBackOption() {
	}

	/**
	 * 
	 * @param context
	 * @param showDialog
	 * @param strResDialog
	 * @param dialog
	 *            if not null, will use it
	 */
	public ExeCallBackOption(Context context, boolean showDialog, int strResDialog, ProgressDialog dialog) {
		this.context = context;
		this.showDialog = showDialog;
		this.strResDialog = strResDialog;
		this.mConfig = dialog;
	}

	private ProgressDialog progressDialog;

	public void showDialog(boolean b) {
		if (this.showDialog) {

			if (!b) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			} else {
				if (progressDialog == null && context != null) {
					if (mConfig != null) {
						progressDialog = mConfig;
						progressDialog.show();
					} else {
						// , android.R.style.Theme_Translucent_NoTitleBar
						progressDialog = new BynalProgressDialog(context);
						// LinearLayout.LayoutParams layoutParams = new
						// LayoutParams(LayoutParams.MATCH_PARENT,
						// LayoutParams.MATCH_PARENT);
						// progressDialog.addContentView(new
						// LoadingView(context),layoutParams);
						// progressDialog.setContentView(new
						// LoadingView(context));
						progressDialog.show();
						// progressDialog = ProgressDialog.show(context, null,
						// context.getString(strResDialog));
					}
				}
			}
		}
	}

}