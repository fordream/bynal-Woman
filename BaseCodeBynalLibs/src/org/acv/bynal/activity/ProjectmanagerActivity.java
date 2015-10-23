package org.acv.bynal.activity;

import org.acv.bynal.camera.VideoActivity;
import org.acv.bynal.camera.uploadyoutube.VideoUtil;
//import org.acv.bynal.fragment.ProjectManagerDeliveryFragment;
import org.acv.bynal.fragment.ProjectManagerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.nbase.BaseActivity;

public class ProjectmanagerActivity extends BaseActivity {
	public static ProjectManagerFragment projectManagerPage;
	// public static ProjectManagerDeliveryFragment
	// projectManagerDeliveryFragment;
	public static String typeProject = "0";
	public static String project_id = "0";
	public static final int CAPTURE_RETURN = 1;
	public static final int GALLERY_RETURN = 2;
	public static final int SUBMIT_RETURN = 3;
	private String ytdDomain = null;
	private String assignmentId = null;
	private TextView domainHeader = null;
	public static final String YTD_DOMAIN = "ytd_domain";
	public static final String ASSIGNMENT_ID = "assignment_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getString("projectIdDelivery") != null) {
				project_id = extras.getString("projectIdDelivery");
				/*
				 * projectManagerDeliveryFragment = new
				 * ProjectManagerDeliveryFragment();
				 * changeFragemt(projectManagerDeliveryFragment);
				 * openFragmentListener(findViewById(R.id.mFrament),
				 * projectManagerDeliveryFragment.getAnimationAction());
				 */
			}
		} else {
			projectManagerPage = new ProjectManagerFragment();
			changeFragemt(projectManagerPage);
			openFragmentListener(findViewById(R.id.mFrament), projectManagerPage.getAnimationAction());
		}

		this.ytdDomain = this.getString(R.string.default_ytd_domain);
		configMenuRight();
		this.showMenuRight(false);
	}

	@Override
	public void configMenuleft() {
		findViewById(R.id.menuslide_header).setVisibility(View.GONE);
		findViewById(R.id.menu_main_content).setBackgroundResource(R.drawable.bg_menuright);
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_favorite), R.drawable.pm_menu_star, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_support), R.drawable.pm_menu_circle, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_post), R.drawable.pm_menu_arrow, true));
		addmenu(new MenuItem(getResources().getString(R.string.menu_project_all), R.drawable.pm_menu_arrow, true));
	}

	@Override
	public void onItemClick(int position, MenuItem item) {
		super.onItemClick(position, item);
		this.showMenuRight(false);
		if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_favorite))) {
			typeProject = "0";
			projectManagerPage.CallAPIProjectList();
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_support))) {
			typeProject = "1";
			projectManagerPage.CallAPIProjectList();
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_post))) {
			typeProject = "2";
			projectManagerPage.CallAPIProjectList();
		} else if ((item.getName()).equalsIgnoreCase(getResources().getString(R.string.menu_project_all))) {
			typeProject = "3";
			projectManagerPage.CallAPIProjectList();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		try {
			if ((requestCode == CAPTURE_RETURN) || (requestCode == GALLERY_RETURN)) {
				try {
					Log.e("video_data", "data video::" + (data.getData().toString()));
					Intent intent = new Intent(this, VideoActivity.class);
					intent.setData(data.getData());
					intent.putExtra(YTD_DOMAIN, this.ytdDomain);
					if (!VideoUtil.isNullOrEmpty(this.assignmentId)) {
						intent.putExtra(ASSIGNMENT_ID, this.assignmentId);
					}
					startActivityForResult(intent, SUBMIT_RETURN);
				} catch (Exception exception) {

				}
			} else if (requestCode == SUBMIT_RETURN) {
				String data_upload = (VideoUtil.jsonData).toString();
				Log.e("upload", "===========video::" + data_upload);
				// Toast.makeText(this, "upload done!",
				// Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void uploadAvatar(String youtubeID) {
		if (youtubeID != null) {
			projectManagerPage.CallAPIUploadVideo(youtubeID);
		}

	}
}