package a.com.acv.bynal.v3.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOperations {

	public static final String BDUPDATE = "BDUPDATE";
	// Database fields
	private DataBaseWrapper dbHelper;

	private SQLiteDatabase database;
	private Context mContext;

	public DBOperations(Context context) {
		dbHelper = new DataBaseWrapper(context);
		this.mContext = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public int updateProjectList(List<ProjectTable> lProject) {
		if (lProject.size() == 0) {
			return -1;
		}
		String project_type = lProject.get(0).getData("project_type");

		open();
		if (project_type.equals("home_project_new")
				|| project_type.equals("home_project_hot")) {
			String where = String.format("project_type = '%s'", project_type);
			database.delete(new ProjectTable().getTableName(), where, null);
		}

		for (ProjectTable projectTable : lProject) {
			ContentValues values = new ContentValues();
			Set<String> keys = projectTable.columNameS();
			for (String key : keys) {
				values.put(key, projectTable.getData(key));
			}

			database.insert(projectTable.getTableName(), null, values);
		}

		close();

		Intent intent = new Intent(BDUPDATE);
		intent.putExtra("table", new ProjectTable().getTableName());
		intent.putExtra("project_type", project_type);
		mContext.sendBroadcast(intent);

		return 1;
	}

	public List<ProjectTable> getListProjectList(String project_type) {
		List<ProjectTable> list = new ArrayList<ProjectTable>();

		open();

		String selection = String.format("project_type = '%s'", project_type);
		Cursor cursor = database.query(new ProjectTable().getTableName(), null,
				selection, null, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				ProjectTable projectTable = new ProjectTable();
				Set<String> keys = projectTable.columNameS();

				for (String key : keys) {
					projectTable.setData(key,
							cursor.getString(cursor.getColumnIndex(key)));
				}

				list.add(projectTable);
			}
		}

		close();
		return list;
	}

	private class DataBaseWrapper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "Students.db";
		private static final int DATABASE_VERSION = 1;
		private List<Table> list = new ArrayList<Table>();

		public DataBaseWrapper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			list.add(new ProjectTable());
			list.add(new MessageRoomTable());
			list.add(new MessageTable());
			list.add(new NewAndEventTable());
			list.add(new UserTable());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (Table table : list) {
				db.execSQL(table.createString());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion > oldVersion) {
				for (Table table : list) {
					db.execSQL(table.deleteString());
				}
			}

			onCreate(db);
		}
	}

	public List<Table> query(Table table, String selection) {
		open();
		List<Table> list = new ArrayList<Table>();
		Cursor cursor = database.query(table.getTableName(), null, selection,
				null, null, null, null);
		while (cursor != null && cursor.moveToNext()) {
			Table mTable = new Table();
			mTable.setTableName(table.getTableName());
			Set<String> keys = table.columNameS();
			for (String key : keys) {
				mTable.addColumns(key);
				mTable.setData(key,
						cursor.getString(cursor.getColumnIndex(key)));

			}

			list.add(mTable);
		}
		close();
		return list;
	}

	public void insert(Table table) {
		open();

		ContentValues values = new ContentValues();
		Set<String> sets = table.columNameS();
		for (String set : sets) {
			values.put(set, table.getData(set));
		}

		long id = database.insert(table.getTableName(), null, values);

		//Log.e("TABG", String.format("insert %s %s", table.getTableName(), id));
		close();
	}

	public void update(Table table) {

	}

	public void delete(Table table) {
		open();
		database.delete(table.getTableName(), null, null);
		close();
	}
}