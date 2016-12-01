package com.monday.companycontact;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface MyPrefs {

	@DefaultString("/mnt/sdcard")
	String currentPath();
	
	@DefaultBoolean(true)
	boolean isFirst1();
	
	@DefaultInt(0)
	int screenWidth();
	
	@DefaultInt(0)
	int screenHeight();
	
}
