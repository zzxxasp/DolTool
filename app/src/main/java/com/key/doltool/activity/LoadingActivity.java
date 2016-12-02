package com.key.doltool.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.key.doltool.R;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.event.PermissionEvent;
import com.key.doltool.event.VoyageEvent;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.view.Toast;

import java.lang.ref.WeakReference;

//import net.youmi.android.AdManager;
//import net.youmi.android.spot.SpotManager;

/**
 * 载入界面
 * **/
public class LoadingActivity extends BaseActivity{

	private MyAnimations my;
	private Thread mThread;
	private MyHandler updateUI;

	private PermissionEvent basePermission;

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
		basePermission=new PermissionEvent(this,new String[]{Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE},listner,100,"应用会在本地读写文件，请务必进行授权否则将无法使用应用");
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case 100:
				basePermission.onRequest(grantResults);
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
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

	private PermissionEvent.PermissionAllowListener listner=new PermissionEvent.PermissionAllowListener(){
		@Override
		public void onPermissionAllow(int code) {
			switch (code){
				case 100:
					mThread=new Thread(mTask);
					mThread.start();
					new VoyageEvent(getApplicationContext()).getRandomChance();
					break;
			}

		}
		@Override
		public void onPermissionDenied(int code) {
			switch (code){
				case 100:
					Toast.makeText(getApplicationContext(),"读写权限被拒绝，即将退出", Toast.LENGTH_SHORT)
							.show();
					new Thread(mTasks_time).start();
					break;
			}

		}
	};

	//5秒监听线程
	private Runnable mTasks_time = new Runnable(){
		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				ExitApplication.getInstance().exit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
}
