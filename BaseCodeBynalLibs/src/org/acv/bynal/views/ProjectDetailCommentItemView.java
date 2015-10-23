package org.acv.bynal.views;

import org.acv.bynal.dialog.ProjectDetailCommentDialog;
//import org.acv.bynal.fragment.ProjectDetailCommentFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;
public class ProjectDetailCommentItemView extends BaseView implements OnClickListener {
	Animation fadeOut, fadeIn;
	private RelativeLayout header;
	public ProjectDetailCommentItemView(Context context) {
		super(context);
		init(R.layout.projectdetail_comment_item);
		
	}

	public ProjectDetailCommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.projectdetail_comment_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_rotate);/*v3_slide_down_search*/
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
		try {
			String s_name = "<b>"+((BaseItem) getData()).getString("name")+"</b> " + getResources().getString(R.string.project_detail_comment_san) ;
			TextView tv_name = (TextView)findViewById(R.id.comment_name);
			tv_name.setText(Html.fromHtml(s_name));
//			setText(((BaseItem) getData()).getString("name"),
//					R.id.comment_name);
			String created_temp = ((BaseItem) getData()).getString("created");
			String created = created_temp.substring(0, 10);//2014-06-20
			setText(created,
					R.id.comment_created);
			setText(((BaseItem) getData()).getString("comment"),
					R.id.comment_commenttxt);
			String img_avata = ((BaseItem) getData()).getString("avatar") + "&time=" + ProjectDetailCommentDialog.onloadImg;
			ImageLoaderUtils.getInstance(getContext()).DisplayImage( img_avata,
						(ImageView) findViewById(R.id.comment_avatar), BitmapFactory.decodeResource(getResources(), R.drawable.default_user_square2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		header.startAnimation(fadeOut);
	}

	@Override
	public void onClick(View v) {
	}

	/*private ProjectDetailCommentFragment projectDetailCommentFragment;

	public void addProjectManagerFragment(ProjectDetailCommentFragment projectDetailCommentFragment) {
		this.projectDetailCommentFragment = projectDetailCommentFragment;
	}*/
}