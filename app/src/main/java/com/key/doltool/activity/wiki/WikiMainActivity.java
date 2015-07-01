package com.key.doltool.activity.wiki;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.WikiInfo;
import com.key.doltool.event.WebEvent;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
/**
 * WIKI详情页面（web方式）
 * @author key
 * @version 0.4
 * @time 2013-1-2
 */
public class WikiMainActivity extends BaseActivity{
	private WebView web_content;
	private LinearLayout layout_alert;
	private DefaultDAO dao;
	private WikiInfo item;
	private int openFlag=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		initToolBar(null);
		dao=SRPUtil.getDAO(context);
		findView();
		init();
	}
	//获取数据，填充显示
	@SuppressLint("SetJavaScriptEnabled")
	private void init(){
		initData();
		web_content.getSettings().setJavaScriptEnabled(true);
		web_content.getSettings().setUseWideViewPort(true);
		web_content.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		web_content.getSettings().setDomStorageEnabled(true);
		web_content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		web_content.getSettings().setBlockNetworkImage(false);
		web_content.getSettings().setSupportZoom(true);
		web_content.getSettings().setAppCacheEnabled(false);
		web_content.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;// false 显示frameset, true 不显示Frameset
			}
		});
		web_content.setWebChromeClient(new WebChromeClient() {

		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
		web_content.loadUrl(FileManager.WIKI +item.getSid());
		web_content.addJavascriptInterface(new WebEvent(this), "jump");
	}
	//初始化控件
	private void findView(){
		web_content=(WebView)findViewById(R.id.content);
		layout_alert=(LinearLayout)findViewById(R.id.layout_alert);
		
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
		layout_alert.setVisibility(View.GONE);
	}
	private void initData(){
		String id=getIntent().getStringExtra("id");
		Log.i("id",id);
		List<?> list=dao.select(WikiInfo.class, false, "id=?", new String[]{id}, null, null, null, null);
		item=(WikiInfo)list.get(0);
	}
	//返回顶部
	private void backTop(){
		web_content.scrollTo(0,0);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	 //缓存清空
	 protected void onDestroy() {
		 super.onDestroy();
		 web_content.clearCache(false);
	 }
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wiki_menu, menu);
		return true;
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.back_top:
					backTop();
					break;
			}
			return true;
		}
	};
}