package org.acv.bynal.views;

//import org.acv.bynal.fragment.ProjectDetailReportFragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;

@SuppressLint("NewApi")
public class ProjectDetailReportItemView extends BaseView implements OnClickListener {
	Animation fadeOut, fadeIn;
	Animator anim;
	private RelativeLayout header;
	public ProjectDetailReportItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_report_item);
		
	}

	public ProjectDetailReportItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_report_item);
	}

	@SuppressLint("NewApi")
	@Override
	public void init(int res) {
		super.init(res);
			findViewById(R.id.report_commentcount_txt).setOnClickListener(this);
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
			
			anim = AnimatorInflater
				    .loadAnimator(this.getContext(), R.anim.v3_flip_on_vertical);
	}

	@SuppressLint("NewApi")
	@Override
	public void refresh() {
		super.refresh();
		setText(((BaseItem) getData()).getString("report_title"),
				R.id.report_title_txt);
		String body_txt = ((BaseItem) getData()).getString("report_body");
		body_txt = body_txt.replaceAll("&nbsp;", "");
		body_txt = body_txt.replaceAll("\r\n", "\n");
		for(int i= 0;i < 5;i++){
			body_txt = body_txt.replaceAll("\n\n", "\n");
		}
		setText(body_txt,
				R.id.report_desc_txt);
		setText(getResources().getString(R.string.report_postdate) + ((BaseItem) getData()).getString("post_date"),
				R.id.report_datepost_txt);
		setText(((BaseItem) getData()).getString("created"),
				R.id.report_datecreate_txt);
		String comment_count_temp = getResources().getString(R.string.report_commentcount) ;//+ "(" +((BaseItem) getData()).getString("comment_count") + ")";
//		setText(comment_count_temp,
//				R.id.report_commentcount_txt);
		TextView report_commentcount_txt =(TextView)findViewById(R.id.report_commentcount_txt);
		SpannableString spanString = new SpannableString(comment_count_temp);
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		report_commentcount_txt.setText(spanString);
		anim.setTarget(header);
		anim.start();
	}

	@Override
	public void onClick(View v) {
		 /*if (projectDetailReportFragment != null) {
//			 projectDetailReportFragment.actionDeleteTag( v , (BaseItem)getData());
		 }*/
	}

	/*private ProjectDetailReportFragment projectDetailReportFragment;

	public void addProjectManagerFragment(ProjectDetailReportFragment projectDetailReportFragment) {
		this.projectDetailReportFragment = projectDetailReportFragment;
	}*/
}