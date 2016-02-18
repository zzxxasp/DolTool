package com.key.doltool.activity.adventure;

import android.app.Dialog;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.adapter.NPCAdapter;
import com.key.doltool.data.sqlite.NPCInfo;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

public class NPCFragment extends BaseFragment implements OnScrollListener{
	//定义部分
	private Dialog layout_alert;
	//船只列表页面
	private ListView listview;
	//数据temp变量
	private DefaultDAO dao;
	private List<NPCInfo> list=new ArrayList<>();
	private NPCAdapter adapter;
	private int add=-30;
	private Thread mThread;	// 线程
	private boolean end_flag=true; //是否为最末标记
	//查询条件
	private String select_if="id>?";
    private String[] select_if_x={"0"};
	//创建Activity
    private View main;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.common_list, container,false);
		 init(view);
		 findView();
		 setListener();
		 if(dao!=null&&list.size()==0){
			 new Thread(mTasks).start();
		 }else{
			 if(!getActivity().isFinishing()){
				 layout_alert.dismiss();
			 }
		 }
		 return view;
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
	
	private void init(View view){
		main=view;
		dao=SRPUtil.getDAO(getActivity());
	}
	
	//通用findView
	private void findView() {
		initPage();
		layout_alert=new DialogEvent().showLoading(getActivity());
		layout_alert.show();
	}
	//通用Listener
	private void setListener() {
		BaseFragmentActivity a=(BaseFragmentActivity)getActivity();
		a.toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	private void initPage(){
		initPageItem();
	}
	private void initPageItem(){
		listview=(ListView)main.findViewById(R.id.listview);
		adapter=new NPCAdapter(list,getActivity());
		listview.setOnScrollListener(this);
		listview.setAdapter(adapter);
	}
	public void onDestroy() {
		dao=null;
		super.onDestroy();
	}
	@SuppressWarnings("unchecked")
	//有限数据查询
	private void selectshow(String limit){
		if(dao==null){
			return;
		}
		//数据前后记录
		int size_before,size_after;
			size_before=list.size();
			list.addAll(((List<NPCInfo>) dao.select(NPCInfo.class, false,select_if, select_if_x, 
				null, null,null,limit)));
			size_after=list.size();
		//数据返回判断
		if(size_after<NPCAdapter.SIZE){
			//表示，小于
			end_flag=false;
		}
    	if(size_after==size_before&&size_after!=0){
    		end_flag=false;
    		Toast.makeText(getActivity(),"已经返回所有查询结果了", Toast.LENGTH_LONG).show();
    	}else if(size_after==0){
    		Toast.makeText(getActivity(),"没有查到您想要的结果", Toast.LENGTH_LONG).show();
    	}
	}
	//数据添加
	private void change(){
		//1.为船只信息，2.为配件信息
		add+=NPCAdapter.SIZE;
		selectshow(add + "," + NPCAdapter.SIZE);
		adapter.notifyDataSetChanged();
	}
	private void jump(){
		View xc=getActivity().getLayoutInflater().inflate(R.layout.select_npc, null);
		ViewUtil.popNPCDialog(this,xc);
	}
	private void findObject(){
		new AreaEvent().showCityDialog(this,dao);
	}
	//修改查询条件
	public void change_if(String if_s,String if_args){
		//初始化所有数据
		select_if=if_s;
		select_if_x=new String[1];
		select_if_x[0]=if_args;
		list.clear();
		add=0;
		selectshow("0,"+NPCAdapter.SIZE);
		//重新setAdapter
		adapter=new NPCAdapter(list,getActivity());
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
		selectshow("0,"+NPCAdapter.SIZE);
		//重新setAdapter
		adapter=new NPCAdapter(list,getActivity());
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
					Toast.makeText(getActivity(),"重置搜索条件", Toast.LENGTH_SHORT).show();
				}
			}
		}
		 return true;
	}
	//滚动监听① - useless
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	//滚动监听②
	public void onScrollStateChanged(final AbsListView view, int scrollState) {
        //当不滚动时
		boolean flag;
		flag=end_flag;
        if(scrollState == SCROLL_STATE_IDLE){  
                System.out.println(view.getFirstVisiblePosition()+"===" + view.getLastVisiblePosition()+"==="+view.getCount());
                //判断滚动到底部   
                if(view.getLastVisiblePosition()==(view.getCount()-1)){
                	//没有线程且不为最末时
                    if ((mThread == null || !mThread.isAlive())&&flag) {
                    	//显示进度条，区域操作控制
						if(!getActivity().isFinishing()){
							layout_alert.show();
						}
                        mThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }  
                        };
                        mThread.start();                
                    }
                }
        	}
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
		inflater.inflate(R.menu.adc_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}