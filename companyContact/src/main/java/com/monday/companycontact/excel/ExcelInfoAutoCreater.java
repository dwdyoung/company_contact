package com.monday.companycontact.excel;

import java.io.IOException;
import java.util.List;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.IInfoProvider;
import com.monday.companycontact.utils.StringUtils;
import com.umeng.socialize.utils.Log;

/**
 * Excel文件自动分析器
 * @author MONDAY
 *
 */
public class ExcelInfoAutoCreater {

	/**
	 * 分析Excel，自动列出联系人和电话号码
	 * @param excel
	 * @return
	 * @throws IOException 
	 */
	public static IInfoProvider createInfoProvider(List<List<Object>> objsList) throws IOException{
		// 获取Excel的前10行进行分析
		if(objsList == null){
			return null;
		}
		
		// 获取Excel的标题栏
		List<Object> titleList = null;
		int titleIndex = -1;
		
		List<Object> lastList = null;
		for(int i = 0; i < 10; i ++){
			
			List<Object> objs = objsList.get(i);
			
			if(checkRowHasNum(objs)){
//				titleList = objs;
				titleIndex = i - 1;
				titleList = lastList;
				break;
			}
			
			lastList = objs;
		}
		
		p("titleList : " + titleList);
		
		// 获取电话一列
		final int phoneIndex = checkColIsPhone(objsList, titleIndex);
		
		p("phoneIndex : " + phoneIndex);
		
		// 获取联系人一列
		final int nameIndex = checkColIsName(titleList);
		
		p("nameIndex : " + nameIndex);
		
		return new IInfoProvider(phoneIndex, nameIndex);
	}
	
	
	/**
	 * 检查某一列是否为标题列
	 * @param objs
	 * @return
	 */
	private static boolean checkRowIsTitle(List<Object> objs){
		if(objs == null || objs.size() < 2){
			return false;
		}
		
		// 检查是否有空置
		boolean hasNull = false;
		for(Object obj : objs){
			if(obj == null){
				hasNull = true;
				break;
			}
		}
		
		// 如果有空置，则不是标题栏
		if(hasNull){
			return false;
		}
		
		// 检查是否有数字
		for(Object obj : objs){
			// 如果有数字则不是标题栏
			if(StringUtils.isPhone(obj.toString())){
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 检查某一行是否有数字
	 * @param row
	 * @return
	 */
	private static boolean checkRowHasNum(List<Object> row){
		if(row == null){
			return false;
		}
		
		for(Object item : row){
			if(item == null){
				continue;
			}
			
			
			if(StringUtils.isNum(item.toString())){
				
				return true;
			}
		}
		
		return false;
	}
	
	
	
	/**
	 * 获取那一列是
	 * @param objsList
	 * @return
	 */
	private static int checkColIsPhone(List<List<Object>> objsList, int titleRow){

		if(objsList == null){
			return -1;
		}
		
		
		if(objsList.size() <= titleRow){
			return -1;
		}
		
		// 从标题列开始，从下数10行内，检查那一列是符合电话的特征
		int maxRow = titleRow + 10 > objsList.size() ? objsList.size() : titleRow + 10;
		for(int i = titleRow + 1; i < maxRow; i++){
			
			List<Object> row = objsList.get(i);
			if(row == null || row.size() < 2){
				continue;
			}
			
			for(int j = 0; j < row.size(); j++){
				Object obj = row.get(j);
				
				if(obj == null || obj.toString().equals("")){
					continue;
				}
				
				String str = obj.toString();
				if(StringUtils.isPhone(str)){
					return j;
				}
			}
			
		}
		
		return -1;
	}
	
	
	private static String[] NAME_CONTAINER = {
		"姓名",
		"name",
		"联系人",
		"供应商",
		"名字",
		"供应商名称",
	};
	/**
	 * 检查那一列是名字
	 * @param objsList
	 * @param titleRow
	 * @return
	 */
	private static int checkColIsName(List<Object> titleList){
		
		/**
		 * 如果找不到标题栏，就默认为第一列是名字
		 */
		if(titleList == null){
			return 0;
		}
		
		for(int i = 0; i < titleList.size(); i ++){
			Object title = titleList.get(i);
			
			if(title == null){
				continue;
			}
			
			if(isStringPossableName(title.toString())){
				return i;
			}
		}
		
		return 0;
		
	}
	
	
	/**
	 * 某一个字符串是否为名字
	 * @param str
	 * @return
	 */
	private static boolean isStringPossableName(String str){
		if(str == null){
			return false;
		}
		
		for(String name : NAME_CONTAINER){
			if(name.equals(str)){
				return true;
			}
		}
		return false;
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
