package com.key.doltool.activity;
import android.app.Activity;

import com.key.doltool.activity.core.BaseFragmentActivity;
/**
 * BaseActivity
 * @author key
 * @version 0.3
 */
public abstract class BaseActivity extends BaseFragmentActivity{
	public Activity context=BaseActivity.this;
	public boolean flag=true;
	@Override
	protected void onResume() {
		if(flag){
			initToolBar(null);
		}
		super.onResume();
	}
}
