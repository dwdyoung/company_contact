package com.monday.companycontact.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.UiThread.Propagation;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.IInfoProvider;
import com.monday.companycontact.IInfoProvider.ExtraInfo;
import com.monday.companycontact.IProviderLoader;
import com.monday.companycontact.R;
import com.monday.companycontact.controller.ExtraInfoController;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.db.ExtraNum;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.excel.ExcelCreater;
import com.monday.companycontact.excel.ExcelInfoAutoCreater;
import com.monday.companycontact.excel.ExcelJxlUtil;
import com.monday.companycontact.ui.adapter.ProviderAdapter;
import com.monday.companycontact.utils.LogUtil;
import com.monday.companycontact.utils.UI;
import com.umeng.socialize.utils.Log;

@EActivity(R.layout.activity_provider_parse)
public class ProviderParseActivity extends BaseActivity{

	@ViewById(R.id.provider_parse_listview)
	ListView listView;
	
	ProviderAdapter providerAdapter;
	
	@Extra
	File excelFile;
	
	@App
	AppContext appContext;
	
	@Bean
	ProviderController providerController;
	
	@Bean
	ExtraInfoController extraInfoController;
	
	List<Provider> providerList;
	
	IInfoProvider infoProvider;
	
	List<List<Object>> objsList;
	
	IProviderLoader providerLoader;
	
	@AfterViews
	void init(){
		
		ActionBar actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
		actionBar.setHideOnContentScrollEnabled(true);
		actionBar.setHomeButtonEnabled(true);  
		actionBar.setDisplayHomeAsUpEnabled(true);  
		
		getSupportActionBar().setTitle("Excel显示的联系人");
		providerList = new ArrayList<Provider>();
		providerAdapter = new ProviderAdapter(providerList, this);
		listView.setAdapter(providerAdapter);
		createLoader(excelFile);
	}
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
	@Click(R.id.provider_parse_confirm)
	void onConfirm(){
		
		UI.showProgress(this, "正在上传到app中，请稍后。");
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				
				providerController.addProviderList(providerList);
				
				// 如果有附加信息
				if(!infoProvider.extraInfos().isEmpty() && providerLoader != null){
					final int count = providerLoader.getCount();
					
					for(int i = 0; i < count; i++){
						List<ExtraNum> extraNums = providerLoader.getProviderExtraInfo(i);
						Provider provider = providerList.get(i);
						if(extraNums != null && (!extraNums.isEmpty())){
							// 为每个extra设置provider的id
							for(ExtraNum num : extraNums){
								num.setProviderId(provider.getId());
							}
							extraInfoController.addExtraToDb(extraNums);
						}
					}
					
				}
				
				return null;
			}
			
