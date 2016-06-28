package com.key.doltool.activity;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragmentActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 * @author key
 * @version 0.3
 */
public abstract class BaseActivity extends BaseFragmentActivity{
	public Activity context=BaseActivity.this;
	public abstract int getContentViewId();
	public boolean flag=true;
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
