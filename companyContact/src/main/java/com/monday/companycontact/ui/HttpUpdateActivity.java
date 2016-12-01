package com.monday.companycontact.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import com.monday.companycontact.R;

/**
 * http上传页面，显示本机地址以及开放端口
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_http)
public class HttpUpdateActivity extends BaseActivity{

	@AfterViews
	void start(){
		getSupportActionBar().setTitle("通过Http上传");
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
}
