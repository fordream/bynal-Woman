package com.acv.libs.base.callback;

import android.os.AsyncTask;

public class ExeCallBack {

	private ExeCallBackOption exeCallBackOption = new ExeCallBackOption();

	public void setExeCallBackOption(ExeCallBackOption exeCallBackOption) {
		this.exeCallBackOption = exeCallBackOption;
	}

	public ExeCallBack() {
	}

	public boolean executeCallBack(Object object) {

		if (canCallBack(object)) {
			Object object2 = ((CallBack) object).execute();
			((CallBack) object).onCallBack(object2);
			return true;
		}

		return false;
	}

	public boolean executeAsynCallBack(Object object) {
		if (canCallBack(object)) {
			if (exeCallBackOption != null) {
				exeCallBackOption.showDialog(true);
			}
			new AsyncTask<Object, Object, Object>() {
				private Object callBack;

				@Override
				protected Object doInBackground(Object... params) {
					callBack = params[0];
					return ((CallBack) callBack).execute();
				}

				protected void onPostExecute(Object result) {
					((CallBack) callBack).onCallBack(result);
					onCallBack(result);
					if (exeCallBackOption != null) {
						exeCallBackOption.showDialog(false);
					}
				};
			}.execute(object);
			return true;
		}

		return false;
	}

	public void onCallBack(Object result) {

	}

	private boolean canCallBack(Object object) {
		return object != null && object instanceof CallBack;
	}
}