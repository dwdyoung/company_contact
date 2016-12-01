package com.monday.companycontact.controller;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.database.sqlite.SQLiteDatabase;

import com.monday.companycontact.db.ContractGroup;
import com.monday.companycontact.db.ContractGroupDao;
import com.monday.companycontact.db.DaoMaster;
import com.monday.companycontact.db.DaoSession;

@EBean(scope=Scope.Singleton)
public class GroupController extends BaseController{

	List<ContractGroup> groupList;
	
	DaoMaster daoMaster;
	SQLiteDatabase db;
	DaoSession daoSession;
	ContractGroupDao groupDao;
    
    @Bean
    DBController dbController;
	
	@AfterInject
	void init(){
		groupDao = dbController.getGroupDao();
		groupList = groupDao.queryRaw("");
	}

	public List<ContractGroup> getGroupList() {
		return groupList;
	}
	
	public void removeAll(){
		groupDao.deleteAll();
		groupDao.insert(new ContractGroup(1L, "常用联系人"));
		groupList.clear();
		groupList.addAll(groupDao.queryRaw(""));
	}
	
	/**
	 * 添加一个供应商到数据库
	 * @param provider
	 */
	public void addGroup(ContractGroup group){
		groupDao.insert(group);
		groupList.add(group);
	}
	
}
