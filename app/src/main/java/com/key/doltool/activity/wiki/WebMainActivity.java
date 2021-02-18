package com.key.doltool.activity.wiki;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.event.WebEvent;
import com.key.doltool.util.FileManager;

import butterknife.BindView;


/**
 * WIKI详情页面（web方式）
 * @author key
 * @version 0.4
 * time 2013-1-2
 */
public class WebMainActivity extends BaseActivity {
	@BindView(R.id.content) WebView web_content;

	@Override
	public int getContentViewId() {
		return R.layout.news_detail;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
		init();
	}

	//获取数据，填充显示
	@SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
	private void init(){
		String url= null;
		if (getIntent() != null) {
			url = getIntent().getStringExtra("url");
			String title=getIntent().getStringExtra("title");
			toolbar.setTitle(title);
		}
		web_content.getSettings().setJavaScriptEnabled(true);
		web_content.getSettings().setUseWideViewPort(true);
		web_content.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		web_content.getSettings().setDomStorageEnabled(true);
		web_content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		web_content.getSettings().setBlockNetworkImage(false);
		web_content.getSettings().setSupportZoom(true);
		web_content.getSettings().setAppCacheEnabled(false);
		web_content.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request) {
				return false;// false 显示frameset, true 不显示Frameset
			}
		});
		web_content.setWebChromeClient(new WebChromeClient() {

		});
		web_content.loadUrl(FileManager.WIKI + url);
		web_content.addJavascriptInterface(new WebEvent(this), "jump");
	}
	//缓存清空
	@Override
	public void onDestroy() {
		super.onDestroy();
		web_content.clearCache(false);
	}
}