package com.key.doltool.activity.person;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.data.sqlite.Verion;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.event.UserEvent;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.util.imageUtil.ImageLoader;
import com.key.doltool.view.HoloCircularProgressBar;
import com.key.doltool.view.SystemBarTintManager;
import com.key.doltool.view.Toast;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.the9tcat.hadi.DefaultDAO;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * 个人信息界面
 * **/
public class PersonActivity extends BaseActivity{
	@BindView(R.id.name) TextView name;
	@BindView(R.id.tag1) TextView tag1;
	@BindView(R.id.tag2) TextView tag2;
	@BindView(R.id.head_img) ImageView head_img;
	@BindView(R.id.function_3) RelativeLayout function_3;
	@BindView(R.id.function_2) RelativeLayout function_2;
	@BindView(R.id.function_1) RelativeLayout function_1;
	@BindView(R.id.holoCircularProgressBar2) HoloCircularProgressBar t_bar;
	@BindView(R.id.holoCircularProgressBar1) HoloCircularProgressBar m_bar;
	@BindView(R.id.mission_number) TextView mission_number;
	@BindView(R.id.trove_number) TextView trove_number;

	private Dialog dialog;
	private DefaultDAO dao;
	private SRPUtil srp;
	private Gson g=new Gson();
	private Verion v;
	private ObjectAnimator mProgressBarAnimator;
	private List<?> a,b;
	private int a_size,b_size;
	private DialogEvent event;
	private Drawable bg;
	private Bitmap bitmap;
	private String back_temp="";

	@Override
	public int getContentViewId() {
		return R.layout.activity_user;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		event=new DialogEvent();
		dialog=event.itemDialog(this,"请稍候...");
		dao=SRPUtil.getDAO(getApplication());
		srp=SRPUtil.getInstance(getApplication());
		findView();
		setListener();
	}

