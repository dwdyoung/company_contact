package com.monday.companycontact;

import android.app.Application;

import com.monday.companycontact.controller.GroupController;
import com.monday.companycontact.db.ContractGroup;
import com.monday.companycontact.service.PhoneLisntenService_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

@EApplication
public class AppContext extends Application{
 
	public static final boolean DEBUG = true;
	
	@Bean
	GroupController groupController;
	
	@AfterInject
	void init(){
		CrashHandler.getInstance().init(this);
		
		// 创建至少一个group
		if(groupController.getGroupList().size() == 0){
			groupController.addGroup(new ContractGroup(1L, "常用"));
		}
		
		PhoneLisntenService_.intent(this).start();
	}
}
