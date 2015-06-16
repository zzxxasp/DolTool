package com.key.doltool.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MessagePostActivity extends BaseActivity{
	private EditText name,content;
	private FlatButton btn;
	//控制提交状态
	private boolean over=false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message_layout);
		findViewById();
		setListener();
	}
	
	private void findViewById(){
		name=(EditText)findViewById(R.id.name);
		content=(EditText)findViewById(R.id.content);
		btn=(FlatButton)findViewById(R.id.btn);
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
			Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
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
			ParseObject message=new ParseObject("CommonWord");
			message.put("username", name);
			message.put("word", content);
			message.saveInBackground(new SaveCallback(){
				public void done(ParseException e) {
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
