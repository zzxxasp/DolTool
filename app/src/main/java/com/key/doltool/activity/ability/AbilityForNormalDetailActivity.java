package com.key.doltool.activity.ability;

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
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.key.doltool.view.LinearLayoutForListView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * 一般技能详情显示界面
 * 查询关联数据：职业，副官加成
 * **/
public class AbilityForNormalDetailActivity extends BaseActivity{
	@BindView(R.id.name) TextView name;
	@BindView(R.id.type) TextView type;
	@BindView(R.id.details) TextView details;
	@BindView(R.id.level) TextView level;
	@BindView(R.id.money) TextView money;
	@BindView(R.id.skill_need) TextView skill_need;
	@BindView(R.id.pic) ImageView pic;
	@BindView(R.id.job_array) FlowLayout job_array;
	@BindView(R.id.adc_array) LinearLayoutForListView adc_array;
	@BindView(R.id.need_layout) RelativeLayout need_layout;
	@BindView(R.id.tag5) TextView tag5;
	@BindView(R.id.tag6) TextView tag6;
	private SRPUtil dao;
	private String id="0";
	private String name_str="0";

	@Override
	public int getContentViewId() {
		return R.layout.skill_details;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		dao=SRPUtil.getInstance(getApplicationContext());
		id=getIntent().getStringExtra("id");
		name_str=getIntent().getStringExtra("name");
		init();
	}

	/**数据库读取**/
	private void init(){
		Skill item;
		if(!StringUtil.isNull(id)){
			item =dao.select(Skill.class,false,"id=?",new String[]{""+id},null,null,null,null).get(0);
		}else{
			item =dao.select(Skill.class,false,"name=?",new String[]{""+name_str},null,null,null,null).get(0);
		}
		name.setText(item.getName());
		details.setText(item.getDetail());
		int types= item.getType();
		//根据类型获得技能类型文字
		type.setText(ResourcesUtil.getArray(this,R.array.skill_type)[types+1]);
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.SKILL+ item.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//处理need数据
		if(types<5){
			String[] temp= item.getNeed().split("\\|");
			Log.i("x",temp.length+"");
			Log.i("x",temp[0]);
			Log.i("x",temp[1]);
			level.setText(temp[0].split(":")[1]);
			money.setText(temp[1].split(":")[1]);
			if(temp.length>2){
				skill_need.setText(temp[2].split(":")[1]);
			}else{
				skill_need.setText(R.string.skill_detail_normal_no_hint);
			}
			//查询职业
			List<Job> list_sp=dao.select(Job.class,false,"good_list like ? or sp=?",new String[]{"%"+"\""+ item.getName()+"\""+"%", item.getName()},null,null,null,null);
			job_array.setAdapter(new TagSkillAdapter(this,list_sp, item.getName()));
			if(list_sp.size()==0){
				tag5.setVisibility(View.GONE);
				job_array.setVisibility(View.GONE);
			}
		}else{
			need_layout.setVisibility(View.GONE);
			tag5.setVisibility(View.GONE);
			job_array.setVisibility(View.GONE);
		}
		List<ADCInfo> list_adc=dao.select(ADCInfo.class,false,"skill_list like ?",new String[]{"%"+"\""+ item.getName()+"\""+"%"},null,null,null,null);
		Log.i("q", ""+list_adc.size());
		adc_array.setAdapter(new ADCSkillSelectAdapter(item.getName(), list_adc, this));
		if(list_adc.size()==0){
			tag6.setVisibility(View.GONE);
			adc_array.setVisibility(View.GONE);
		}
	}
}
