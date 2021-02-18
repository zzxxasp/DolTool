package com.key.doltool.activity.core;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.key.doltool.R;
import com.key.doltool.activity.InfoMainFragment;
import com.key.doltool.activity.adventure.AdventureMainFragment;
import com.key.doltool.activity.adventure.NPCFragment;
import com.key.doltool.activity.adventure.card.CardComboFragment;
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
import com.key.doltool.activity.wiki.WebMainFragment;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.app.util.behavior.LoginBehavior;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.MenuEvent;
import com.key.doltool.event.app.VersionManager;
import com.key.doltool.event.rx.RxBusEvent;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.imageUtil.ImageLoader;
import com.key.doltool.view.stick.StickyListHeadersListView;


import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVFile;
import cn.leancloud.AVUser;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseFragmentActivity{
	private DrawerLayout mDrawerLayout = null;
	private ActionBarDrawerToggle mDrawerToggle;
	//侧边栏
	private StickyListHeadersListView menu_list;
	private List<MenuItem> list;
	private MenuAdapter adapter;
	
	private LinearLayout person_info;
	private ImageView headPic;
	private TextView username;
	
	//内容
	private Fragment mContent;
	private List<FragmentItem> fragment_list=new ArrayList<>();
	private int index=0;
	private ViewHandler UIHandler;
	private Disposable updateSubscription;

	@Override
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
		Observable<Boolean> sailBoatObservable = RxBusEvent.get().register(RxBusEvent.UPDATE);
		updateSubscription=sailBoatObservable.subscribe(new Consumer<Boolean>() {
			@Override
			public void accept(Boolean aBoolean) {
				if(aBoolean) {
					initParseUser();
				}
			}
		});
	}
	private void initUser(){
		UIHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
			@Override
			public void onHandleMessage(Message msg) {
				switchContent(msg.what);
				index=msg.what;
				adapter.setIndex(msg.what);
				adapter.notifyDataSetChanged();
				toolbar.setTitle(list.get(msg.what).text);
			}
		});

		person_info= findViewById(R.id.person_info);
		headPic= findViewById(R.id.head_img);
		username= findViewById(R.id.name);
		//获取默认用户
		LoginBehavior loginBehavior=new LoginBehavior();
		String currentUser = loginBehavior.getUserName();
		String headURL = loginBehavior.getUserHeadImageURL();
		//如果有用户则
		if (currentUser != null) {
			if(loginBehavior.getUserHeadImageURL()!=null){
				ImageLoader.picassoLoadCircle(this, headURL, headPic);
			}else{
				ImageLoader.picassoLoadCircle(this, headPic);
			}
			username.setText(currentUser);
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
				VersionManager.getInstance().setActivity(this);
				VersionManager.getInstance().checkVersion(false);
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
		mDrawerLayout= findViewById(R.id.drawer_layout);
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
		mDrawerLayout.addDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		init();
	}
	private void init(){
		menu_list = findViewById(R.id.listview);
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
//    		case 9:mContent=new MainBroadFragment();break;
    		case 9:mContent=new SqureMainFragment();break;
    		case 10:mContent=new SettingMainFragment();break;
//    		case 11:mContent=new HelpCenterFragment();break;
//    		case 12:mContent=new DevFragment();break;
			case 11:
				WebMainFragment temp=new WebMainFragment();
				Bundle bundle=new Bundle();
				bundle.putString("url","ser.html");
				temp.setArguments(bundle);
				mContent= temp;
				break;
			case 12:
				WebMainFragment temp2=new WebMainFragment();
				Bundle bundle2=new Bundle();
				bundle2.putString("url","user.html");
				temp2.setArguments(bundle2);
				mContent=temp2;
				break;
//			case 13:mContent=new DataBaseInsertFragment();break;
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
		super.onActivityResult(requestCode, resultCode, data);
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
    	AVUser currentUser = AVUser.getCurrentUser();
    	//如果有用户则
    	if (currentUser != null) {
			AVFile headImg=currentUser.getAVFile("headPic");
    		headPic.setVisibility(View.VISIBLE);
    		if(headImg!=null){
				ImageLoader.picassoLoadCircle(this, headImg.getUrl(), headPic);
			} else{
				ImageLoader.picassoLoadCircle(this, headPic);
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
    
    //系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mContent instanceof BaseFragment){
			BaseFragment s=(BaseFragment)mContent;
			if(!s.onKeyDown(keyCode, event)){
				if(keyCode==KeyEvent.KEYCODE_BACK){
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
				}
			}else{
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(updateSubscription!=null){
			updateSubscription.dispose();
		}
		RxBusEvent.get().unregister(RxBusEvent.UPDATE);
	}
}