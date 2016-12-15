package com.key.doltool.activity.person;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.adapter.AutoTextViewAdapter;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import butterknife.BindView;


public class LoginActivity extends BaseActivity{
	//登录
	@BindView(R.id.usernameWrapper) TextInputLayout usernameWrapper;
	@BindView(R.id.passwordWrapper) TextInputLayout passwordWrapper;
	@BindView(R.id.password) EditText password;
	@BindView(R.id.account) AutoCompleteTextView account;
	@BindView(R.id.register) FlatButton register;
	@BindView(R.id.forget_password) FlatButton forgetpassword;
	@BindView(R.id.login) FlatButton login;
	private Dialog dialog;
	private AutoTextViewAdapter adapter;

	@Override
	public int getContentViewId() {
		return R.layout.login_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
	}

	private void findView(){
		usernameWrapper.setHint("登录邮箱");
		passwordWrapper.setHint("登录密码");
		dialog=new DialogEvent().itemDialog(this,"请等待");

		adapter = new AutoTextViewAdapter(this);
		account.setAdapter(adapter);
		account.setThreshold(1);//输入1个字符时就开始检测，默认为2个
		account.addTextChangedListener(watahcer);//监听autoview的变化
		login=(FlatButton)findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(judge()){
					login();
				}
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(it,101);
			}
		});
		forgetpassword.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogPop(R.layout.select_trove);
			}
		});
	}
	private void dialogPop(final int layout){
		final Dialog updateDialog = new Dialog(this,R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(this)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(layout, null);
        updateDialog.setContentView(view,params);
        updateDialog.show();
		final EditText name=(EditText)view.findViewById(R.id.boat_name);
		if(name!=null){
			name.setHint("请输入需要重置密码的账号邮箱");
		}
		final Button positive=(Button)view.findViewById(R.id.btn_confirm);
		final Button negative=(Button)view.findViewById(R.id.btn_cancel);
		positive.setText("确定");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(layout==R.layout.select_trove){
					if (name != null) {
						resetPassword(name.getText().toString().trim());
					}
				}
				updateDialog.dismiss();
			}
		});
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}

	private TextWatcher watahcer=new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String input = s.toString();
			adapter.mList.clear();
			adapter.getAutoAdapterHelper().autoAddEmails(adapter,input);
			adapter.notifyDataSetChanged();
			account.showDropDown();
		}
	};


	//重置密码（未登录版本）
	private void resetPassword(String emali){
		dialog.show();
		AVUser.requestPasswordResetInBackground(emali,
				new RequestPasswordResetCallback() {
					public void done(AVException e) {
						if (e == null) {
							Toast.makeText(getApplicationContext(), "请查收注册的邮箱进行重置密码", Toast.LENGTH_SHORT).show();
						} else {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "错误4", Toast.LENGTH_SHORT).show();
						}
						dialog.dismiss();
					}
				});
	}
	private boolean judge(){
		if(account.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(password.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
        	case 101:
        		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        		setResult(RESULT_OK, intent);
        		finish();
        		break;
        }
    }
	
	
	private void login(){
		dialog.show();
		AVUser.logInInBackground(account.getText().toString(),password.getText().toString(),new LogInCallback<AVUser>() {
			public void done(AVUser user, AVException e) {
				if (user != null) {
					//登录
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
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
