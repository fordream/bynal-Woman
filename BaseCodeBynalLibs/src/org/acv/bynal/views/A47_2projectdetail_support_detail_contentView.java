package org.acv.bynal.views;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;

//org.acv.bynal.views.A47_2projectdetail_support_detail_contentView
public class A47_2projectdetail_support_detail_contentView extends BaseView implements OnClickListener {

	public A47_2projectdetail_support_detail_contentView(Context context) {
		super(context);
		init(R.layout.a47_2projectdetail_support_detail_content);
	}

	public A47_2projectdetail_support_detail_contentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.a47_2projectdetail_support_detail_content);
	}

	@Override
	public void init(int res) {
		super.init(res);
	}

	@Override
	public void refresh() {
		super.refresh();

	}

	@Override
	public void onClick(View v) {
	}

	public void addJsonAddress(JSONObject jsonAddress) {
		try {
			((TextView) findViewById(R.id.a47_1_1)).setText(jsonAddress.getString("delivery_name"));
			((TextView) findViewById(R.id.a47_1_2)).setText(String.format("%s - %s", jsonAddress.getString("delivery_zip1"), jsonAddress.getString("delivery_zip2")));
			((TextView) findViewById(R.id.a47_1_3)).setText(jsonAddress.getString("delivery_address"));
			((TextView) findViewById(R.id.a47_1_4)).setText(jsonAddress.getString("delivery_phone"));
		} catch (Exception exception) {

		}
	}
}