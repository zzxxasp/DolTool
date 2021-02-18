package com.key.doltool.activity.wiki;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.data.sqlite.WikiInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.WebEvent;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.List;

import butterknife.BindView;


/**
 * WIKI详情页面（web方式）
 * @author key
 * @version 0.4
 * time 2013-1-2
 */
public class WikiMainActivity extends BaseActivity{
	@BindView(R.id.content) WebView web_content;
	private Dialog layout_alert;
	private DefaultDAO dao;
	private WikiInfo item;

	@Override
	public int getContentViewId() {
		return R.layout.news_detail;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		dao=SRPUtil.getDAO(context);
		findView();
		init();
	}

	//获取数据，填充显示
	@SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
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
			public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request) {
				return false;// false 显示frameset, true 不显示Frameset
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				DialogUtil.dismiss(context,layout_alert);
			}
		});
		web_content.setWebChromeClient(new WebChromeClient() {

		});
		web_content.loadUrl(FileManager.WIKI + item.getSid());
		web_content.addJavascriptInterface(new WebEvent(this), "jump");
	}
	//初始化控件
	private void findView(){
		layout_alert=new DialogEvent().showLoading(this);
		DialogUtil.show(context,layout_alert);
	}
	private void initData(){
		String id=getIntent().getStringExtra("id");
		Log.i("id",id);
		List<?> list=dao.select(WikiInfo.class, false, "id=?", new String[]{id}, null, null, null, null);
		item=(WikiInfo)list.get(0);
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle(item.getName());
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
	@Override
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