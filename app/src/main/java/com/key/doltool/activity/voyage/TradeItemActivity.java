package com.key.doltool.activity.voyage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.adapter.TradeListAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import butterknife.BindView;

public class TradeItemActivity extends BaseActivity{
	@BindView(R.id.listview) GridView listview;
	//定义部分
	private Dialog alert;
	//数据temp变量
	private TradeListAdapter adapter;
	private ViewHandler viewHandler;
	private ListFlowHelper<TradeItem> listFlowHelper;
	@Override
	public int getContentViewId() {
		return R.layout.trade_list_toolbar;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle("交易品");
		findView();
		setListener();
		listFlowHelper.getExtra(getIntent());
		if(listFlowHelper.list.size()==0){
			new Thread(mTasks).start();
		}else{
			DialogUtil.dismiss(this,alert);
		}
	}

	private Runnable mTasks =new Runnable(){
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            Message message = new Message();
            message.what = 0;
			viewHandler.sendMessage(message);
		}
	};

	//通用findView
	private void findView() {
		//基本设置
		alert=new DialogEvent().showLoading(context);
		DialogUtil.show(context,alert);
		listFlowHelper=new ListFlowHelper<>(TradeItem.class, context, new ListFlowHelper.ListFlowCallBack() {
			@Override
			public void showSelectToast(String msg) {
				Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
			}

			@Override
			public void setAdapter() {
				adapter=new TradeListAdapter(listFlowHelper.list,context);
				listview.setAdapter(adapter);
			}

			@Override
			public void updateAdapter() {
				adapter.notifyDataSetChanged();
			}
		}, TradeListAdapter.SIZE);

		viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
			@Override
			public void onHandleMessage(Message msg) {
				switch (msg.what){
					case 0:
						listFlowHelper.change(true);
						break;
					case 1:
						listFlowHelper.change();
						break;
				}
				DialogUtil.dismiss(context,alert);
			}
		});
		initPage();
	}
	//通用Listener
	private void setListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Intent it = new Intent(TradeItemActivity.this, TradeDetailActivity.class);
				it.putExtra("id", adapter.getItem(arg2).getId()+"");
				startActivity(it);
			}
		});
	}
	private void initPage(){
		initPageItem();
	}
	private void initPageItem(){
		adapter=new TradeListAdapter(listFlowHelper.list,context);
		ListScrollListener scrollListener = new ListScrollListener(alert,viewHandler,context);
		listFlowHelper.setListScrollListener(scrollListener);
		listview.setOnScrollListener(scrollListener);
		listview.setAdapter(adapter);
	}

	private void jump(){
		View xc=getLayoutInflater().inflate(R.layout.select_trade, null);
		ViewUtil.popTradeDialog(listFlowHelper,context,xc);
	}
	private void findObject(){
		DefaultDAO dao= SRPUtil.getDAO(context);
		new AreaEvent().showCityDialog(this,dao);
	}

	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(listFlowHelper.isChange()){
			return super.onKeyDown(keyCode,event);
		}
		//其他
		else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK) {
				//条件不是初始状态就重置
				listFlowHelper.reback();
			}
		}
		return true;
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:findObject();break;
				case R.id.type_search:jump();break;

			}
			return true;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.adc_menu, menu);
		return true;
	}
}