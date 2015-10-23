package a.com.acv.bynal.v3.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ActionBar.Tab;

public class Table {
	public static final String TEXT_TYPE = " TEXT";
	public static final String COMMA_SEP = ",";
	private Map<String, String> data = new HashMap<String, String>();
	private List<String> coloums = new ArrayList<String>();

	public void addColumns(String key) {
		// data.put(key, null);
		if (!coloums.contains(key))
			coloums.add(key);
	}

	public void setData(String key, String mData) {
		if (coloums.contains(key)) {
			data.remove(key);
			data.put(key, mData);
		}
	}

	public String getData(String key) {
		if (coloums.contains(key)) {
			return data.get(key);
		}

		return null;
	}

	final public Set<String> columNameS() {
		// data.keySet().iterator();
		Set<String> set = new HashSet<String>();
		set.addAll(coloums);
		return set;
	}

	private String tableName = this.getClass().getSimpleName();

	public final String getTableName() {
		return tableName;
	}

	final public void setTableName(String name) {
		tableName = name;
	}

	final public String createString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE ").append(getTableName()).append("(");
		Set<String> keys = columNameS();

		for (String c : keys) {
			if (builder.toString().endsWith("TEXT")) {
				builder.append(",");
			}

			builder.append("	" + c + " TEXT");
		}

		builder.append(");");
		return builder.toString();
	}

	public String deleteString() {
		return "DROP TABLE IF EXISTS " + getTableName();
	}

}
