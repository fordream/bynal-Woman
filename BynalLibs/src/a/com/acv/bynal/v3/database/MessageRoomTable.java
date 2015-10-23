package a.com.acv.bynal.v3.database;

public class MessageRoomTable extends Table {

	public MessageRoomTable() {
		addColumns("id");
		addColumns("user_login_id");
		addColumns("user_name");
		addColumns("date");
		addColumns("message");
		addColumns("block_flg");
		addColumns("spam_flg");
		addColumns("hide_flg");
	}

}