package com.key.doltool.activity.wiki;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.DockYardMenuAdapter;
import com.key.doltool.adapter.WikiAdapter;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.WikiInfo;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SlideHolder;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

public class WikiListActivity extends BaseActivity implements OnScrollListener{
		//定义部分
		private LinearLayout layout_alert;
		private ImageView main_menu;
		private TextView main_title;
		//侧边栏
		private SlideHolder mSlideHolder;
		private ListView menu_list;
		//船只列表页面
		private ListView listview;
		
		//数据temp变量
		private DefaultDAO dao;
		private List<WikiInfo> list=new ArrayList<>();
		private WikiAdapter adapter;
		private int add=0;
		private Thread mThread;	// 线程
		private boolean end_flag=true; //是否为最末标记
		//查询条件
		private String select_if="id>?";
	    private String[] select_if_x={"0"};
		//创建Activity
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.card_combo_main);
	        dao=SRPUtil.getDAO(context);
			selectshow("0,"+WikiAdapter.SIZE);
	        findView();
	        setListener();
	    }
		//通用findView
		private void findView() {
			initPage();
			main_title=(TextView)findViewById(R.id.main_title);
			main_title.setText("大航海百科");
			mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
			layout_alert=(LinearLayout)findViewById(R.id.layout_alert);
			main_menu=(ImageView)findViewById(R.id.main_menu);
		}
		//通用Listener
		private void setListener() {
			main_menu.setVisibility(View.GONE);
			main_menu.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mSlideHolder.toggle();
				}
			});
			listview.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent it=new Intent(WikiListActivity.this,WikiMainActivity.class);
					it.putExtra("id",list.get(arg2).getId()+"");
					startActivity(it);
				}
			});
		}
		private void initPage(){
			//初始化边缘栏
			initMenu();
			initPageItem();
		}
		private void initPageItem(){
			listview=(ListView)findViewById(R.id.listview);
			adapter=new WikiAdapter(list,this);
			listview.setOnScrollListener(this);
			listview.setAdapter(adapter);
		}
		protected void onDestroy() {
			dao=null;
			if(mSlideHolder.mCachedBitmap!=null){
				mSlideHolder.mCachedBitmap.recycle();
				mSlideHolder.mCachedBitmap=null;
				System.gc();
			}
			super.onDestroy();
		}
		protected void onResume() {
			super.onResume();
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
				list.addAll(((List<WikiInfo>) dao.select(WikiInfo.class, false,select_if, select_if_x, 
					null, null,null,limit)));
				size_after=list.size();
			//数据返回判断
			if(size_after<WikiAdapter.SIZE){
				//表示，小于
				end_flag=false;
			}
	    	if(size_after==size_before&&size_after!=0){
	    		end_flag=false;
	    		Toast.makeText(getApplicationContext(),"已经返回所有查询结果了", Toast.LENGTH_LONG).show();
	    	}else if(size_after==0){
	    		Toast.makeText(getApplicationContext(),"没有查到您想要的结果", Toast.LENGTH_LONG).show();
	    	}
		}
		//数据添加
		private void change(){
			//1.为船只信息，2.为配件信息
			add+=WikiAdapter.SIZE;
			selectshow(add+","+	WikiAdapter.SIZE);
			adapter.notifyDataSetChanged();
		}
		//初始化边缘菜单栏
		private void initMenu(){
			menu_list=(ListView)findViewById(R.id.menu_list);
			List<MenuItem> list=new ArrayList<>();
			ViewUtil.setList(list,6);
			menu_list.setAdapter(new DockYardMenuAdapter(list,this));
			menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					mSlideHolder.toggle();
					switch(position){
						case 0:findObject();break;
						case 1:jump();break;
					}		
				}
			});
		}
		private void jump(){
			View xc=getLayoutInflater().inflate(R.layout.select_npc, null);
			ViewUtil.popWikiDialog(this,xc);
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
			selectshow("0,"+WikiAdapter.SIZE);
			//重新setAdapter
			adapter=new WikiAdapter(list,this);
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
			selectshow("0,"+WikiAdapter.SIZE);
			//重新setAdapter
			adapter=new WikiAdapter(list,this);
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
					layout_alert.setVisibility(View.GONE);
					ViewUtil.disableSubControls(mSlideHolder, true);
			 }
		 };

		//系统按键监听覆写
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 //菜单键覆写，调用边缘栏菜单
			 if(keyCode==KeyEvent.KEYCODE_MENU){
				 mSlideHolder.toggle();
				 return true;
			 }
			 //条件:当菜单未关闭且搜索条件为初始态，允许退出
			if(select_if.equals("id>?")&&!mSlideHolder.isOpened()){
				super.onKeyDown(keyCode, event);
			}
			//其他
			else{
				//按键返回
				if(keyCode==KeyEvent.KEYCODE_BACK)
				{
					//开启就关闭
					if(mSlideHolder.isOpened()){
						mSlideHolder.toggle();
					}
					//条件不是初始状态就重置
					if(!select_if.equals("id>?")){
						end_flag=true;
						change_if("id>?","0");
						Toast.makeText(getApplicationContext(),"重置搜索条件", Toast.LENGTH_SHORT).show();
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
	                    	layout_alert.setVisibility(View.VISIBLE);
	                    	ViewUtil.disableSubControls(mSlideHolder, false);
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
}
