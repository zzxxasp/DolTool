package com.key.doltool.activity.trade;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.TradeSimpleListAdapter;
import com.key.doltool.data.City;
import com.key.doltool.data.TradeCityItem;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

public class TradeCityDetailActivity extends BaseActivity{
	private ListView listview;
	private String name="";
	private String tw_name="";
	private String pic_id="";
	private City item;
	private DefaultDAO dao;
	private Gson gson;
	private List<TradeCityItem> list;
	
	private ImageView pic;
	private TextView city_name,city_area,city_culture;
	
	private RelativeLayout inverst;
	private TextView inverst_money,inverst_item;
	
	private LinearLayout lang1,lang2;
	private TextView lang_txt1,lang_txt2;
	private ImageView lang_pic1,lang_pic2;
	private String id;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trade_city_list);
		dao=SRPUtil.getDAO(context);
		gson=new Gson();
		name=getIntent().getStringExtra("city_name");
		tw_name=getIntent().getStringExtra("tw_name");
		id=getIntent().getStringExtra("id");
		init();
		findView();
		setListener();
	}
	private void findView(){
		listview=(ListView)findViewById(R.id.listview);
		pic=(ImageView)findViewById(R.id.city_img);
		city_name=(TextView)findViewById(R.id.city_name);
		city_area=(TextView)findViewById(R.id.city_area);
		city_culture=(TextView)findViewById(R.id.city_culture);
		
		inverst=(RelativeLayout)findViewById(R.id.inverst);
		inverst_money=(TextView)findViewById(R.id.inverst_money);
		inverst_item=(TextView)findViewById(R.id.inverst_item);
		
		lang1=(LinearLayout)findViewById(R.id.lang1);
		lang2=(LinearLayout)findViewById(R.id.lang2);
		lang_txt1=(TextView)findViewById(R.id.lang_txt1);
		lang_txt2=(TextView)findViewById(R.id.lang_txt2);
		
		lang_pic1=(ImageView)findViewById(R.id.lang_pic1);
		lang_pic2=(ImageView)findViewById(R.id.lang_pic2);
	}
	
	private void setListener(){
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(this.getAssets().open(FileManager.TROVE+pic_id+".jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		city_name.setText(item.getName());
		city_area.setText(item.getArea());
		city_culture.setText(item.getCulture());
		
		if(item.getInvest().equals("")){
			inverst.setVisibility(View.GONE);
		}else{
			String[] temp=item.getInvest().split(" ");
			inverst_money.setText(temp[1]);
			inverst_item.setText(temp[0]);
		}
		String[] temp_lang=item.getLang().split(",");
		if(temp_lang.length<1){
			lang1.setVisibility(View.GONE);
		}else{
			lang_txt1.setText(temp_lang[0]);
			try {
				lang_pic1.setImageBitmap(BitMapUtil.getBitmapByInputStream(this.getAssets().open(DataSelectUtil.getSkillPicByName(temp_lang[0], dao)+".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(temp_lang.length<2){
			lang2.setVisibility(View.GONE);
		}else{
			lang_txt2.setText(temp_lang[1]);
			try {
				lang_pic2.setImageBitmap(BitMapUtil.getBitmapByInputStream(this.getAssets().open(DataSelectUtil.getSkillPicByName(temp_lang[1], dao)+".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		list=gson.fromJson(item.getTrade_list(), new TypeToken<List<TradeCityItem>>(){}.getType());
		listview.setAdapter(new TradeSimpleListAdapter(list, this));
		
	}
	private void init(){
		if(StringUtil.isNull(id)){
			if(StringUtil.isNull(tw_name)){
				item=(City)dao.select(City.class,false,"name=?",new String[]{name}, null, null, null, null).get(0);
			}else{
				item=(City)dao.select(City.class,false,"name=? or name=?",new String[]{name,tw_name}, null, null, null, null).get(0);
			}
			pic_id=DataSelectUtil.getTrovePicByName(item.getName(), dao);
		}else{
			item=(City)dao.select(City.class,false,"id=?",new String[]{id}, null, null, null, null).get(0);
			pic_id=DataSelectUtil.getTrovePicByName(item.getName(), dao);
		}
	}
}
