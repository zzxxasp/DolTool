package com.key.doltool.activity.adc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ADCSkillListAdapter;
import com.key.doltool.data.ADCInfo;
import com.key.doltool.data.ADCSkill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

public class ADCDetailsActivity extends BaseActivity{
	private TextView sex,type,country,city,name;
	private ImageView head;
	private DefaultDAO dao;
	private String id="0";
	private String name_str="0";
	private ADCInfo item;
	private ListView listview;
	private List<ADCSkill> list=new ArrayList<>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adc_details);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		name_str=getIntent().getStringExtra("name");
		findViewById();
		setListener();
		init();
	}
	
	private void findViewById(){
		sex=(TextView)findViewById(R.id.adc_sex);
		country=(TextView)findViewById(R.id.adc_country);
		type=(TextView)findViewById(R.id.adc_type);
		city=(TextView)findViewById(R.id.adc_city);
		name=(TextView)findViewById(R.id.adc_name);
		
		head=(ImageView)findViewById(R.id.adc_head);
		listview=(ListView)findViewById(R.id.listview);
	}
	private void setListener(){
		
	}
	
	private void init(){
		if(!StringUtil.isNull(id)){
			item=(ADCInfo)dao.select(ADCInfo.class, false, "id=?",new String[]{id+""}, null, null, null, null).get(0);
		}else{
			item=(ADCInfo)dao.select(ADCInfo.class, false, "name=?",new String[]{name_str+""}, null, null, null, null).get(0);
		}

		sex.setText(item.getSex());
		int types=item.getType();
		if(types==1){
			type.setText("冒险");
		}else if(types==2){
			type.setText("商业");
		}else{
			type.setText("海事");
		}
		if(item.getSex().equals("男")){
			sex.setBackgroundResource(R.drawable.theme_blue_btn_rate);
		}else{
			sex.setBackgroundResource(R.drawable.theme_pink_btn_rate);
		}
		name.setText(item.getName());
		country.setText(item.getCountry());
		city.setText(item.getCity());
		try {
			head.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.ADC+item.getHead_img()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		list=new Gson().fromJson(item.getSkill_list(), new TypeToken<List<ADCSkill>>(){}.getType());
		listview.setAdapter(new ADCSkillListAdapter(list, this));
	}
}
