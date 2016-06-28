package com.key.doltool.activity;

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
import com.key.doltool.view.Toast;

//import net.youmi.android.AdManager;
//import net.youmi.android.spot.SpotManager;

import java.lang.ref.WeakReference;


public class LoadingActivity extends BaseActivity{
	private MyAnimations my;
	private Thread mThread;
	private MyHandler updateUI;

	@Override
	public int getContentViewId() {
		return R.layout.loading;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		updateUI=new MyHandler(this);
		ImageView load = (ImageView) findViewById(R.id.load);
		my=new MyAnimations();
		my.roate(this,load,2000);
		mThread=new Thread(mTask);
		mThread.start();
		new VoyageEvent(this).getRandomChance();
	}

	private Runnable mTask=new Runnable(){
		public void run() {
			DBUtil.copyDB(getApplicationContext(),updateUI);
			try {
				Thread.sleep(1500);
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
						Toast.makeText(activity.getApplicationContext(),"更新成功", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(activity.getApplicationContext(),"初始化失败",Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		my=null;
		mThread=null;
	}
}
