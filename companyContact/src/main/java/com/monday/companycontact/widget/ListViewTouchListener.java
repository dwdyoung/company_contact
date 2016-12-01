package com.monday.companycontact.widget;

import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

public class ListViewTouchListener implements OnTouchListener{

	ActionBar actionBar;
	
	public ListViewTouchListener(ActionBar actionBar) {
		this.actionBar = actionBar;
	}

	float mStartY;
	
	float mLastY;

	private float mLastDeltaY;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(!(v instanceof ListView) || actionBar == null){
			return false;
		}
		
		final float y = event.getY();  
        switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                mStartY = y;  
                mLastY = mStartY;  
                break;  
            case MotionEvent.ACTION_MOVE:  
                if(mLastY > y){
                	actionBar.hide();
                } else {
                	actionBar.show();
                }
                
                mLastY = y;  
                break;  
            case MotionEvent.ACTION_UP:  
            	if(mLastY > y){
                	actionBar.hide();
                } else {
                	actionBar.show();
                }
                break;  
        }  
		return false;
	}

}
