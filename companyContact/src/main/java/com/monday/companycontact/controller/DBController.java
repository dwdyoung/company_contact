package com.monday.companycontact.controller;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.database.sqlite.SQLiteDatabase;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.db.ContractGroupDao;
import com.monday.companycontact.db.DaoMaster;
import com.monday.companycontact.db.DaoMaster.DevOpenHelper;
import com.monday.companycontact.db.DaoSession;
import com.monday.companycontact.db.ExtraNumDao;
import com.monday.companycontact.db.ProviderDao;

@EBean(scope=Scope.Singleton)
public class DBController extends BaseController{

    @App
    AppContext appContext;
    
    DaoMaster daoMaster;
	SQLiteDatabase db;
	DaoSession daoSession;
	
    ProviderDao providerDao;

    ContractGroupDao groupDao;
    
    ExtraNumDao extraNumDao;
    
	@AfterInject
	void init(){
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(appContext, "provider-db", null);
		db = helper.getWritableDatabase();
	    daoMaster = new DaoMaster(db);
	    daoSession = daoMaster.newSession();
	    providerDao = daoSession.getProviderDao();	
	    groupDao = daoSession.getContractGroupDao();
	    extraNumDao = daoSession.getExtraNumDao();
	}
	
	public ProviderDao getProviderDao() {
		return providerDao;
	}

	public ContractGroupDao getGroupDao() {
		return groupDao;
	}

	public ExtraNumDao getExtraNumDao() {
		return extraNumDao;
	}
	
}
