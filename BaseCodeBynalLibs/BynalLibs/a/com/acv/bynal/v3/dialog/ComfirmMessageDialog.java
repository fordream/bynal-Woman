package a.com.acv.bynal.v3.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.LibsBaseAdialog;

public class ComfirmMessageDialog extends LibsBaseAdialog implements android.view.View.OnClickListener {
	public ComfirmMessageDialog(Context context, String message, OnClickListener onClickListener, OnClickListener onClickListener2, int strOkie, int strCancel) {
		super(context);

		this.message = message;
		this.onClickOkListener = onClickListener;
		this.onClickCancelListener = onClickListener2;
		this.strOkie = strOkie;
		this.strCancel = strCancel;
	}

	private String message;
	private OnClickListener onClickOkListener, onClickCancelListener;
	private int strOkie = 0;
	private int strCancel = 0;

	private TextView txt_content, txt_okie, txt_cancel;
	private LinearLayout ll_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		txt_content = (TextView) findViewById(R.id.txt_content);
		txt_okie = (TextView) findViewById(R.id.txt_okie);
		txt_cancel = (TextView) findViewById(R.id.txt_cancel);
		ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);

		txt_content.setText(message);
		txt_okie.setText(strOkie);

		if (strCancel == 0)
			ll_cancel.setVisibility(View.GONE);
		else
			txt_cancel.setText(strCancel);
		
		txt_okie.setOnClickListener(this);
		txt_cancel.setOnClickListener(this);

	}

	@Override
	public int getLayout() {
		return R.layout.dialog_comfirm;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v.getId() == R.id.txt_cancel) {
			if (onClickCancelListener != null) {
				onClickCancelListener.onClick(null, 0);
			}

		} else if (v.getId() == R.id.txt_okie) {
			if (onClickOkListener != null) {
				onClickOkListener.onClick(null, 0);
			}
		}
	}
}