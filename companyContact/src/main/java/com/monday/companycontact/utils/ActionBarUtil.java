package com.monday.companycontact.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.view.Window;

public class ActionBarUtil {

	/** 
	 * 如果设备有物理菜单按键，需要将其屏蔽才能显示OverflowMenu 
	 */  
	public static void forceShowOverflowMenu(ActionBarActivity context) {  
		try {  
			ViewConfiguration config = ViewConfiguration.get(context);  
			Field menuKeyField = ViewConfiguration.class  
					.getDeclaredField("sHasPermanentMenuKey");  
			if (menuKeyField != null) {  
				menuKeyField.setAccessible(true);  
				menuKeyField.setBoolean(config, false);  
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
	
	
	/** 
	 * 显示OverflowMenu的Icon 
	 *  
	 * @param featureId 
	 * @param menu 
	 */  
	public static void setOverflowIconVisible(int featureId, Menu menu) {  
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {  
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {  
				try {  
					Method m = menu.getClass().getDeclaredMethod(  
							"setOptionalIconsVisible", Boolean.TYPE);  
					m.setAccessible(true);  
					m.invoke(menu, true);  
				} catch (Exception e) {  
					Log.d("OverflowIconVisible", e.getMessage());  
				}  
			}  
		}  
	}  
	
}
