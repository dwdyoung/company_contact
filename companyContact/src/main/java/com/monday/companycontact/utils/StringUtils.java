package com.monday.companycontact.utils;


public class StringUtils {

	/**
	 * 判断字符串是否电话号码
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str){
		if(str == null){
			return false;
		}
		
		if(str.contains("-")){
			str = str.replace("-", "");
		}
		
		if(str.contains(" ")){
			str = str.replace(" ", "");
		}
		
		try {
			Long.valueOf(str);
			
			if(isMobileNum(str) || isFixedLineNum(str)){
				return true;
			}
			
			
			
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 是否为手机号
	 * @return
	 */
	// 以一下数字开头 139\138\137\136\135\134\159\158\157\150\151\152\188
	// 130、131、132、156、155
	// 133、153、189
	private static String[] PHONE_HEAD = {
		// 移动
		"139", "138", "137",
		"136", "135", "134",
		"159", "158", "157",
		"150", "151", "152",
		"188",
		
		// 联通
		"130", "131", "132",
		"156", "155",
		
		// 电信
		"133", "153", "189",
	};
	
	
	/**
	 * 是否手机电话号码
	 * @param str
	 * @return
	 */
	private static boolean isMobileNum(String str){
		if(str == null){
			return false;
		}
		
		if(str.length() == 11){
			
			for(String phone : PHONE_HEAD){
				if(str.startsWith(phone)){
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	
	
	/**
	 * 是否固话号码
	 * @return
	 */
	private static boolean isFixedLineNum(String str){
		if(str == null){
			return false;
		}
		
		if(str.length() == 8 || str.length() == 12 || str.length() == 11){
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * 是否是拼音
	 * @param str
	 * @return
	 */
	public static boolean isPinyin(String str){
		if(str == null){
			return false;
		}
		
		if(str.length() == 1){
			char c = str.charAt(0);
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
				return false;
			}
		
			return true;
		}
		
		int count = str.length();
		for(int i = 0; i < count; i++){
			char c = str.charAt(i);
			if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))){
				return true;
			}
		}
		
		return false;
	}



	/**
	 * 是否是数字
	 * @param phone
	 * @return
	 */
	public static boolean isNum(String str) {
		if(str == null){
			return false;
		}
		
		if(str.contains("-")){
			str = str.replace("-", "");
		}
		
		if(str.contains(" ")){
			str = str.replace(" ", "");
		}
		
		try {
			Long.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
