package com.key.doltool.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.app.util.behavior.LoginBehavior;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import butterknife.BindView;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.callback.SaveCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessagePostActivity extends BaseActivity{
	@BindView(R.id.content) EditText content;
	@BindView(R.id.btn) FlatButton btn;
	//控制提交状态
	private boolean over=false;

	@Override
	public int getContentViewId() {
		return R.layout.system_message_layout;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		setListener();
	}

	private void setListener(){
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!over){
					over=true;
					postMessage(content.getText().toString().trim());
				}
			}
		});
	}
	private boolean check(){
		if(content.getText().toString().trim().equals("")){
			//信息不为空
			Toast.makeText(getApplicationContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void clear(){
		content.setText("");
	}
	//发信息给作者
	private void postMessage(String content){
		if(check()){
			LoginBehavior loginBehavior=new LoginBehavior();
			String name=loginBehavior.getUserName();

			if(name==null){
				name="游客";
			}

			AVObject message=new AVObject("CommonWord");
			message.put("username",name);
			message.put("word", content);
			message.saveInBackground().subscribe(new Observer<AVObject>() {
				@Override
				public void onSubscribe(Disposable d) {

				}

				@Override
				public void onNext(AVObject avObject) {
					Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_LONG).show();
					clear();
				}

				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_LONG).show();
				}

				@Override
				public void onComplete() {

				}
			});
		}else{
			over=false;
		}
	}
}
