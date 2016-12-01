package com.monday.companycontact.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;

import com.monday.companycontact.R;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.ui.adapter.ProviderIndexableAdapter;
import com.woozzu.android.widget.IndexableListView;

@EFragment(R.layout.fragment_group)
public class GroupFragment extends Fragment{

	@ViewById(R.id.group_listview)
	IndexableListView listView;
	
	@Bean
	ProviderController providerController;

	ProviderIndexableAdapter providerAdapter;

	@AfterViews
	void init(){
		
	}
	
}
