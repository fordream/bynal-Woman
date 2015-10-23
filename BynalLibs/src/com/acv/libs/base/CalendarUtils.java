package com.acv.libs.base;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;

@SuppressLint("NewApi")
public class CalendarUtils {
	/*
	 * <uses-permission android:name="android.permission.WRITE_CALENDAR" />
	 * <uses-permission android:name="android.permission.READ_CALENDAR" />
	 */
	private Context context;

	public CalendarUtils(Context context) {
		this.context = context;
	}

	public void create1() {
		// ACTION_INSERT does not work on all phones
		// use Intent.ACTION_EDIT in this case
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(CalendarContract.Events.CONTENT_URI);
		context.startActivity(intent);
	}

	public void create2() {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Learn Android");
		intent.putExtra(Events.EVENT_LOCATION, "Home suit home");
		intent.putExtra(Events.DESCRIPTION, "Download Examples");

		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar(2012, 10, 02);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis());

		// make it a full day event
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// make it a recurring Event
		intent.putExtra(Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

		context.startActivity(intent);
	}

	public void addCalendarEvent() {
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
		intent.putExtra("title", "Test Event");
		intent.putExtra("description", "This is a sample description");
		context.startActivity(intent);
	}

	public static long insertCalendar(Context context) {

		long calID = 0;
		long startMillis = 0;
		long endMillis = 0;
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2014, 11, 5, 7, 30);
		startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(2014, 11, 6, 8, 45);
		endMillis = endTime.getTimeInMillis();

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, "Jazzercise");
		values.put(Events.DESCRIPTION, "Group workout");
		values.put(Events.CALENDAR_ID, calID);
		values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// get the event ID that is the last element in the Uri
		long eventID = Long.parseLong(uri.getLastPathSegment());
		return eventID;
		//
		// ... do something with event ID
		//
		//

		// ContentResolver contentResolver = context.getContentResolver();
		// Uri url = Uri.parse("content://com.android.calendar/calendars");
		// url = CalendarContract.Calendars.CONTENT_URI;
		// ContentValues values = new ContentValues();
		// values.put(CalendarContract.Calendars.NAME, "Test");
		// values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Test");
		// values.put(CalendarContract.Calendars.VISIBLE, 1);
		// values.put(CalendarContract.Calendars.SYNC_EVENTS, false);
		// contentResolver.insert(url, values);
	}

	public static void readCalendar(Context context) {
		ContentResolver contentResolver = context.getContentResolver();

		Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"), (new String[] { "_id", "displayName", "selected" }), null, null, null);

		HashSet<String> calendarIds = new HashSet<String>();

		try {
			System.out.println("Count=" + cursor.getCount());
			if (cursor.getCount() > 0) {
				System.out.println("the control is just inside of the cursor.count loop");
				while (cursor.moveToNext()) {

					String _id = cursor.getString(0);
					String displayName = cursor.getString(1);
					Boolean selected = !cursor.getString(2).equals("0");

					System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
					calendarIds.add(_id);
				}
			}
		} catch (AssertionError ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// For each calendar, display all the events from the previous week to
		// the end of next week.
		for (String id : calendarIds) {
			Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
			// Uri.Builder builder =
			// Uri.parse("content://com.android.calendar/calendars").buildUpon();
			long now = new Date().getTime();

			ContentUris.appendId(builder, now - DateUtils.DAY_IN_MILLIS * 10000);
			ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000);

			Cursor eventCursor = contentResolver.query(builder.build(), new String[] { "title", "begin", "end", "allDay" }, "Calendars._id=" + 1, null, "startDay ASC, startMinute ASC");

			System.out.println("eventCursor count=" + eventCursor.getCount());
			if (eventCursor.getCount() > 0) {

				if (eventCursor.moveToFirst()) {
					do {
						Object mbeg_date, beg_date, beg_time, end_date, end_time;

						final String title = eventCursor.getString(0);
						final Date begin = new Date(eventCursor.getLong(1));
						final Date end = new Date(eventCursor.getLong(2));
						final Boolean allDay = !eventCursor.getString(3).equals("0");

						/*
						 * System.out.println("Title: " + title + " Begin: " +
						 * begin + " End: " + end + " All Day: " + allDay);
						 */
						System.out.println("Title:" + title);
						System.out.println("Begin:" + begin);
						System.out.println("End:" + end);
						System.out.println("All Day:" + allDay);

						/*
						 * the calendar control metting-begin events Respose
						 * sub-string (starts....hare)
						 */

						Pattern p = Pattern.compile(" ");
						String[] items = p.split(begin.toString());
						String scalendar_metting_beginday, scalendar_metting_beginmonth, scalendar_metting_beginyear, scalendar_metting_begindate, scalendar_metting_begintime, scalendar_metting_begingmt;

						scalendar_metting_beginday = items[0];
						scalendar_metting_beginmonth = items[1];
						scalendar_metting_begindate = items[2];
						scalendar_metting_begintime = items[3];
						scalendar_metting_begingmt = items[4];
						scalendar_metting_beginyear = items[5];

						String calendar_metting_beginday = scalendar_metting_beginday;
						String calendar_metting_beginmonth = scalendar_metting_beginmonth.toString().trim();

						int calendar_metting_begindate = Integer.parseInt(scalendar_metting_begindate.trim());

						String calendar_metting_begintime = scalendar_metting_begintime.toString().trim();
						String calendar_metting_begingmt = scalendar_metting_begingmt;
						int calendar_metting_beginyear = Integer.parseInt(scalendar_metting_beginyear.trim());

						System.out.println("calendar_metting_beginday=" + calendar_metting_beginday);

						System.out.println("calendar_metting_beginmonth =" + calendar_metting_beginmonth);

						System.out.println("calendar_metting_begindate =" + calendar_metting_begindate);

						System.out.println("calendar_metting_begintime=" + calendar_metting_begintime);

						System.out.println("calendar_metting_begingmt =" + calendar_metting_begingmt);

						System.out.println("calendar_metting_beginyear =" + calendar_metting_beginyear);

						/*
						 * the calendar control metting-begin events Respose
						 * sub-string (starts....ends)
						 */

						/*
						 * the calendar control metting-end events Respose
						 * sub-string (starts....hare)
						 */

						Pattern p1 = Pattern.compile(" ");
						String[] enditems = p.split(end.toString());
						String scalendar_metting_endday, scalendar_metting_endmonth, scalendar_metting_endyear, scalendar_metting_enddate, scalendar_metting_endtime, scalendar_metting_endgmt;

						scalendar_metting_endday = enditems[0];
						scalendar_metting_endmonth = enditems[1];
						scalendar_metting_enddate = enditems[2];
						scalendar_metting_endtime = enditems[3];
						scalendar_metting_endgmt = enditems[4];
						scalendar_metting_endyear = enditems[5];

						String calendar_metting_endday = scalendar_metting_endday;
						String calendar_metting_endmonth = scalendar_metting_endmonth.toString().trim();

						int calendar_metting_enddate = Integer.parseInt(scalendar_metting_enddate.trim());

						String calendar_metting_endtime = scalendar_metting_endtime.toString().trim();
						String calendar_metting_endgmt = scalendar_metting_endgmt;
						int calendar_metting_endyear = Integer.parseInt(scalendar_metting_endyear.trim());

						System.out.println("calendar_metting_beginday=" + calendar_metting_endday);

						System.out.println("calendar_metting_beginmonth =" + calendar_metting_endmonth);

						System.out.println("calendar_metting_begindate =" + calendar_metting_enddate);

						System.out.println("calendar_metting_begintime=" + calendar_metting_endtime);

						System.out.println("calendar_metting_begingmt =" + calendar_metting_endgmt);

						System.out.println("calendar_metting_beginyear =" + calendar_metting_endyear);

						/*
						 * the calendar control metting-end events Respose
						 * sub-string (starts....ends)
						 */

						System.out.println("only date begin of events=" + begin.getDate());
						System.out.println("only begin time of events=" + begin.getHours() + ":" + begin.getMinutes() + ":" + begin.getSeconds());

						System.out.println("only date begin of events=" + end.getDate());
						System.out.println("only begin time of events=" + end.getHours() + ":" + end.getMinutes() + ":" + end.getSeconds());

						beg_date = begin.getDate();
						mbeg_date = begin.getDate() + "/" + calendar_metting_beginmonth + "/" + calendar_metting_beginyear;
						beg_time = begin.getHours();

						System.out.println("the vaule of mbeg_date=" + mbeg_date.toString().trim());
						end_date = end.getDate();
						end_time = end.getHours();

						// CallHandlerUI.metting_begin_date.add(beg_date
						// .toString());
						// CallHandlerUI.metting_begin_mdate.add(mbeg_date
						// .toString());
						//
						// CallHandlerUI.metting_begin_mtime
						// .add(calendar_metting_begintime.toString());
						//
						// CallHandlerUI.metting_end_date.add(end_date.toString());
						// CallHandlerUI.metting_end_time.add(end_time.toString());
						// CallHandlerUI.metting_end_mtime
						// .add(calendar_metting_endtime.toString());

					} while (eventCursor.moveToNext());
				}
			}
			break;
		}
	}
}
