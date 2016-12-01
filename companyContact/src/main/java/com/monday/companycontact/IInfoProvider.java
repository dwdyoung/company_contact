package com.monday.companycontact;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供excel文件一些基本的信息
 * 实现由解释器或者用户输入
 * @author MONDAY
 *
 */
public class IInfoProvider {
	
	List<ExtraInfo> listExtraInfo;
	
	int phoneIndex;
	
	int nameIndex;
	
	public IInfoProvider(int phoneIndex, int nameIndex) {
		super();
		this.phoneIndex = phoneIndex;
		this.nameIndex = nameIndex;
		listExtraInfo = new ArrayList<IInfoProvider.ExtraInfo>();
	}

	
	
	public int getPhoneIndex() {
		return phoneIndex;
	}



	/**
	 * 返回电话所在列 从0开始
	 * @return
	 */
	public void setPhoneIndex(int phoneIndex) {
		this.phoneIndex = phoneIndex;
	}



	/**
	 * 返回联系人名字所在列 从0开始
	 * @return
	 */
	public int getNameIndex() {
		return nameIndex;
	}




	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	
	
	/**
	 * 返回其他额外的信息
	 * @return
	 */
	public List<ExtraInfo> extraInfos(){
		if(listExtraInfo == null){
			listExtraInfo = new ArrayList<IInfoProvider.ExtraInfo>();
		}
		return listExtraInfo;
	}
	
	/**
	 * 额外信息结构体
	 * @author MONDAY
	 *
	 */
	public static class ExtraInfo{
		public int index;	// 所在列
		public String desp;	// 此列对应的描述
	}

}
