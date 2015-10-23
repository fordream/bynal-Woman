package a.com.acv.crash;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
	public static final void onCreate(Activity activity) {
		Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(activity));
	}

	public static final void sendCrash(Activity activity) {
//		StringBuilder builder = new StringBuilder();
//
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(activity.openFileInput("stack.trace")));
//			String trace = null;
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				if (line != null) {
//					builder.append(line).append("\n");
//				}
//			}
//		} catch (FileNotFoundException fnfe) {
//		} catch (IOException ioe) {
//		}
//
//		Intent sendIntent = new Intent(Intent.ACTION_SEND);
//		String subject = "Error report";
//		String body = "Mail this to appdeveloper@gmail.com: " + "\n" + builder.toString() + "\n";
//
//		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "readerscope@altcanvas.com" });
//		sendIntent.putExtra(Intent.EXTRA_TEXT, body);
//		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//		sendIntent.setType("message/rfc822");
//
//		activity.startActivity(Intent.createChooser(sendIntent, "Title:"));
//
//		activity.deleteFile("stack.trace");
	}

	private Thread.UncaughtExceptionHandler defaultUEH;

	private Activity app = null;

	public CrashExceptionHandler(Activity app) {
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		this.app = app;
	}

	public void uncaughtException(Thread t, Throwable e) {
		StackTraceElement[] arr = e.getStackTrace();
		String report = e.toString() + "\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i = 0; i < arr.length; i++) {
			report += "    " + arr[i].toString() + "\n";
		}
		report += "-------------------------------\n\n";

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if (cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i = 0; i < arr.length; i++) {
				report += "    " + arr[i].toString() + "\n";
			}
		}
		report += "-------------------------------\n\n";

		try {
			FileOutputStream trace = app.openFileOutput("stack.trace", Context.MODE_PRIVATE);
			trace.write(report.getBytes());
			trace.close();
		} catch (IOException ioe) {
		}

		defaultUEH.uncaughtException(t, e);
	}
}