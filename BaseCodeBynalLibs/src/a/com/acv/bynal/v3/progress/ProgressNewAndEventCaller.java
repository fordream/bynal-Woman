package a.com.acv.bynal.v3.progress;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import a.com.acv.bynal.v3.database.DBOperations;
import a.com.acv.bynal.v3.database.NewAndEventTable;
import android.content.Context;

public class ProgressNewAndEventCaller extends ProgressCaller {

	public ProgressNewAndEventCaller(Context context, String message) {
		super(context, message);
	}

	@Override
	public void onSaveData(String response) {
		try {
			DBOperations dbOperations = new DBOperations(getmContext());

			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("status").equals("1")) {
				dbOperations.delete(new NewAndEventTable());

				JSONArray array = jsonObject.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);

					NewAndEventTable andEventTable = new NewAndEventTable();
					Set<String> keys = andEventTable.columNameS();
					for (String key : keys) {
						if (object.has(key)) {
							andEventTable.setData(key, object.getString(key));
						} else {
							andEventTable.setData(key, "1");
						}
					}

					dbOperations.insert(andEventTable);
				}
			}
		} catch (Exception exception) {
		}
	}

}
