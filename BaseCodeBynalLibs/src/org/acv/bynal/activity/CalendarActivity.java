package org.acv.bynal.activity;

import a.com.acv.crash.CrashExceptionHandler;
import android.app.Activity;
import app.bynal.woman.R;

public class CalendarActivity extends Activity {
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		CrashExceptionHandler.onCreate(this);
	}
}