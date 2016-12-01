package com.monday.companycontact.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class UI {

	public static ProgressDialog showProgress(Context context, String str){
		if(context == null){
			return null;
		}
		
		ProgressDialog dialog = new ProgressDialog(context);
		if(str != null){
			dialog.setMessage(str);
		}
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		return dialog;
	}
	
	
	public static void toast(Context context, String str){
		if(context == null || str == null){
			return;
		}
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
}
