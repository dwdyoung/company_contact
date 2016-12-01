package com.monday.companycontact.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.monday.companycontact.AppContext;
import com.monday.companycontact.MyPrefs_;
import com.monday.companycontact.R;
import com.monday.companycontact.controller.ExtraInfoController;
import com.monday.companycontact.controller.GroupController;
import com.monday.companycontact.controller.ProviderController;
import com.monday.companycontact.db.ProviderList;
import com.monday.companycontact.ui.adapter.ProviderIndexableAdapter;
import com.monday.companycontact.ui.adapter.ProviderIndexableAdapter.OnItemBtClickListener;
import com.monday.companycontact.utils.ActionBarUtil;
import com.monday.companycontact.widget.ListViewTouchListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.woozzu.android.widget.IndexableListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends BaseActivity { 

	ProviderList providerList;

	@ViewById(R.id.main_listview)
	IndexableListView listView;

	@Bean
	ProviderController providerController;
	
	@Bean
	ExtraInfoController extraInfoController;
	
	@Bean
	GroupController groupController;
	
	@Pref
	MyPrefs_ myPrefs;

	ProviderIndexableAdapter providerAdapter;

	boolean isStartVersionUpdateFlag;

	SearchView searchView;
	
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
		UmengUpdateAgent.update(this);
		
	}
	
	void test() throws BiffException, IOException {
		// new File("/sdcard/通讯APP.xls"))
//		Workbook book = Workbook.getWorkbook(new File("/sdcard/公司通讯录201503.xls"));  
		Workbook book = Workbook.getWorkbook(new File("/sdcard/通讯APP.xls"));  
        book.getNumberOfSheets();  
        
        // 获得第一个工作表对象  
        Sheet sheet = book.getSheet(0);  
        int Rows = sheet.getRows();  
        int Cols = sheet.getColumns();  
        
        for (int i = 0; i < Rows; ++i) {  
            for (int j = 0; j < Cols; ++j) {  
                // getCell(Col,Row)获得单元格的值  
                p((sheet.getCell(j, i)).getContents() + "\t");  
            }  
            p("\n");  
        }  
        // 得到第一列第一行的单元格  
        Cell cell1 = sheet.getCell(0, 0);  
        String result = cell1.getContents();  
        System.out.println(result);  
        book.close();  
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@AfterInject
	void start(){
//		getWindow().requestFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
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
	

	@SuppressLint("NewApi")
	@AfterViews
	void init(){
		actionBar = getSupportActionBar();
		// 是否显示应用程序图标，默认为true  
		actionBar.setDisplayShowHomeEnabled(true);  
		actionBar.setDisplayShowTitleEnabled(true);  
		actionBar.setHideOnContentScrollEnabled(true);
		ActionBarUtil.forceShowOverflowMenu(this);
		
		providerList = new ProviderList();
		providerAdapter = new ProviderIndexableAdapter(providerList, this);
		listView.setAdapter(providerAdapter);
		listView.setFastScrollAlwaysVisible(true);
		providerAdapter.setOnItemBtClickListener(new OnItemBtClickListener() {
			@Override
			public void onClick(int position, View view) {
			}
		});
		
		
		if(Build.VERSION.SDK_INT > 20){
			listView.setNestedScrollingEnabled(true);
		} else {
			listView.setOnTouchListener(new ListViewTouchListener(actionBar));
		}
		
		// 添加平台分享支持
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "appid", "appkey");
		qqSsoHandler.addToSocialSDK();
		
		UMWXHandler umwxHandler = new UMWXHandler(this, "appid", "secret");
		umwxHandler.setToCircle(true);
		umwxHandler.addToSocialSDK();
		
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "appId", "appKey");
		qZoneSsoHandler.addToSocialSDK();
		
		SinaSsoHandler sinaSsoHandler = new SinaSsoHandler(this);
		sinaSsoHandler.addToSocialSDK();
	}

	
	@Override  
	public boolean onMenuOpened(int featureId, Menu menu) {  
		return super.onMenuOpened(featureId, menu);  
	}  

	@Override
	protected void onResume() {
		super.onResume();
		providerList.clear();
		providerList.addAll(providerController.getProviderList());
		providerAdapter.notifyDataSetChanged();
		
		
		if(myPrefs.screenHeight().get() == 0){
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			myPrefs.screenHeight().put(dm.heightPixels);
			myPrefs.screenWidth().put(dm.widthPixels);
		}
	}
	
	@OptionsItem(R.id.action_search)
	void onSearch(){
		SearchActivity_.intent(this).start();
	}

	@OptionsItem(R.id.action_file)
	void onFile(){
		FileViewActivity_.intent(this).start();
	}

	@OptionsItem(R.id.action_http)
	void onHttp(){
		HttpUpdateActivity_.intent(this).start();
	}

	@OptionsItem(R.id.action_introduce)
	void onIntroduce(){
		IntroduceActivity_.intent(this).start();
	}

	@OptionsItem(R.id.action_import_contract)
	void onImportToContact(){
		ImportToContactActivity_.intent(this).providerList(providerList).start();
	}

	@OptionsItem(R.id.action_single)
	void onAddSingle(){
		AddOneActivity_.intent(this).start();
	}
	
	@OptionsItem(R.id.action_delete_all)
	void onDeletaAll(){
		new AlertDialog.Builder(this)
		.setTitle("警告")
		.setMessage("是否要清空所有的联系人（此操作只针对app中存储的联系人，不影响手机里的联系人.）")
		.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				providerController.removeAllProvider();
				extraInfoController.removeAll();
				groupController.removeAll();
				
				providerList.clear();
				providerList.addAll(providerController.getProviderList());
				providerAdapter.notifyDataSetChanged();
			}
		})
		.show();
		
	}
	

