package com.monday.companycontact.ui;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.monday.companycontact.R;
import com.monday.companycontact.controller.ExtraInfoController;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.db.ExtraNum;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.utils.PhoneUtils;
import com.monday.companycontact.utils.UI;

@EActivity(R.layout.activity_provider_detail)
@OptionsMenu(R.menu.edit)
public class ProviderDetailActivity extends BaseActivity{

	@Extra
	Provider provider;
	
	@Bean
	ExtraInfoController extraInfoController;
	
	@ViewById(R.id.provider_detail_layout)
	ViewGroup viewGroup;
		
	@ViewById(R.id.provider_detail_num)
	EditText numTv;
	
	@Bean
	ProviderController providerController;
	
	@AfterViews
	void init(){
		getSupportActionBar().setTitle(provider.getProviderName());
		getSupportActionBar().setHomeButtonEnabled(true);
		numTv.setText((provider.getProviderPhone() == null ? "" : provider.getProviderPhone()));
		
		List<ExtraNum> extraNums = extraInfoController.getExtraByProviderId(provider.getId());
		
		if(extraNums != null && (!extraNums.isEmpty())){
			for(ExtraNum item : extraNums){
				View itemView = LayoutInflater.from(this).inflate(R.layout.provider_detail_item, null);
				TextView nameV = (TextView)itemView.findViewById(R.id.provider_detail_item_name);
				TextView numV = (TextView)itemView.findViewById(R.id.provider_detail_item_num);
				nameV.setText(item.getExtraNumName() + " : ");
				numV.setText(item.getExtraNum());
				ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT);
				viewGroup.addView(itemView, lp);
			}
		}
		
		numTv.setEnabled(false);
		isEditting = false;
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
	@Click(R.id.provider_detail_call)
	void onCall(){
		new AlertDialog.Builder(this)
		.setTitle("是否打电话给" + provider.getProviderName())
		.setPositiveButton(android.R.string.yes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhoneUtils.call(ProviderDetailActivity.this, 
						provider.getProviderPhone());
				
				dialog.cancel();
			}
		})
		.setNegativeButton(android.R.string.no, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.show();
	}
	
	@Click(R.id.provider_detail_message)
	void onMessage(){
		
		final EditText edit = (EditText)LayoutInflater.from(this).inflate(
				R.layout.message_dialog, null);
		
		new AlertDialog.Builder(this)
		.setTitle("发短信给" + provider.getProviderName())
		.setMessage("请输入短信内容：")
		.setView(edit)
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhoneUtils.message(
						provider.getProviderPhone(),
						edit.getText().toString());
				
				dialog.cancel();
			}
		})
		.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.show();
	}
	
	@OptionsMenuItem(R.id.action_edit)
	MenuItem menuItem;
	
	boolean isEditting = false;
	
	@OptionsItem(R.id.action_edit)
	void onEdit(){
		if(!isEditting){
			menuItem.setTitle("保存");
			numTv.setEnabled(true);
			isEditting = true;
		} else {

			String phone = numTv.getText().toString();
			if(phone.equals("")){
				UI.toast(this, "请输入电话号码");
				return;
			}

			menuItem.setTitle("编辑");
			numTv.setEnabled(false);
			isEditting = false;
			
			provider.setProviderPhone(phone);
			
			providerController.addProvider(provider);
		}
	}
	
	
	@OptionsItem(R.id.action_delete)
	void onDelete(){
		providerController.removeProvider(provider);
		finish();
	}
}
