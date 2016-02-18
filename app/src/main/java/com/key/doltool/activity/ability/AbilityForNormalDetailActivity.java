package com.key.doltool.activity.ability;
import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ADCSkillSelectAdapter;
import com.key.doltool.adapter.TagSkillAdapter;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.data.sqlite.Job;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.key.doltool.view.LinearLayoutForListView;
import com.the9tcat.hadi.DefaultDAO;

public class AbilityForNormalDetailActivity extends BaseActivity{
	private TextView name,type,details;
	private TextView level,money,skill_need;
	private ImageView pic;
	private FlowLayout job_array;
	private LinearLayoutForListView adc_array;
	private RelativeLayout need_layout;
	private TextView tag5,tag6;
	private DefaultDAO dao;
	private String id="0";
	private String name_str="0";
	private Skill item;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skill_details);
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
		pic=(ImageView)findViewById(R.id.pic);
		
		level=(TextView)findViewById(R.id.level);
		money=(TextView)findViewById(R.id.money);
		skill_need=(TextView)findViewById(R.id.skill_need);
		need_layout=(RelativeLayout)findViewById(R.id.need_layout);
		job_array=(FlowLayout)findViewById(R.id.job_array);
		adc_array=(LinearLayoutForListView)findViewById(R.id.adc_array);
		tag5=(TextView)findViewById(R.id.tag5);
		tag6=(TextView)findViewById(R.id.tag6);
	}
	private void setListener(){
		
	}
	
	@SuppressWarnings("unchecked")
	private void init(){
		if(!StringUtil.isNull(id)){
			item=(Skill)dao.select(Skill.class,false,"id=?",new String[]{""+id},null,null,null,null).get(0);
		}else{
			item=(Skill)dao.select(Skill.class,false,"name=?",new String[]{""+name_str},null,null,null,null).get(0);
		}
		name.setText(""+item.getName());
		details.setText(""+item.getDetail());
		int types=item.getType();
		if(types==1){
			type.setText("冒险");
		}else if(types==2){
			type.setText("商业");
		}else if(types==3){
			type.setText("海事");
		}else if(types==4){
			type.setText("语言");
		}else if(types==5){
			type.setText("副官");
		}
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.SKILL+item.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//处理need数据
		if(types<5){
			String[] temp=item.getNeed().split("\\|");
			Log.i("x",temp.length+"");
			Log.i("x",temp[0]);
			Log.i("x",temp[1]);
			level.setText(temp[0].split(":")[1]);
			money.setText(temp[1].split(":")[1]);
			if(temp.length>2){
				skill_need.setText(temp[2].split(":")[1]);
			}else{
				skill_need.setText("无技能前置");
			}
			//查询职业
			List<Job> list_sp=(List<Job>)dao.select(Job.class,false,"good_list like ? or sp=?",new String[]{"%"+"\""+item.getName()+"\""+"%",item.getName()},null,null,null,null);
			job_array.setAdapter(new TagSkillAdapter(this,list_sp,item.getName()));
			if(list_sp.size()==0){
				tag5.setVisibility(View.GONE);
				job_array.setVisibility(View.GONE);
			}
		}else{
			need_layout.setVisibility(View.GONE);
			tag5.setVisibility(View.GONE);
			job_array.setVisibility(View.GONE);
		}
		List<ADCInfo> list_adc=(List<ADCInfo>)dao.select(ADCInfo.class,false,"skill_list like ?",new String[]{"%"+"\""+item.getName()+"\""+"%"},null,null,null,null);
		Log.i("q", ""+list_adc.size());
		adc_array.setAdapter(new ADCSkillSelectAdapter(item.getName(), list_adc, this));
		if(list_adc.size()==0){
			tag6.setVisibility(View.GONE);
			adc_array.setVisibility(View.GONE);
		}
	}
}
