package com.acv.libs.base;

import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acv.libs.base.HeaderOption.TYPEHEADER;

public abstract class LibsBaseFragment extends Fragment implements OnClickListener {
	public Handler mHandle = new Handler();

	private AnimationAction animationAction = new AnimationAction() {

		@Override
		public void onAnimationEnd() {
			_onAnimationEnd();
		}
	};

	public void _onAnimationEnd() {

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

	private LibsBaseFragment mparentFragment;

	public void setMParentFragment(LibsBaseFragment parentFragment) {
		this.mparentFragment = parentFragment;
	}

	public LibsBaseFragment getMParentFragment() {
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
		return new HeaderOption(getActivity(), TYPEHEADER.NORMAL);
	}

	public String getTextStr(int forgotpasswordEdit) {
		return ((TextView) getView().findViewById(forgotpasswordEdit)).getText().toString().trim();
	}

	public void setTextStr(int messagepostFooterText, String string) {
		((TextView) getView().findViewById(messagepostFooterText)).setText(string);
	}

}