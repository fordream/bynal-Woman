package com.acv.libs.nbase;

import org.acv.bynal.activity.SendContactActivity;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.fragment.SearchFragment;
import org.acv.bynal.fragment.ShareContactFragment;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.message.MessagePostFragment;

import a.com.acv.bynal.v3.V3HomeFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.LibsBaseFragment;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;

public abstract class BaseFragment extends LibsBaseFragment implements OnClickListener {
	public Handler mHandle = new Handler();

	public ACVbaseApplication getACVbaseApplication() {
		return (ACVbaseApplication) getActivity().getApplication();
	}

	private AnimationAction animationAction = new AnimationAction() {

		@Override
		public void onAnimationEnd() {
			_onAnimationEnd();
		}
	};

	public void _onAnimationEnd() {

	}

	@Override
	public void startActivity(Intent intent) {
		startActivityForResult(intent, ByUtils.REQUEST_HOME);
	}

	public AnimationAction getAnimationAction() {
		return animationAction;
	}

	@Override
	public void onClick(View v) {

	}

	public void showDialogMessage(String message) {
		CommonAndroid.showDialog(getActivity(), message, null);
	}

	private BaseFragment mparentFragment;

	public void setMParentFragment(BaseFragment parentFragment) {
		this.mparentFragment = parentFragment;
	}

	public BaseFragment getMParentFragment() {
		return mparentFragment;
	}

	public void setTextStrHtml(int messagepostFooterText, String string) {
		((TextView) getView().findViewById(messagepostFooterText)).setText(Html.fromHtml(string));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (layoytResurce() != 0) {
			View view = inflater.inflate(layoytResurce(), container, false);
			setUpFragment(view);
			return view;
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public abstract void setUpFragment(View view);

	public abstract int layoytResurce();

	private Object data;

	public void setData(Object object) {
		data = object;
	}

	public Object getData() {
		return data;
	}

	public void setText(String str, int res) {
		((TextView) getView().findViewById(res)).setText(str);
	}

	public HeaderOption getHeaderOption() {
		TYPEHEADER type = TYPEHEADER.NORMAL;
		// if (this instanceof LoginFragment) {
		// type = TYPEHEADER.CHECKBOX;
		// }

		HeaderOption headerOption = new HeaderOption(getActivity(), type) {
			@Override
			public void onClickButtonLeft() {

				CommonAndroid.hiddenKeyBoard(getActivity());
				if (getActivity() instanceof MainHomeActivity) {
					_onClickButtonLeft();
					return;
				}
				if (BaseFragment.this instanceof SearchFragment) {
					getActivity().finish();
				}
				// else if (BaseFragment.this instanceof LoginFragment) {
				// ((BaseActivity)
				// getActivity()).changeFragemt(getMParentFragment());
				// }
				else if (BaseFragment.this instanceof MessagePostFragment) {
					getActivity().finish();
				}
				if (BaseFragment.this instanceof ShareContactFragment) {
					getActivity().finish();
				} else {
					((BaseActivity) getActivity()).showMenuLeft(true);
				}
			}

			@Override
			public void onClickButtonRight() {
				CommonAndroid.hiddenKeyBoard(getActivity());

				if (getActivity() instanceof MainHomeActivity) {
					_onClickButtonRight();
					return;
				}
				if (BaseFragment.this instanceof SearchFragment) {
					((BaseActivity) getActivity()).showMenuLeft(true);
				}
				// else if (BaseFragment.this instanceof HomeFragment) {
				// BaseTabActivity.senBroadCast(getActivity(), 1000);
				// }
				else if (BaseFragment.this instanceof MessagePostFragment) {
					//
					((MessageActivity) getActivity()).gotoHome();
				} else if (BaseFragment.this instanceof ShareContactFragment) {
					((SendContactActivity) getActivity()).gotoHome();
				} else if (BaseFragment.this instanceof V3HomeFragment) {
					BaseTabActivity.senBroadCast(getActivity(), 1000);
				}
			}
		};

		headerOption.setResHeader(R.string.blank);
		headerOption.setResDrawableLeft(R.drawable.menu_xml);
		headerOption.setResDrawableRight(R.drawable.search_xml);
		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);

		if (BaseFragment.this instanceof SearchFragment) {
			headerOption.setResHeader(R.string.search);
			headerOption.setResDrawableLeft(R.drawable.back_xml);
			headerOption.setResDrawableRight(R.drawable.menu_xml);
		}

		// if (BaseFragment.this instanceof LoginFragment) {
		// headerOption.setShowButtonRight(false);
		// headerOption.setResDrawableLeft(R.drawable.back_xml);
		// }
		// if (BaseFragment.this instanceof HomeFragment) {
		// headerOption.setResHeader(R.string.home);
		// }

		if (BaseFragment.this instanceof MessagePostFragment) {
			headerOption.setTypeHeader(TYPEHEADER.NORMAL);
			headerOption.setResHeader(R.string.message);
			headerOption.setShowButtonRight(true);
			headerOption.setResDrawableLeft(R.drawable.back_xml);
			headerOption.setResDrawableRight(R.drawable.home_xml);
		}
		if (this instanceof ShareContactFragment) {
			headerOption.setResHeader(R.string.sharecontact);
			headerOption.setShowButtonRight(true);
			headerOption.setResDrawableLeft(R.drawable.back_xml);
			headerOption.setResDrawableRight(R.drawable.home_xml);
		}
		if (BaseFragment.this instanceof V3HomeFragment) {
			headerOption.setResHeader(R.string.home);
		}
		return headerOption;
	}

	public String getTextStr(int forgotpasswordEdit) {
		return ((TextView) getView().findViewById(forgotpasswordEdit)).getText().toString().trim();
	}

	public void setTextStr(int messagepostFooterText, String string) {
		((TextView) getView().findViewById(messagepostFooterText)).setText(string);
	}

	public void setChoosePosition(int index) {

	}

	/**
	 * Calendar
	 */

	public static final int CALENDAR = 10212;

	/**
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param title
	 * @param desctipmtion
	 */
	public final void addCalendar(long beginTime, long endTime, String title, String desctipmtion) {
		ByUtils.addCalendar(getActivity(), beginTime, endTime, title, desctipmtion);
	}

	public void relaseTooken() {
		if (getActivity().getParent() instanceof BaseTabActivity) {
			((BaseTabActivity) getActivity().getParent()).relaseTooken();
		} else if (getActivity() instanceof BaseActivity) {
			getActivity().setResult(ByUtils.RESPONSE_RELEASETOKEN);
			getActivity().finish();
		}
	}

	private void _onClickButtonLeft() {
		// if (this instanceof HomeFragment) {
		// BaseTabActivity.senBroadCastHeaderMenu(getActivity(), "menu");
		// } else

		{
			BaseTabActivity.senBroadCastHeaderMenu(getActivity(), "menu");
		}
	}

	private void _onClickButtonRight() {
		if (BaseFragment.this instanceof V3HomeFragment) {
			BaseTabActivity.senBroadCastHeaderMenu(getActivity(), "search");
		} else

		{
			BaseTabActivity.senBroadCastHeaderMenu(getActivity(), "home");
		}
	}

	public void gotoHome() {
		if (getActivity() instanceof Activity) {
			((Activity) getActivity()).setResult(ByUtils.REQUEST_HOME);
			((Activity) getActivity()).finish();
		} else if (getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).setResult(ByUtils.REQUEST_HOME);
			((BaseActivity) getActivity()).finish();
		}
	}
}