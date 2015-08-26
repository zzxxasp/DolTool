package com.key.doltool.activity.core;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.InfoMainFragment;
import com.key.doltool.activity.adventure.AdventureMainFragment;
import com.key.doltool.activity.adventure.CardComboFragment;
import com.key.doltool.activity.adventure.NPCFragment;
import com.key.doltool.activity.dockyard.DataBaseInsertFragment;
import com.key.doltool.activity.dockyard.DockYardFragment;
import com.key.doltool.activity.infobroad.MainBroadFragment;
import com.key.doltool.activity.person.LoginActivity;
import com.key.doltool.activity.person.PersonActivity;
import com.key.doltool.activity.recipe.RecipeMainFragment;
import com.key.doltool.activity.search.SearchFragment;
import com.key.doltool.activity.setting.SettingMainFragment;
import com.key.doltool.activity.squre.SqureMainFragment;
import com.key.doltool.activity.trade.TradeItemFragment;
import com.key.doltool.activity.voyage.VoyageMainFragment;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.MenuEvent;
import com.key.doltool.event.UpdataList;
import com.key.doltool.event.app.VersionManager;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.BootstrapCircleThumbnail;
import com.key.doltool.view.stick.StickyListHeadersListView;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
public class MainActivity extends BaseFragmentActivity{
	private DrawerLayout mDrawerLayout = null;
	private ActionBarDrawerToggle mDrawerToggle;
	//侧边栏
	private StickyListHeadersListView menu_list;
	private List<MenuItem> list;
	private MenuAdapter adapter;
	
	private LinearLayout person_info;
	private BootstrapCircleThumbnail headPic;
	private TextView username;
	
