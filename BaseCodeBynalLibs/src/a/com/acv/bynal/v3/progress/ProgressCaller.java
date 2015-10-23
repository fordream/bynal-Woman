package a.com.acv.bynal.v3.progress;

import android.content.Context;

import com.acv.libs.base.callback.CallBack;
import com.acv.libs.base.callback.ExeCallBack;

public abstract class ProgressCaller extends ExeCallBack {
	private Context mContext;
	private String message;

	public Context getmContext() {
		return mContext;
	}

	public ProgressCaller(Context context, String message) {
		mContext = context;
		this.message = message;
	}

	public void executeAsynCallBack() {
		executeAsynCallBack(new CallBack() {

			@Override
			public Object execute() {
				onSaveData(message);
				return null;
			}

			@Override
			public void onCallBack(Object object) {

			}

		});
	}

	public abstract void onSaveData(String message);
}