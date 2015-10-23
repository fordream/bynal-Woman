package org.acv.bynal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acv.bynal.activity.ProjectDetailActivity;
import org.acv.bynal.activity.SearchActivity;
import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.views.ProjectSearchItemView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseAdapter;
import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.CommonAndroid;
import com.acv.libs.base.HeaderOption;
import com.acv.libs.base.HeaderOption.TYPEHEADER;
import com.acv.libs.base.callback.APICaller;
import com.acv.libs.base.callback.APICaller.ICallbackAPI;
import com.acv.libs.lazyload.ImageLoader2;
import com.acv.libs.nbase.BaseActivity;
import com.acv.libs.nbase.BaseFragment;

public class SearchFragment extends BaseFragment implements
OnClickListener{
	public static ImageLoader2 imageLoader; 
	private ListView viewPager;
	public static int optionView = 1;
	Map<String, String> sendData = new HashMap<String, String>();
	ICallbackAPI callbackAPI;
	ICallbackAPI callbackAPIMenu;
	ImageView option_1;
	ImageView option_2;
	ImageView imageView2;
	ListView projectListSearchView;
	private int total_pages = 1;
	private int currentPage = 1;
	private boolean isLoadMore = false; 
	List<BaseItem> baseItems = new ArrayList<BaseItem>();
	TextView txtErr;
	@Override
	public void _onAnimationEnd() {
		super._onAnimationEnd();
		imageLoader=new ImageLoader2(getActivity());
		loadPage();
	}
	
	private void loadPage(){
		callbackAPI = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equals("1")) {
						total_pages = jsonObject.getInt("total_pages");
						currentPage = jsonObject.getInt("currentPage");
						if(total_pages == 0){
//							CommonAndroid.showDialog(getActivity(), getResources().getString(
//									R.string.search_return_null) , null);
							txtErr.setVisibility(View.VISIBLE);
							setText(getResources().getString(R.string.search_return_null),
									R.id.pm_err_message);
						}else{
							JSONArray array = jsonObject.getJSONArray("array_data");
							if(total_pages > 1 && total_pages > currentPage){
								isLoadMore = true;
							}
							for (int i = 0; i < array.length(); i++) {
								baseItems.add(new BaseItem(array.getJSONObject(i)));
							}
						}
					}
				} catch (Exception e) {
					CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_data), null);
				}
				updateUi2(optionView);
				((SearchActivity) getActivity()).refresh();
			}

			@Override
			public void onError(String message) {
				((SearchActivity) getActivity()).refresh();
				CommonAndroid.showDialog(getActivity(), getResources().getString(R.string.error_connect_server), null);
			}
		};
		callbackAPIMenu = new ICallbackAPI() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						JSONArray array = jsonObject.getJSONArray("array_data1");
						SearchActivity.listhot.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject11 = (JSONObject) array.get(i);
							String title = jsonObject11.getString("title");
							SearchActivity.listhot.add(title);
							String value = jsonObject11.getString("value");
							SearchActivity.listhot_v.add(value);
						}
						JSONArray array2 = jsonObject.getJSONArray("array_data2");
						SearchActivity.listcat.clear();
						for (int i = 0; i < array2.length(); i++) {
							JSONObject jsonObject12 = (JSONObject) array2.get(i);
							String title = jsonObject12.getString("title");
							SearchActivity.listcat.add(title);
							String value = jsonObject12.getString("value");
							SearchActivity.listcat_v.add(value);
						}
						JSONArray array3 = jsonObject.getJSONArray("array_data3");
						SearchActivity.listtag.clear();
						for (int i = 0; i < array3.length(); i++) {
							JSONObject jsonObject13 = (JSONObject) array3.get(i);
							String title = jsonObject13.getString("title");
							SearchActivity.listtag.add(title);
							String value = jsonObject13.getString("value");
							SearchActivity.listtag_v.add(value);
						}
						((SearchActivity) getActivity()).refresh();
					}
				} catch (Exception e) {
					Log.e("err menu search", "err==" +e.getMessage());
				}
			}

			@Override
			public void onError(String message) {
			}
		};
		CallAPISearch();
		CallAPIMenu();
	}
	Animation animation;
	@Override
	public void setUpFragment(View view) {
		optionView = 1;
		txtErr = (TextView) view.findViewById(R.id.pm_err_message);
		projectListSearchView = (ListView) view.findViewById(R.id.listview);
		view.findViewById(R.id.v3home_pager_have_left).setOnClickListener(this);
		view.findViewById(R.id.imageView1_2).setOnClickListener(this);
		view.findViewById(R.id.imageView2).setOnClickListener(this);
		option_1 = (ImageView) view.findViewById(R.id.v3home_pager_have_left);
		option_2 = (ImageView) view.findViewById(R.id.imageView1_2);
		imageView2 = (ImageView) view.findViewById(R.id.imageView2);
		
//		VNPResize vnpResize = VNPResize.getInstance();
//		vnpResize.resizeSacle(view.findViewById(R.id.txtSearch), 200, 50);
//		vnpResize.resizeSacle(view.findViewById(R.id.imageView2), 60, 50);
		
		//TODO: action search key from keyboard
		EditText editText= (EditText) view.findViewById(R.id.txtSearch);
		editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (/*(event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE) || */(actionId == EditorInfo.IME_ACTION_SEARCH)  ) {
                    //Do your action
					String keySearch = getTextStr(R.id.txtSearch);
					SearchActivity.ValueSearchHeader = getActivity().getResources().getString(R.string.header_search_key);
					if (keySearch.equals("")) {
//						CommonAndroid.showDialog(getActivity(), "Please inout key search", null);
						SearchActivity.TypeSearch = "1";
						SearchActivity.ValueSearch = "1";
					} else {
						SearchActivity.TypeSearch = "4";
						SearchActivity.ValueSearch = keySearch;
					}
					InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		            keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
					CallAPISearch();
                }
				return false;
			}
        });
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String keySearch = getTextStr(R.id.txtSearch);
				if (!keySearch.equals("")) {
					imageView2.setVisibility(View.VISIBLE);
				}else{
					imageView2.setVisibility(View.GONE);
				}
			}
		});
		
		
		projectListSearchView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaseItem baseItem = (BaseItem) parent
						.getItemAtPosition(position);
				String projectID = baseItem.getString("id");
				Intent PageDetail = new Intent(getActivity(), ProjectDetailActivity.class);
