package a.com.acv.bynal.v3.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;

/**
 * a.com.acv.bynal.v3.views.MainMenuView
 * 
 * @author teemo
 * 
 */
public class MainListMenuView extends BaseView implements android.view.View.OnClickListener, OnItemClickListener {
	private AccountDB accountDB;
	private Handler handler = new Handler();
	private MainListMenuViewListener mainListMenuViewListener;

	public void setMainListMenuViewListener(MainListMenuViewListener mainListMenuViewListener) {
		this.mainListMenuViewListener = mainListMenuViewListener;
	}

	public interface MainListMenuViewListener {
		public void mOnClick(int[] a);

		public void mOnItemClick(int[] a);
	}

	public MainListMenuView(Context context) {
		super(context);
		accountDB = new AccountDB(getContext());
		init();
	}

	private BaseAdapter adapter;

	public MainListMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		accountDB = new AccountDB(getContext());
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.v3_menu_list_left, this);

		findViewById(R.id.menuslide_header).setOnClickListener(this);
		findViewById(R.id.mMain).setOnClickListener(this);

		List<Object> list = new ArrayList<Object>();

		list.add(new int[] { R.drawable.menu_home_m1_xml, R.string.menu_home_Login });
		list.add(new int[] { R.drawable.menu_home_m2_xml, R.string.menu_home_project_manager });
		list.add(new int[] { R.drawable.menu_home_m22_xml, R.string.menu_home_blog });
		list.add(new int[] { R.drawable.menu_home_m3_xml, R.string.menu_home_message });
		list.add(new int[] { R.drawable.menu_home_m4_xml, R.string.menu_home_news_event });
		list.add(new int[] { R.drawable.menu_home_m5_xml, R.string.menu_home_more });

		if (!ByUtils.isBlank(accountDB.getToken())) {
			list.add(new int[] { R.drawable.menu_home_m7_xml, R.string.menu_home_logout });
		}

		adapter = new BaseAdapter(getContext(), list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				BaseView view = (BaseView) super.getView(position, convertView, parent);
				view.findViewById(R.id.menu_header_img).setBackgroundResource(R.drawable.menu_m1);
				if (((int[]) getItem(position))[0] == R.drawable.menu_home_m5_xml) {
					List<Object> list = getlData();

					int check[] = { R.drawable.menu_child_m1_xml, R.string.menu_home_contact_us };
					boolean isAdded = false;

					for (Object object : list) {
						int data[] = (int[]) object;
						if (data[0] == check[0]) {
							isAdded = true;
							break;
						}
					}
					
					if(isAdded){
						view.findViewById(R.id.menu_header_img).setBackgroundResource(R.drawable.menu_m2);
					}else{
						
					}

				}
				return view;
			}

			@Override
			public BaseView getView(Context context, Object data) {
				return new MainMenuItemView(context);
			}
		};

		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	private class MainMenuItemView extends BaseView {

		public MainMenuItemView(Context context) {
			super(context);
			init(R.layout.v3_menu_item);
		}

		@Override
		public void refresh() {
			super.refresh();
			try {
				final int[] daa = (int[]) getData();
				View view = findViewById(R.id.menu_img_header);
				TextView textView = (TextView) findViewById(R.id.menutextview_header);
				view.setBackgroundResource(daa[0]);
				textView.setText(daa[1]);
				if (daa[0] == R.drawable.menu_home_m1_xml) {
					if (!ByUtils.isBlank(accountDB.getToken())) {
						textView.setText(accountDB.getName());
					}
				}

			} catch (Exception exception) {

			}

			try {
				if (getData() != null) {
					int[] daa = (int[]) getData();
					View view = findViewById(R.id.ImageView04);
					TextView textView = (TextView) findViewById(R.id.BaseTextView04);
					view.setBackgroundResource(daa[0]);
					textView.setText(daa[1]);
				}
			} catch (Exception exception) {
			}

			int[] childs = new int[] { R.drawable.menu_child_m1_xml, R.drawable.menu_child_m2_xml, R.drawable.menu_child_m3_xml, R.drawable.menu_child_m4_xml, R.drawable.menu_child_m5_xml,
					R.drawable.menu_child_m6_xml };

			boolean isChild = false;
			if (getData() != null) {
				int[] daa = (int[]) getData();
				for (int i : childs) {
					if (i == daa[0]) {
						isChild = true;
					}
				}
			}

			findViewById(R.id.menu_sup_1).setVisibility(isChild ? VISIBLE : GONE);
			findViewById(R.id.menu_header).setVisibility(isChild ? GONE : VISIBLE);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int[] data = (int[]) parent.getItemAtPosition(position);
		if (data[0] == R.drawable.menu_home_m5_xml) {
			onShowMenuChild();
		} else {
			// close and to menu
			mainListMenuViewListener.mOnClick(data);
		}
	}

	private void onShowMenuChild() {
		int CHILD[][] = new int[][] { //
		{ R.drawable.menu_child_m1_xml, R.string.menu_home_contact_us }, //
				{ R.drawable.menu_child_m2_xml, R.string.menu_home_about },//
				{ R.drawable.menu_child_m3_xml, R.string.menu_home_help }, //
				{ R.drawable.menu_child_m4_xml, R.string.menu_home_x1 }, //
				{ R.drawable.menu_child_m5_xml, R.string.menu_home_x2 },//
				{ R.drawable.menu_child_m6_xml, R.string.menu_home_x3 },//
		};

		List<Object> list = adapter.getlData();

		int check[] = CHILD[0];
		boolean isAdded = false;

		for (Object object : list) {
			int data[] = (int[]) object;
			if (data[0] == check[0]) {
				isAdded = true;
				break;
			}
		}

		if (isAdded) {
			remove(CHILD, CHILD.length - 1);
		} else {
			add(CHILD, 0);
		}
	}

	private void add(final int[][] cHILD, final int i) {

		if (cHILD.length > i) {

			List<Object> list = adapter.getlData();

			Object object = null;
			if (!ByUtils.isBlank(accountDB.getToken())) {
				object = list.get(list.size() - 1);
				list.remove(list.size() - 1);
			}
			list.add(cHILD[i]);

			if (object != null)
				list.add(object);

			adapter.notifyDataSetChanged();

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					add(cHILD, i + 1);
				}
			}, 10);
		}
	}

	private void remove(final int[][] cHILD, final int i) {
		if (i >= 0) {
			int[] check = cHILD[i];
			List<Object> list = adapter.getlData();

			for (Object object : list) {
				if (check[0] == ((int[]) object)[0]) {
					list.remove(object);
					break;
				}
			}

			adapter.notifyDataSetChanged();

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					remove(cHILD, i - 1);
				}
			}, 10);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.menuslide_header) {
			// goto home
			if (mainListMenuViewListener != null) {
				mainListMenuViewListener.mOnClick(new int[] { -1, -1 });
			}
		} else if (v.getId() == R.id.mMain) {
			// close
			mainListMenuViewListener.mOnClick(new int[] { 0, 0 });
		}
	}
}