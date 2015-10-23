package com.example.ttest.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bynal.libs.R;

// com.example.ttest.views.AnimationView
public class AnimationView extends LinearLayout {
	private ImageView left, right, img_center;
	private Bitmap bitmap;

	public AnimationView(Context context) {
		super(context);

		init();
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.animation_view, this);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		img_center = (ImageView) findViewById(R.id.img_center);
	}

	public void setBimatMap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void startMAnimation(AnimationListener listener) {
		if (bitmap != null) {

			left.setImageBitmap(bitmap);
			right.setImageBitmap(bitmap);
			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.center_to_left);
			animation.setAnimationListener(listener);
			left.startAnimation(animation);
			right.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.center_to_right));

		}
	}
}