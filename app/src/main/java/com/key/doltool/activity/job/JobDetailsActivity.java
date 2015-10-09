package com.key.doltool.activity.job;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.TagPicAdapter;
import com.key.doltool.data.Job;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.the9tcat.hadi.DefaultDAO;

public class JobDetailsActivity extends BaseActivity{
	private TextView name,type,details;
	private TextView level,money,book_mark;
	private RelativeLayout sp_layout;
	private ImageView sp_pic;
	private TextView sp_name;
	private DefaultDAO dao;
	private String id="0";
	private String name_str="0";
	private Job item;
	private FlowLayout skill_array;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initToolBar(null);
		setContentView(R.layout.job_details);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		name_str=getIntent().getStringExtra("name");
		findViewById();
		setListener();
		init();
	}
	
	private void findViewById(){
		type=(TextView)findViewById(R.id.type);
		name=(TextView)findViewById(R.id.name);
		details=(TextView)findViewById(R.id.details);
		
		level=(TextView)findViewById(R.id.level);
		money=(TextView)findViewById(R.id.money);
		book_mark=(TextView)findViewById(R.id.book_mark);
		
		sp_layout=(RelativeLayout)findViewById(R.id.sp_layout);
		sp_pic=(ImageView)findViewById(R.id.sp_pic);
		sp_name=(TextView)findViewById(R.id.sp_name);
		
		skill_array=(FlowLayout)findViewById(R.id.skill_array);

	}
	private void setListener(){
		
	}
	
	private void init(){
		if(!StringUtil.isNull(id)){
			item=(Job)dao.select(Job.class,false,"id=?",new String[]{""+id},null,null,null,null).get(0);
		}else{
			item=(Job)dao.select(Job.class,false,"name=?",new String[]{""+name_str},null,null,null,null).get(0);
		}

		name.setText(""+item.getName());
		details.setText(""+item.getDetail());
		int types=item.getType();
		if(types==1){
			type.setText("冒险");
		}else if(types==2){
			type.setText("商业");
		}else{
			type.setText("海事");
		}
		
		book_mark.setText(item.getMetal());
		String[] temp=item.getChang_if().split("\\|");
		Log.i("x",temp.length+"");
		Log.i("x",temp[0]);
		Log.i("x",temp[1]);
		String temp_1[]=temp[0].split(":");
		String levle_string=temp_1[1].replace("合计","")+"(合计:"+temp_1[2]+")";
		level.setText(levle_string);
		money.setText(temp[1].split(":")[1]);
		if(StringUtil.isNull(item.getSp())){
			sp_layout.setVisibility(View.GONE);
		}else{
			sp_name.setText(item.getSp());
			try {
				sp_pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(DataSelectUtil.getSkillPicByName(item.getSp(), dao)+".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list=new Gson().fromJson(item.getGood_list(),new TypeToken<List<String>>(){}.getType());
		String[] skill=StringUtil.listToArray(list);
		skill_array.setAdapter(new TagPicAdapter(this, skill));
	}
}
