package com.monday.companycontact.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.ListView;

import com.monday.companycontact.R;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.db.ProviderList;
import com.monday.companycontact.ui.adapter.ProviderCheckableAdapter;
import com.monday.companycontact.utils.ContactUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_import) 
public class ImportToContactActivity extends BaseActivity{

	@ViewById(R.id.import_listview)
	ListView listView;
	
	ProviderCheckableAdapter adapter;
	
	@Extra
	ProviderList providerList;
	
	@ViewById(R.id.import_pick_all)
	Button pickAllBt;
	
	@AfterViews
	void init(){
		
		ActionBar actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
		actionBar.setHideOnContentScrollEnabled(true);
		actionBar.setHomeButtonEnabled(true);  
		actionBar.setDisplayHomeAsUpEnabled(true);  
		
		getSupportActionBar().setTitle("导入到通讯录");
		adapter = new ProviderCheckableAdapter(providerList, this);
		listView.setAdapter(adapter);
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
	@ItemClick(R.id.import_listview)
	void onItemClick(int position){
		adapter.setChecked(position, !adapter.getCheck(position));
		adapter.notifyDataSetChanged();
	}
	
	@Click(R.id.import_import)
	void onImport(){
		
		new AlertDialog.Builder(this).setTitle("警告").setMessage("是否将供应商信息导入到本机的通讯录？（注：相同号码的条目将会覆盖，不会造成重复添加。）")
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				insertProviders();
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
	
	@Click(R.id.import_pick_all)
	void onPickAll(){
		int count = providerList.size();
		
		// 检查是否全选了
		boolean isAllCheck = true;
		for(int i = 0; i < count; i++){
		
			if(!adapter.getCheck(i)){
				isAllCheck = false;
				break;
			}
		}
		
		if(!isAllCheck){
			for(int i = 0; i < count; i++){
				adapter.setChecked(i, true);
			} 
			
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.checkbox_on_background);
			BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
			bd.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
			pickAllBt.setCompoundDrawables(null, null, bd, null);
			
		} else {
			
			for(int i = 0; i < count; i++){
				adapter.setChecked(i, false);
			}
			
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.checkbox_off_background);
			BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
			bd.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
			pickAllBt.setCompoundDrawables(null, null, bd, null);
			
		}
		adapter.notifyDataSetChanged();
	}
	
	void insertProviders(){
		
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在导入，请稍后...");
		dialog.show();
		
		int count = providerList.size();
		
		List<Provider> importList = new ArrayList<Provider>();
		
		for(int i = 0; i < count;i ++){
			if(adapter.getCheck(i)){
				importList.add(providerList.get(i));
			}
		}
		
		_insertProviders(importList, dialog);
	}
	
	@Background
	void _insertProviders(List<Provider> list, ProgressDialog dialog){
		ContactUtils.insertProviders(this, list);
		onInsertFinished(dialog);
	}
	
	@UiThread
	void onInsertFinished(ProgressDialog dialog){
		dialog.dismiss();
		new AlertDialog.Builder(this).setMessage("导入完毕").setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.cancel();
				finish();
				
			}
		}).show();
	}
	
}
