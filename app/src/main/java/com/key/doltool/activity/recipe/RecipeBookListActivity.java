package com.key.doltool.activity.recipe;

import android.app.Dialog;
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
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.BookAdapter;
import com.key.doltool.adapter.SkillAdapter;
import com.key.doltool.data.sqlite.Book;
import com.key.doltool.event.BookEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookListActivity extends BaseActivity implements OnScrollListener{
		//定义部分
		private Dialog alert;
		//列表
		private ListView listview;
		private DefaultDAO dao;
		private List<Book> list=new ArrayList<>();
		private BookAdapter adapter;
		private int add=0;
		private Thread mThread;	// 线程
		private boolean end_flag=true; //是否为最末标记
		//查询条件
		private String select_if="type=?";
	    private String[] select_if_x={"0"};
	    private String type_id="";
	    private String order="name desc";
		//创建Activity
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.card_combo_main);
			initToolBar(null);
	        dao=SRPUtil.getDAO(this);
			getExtra();//外部搜索链接参数处理
			type_id=getIntent().getIntExtra("types",0)+"";
			select_if_x[0]=type_id;
			selectshow("0,"+SkillAdapter.SIZE);
	        findView();
	        setListener();
	    }
		private void getExtra(){
			if(!StringUtil.isNull(getIntent().getStringExtra("type"))){
				select_if=getIntent().getStringExtra("type");
				select_if_x[0]=getIntent().getStringExtra("args");
			}
		}
		//通用findView
		private void findView() {
			initPage();
			toolbar.setTitle(new BookEvent().TITLE[getIntent().getIntExtra("type",0)]);
			alert=new DialogEvent().showLoading(this);
		}
		//通用Listener
		private void setListener() {
			listview.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent it=new Intent(RecipeBookListActivity.this,RecipeForBookDetailsActivity.class);
					it.putExtra("id",list.get(arg2).getId()+"");
					startActivity(it);
				}
			});
		}
		private void initPage(){
			initPageItem();
		}
		private void initPageItem(){
			listview=(ListView)findViewById(R.id.listview);
			adapter=new BookAdapter(list,this);
			listview.setOnScrollListener(this);
			listview.setAdapter(adapter);
		}
		protected void onDestroy() {
			dao=null;
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
				list.addAll(((List<Book>) dao.select(Book.class, false,select_if, select_if_x, 
					null, null,order,limit)));
				size_after=list.size();
			//数据返回判断
			if(size_after<SkillAdapter.SIZE){
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
			add+=SkillAdapter.SIZE;
			selectshow(add+","+	SkillAdapter.SIZE);
			adapter.notifyDataSetChanged();
		}
		//修改查询条件
		public void change_if(String if_s,String if_args){
			//初始化所有数据
			select_if=if_s;
			select_if_x=new String[1];
			select_if_x[0]=if_args;
			list.clear();
			add=0;
			selectshow("0,"+SkillAdapter.SIZE);
			//重新setAdapter
			adapter=new BookAdapter(list,this);
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
			selectshow("0,"+SkillAdapter.SIZE);
			//重新setAdapter
			adapter=new BookAdapter(list,this);
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
				 alert.dismiss();
			 }
		 };

		//系统按键监听覆写
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 //条件:当菜单未关闭且搜索条件为初始态，允许退出
			if(select_if.equals("type=?")){
				super.onKeyDown(keyCode, event);
			}
			//其他
			else{
				//按键返回
				if(keyCode==KeyEvent.KEYCODE_BACK)
				{
					//条件不是初始状态就重置
					if(!select_if.equals("type=?")){
						end_flag=true;
						change_if("type=?",type_id+"");
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
							alert.show();
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
