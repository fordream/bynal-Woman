package a.com.acv.bynal.v3.dialog;

import java.util.HashMap;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class A47_1Dialog extends MbaseAdialog {
	@Override
	public int getLayout() {
		return R.layout.a47_1;
	}

	private JSONObject itemAtPosition;

	public A47_1Dialog(Context context, OnClickListener clickListener, JSONObject itemAtPosition) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.itemAtPosition = itemAtPosition;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (itemAtPosition != null) {
			((TextView) findViewById(R.id.headertextView1)).setText(R.string.edit_address);
			try {
				((TextView) findViewById(R.id.a47_1_1)).setText(itemAtPosition.getString("delivery_name"));
				((TextView) findViewById(R.id.a47_1_2)).setText(itemAtPosition.getString("delivery_zip1"));
				((TextView) findViewById(R.id.a47_1_3)).setText(itemAtPosition.getString("delivery_zip2"));
				((TextView) findViewById(R.id.a47_1_4)).setText(itemAtPosition.getString("delivery_address"));
				((TextView) findViewById(R.id.a47_1_5)).setText(itemAtPosition.getString("delivery_phone"));
			} catch (Exception exception) {

			}
		} else {
			((TextView) findViewById(R.id.headertextView1)).setText(R.string.add_address);
		}

		findViewById(R.id.a47_1_btn).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callApi();
			}

		});

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.a_bot_to_top_in);
		findViewById(R.id.a47_1_main).startAnimation(animation);
		findViewById(R.id.a47_1_x).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(null);
			}
		});
	}

	private void close(final JSONObject jsonObject) {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.a_top_to_bot_out);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
				if (jsonObject != null) {
					compelete(jsonObject);
				}
			}

		});

		findViewById(R.id.a47_1_main).startAnimation(animation);
	}

	private void callApi() {

		if (ByUtils.isBlankCheckBllank(((TextView) findViewById(R.id.a47_1_1)))) {
			CommonAndroid.toast(getContext(), R.string.editaddress_1);
			return;
		}

		if (ByUtils.isBlankCheckBllank(((TextView) findViewById(R.id.a47_1_2)))) {

			CommonAndroid.toast(getContext(), R.string.editaddress_2);
			return;
		}

		if (ByUtils.isBlankCheckBllank(((TextView) findViewById(R.id.a47_1_3)))) {
			CommonAndroid.toast(getContext(), R.string.editaddress_3);
			return;
		}

		if (ByUtils.isBlankCheckBllank(((TextView) findViewById(R.id.a47_1_4)))) {
			CommonAndroid.toast(getContext(), R.string.editaddress_3);
			return;
		}

		if (ByUtils.isBlankCheckBllank(((TextView) findViewById(R.id.a47_1_5)))) {
			CommonAndroid.toast(getContext(), R.string.editaddress_4);
			return;
		}

		APICaller apiCaller = new APICaller(getContext());
		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("token", new AccountDB(getContext()).getToken());
		try {
			sendData.put("delivery_id", itemAtPosition.getString("delivery_id"));
		} catch (Exception exception) {
			sendData.put("delivery_id", "");
		}
		sendData.put("delivery_name", ((TextView) findViewById(R.id.a47_1_1)).getText().toString().trim());
		sendData.put("delivery_zip1", ((TextView) findViewById(R.id.a47_1_2)).getText().toString().trim());
		sendData.put("delivery_zip2", ((TextView) findViewById(R.id.a47_1_3)).getText().toString().trim());
		sendData.put("delivery_address", ((TextView) findViewById(R.id.a47_1_4)).getText().toString().trim());
		sendData.put("delivery_phone", ((TextView) findViewById(R.id.a47_1_5)).getText().toString().trim());

		apiCaller.callApi("/user/updatedelivery", true, new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {

				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						CommonAndroid.toast(getContext(), itemAtPosition == null ? R.string.add_address_success : R.string.update_address_success);
						JSONObject data = jsonObject.getJSONObject("data");
						data.put("delivery_name", ((TextView) findViewById(R.id.a47_1_1)).getText().toString().trim());
						data.put("delivery_zip1", ((TextView) findViewById(R.id.a47_1_2)).getText().toString().trim());
						data.put("delivery_zip2", ((TextView) findViewById(R.id.a47_1_3)).getText().toString().trim());
						data.put("delivery_address", ((TextView) findViewById(R.id.a47_1_4)).getText().toString().trim());
						data.put("delivery_phone", ((TextView) findViewById(R.id.a47_1_5)).getText().toString().trim());

						close(data);
					} else if (jsonObject.getString("status").equals("0")) {
						CommonAndroid.toast(getContext(), itemAtPosition == null ? R.string.add_address_fail : R.string.update_address_fail);
						CommonAndroid.showDialog(MainHomeActivity.homeActivity, jsonObject.getString("message"), null);
					}
				} catch (JSONException e) {
					CommonAndroid.showDialog(MainHomeActivity.homeActivity, getContext().getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(MainHomeActivity.homeActivity, getContext().getResources().getString(R.string.error_connect_server), null);
			}
		}, sendData);
	}

	public void compelete(JSONObject jsonObject) {

	}
}