package org.acv.bynal.dialog;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.views.A47_2projectdetail_support_detail_contentView;
import org.acv.bynal.views.HeaderView;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.nbase.MbaseAdialog;
import com.org.payment.sdk.StripeActivity;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;

public class ProjectDetailSupportDetailDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	String support_info_value = "";
	ProjectDetailSupportDialog projectDetailSupportDetailDialog;
	Animation fadeOut, fadeIn;
	private LinearLayout header;
	private JSONObject jsonAddress;

	public ProjectDetailSupportDetailDialog(Context context, OnClickListener clickListener, ProjectDetailSupportDialog projectDetailSupportDetailDialog, JSONObject jsonObject) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.projectDetailSupportDetailDialog = projectDetailSupportDetailDialog;
		this.jsonAddress = jsonObject;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		A47_2projectdetail_support_detail_contentView a47_2projectdetail_support_detail_contentView = (A47_2projectdetail_support_detail_contentView) findViewById(R.id.a47_2projectdetail_support_detail_contentView);
		a47_2projectdetail_support_detail_contentView.addJsonAddress(jsonAddress);
		initHeader();
		openPopActivity(findViewById(R.id.pj_detail_support_detail_m_main));
		try {
			AccountDB support_info = new AccountDB(getActivity());
			support_info_value = support_info.getData("support_info_value");
			String support_info_desc = support_info.getData("support_info_desc");
			String support_info_offer_date = support_info.getData("support_info_offer_date");
			((TextView) findViewById(R.id.support_detail_value)).setText(support_info_value + "円");
			((TextView) findViewById(R.id.support_detail_desc)).setText(support_info_desc);

			StringTokenizer stringTokenizer = new StringTokenizer(support_info_offer_date, "-");
			String year = stringTokenizer.nextToken();
			String month = stringTokenizer.nextToken();
			String day = stringTokenizer.nextToken().substring(0, 2);
			String dayShow = String.format("%s年%s月%s日", year, month, day);
			((TextView) findViewById(R.id.support_detail_date)).setText(dayShow);

			findViewById(R.id.support_detail_btnok).setOnClickListener(this);
			findViewById(R.id.support_detail_btcancel).setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if (ProjectDetailActivity.PaymentService == null) {
		 * PaymentServiceStart(); }
		 */
		header = (LinearLayout) findViewById(R.id.pj_detail_support_detail_m_main);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_zoom_in);/* v3_slide_down_search */
		fadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				final ScrollView scrollview = ((ScrollView) findViewById(R.id.detail_main));
				scrollview.post(new Runnable() {
					@Override
					public void run() {
						scrollview.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
		header.startAnimation(fadeOut);

	}

	/*
	 * public void PaymentServiceStart() { Intent intent = new
	 * Intent(getActivity(), PayPalService.class);
	 * intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
	 * ProjectDetailActivity.config); ProjectDetailActivity.PaymentService =
	 * getActivity().startService(intent); // Toast.makeText(getActivity(),
	 * "Payment start Service", Toast.LENGTH_LONG).show(); }
	 */

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_support_detail_header);
		HeaderOption headerOption = new HeaderOption(getContext(), TYPEHEADER.NORMAL) {
			@Override
			public void onClickButtonLeft() {
				super.onClickButtonLeft();
				close(false);
			}

			@Override
			public void onClickButtonRight() {
				super.onClickButtonRight();
				close(true);
				// Intent mIntent = new Intent(getActivity(),
				// BaseTabActivity.class);
				// ProjectDetailActivity.gotoNewPage(mIntent);
				// BaseTabActivity.gotoHome(getActivity());
				projectDetailSupportDetailDialog.dismiss();
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_support_confirm);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_support_detail_m_main), new AnimationAction() {
			@Override
			public void onAnimationEnd() {
				dismiss();
				if (isOnClick) {
					clickListener.onClick(null, 0);
				}
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.projectdetail_support_detail;
	}

	@Override
	public void onClick(View v) {
		int option = v.getId();
		switch (option) {
		case R.id.support_detail_btnok:
			// Intent PagePayment = new Intent(getActivity(),
			// SampleActivity.class);
			// PageDetail.putExtra("projectIdDetail",
			// Integer.parseInt(projectID));
			// getActivity().startActivity(PagePayment);

			// CommonAndroid.showDialog(getActivity(),getActivity().getResources().getString(R.string.error_message_payment_not_support)
			// , null);

			/*
			 * final String[] items = new String[] {
			 * getActivity().getString(R.string.payment_support_paypal),
			 * getActivity().getString(R.string.payment_support_stripe) };
			 * String title =
			 * getActivity().getString(R.string.payment_dialog_title) ; new
			 * TvChooserDialog(getActivity(), items , title) {
			 * 
			 * @Override public void onItemClick(int item, String text) {
			 * onBuyPressed(support_info_value, item); } }.show();
			 */
			break;
		case R.id.support_detail_btcancel:

			closePopActivity(getContext(), findViewById(R.id.pj_detail_support_detail_m_main), new AnimationAction() {
				@Override
				public void onAnimationEnd() {
					dismiss();
				}
			});

			break;
		}
	}

	/*
	 * private PayPalPayment getThingToBuy(String paymentIntent) { return new
	 * PayPalPayment(new BigDecimal(support_info_value ), "JPY", "Support: " +
	 * ProjectDetailActivity.projectTitle, paymentIntent); }
	 */
	/*
	 * public void onBuyPressed(String support_info_value, int type) {
	 * ProjectDetailActivity.tChargeStripeID = null; if (type == 0) {
	 * 
	 * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
	 * Change PAYMENT_INTENT_SALE to - PAYMENT_INTENT_AUTHORIZE to only
	 * authorize payment and capture funds later. - PAYMENT_INTENT_ORDER to
	 * create a payment for authorization and capture later via calls from your
	 * server.
	 * 
	 * Also, to include additional payment details and an item list, see
	 * getStuffToBuy() below.
	 * 
	 * 
	 * PayPalPayment thingToBuy =
	 * getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
	 * 
	 * 
	 * * See getStuffToBuy(..) for examples of some available payment options.
	 * 
	 * 
	 * Intent intent = new Intent(getActivity(), PaymentActivity.class);
	 * 
	 * intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy); close(true);
	 * ProjectDetailActivity.gotoNewPage(intent,
	 * ProjectDetailActivity.REQUEST_CODE_PAYMENT); //
	 * getActivity().startActivityForResult(intent, //
	 * ProjectDetailActivity.REQUEST_CODE_PAYMENT); } else if (type == 1) { //
	 * ======payment service strip Intent intent = new Intent(getActivity(),
	 * StripeActivity.class); intent.putExtra("support_info_title",
	 * ProjectDetailActivity.projectTitle);
	 * intent.putExtra("support_info_value", support_info_value); close(true);
	 * ProjectDetailActivity.gotoNewPage(intent,
	 * ProjectDetailActivity.REQUEST_CODE_PAYMENT2); //
	 * getActivity().startActivityForResult(intent, //
	 * ProjectDetailActivity.REQUEST_CODE_PAYMENT2); }
	 * 
	 * }
	 */
	private Context getActivity() {
		return getContext();
	}
}