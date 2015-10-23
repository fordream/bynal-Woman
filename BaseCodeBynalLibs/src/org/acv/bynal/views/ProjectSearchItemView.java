package org.acv.bynal.views;

import org.acv.bynal.fragment.SearchFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;

public class ProjectSearchItemView extends BaseView implements OnClickListener {
	Context context;
	Animation fadeOut, fadeIn;
	private RelativeLayout header;
	public ProjectSearchItemView(Context context) {
		super(context);
		if(SearchFragment.optionView == 1){
			init(R.layout.v3_searchitem);
		}else{
			init(R.layout.v3_searchitem2);
		}
		
	}

	public ProjectSearchItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(SearchFragment.optionView == 1){
			init(R.layout.v3_searchitem);
		}else{
			init(R.layout.v3_searchitem2);
		}
	}

	@Override
	public void init(int res) {
		super.init(res);
		header = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_zoom_in);/*v3_slide_down_search, v3_rotate*/
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
		String pj_title_value = ((BaseItem) getData()).getString("title");
		pj_title_value = pj_title_value.replaceAll("\r\n", "");
        setText(pj_title_value, R.id.homeitem_txtname);
        setText(((BaseItem) getData()).getString("supported"), /*total_money_support*/
				R.id.homeitem_txtname_1);
        setText(((BaseItem) getData()).getString("total_date"),
				R.id.homeitem_txtname_3);
        setText(((BaseItem) getData()).getString("percent"),
				R.id.homeitem_txtname_2);
        if(SearchFragment.optionView == 1){
//        	ImageLoaderUtils.getInstance(context).DisplayImage(((BaseItem) getData()).getString("img"),
//    				(ImageView) findViewById(R.id.homeitem_img));
        	ImageView image= (ImageView) findViewById(R.id.homeitem_img);//ok
        	SearchFragment.imageLoader.DisplayImage(((BaseItem) getData()).getString("img"), image);
        }else{
        	String pj_des_value = ((BaseItem) getData()).getString("desc");
        	pj_des_value = pj_des_value.replaceAll("\r\n", "");
			TextView pj_des = (TextView)findViewById(R.id.home_des_txtname);
			pj_des.setText(Html.fromHtml(pj_des_value)); 	
        }
        header.startAnimation(fadeOut);
	}

	@Override
	public void onClick(View v) {
	}

	private SearchFragment searchFragment;

	public void addProjectSearchFragment(SearchFragment searchFragment) {
		this.searchFragment = searchFragment;
	}
}