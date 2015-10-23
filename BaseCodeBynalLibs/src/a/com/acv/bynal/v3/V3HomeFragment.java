package a.com.acv.bynal.v3;

import java.util.ArrayList;
import java.util.List;

import org.acv.bynal.views.HeaderView;

import a.com.acv.bynal.v3.database.DBOperations;
import a.com.acv.bynal.v3.database.ProjectTable;
import a.com.acv.bynal.v3.service.V3BynalService;
import a.com.acv.bynal.v3.service.V3BynalService.V3BynalServiceACTION;
import a.com.acv.bynal.v3.views.V3HomeSession1View;
import a.com.acv.bynal.v3.views.V3homesesion2itemView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.nbase.BaseFragment;

public class V3HomeFragment extends BaseFragment {
	private V3HomeSession1View homeSession1View;
	private View v3home_pager_have_left, v3home_pager_have_right;
	private View mprogressBar_home;

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
	}

	private View sessionTwo;
	private ListView v3home_step2_listview;

	@Override
	public void setUpFragment(final View view) {
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		if (headerView != null) {
			headerView.setheaderOption(getHeaderOption());
		}

		mprogressBar_home = view.findViewById(R.id.mprogressBar_home);
		
		v3home_pager_have_left = view.findViewById(R.id.v3home_pager_have_left);
		v3home_pager_have_right = view.findViewById(R.id.v3home_pager_have_right);
		v3home_pager_have_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				homeSession1View.setCurrentItem(homeSession1View.getCurrentItem() - 1, true);
			}
		});

		v3home_pager_have_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				homeSession1View.setCurrentItem(homeSession1View.getCurrentItem() + 1, true);
			}
		});
		homeSession1View = (V3HomeSession1View) view.findViewById(R.id.v3home_pager);
		homeSession1View.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				showNextPre();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		sessionTwo = view.findViewById(R.id.v3home_step2_main);
		v3home_step2_listview = (ListView) view.findViewById(R.id.v3home_step2_listview);

		view.findViewById(R.id.v3home_step2_open).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSessionTwo(true);
			}
		});

		view.findViewById(R.id.v3home_step2_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSessionTwo(true);
			}
		});

		mHandle.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					updateView("home_project_new");
					// updateView("home_project_hot");

					Intent intent = new Intent(getActivity(), V3BynalService.class);
					intent.putExtra("V3BynalServiceACTION", V3BynalServiceACTION.HOME_NEW_PROJECT);
					getActivity().startService(intent);

					intent = new Intent(getActivity(), V3BynalService.class);
					intent.putExtra("V3BynalServiceACTION", V3BynalServiceACTION.HOME_HOT_PROJECT);
					getActivity().startService(intent);
				} catch (Exception exception) {
				}
			}
		}, 800);
	}

	protected void showNextPre() {

		int count = homeSession1View.getAdapter().getCount();
		int current = homeSession1View.getCurrentItem();

		if (current < 0) {
			v3home_pager_have_left.clearAnimation();
			v3home_pager_have_right.clearAnimation();

			v3home_pager_have_left.setVisibility(View.GONE);
			v3home_pager_have_right.setVisibility(View.GONE);
		} else {
			if (current == 0) {
				v3home_pager_have_left.clearAnimation();
				v3home_pager_have_left.setVisibility(View.GONE);
			} else {
				// v3home_pager_have_left.startAnimation(animation);
				v3home_pager_have_left.setVisibility(View.VISIBLE);
			}

			if (current < count - 1) {
				// v3home_pager_have_right.startAnimation(animation);
				v3home_pager_have_right.setVisibility(View.VISIBLE);
			} else {
				v3home_pager_have_right.clearAnimation();
				v3home_pager_have_right.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DBOperations.BDUPDATE));
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	private void updateView(String project_type) {

		List<Object> list = new ArrayList<Object>();
		list.addAll(new DBOperations(getActivity()).getListProjectList(project_type));
		if ("home_project_hot".equals(project_type)) {
			homeSession1View.setDataList(list);
			showNextPre();
			if (list.size() > 0) {
				homeSession1View.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));
				mprogressBar_home.setVisibility(View.GONE);
			}
		} else if ("home_project_new".equals(project_type)) {
			v3home_step2_listview.setAdapter(new com.acv.libs.base.BaseAdapter(getActivity(), list) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale);
					// if (position % 2 == 0) {
					// animation =
					// AnimationUtils.loadAnimation(parent.getContext(),
					// R.anim.lefttoright_in);
					// } else {
					// animation =
					// AnimationUtils.loadAnimation(parent.getContext(),
					// R.anim.righttoleft_in);
					// }

					view.startAnimation(animation);
					return view;
				}

				@Override
				public BaseView getView(Context context, Object data) {
					return new V3homesesion2itemView(context);
				}
			});
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String table = intent.getStringExtra("table");
			if (new ProjectTable().getTableName().equals(table)) {
				String project_type = intent.getStringExtra("project_type");
				updateView(project_type);
			}
		}
	};

	private void showSessionTwo(boolean show) {
		if (sessionTwo.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bot);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					sessionTwo.setVisibility(View.GONE);

				}
			});

			animation.setFillAfter(false);
			sessionTwo.startAnimation(animation);
		} else {

			v3home_step2_listview.setAdapter(null);
			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bot_to_top);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					updateView("home_project_new");
				}
			});

			animation.setFillAfter(true);
			sessionTwo.setVisibility(View.VISIBLE);
			sessionTwo.startAnimation(animation);
		}
	}

	@Override
	public int layoytResurce() {
		return R.layout.v3home;
	}

	@Override
	public HeaderOption getHeaderOption() {
		return super.getHeaderOption();
	}
}