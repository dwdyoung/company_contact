package com.monday.companycontact.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import android.support.v7.app.ActionBar;

import com.monday.companycontact.R;

/**
 * 使用介绍页面
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_introduce)
public class IntroduceActivity extends BaseActivity{

	@AfterViews
	void start(){
		ActionBar actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
		actionBar.setHideOnContentScrollEnabled(true);
		actionBar.setHomeButtonEnabled(true);  
		actionBar.setDisplayHomeAsUpEnabled(true);  
		getSupportActionBar().setTitle("使用方法介绍");
	}
	
	@Click(R.id.introduc_jump)
	void onJump(){
		MainActivity_.intent(this).start();
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		MainActivity_.intent(this).start();
		finish();
	}
}
