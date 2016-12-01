package com.monday.companycontact.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.widget.ListView;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.MyPrefs_;
import com.monday.companycontact.R;
import com.monday.companycontact.ui.adapter.FileAdapter;
import com.monday.companycontact.utils.FileUtils;
import com.monday.companycontact.widget.ListViewTouchListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 文件浏览界面
 * @author monday
 *
 */
@EActivity(R.layout.activity_file)
@OptionsMenu(R.menu.file)
public class FileViewActivity extends BaseActivity{

	@ViewById(R.id.file_listview)
	ListView listView;
	
	@Pref
	MyPrefs_ myPrefs;
	
	@App
	AppContext appContext;
	
	FileAdapter fileAdapter;
	
	List<File> currentFileList;
	
	ActionBar actionBar;
	
	@AfterViews
	void init(){
		
		actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
		actionBar.setHideOnContentScrollEnabled(true);
		actionBar.setHomeButtonEnabled(true);  
		actionBar.setDisplayHomeAsUpEnabled(true);  
		
		currentFileList = new ArrayList<File>();
//		actionBar.setTitle(myPrefs.currentPath().get());
		
		fileAdapter = new FileAdapter(this, currentFileList);
		listView.setAdapter(fileAdapter);
		listView.setOnTouchListener(new ListViewTouchListener(actionBar));
		refreshFileList();
	}
	
	
	@OptionsItem(android.R.id.home)
	void onHome(){
		finish();
	}
	
	/**
	 * 刷新文件列表
	 */
	void refreshFileList(){
		currentFileList.clear();
		String path = myPrefs.currentPath().get();
		File currentPath = new File(path);
		if(currentPath.isDirectory()){
			File[] listFile = currentPath.listFiles();
			Collections.addAll(currentFileList, listFile);

			// 去除隐藏文件
			List<File> hiddenFiles = new ArrayList<File>();
			for(File file : currentFileList){
				if(file.isHidden()){
					hiddenFiles.add(file);
				}
			}

			for(File file : hiddenFiles){
				currentFileList.remove(file);
			}

			FileUtils.sort(currentFileList);
		}

		actionBar.setTitle(path);
		fileAdapter.notifyDataSetChanged();
	}
	
//	@Click(R.id.file_upper)
	@OptionsItem(R.id.action_up)
	void onUpper(){
		
		File currentPath = new File(myPrefs.currentPath().get());

		String parent = currentPath.getParent();

		String current = currentPath.getPath();

		if(current.equals("/") || current.equals("\\") || current.equals("/mnt/sdcard") ||
				current.equals(Environment.getExternalStorageDirectory().getPath())){
			return;
		}

		myPrefs.currentPath().put(parent);
		
		refreshFileList();
	}
	
	@ItemClick(R.id.file_listview)
	void onFileItemClick(int position){
		final File file = currentFileList.get(position);
		
		if(file.isDirectory()){
//			myPrefs.currentPath().put(file.getPath());
			refreshFileList();
		} else {
			
			String suffix = FileUtils.getFileSuffix(file);
			if(suffix.equals("xls")/*  || suffix.equals("xlsx")*/){
				new AlertDialog.Builder(this).setTitle("警告")
				.setMessage("是否导入名字为“" + file.getName() + "”的文件?")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ProviderParseActivity_.intent(FileViewActivity.this).excelFile(file).start();
						dialog.cancel();
						finish();
						
//						TellMeYourExcelActivity_.intent(FileViewActivity.this).excelFile(file).start();
//						dialog.cancel();
//						finish();
					}
				})
				.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
			} else if(suffix.equals("xlsx")){
				new AlertDialog.Builder(this).setTitle("警告")
				.setMessage("目前只支持xls格式文件")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
			}
		}
	}
	
}
