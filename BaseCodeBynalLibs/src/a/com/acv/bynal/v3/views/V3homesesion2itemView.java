package a.com.acv.bynal.v3.views;

import org.json.JSONObject;

import a.com.acv.bynal.v3.database.ProjectTable;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.tab.BaseTabActivity;

public class V3homesesion2itemView extends BaseView implements OnClickListener {

	public V3homesesion2itemView(Context context) {
		super(context);

		init(R.layout.v3home_sesion2_item);
	}

	public V3homesesion2itemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.v3home_sesion2_item);

	}

	@Override
	public void init(int res) {
		super.init(res);
		findViewById(R.id.v3home_sesion2_item_main).setOnClickListener(this);
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
		ProjectTable baseItem = (ProjectTable) getData();
		String img = baseItem.getData("img");
		String title = baseItem.getData("title");
		String total_money_support = baseItem.getData("supported");
		String total_date = baseItem.getData("total_date");
		String percent = baseItem.getData("percent");
		ImageLoaderUtils.getInstance(getContext()).DisplayImage(img, (ImageView) findViewById(R.id.v3home_sesion2_item_img), null);
		setText(title, R.id.v3home_sesion2_item_img_txt);
	}
}