//	        	PageDetail.putExtra("projectIdDetail", projectID);
	        	PageDetail.putExtra("projectIdDetail", Integer.parseInt(projectID));
	        	getActivity().startActivity(PageDetail); 
			}
		});
		projectListSearchView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(totalItemCount > 0 && isLoadMore){
					if(totalItemCount - 1 <= firstVisibleItem + visibleItemCount){
//		    			CommonAndroid.showDialog(getActivity(), "load more:" + (currentPage + 1), null);
		    			sendData = new HashMap<String, String>();
		    			sendData.put("type", SearchActivity.TypeSearch);
		    			sendData.put("value", SearchActivity.ValueSearch);
		    			sendData.put("page", "" +(currentPage + 1));
		    			new APICaller(getActivity()).callApi("/project/listByFilter", true,
		    					callbackAPI, sendData);
		    			isLoadMore = false;
			    	}
				}
		    	
			}
			
		});
		
		//TODO:test
		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.test_slide_right);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Called when the Animation starts
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Called when the Animation ended
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// This is called each time the Animation repeats
			}
		});
	}
	
	private BaseAdapter baseAdapter;
	
	public void CallAPISearch(){
		int txtHeader = 0;
		int type = Integer.parseInt(SearchActivity.TypeSearch);
		switch (type) {
			case 1:
				txtHeader = R.string.header_search_hot;
				break;
			case 2:
				txtHeader = R.string.header_search_cat;
				break;
			case 3:
				txtHeader = R.string.header_search_tag;
				break;
			case 4:
				txtHeader = R.string.header_search_key;
				break;
		}
//		((BaseActivity)getActivity()).setTextheader(txtHeader);
		if(!(SearchActivity.ValueSearchHeader).equalsIgnoreCase("")){
			((BaseActivity)getActivity()).setTextheader(SearchActivity.ValueSearchHeader);
		}else{
			((BaseActivity)getActivity()).setTextheader(R.string.header_search_default);
		}
		baseItems.clear();
		total_pages = 1;
		currentPage = 1;
		setText("",R.id.pm_err_message);
		txtErr.setVisibility(View.GONE);
		sendData = new HashMap<String, String>();
		sendData.put("type", SearchActivity.TypeSearch);
		sendData.put("value", SearchActivity.ValueSearch);
		new APICaller(getActivity()).callApi("/project/listByFilter", true,
				callbackAPI, sendData);
		
	}
	
	public void CallAPIMenu(){
		new APICaller(getActivity()).callApi("/project/listFilter2", false,
				callbackAPIMenu, sendData);
	}

	@Override
	public int layoytResurce() {
		return R.layout.search_2;
	}
	
	@Override
	public void onClick(View v) {
		int option = v.getId();
		switch (option) {
		case R.id.imageView2:
			/*String keySearch = getTextStr(R.id.txtSearch);
            InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(
            		v.getWindowToken(), 0); 
			if (keySearch.equals("")) {
//				CommonAndroid.showDialog(getActivity(), "Please inout key search", null);
				SearchActivity.TypeSearch = "1";
				SearchActivity.ValueSearch = "1";
			} else {
				SearchActivity.TypeSearch = "4";
				SearchActivity.ValueSearch = keySearch;
			}
			CallAPISearch();*/
			setText("",R.id.txtSearch);
			break;
		case R.id.v3home_pager_have_left:
				if(optionView == 1){
					optionView = 2;
				}else{
					optionView = 1;
				}
				updateUi2(optionView);
				option_1.setVisibility(View.GONE);
				option_2.setVisibility(View.VISIBLE);
			break;
		case R.id.imageView1_2:
				if(optionView == 1){
					optionView = 2;
				}else{
					optionView = 1;
				}
				updateUi2(optionView);
				option_1.setVisibility(View.VISIBLE);
				option_2.setVisibility(View.GONE);
			break;
		}
	}
	
    private void updateUi2(int optionView) {
    	baseAdapter = new BaseAdapter(getActivity(), new ArrayList<Object>()) {
			@Override
			public BaseView getView(Context context, Object data) {
				final ProjectSearchItemView projectSearchItemView = new ProjectSearchItemView(
							context);
				projectSearchItemView.addProjectSearchFragment(SearchFragment.this);
				return projectSearchItemView;
			}
		};
		projectListSearchView.setAdapter(baseAdapter);
		baseAdapter.clear();
		baseAdapter.addAllItems(baseItems);
		baseAdapter.notifyDataSetChanged();
//		BynalAnimationUtils.faceoutView(getActivity().getBaseContext(),projectListSearchView, null);
		
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
		projectListSearchView.startAnimation(animation);
//		projectListSearchView.startAnimation(animation);
	}
    
    @Override
	public HeaderOption getHeaderOption() {
		TYPEHEADER type = TYPEHEADER.NORMAL;
		HeaderOption headerOption = new HeaderOption(getActivity(), type) {
			@Override
			public void onClickButtonLeft() {
				optionView = 1;
				SearchActivity.TypeSearch = "1";
				SearchActivity.ValueSearch = "1";
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
			}

			@Override
			public void onClickButtonRight() {
				((BaseActivity) getActivity()).showMenuLeft(true);
			}
			
			@Override
			public void onClickButtonRight2() {
				optionView = 1;
				SearchActivity.TypeSearch = "1";
				SearchActivity.ValueSearch = "1";
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
			}
		};
		headerOption.setShowButtonLeft(true);
		headerOption.setShowButtonRight(true);
		headerOption.setShowButtonRight2(true);
		headerOption.setResHeader(R.string.header_search_hot);
		headerOption.setResDrawableLeft(R.drawable.back_xml);
		headerOption.setResDrawableRight(R.drawable.menuright_xml);
		headerOption.setResDrawableRight2(R.drawable.home_xml);
		return headerOption;
	}

}