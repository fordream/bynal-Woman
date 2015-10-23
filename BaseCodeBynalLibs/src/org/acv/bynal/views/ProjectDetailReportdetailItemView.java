package org.acv.bynal.views;

//import org.acv.bynal.fragment.ProjectDetailReportdetailFragment;

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

public class ProjectDetailReportdetailItemView extends BaseView implements OnClickListener {
	Animation fadeOut, fadeOut2;
	private RelativeLayout header;
	public ProjectDetailReportdetailItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_reportdetail_item);
		
	}

	public ProjectDetailReportdetailItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_reportdetail_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
//			findViewById(R.id.report_commentcount_txt).setOnClickListener(this);
		header = (RelativeLayout) findViewById(R.id.list_comment_main);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_left);
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
		fadeOut2 = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_right);
		fadeOut2.setAnimationListener(new Animation.AnimationListener() {
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
		if((BaseItem) getData() != null){
			findViewById(R.id.list_comment_main).setVisibility(View.VISIBLE);
			setText(((BaseItem) getData()).getString("key_value") , R.id.report_no_txt);
			setText(((BaseItem) getData()).getString("comment_body"),
					R.id.report_comment_body_txt);
			setText(((BaseItem) getData()).getString("user_name"),
					R.id.report_user_name_txt);
			setText(((BaseItem) getData()).getString("created"),
					R.id.reportdetail_created_txt);
		}else{
			findViewById(R.id.list_comment_main).setVisibility(View.INVISIBLE);
		}
		try {
			int post = Integer.parseInt(((BaseItem) getData()).getString("key_value2"));
			if(post % 2 == 0){
				header.startAnimation(fadeOut);
			}else{
				header.startAnimation(fadeOut2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		/* if (projectDetailReportdetailFragment != null) {
//			 projectDetailReportdetailFragment.actionDeleteTag( v , (BaseItem)getData());
		 }*/
	}

	/*private ProjectDetailReportdetailFragment projectDetailReportdetailFragment;

	public void addProjectDetailFragment(ProjectDetailReportdetailFragment projectDetailReportdetailFragment) {
		this.projectDetailReportdetailFragment = projectDetailReportdetailFragment;
	}*/
}