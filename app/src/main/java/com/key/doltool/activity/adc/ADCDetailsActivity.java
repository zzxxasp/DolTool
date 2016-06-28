package com.key.doltool.activity.adc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ADCSkillListAdapter;
import com.key.doltool.data.ADCSkill;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * 副官详情界面
 * **/
public class ADCDetailsActivity extends BaseActivity{
	@BindView(R.id.adc_sex) TextView sex;
	@BindView(R.id.adc_type) TextView type;
	@BindView(R.id.adc_country) TextView country;
	@BindView(R.id.adc_city) TextView city;
	@BindView(R.id.adc_name) TextView name;
	@BindView(R.id.adc_head) ImageView head;
	@BindView(R.id.listview) ListView listview;
	private DefaultDAO dao;
	private String id="0";
	private String name_str="0";


	@Override
	public int getContentViewId() {
		return R.layout.adc_details;
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
		ADCInfo item;
		if(!StringUtil.isNull(id)){
			item =(ADCInfo)dao.select(ADCInfo.class, false, "id=?",new String[]{id+""}, null, null, null, null).get(0);
		}else{
			item =(ADCInfo)dao.select(ADCInfo.class, false, "name=?",new String[]{name_str+""}, null, null, null, null).get(0);
		}

		sex.setText(item.getSex());
		int types= item.getType();
		type.setText(ResourcesUtil.getArray(this,R.array.adc_type_txt)[types]);

		if(item.getSex().equals(getResources().getString(R.string.adc_sex_male))){
			sex.setBackgroundResource(R.drawable.theme_blue_btn_rate);
		}else{
			sex.setBackgroundResource(R.drawable.theme_pink_btn_rate);
		}
		name.setText(item.getName());
		country.setText(item.getCountry());
		city.setText(item.getCity());
		try {
			head.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.ADC+ item.getHead_img()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<ADCSkill> list = new Gson().fromJson(item.getSkill_list(), new TypeToken<List<ADCSkill>>() {
		}.getType());
		listview.setAdapter(new ADCSkillListAdapter(list, this));
	}
}
