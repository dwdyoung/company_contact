package com.monday.companycontact.ui;

import android.support.v7.app.ActionBar;

import com.monday.companycontact.MyPrefs_;
import com.monday.companycontact.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity{

	@Pref
	MyPrefs_ myPrefs;
	
	@AfterViews
	void init(){
		
		ActionBar actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.hide();  
		
		if(myPrefs.isFirst1().get()){
			IntroduceActivity_.intent(this).start();
			myPrefs.isFirst1().put(false);
		} else {
			MainActivity_.intent(this).start();
			finish();
		}
	}
	
}
