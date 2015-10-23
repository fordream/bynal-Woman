package org.acv.bynal.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.dialog.BynalProgressDialog;
import org.acv.bynal.dialog.ProjectDetailSupportDialog;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import app.bynal.woman.R;

import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.BaseFragment;

public class ProjectDetailFragment extends BaseFragment implements OnClickListener {
	private String urlDetailproject = null;
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ICallbackAPI callbackAPIFavorite;
	Context context = null;
	private WebView credit;
	/** Layout of credit. */
	private RelativeLayout rl;
	private LinearLayout header;
	private boolean showHeader = false;
	String time_start_temp = "";
	String time_end_temp = "";
	String set_staus_favorite = "";
	String token = null;
	Animation fadeOut, fadeIn, fadeOut2, fadeIn2;
	private int deltaY = 10;
	private int eventY = 0;
	private RelativeLayout viewdialog;
	private boolean showDialogshare = false;
	private boolean showDialogshare_gone = false;
	Animation Ani;
	private LinearLayout content_main;
	private boolean onLoad = true;

	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		loadPage();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		onLoad = true;
		super.onCreate(savedInstanceState);
	}

	// load page
	private ProgressDialog progressDialog;
	private String project_image;
	private String div_block;
	private String project_movie;

	private void loadPage() {
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {

				// Log.e("detail", "===========" + response);
				try {
					if (response != null) {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.getString("status").equals("1")) {
							time_start_temp = jsonObject.getString("public_date");// "2014-09-12";
							time_end_temp = jsonObject.getString("end_date");// "2014-09-12";
							project_image = jsonObject.getString("project_image");
							div_block = jsonObject.getString("div_block");
							project_movie = jsonObject.getString("project_movie");

							if (jsonObject.has("url")) {
								urlDetailproject = jsonObject.getString("url");
							}

							ProjectDetailActivity.projectTitle = jsonObject.getString("title");
							ProjectDetailActivity.projectDesc = jsonObject.getString("desc");
							ProjectDetailActivity.projectTotalDate = jsonObject.getInt("total_date");

							updataDetail(project_image, project_movie, div_block);

							if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
								ImageView img = (ImageView) getActivity().findViewById(R.id.project_detail_setlike);
								if (jsonObject.getString("favorite").equalsIgnoreCase("1")) {
									img.setImageResource(R.drawable.project_detail_setunlike_xml);
									set_staus_favorite = "0";
								} else if (jsonObject.getString("favorite").equalsIgnoreCase("0")) {
									img.setImageResource(R.drawable.project_detail_setlike_xml);
									set_staus_favorite = "1";
								}
							}

						} else {
							CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
							progressDialog.dismiss();
						}
					} else {
						Log.e("detail", "===========null");
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
						progressDialog.dismiss();
					}
				} catch (JSONException e) {
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
					progressDialog.dismiss();
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
				progressDialog.dismiss();
			}
		};

		// progressDialog = ProgressDialog.show(getActivity(), "", "Loading...",
		// true);
		progressDialog = new BynalProgressDialog(getActivity());
		progressDialog.show();
		sendData = new HashMap<String, String>();
		sendData.put("token", token);
		sendData.put("id", "" + ProjectDetailActivity.projectId);
		new APICaller(getActivity()).callApi("/project/detail", false, callbackAPI, sendData);

		// call back favorite
		callbackAPIFavorite = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						// sendData = new HashMap<String, String>();
						// sendData.put("token", token);
						// sendData.put("id", "" +
						// ProjectDetailActivity.projectId);
						// new
						// APICaller(getActivity()).callApi("/project/detail",
						// true, callbackAPI,
						// sendData);
						if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
							ImageView img = (ImageView) getActivity().findViewById(R.id.project_detail_setlike);
							if (set_staus_favorite.equalsIgnoreCase("0")) {
								img.setImageResource(R.drawable.project_detail_setlike_xml);
								set_staus_favorite = "1";
							} else if (set_staus_favorite.equalsIgnoreCase("1")) {
								CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.add_to_favorite), null);
								img.setImageResource(R.drawable.project_detail_setunlike_xml);
								set_staus_favorite = "0";
							}
						}

					} else if (!jsonObject.getBoolean("is_login")) {
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_session_login), null);
					}
				} catch (JSONException e) {
					// Log.e("PD", "err::" + e.getMessage());
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
				}
			}

			@Override
			public void onError(String message) {
				CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
			}
		};
	}

	@Override
	public void setUpFragment(View view) {
		viewdialog = (RelativeLayout) view.findViewById(R.id.share_block);
		AccountDB accountDB = new AccountDB(getActivity());
		token = accountDB.getToken();
		credit = new WebView(getActivity());
		credit.getSettings().setJavaScriptEnabled(true);
		credit.getSettings().setPluginState(PluginState.ON);
		credit.getSettings().setAllowFileAccess(true);
		// credit.requestFocus(View.FOCUS_DOWN);
		credit.setHorizontalScrollBarEnabled(false);
		credit.setBackgroundColor(Color.parseColor("#ffffff"));
		// test
		credit.getSettings().setRenderPriority(RenderPriority.HIGH);
		credit.getSettings().setSaveFormData(true);
		credit.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);
		credit.getSettings().setAppCacheEnabled(true);

		rl = (RelativeLayout) view.findViewById(R.id.detail_display);
		header = (LinearLayout) view.findViewById(R.id.pj_detail_header_action);
		content_main = (LinearLayout) view.findViewById(R.id.content_main);
		if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("1")) {
			try {
				ImageView img1 = (ImageView) header.findViewById(R.id.project_detail_share);
				img1.setImageResource(R.drawable.project_detail_share_dis);
				ImageView img2 = (ImageView) header.findViewById(R.id.project_detail_setlike);
				img2.setImageResource(R.drawable.project_detail_setlike_dis);
				ImageView img3 = (ImageView) header.findViewById(R.id.project_detail_calendar);
				img3.setImageResource(R.drawable.project_detail_calendar_dis);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		rl.addView(credit);
		header.setVisibility(View.INVISIBLE);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_down);
		fadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showHeader = true;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				if (onLoad) {
					content_main.startAnimation(Ani);
					onLoad = false;
				}
				showHeader = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
		fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_up);
		fadeIn.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showHeader = false;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				showHeader = false;
				header.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});

		fadeOut2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
		fadeOut2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showDialogshare = true;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				showDialogshare = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
		fadeIn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
		fadeIn2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
				showDialogshare = false;
				// viewdialog.setVisibility(View.GONE);
			}

			@SuppressLint("NewApi")
			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
				showDialogshare = false;
				viewdialog.setVisibility(View.GONE);
				viewdialog.setTop(2000);
				if (showDialogshare_gone) {
					showDialogshare_gone = false;
					String URL = "http://" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId;
					ByUtils.sendContact(getActivity(), "" + ProjectDetailActivity.projectId, ProjectDetailActivity.projectTitle, URL, getActivity().findViewById(R.id.mFrament));
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});

		Ani = AnimationUtils.loadAnimation(getActivity(), R.anim.v3_slide_down_pj_detail);
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
		view.findViewById(R.id.project_detail_share).setOnClickListener(this);
		view.findViewById(R.id.project_detail_money).setOnClickListener(this);
		view.findViewById(R.id.project_detail_setlike).setOnClickListener(this);
		view.findViewById(R.id.project_detail_calendar).setOnClickListener(this);
		view.findViewById(R.id.share_block).setOnClickListener(this);
		credit.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					// header.setVisibility(View.INVISIBLE);
					eventY = (int) event.getY();
					break;

				case MotionEvent.ACTION_UP:
					// header.setVisibility(View.VISIBLE);
					// if(!showHeader){
					// header.startAnimation(fadeOut);
					// }
					if (((int) event.getY() - eventY) > deltaY) {
						// Log.e("KKKK","ACTION_MOVE==DOW" + eventY + "::" +
						// ((int) event.getY()));
						if (!showHeader) {
							showHeader = true;
							header.setVisibility(View.INVISIBLE);
							header.startAnimation(fadeOut);
						} else {
							fadeIn.cancel();
							fadeOut.cancel();
						}
					} else if ((eventY - (int) event.getY()) > deltaY) {
						// Log.e("KKKK","ACTION_MOVE==UP" + eventY + "::" +
						// ((int) event.getY()));
						if (showHeader) {
							header.startAnimation(fadeIn);
						} else {
							fadeIn.cancel();
							fadeOut.cancel();
						}
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// header.setVisibility(View.GONE);
					fadeIn2.cancel();
					fadeOut2.cancel();
					break;
				default:
					break;

				}

				return false;
			}
		});
	}

	@Override
	public int layoytResurce() {
		return R.layout.projectdetail;
	}

	@Override
	public void onClick(View v) {
		int type = v.getId();
		switch (type) {
		case R.id.project_detail_share:
			if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
				final String[] items = new String[] { getResources().getString(R.string.project_detail_share_facebook), getResources().getString(R.string.project_detail_share_twitter),
						getResources().getString(R.string.project_detail_share_bookmark), getResources().getString(R.string.project_detail_share_contact) };
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);

				/*
				 * new TvChooserDialog(getActivity(), items) {
				 * 
				 * @Override public void onItemClick(int item, String text) { if
				 * (item == 0) { // share face String URL =
				 * "http://www.facebook.com/sharer/sharer.php?u=http://" +
				 * ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				 * + ProjectDetailActivity.projectId; Intent browserIntent = new
				 * Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				 * getActivity().startActivityForResult(browserIntent,
				 * ByUtils.REQUEST_PROJECT_DETAIL); } else if (item == 1) { //
				 * share twitter //
				 * https://twitter.com/share?url=http://bynal.acvdev
				 * .com/projects/detail/" // + pid ; String title =
				 * ProjectDetailActivity.projectTitle; String fotmat =
				 * getString(R.string.share_twitter_title); String str =
				 * String.format(fotmat, title, ""); String URL =
				 * "https://twitter.com/share?url=http://" +
				 * ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				 * + ProjectDetailActivity.projectId + "&text=" + str; Intent
				 * browserIntent = new Intent(Intent.ACTION_VIEW,
				 * Uri.parse(URL));
				 * getActivity().startActivityForResult(browserIntent,
				 * ByUtils.REQUEST_PROJECT_DETAIL); } else if (item == 2) { //
				 * share bookmark //
				 * http://b.hatena.ne.jp/entry/bynal.acvdev.com
				 * /projects/detail/" // + pid; String URL =
				 * "http://b.hatena.ne.jp/entry/" +
				 * ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				 * + ProjectDetailActivity.projectId; Intent browserIntent = new
				 * Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				 * getActivity().startActivityForResult(browserIntent,
				 * ByUtils.REQUEST_PROJECT_DETAIL); } else if (item == 3) { //
				 * share contact // Intent PageDetail = new
				 * Intent(getActivity(), // SendContactActivity.class); //
				 * PageDetail.putExtra("id", //
				 * ProjectDetailActivity.projectId); //
				 * PageDetail.putExtra("title", "title"); //
				 * getActivity().startActivityForResult(PageDetail, //
				 * ByUtils.REQUEST_PROJECT_DETAIL); String URL = "http://" +
				 * ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				 * + ProjectDetailActivity.projectId;
				 * ByUtils.sendContact(getActivity(), "" +
				 * ProjectDetailActivity.projectId,
				 * ProjectDetailActivity.projectTitle, URL,
				 * getActivity().findViewById(R.id.mFrament)); } } }.show();
				 */

				showController(20);
				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(getActivity());
				// builder.setAdapter(adapter, new
				// DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int item) {
				// if (item == 0) {
				// // share face
				// String URL =
				// "http://www.facebook.com/sharer/sharer.php?u=http://" +
				// ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				// + ProjectDetailActivity.projectId;
				// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse(URL));
				// getActivity().startActivityForResult(browserIntent,
				// ByUtils.REQUEST_PROJECT_DETAIL);
				// } else if (item == 1) {
				// // share twitter
				// //
				// https://twitter.com/share?url=http://bynal.acvdev.com/projects/detail/"
				// // + pid ;
				// String title = ProjectDetailActivity.projectTitle;
				// String fotmat = getString(R.string.share_twitter_title);
				// String str = String.format(fotmat, title, "");
				// String URL = "https://twitter.com/share?url=http://" +
				// ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				// + ProjectDetailActivity.projectId + "&text=" + str;
				// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse(URL));
				// getActivity().startActivityForResult(browserIntent,
				// ByUtils.REQUEST_PROJECT_DETAIL);
				// } else if (item == 2) {
				// // share bookmark
				// //
				// http://b.hatena.ne.jp/entry/bynal.acvdev.com/projects/detail/"
				// // + pid;
				// String URL = "http://b.hatena.ne.jp/entry/" +
				// ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				// + ProjectDetailActivity.projectId;
				// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse(URL));
				// getActivity().startActivityForResult(browserIntent,
				// ByUtils.REQUEST_PROJECT_DETAIL);
				// } else if (item == 3) {
				// // share contact
				// // Intent PageDetail = new Intent(getActivity(),
				// // SendContactActivity.class);
				// // PageDetail.putExtra("id",
				// // ProjectDetailActivity.projectId);
				// // PageDetail.putExtra("title", "title");
				// // getActivity().startActivityForResult(PageDetail,
				// // ByUtils.REQUEST_PROJECT_DETAIL);
				// String URL = "http://" +
				// ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				// + ProjectDetailActivity.projectId;
				// ByUtils.sendContact(getActivity(), "" +
				// ProjectDetailActivity.projectId,
				// ProjectDetailActivity.projectTitle, URL);
				// }
				//
				// }
				// });
				//
				// final AlertDialog dialog = builder.create();
				// dialog.show();
			}

			break;
		case R.id.project_detail_money:
			// ((BaseActivity) getActivity()).changeFragemt(new
			// ProjectDetailSupportFragment());
			if (showDialogshare) {
				viewdialog.startAnimation(fadeIn2);
			} else {
				new ProjectDetailSupportDialog(getActivity(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
			}
			break;
		case R.id.project_detail_setlike:
			if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
				if (showDialogshare) {
					viewdialog.startAnimation(fadeIn2);
				} else {
					AccountDB accountDB = new AccountDB(getActivity());
					if (!set_staus_favorite.equalsIgnoreCase("") && accountDB.getToken() != null) {
						sendData = new HashMap<String, String>();
						sendData.put("id", Integer.toString(ProjectDetailActivity.projectId));
						sendData.put("token", token);
						sendData.put("value", set_staus_favorite);
						new APICaller(getActivity()).callApi("/project/setFavorite", true, callbackAPIFavorite, sendData);
					} else {
						// login
						CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_message_pleaselogin), null);
					}
				}
			}
			break;
		case R.id.project_detail_calendar:
			if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("0")) {
				if (showDialogshare) {
					viewdialog.startAnimation(fadeIn2);
				} else {
					try {
						if (ProjectDetailActivity.projectTotalDate > 0) {

							Log.e("date", time_start_temp + " : " + time_end_temp);
							Calendar cal_start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							Calendar cal_end = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// HHmmss
							sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							cal_start.setTime(sdf.parse(time_start_temp));
							cal_end.setTime(sdf.parse(time_end_temp));

							long time_start = cal_start.getTimeInMillis();
							long time_end = cal_end.getTimeInMillis();

							// CommonAndroid.showDialog(getActivity(),
							// ":time_start:" +
							// time_start + ":time_end:" + time_end, null);

							if (urlDetailproject == null) {
								urlDetailproject = "";
							}
							String description = String.format("%s\n%s", ProjectDetailActivity.projectDesc, urlDetailproject);

							ByUtils.addCalendar(getActivity(), time_start, time_end, ProjectDetailActivity.projectTitle, description);
						} else {
							CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.project_detail_support_err_projectended), null);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
		case R.id.share_block:
			if (showDialogshare) {
				viewdialog.startAnimation(fadeIn2);
			}
			break;
		default:

			break;
		}
	}

	private void updataDetail(String project_image, String project_movie, String div_block) {
		try {
			// progressDialog = ProgressDialog.show(getActivity(), "",
			// "Loading...", true);
			// progressDialog.dismiss();
			div_block = div_block.replaceAll("<br />", "<br/>");
			div_block = div_block.replaceAll("<br/>", "");
			div_block = div_block.replaceAll("<p>&nbsp;</p>", "");
			div_block = div_block.replaceAll("<p>　</p>", "");
			div_block = div_block.replaceAll("<p>  </p>", "");
			div_block = div_block.replaceAll("<p>&#12288;</p>", "");
			div_block = div_block.replaceAll("&nbsp;", "");
			div_block = div_block.replaceAll("\r\n", "\n");
			div_block = div_block.replaceAll("margin-left:", "margin-top:");
			div_block = div_block.replaceAll("margin-right:", "margin-bottom:");

			for (int i = 0; i < 10; i++) {
				div_block = div_block.replaceAll("\n\n", "\n");
			}
			div_block = div_block.replaceAll("\n</p>\n<p>", "</p>\n<p>");
			div_block = div_block.replaceAll("</p>\n<p>\n", "</p>\n<p>");
			div_block = div_block.replaceAll("\n<h1>\n", "<h1>\n");
			div_block = div_block.replaceAll("\n", "<br />");

			div_block = div_block.replaceAll("<img", "<img style=\"background:#ffffff url('./ajax_loader.gif') no-repeat center center;width:100%;\" ");
			// CommonAndroid.showDialog(getActivity(), div_block , null);
			String str = "";
			str += loadResouceHTMLFromAssets("project_detail_header.html");
			if (!project_movie.equalsIgnoreCase("")) {
				str += "<div  style=\"background:#ffffff url('./ajax_loader.gif') no-repeat center center;width:100%;\" >" + project_movie + "</div><br /><div style=\"margin-top:15px;\"></div>";
			} else if (!project_image.equalsIgnoreCase("")) {
				// getView().findViewById(R.id.project_detail_image).setVisibility(View.VISIBLE);
				// ImageLoaderUtils.getInstance(getActivity()).DisplayImage(project_image,
				// (ImageView)
				// getView().findViewById(R.id.project_detail_image));
				str += "<div  style=\"background:#ffffff url('./ajax_loader.gif') no-repeat center center;width:100%;\" ><img src=\"" + project_image + "\" width=\"100%\" ></div>";
				str += "<br /><div style=\"margin-top:15px;\"></div>";
			}
			str += div_block;
			str += loadResouceHTMLFromAssets("project_detail_footer.html");

			Log.e("TIME", System.currentTimeMillis() + "");
			credit.loadDataWithBaseURL("file:///android_asset/", str, "text/html", "UTF-8", null);
			// credit.loadData(str, "text/html",null);

			// webView.loadUrl("file:///android_asset/test.html");
			credit.setWebChromeClient(new WebChromeClient() {
			});

			credit.setLongClickable(false);
			credit.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return true;
				}
			});
			/**
			 * change code
			 */

			try {
				header.startAnimation(fadeOut);
				progressDialog.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				progressDialog.dismiss();
				e.printStackTrace();
			}

			credit.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return false;
				}

				public void onPageFinished(WebView view, String url) {
					credit.stopLoading();
					Log.e("TIME1", "2 : " + System.currentTimeMillis() + "");
					return;
					// credit.refreshDrawableState();
					// Toast.makeText(context, "Page Load Finished",
					// Toast.LENGTH_SHORT).show();
				}

			});
			int secondsDelayed = 60;
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Log.e("TIME::", System.currentTimeMillis() + "");
					credit.stopLoading();
				}
			}, secondsDelayed * 1000);
		} catch (Exception e) {
			Log.e("TIME3", System.currentTimeMillis() + "");
			// credit.stopLoading();
			e.printStackTrace();
			progressDialog.dismiss();
		} catch (OutOfMemoryError e) {
			Log.e("TIME4", System.currentTimeMillis() + "");
			progressDialog.dismiss();
		}
	}

	public String loadResouceHTMLFromAssets(String filename) {
		String tmp = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(filename)));
			String word;
			while ((word = br.readLine()) != null) {
				if (!word.equalsIgnoreCase("")) {
					tmp += word;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // stop reading
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tmp;
	}

	@Override
	public HeaderOption getHeaderOption() {
		TYPEHEADER type = TYPEHEADER.NORMAL;
		HeaderOption headerOption = new HeaderOption(getActivity(), type) {
			@Override
			public void onClickButtonLeft() {
				credit.loadUrl("about:blank");
				ProjectDetailActivity.projectIdDetailPreview = "0";
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
			}

			@Override
			public void onClickButtonRight() {
				((BaseActivity) getActivity()).showMenuLeft(true);
			}
			
			@Override
			public void onClickButtonRight2() {
				gotoHome();
			}
		};
		if ((ProjectDetailActivity.projectIdDetailPreview).equalsIgnoreCase("1")) {
			headerOption.setShowButtonLeft(true);
			headerOption.setResHeader(R.string.header_project_preview);
			headerOption.setResDrawableLeft(R.drawable.back_xml);
		} else {
			headerOption.setShowButtonLeft(true);
			headerOption.setShowButtonRight(true);
			headerOption.setResHeader(R.string.header_project_detail);
			headerOption.setResDrawableLeft(R.drawable.back_xml);
			headerOption.setResDrawableRight(R.drawable.menuright_xml);
		}
		headerOption.setShowButtonRight2(true);
		headerOption.setResDrawableRight2(R.drawable.home_xml);
		return headerOption;
	}

	@Override
	public void onResume() {
		super.onResume();
		getView().findViewById(R.id.share_block).setVisibility(View.GONE);
	}

	public void showController(int top) {
		// if (top < 0)
		// top = 0;
		//
		// int height = (int) (25 * VNPResize.getInstance().getScale());
		// top = top + height;

		viewdialog.setVisibility(View.VISIBLE);
		// BynalAnimationUtils.faceoutView(getActivity().getBaseContext(),viewdialog,
		// null);
		viewdialog.startAnimation(fadeOut2);

		// View view = getView().findViewById(R.id.share_block_content);
		// RelativeLayout.LayoutParams layoutParams =
		// (RelativeLayout.LayoutParams) view.getLayoutParams();
		//
		// layoutParams.setMargins((int) (5 *
		// VNPResize.getInstance().getScale()), top, 0, 0);
		//
		// view.setLayoutParams(layoutParams);

		getView().findViewById(R.id.share001).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewdialog.startAnimation(fadeIn2);
				// share face
				String URL = "http://www.facebook.com/sharer/sharer.php?u=http://" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId;
				String URL_app = "http://" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				// getActivity().startActivityForResult(browserIntent,
				// ByUtils.REQUEST_PROJECT_DETAIL);
				boolean check_share_app = false;
				try {
					Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, URL_app);
					PackageManager pm = v.getContext().getPackageManager();
					List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						if ((app.activityInfo.name).contains("facebook")) {
							final ActivityInfo activity = app.activityInfo;
							final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
							shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							shareIntent.setComponent(name);
							v.getContext().startActivity(shareIntent);
							check_share_app = true;
							break;
						}
					}
					if (!check_share_app) {
						getActivity().startActivityForResult(browserIntent, ByUtils.REQUEST_PROJECT_DETAIL);
					}
				} catch (Exception e) {
					getActivity().startActivityForResult(browserIntent, ByUtils.REQUEST_PROJECT_DETAIL);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		getView().findViewById(R.id.share002).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewdialog.startAnimation(fadeIn2);
				// share twitter
				// https://twitter.com/share?url=http://bynal.acvdev.com/projects/detail/"
				// + pid ;
				String title = ProjectDetailActivity.projectTitle;
				String fotmat = getString(R.string.share_twitter_title);
				String str = String.format(fotmat, title, "");
				String URL = "https://twitter.com/share?url=http://" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId + "&text=" + Uri.encode(str);
				String URL_app = "http://" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId + " \n" + str;
				// Log.e("url", "url==" + URL);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				// getActivity().startActivityForResult(browserIntent,
				// ByUtils.REQUEST_PROJECT_DETAIL);

				boolean check_share_app = false;
				try {
					Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, URL_app);
					PackageManager pm = v.getContext().getPackageManager();
					List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
							final ActivityInfo activity = app.activityInfo;
							final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
							shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							shareIntent.setComponent(name);
							v.getContext().startActivity(shareIntent);
							check_share_app = true;
							break;
						}
					}
					if (!check_share_app) {
						getActivity().startActivityForResult(browserIntent, ByUtils.REQUEST_PROJECT_DETAIL);
					}
				} catch (Exception e) {
					getActivity().startActivityForResult(browserIntent, ByUtils.REQUEST_PROJECT_DETAIL);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		getView().findViewById(R.id.share003).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewdialog.startAnimation(fadeIn2);
				// share bookmark
				// http://b.hatena.ne.jp/entry/bynal.acvdev.com/projects/detail/"
				// + pid;
				String URL = "http://b.hatena.ne.jp/entry/" + ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/" + ProjectDetailActivity.projectId;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				getActivity().startActivityForResult(browserIntent, ByUtils.REQUEST_PROJECT_DETAIL);
			}

		});

		getView().findViewById(R.id.share004).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogshare_gone = true;
				viewdialog.startAnimation(fadeIn2);
				// share contact
				// Intent PageDetail = new Intent(getActivity(),
				// SendContactActivity.class);
				// PageDetail.putExtra("id",
				// ProjectDetailActivity.projectId);
				// PageDetail.putExtra("title", "title");
				// getActivity().startActivityForResult(PageDetail,
				// ByUtils.REQUEST_PROJECT_DETAIL);
				// String URL = "http://" +
				// ByUtils.BASEDOMAINSERVER_SHARE_PROJECT + "/projects/detail/"
				// + ProjectDetailActivity.projectId;
				// ByUtils.sendContact(getActivity(), "" +
				// ProjectDetailActivity.projectId,
				// ProjectDetailActivity.projectTitle, URL,
				// getActivity().findViewById(R.id.mFrament));
			}

		});
	}
}