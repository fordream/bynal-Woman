package org.acv.bynal.dialog;

import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import app.bynal.woman.R;

import com.acv.libs.nbase.MbaseAdialog;

public class ProfileCameraMenuDialogDialog extends MbaseAdialog implements android.view.View.OnClickListener {

	public ProfileCameraMenuDialogDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
	}

	@Override
	public void onBackPressed() {
		close(-1);
		// super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		openPopActivity(findViewById(R.id.profile_camera_menu_dialog_main));
		findViewById(R.id.profile_camera_menu_dialog_main).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(-1);
			}
		});

		findViewById(R.id.profile_camera_menu_dialog_camera001).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(0);
			}
		});

		findViewById(R.id.profile_camera_menu_dialog_camera002).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(1);
			}
		});
	}

	private void close(final int index) {
		closePopActivity(getContext(), findViewById(R.id.profile_camera_menu_dialog_main), new AnimationAction() {
			@Override
			public void onAnimationEnd() {
				dismiss();
				clickListener.onClick(null, index);
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public int getLayout() {
		return R.layout.profile_camera_menu_dialog;
	}
}