//	@ItemClick(R.id.main_listview)
//	void onItemClick(int position){
//		ProviderDetailActivity_.intent(this).provider(providerList.get(position)).start();
//	}

	@OptionsItem(R.id.action_checkupdate)
	void onCheckUpdate(){
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(MainActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		            Toast.makeText(MainActivity.this, "没有更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            Toast.makeText(MainActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout: // time out
		            Toast.makeText(MainActivity.this, "超时", Toast.LENGTH_SHORT).show();
		            break;
		        }
		        
		        UmengUpdateAgent.setUpdateListener(null);
		    }
		});
		UmengUpdateAgent.update(this);
	}
	
	@OptionsItem(R.id.action_feedback)
	void onFeedback(){
		 FeedbackAgent fb = new FeedbackAgent(this);
	     fb.startFeedbackActivity();
	}
	
	
	@OptionsItem(R.id.action_share)
	void onShare(){
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this, "http://www.umeng.com/images/pic/banner_module_social.png"));
		mController.openShare(this, false);
		// 设置分享图片，参数2为本地图片的资源引用
		//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		//mController.setShareMedia(new UMImage(getActivity(), 
//		                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		// 设置分享音乐
		//UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		//uMusic.setAuthor("GuGu");
		//uMusic.setTitle("天籁之音");
		// 设置音乐缩略图
		//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//mController.setShareMedia(uMusic);

		// 设置分享视频
		//UMVideo umVideo = new UMVideo(
//		          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// 设置视频缩略图
		//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//umVideo.setTitle("友盟社会化分享!");
		//mController.setShareMedia(umVideo);
	}
	
	
	void initSearchView(SearchView searchView){
		if(searchView == null){
			return;
		}
		
		searchView.setOnSearchClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
			
		});
		
		searchView.setOnSuggestionListener(new OnSuggestionListener() {
			
			@Override
			public boolean onSuggestionSelect(int position) {
				return false;
			}
			
			@Override
			public boolean onSuggestionClick(int position) {
				
				return true;
			}
		});
	}
}