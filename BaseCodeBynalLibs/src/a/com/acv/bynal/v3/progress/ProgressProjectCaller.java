package a.com.acv.bynal.v3.progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import a.com.acv.bynal.v3.database.DBOperations;
import a.com.acv.bynal.v3.database.NewAndEventTable;
import a.com.acv.bynal.v3.database.ProjectTable;
import a.com.acv.bynal.v3.service.V3BynalService.V3BynalServiceACTION;
import android.content.Context;
import android.util.Log;

public class ProgressProjectCaller extends ProgressCaller {
	private String project_type;

	public ProgressProjectCaller(Context context, String message,
			String project_type) {
		super(context, message);
		this.project_type = project_type;
	}

	@Override
	public void onSaveData(String response) {
		List<ProjectTable> lProject = new ArrayList<ProjectTable>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("status").equals("1")) {
				JSONArray array = jsonObject.getJSONArray("array_data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject mData = array.getJSONObject(i);

					ProjectTable projectTable = new ProjectTable();

					Set<String> keys = projectTable.columNameS();
					for (String key : keys) {
						if (mData.has(key)) {
							projectTable.setData(key, mData.getString(key));
						}
					}

					// if (action == V3BynalServiceACTION.HOME_NEW_PROJECT) {
					// projectTable
					// .setData("project_type", "home_project_new");
					// } else if (action ==
					// V3BynalServiceACTION.HOME_HOT_PROJECT) {
					// projectTable
					// .setData("project_type", "home_project_hot");
					// }

					projectTable.setData("project_type", project_type);
					lProject.add(projectTable);
				}
			} else {
			}

			new DBOperations(getmContext()).updateProjectList(lProject);
		} catch (Exception e) {
			Log.e("V3BynalService", "V3BynalService", e);
		}
	}

}
