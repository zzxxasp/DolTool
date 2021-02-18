package com.key.doltool.activity;

import android.app.Activity;
import android.os.Bundle;

import com.key.doltool.activity.core.BaseFragmentActivity;

import butterknife.ButterKnife;

/**
 * BaseActivity
 * @author key
 * @version 0.3
 */
public abstract class BaseActivity extends BaseFragmentActivity{
	public Activity context=BaseActivity.this;
	public abstract int getContentViewId();
	public boolean flag=true;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		ButterKnife.bind(this);
		initAllMembersView(savedInstanceState);
	}

	protected abstract void initAllMembersView(Bundle savedInstanceState);

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		if(flag){
			initToolBar(null);
		}
		super.onResume();
	}
}
