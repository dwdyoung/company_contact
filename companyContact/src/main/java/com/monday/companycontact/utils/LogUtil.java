package com.monday.companycontact.utils;

import com.monday.companycontact.AppContext;

import android.util.Log;

public class LogUtil {

	public static void v(Object obj){
		
		if(!AppContext.DEBUG){
			return;
		}
		
		if(obj == null){
			Log.v("TEST", " null ");
		} else {
			Log.v("TEST", obj.toString());
		}
	}
	
}
