package com.monday.companycontact;

import java.io.Serializable;
import java.util.List;

import com.monday.companycontact.db.ExtraNum;
import com.monday.companycontact.db.Provider;

/**
 * 联系人提供器
 * @author MONDAY
 *
 */
public interface IProviderLoader extends Serializable{

	/**
	 * 返回联系人
	 * @param index
	 * @return
	 */
	public Provider getProvider(int index);
	
	
	/**
	 * 联系人数量
	 * @return
	 */
	public int getCount();
	
	/**
	 * 返回全部
	 * @return
	 */
	public List<Provider> getAll();
	
	/**
	 * 联系
	 * @param index
	 * @return
	 */
	public List<ExtraNum> getProviderExtraInfo(int index);
	
}
