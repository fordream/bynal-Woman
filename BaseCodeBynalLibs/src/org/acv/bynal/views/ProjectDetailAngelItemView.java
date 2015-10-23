package org.acv.bynal.views;

import org.acv.bynal.dialog.ProjectDetailAngelDialog;
//import org.acv.bynal.fragment.ProjectDetailAngelFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;

public class ProjectDetailAngelItemView extends BaseView implements OnClickListener {
	Animation Ani;
	private RelativeLayout header;
	public ProjectDetailAngelItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_angel_item);
		
	}

	public ProjectDetailAngelItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_angel_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		Ani = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_push_left_in);
		Ani.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
	}

	@Override
	public void refresh() {
		super.refresh();
		String name_txt = ((BaseItem) getData()).getString("name");
		name_txt.replaceFirst("\\s+$", "");
		setText(name_txt, R.id.angel_name_txt);
		String img_avata = ((BaseItem) getData()).getString("avatar") + "&time=" + ProjectDetailAngelDialog.onloadImg;
		ImageLoaderUtils.getInstance(getContext()).DisplayImage( img_avata ,
				(ImageView) findViewById(R.id.angel_img),  BitmapFactory.decodeResource(getResources(), R.drawable.default_user_square2));
		header.startAnimation(Ani);
	}

	@Override
	public void onClick(View v) {
	}

//	private ProjectDetailAngelFragment projectDetailAngelFragment;
//
//	public void addProjectManagerFragment(ProjectDetailAngelFragment projectDetailAngelFragment) {
//		this.projectDetailAngelFragment = projectDetailAngelFragment;
//	}
}