	//内容
	private Fragment mContent;
	private List<FragmentItem> fragment_list=new ArrayList<>();
	private int index=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_area);
		if(savedInstanceState!=null){
			index=savedInstanceState.getInt("index");
		}
		initToolBar(null);
		findView();
		initUser();
		initFragment(savedInstanceState);
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		SpotManager.getInstance(this).showSpotAds(this);
		SpotManager.getInstance(this).setCloseTime(3000);
	}
	private void initUser(){
		person_info=(LinearLayout)findViewById(R.id.person_info);
		headPic=(BootstrapCircleThumbnail)findViewById(R.id.head_img);
		username=(TextView)findViewById(R.id.name);
		//获取默认用户
		ParseUser currentUser = ParseUser.getCurrentUser();
		//如果有用户则
		if (currentUser != null) {
			currentUser.fetchInBackground();
			ParseFile headImg=currentUser.getParseFile("headPic");
			if(headImg!=null){
				headImg.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, ParseException e) {
						if (e == null) {
							headPic.setImageBitmap(BitMapUtil.getBitmapByInputStream(data,3));
						} else {
							headPic.setImageResource(R.drawable.dol_trove_defalut);
						}
					}
				});
			}else{
				headPic.setImageResource(R.drawable.dol_trove_defalut);
			}

			if(!StringUtil.isNull(currentUser.getString("nickName"))){
				username.setText(currentUser.getString("nickName"));
			}else{
				username.setText(currentUser.getUsername());
			}
			person_info.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent it=new Intent(MainActivity.this,PersonActivity.class);
					mDrawerLayout.closeDrawers();
					startActivityForResult(it,100);
				}
			});
		//没有则
		} else {
			headPic.setVisibility(View.GONE);
			username.setText("登录/注册");
			person_info.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent it=new Intent(MainActivity.this,LoginActivity.class);
					mDrawerLayout.closeDrawers();
					startActivityForResult(it,101);
				}
			});
		}
	}
	
	private void initFragment(Bundle savedInstanceState){
		//判断savedInstanceState不为空的时候，获取当前的mContent
		if(savedInstanceState!=null){
			index=savedInstanceState.getInt("index");
			Log.i("index", index + "");
			toolbar.setTitle(list.get(index).text);
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
			FragmentItem item=new FragmentItem();
			item.fragment=mContent;
			item.index=index;
			item.title=list.get(index).text;
			fragment_list.add(item);
		}
		//如果mContent为空，默认显示AppFragment的内容
		if(mContent == null){
			SystemInfo info=new SystemInfo(this);
			if(info.getUpdateFlag()==1){
		        ((VersionManager)VersionManager.getInstance()).setActivity(this);
		        ((VersionManager)VersionManager.getInstance()).checkVersion(false);
			}
			Log.i("mContent", mContent + "");
			toolbar.setTitle("发现之旅");
			mContent = new VoyageMainFragment();
			FragmentItem item=new FragmentItem();
			item.fragment=mContent;
			item.index=0;
			item.title="发现之旅";
			fragment_list.add(item);
		}
		//添加content的fragment
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.content_frame, mContent);
		transaction.commit();
	}
	
	private void findView(){
		list=new MenuEvent().initMenuList();
		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,R.string.app_name,R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		init();
	}
	private void init(){
		menu_list = (StickyListHeadersListView) findViewById(R.id.listview);
		menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				show(list.get(position).index);
			}
		});
		menu_list.setDrawingListUnderStickyHeader(false);
		menu_list.setAreHeadersSticky(true);
		adapter=new MenuAdapter(this,list,new MenuEvent().head,index);
		menu_list.setAdapter(adapter);
	}

    /**
     * 切换模块的内容
     * @param index 模块索引
     */
    public void switchContent(int index){
    	if(this.index==index){
    		return;
    	}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
    	switch(index){
			case 0:mContent=new VoyageMainFragment();break;
    		case 1:mContent=new AdventureMainFragment();break;
    		case 2:mContent=new NPCFragment();break;
    		case 3:mContent=new CardComboFragment();break;
    		case 4:mContent=new TradeItemFragment();break;
    		case 5:mContent=new RecipeMainFragment();break;
    		case 6:mContent=new DockYardFragment();break;
    		case 7:mContent=new InfoMainFragment();break;
    		case 8:mContent=new SearchFragment();break;
    		case 9:mContent=new MainBroadFragment();break;
    		case 10:mContent=new SqureMainFragment();break;
    		case 11:mContent=new SettingMainFragment();break;
    		case 12:mContent=new HelpCenterFragment();break;
//    		case 13:mContent=new DevFragment();break;
			case 13:mContent=new DataBaseInsertFragment();break;
    		default:
    			mContent=new AdventureMainFragment();
    			break;
    	}
    	Log.i("tag", mContent.getClass() + "");
    	transaction.setCustomAnimations(R.anim.activity_transition_slide_in_new, R.anim.activity_transition_slide_out_new);
		FragmentItem item=new FragmentItem();
		item.fragment=mContent;
		item.index=index;
		item.title=list.get(index).text;
    	transaction.replace(R.id.content_frame, mContent);
		fragment_list.add(item);
		transaction.commit();
    }
    //保存销毁的数据
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", index);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        super.onSaveInstanceState(outState);
    }
    //点击侧边栏跳转
    public void show(final int id) {
    	mDrawerLayout.closeDrawers();
    	new Thread(new Runnable(){
			public void run() {
				while(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
					//Dlow
				}
				UIHandler.sendEmptyMessage(id);
			}
		}).start();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
        	case 100:		
        		initParseUser();
        		break;
        	case 101:
        		initParseUser();
        		break;
        }
    }
    
    private void initParseUser(){
    	//获取默认用户
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	//如果有用户则
    	if (currentUser != null) {
    		ParseFile headImg=currentUser.getParseFile("headPic");
    		headPic.setVisibility(View.VISIBLE);
    		if(headImg!=null){
        		headImg.getDataInBackground(new GetDataCallback() {
        			public void done(byte[] data, ParseException e) {
        				if (e == null) {
        					headPic.setImageBitmap(BitMapUtil.getBitmapByInputStream(data));
        				} else {
        					headPic.setImageResource(R.drawable.dol_trove_defalut);
        				}
        			}
        		});
    		}else{
    			headPic.setImageResource(R.drawable.dol_trove_defalut);
    		}

    		if(!StringUtil.isNull(currentUser.getString("nickName"))){
    			username.setText(currentUser.getString("nickName"));
    		}else{
    			username.setText(currentUser.getUsername());
    		}
    		person_info.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Intent it=new Intent(MainActivity.this,PersonActivity.class);
    				mDrawerLayout.closeDrawers();
    				startActivityForResult(it,100);
    			}
    		});
    	//没有则
    	} else {
    		headPic.setVisibility(View.GONE);
    		username.setText("登录/注册");
    		person_info.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Intent it=new Intent(MainActivity.this,LoginActivity.class);
    				mDrawerLayout.closeDrawers();
    				startActivityForResult(it,101);
    			}
    		});
    	}
    }
    
    private Handler UIHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switchContent(msg.what);
    		index=msg.what;
    		adapter.setIndex(msg.what);
    		adapter.notifyDataSetChanged();
			toolbar.setTitle(list.get(msg.what).text);
    	}
    };
    
    //系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mContent instanceof BaseFragment){
			BaseFragment s=(BaseFragment)mContent;
			if(!s.onKeyDown(keyCode, event)){
				if(keyCode==KeyEvent.KEYCODE_BACK){
					if(!SpotManager.getInstance(this).disMiss(true)){
						if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
							mDrawerLayout.closeDrawers();
							return true;
						}else{
							if(fragment_list.size()>1){
								fragment_list.remove(fragment_list.size()-1);
								index=fragment_list.get(fragment_list.size()-1).index;
								mContent=fragment_list.get(fragment_list.size()-1).fragment;
								toolbar.setTitle(fragment_list.get(fragment_list.size()-1).title);
								FragmentManager fm = getSupportFragmentManager();
								FragmentTransaction transaction = fm.beginTransaction();
								transaction.setCustomAnimations(R.anim.activity_transition_slide_in_new, R.anim.activity_transition_slide_out_new);
								transaction.replace(R.id.content_frame, mContent);
								transaction.commit();
								adapter=new MenuAdapter(this,list,new MenuEvent().head,fragment_list.get(fragment_list.size()-1).index);
								menu_list.setAdapter(adapter);
								return false;
							}else{
								//弹出退出对话框
								new DialogEvent().materialDialog(0,"退出","确认是否要退出？",MainActivity.this,0);
							}
						}
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	 
	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}
    
	@Override
	public void onBackPressed() {
		// 如果有需要，可以点击后退关闭插屏广告。
		if (!SpotManager.getInstance(this).disMiss(true)) {
			super.onBackPressed();
		}
	} 
	@Override
	protected void onStop() {
	     //如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
	     SpotManager.getInstance(this).disMiss(false);
	     super.onStop();
	} 
	@Override
	protected void onRestart() {
		if(UpdataList.PIC_CHANGE==1){
			init();
		}
		super.onRestart();
	}
}