			protected void onPostExecute(Void result) {
				new AlertDialog.Builder(ProviderParseActivity.this).setTitle("保存成功")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
					
				}).show();
			};
			
		}.execute();
	}
	
	@Click(R.id.provider_parse_wrong)
	void onWrong(){
		
		final View view = LayoutInflater.from(this).inflate(R.layout.dialog_wrong, null);
		final NumberPicker extraNameView = (NumberPicker)view.findViewById(R.id.wrong_name);
		final NumberPicker extraNumPick = (NumberPicker)view.findViewById(R.id.wrong_phone);
		
		extraNameView.setMaxValue(10);
		extraNameView.setMinValue(1);
		if(infoProvider != null){
			extraNameView.setValue(infoProvider.getNameIndex() + 1);
		}
		
		extraNumPick.setMaxValue(10);
		extraNumPick.setMinValue(1);
		if(infoProvider != null){
			extraNumPick.setValue(infoProvider.getPhoneIndex() + 1);
		}
		
		new AlertDialog.Builder(this)
		.setTitle("请输入通讯录信息")
		.setView(view)
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int name = extraNameView.getValue();
				int phone = extraNumPick.getValue();
				if(name == phone){
					UI.toast(ProviderParseActivity.this, "名字和电话不在同一列");
					return;
				}
				
				name--;
				phone--;
				
				if(infoProvider == null){
					infoProvider = new IInfoProvider(phone, name);
				}
				
				infoProvider.setNameIndex(name);
				infoProvider.setPhoneIndex(phone);
				
				dialog.cancel();
				
				// 重新刷新列表
				createLoader(excelFile);
			}
		})
		.show();
	}
	
	ProgressDialog parseDialog = null;
	@Click(R.id.provider_parse_extra)
	@UiThread(propagation=Propagation.REUSE)
	void onExtra(){
		if(objsList == null || infoProvider == null){
			parseDialog = UI.showProgress(this, "正在读取Excel，请稍后");
			parseExcelFile();
			return;
		}
		
		if(parseDialog != null){
			parseDialog.dismiss();
			parseDialog = null;
		}
		
		final View view = LayoutInflater.from(this).inflate(R.layout.dialog_extra, null);
		final TextView extraNameView = (TextView)view.findViewById(R.id.extra_name);
		final NumberPicker extraNumPick = (NumberPicker)view.findViewById(R.id.extra_numpicker);
		
		extraNumPick.setMaxValue(10);
		extraNumPick.setMinValue(1);
		
		// TODO 加上删除额外备注的功能
		new AlertDialog.Builder(this)
		.setTitle("请输入附加信息")
		.setView(view)
		.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String extraName = extraNameView.getText().toString();
				if(extraName.equals("")){
					UI.toast(ProviderParseActivity.this, "请输入此列的名字");
					return;
				}
				
				int extraIndex = extraNumPick.getValue();
				
				extraIndex--;
				
				ExtraInfo extra = new ExtraInfo();
				extra.desp = extraName;
				extra.index = extraIndex;
				infoProvider.extraInfos().add(extra);
				dialog.cancel();
				
				// 重新创建
				createLoader(excelFile);
			}
		})
		.show();
	}
	
	@Background
	void parseExcelFile(){
		if(objsList == null){
			try {
				objsList = ExcelJxlUtil.readExcel(excelFile);
			} catch (Exception e) {
				e.printStackTrace();
				
				onParseError();
				return;
			}
		}
		
		if(infoProvider == null){
			try {
				infoProvider = ExcelInfoAutoCreater.createInfoProvider(objsList);
			} catch (IOException e) {
				e.printStackTrace();
				
				onParseError();
				return;
			}
		}
		
		onExtra();
	}
	
	@UiThread(propagation=Propagation.REUSE)
	void onParseError(){
		if(parseDialog != null){
			parseDialog.dismiss();
			parseDialog = null;
		}
		
		UI.toast(this, "解释Excel错误");
	}
	
	
	
	void createLoader(final File file){
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在解释excel文件");
		dialog.show();
		new AsyncTask<Void, Void, IProviderLoader>() {
			
			@Override
			protected IProviderLoader doInBackground(Void... params) {
				
				if(objsList == null){
					try {
						objsList = ExcelJxlUtil.readExcel(file);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				
				if(infoProvider == null){
					try {
						infoProvider = ExcelInfoAutoCreater.createInfoProvider(objsList);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
				
				IProviderLoader providerLoader = new ExcelCreater().createLoader(objsList, infoProvider);
				
				return providerLoader;
			}
			
			@Override
			protected void onPostExecute(IProviderLoader providerLoader) {
				dialog.hide();
				
				if(providerLoader == null){
					new AlertDialog.Builder(ProviderParseActivity.this)
						.setTitle("解析Excel文件失败!")
						.setPositiveButton(android.R.string.ok, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.show();
					return;
				}
				providerList.clear();
				
				providerList.addAll(providerLoader.getAll());
				
				ProviderParseActivity.this.providerLoader = providerLoader;
				
				providerAdapter.notifyDataSetChanged();
			}
			
		}.execute();
	}
	
	@ItemClick(R.id.provider_parse_listview)
	void onItemClick(int position){
		ProviderDetailActivity_.intent(this).provider(providerList.get(position)).start();
	}
	
	
	void p(Object obj){
		if(!AppContext.DEBUG){
			return;
		}
		if(obj == null){
			Log.v("TEST", "null");
		} else {
			Log.v("TEST", obj.toString());
		}
	}
	
}
