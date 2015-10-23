package a.com.acv.bynal.v3.database;

public class MessageTable extends Table {

	public MessageTable() {
		addColumns("id");
		addColumns("user_login_id");
		addColumns("user_id");
		addColumns("user_name");
		addColumns("date");
		addColumns("message");
	}

}