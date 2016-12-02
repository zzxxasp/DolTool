package com.key.doltool.activity.adventure.card;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.adapter.CardComboAdapter;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.data.sqlite.CardCombo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CardComboFragment extends BaseFragment{
	//定义部分
	private Dialog layout_alert;
	//船只列表页面
	@BindView(R.id.listview) ListView listview;
	//数据temp变量
	private SRPUtil dao;
	private List<CardCombo> list=new ArrayList<>();
	private CardComboAdapter adapter;
	private int add=-30;
	private boolean end_flag=true; //是否为最末标记
	//查询条件
	private String select_if="id>?";
    private String[] select_if_x={"0"};
	//创建Activity
	private Activity context;
	private ListScrollListener scrollListener;

	@Override
	public int getContentViewId() {
		return R.layout.common_list;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		context=getActivity();
		init();
		findView();
		setListener();
		if(dao!=null&&list.size()==0){
			new Thread(mTasks).start();
		}else{
			if(!getActivity().isFinishing()){
				layout_alert.dismiss();
			}
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
            message.what = 1;
            handler.sendMessage(message);
		}
	};

	private void init(){
		dao=SRPUtil.getInstance(context);
		BaseFragmentActivity a=(BaseFragmentActivity)context;
		a.toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}

	//通用findView
	private void findView() {
		layout_alert=new DialogEvent().showLoading(getActivity());
		layout_alert.show();
		initPage();
	}
	//通用Listener
	private void setListener() {

	}
	private void initPage(){
		//初始化边缘栏
		initPageItem();
	}
	private void initPageItem(){
		adapter=new CardComboAdapter(list,context);
		scrollListener = new ListScrollListener(end_flag,layout_alert, handler);
		listview.setOnScrollListener(scrollListener);
		listview.setAdapter(adapter);
	}
	public void onDestroy() {
		dao=null;
		super.onDestroy();
	}
	//有限数据查询
	private void selectshow(String limit){
		if(dao==null){
			return;
		}
		//数据前后记录
		int size_before,size_after;
			size_before=list.size();
			list.addAll((dao.select(CardCombo.class, false,select_if, select_if_x,
				null, null,null,limit)));
			size_after=list.size();
		//数据返回判断
		if (size_after < CardComboAdapter.SIZE) {
			//表示，小于
			end_flag = false;
			scrollListener.changeFlag(false);
		}
    	if(size_after==size_before&&size_after!=0){
    		end_flag=false;
			scrollListener.changeFlag(false);
    		Toast.makeText(context.getApplicationContext(),R.string.search_no_more, Toast.LENGTH_LONG).show();
    	}else if(size_after==0){
    		Toast.makeText(context.getApplicationContext(),R.string.search_no, Toast.LENGTH_LONG).show();
    	}
	}
	//数据添加
	private void change(){
		//1.为船只信息，2.为配件信息
		add+=SailBoatListAdapter.SIZE;
		selectshow(add + "," + SailBoatListAdapter.SIZE);
		adapter.notifyDataSetChanged();
	}
	private void jump(){
		Intent intent=new Intent(getActivity(),CardListActivity.class);
		startActivity(intent);
	}
	private void findObject(){
		View xc=getActivity().getLayoutInflater().inflate(R.layout.select_cardcombo, null);
		ViewUtil.popCardDialog(this, xc);
	}
	//修改查询条件
	public void change_if(String if_s,String if_args){
		//初始化所有数据
		select_if=if_s;
		select_if_x=new String[1];
		select_if_x[0]=if_args;
		list.clear();
		add=0;
		selectshow("0,"+SailBoatListAdapter.SIZE);
		//重新setAdapter
		adapter=new CardComboAdapter(list,getActivity());
		listview.setAdapter(adapter);
	}
	//修改查询条件
	public void change_if(String if_s,List<String> if_args){
		//初始化所有数据
		select_if=if_s;
		select_if_x=new String[if_args.size()];
		for(int i=0;i<select_if_x.length;i++){
			select_if_x[i]=if_args.get(i);
		}
		list.clear();
		add=0;
		selectshow("0,"+SailBoatListAdapter.SIZE);
		//重新setAdapter
		adapter=new CardComboAdapter(list,getActivity());
		listview.setAdapter(adapter);
	}
	//重置最末尾标记
	public void begin(){
		end_flag=true;
	}
	
	/**
	 * 华丽的分割线——以下是Handler,线程,系统按键等处理
	 */
	//Handler——线程结束后更新界面
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			change();
			if(!getActivity().isFinishing()){
				layout_alert.dismiss();
			}
		}
	};
	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(select_if.equals("id>?")){
			return false;
		}
		//其他
		else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK)
			{
				//条件不是初始状态就重置
				if(!select_if.equals("id>?")){
					end_flag=true;
					change_if("id>?","0");
					Toast.makeText(context.getApplicationContext(), R.string.search_rest, Toast.LENGTH_SHORT).show();
				}
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