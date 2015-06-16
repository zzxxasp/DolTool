package com.key.doltool.activity;


import android.view.View;

import com.key.doltool.R;
import com.key.doltool.util.ViewUtil;

/**
 * BaseActivity
 * @author key
 * @version 0.2
 */
public abstract class BaseAdventureActivity extends BaseActivity{
	public String select_txt="";
	public void getSelectText(String txt){
		select_txt=txt;
	}
	public void popWindow(){
		//对话框显示，输入返回搜索条件
		View xc=getLayoutInflater().inflate(R.layout.select_trove, null);
		ViewUtil.popDialog(this,xc);
	}
	public void select(String select){
		select_txt=select;
	}
}
