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

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.MessageAdapter;
import com.key.doltool.data.Message;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.view.Toast;

import butterknife.BindView;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessageShowActivity extends BaseActivity{
	@BindView(R.id.listview) ListView listview;
	private List<Message> list=new ArrayList<>();
	private Dialog alert;

	@Override
	public int getContentViewId() {
		return R.layout.message_show;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
		toolbar.setTitle("本月作者留言");
		alert=new DialogEvent().showLoading(this);
		alert.show();
		postMessage();
	}

	//显示作者的留言
	private void postMessage(){
		AVQuery<AVObject> query= AVQuery.getQuery("message_maker");
		Calendar curDate=Calendar.getInstance();
		int year=curDate.get(Calendar.YEAR);
		int month=curDate.get(Calendar.MONTH)+1;
		String data=year+"."+month;
		query.whereEqualTo("date",data).orderByDescending("time");
		query.findInBackground().subscribe(new Observer<List<AVObject>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(List<AVObject> avObjects) {
				if(!isFinishing()){
					alert.dismiss();
				}
				show(avObjects);
			}

			@Override
			public void onError(Throwable e) {
				if(!isFinishing()){
					alert.dismiss();
				}
				Toast.makeText(getApplicationContext(),"连接失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete() {

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
