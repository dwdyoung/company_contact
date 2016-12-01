package com.monday.companycontact.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.monday.companycontact.MyPrefs_;
import com.monday.companycontact.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.activity_comingcall)
public class ComingCallActivity extends Activity{

	@Pref
	MyPrefs_ myPrefs;
	
	@ViewById(R.id.comingcall_phone)
	TextView phoneTv;
	
	@ViewById(R.id.comingcall_name)
	TextView nameTv;
	
	@Extra
	String phoneNum;
	
	@Extra
	String name;
	
	@AfterViews
	void init(){
		phoneTv.setText(phoneNum);
		nameTv.setText(name);
	}
	
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	 
        final View view = getWindow().getDecorView();
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
 
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lp.verticalMargin = 100;
        lp.width = myPrefs.screenWidth().get() - 100;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        getWindowManager().updateViewLayout(view, lp);
	}
	
}
