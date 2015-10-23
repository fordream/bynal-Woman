package a.com.acv.bynal.v3.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.example.ttest.views.AnimationView;

public class AnimationShowDialog extends Dialog {

	public DialogInterface.OnClickListener clickListener;
	private AnimationView animationView;

	private Context mContext;

	public Context getmContext() {
		return mContext;
	}

	private Bitmap mBitmap;

	public AnimationShowDialog(Context context, Bitmap bimap) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;

		mBitmap = bimap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		setContentView(animationView = new AnimationView(getContext()));
		animationView.setBimatMap(mBitmap);
		animationView.startMAnimation(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
			}
		});
	}

}