package a.com.acv.bynal.v3.fragment;

import org.acv.bynal.views.HeaderView;

import a.com.acv.bynal.v3.NoScollViewPager;
import a.com.acv.bynal.v3.views.V3LoginView;
import a.com.acv.bynal.v3.views.V3RegisterView;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseFragment;

public class V3LoginGFragment extends BaseFragment {
	private FrameLayout frame_noscrollview;
	private RadioButton radioButtonLogin;
	private RadioButton radioButtonRegister;
	private HeaderView headerView;

	private V3LoginView v3LoginView;
	private V3RegisterView v3RegisterView;

	public V3LoginGFragment() {
		super();
	}

	@Override
	public void setUpFragment(View view) {
		headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setheaderOption(getHeaderOption());
		frame_noscrollview = (FrameLayout) view.findViewById(R.id.frame_noscrollview);
		radioButtonLogin = (RadioButton) view.findViewById(R.id.radio0);
		radioButtonRegister = (RadioButton) view.findViewById(R.id.radio1);
		radioButtonLogin.setOnCheckedChangeListener(n);
		radioButtonRegister.setOnCheckedChangeListener(n);
		radioButtonLogin.setChecked(true);

		v3LoginView = (V3LoginView) view.findViewById(R.id.v3login);
		v3RegisterView = (V3RegisterView) view.findViewById(R.id.v3register);
	}

	@Override
	public void onResume() {
		super.onResume();
		v3RegisterView.refresh();
		v3LoginView.refresh();

		SharedPreferences preferences = getActivity().getSharedPreferences(ByUtils.SHAREREFERRENT_SETTING, 0);
		if (preferences.getBoolean("LOGINTAB", false)) {
			radioButtonLogin.setChecked(true);
		} else {
			radioButtonRegister.setChecked(true);
		}
	}

	private OnCheckedChangeListener n = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			CommonAndroid.hiddenKeyBoard(getActivity());
			if (radioButtonLogin.isChecked()) {
				frame_noscrollview.getChildAt(0).setVisibility(View.VISIBLE);
				frame_noscrollview.getChildAt(1).setVisibility(View.GONE);
			} else {
				frame_noscrollview.getChildAt(0).setVisibility(View.GONE);
				frame_noscrollview.getChildAt(1).setVisibility(View.VISIBLE);
			}

			headerView.setheaderOption(getHeaderOption());
		}
	};

	@Override
	public int layoytResurce() {
		return R.layout.v3_login;
	}

	@Override
	public HeaderOption getHeaderOption() {
		HeaderOption headerOption = super.getHeaderOption();
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setTypeHeader(TYPEHEADER.NORMAL);

		headerOption.setResHeader(R.string.menu_home_Login);

		if (radioButtonLogin == null) {
		} else if (radioButtonLogin.isChecked()) {

		} else {
			headerOption.setResHeader(R.string.register);
		}

		return headerOption;
	}

}