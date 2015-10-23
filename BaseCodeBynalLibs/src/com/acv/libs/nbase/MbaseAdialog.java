package com.acv.libs.nbase;

import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import app.bynal.woman.R;

import com.acv.libs.base.LibsBaseAdialog;
import com.acv.libs.base.util.ByUtils;

public abstract class MbaseAdialog extends LibsBaseAdialog {
	public int getAnimationOpen() {
		return R.anim.bot_to_top;
	}

	public int getAnimationClose() {
		return R.anim.top_to_bot;
	}

	public void onOpenDialogAnimationSuccess() {

	}

	public MbaseAdialog(Context context) {
		super(context);
	}

	public void openPopActivity(View mMain) {
		if (mMain == null)
			return;
		final AnimationAction animationAction = new AnimationAction() {

			@Override
			public void onAnimationEnd() {
				onOpenDialogAnimationSuccess();
			}
		};
		Animation animation = AnimationUtils.loadAnimation(getContext(), getAnimationOpen());
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animationAction != null) {
					animationAction.onAnimationEnd();
				}
			}
		});
		mMain.startAnimation(animation);
	}

	boolean isRunAiniation = false;

	public void closePopActivity(Context context, View mMain, final AnimationAction animationAction) {
		if (!isRunAiniation) {
			isRunAiniation = true;
		} else {
			return;
		}
		Animation animation = AnimationUtils.loadAnimation(context, getAnimationClose());
		animation.setFillAfter(false);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animationAction != null)
					animationAction.onAnimationEnd();
			}
		});
		mMain.startAnimation(animation);
	}

	public MbaseAdialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
	}

	public void gotoHome() {
		if (getmContext() instanceof Activity) {
			((Activity) getmContext()).setResult(ByUtils.REQUEST_HOME);
			((Activity) getmContext()).finish();
		} else if (getmContext() instanceof BaseActivity) {
			((BaseActivity) getmContext()).setResult(ByUtils.REQUEST_HOME);
			((BaseActivity) getmContext()).finish();
		}
	}
}