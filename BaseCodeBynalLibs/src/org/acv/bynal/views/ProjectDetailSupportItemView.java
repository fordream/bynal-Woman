package org.acv.bynal.views;

//import org.acv.bynal.fragment.ProjectDetailSupportFragment;

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

public class ProjectDetailSupportItemView extends BaseView implements OnClickListener {
	Animation fadeOut, fadeIn;
	private RelativeLayout header;
	public ProjectDetailSupportItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_support_item);
		
	}

	public ProjectDetailSupportItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_support_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
//			findViewById(R.id.support_item_btn).setOnClickListener(this);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_zoom_in);/*v3_slide_down_search*/
		fadeOut.setAnimationListener(new Animation.AnimationListener() {
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
		setText(((BaseItem) getData()).getString("value") + getResources().getString(R.string.project_detail_support_btsupport) ,
				R.id.support_value);
		setText(((BaseItem) getData()).getString("desc"),
				R.id.support_desc);
		setText( getResources().getString(R.string.project_detail_support_backer) + ((BaseItem) getData()).getString("backer"),
				R.id.support_backer);
		if(!(((BaseItem) getData()).getString("support_num_limit")).equalsIgnoreCase("0") ){
			setText(getResources().getString(R.string.project_detail_support_limit1) + ((BaseItem) getData()).getString("support_num_limit") + getResources().getString(R.string.project_detail_support_limit2),
					R.id.support_support_num_limit);
		}else{
			setText("",R.id.support_support_num_limit);
		}
		header.startAnimation(fadeOut);
	}

	@Override
	public void onClick(View v) {
//		 if (projectDetailSupportFragment != null) {
//			 projectDetailSupportFragment.viewSupportDetail( v , (BaseItem)getData());
//		 }
	}

	/*private ProjectDetailSupportFragment projectDetailSupportFragment;

	public void addProjectManagerFragment(ProjectDetailSupportFragment projectDetailSupportFragment) {
		this.projectDetailSupportFragment = projectDetailSupportFragment;
	}*/
}