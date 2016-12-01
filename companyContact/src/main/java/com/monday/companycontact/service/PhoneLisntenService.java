package com.monday.companycontact.service;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.monday.companycontact.controller.ProviderController;

@EService
public class PhoneLisntenService extends Service{

	@Bean
	ProviderController providerController;
	
	PhoneReceiver phoneReceive; 
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		phoneReceive = new PhoneReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		filter.addAction(Intent.EXTRA_PHONE_NUMBER);
		registerReceiver(phoneReceive, filter);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(phoneReceive != null){
			unregisterReceiver(phoneReceive);
		}
	}
	
}
