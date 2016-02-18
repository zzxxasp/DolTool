package com.key.doltool.activity.trade;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.TagAdapter;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.event.TradeEvent;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.key.doltool.view.LinearLayoutForListView;
import com.the9tcat.hadi.DefaultDAO;

import java.io.IOException;

public class TradeDetailActivity extends BaseActivity{
	private TextView name,sp;
	private ImageView pic;
	private TextView type_txt,number_txt,city_txt;
	private ImageView pic_type,number_type;
	private TextView details;
	private FlowLayout city_area;
	private FlowLayout recipe_area;
	private DefaultDAO dao;
	private String id="";
	private String name_txt="";
	private String tw_name="";
	private LinearLayout recipe_way;
	//钓点查询
	//--------
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trade_detail);
		initToolBar(null);
		dao=SRPUtil.getDAO(getApplicationContext());
		id=getIntent().getStringExtra("id");
		name_txt=getIntent().getStringExtra("name");
		tw_name=getIntent().getStringExtra("tw_name");
		findView();
		setListener();
		init();
	}
	private void findView(){
		recipe_way=(LinearLayout)findViewById(R.id.recipe_way);
		recipe_area=(FlowLayout)findViewById(R.id.recipse_array);
		name=(TextView)findViewById(R.id.name);
		sp=(TextView)findViewById(R.id.sp);
		type_txt=(TextView)findViewById(R.id.type_txt);
		number_txt=(TextView)findViewById(R.id.number_txt);
		details=(TextView)findViewById(R.id.main_txt);
		city_txt=(TextView)findViewById(R.id.city_txt);
		pic=(ImageView)findViewById(R.id.pic);
		pic_type=(ImageView)findViewById(R.id.tag_type);
		number_type=(ImageView)findViewById(R.id.tag_number);
		city_area=(FlowLayout)findViewById(R.id.city_array);
	}
	private void setListener(){
		
	}
	private void init(){
		TradeItem item;
		if(StringUtil.isNull(name_txt)){
			item=(TradeItem)dao.select(TradeItem.class, false, "id=?", new String[]{""+id}, null, null, null, null).get(0);
		}else{
			item=(TradeItem)dao.select(TradeItem.class, false, "name=? or name=?", new String[]{""+name_txt,tw_name}, null, null, null, null).get(0);
		}
		if(item.getProducing_area().equals("")){
			city_txt.setVisibility(View.GONE);
			city_area.setVisibility(View.GONE);
		}else{
			String[] city=item.getProducing_area().split(",");
			city_area.setAdapter(new TagAdapter(this,city));
		}
		if(item.getRecipe_way().equals("")){
			recipe_way.setVisibility(View.GONE);
		}else{
			String[] city=item.getRecipe_way().split(",");
			recipe_area.setAdapter(new TagAdapter(this,city,item.getName()));
		}
		details.setText(item.getDetail());
		name.setText(item.getName());
		flag=false;
		initToolBar(null);
		toolbar.setTitle(item.getName());
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.TRADE+item.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(item.getSp().equals("")||item.getSp().equals("钓鱼")){
			sp.setVisibility(View.GONE);
		}else{
			sp.setText(item.getSp());
		}
		TradeEvent event=new TradeEvent(this);
		event.setNumber(item.getTrade(), number_type, number_txt);
		event.setType(item.getType(), pic_type, type_txt);
	}
}
