package com.key.doltool.activity.adventure.card;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.adapter.CardComboAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.CardCombo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.view.Toast;

import butterknife.BindView;

public class CardComboFragment extends BaseFragment{
	//定义部分
	private Dialog layout_alert;
	//船只列表页面
	@BindView(R.id.listview) ListView listview;
	private CardComboAdapter adapter;
	private ListFlowHelper<CardCombo> listFlowHelper;
	private ViewHandler viewHandler;
	@Override
	public int getContentViewId() {
		return R.layout.common_list;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		init();
		findView();
		if(listFlowHelper.list.size()==0){
			new Thread(mTasks).start();
		}else{
			DialogUtil.dismiss(context,layout_alert);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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

	private void init(){
		BaseFragmentActivity a=(BaseFragmentActivity)context;
		a.toolbar.setOnMenuItemClickListener(onMenuItemClick);

		listFlowHelper=new ListFlowHelper<>(CardCombo.class, context, new ListFlowHelper.ListFlowCallBack() {
			@Override
			public void showSelectToast(String msg) {
				Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void setAdapter() {
				adapter = new CardComboAdapter(listFlowHelper.list, context);
				listview.setAdapter(adapter);
			}

			@Override
			public void updateAdapter() {
				adapter.notifyDataSetChanged();
			}
		}, CardComboAdapter.SIZE);
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
				DialogUtil.dismiss(getActivity(),layout_alert);
			}
		});
	}

	//通用findView
	private void findView() {
		layout_alert=new DialogEvent().showLoading(context);
		DialogUtil.show(getActivity(),layout_alert);
		initPageItem();
	}

	private void initPageItem(){
		adapter=new CardComboAdapter(listFlowHelper.list,context);
		ListScrollListener scrollListener = new ListScrollListener(layout_alert, viewHandler, context);
		listFlowHelper.setListScrollListener(scrollListener);
		listview.setOnScrollListener(scrollListener);
		listview.setAdapter(adapter);
	}

	private void jump(){
		Intent intent=new Intent(getActivity(),CardListActivity.class);
		startActivity(intent);
	}

	private void findObject(){
		View xc=getActivity().getLayoutInflater().inflate(R.layout.select_cardcombo, null);
		ViewUtil.popCardDialog(listFlowHelper,context, xc);
	}

	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(listFlowHelper.isChange()){
			return false;
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.common_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}