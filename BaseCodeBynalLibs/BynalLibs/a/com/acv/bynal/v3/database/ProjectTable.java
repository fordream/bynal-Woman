package a.com.acv.bynal.v3.database;

public class ProjectTable extends Table {

	public ProjectTable() {
		addColumns("id");
		addColumns("user_img");
		addColumns("img");
		addColumns("title");
		addColumns("desc");
		addColumns("total_money_support");
		addColumns("supported");
		addColumns("total_date");
		addColumns("percent");
		addColumns("person");

		// home_project_new
		// home_project_hot
		addColumns("project_type");
	}


}