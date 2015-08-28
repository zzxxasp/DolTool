package com.key.doltool.activity.adventure;

import android.os.Bundle;
import android.util.Log;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.Card;
import com.key.doltool.data.CardCombo;
import com.key.doltool.data.Trove;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * 全卡片预览，提供搜索和选择
 */
public class CardListActivity extends BaseActivity{
	private List<Card> list=new ArrayList<>();
	private DefaultDAO dao;
	private SRPUtil srp;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adc_details);
		srp=SRPUtil.getInstance(getApplicationContext());
		dao=SRPUtil.getDAO(getApplicationContext());
		findView();
	}
	//初始化控件
	private void findView(){

	}
}