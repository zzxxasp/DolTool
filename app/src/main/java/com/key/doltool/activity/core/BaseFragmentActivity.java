package com.key.doltool.activity.core;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.key.doltool.R;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.view.SystemBarTintManager;
import com.key.doltool.view.Toast;
public abstract class BaseFragmentActivity extends AppCompatActivity {
	public Toolbar toolbar;
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.blue_dark);
		//去背景
        getWindow().setBackgroundDrawable(null);
        //添加栈内进行管理
        ExitApplication.getInstance().addActivity(this);
    }

	public void initToolBar(Toolbar.OnMenuItemClickListener onMenuItemClick){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if(toolbar!=null){
			toolbar.setTitle("大航海时代助手");
			toolbar.setTitleTextColor(getResources().getColor(R.color.White));
			setSupportActionBar(toolbar);
			if(onMenuItemClick!=null){
				toolbar.setOnMenuItemClickListener(onMenuItemClick);
			}
			toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		}
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
			default:
		}
		return super.onOptionsItemSelected(item);
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
