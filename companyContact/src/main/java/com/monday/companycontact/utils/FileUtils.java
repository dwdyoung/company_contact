package com.monday.companycontact.utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtils {

	/**
	 * 取出一个文件的后缀
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(File file){
		if(file == null || file.isDirectory() || !file.exists()){
			return "";
		}
		
		String name = file.getName();
		String[] nameSp = name.split("\\.");
		return nameSp == null ? "" : 
			(nameSp.length == 0 ? "" :
				(nameSp[nameSp.length - 1]));
	}
	
	
	/**
	 * 文件排列
	 * @return
	**/
	public static void sort(List<File> fileList){
		Collections.sort(fileList, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				
				if(lhs.isDirectory() && !rhs.isDirectory()){
					return -1;
				}
				
				if(!lhs.isDirectory() && rhs.isDirectory()){
					return 1;
				}
				
				String lname = PingYinUtil.getPingYin(lhs.getName());
				String rname = PingYinUtil.getPingYin(rhs.getName());
				
				return lname.compareTo(rname);
			}
		});
	}
	
}
