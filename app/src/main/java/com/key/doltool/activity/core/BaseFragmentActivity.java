package com.key.doltool.activity.core;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.view.Toast;
public abstract class BaseFragmentActivity extends FragmentActivity{
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		//去背景
        getWindow().setBackgroundDrawable(null);
        //添加栈内进行管理
        ExitApplication.getInstance().addActivity(this);
    }
    @Override
    public void setContentView(int layoutResID) {
    	View view=getLayoutInflater().inflate(layoutResID,null);
    	LinearLayout status=(LinearLayout)view.findViewById(R.id.status);
    	ViewGroup status2=(ViewGroup)view.findViewById(R.id.mission);
    	LinearLayout slider=(LinearLayout)view.findViewById(R.id.menu_main_top);
    	if(android.os.Build.VERSION.SDK_INT>=19){
    		if(status!=null){
        		status.setPadding(0,getstatusBarHeight(),0,0);
    		}
    		if(slider!=null){
    			slider.setPadding(0,getstatusBarHeight(),0,0);
    		}
    		if(status2!=null){
    			status2.setPadding(0,getstatusBarHeight(),0,0);
    		}
    	}
    	setContentView(view);
    }
    
    public SimpleActionBar getSimpleActionBar(){
    	return new SimpleActionBar(this, false);
    }
    
    public SimpleActionBar getSimpleActionBar(boolean back){
    	return new SimpleActionBar(this, back);
    }
    
    public int getstatusBarHeight(){
		Class<?> c ;
		Object obj ;
		Field field ;
		int x,sbar;
		try {
		    c = Class.forName("com.android.internal.R$dimen");
		    obj = c.newInstance();
		    field = c.getField("status_bar_height");
		    x = Integer.parseInt(field.get(obj).toString());
		    sbar = getResources().getDimensionPixelSize(x);
		} catch(Exception e1) {
			sbar=40;
		}
		return sbar;
    }
    
	public void onBackPressed() {    
		Toast.cancelToast();
		super.onBackPressed();    
	}
	protected void onDestroy(){
		super.onDestroy();
		ExitApplication.getInstance().removeActivity(this);
	}
}
