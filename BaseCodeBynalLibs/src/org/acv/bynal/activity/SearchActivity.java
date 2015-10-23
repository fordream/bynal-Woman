package org.acv.bynal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.fragment.SearchFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import app.bynal.woman.R;

import com.acv.libs.base.menu.MenuItem;
import com.acv.libs.nbase.BaseActivity;

public class SearchActivity extends BaseActivity {
	public static String TypeSearch = "1";
	public static String ValueSearch = "1";
	public static String ValueSearchHeader = "";
	SearchFragment project_search;
	public static List<String> listhot = new ArrayList<String>();
	public static List<String> listcat = new ArrayList<String>();
	public static List<String> listtag = new ArrayList<String>();
	public static List<String> listhot_v = new ArrayList<String>();
	public static List<String> listcat_v = new ArrayList<String>();
	public static List<String> listtag_v = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configMenuRight();
		project_search = new SearchFragment();
		changeFragemt(project_search);
		openFragmentListener( findViewById(R.id.mFrament), project_search.getAnimationAction());
	}

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	static List<String> listDataHeader;
	static HashMap<String, List<String>> listDataChild;
	public int menu1 = 0;
	public int menu2 = 0;
	public int menu3 = 0;

	@Override
	public void configMenuleft() {
		// addmenu(new MenuItem("cate 1", 0, false));
		findViewById(R.id.menuslide_header).setVisibility(View.GONE);
		findViewById(R.id.menu_main_content).setBackgroundResource(R.drawable.menuright_detail_shadow);
		addmenu(new MenuItem(getResources().getString(R.string.menu_search_hot), R.drawable.menu_search_hot , true));
		if(menu1 == 1){
			for(int i = 0; i < listhot.size(); i++){
				addmenu(new MenuItem(listhot.get(i), R.drawable.menu_search_hotsub, false));
			}
		}
		addmenu(new MenuItem(getResources().getString(R.string.menu_search_cat), R.drawable.menu_search_cat , true));
		if(menu2 == 1){
			for(int i = 0; i < listcat.size(); i++){
				addmenu(new MenuItem(listcat.get(i), R.drawable.menu_search_catsub, false));
			}
		}
		addmenu(new MenuItem(getResources().getString(R.string.menu_search_tag), R.drawable.menu_search_tag , true));
		if(menu3 == 1){
			for(int i = 0; i < listtag.size(); i++){
				addmenu(new MenuItem(listtag.get(i), R.drawable.menu_search_tagsub, false));
			}
		}

	}

	@Override
	public void refresh() {
		super.refresh();
		 clearMenu();
		 configMenuleft();
		 refreshMenu();
		 showMenuSubRight(true);
		// changeFragemt(new HomeFragment());
	}
	
	@Override
	public void onResume(){
		super.onResume();
		TypeSearch = "1";
		ValueSearch = "1";
		ValueSearchHeader = "";
	}

	@Override
	public void onItemClick(int position, MenuItem item) {
		super.onItemClick(position, item);
//		Toast.makeText(this, item.getName() + "item", Toast.LENGTH_SHORT).show();
		String item_click = item.getName(); 
		boolean clicksub = false;
		int position_array = 0;
		if(menu1 == 1){
			if(position != 0 && position != (listhot.size() + 1) && position !=  (listhot.size() + 2)){
				clicksub = true;
				TypeSearch = "1";
				position_array = position - 1;
				if(listhot_v.get(position_array) != null){
					ValueSearch = listhot_v.get(position_array);
					ValueSearchHeader = listhot.get(position_array);
				}
//				Toast.makeText(this, ValueSearch + ":value", Toast.LENGTH_SHORT).show();
				
			}
		}else if(menu2 == 1){
			if(position != 0 && position != 1 && position !=  (listcat.size() + 2)){
				clicksub = true;
				TypeSearch = "2";
				position_array = position - 2;
				if(listcat_v.get(position_array) != null){
					ValueSearch = listcat_v.get(position_array);
					ValueSearchHeader = listcat.get(position_array);
				}
//				Toast.makeText(this, ValueSearch + ":value", Toast.LENGTH_SHORT).show();
			}
		}else if(menu3 == 1){
			if(position != 0 && position != 1 && position !=  2){
				clicksub = true;
				TypeSearch = "3";
				position_array = position - 3;
				if(listtag_v.get(position_array) != null){
					ValueSearch = listtag_v.get(position_array);
					ValueSearchHeader = listtag.get(position_array);
				}
//				Toast.makeText(this, ValueSearch + ":value", Toast.LENGTH_SHORT).show();
			}
		}
		if(!clicksub){
			if( item_click.equalsIgnoreCase(getResources().getString(R.string.menu_search_hot))){
				TypeSearch = "1";
				if(menu1 == 0){
					menu1 = 1;
				}else{
					menu1 = 0;
				}
				menu2 = 0;
				menu3 = 0;
			}else if(item_click.equalsIgnoreCase(getResources().getString(R.string.menu_search_cat))){
				TypeSearch = "2";
				menu1 = 0;
				if(menu2 == 0){
					menu2 = 1;
				}else{
					menu2 = 0;
				}
				menu3 = 0;
			}else if(item_click.equalsIgnoreCase(getResources().getString(R.string.menu_search_tag))){
				TypeSearch = "3";
				menu1 = 0;
				menu2 = 0;
				if(menu3 == 0){
					menu3 = 1;
				}else{
					menu3 = 0;
				}
			}
			clearMenu();
			configMenuleft();
			refreshMenu();
			showMenuSubRight(true);
		}else{
			showMenuSubRight(false);
			project_search.CallAPISearch();
		}
		
	}

}
