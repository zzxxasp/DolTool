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
import com.key.doltool.data.sqlite.Job;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.the9tcat.hadi.DefaultDAO;

import butterknife.BindView;

public class JobDetailsActivity extends BaseActivity{
	@BindView(R.id.name) TextView name;
	@BindView(R.id.type) TextView type;
	@BindView(R.id.details) TextView details;
	@BindView(R.id.level) TextView level;
	@BindView(R.id.money) TextView money;
	@BindView(R.id.book_mark) TextView book_mark;
	@BindView(R.id.sp_layout) RelativeLayout sp_layout;
	@BindView(R.id.sp_pic) ImageView sp_pic;
	@BindView(R.id.sp_name) TextView sp_name;
	@BindView(R.id.skill_array) FlowLayout skill_array;
	private DefaultDAO dao;
	private String id="0";
	private String name_str="0";
	private Job item;

	@Override
	public int getContentViewId() {
		return R.layout.job_details;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		name_str=getIntent().getStringExtra("name");
		init();
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
