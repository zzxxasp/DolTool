package com.key.doltool.activity.person;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UserEvent;
import com.key.doltool.view.EmailAutoCompleteTextView;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends BaseActivity{
	//注册
	private EditText password_register,password_repeat;
	private EmailAutoCompleteTextView email;
	private EditText nickName;
	private Spinner area,server;
	private String server_name="0-0";
	private FlatButton login;
	private Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_register);
		findView();
	}
	private void findView(){
		dialog=new DialogEvent().itemDialog(this,"注册中");
		
		password_register=(EditText)findViewById(R.id.password);
		password_repeat=(EditText)findViewById(R.id.password_repeat);
		nickName=(EditText)findViewById(R.id.nick);
		email=(EmailAutoCompleteTextView)findViewById(R.id.email);
		
		area=(Spinner)findViewById(R.id.area);
		server=(Spinner)findViewById(R.id.server);
		login=(FlatButton)findViewById(R.id.login);
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
			Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!password_repeat.getText().toString().equals(password_register.getText().toString())){
			Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(email.getText().toString().equals("")){
			Toast.makeText(this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	//注册
	public void signUp(){
		server_name=area.getSelectedItemPosition()+"-"+server.getSelectedItemPosition();
		Log.i("tag",""+server_name);
		ParseUser user = new ParseUser();
		user.setUsername(email.getText().toString());
		user.setPassword(password_register.getText().toString());
		user.setEmail(email.getText().toString());
		if(!nickName.getText().toString().trim().equals("")){
			user.put("nickName",nickName.getText().toString().trim());
		}
		user.put("server",server_name);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					dialog.dismiss();
					Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					//登录失败
					switch(e.getCode()){
						case 101:
							Toast.makeText(getApplicationContext(),"账号不存在或密码错误",Toast.LENGTH_SHORT).show();
							break;
						default:
							Toast.makeText(getApplicationContext(),"服务器异常:"+e.getCode(),Toast.LENGTH_SHORT).show();
							break;
					}
					dialog.dismiss();
				}
			}
		});
	}
}
