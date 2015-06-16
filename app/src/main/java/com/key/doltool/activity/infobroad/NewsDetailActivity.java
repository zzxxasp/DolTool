package com.key.doltool.activity.infobroad;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.HttpUtil;
import com.key.doltool.util.jsoup.JsoupForTX;
/**
 * 新闻详情
 * @author key
 * @version 0.5
 * @time 2013-7-17
 * @日志
 * 0.5-基本调整,界面适配度80%<br>
 */
@SuppressLint("NewApi")
public class NewsDetailActivity extends BaseActivity{
	private WebView web_content;
	private LinearLayout layout_alert;
	private ImageView menu;
	private String url="";
	private String content="";
	private int width=480;
	private String head="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		findView();
		setListener();
		url=getIntent().getStringExtra("url");
		new Thread(mTask).start();
	}
	//获取数据，填充显示
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void init(){
		float screenDensity = getResources().getDisplayMetrics().density ; 
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ; 
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
		//如果是页面则设置缩放
		if(HttpUtil.STATE==3){
			web_content.getSettings().setBuiltInZoomControls(true);
			web_content.getSettings().setDisplayZoomControls(false);
			web_content.getSettings().setUseWideViewPort(true);
		}
		web_content.getSettings().setDefaultZoom(zoomDensity);
		web_content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		width=(int)(CommonUtil.getScreenWidth(this)/screenDensity)-10;
		Log.i("x",""+width);
		head="<style type='text/css'>*{max-width:"+width+"px!important;height:auto!important;}" +
				"table{width:100%!important;}"+"</style>";
		if(HttpUtil.STATE==3){
			web_content.loadUrl(content);
		}else{
			web_content.loadData(head+content,"text/html; charset=UTF-8",null);
		}
	}
	//初始化控件
	private void findView(){
		web_content=(WebView)findViewById(R.id.content);
		layout_alert=(LinearLayout)findViewById(R.id.layout_alert);
		menu=(ImageView)findViewById(R.id.main_menu);
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}
	//事件监听
	private void setListener(){
		menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//截图保存
				Bitmap map=BitMapUtil.webView2Bitmap(web_content);
				BitMapUtil.savepic(map,101);
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	 private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//更新页面
			if(HttpUtil.STATE==0||HttpUtil.STATE==3){
				init();
				if(!content.equals(""))
					layout_alert.setVisibility(View.GONE);
			}
			else{
				layout_alert.setVisibility(View.GONE);
			}
		}
	 };
	 //获得数据
	 private Runnable mTask=new Runnable(){
		public void run() {
			Looper.prepare(); 
			while(content.equals("")&&HttpUtil.STATE==0){
				content=JsoupForTX.getNews(url, NewsDetailActivity.this);
				mHandler.sendMessage(mHandler.obtainMessage());
			}
		    if(HttpUtil.STATE==1&&layout_alert.getVisibility()==View.VISIBLE){
				mHandler.sendMessage(mHandler.obtainMessage());
			}
           Looper.loop(); 
		}
	 };
	 //缓存清空
	 protected void onDestroy() {
		 super.onDestroy();
		 web_content.clearCache(false);
	 };
	 @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}