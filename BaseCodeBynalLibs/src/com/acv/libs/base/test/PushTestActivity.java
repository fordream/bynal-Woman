package com.acv.libs.base.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import app.bynal.woman.R;

public class PushTestActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_m1);
		findViewById(R.id.message).setOnClickListener(this);
		findViewById(R.id.new_pj).setOnClickListener(this);
		findViewById(R.id.support_pj).setOnClickListener(this);
		findViewById(R.id.favorite_pj).setOnClickListener(this);
		findViewById(R.id.report_pj).setOnClickListener(this);
	}

	private static final String parttern = "\"%s\":\"%s\"";

	@Override
	public void onClick(View v) {

		String title = ((Button) v).getText().toString();

		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.format(parttern, "title", title)).append(",");
		builder.append(String.format(parttern, "project_id", "123"))
				.append(",");
		builder.append(String.format(parttern, "description", title)).append(
				",");
		builder.append(
				String.format(parttern, "username_support", "username_support"))
				.append(",");
		builder.append(String.format(parttern, "userid_project", "11")).append(
				",");
		builder.append(String.format(parttern, "username_addfavorite", "12"));
		builder.append("}");

		Intent intent = new Intent("com.google.android.c2dm.intent.RECEIVE");
		intent.addCategory("app.bynal.woman");
		intent.putExtra("message", builder.toString());
		sendBroadcast(intent);
		Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
	}
}
