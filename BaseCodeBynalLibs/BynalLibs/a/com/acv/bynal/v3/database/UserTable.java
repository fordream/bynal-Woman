package a.com.acv.bynal.v3.database;

public class UserTable extends Table {

	public UserTable() {
		addColumns("id");
		addColumns("user");
		addColumns("password");
		addColumns("name");
		addColumns("token");

		// 0 disable
		// 1 active
		addColumns("status");
	}

}