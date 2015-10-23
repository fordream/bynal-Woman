package org.acv.bynal.views;

import org.acv.bynal.activity.ProjectmanagerActivity;
import org.acv.bynal.fragment.ProjectManagerFragment;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
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
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.ImageLoaderUtils;

public class ProjectListItemView extends BaseView implements OnClickListener {
	Context context;
	Animation fadeOut, fadeOut2, fadeIn;
	private RelativeLayout header;
	public ProjectListItemView(Context context) {
		super(context);
		context = context;
		if((ProjectManagerFragment.typeProject).equalsIgnoreCase("2")){
			init(R.layout.project_manager_item_post);
		}else{
			init(R.layout.project_manager_item_v3);
		}
		
	}

	public ProjectListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if((ProjectManagerFragment.typeProject).equalsIgnoreCase("2")){
			init(R.layout.project_manager_item_post);
		}else{
			init(R.layout.project_manager_item_v3);
		}
	}

	@Override
	public void init(int res) {
		super.init(res);
//		Log.e("typeProject", "typeProject==" + ProjectManagerFragment.typeProject);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		if((ProjectManagerFragment.typeProject).equalsIgnoreCase("2")){
//			findViewById(R.id.pm_button_video).setOnClickListener(this);
			findViewById(R.id.pm_button_preview).setOnClickListener(this);
			findViewById(R.id.pm_button_support).setOnClickListener(this);
			findViewById(R.id.pm_button_delivery).setOnClickListener(this);
			findViewById(R.id.pm_button_delete).setOnClickListener(this);
			fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_down_search);
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
		}else{
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
		
		
	}

	@Override
	public void refresh() {
		super.refresh();
		
       try {
		if((ProjectManagerFragment.typeProject).equalsIgnoreCase("2")){
			   	setText(((BaseItem) getData()).getString("supported"),
					R.id.homeitem_txtname_1);//total_money_support
		   		setText(((BaseItem) getData()).getString("percent"),
		   				R.id.homeitem_txtname_2);
		   		setText(((BaseItem) getData()).getString("total_date"),
		   				R.id.homeitem_txtname_3);
		   		ImageLoaderUtils.getInstance(context).DisplayImage(((BaseItem) getData()).getString("img"),
					(ImageView) findViewById(R.id.homeitem_img));
			    if(((BaseItem) getData()).getString("title").equalsIgnoreCase("null")){
			    	setText("",
			   				R.id.project_postlist_title);
			    }else{
		    	   	setText(((BaseItem) getData()).getString("title"),
		   				R.id.project_postlist_title);
			    }
		   		setText(((BaseItem) getData()).getString("status"),
		   				R.id.project_postlist_status);
		   		setText(getResources().getString(R.string.project_post_date_modified)  + ((BaseItem) getData()).getString("modified"),
		   				R.id.project_postlist_time_modified);
		   		setText(getResources().getString(R.string.project_post_date_created)  + ((BaseItem) getData()).getString("created"),
		   				R.id.project_postlist_time_created);
		   		if(((BaseItem) getData()).getString("desc").equalsIgnoreCase("null")){
		   			setText("",
			   				R.id.project_postlist_desc);
		   		}else{
		   			String desc_value = ((BaseItem) getData()).getString("desc");
//	   			String d= Html.fromHtml(abc); 
//	   			setText("",
//		   				R.id.project_postlist_desc);
		   			TextView desc = (TextView)findViewById(R.id.project_postlist_desc);
		   			desc.setText(Html.fromHtml( desc_value ));
		   		}
		   		if((((BaseItem) getData()).getString("public_flg")).equalsIgnoreCase("1")){
		   			((TextView) findViewById(R.id.project_postlist_status)).setTextColor(Color.RED);
		   		}else{
		   			((TextView) findViewById(R.id.project_postlist_status)).setTextColor(Color.BLACK);
		   		}
				
				String public_flg = ((BaseItem) getData()).getString("public_flg");
				String total_date = ((BaseItem) getData()).getString("total_date");
				this.findViewById(R.id.pm_button_video).setVisibility(View.GONE);
				this.findViewById(R.id.pm_button_preview).setVisibility(View.GONE);
				this.findViewById(R.id.pm_button_support).setVisibility(View.GONE);
				this.findViewById(R.id.pm_button_delivery).setVisibility(View.GONE);
				this.findViewById(R.id.pm_button_delete).setVisibility(View.GONE);
				if(public_flg.equalsIgnoreCase("1")){
					this.findViewById(R.id.pm_button_support).setVisibility(View.VISIBLE);
					this.findViewById(R.id.pm_button_delivery).setVisibility(View.VISIBLE);
				}else{
					if(total_date.equalsIgnoreCase(getResources().getString(R.string.project_post_date_end))){
//						this.findViewById(R.id.pm_button_video).setVisibility(View.VISIBLE);
						this.findViewById(R.id.pm_button_preview).setVisibility(View.VISIBLE);
						this.findViewById(R.id.pm_button_delivery).setVisibility(View.VISIBLE);
						this.findViewById(R.id.pm_button_delete).setVisibility(View.VISIBLE);
					}else{
//						this.findViewById(R.id.pm_button_video).setVisibility(View.VISIBLE);
						this.findViewById(R.id.pm_button_preview).setVisibility(View.VISIBLE);
						this.findViewById(R.id.pm_button_delivery).setVisibility(View.VISIBLE);
					}
				}
				header.startAnimation(fadeOut);
			}else{
				ImageLoaderUtils.getInstance(context).DisplayImage(((BaseItem) getData()).getString("img"),
		   				(ImageView) findViewById(R.id.homeitem_img));
				if(((BaseItem) getData()).getString("title").equalsIgnoreCase("null")){
			    	setText("",
			   				R.id.homeitem_txtname);
			    }else{
		    	   	setText(((BaseItem) getData()).getString("title"),
		   				R.id.homeitem_txtname);
			    }
//			setText(((BaseItem) getData()).getString("title"),
//					R.id.homeitem_txtname);
				int post = Integer.parseInt(((BaseItem) getData()).getString("key_value"));
				if(post % 2 == 0){
					header.startAnimation(fadeOut);
				}else{
					header.startAnimation(fadeOut2);
				}
			}
		   
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	@Override
	public void onClick(View v) {
//		int id = v.getId();
//		 CommonAndroid.showDialog(getContext(), getTop() + "::" + projectmanagerFragment, null);
		 if (projectmanagerFragment != null) {
			 projectmanagerFragment.actionProjectPost( v , (BaseItem)getData());
		 }
	}

	private ProjectManagerFragment projectmanagerFragment;

	public void addProjectManagerFragment(ProjectManagerFragment projectmanagerFragment) {
		this.projectmanagerFragment = projectmanagerFragment;
	}
}