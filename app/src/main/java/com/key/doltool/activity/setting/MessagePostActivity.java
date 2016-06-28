package com.key.doltool.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import butterknife.BindView;

public class MessagePostActivity extends BaseActivity{
	@BindView(R.id.name) EditText name;
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
					postMessage(name.getText().toString(),content.getText().toString().trim());
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
		name.setText("");
		content.setText("");
	}
	//发信息给作者
	private void postMessage(String name,String content){
		if(check()){
			AVObject message=new AVObject("CommonWord");
			message.put("username", name);
			message.put("word", content);
			message.saveInBackground(new SaveCallback(){
				public void done(AVException e) {
					if(e==null){
						//成功
						Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_LONG).show();
						clear();
					}else{
						//失败
						Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_LONG).show();
					}
					over=false;
				}
			});
		}else{
			over=false;
		}
	}
}
