package org.acv.bynal.activity;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.dialog.ProjectDetailAngelDialog;
import org.acv.bynal.dialog.ProjectDetailCommentDialog;
import org.acv.bynal.dialog.ProjectDetailInfoDialog;
import org.acv.bynal.dialog.ProjectDetailReportDialog;
import org.acv.bynal.dialog.ProjectDetailTagDialog;
import org.acv.bynal.fragment.ProjectDetailFragment;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class ProjectDetailActivity extends BaseActivity {
	public static int projectId = 125;
	public static String projectTitle = "";
	public static String projectDesc = "";
	public static int projectTotalDate = -1;
	public static String project_report_no = "";
	public static String projectIdDetailPreview = "0";
	ProjectDetailFragment project_detail;

	private static final String TAG = "paymentExample";
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "ASSsXhCW0ouJ2LbtHRIr7sjdQr3c77KYEoUj42IgMG0CEo0r7OHQZ3NoLS3D";//

	public static final int REQUEST_CODE_PAYMENT = 1;
	public static final int REQUEST_CODE_PAYMENT2 = 2;
	public static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)
	// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy")).merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
	public static ComponentName PaymentService = null;
	public static String tChargeStripeID = null;
	static Context contex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		projectIdDetailPreview = "0";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// TODO: get projectid
			projectId = extras.getInt("projectIdDetail");
			if (extras.getString("projectIdDetailPreview") != null) {
				projectIdDetailPreview = extras.getString("projectIdDetailPreview");
			}
		}
		configMenuRight();
		project_detail = new ProjectDetailFragment();
		changeFragemt(project_detail);
		openFragmentListener(findViewById(R.id.mFrament), project_detail.getAnimationAction());
		this.contex = this;
	}

	// @Override
	// protected void onResume(){
	// super.onResume();
	// changeFragemt(project_detail);
	// BynalAnimationUtils.openActivityRightToLeft(this,
	// findViewById(R.id.mFrament), project_detail.getAnimationAction());
	//
	// }

	@Override
	public void configMenuleft() {
		findViewById(R.id.menuslide_header).setVisibility(View.GONE);
		findViewById(R.id.menu_main_content).setBackgroundResource(R.drawable.menuright_detail_shadow);
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_detail_info), R.drawable.menu_pj_detail_info, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_detail_tag), R.drawable.menu_pj_detail_tag, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_detail_report), R.drawable.menu_pj_detail_report, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_detail_angel), R.drawable.menu_pj_detail_angel, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_detail_comment), R.drawable.menu_pj_detail_comment, true));
	}

	/*
	 * @Override public void refresh() { super.refresh(); clearMenu();
	 * configMenuleft(); refreshMenu(); // changeFragemt(new HomeFragment()); }
	 */
	public void opendialog(int dialog) {
		switch (dialog) {
		case 1:
			new ProjectDetailInfoDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case 2:
			new ProjectDetailTagDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case 3:
			new ProjectDetailReportDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case 4:
			new ProjectDetailAngelDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		case 5:
			new ProjectDetailCommentDialog(this, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			break;
		default:
			break;
		}

	}

	private int TIMEDELAY = 500;

	@Override
	public void onItemClick(int position, MenuItem item) {
		super.onItemClick(position, item);
		if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_detail_info))) {
			// changeFragemt(new ProjectDetailInfoFragment());
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(1);
				}
			}, TIMEDELAY);

		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_detail_tag))) {
			// changeFragemt(new ProjectDetailTagFragment());
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(2);
				}
			}, TIMEDELAY);
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_detail_report))) {
			// changeFragemt(new ProjectDetailReportFragment());
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(3);
				}
			}, TIMEDELAY);

		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_detail_angel))) {
			// changeFragemt(new ProjectDetailAngelFragment());
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(4);
				}
			}, TIMEDELAY);

		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_detail_comment))) {
			// changeFragemt(new ProjectDetailCommentFragment());
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					opendialog(5);
				}
			}, TIMEDELAY);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.e(TAG, confirm.toJSONObject().toString(4));
						Log.e(TAG, confirm.getPayment().toJSONObject().toString(4));
						String response = confirm.toJSONObject().toString(4);
						JSONObject jsonObject = new JSONObject(response);
						String temp = jsonObject.getString("response");
						JSONObject jsonObject2 = new JSONObject(temp);
						String TDPay = jsonObject2.getString("id");
						/**
						 * TODO: send 'confirm' (and possibly
						 * confirm.getPayment() to your server for verification
						 * or consent completion. See
						 * https://developer.paypal.com
						 * /webapps/developer/docs/integration
						 * /mobile/verify-mobile-payment/ for more details.
						 * 
						 * For sample mobile backend interactions, see
						 * https://github
						 * .com/paypal/rest-api-sdk-python/tree/master
						 * /samples/mobile_backend
						 */
						// Toast.makeText(
						// getApplicationContext(),
						// "PaymentConfirmation info received from PayPal",
						// Toast.LENGTH_LONG)
						// .show();
						CommonAndroid.showDialog(this, "PayPal Successful \nTransactionsID: " + TDPay, null);
					} catch (JSONException e) {
						// Log.e(TAG,
						// "an extremely unlikely failure occurred: ", e);
						CommonAndroid.showDialog(this, e.getMessage(), null);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
				Toast.makeText(getApplicationContext(), this.getString(R.string.payment_user_cancel) , Toast.LENGTH_LONG).show();
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
				Toast.makeText(getApplicationContext(), "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == REQUEST_CODE_PAYMENT2) {
			if (tChargeStripeID != null) {
				CommonAndroid.showDialog(this, "Stripe Successful \nTransactionsID: " + tChargeStripeID, null);
			} else {
				Toast.makeText(getApplicationContext(), this.getString(R.string.payment_user_cancel), Toast.LENGTH_LONG).show();
			}

		}
	}

	// public static void gotoHome(){
	// ((Activity) contex).setResult(ByUtils.REQUEST_HOME);
	// ((Activity) contex).finish();
	// }

	public static void gotoNewPage(Intent mIntent) {
		((Activity) contex).startActivityForResult(mIntent, ByUtils.REQUEST_PROJECT_DETAIL);
	}

	public static void gotoNewPage(Intent mIntent, int requestcode) {
		((Activity) contex).startActivityForResult(mIntent, requestcode);
	}

}
