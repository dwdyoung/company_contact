package com.monday.companycontact.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.ICreater;
import com.monday.companycontact.IInfoProvider;
import com.monday.companycontact.IInfoProvider.ExtraInfo;
import com.monday.companycontact.IProviderLoader;
import com.monday.companycontact.db.ExtraNum;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.utils.LogUtil;
import com.monday.companycontact.utils.StringUtils;

public class ExcelCreater implements ICreater{

	@Override
	public IProviderLoader createLoader(List<List<Object>> excelObjs, IInfoProvider infoProvider) {
		
		if(infoProvider == null || excelObjs == null){
			return null;
		}
		
		final List<Provider> providerList = new ArrayList<Provider>();
		
		final List< List<ExtraNum>> extraMap = new ArrayList<List<ExtraNum>>();
		
		try {
			for(List<Object> litem : excelObjs){
				
				if(litem == null){
					continue;
				}
				
				int nameIndex = infoProvider.getNameIndex();
				int phoneIndex= infoProvider.getPhoneIndex();
				
				LogUtil.v("phoneIndex : " + phoneIndex);
				
				if(nameIndex < 0 || phoneIndex < 0 || nameIndex >= litem.size() || phoneIndex >= litem.size()){
					continue;
				}
				
				Object phoneObj = litem.get(phoneIndex);
				
				if(phoneObj == null){
					continue;
				}
				String phone = phoneObj.toString();
				
				if(StringUtils.isNum(phone)){
					Provider item = new Provider();
					
					Object nameObj = litem.get(nameIndex);
					
					if(nameObj == null){
						continue;
					}
					
					// 设置联系人和电话
					item.setProviderName(nameObj.toString());
					item.setProviderPhone(phone);
					if(providerList.add(item)){
						
						// 添加额外的
						List<ExtraInfo> extraInfos = infoProvider.extraInfos();
						if(extraInfos != null && !extraInfos.isEmpty()){
							
							List<ExtraNum> extraNumDb = new ArrayList<ExtraNum>();
							for(ExtraInfo extraa : extraInfos){
								
								if(extraa == null){
									continue;
								}
								
								if(extraa.index < 0 || extraa.index >= litem.size()){
									continue;
								}
								
								Object extraObj = litem.get(extraa.index);
								if(extraObj == null){
									continue;
								}
								
								ExtraNum num = new ExtraNum();
								num.setExtraNum(extraObj.toString());
								num.setExtraNumName(extraa.desp);
								extraNumDb.add(num);
							}
							
							extraMap.add(extraNumDb);
						}
					}
				}
			}
			
			IProviderLoader loader = new IProviderLoader() {
				@Override
				public Provider getProvider(int index) {
					return providerList.get(index);
				}

				@Override
				public int getCount() {
					return providerList.size();
				}

				@Override
				public List<Provider> getAll() {
					return providerList;
				}

				@Override
				public List<ExtraNum> getProviderExtraInfo(int index) {
					return extraMap.get(index);
				}
				
			};
			
			return loader;
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	
	static void p(Object obj){
		if(!AppContext.DEBUG){
			return;
		}
		if(obj == null){
			Log.v("TEST", "null");
		} else {
			Log.v("TEST", obj.toString());
		}
	}

}
