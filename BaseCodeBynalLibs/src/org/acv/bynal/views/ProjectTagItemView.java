package org.acv.bynal.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;

public class ProjectTagItemView extends BaseView implements OnClickListener {
	Animation Ani;
	private RelativeLayout header;
	public ProjectTagItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_tag_item);
		
	}

	public ProjectTagItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_tag_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
//			findViewById(R.id.tag_delete).setOnClickListener(this);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		Ani = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_zoom_in);
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
		setText(((BaseItem) getData()).getString("value"),
				R.id.tag_value);
		header.startAnimation(Ani);
	}

	@Override
	public void onClick(View v) {
		 if (projectDetailTagFragment != null) {
//			 projectDetailTagFragment.actionDeleteTag( v , (BaseItem)getData());
		 }
	}

	private Context projectDetailTagFragment;

//	public void addProjectManagerFragment(Class<ProjectDetailTagDialog> class1) {
//		this.projectDetailTagFragment = class1;
//	}

	public void addProjectManagerFragment(Context class1) {
		// TODO Auto-generated method stub
		this.projectDetailTagFragment = class1;
	}

}