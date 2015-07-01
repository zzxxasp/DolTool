package com.key.doltool.activity.help;


import android.os.Bundle;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;

public class HintListActivity extends BaseActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_hint_main);
		flag=false;
		initToolBar(null);
		toolbar.setTitle("应用贴士");
	}
}
