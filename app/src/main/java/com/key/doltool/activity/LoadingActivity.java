package com.key.doltool.activity;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.key.doltool.R;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.event.VoyageEvent;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.lang.ref.WeakReference;


public class LoadingActivity extends BaseActivity{
	private DefaultDAO dao;
	private MyAnimations my;
	private Thread mThread;
	private MyHandler updateUI;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		dao=SRPUtil.getDAO(getApplicationContext());
		updateUI=new MyHandler(LoadingActivity.this);
		AdManager.getInstance(this).init("8b0be299a6c08260", "65fb4b2fb906d2e3", false);
		SpotManager.getInstance(this).loadSpotAds();
		SpotManager.getInstance(this)
	    .setAutoCloseSpot(true);
		SpotManager.getInstance(this)
	    .setCloseTime(5000);
		ImageView load = (ImageView) findViewById(R.id.load);
		my=new MyAnimations();
		my.roate(this,load,2000);
		mThread=new Thread(mTask);
		mThread.start();
		new VoyageEvent(this).getRandomChance();
	}
	private Runnable mTask=new Runnable(){
		public void run() {
			DBUtil.copyDB(getApplicationContext(),updateUI,dao);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			jump();
		}
	};
	private void jump(){
		startActivity(new Intent(this,MainActivity.class));
		finish();
	}

	private static class MyHandler extends Handler {
		private final WeakReference<LoadingActivity> mActivity;

		public MyHandler(LoadingActivity activity) {
			mActivity = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			LoadingActivity activity = mActivity.get();
			if (activity != null) {
				switch(msg.what) {
					case 1:
						Toast.makeText(activity,"更新成功", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(activity,"初始化失败",Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dao=null;
		my=null;
		mThread=null;
	}
}
