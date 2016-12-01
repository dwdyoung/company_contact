package com.monday.companycontact.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monday.companycontact.R;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.utils.UI;

@EActivity(R.layout.activity_add_one)
public class AddOneActivity extends BaseActivity{

	@ViewById(R.id.provider_add_one_name)
	TextView nameTv;
	
	@ViewById(R.id.provider_add_one_phone)
	TextView phoneTv;
	
	@ViewById(R.id.provider_add_one_extraLayout)
	ViewGroup viewGroup;
	
	@Bean
	ProviderController providerController;
	
	@AfterViews
	void init(){
		
	}
	
	@Click(R.id.provider_add_one_confirm)
	void onConfirm(){
		
		String name = nameTv.getText().toString();
		if(name.equals("")){
			UI.toast(this, "请输入名字");
			return;
		}
		
		String phone = phoneTv.getText().toString();
		if(phone.equals("")){
			UI.toast(this, "请输入电话");
			return;
		}
		
		Provider provider = new Provider();
		provider.setProviderName(name);
		provider.setProviderPhone(phone);
		providerController.addProvider(provider);
		new AlertDialog.Builder(this)
		.setTitle("添加成功")
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		})
		.show();
		
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
}
