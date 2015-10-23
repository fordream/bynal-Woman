package org.acv.bynal.dialog;

import java.util.HashMap;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;
import org.acv.bynal.main.activity.MainHomeActivity;
import org.acv.bynal.message.MessageActivity;
import org.acv.bynal.views.HeaderView;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.ImageLoaderUtils;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.MbaseAdialog;

public class ProjectDetailInfoDialog extends MbaseAdialog implements android.view.View.OnClickListener {
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	String user_id;
	Animation Ani;
	private RelativeLayout header;

	public ProjectDetailInfoDialog(Context context, OnClickListener clickListener) {
		super(context, clickListener);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHeader();
		header = (RelativeLayout) findViewById(R.id.pj_detail_info_m_main);
		Ani = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_bounce);
		Ani.setAnimationListener(new Animation.AnimationListener() {
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
		openPopActivity(findViewById(R.id.pj_detail_info_m_main));
		final LinearLayout viewcontent = (LinearLayout) findViewById(R.id.main_content);
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				viewcontent.setVisibility(View.VISIBLE);
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						if (jsonObject.getString("user_id") != null) {
							user_id = jsonObject.getString("user_id");
							TextView user_name = (TextView) findViewById(R.id.project_info_name);
							user_name.setText(jsonObject.getString("user_name"));
							String img_avata = jsonObject.getString("user_img") + "&time=" + System.currentTimeMillis();
							ImageLoaderUtils.getInstance(getActivity()).DisplayImage( img_avata, (ImageView) findViewById(R.id.image_project_profile), BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_user_square2));
							String tempString = getActivity().getString(R.string.project_info_sendmessage_txt);
							TextView text_send_message = (TextView) findViewById(R.id.project_info_sendmessage);
							// text_send_message.setPaintFlags(text_send_message.getPaintFlags()
							// | Paint.UNDERLINE_TEXT_FLAG);
							SpannableString spanString = new SpannableString(tempString);
							spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
							text_send_message.setText(spanString);
							header.startAnimation(Ani);
						}
					}

				} catch (JSONException e) {
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				viewcontent.setVisibility(View.VISIBLE);
				CommonAndroid.showDialog(getActivity(), getActivity().getResources().getString(R.string.error_connect_server), null);
			}
		};
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				sendData = new HashMap<String, String>();
				sendData.put("project_id", Integer.toString(ProjectDetailActivity.projectId));
				new APICaller(getActivity()).callApi("/project/info", true, callbackAPI, sendData);
			}
		}, 500);

		findViewById(R.id.project_info_sendmessage).setOnClickListener(this);
		findViewById(R.id.send_message).setOnClickListener(this);
	}

	private void initHeader() {
		HeaderView headerView = (HeaderView) findViewById(R.id.pj_detail_info_header);
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
				gotoHome();
			}
		};

		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.home_xml);
		headerOption.setResHeader(R.string.header_project_detail_info);
		headerView.setheaderOption(headerOption);
		headerView.refresh();
	}

	private void close(final boolean isOnClick) {
		closePopActivity(getContext(), findViewById(R.id.pj_detail_info_m_main), new AnimationAction() {

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
		return R.layout.projectdetail_info;
	}

	@Override
	public void onClick(View v) {
		int option = v.getId();
		switch (option) {
		case R.id.send_message:
		case R.id.project_info_sendmessage:
			AccountDB accountDB = new AccountDB(getActivity());
			if (accountDB.getToken() == null) {
				CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.error_message_pleaselogin), null);
			} else {
				String user_id_login = accountDB.getUserId();
				// CommonAndroid.showDialog(getActivity(), "user_id:" + user_id
				// + "::user_id_login:" + user_id_login , null);
				if (!user_id.equalsIgnoreCase(user_id_login)) {

					// pj_detail_info_m_main

					MessageActivity.bitmap = MainHomeActivity.getBimap(findViewById(R.id.pj_detail_info_m_main));
					MessageActivity.startActivity(getmContext(), user_id);
				} else {
					CommonAndroid.showDialog(getActivity(), getActivity().getString(R.string.err_sendmessage_userid_invalid), null);
				}

			}
			break;
		default:
			break;
		}
	}

	private Context getActivity() {
		return getContext();
	}
}