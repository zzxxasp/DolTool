package com.key.doltool.activity.person;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.adapter.AutoTextViewAdapter;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UserEvent;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import butterknife.BindView;
import cn.leancloud.AVException;
import cn.leancloud.AVUser;
import cn.leancloud.callback.SignUpCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity{
	//注册
	@BindView(R.id.password) EditText password_register;
	@BindView(R.id.password_repeat) EditText password_repeat;
	@BindView(R.id.emailWrapper)
	TextInputLayout usernameWrapper;
	@BindView(R.id.passwordWrapper) TextInputLayout passwordWrapper;
	@BindView(R.id.password_repeatWrapper) TextInputLayout password_repeatWrapper;
	@BindView(R.id.nickWrapper) TextInputLayout nickWrapper;
	@BindView(R.id.email) AutoCompleteTextView email;
	@BindView(R.id.nick) EditText nickName;
	@BindView(R.id.area) Spinner area;
	@BindView(R.id.server) Spinner server;
	@BindView(R.id.login) FlatButton login;

	private AutoTextViewAdapter adapter_email;
	private Dialog dialog;

	@Override
	public int getContentViewId() {
		return R.layout.login_register;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
		flag=false;
		initToolBar(null);
		toolbar.setTitle("注册");
	}

	private void findView(){
		dialog=new DialogEvent().itemDialog(this,"注册中");

		usernameWrapper.setHint("登录邮箱");
		passwordWrapper.setHint("登录密码");
		password_repeatWrapper.setHint("重复密码");
		nickWrapper.setHint("昵称(非必填)");

		adapter_email = new AutoTextViewAdapter(this);
		email.setAdapter(adapter_email);
		email.setThreshold(1);//输入1个字符时就开始检测，默认为2个
		email.addTextChangedListener(watcher);//监听autoview的变化


        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
		(RegisterActivity.this,UserEvent.AREA,1);
        area.setAdapter(adapter);
		
        area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
		        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
				(RegisterActivity.this, UserEvent.HOST[arg2],1);
				server.setAdapter(adapter);
				}
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
		
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(judge()){
					signUp();
				}
			}
		});
	}
	//判断
	private boolean judge(){
		if(password_register.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!password_repeat.getText().toString().equals(password_register.getText().toString())){
			Toast.makeText(getApplicationContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(email.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	//注册
	public void signUp(){
		String server_name = area.getSelectedItemPosition() + "-" + server.getSelectedItemPosition();
		Log.i("tag",""+ server_name);
		AVUser user = new AVUser();
		user.setUsername(email.getText().toString());
		user.setPassword(password_register.getText().toString());
		user.setEmail(email.getText().toString());
		if(!nickName.getText().toString().trim().equals("")){
			user.put("nickName", nickName.getText().toString().trim());
		}
		user.put("server", server_name);
		user.signUpInBackground().subscribe(new Observer<AVUser>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(AVUser avUser) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}

			@Override
			public void onComplete() {

			}
		});
	}

	private TextWatcher watcher=new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String input = s.toString();
			adapter_email.mList.clear();
			adapter_email.getAutoAdapterHelper().autoAddEmails(adapter_email,input);
			adapter_email.notifyDataSetChanged();
			email.showDropDown();
		}
	};

}
