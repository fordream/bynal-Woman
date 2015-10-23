package a.com.acv.bynal.v3.views;

import a.com.acv.bynal.v3.database.ProjectTable;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.tab.BaseTabActivity;

public class V3homesesion1itemView extends BaseView implements View.OnClickListener {

	public V3homesesion1itemView(Context context) {
		super(context);

		init(R.layout.v3home_sesion1_item);
	}

	public V3homesesion1itemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.v3home_sesion1_item);
	}

	@Override
	public void init(int res) {
		super.init(res);
		findViewById(R.id.v3home_sesion1_item_img_bg).setOnClickListener(this);
		
		//findViewById(R.id.v3home_sesion1_item_img).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpharepeat));
	}

	@Override
	public void onClick(View v) {
		ProjectTable baseItem = ((ProjectTable) getData());
		String id = baseItem.getData("id");
		BaseTabActivity.senBroadCastHeaderMenu(getContext(), "projectdetail", Integer.parseInt(id));
	}

	@Override
	public void refresh() {
		super.refresh();
		ProjectTable baseItem = ((ProjectTable) getData());
		String img = baseItem.getData("img");
		String title = baseItem.getData("title");
		String total_money_support = baseItem.getData("supported");
		String total_date = baseItem.getData("total_date");
		String percent = baseItem.getData("percent");

		ImageLoaderUtils.getInstance(getContext()).DisplayImage(img, (ImageView) findViewById(R.id.v3home_sesion1_item_img), null);

		setText(title, R.id.v3home_sesion1_item_name);

		setText(total_money_support, R.id.homeitem_txtname_1);
		setText(percent, R.id.homeitem_txtname_2);
		setText(total_date, R.id.homeitem_txtname_3);
	}
}