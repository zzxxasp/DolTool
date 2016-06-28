package com.key.doltool.activity.help;


import android.os.Bundle;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;

public class HintListActivity extends BaseActivity{
	@Override
	public int getContentViewId() {
		return R.layout.help_hint_main;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
		toolbar.setTitle("应用贴士");
	}
}
