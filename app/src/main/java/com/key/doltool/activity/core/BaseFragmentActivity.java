package com.key.doltool.activity.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.key.doltool.R;
import com.key.doltool.event.CrashHandler;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.util.StringUtil;
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
		CrashHandler.WHERE=getClass().getName();
		CrashHandler.WHO="";
    }

	@Override
	public Intent getIntent() {
		Intent it=super.getIntent();
		CrashHandler.WHO=setWho(it);
		return it;
	}

	public String setWho(Intent it){
		String temp="";
		if(it!=null){
			String id=it.getStringExtra("id");
			String name=it.getStringExtra("name");
			String tw_name=it.getStringExtra("tw_name");
			String city_name=it.getStringExtra("city_name");
			if(!StringUtil.isNull(id)){
				temp+="id:"+id+" ";
			}
			if(!StringUtil.isNull(name)){
				temp+="name:"+name+" ";
			}
			if(!StringUtil.isNull(tw_name)){
				temp+="tw_name:"+tw_name+" ";
			}
			if(!StringUtil.isNull(city_name)){
				temp+="city_name:"+city_name+" ";
			}
		}
		return temp;
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
