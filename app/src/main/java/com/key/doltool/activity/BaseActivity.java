package com.key.doltool.activity;
import android.app.Activity;

import com.key.doltool.activity.core.BaseFragmentActivity;
/**
 * BaseActivity
 * @author key
 * @version 0.3
 */
public abstract class BaseActivity extends BaseFragmentActivity{
	private boolean start=true;
	public Activity context=BaseActivity.this;
	@Override
	protected void onResume() {
		super.onResume();
		//每次不销毁只执行一次
		if(start){
			start=false;
			getSimpleActionBar(true).setBackIcon();
		}
	}
}
