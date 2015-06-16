package com.key.doltool.activity.person;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.Mission;
import com.key.doltool.data.Trove;
import com.key.doltool.data.Verion;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.event.UserEvent;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.BootstrapCircleThumbnail;
import com.key.doltool.view.HoloCircularProgressBar;
import com.key.doltool.view.Toast;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 个人信息界面
 * **/
public class PersonActivity extends BaseActivity{
	private TextView main_title;
	private ImageView main_menu;
	private TextView name,tag1,tag2;
	private BootstrapCircleThumbnail head_img;
	private RelativeLayout function_3,function_2,function_1;
	private Dialog dialog;
	
	private HoloCircularProgressBar t_bar,m_bar;
	private TextView mission_number,trove_number;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		event=new DialogEvent();
		dialog=event.itemDialog(this,"请稍候...");
		dao=SRPUtil.getDAO(this);
		srp=SRPUtil.getInstance(this);
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
		main_title=(TextView)findViewById(R.id.main_title);
		main_title.setText("个人信息");
		main_menu=(ImageView)findViewById(R.id.main_menu);
		main_menu.setVisibility(View.VISIBLE);
		main_menu.setImageResource(R.drawable.ic_edit);
		main_menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(PersonActivity.this,EditUserInfoActivity.class));
			}
		});
		
		head_img=(BootstrapCircleThumbnail)findViewById(R.id.head_img);
		name=(TextView)findViewById(R.id.name);
		tag1=(TextView)findViewById(R.id.tag1);
		tag2=(TextView)findViewById(R.id.tag2);
		function_2=(RelativeLayout)findViewById(R.id.function_2);
		function_3=(RelativeLayout)findViewById(R.id.function_3);
		function_1=(RelativeLayout)findViewById(R.id.function_1);
		
		t_bar=(HoloCircularProgressBar)findViewById(R.id.holoCircularProgressBar2);
		m_bar=(HoloCircularProgressBar)findViewById(R.id.holoCircularProgressBar1);
		trove_number=(TextView)findViewById(R.id.trove_number);
		mission_number=(TextView)findViewById(R.id.mission_number);
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
    	if(head_img.mBitmap!=null){
    		head_img.mBitmap.recycle();
    		head_img.mBitmap=null;
    	}
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
				Toast.makeText(PersonActivity.this,"数据已同步",Toast.LENGTH_SHORT).show();
			}
            mission_number.setText(""+a_size);
            trove_number.setText(""+b_size);
            if (mProgressBarAnimator != null) {
                mProgressBarAnimator.cancel();
            }
            animate(t_bar, null,b_size*1.0f/ResourcesUtil.getInt(PersonActivity.this,R.integer.all_trove)*1.0f,1000);
            animate(m_bar, null,a_size*1.0f/ResourcesUtil.getInt(PersonActivity.this,R.integer.all_mission)*1.0f,1000);
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
	
    private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener,
            final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        mProgressBarAnimator.start();
    }
	
	//同步信息
	public void syncInfo(String fileName){
		if(a==null||b==null){
			Toast.makeText(PersonActivity.this,"暂无数据，无需同步",Toast.LENGTH_SHORT).show();
		}
		//同步数据
		dialog.show();
		v=DataSelectUtil.UpdateTime(dao);
		//1.根据时间判断是需要下载还是更新数据
		//2.如果本地的时间过早则进行下载，并提示，其他一律进行数据的同步
		List<String> temp=new ArrayList<String>();
		for(int i=0;i<a.size();i++){
			Mission misson=(Mission)a.get(i);
			temp.add("m+"+misson.getId());
		}
		for(int i=0;i<b.size();i++){
			Trove trove=(Trove)b.get(i);
			temp.add("t+"+trove.getId());
		}
		byte[] data = null;
		
		try {
			data = g.toJson(temp).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		
		final ParseFile file=new ParseFile(fileName,data);
		file.saveInBackground();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("BackUp");
		query.whereEqualTo("userId",ParseUser.getCurrentUser());
		//更新
		query.findInBackground(new FindCallback<ParseObject>() {
			  public void done(List<ParseObject> commentList, ParseException e) {
				  if(e==null){
					  if(commentList.size()!=0){
						  //有数据的情况（判断时间进行更新）
						  SimpleDateFormat s=new SimpleDateFormat("yyyy.MM.dd hh:mm:ss",Locale.CHINA);
						  int temp=NumberUtil.compare_date(v.update_time,s.format(commentList.get(0).getDate("syncTime")));
						  Log.i("temp",temp+"");
						  if(temp<0){
							  //需要本地更新（弹出提示）
							  ParseFile syncFile =commentList.get(0).getParseFile("backFile");
							  //展示对话框-时间不同步
							  v.update_time=StringUtil.dateFormat(commentList.get(0).getDate("syncTime"));
							  syncFile.getDataInBackground(new GetDataCallback() {
									public void done(byte[] data, ParseException e) {
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
											Toast.makeText(PersonActivity.this,"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
										}
									}
								});
						  }else{
							  //云服务更新
							  Date date=new Date();
							  v.update_time=StringUtil.dateFormat(date);
							  dao.update(v,new String[]{"update_time"},"verion>?",new String[]{"0"});
							  ParseObject syncData=commentList.get(0);
							  ParseRelation<ParseObject> relation=syncData.getRelation("userId");
							  relation.add(ParseUser.getCurrentUser());
							  syncData.put("backFile",file);
							  syncData.put("syncTime", date);
							  syncData.saveInBackground(new SaveCallback(){
									public void done(ParseException e) {
										if (e == null) {
											Toast.makeText(PersonActivity.this,"数据已同步",Toast.LENGTH_SHORT).show();
										} else {
											e.printStackTrace();
											Toast.makeText(PersonActivity.this,"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
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
						  ParseObject syncData=new ParseObject("BackUp");
						  ParseRelation<ParseObject> relation=syncData.getRelation("userId");
						  relation.add(ParseUser.getCurrentUser());
						  syncData.put("backFile",file);
						  syncData.put("syncTime", date);
						  syncData.saveInBackground(new SaveCallback(){
							  public void done(ParseException e) {
								  if (e == null) {
									  Toast.makeText(PersonActivity.this,"数据已同步",Toast.LENGTH_SHORT).show();
									} else {
										e.printStackTrace();
										Toast.makeText(PersonActivity.this,"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
									}
									dialog.dismiss();
								}
						  });
					  }
				  }else{
					  e.printStackTrace();
					  Toast.makeText(PersonActivity.this,"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
					  dialog.dismiss();
				  }
			  }
		});
	}
	//重新密码
	public void resetPassword(){
		dialog.show();
		ParseUser.requestPasswordResetInBackground(ParseUser.getCurrentUser().getEmail(),
                new RequestPasswordResetCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(PersonActivity.this,"请查收注册的邮箱进行重置密码",Toast.LENGTH_SHORT).show();
				} else {
					e.printStackTrace();
					Toast.makeText(PersonActivity.this,"错误"+e.getCode(),Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
	}
	//初始化用户
	private void init(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		//如果有用户则
		if (currentUser != null) {
			ParseFile headImg=currentUser.getParseFile("headPic");
			if(headImg!=null){
				headImg.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, ParseException e) {
						if (e == null) {
							bitmap=BitMapUtil.getBitmapByInputStream(data);
							head_img.setImageBitmap(bitmap);
						} else {
							bg = getResources().getDrawable(R.drawable.dol_trove_defalut); 
							head_img.setImageDrawable(bg);
						}
					}
				});
			}else{
				bg = getResources().getDrawable(R.drawable.dol_trove_defalut); 
				head_img.setImageDrawable(bg);
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
}
