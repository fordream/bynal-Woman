package org.acv.bynal.fragment;

import android.text.Html.ImageGetter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.nbase.BaseFragment;

public class SplashFragment extends BaseFragment implements AnimationListener {
	private ImageView spalsh, spalsh_logo;
	View logo_text_splash;

	public SplashFragment() {
		super();
	}

	@Override
	public void setUpFragment(View view) {
		spalsh = (ImageView) view.findViewById(R.id.spalsh);
		spalsh_logo = (ImageView) view.findViewById(R.id.spalsh_logo);
		logo_text_splash =  view.findViewById(R.id.logo_text_splash);

//		ImageLoaderUtils loaderUtils = ImageLoaderUtils.getInstance(getActivity());
//		loaderUtils.DisplayImage("file:///android_asset/img/intro/bg_intro.png", spalsh, null);
//		loaderUtils.DisplayImage("file:///android_asset/img/intro/logo.png", spalsh_logo, null);
		
		
		/**
		 * animation for splash
		 */
		spalsh.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.splash));

		/**
		 * splash for logo
		 */
		spalsh_logo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.room2));

		/**
		 * animation for splash text
		 */
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_text);
		animation.setAnimationListener(this);
		logo_text_splash.startAnimation(animation);
	}

	@Override
	public int layoytResurce() {
		return R.layout.splash;
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mHandle.sendEmptyMessage(0);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}