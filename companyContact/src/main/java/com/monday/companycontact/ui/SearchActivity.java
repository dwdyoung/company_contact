package com.monday.companycontact.ui;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.monday.companycontact.R;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.ui.adapter.ProviderAdapter;
import com.monday.companycontact.ui.adapter.ProviderIndexableAdapter.OnItemBtClickListener;
import com.monday.companycontact.utils.PingYinUtil;
import com.monday.companycontact.utils.StringUtils;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity{

	@Bean
	ProviderController providerController;
	
	@ViewById(R.id.search_search)
	SearchView searchView;
	
	@ViewById(R.id.search_listview)
	ListView listView;
	
	List<Provider> providerList;
	
	ProviderAdapter adapter;
	
	@AfterViews
	void init(){
		
		ActionBar actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
//		actionBar.setHideOnContentScrollEnabled(true);
		actionBar.setHomeButtonEnabled(true);  
		actionBar.setDisplayHomeAsUpEnabled(true);  
		
		providerList = new ArrayList<Provider>();
		
		adapter = new ProviderAdapter(providerList, this);
		listView.setAdapter(adapter);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String text) {
				
				providerList.clear();
				
				if(!StringUtils.isPinyin(text)){
					text = PingYinUtil.getPingYin(text);
				}
				
				if(StringUtils.isNum(text)){
					providerList.addAll(providerController.queryPhoneOrNum(text, text));
				} else {
					providerList.addAll(providerController.queryPinyin(text, text));
				}
				
				Log.v("TEST", providerList.size() + "");
				
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		
		
		adapter.setOnItemBtClickListener(new OnItemBtClickListener() {
			@Override
			public void onClick(int position, View view) {
			}
		});
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
	@ItemClick(R.id.search_listview)
	void onItemClick(int position){
		ProviderDetailActivity_.intent(this).provider(providerList.get(position)).start();
	}
	
}
