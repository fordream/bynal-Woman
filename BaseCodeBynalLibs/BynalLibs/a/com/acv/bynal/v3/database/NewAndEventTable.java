package a.com.acv.bynal.v3.database;

public class NewAndEventTable extends Table {

	public NewAndEventTable() {
		addColumns("id");
		addColumns("time");
//		addColumns("title");
		addColumns("message");
		addColumns("new");
		
		// 0 disable
		// 1 active
	}


}