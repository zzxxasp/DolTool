package com.key.doltool.activity.wiki;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.WikiAdapter;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.WikiInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.view.Toast;

import butterknife.BindView;

public class WikiListActivity extends BaseActivity{
	//定义部分
	private Dialog alert;
	//船只列表页面
	@BindView(R.id.listview) ListView listview;
	//数据temp变量
	private WikiAdapter adapter;
	private ListFlowHelper<WikiInfo> listFlowHelper;
	private ViewHandler viewHandler;
	@Override
	public int getContentViewId() {
		return R.layout.card_combo_main;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		listFlowHelper=new ListFlowHelper<>(WikiInfo.class,
				getApplicationContext(), new ListFlowHelper.ListFlowCallBack() {
			@Override
			public void showSelectToast(String msg) {
				Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
			}

			@Override
			public void setAdapter() {
				adapter=new WikiAdapter(listFlowHelper.list,context);
				listview.setAdapter(adapter);
			}

			@Override
			public void updateAdapter() {
				adapter.notifyDataSetChanged();
			}
		}, WikiAdapter.SIZE);
		findView();
		setListener();
	}

	//通用findView
	private void findView() {
		flag=false;
		initToolBar(null);
		toolbar.setTitle("大航海百科");
		alert=new DialogEvent().showLoading(this);
		viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
			@Override
			public void onHandleMessage(Message msg) {
				listFlowHelper.change();
				alert.dismiss();
			}
		});
		initPage();
	}
	//通用Listener
	private void setListener() {
		listview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
				Intent it=new Intent(WikiListActivity.this,WikiMainActivity.class);
				it.putExtra("id",adapter.getItem(arg2).getId()+"");
				startActivity(it);
			}
		});
	}
	private void initPage(){
		initPageItem();
	}
	private void initPageItem(){
		adapter=new WikiAdapter(listFlowHelper.list,context);
		listview.setAdapter(adapter);
		ListScrollListener listScrollListener=new ListScrollListener(true,alert,viewHandler);
		listview.setOnScrollListener(listScrollListener);
		listFlowHelper.setListScrollListener(listScrollListener);
		listFlowHelper.selectshow("0,"+WikiAdapter.SIZE);
	}

	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(listFlowHelper.isChange()){
			super.onKeyDown(keyCode, event);
		}
		//其他
		else{
			if(keyCode==KeyEvent.KEYCODE_BACK){
				//条件不是初始状态就重置
				listFlowHelper.reback();
			}
		}
		return true;
	}
}
