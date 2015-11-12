package com.key.doltool.activity.infobroad;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.NewsAdapter;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.HttpUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.jsoup.JsoupForTX;
import com.key.doltool.view.SlidingTabLayout;
import com.key.doltool.viewpage.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * 新闻看板界面
 * @author key
 * @version 0.3
 * @time 2013-7-3
 * @日志
 * 0.1-加入基本Activity的相关方法和操作<br>
 * 0.2-加入ViewPager负责进行多个页面的切换，并加入显示效果<br>
 * 0.3-加入有限ListView的展示<br>
 * 0.4-加入进入动画
 * 0.5-网络调整
 */
public class MainBroadFragment extends BaseFragment{
		//ViewPager定义部分
		private ViewPager main_ViewPage;
		private MyPagerAdapter main_adapter;
		private View layout1,layout2,layout3;
		private List<View> main_list;
		private LayoutInflater mInflater;
		//add-非共通部分
		private Dialog alert;
		private LinearLayout main;
		private ListView listview1,listview2,listview3; 
		//创建Activity
	    private View main_view;
		private SlidingTabLayout mSlidingTabLayout;
		public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
			 View view =  inflater.inflate(R.layout.news_main_area,container,false);
			 init(view);
			 findView();
			 setListener();
			 new Thread(mTask).start();
			 return view; 
		}
		private void init(View view){
			main_view=view;
		}		
		private void findView() {
			initPage();
			main=(LinearLayout)main_view.findViewById(R.id.main);
			alert=new DialogEvent().showLoading(getActivity());
			if(JsoupForTX.list3.size()!=0){
				alert.dismiss();
			}
		}
		private void setListener() {
		}
		private void initPage(){
			//初始化layout相关
			main_list = new ArrayList<>();
			mInflater = getActivity().getLayoutInflater();
			//添加布局文件
			layout1 = mInflater.inflate(R.layout.news_main_item_layout, null);
			layout2 = mInflater.inflate(R.layout.news_main_item_layout, null);
			layout3 =mInflater.inflate(R.layout.news_main_item_layout, null);
			main_list.add(layout1);
			main_list.add(layout2);
			main_list.add(layout3);
			//初始化ViewPager相关
			main_adapter = new MyPagerAdapter(main_list,new String[]{"新闻","活动","公告"});
			main_ViewPage = (ViewPager)main_view.findViewById(R.id.main_viewpagers);
			main_ViewPage.setAdapter(main_adapter);
			main_ViewPage.setCurrentItem(0);
			//初始化PageItem
			initPageItem();
			//初始化PageEvent相关
			mSlidingTabLayout=(SlidingTabLayout)main_view.findViewById(R.id.sliding_tabs);
			mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.White));
			mSlidingTabLayout.setBackgroundResource(R.drawable.theme_dark_blue);
			mSlidingTabLayout.setViewPager(main_ViewPage);
		}
		private void initPageItem(){
			//初始化第一页(船只列表)
			listview1=(ListView)layout1.findViewById(R.id.listview);
			listview1.setAdapter(new NewsAdapter(JsoupForTX.list1, getActivity()));
			//初始化第二页(造船模拟)
			listview2=(ListView)layout2.findViewById(R.id.listview);
			listview2.setAdapter(new NewsAdapter(JsoupForTX.list2, getActivity()));
			//初始化第三页(船只配件)
			listview3=(ListView)layout3.findViewById(R.id.listview);
			listview3.setAdapter(new NewsAdapter(JsoupForTX.list3, getActivity()));
		}
		
	 private  Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//更新页面
			if(HttpUtil.STATE==0){
				initPageItem();
				if(JsoupForTX.list3.size()!=0)
					alert.dismiss();
			}
			else{
				alert.dismiss();
			}
			ViewUtil.disableSubControls(main, true);
		}
	 };
	 //获得数据
	 private Runnable mTask=new Runnable(){
		public void run() {
			Looper.prepare(); 
			while(JsoupForTX.list3.size()==0&&HttpUtil.STATE==0){
				JsoupForTX.getUrl();
				mHandler.sendMessage(mHandler.obtainMessage());
			}
		    if(HttpUtil.STATE==1&&alert.isShowing()){
				mHandler.sendMessage(mHandler.obtainMessage());
			}
            Looper.loop(); 
		}
	 };
}
