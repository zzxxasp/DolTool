package com.key.doltool.activity.infobroad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.HttpUtil;
import com.key.doltool.util.jsoup.JsoupForTX;

import butterknife.BindView;

/**
 * 新闻详情
 * @author key
 * @version 0.5
 * @date 2013-7-17
 * 0.5-基本调整,界面适配度80%<br>
 */
@SuppressLint("NewApi")
public class NewsDetailActivity extends BaseActivity{
	@BindView(R.id.content) WebView web_content;
	private Dialog layout_alert;
	private String url="";
	private String content="";
	private ViewHandler viewHandler;
	@Override
	public int getContentViewId() {
		return R.layout.news_detail;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
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
		//如果是页面则设置缩放
		if(HttpUtil.STATE==3){
			web_content.getSettings().setBuiltInZoomControls(true);
			web_content.getSettings().setDisplayZoomControls(false);
			web_content.getSettings().setUseWideViewPort(true);
		}
		web_content.getSettings().setDefaultZoom(zoomDensity);
		web_content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		int width = (int) (CommonUtil.getScreenWidth(this) / screenDensity) - 10;
		Log.i("x",""+ width);
		String head = "<style type='text/css'>*{max-width:" + width + "px!important;height:auto!important;}" +
				"table{width:100%!important;}" + "</style>";
		if(HttpUtil.STATE==3){
			web_content.loadUrl(content);
		}else{
			web_content.loadData(head +content,"text/html; charset=UTF-8",null);
		}
	}
	//初始化控件
	private void findView(){
		web_content=(WebView)findViewById(R.id.content);
		layout_alert=new DialogEvent().showLoading(this);
		DialogUtil.show(context,layout_alert);
	}
	//事件监听
	private void setListener(){
		viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
			@Override
			public void onHandleMessage(Message msg) {
				if(HttpUtil.STATE==0||HttpUtil.STATE==3){
					init();
					if(!content.equals(""))
						DialogUtil.dismiss(context,layout_alert);
				}
				else{
					DialogUtil.dismiss(context,layout_alert);
				}
			}
		});
	}

	 //获得数据
	private Runnable mTask=new Runnable(){
		public void run() {
			while(content.equals("")&&HttpUtil.STATE==0){
				content=JsoupForTX.getNews(url);
				viewHandler.sendMessage(viewHandler.obtainMessage());
			}
			if(HttpUtil.STATE==1&&layout_alert.isShowing()){
				viewHandler.sendMessage(viewHandler.obtainMessage());
			}
		}
	};
	//缓存清空
	@Override
    protected void onDestroy() {
		super.onDestroy();
		web_content.clearCache(false);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}