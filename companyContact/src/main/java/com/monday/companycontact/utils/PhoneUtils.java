package com.monday.companycontact.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

public class PhoneUtils {

	/**
	 * 打电话
	 * @param context
	 * @param phone
	 */
	public static void call(Context context, String phone){
		Intent intent = new Intent(
				Intent.ACTION_CALL, Uri
				.parse("tel:" + phone));
		context.startActivity(intent);
		
	}
	
	
	/**
	 * 发短信
	 * @param context
	 * @param phone
	 */
	public static void message(String phone, String message){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, message, null, null);
	}

}
