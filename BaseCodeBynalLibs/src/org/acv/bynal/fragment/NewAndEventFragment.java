package org.acv.bynal.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.acv.bynal.activity.CalendarActivity;
import org.acv.bynal.views.HeaderView;
import org.acv.bynal.views.LoadingView;
import org.json.JSONArray;
import org.json.JSONObject;

import a.com.acv.bynal.v3.database.DBOperations;
import a.com.acv.bynal.v3.database.NewAndEventTable;
import a.com.acv.bynal.v3.database.Table;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.nbase.BaseFragment;

public class NewAndEventFragment extends BaseFragment implements ICallbackAPI {
	private ListView newandeventListView;
	private LoadingView newandevent_loadingview;

	public NewAndEventFragment() {
		super();
	}

	private void postCallendar(Table table) {

		String time = table.getData("time");
		// 2013-11-09
		StringTokenizer stringTokenizer = new StringTokenizer(time, "-");
		int year = Integer.parseInt(stringTokenizer.nextToken());
		int month = Integer.parseInt(stringTokenizer.nextToken());
		int day = Integer.parseInt(stringTokenizer.nextToken());
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, 0, 0, 0);
		// cal.getTimeInMillis();
		long getTime = cal.getTimeInMillis();
		long beginTime = getTime;
		long endTime = getTime;

		if (getTime > System.currentTimeMillis()) {
			addCalendar(beginTime, endTime, table.getData("title"), table.getData("message"));
		} else {
			CommonAndroid.showDialog(getActivity(), getString(R.string.error_event_calendar), null);
		}
	}

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		newandevent_loadingview.endProgressBar("");

		List<Table> list = new DBOperations(getActivity()).query(new NewAndEventTable(), null);

		adapter.clear();
		adapter.addAllItems(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setUpFragment(View view) {
		newandevent_loadingview = (LoadingView) view.findViewById(R.id.newandevent_loadingview);
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		newandeventListView = (ListView) view.findViewById(R.id.newandevent_listview);
		adapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale);
				view.startAnimation(animation);
				return view;
			}

			@Override
			public BaseView getView(Context context, Object data) {
				return new ItemEventsView(context);
			}
		};
		newandeventListView.setAdapter(adapter);
	}

	private BaseAdapter adapter;

	@Override
	public int layoytResurce() {
		return R.layout.newandevent;
	}

	@Override
	public void onError(String message) {

	}

	@Override
	public void onSuccess(String response) {

	}

	private class ItemEventsView extends BaseView implements OnClickListener {

		public ItemEventsView(Context context) {
			super(context);
			init(R.layout.eventitem);
			findViewById(R.id.newevent_event).setOnClickListener(this);
		}

		@Override
		public void refresh() {
			super.refresh();

			if (getData() instanceof Table) {
				Table table = (Table) getData();
				setTextStr(R.id.newevent_title, table.getData("title"));
				setTextStr(R.id.newevent_date, table.getData("time"));
				setTextStrHtml(R.id.newevent_content, table.getData("message"));

				String text = table.getData("message");

				TextView textView = (TextView) findViewById(R.id.newevent_content_txt);
				textView.setText(Html.fromHtml(text));
				textView.setMovementMethod(LinkMovementMethod.getInstance());

				if (table.getData("new").equalsIgnoreCase("true")) {
					findViewById(R.id.newevent_news).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.newevent_news).setVisibility(View.GONE);
				}

			} else {
			}
		}

		@Override
		public void onClick(View v) {
			postCallendar((Table) getData());
		}

	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);
		headerOption.setResHeader(R.string.menu_home_news_event);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		return headerOption;
	}
}
