package com.monday.companycontact.controller;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.monday.companycontact.db.ExtraNum;
import com.monday.companycontact.db.ExtraNumDao;
import com.monday.companycontact.db.MyIterable;

@EBean(scope=Scope.Singleton)
public class ExtraInfoController extends BaseController{

	@Bean
	DBController dbController;
	
	ExtraNumDao extraDao;
	
	@AfterInject
	void init(){
		extraDao = dbController.getExtraNumDao();
	}
	
	public void addExtraToDb(List<ExtraNum> extras){
		if(extras != null && (!extras.isEmpty())){
			extraDao.insertInTx(new MyIterable<ExtraNum>(extras));
		}
	}
	
	public void removeAll(){
		extraDao.deleteAll();
	}
	
	
	public List<ExtraNum> getExtraByProviderId(long id){
		return extraDao.queryRaw("where provider_id = " + id);
	}
}
