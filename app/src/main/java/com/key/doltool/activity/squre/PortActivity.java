package com.key.doltool.activity.squre;

import android.os.Bundle;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;

/**
 * 定期船
 * @author key
 * @version 1.0
 **/
public class PortActivity extends BaseActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dol_map);
		flag=false;
		initToolBar(null);
		toolbar.setTitle("定期船");
	}

	private void init(){

	}
}