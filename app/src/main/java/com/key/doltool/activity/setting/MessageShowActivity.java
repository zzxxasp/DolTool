package com.key.doltool.activity.setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.MessageAdapter;
import com.key.doltool.data.Message;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.view.Toast;

public class MessageShowActivity extends BaseActivity{
	private List<Message> list=new ArrayList<>();
	private ListView listview;
	private Dialog alert;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_show);
		findView();
		flag=false;
		initToolBar(null);
		toolbar.setTitle("本月作者留言");
		alert=new DialogEvent().showLoading(this);
		alert.show();
		postMessage();
	}
	private void findView(){
		listview=(ListView)findViewById(R.id.listview);
	}
	//显示作者的留言
	private void postMessage(){
		AVQuery<AVObject> query= AVQuery.getQuery("message_maker");
		Calendar curDate=Calendar.getInstance();
		int year=curDate.get(Calendar.YEAR);
		int month=curDate.get(Calendar.MONTH)+1;
		String data=year+"."+month;
		query.whereEqualTo("date",data).orderByDescending("time");
		query.findInBackground(new FindCallback<AVObject>() {
		     public void done(List<AVObject> objects, AVException e) {
		         if (e == null) {
		        	 show(objects);
		         } else {
		             Toast.makeText(getApplicationContext(),"连接失败", Toast.LENGTH_LONG).show();
		         }
				 if(!isFinishing()){
					 alert.dismiss();
				 }
		     }
		 });
	}
	private void show(List<AVObject> objects){
		SimpleDateFormat s=new SimpleDateFormat("yyyy.MM.dd hh:mm:ss",Locale.CHINA);
		for(int i=0;i<objects.size();i++){
			Message msg=new Message();
			msg.time=s.format(objects.get(i).getDate("time").getTime());
			msg.word= objects.get(i).getString("word");
			list.add(msg);
		}
		listview.setAdapter(new MessageAdapter(list, this));
		listview.setVisibility(View.VISIBLE);
	}
}
