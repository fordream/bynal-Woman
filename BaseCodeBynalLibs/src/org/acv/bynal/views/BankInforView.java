package org.acv.bynal.views;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseView;

//org.acv.bynal.views.BankInforView
public class BankInforView extends BaseView implements OnClickListener {

	public BankInforView(Context context) {
		super(context);
		init(R.layout.bank_infor);
	}

	public BankInforView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.bank_infor);
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

	public void update(JSONObject jsonObject) {
		((TextView) findViewById(R.id.bank_infor_card_no)).setText(getString(jsonObject, "card_number"));
		((TextView) findViewById(R.id.bank_infor_card_expire)).setText(getString(jsonObject, "card_expire"));
		((TextView) findViewById(R.id.bank_infor_card_securitycode)).setText(getString(jsonObject, "card_code"));
	}

	private CharSequence getString(JSONObject jsonObject, String key) {
		String value = null;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
			}
		}

		if (value != null && value.equalsIgnoreCase("null")) {
			value = "";
		}
		return value;
	}

	public void put(Map<String, String> sendData) {
		sendData.put("bank_infor_card_no", ((TextView) findViewById(R.id.bank_infor_card_no)).getText().toString().trim());
		sendData.put("bank_infor_card_expire", ((TextView) findViewById(R.id.bank_infor_card_expire)).getText().toString().trim());
		sendData.put("bank_infor_card_securitycode", ((TextView) findViewById(R.id.bank_infor_card_securitycode)).getText().toString().trim());
	}
}