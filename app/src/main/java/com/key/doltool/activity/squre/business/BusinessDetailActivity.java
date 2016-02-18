package com.key.doltool.activity.squre.business;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.HttpUtil;
import com.key.doltool.util.jsoup.JsoupForTB;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

/**
 * 新闻详情
 * @author key
 * @version 0.5
 * @date 2013-7-17
 * 0.5-基本调整,界面适配度80%<br>
 */
@SuppressLint("NewApi")
public class BusinessDetailActivity extends BaseActivity{
	private WebView web_content;
	private Dialog layout_alert;
	private String id="4157094114";
	private String content="";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initToolBar(null);
		setContentView(R.layout.news_detail);
		findView();
		setListener();
		id="4157094114";
//		id=getIntent().getStringExtra("id");
		new Thread(mTask).start();
	}
	//获取数据，填充显示
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void init(){
		float screenDensity = getResources().getDisplayMetrics().density ; 
		WebSettings.ZoomDensity zoomDensity;
		if(screenDensity==0.75f){
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
		}
		else if(screenDensity==1.0f){
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		}
		else{
			zoomDensity = WebSettings.ZoomDensity.FAR;
		}
		Log.i("",zoomDensity.name());
		web_content.getSettings().setJavaScriptEnabled(true);
		web_content.getSettings().setDefaultZoom(zoomDensity);
		web_content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		web_content.setWebChromeClient(new WebChromeClient());
		int width = (int) (CommonUtil.getScreenWidth(this) / screenDensity) - 10;
		Log.i("x", "" + width);
		String head = "<style type='text/css'>*{max-width:" + width + "px!important;height:auto!important;}" +
				"table{width:100%!important;}" + "</style>";
//		web_content.loadUrl(JsoupForTB.TB_BASE_URL + id);
		web_content.loadData(head + content, "text/html; charset=UTF-8",null);
	}
	//初始化控件
	private void findView(){
		web_content=(WebView)findViewById(R.id.content);
		layout_alert=new DialogEvent().showLoading(this);
		layout_alert.show();
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}
	//事件监听
	private void setListener(){
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//更新页面
			if(HttpUtil.STATE==0){
				init();
				if(!content.equals(""))
					layout_alert.dismiss();
			}
			else{
				layout_alert.dismiss();
			}
		}
	 };
	 //获得数据
	private Runnable mTask=new Runnable(){
		public void run() {
			while(content.equals("")&&HttpUtil.STATE==0){
				content=JsoupForTB.getData(id);
//				 JsoupForTB.TB_BASE_URL+id;
				mHandler.sendMessage(mHandler.obtainMessage());
			}
			if(HttpUtil.STATE==1&&layout_alert.isShowing()){
				mHandler.sendMessage(mHandler.obtainMessage());
			}
		}
	};
	//缓存清空
	protected void onDestroy() {
		super.onDestroy();
		web_content.clearCache(false);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}