	private void setListener(){
		function_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.materialDialog(0,"数据同步","是否要同步数据？",PersonActivity.this,0);
			}
		});
		function_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.materialDialog(0,"密码重置","是否要重置密码？",PersonActivity.this,1);
			}
		});
		
		function_3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.materialDialog(0,"注销","是否需要注销当前用户？",PersonActivity.this,2);
			}
		});
	}
	
	private void findView(){
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle("个人信息");
		toolbar.setBackgroundColor(getResources().getColor(R.color.black));
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.black);
		new Thread(mTask).start();
		init();
	}
	@Override
	protected void onResume() {
		Log.i("re","onResume");
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		if(UpdataList.PIC_CHANGE==1){
			init();
			Log.i("re","onRestart");
		}
		super.onRestart();
	}
		
	@Override
	protected void onDestroy() {
        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.cancel();
            mProgressBarAnimator=null;
        }
        if(bitmap!=null&&!bitmap.isRecycled()){
        	bitmap.recycle();
        	bitmap=null;
        }
        if(bg!=null){
            bg.setCallback(null);
            bg=null;
        }
        dao=null;
		System.gc();
		super.onDestroy();
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(!StringUtil.isNull(back_temp)){
				dialog.dismiss();
				back_temp="";
				Toast.makeText(getApplicationContext(),"数据已同步",Toast.LENGTH_SHORT).show();
			}
            mission_number.setText("" + a_size);
            trove_number.setText("" + b_size);
            if (mProgressBarAnimator != null) {
                mProgressBarAnimator.cancel();
            }
			t_bar.setProgressWithAnimation(b_size * 1.0f / ResourcesUtil.getInt(PersonActivity.this, R.integer.all_trove) * 1.0f, 2000);
			m_bar.setProgressWithAnimation(a_size * 1.0f / ResourcesUtil.getInt(PersonActivity.this, R.integer.all_mission) * 1.0f, 2000);
		}
	 };
	
	
	private Runnable mTask=new Runnable(){
		public void run() {
			if(!StringUtil.isNull(back_temp)){
				List<String> list=g.fromJson(back_temp, new TypeToken<List<String>>(){}.getType());
				UpdataCount event=new UpdataCount(PersonActivity.this);
				event.backSync(list);
			}
			a_size=(int) srp.countByType(true,0);
			b_size=(int) srp.countByType(true,1);
			mHandler.sendMessage(mHandler.obtainMessage());
			a=dao.select(Mission.class,false,"tag=?", new String[]{"1"},null, null, null,null);		
			b=dao.select(Trove.class,false,"flag=?", new String[]{"1"},null, null, null,null);
		}
	};

	//同步信息
	public void syncInfo(String fileName){
		if(a==null||b==null){
			Toast.makeText(getApplicationContext(),"暂无数据，无需同步",Toast.LENGTH_SHORT).show();
		}
		//同步数据
		dialog.show();
		v=DataSelectUtil.UpdateTime(dao);
		//1.根据时间判断是需要下载还是更新数据
		//2.如果本地的时间过早则进行下载，并提示，其他一律进行数据的同步
		List<String> temp=new ArrayList<>();
		for(int i=0;i<a.size();i++){
			Mission misson=(Mission)a.get(i);
			temp.add("m+"+misson.getId());
		}
		for(int i=0;i<b.size();i++){
			Trove trove=(Trove)b.get(i);
			temp.add("t+"+trove.getId());
		}
		byte[] data={};
		
		try {
			data = g.toJson(temp).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		final AVFile file=new AVFile(fileName,data);
		file.saveInBackground();

		AVQuery<AVObject> query = AVQuery.getQuery("BackUp");
		query.whereEqualTo("userId", AVUser.getCurrentUser());
		//更新
		query.findInBackground(new FindCallback<AVObject>() {
			  public void done(List<AVObject> commentList, AVException e) {
				  if(e==null){
					  if(commentList.size()!=0){
						  //有数据的情况（判断时间进行更新）
						  SimpleDateFormat s=new SimpleDateFormat("yyyy.MM.dd hh:mm:ss",Locale.CHINA);
						  int temp=NumberUtil.compare_date(v.update_time,s.format(commentList.get(0).getDate("syncTime")));
						  Log.i("temp",temp+"");
						  if(temp<0){
							  //需要本地更新（弹出提示）
							  AVFile syncFile =commentList.get(0).getAVFile("backFile");
							  //展示对话框-时间不同步
							  v.update_time=StringUtil.dateFormat(commentList.get(0).getDate("syncTime"));
							  syncFile.getDataInBackground(new GetDataCallback() {
									public void done(byte[] data, AVException e) {
										if (e == null) {
											try {
												back_temp=new String(data,"UTF-8");
												dao.update(v,new String[]{"update_time"},"verion>?",new String[]{"0"});
												new Thread(mTask).start();
												UpdataList.FLAG_CHANGE_LIST=1;
											} catch (UnsupportedEncodingException e1) {
												e1.printStackTrace();
											}
										} else {
											e.printStackTrace();
											dialog.dismiss();
											Toast.makeText(getApplicationContext(),"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
										}
									}
								});
						  }else{
							  //云服务更新
							  Date date=new Date();
							  v.update_time=StringUtil.dateFormat(date);
							  dao.update(v,new String[]{"update_time"},"verion>?",new String[]{"0"});
							  AVObject syncData=commentList.get(0);
							  AVRelation<AVObject> relation=syncData.getRelation("userId");
							  relation.add(AVUser.getCurrentUser());
							  syncData.put("backFile",file);
							  syncData.put("syncTime", date);
							  syncData.saveInBackground(new SaveCallback(){
									public void done(AVException e) {
										if (e == null) {
											Toast.makeText(getApplicationContext(),"数据已同步",Toast.LENGTH_SHORT).show();
										} else {
											e.printStackTrace();
											Toast.makeText(getApplicationContext(),"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
										}
										dialog.dismiss();
									}
								});  
						  }
					  }else{
						  //没有数据的情况（直接更新）
						  Date date=new Date();
						  v.update_time=StringUtil.dateFormat(date);
						  dao.update(v,new String[]{"update_time"},"verion>?",new String[]{"0"});
						  AVObject syncData=new AVObject("BackUp");
						  AVRelation<AVObject> relation=syncData.getRelation("userId");
						  relation.add(AVUser.getCurrentUser());
						  syncData.put("backFile",file);
						  syncData.put("syncTime", date);
						  syncData.saveInBackground(new SaveCallback(){
							  public void done(AVException e) {
								  if (e == null) {
									  Toast.makeText(getApplicationContext(),"数据已同步",Toast.LENGTH_SHORT).show();
									} else {
										e.printStackTrace();
										Toast.makeText(getApplicationContext(),"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
									}
									dialog.dismiss();
								}
						  });
					  }
				  }else{
					  e.printStackTrace();
					  Toast.makeText(getApplicationContext(),"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
					  dialog.dismiss();
				  }
			  }
		});
	}
	//重新密码
	public void resetPassword(){
		dialog.show();
		AVUser.requestPasswordResetInBackground(AVUser.getCurrentUser().getEmail(),
                new RequestPasswordResetCallback() {
			public void done(AVException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),"请查收注册的邮箱进行重置密码",Toast.LENGTH_SHORT).show();
				} else {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
	}
	//初始化用户
	private void init(){
		AVUser currentUser = AVUser.getCurrentUser();
		//如果有用户则
		if (currentUser != null) {
			AVFile headImg=currentUser.getAVFile("headPic");
			if(headImg!=null){
				ImageLoader.picassoLoadCircle(this, headImg.getUrl(),head_img);
			}else{
				ImageLoader.picassoLoadCircle(this,head_img);
			}
			if(!StringUtil.isNull(currentUser.getString("nickName"))){
				name.setText(currentUser.getString("nickName"));
			}else{
				name.setText(currentUser.getUsername());
			}
			
			if(!StringUtil.isNull(currentUser.getString("server"))){
				String temp[]=currentUser.getString("server").split("-");
				tag1.setText(UserEvent.AREA[Integer.parseInt(temp[0])]);
				tag2.setText(UserEvent.HOST[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])]);
			}else{
				tag1.setText("未填写");
				tag2.setText("未填写");
			}
		}
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:startActivity(new Intent(PersonActivity.this, EditUserInfoActivity.class));break;
			}
			return true;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
	}
}
