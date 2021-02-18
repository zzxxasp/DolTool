package com.key.doltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.key.doltool.R;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.activity.wiki.WebMainActivity;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.PermissionEvent;
import com.key.doltool.event.VoyageEvent;
import com.key.doltool.event.rx.RxCommonEvent;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.view.Toast;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 载入界面
 * **/
public class LoadingActivity extends BaseActivity{

	private MyHandler updateUI;

	@Override
	public int getContentViewId() {
		return R.layout.loading;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		updateUI=new MyHandler(this);
		showDialog();
		new VoyageEvent(getApplicationContext()).getRandomChance();
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

	private void showDialog() {
		SystemInfo spUtils = new SystemInfo(this);
		boolean isAgreement = spUtils.getFirst();
		if (isAgreement) {
			new Thread(mTask).start();
			return ;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("请你务必审慎阅读、充分理解\"服务协议\"和\"隐私政策\"各条款，包括但不限于：为了向你提供即时通讯、内容分享等服务，我们需要收集你的设备信息、操作日志等个人信息。\n你可阅读");
		int startIndex1 = sb.length();
		sb.append("《用户协议》");
		int endIndex1 = sb.length();
		sb.append("和");
		int startIndex2 = sb.length();
		sb.append("《隐私政策》");
		int endIndex2 = sb.length();
		sb.append("了解详细信息。如你同意，请点击\"同意\"开始接受我们的服务。");

		SpannableString ss = new SpannableString(sb.toString());
		ss.setSpan(new ClickableSpan() {
			@Override
			public void updateDrawState(@NonNull TextPaint ds) {
				ds.setColor(ContextCompat.getColor(LoadingActivity.this, R.color.Blue_SP));
			}

			@Override
			public void onClick(@NonNull View widget) {
				//用户协议
				Intent it=new Intent(LoadingActivity.this, WebMainActivity.class);
				it.putExtra("url","user.html");
				it.putExtra("title","用户协议");
				startActivity(it);
			}
		}, startIndex1, endIndex1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ClickableSpan() {
			@Override
			public void updateDrawState(@NonNull TextPaint ds) {
				ds.setColor(ContextCompat.getColor(LoadingActivity.this, R.color.Blue_SP));
			}

			@Override
			public void onClick(@NonNull View widget) {
				//隐私政策
				Intent it=new Intent(LoadingActivity.this, WebMainActivity.class);
				it.putExtra("url","ser.html");
				it.putExtra("title","隐私政策");
				startActivity(it);
			}
		}, startIndex2, endIndex2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

		AlertDialog dialog=new AlertDialog.Builder(this)
				.setTitle("用户协议&隐私协议")
				.setMessage(ss)
				.setCancelable(false)
				.setPositiveButton("确认", (dialogInterface, i) -> {
					new Thread(mTask).start();
					spUtils.setFirst(true);
				})
				.setNegativeButton("取消", (dialogInterface, i) -> ExitApplication.getInstance().exit())
				.create();
		dialog.show();

		TextView messageView = getSystemDialogMessageView(dialog);
		if (messageView != null) {
			messageView.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}


	protected TextView getSystemDialogMessageView(AlertDialog dialog) {
		try {
			Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
			mAlert.setAccessible(true);
			Object mAlertController = mAlert.get(dialog);
			Field mMessage;
			if (mAlertController != null) {
				mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
				mMessage.setAccessible(true);
				return (TextView) mMessage.get(mAlertController);
			}
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
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
}
