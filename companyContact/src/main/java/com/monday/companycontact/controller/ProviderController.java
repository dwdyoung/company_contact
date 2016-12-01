package com.monday.companycontact.controller;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.database.sqlite.SQLiteDatabase;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.db.DaoMaster;
import com.monday.companycontact.db.DaoMaster.DevOpenHelper;
import com.monday.companycontact.db.DaoSession;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.db.ProviderDao;
import com.monday.companycontact.db.MyIterable;
import com.monday.companycontact.utils.ContactUtils;

@EBean(scope=Scope.Singleton)
public class ProviderController extends BaseController{
	
	List<Provider> providerList;
	
    @App
    AppContext appContext;
    
    @Bean
    DBController dbController;
    
    ProviderDao providerDao;
	
	@AfterInject
	void init(){
		
        providerDao = dbController.getProviderDao();
        providerList = providerDao.loadAll();
        
        ContactUtils.sort(providerList);
	}
	
	
	public List<Provider> getProviderList() {
		return providerList;
	}
	
	
	/**
	 * 检验电话号码是否在列表中
	 * @param phone
	 * @return
	 */
	public String isPhoneContaind(String phone){
		List<Provider> list =providerDao.queryRaw("where PROVIDER_PHONE = '"+ phone +"'");
		if(list != null && !list.isEmpty()){
			return list.get(0).getProviderPhone();
		}
		return null;
	}
	
	
	/**
	 * 批量保存供应商
	 * @param list
	 * @return
	 */
	public boolean addProviderList(List<Provider> list){
		if(list != null && list.size() > 0){
			providerDao.insertInTx(new MyIterable<Provider>(list));
			providerList.addAll(list);
			ContactUtils.sort(providerList);
			return true;
		} else {
			return false;
		}
	}
	
	
	public void removeAllProvider(){
		providerDao.deleteAll();
		providerList.clear();
	}
	

	/**
	 * 添加一个供应商到数据库
	 * @param provider
	 */
	public void addProvider(Provider provider){
		providerDao.insertOrReplace(provider);
		providerList.add(provider);
		ContactUtils.sort(providerList);
	}

	
	
	public void removeProvider(Provider provider){
		if(provider.getId() != null){
			providerDao.deleteByKey(provider.getId());
			
			for(Provider item : providerList){
				if(item.getId() != null && item.getId().longValue() == provider.getId().longValue()){
					providerList.remove(item);
				}
			}
		}
	}
	
	
	public List<Provider> queryPinyin(String pinyin, String pinyin2){
		if(pinyin != null && pinyin2 == null){
			return providerDao.queryRaw("where pinyin like '%" + pinyin + "%'");
		}
		
		if(pinyin == null && pinyin2 != null){
			return providerDao.queryRaw("where pingyin2 like '%" + pinyin2 + "%'");
		}
		
		if(pinyin != null && pinyin2 != null ){
			return providerDao.queryRaw("where pinyin like '%" + pinyin + "%' or pingyin2 like '%" + pinyin2 + "%'");
		}
		
		return providerList;
	}
	
	
	public List<Provider> queryPhoneOrNum(String phone, String num){
		if(num != null && phone == null){
			return providerDao.queryRaw("where provider_num like '%" + num + "%'");
		}
		
		if(num == null && phone != null){
			return providerDao.queryRaw("where provider_phone like '%" + phone + "%'");
		}
		
		if(num != null && phone != null ){
			return providerDao.queryRaw("where provider_num like '%" + num + "%' or provider_phone like '%" + phone + "%'");
		}
		
		return providerList;
	}
	
	/**
	 * 根据groupid 获取联系人
	 * @param groupId
	 * @return
	 */
	public List<Provider> queryByGroup(long groupId){
		return providerDao.queryRaw("where GROUP_ID = " + groupId);
	}
	
}
