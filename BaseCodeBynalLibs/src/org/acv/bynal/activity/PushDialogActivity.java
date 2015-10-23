package org.acv.bynal.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import com.acv.libs.base.CommonAndroid;

//import com.xing.joy.processdata.ApplicationData;

public class PushDialogActivity extends Activity {

	// ApplicationData data;
	String message;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CommonAndroid.showDialog(this,
				getIntent().getExtras().getString("message"),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		// setContentView(R.layout.push_dialog);
		//
		// TextView textView = (TextView) findViewById(R.id.message);
		// textView.setText(getIntent().getExtras().getString("message"));
		// findViewById(R.id.button_1).setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });

	}
}
