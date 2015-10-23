package com.org.payment.sdk;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.dialog.BynalProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.util.ByUtils;
import com.stripe.android.Stripe;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Charge;
import com.stripe.android.utils.CVCNumberEditText;
import com.stripe.android.utils.CardNumberEditText;
import com.stripe.android.utils.MonthEditText;
import com.stripe.android.utils.YearEditText;
/**
 * Simple demo Activity.
 * To use validation of credit card number, cvc, month and year you should use custom EditText widgets:
 * <br />CardNumberEditText, MonthEditText, YearEditText, CVCNumberEditText 
 * <br /><br />To use them in layout you have to type full class name e.g. com.stripe.android.utils.CardNumberEditText 
 */
public class StripeActivity extends Activity {
	private ImageButton ImageButtonBack;
	private Button mOrderButton;
	private TextView mTreatCarTextField;
	private CardNumberEditText mNumberTextField;
	private MonthEditText mExpMonthTextField;
	private YearEditText mExpYearTextField;
	private CVCNumberEditText mCVCTextField;
//	private TextView mAmountTextView;
	
	private Map<String, Object> mChargeMap;
	private Map<String, Object> mCardMap;
	Context txt ;
	String support_info_value = "";
	String support_info_title = "";
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Replace api key for your own use
		Stripe.apiKey = "sk_test_5va06wOo3ltjYIWot6IrNRjn";//pk_test_d3uakf7qDKxP8kMgPOKafKVZ
		setContentView(R.layout.stripe_activity_layout);
		
		mChargeMap = new HashMap<String, Object>();
		mCardMap = new HashMap<String, Object>();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			support_info_value = extras.getString("support_info_value");
			support_info_title = extras.getString("support_info_title");
//			CommonAndroid.showDialog(this, "support_info_value:" + support_info_value, null);
		}else{
			finish();
		}
		setupUIInstances();
		setupEditTextListeners();
		setupButtonListeners();
		txt = this;
		progressDialog = new BynalProgressDialog(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCardMap = null;
		mChargeMap = null;
		System.gc();
	}
	
	/**
	 * Sets up UI variables.
	 */
	private void setupUIInstances() {
		ImageButtonBack = (ImageButton) findViewById(R.id.headerbutton1);
		mOrderButton = (Button) findViewById(R.id.orderButton);
		mTreatCarTextField = (TextView) findViewById(R.id.treatCarTextField);
		mNumberTextField = (CardNumberEditText) findViewById(R.id.numberTextField);
		mExpMonthTextField = (MonthEditText) findViewById(R.id.expMonthTextField);
		mExpYearTextField = (YearEditText) findViewById(R.id.expYearTextField);
		mCVCTextField = (CVCNumberEditText) findViewById(R.id.cvcTextField);
//		mAmountTextView = (TextView) findViewById(R.id.textView1);
	}
	/**
	 * Private method used to reset fields
	 */
	private void resetErrors() {
		mNumberTextField.setBackgroundResource(android.R.drawable.edit_text);
		mCVCTextField.setBackgroundResource(android.R.drawable.edit_text);
		mExpMonthTextField.setBackgroundResource(android.R.drawable.edit_text);
		mExpYearTextField.setBackgroundResource(android.R.drawable.edit_text);
	}
	
	/**
	 * Private method invoking fields validation
	 */
	private boolean validateFields() {
		boolean validated = true;
		
		if(!mNumberTextField.isValidCardNumber()) {
			mNumberTextField.setBackgroundResource(R.drawable.textfield_search_empty_pressed);
			validated = false;
		}
		if(!mCVCTextField.isValidCVCNumber(mNumberTextField.getType())) {
			mCVCTextField.setBackgroundResource(R.drawable.textfield_search_empty_pressed);
			validated = false;
		}
		if(!mExpMonthTextField.isValidMonth()) {
			mExpMonthTextField.setBackgroundResource(R.drawable.textfield_search_empty_pressed);
			validated = false;
		}
		if(!mExpYearTextField.isValidDate(mExpMonthTextField.getMonth())) {
			mExpYearTextField.setBackgroundResource(R.drawable.textfield_search_empty_pressed);
			mExpMonthTextField.setBackgroundResource(R.drawable.textfield_search_empty_pressed);
			validated = false;
		}
		
		return validated;
	}

	/**
	 * Adds a button onClick event to send the request.
	 */
	private void setupButtonListeners() {
		ImageButtonBack.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mOrderButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				ProjectDetailActivity.tChargeStripeID = null;
//				progressDialog = ProgressDialog.show(txt, "", "Loading...", true);
				progressDialog.show();
				resetErrors();
				
				if(!validateFields()) {
					Toast.makeText(
                            getApplicationContext(),
                            "err1 validateFields", Toast.LENGTH_LONG)
                            .show();
					progressDialog.dismiss();
					return;
				}
				
				mChargeMap.put("amount", support_info_value);
				mChargeMap.put("currency", "jpy");
				mCardMap.put("number", mNumberTextField.getText().toString());
				mCardMap.put("exp_month", Integer.valueOf(mExpMonthTextField.getText().toString()));
				mCardMap.put("exp_year", Integer.valueOf(mExpYearTextField.getText().toString()));
				mChargeMap.put("card", mCardMap);

				new AsyncTask<Map<String, Object>, Void, Charge>() {
					Charge tCharge = null;

					/**
					 * Charge.create should be done in async task, because it uses HTTPS connection.
					 * All methods using internet connection should be used in Async Task in Android.
					 */
					@Override
					protected Charge doInBackground(Map<String, Object>... param) {
						try {
							tCharge = Charge.create(param[0]);
							System.out.println(tCharge);
							if(tCharge.getId() != null){
								ProjectDetailActivity.tChargeStripeID = tCharge.getId();
							}
							progressDialog.dismiss();
							finish();
						} catch (StripeException e) {
							progressDialog.dismiss();
							e.printStackTrace();
						}
						return tCharge;
					}
				}.execute(mChargeMap);
			}
		});
	}
	/**
	 * Sets text listener to update price in textview.
	 */
	private void setupEditTextListeners() {
//		mTreatCarTextField.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				if(!arg0.toString().equals("")) {
				mTreatCarTextField.setText( support_info_title + ": "+support_info_value + this.getString(R.string.project_detail_support_btsupport));
//				} else {
//					mAmountTextView.setText("$0");
//				}
//			}
//		});
	}
}
