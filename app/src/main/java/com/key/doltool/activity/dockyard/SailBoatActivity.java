package com.key.doltool.activity.dockyard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SailBoat;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
//import com.key.doltool.util.DataSelectUtil;
public class SailBoatActivity extends BaseActivity{
	//数据
	private List<SailBoat> list=new ArrayList<>();
	private DefaultDAO dao;
	private String[] temp=new String[3];
	//控件显示(区域一:Base-基本)
	private TextView ship_name,need;
	private ImageView boat_pic;
	private TextView[] equip_txt;
	private TextView type1,type2,type3;
	//控件显示(区域二:Ability-能力)
	private TextView vo_s,vo_f,vo_tu,vo_de,vo_p;
	private TextView bt_h,bt_p,bt_a,bt_c,bt_s;
	//控件显示(区域三:GetWay-获得途径)
	private TableLayout line;
	private ImageView menu;
	//布局显示
	private RelativeLayout equip;
	private ScrollView content_s;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dockyard_boat_detail);
		dao=SRPUtil.getDAO(this);
		findView();
		init();
		setListener();
	}
	@SuppressWarnings("unchecked")
	//获取数据，填充显示
	private void init(){
		int id=getIntent().getIntExtra("id",0);
		String[] x={""+id};
		list=(List<SailBoat>) dao.select(SailBoat.class, false, "id=?", x, 
				null, null, null, null);
		ship_name.setText(list.get(0).getName());
		String[] part=list.get(0).getNumber_part().split(",");
		equip_txt=new TextView[equip.getChildCount()];
		for(int i=0;i<equip.getChildCount();i++){
			equip_txt[i]=(TextView)equip.getChildAt(i);
			equip_txt[i].append(""+part[i]);
		}
		try {
			boat_pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.BOAT+list.get(0).getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		need.setText(list.get(0).getLevel_m()+"/"+list.get(0).getLevel_s()+"/"+list.get(0).getLevel_j());
		temp=ViewUtil.setDataForType(list.get(0).getType(), list.get(0).getSize(), list.get(0).getWay_id());
		type1.setText(temp[0]);
		type2.setText(temp[1]);
		type3.setText(temp[2]);
		//能力
		vo_s.setText(list.get(0).getSquare_sail()+"");
		vo_f.setText(list.get(0).getFore_sail()+"");
		vo_tu.setText(list.get(0).getTurn()+"");
		vo_de.setText(list.get(0).getDef_wave()+"");
		vo_p.setText(list.get(0).getPaddle()+"");
		
		bt_h.setText(list.get(0).getHealth_boat()+"");
		bt_p.setText(list.get(0).getPeople_must()+"/"+list.get(0).getPeople_number());
		bt_a.setText(list.get(0).getArmor()+"");
		bt_c.setText(list.get(0).getCrenelle()+"");
		bt_s.setText(list.get(0).getShipping_space()+"");
		
		List<MenuItem> ab;
		ab=initMenuItem();
		ViewUtil.fill_boat_detail(this,ab, line);
	}
	//初始化控件
	private void findView(){
		ship_name=(TextView)findViewById(R.id.boat_name);
		boat_pic=(ImageView)findViewById(R.id.boat_pic);
		line=(TableLayout)findViewById(R.id.line);
		need=(TextView)findViewById(R.id.boat_need);
		equip=(RelativeLayout)findViewById(R.id.equip);
		content_s=(ScrollView)findViewById(R.id.content_s);
		
		type1=(TextView)findViewById(R.id.type_1);
		type2=(TextView)findViewById(R.id.type_2);
		type3=(TextView)findViewById(R.id.type_3);
		menu=(ImageView)findViewById(R.id.main_menu);
		menu.setVisibility(View.VISIBLE);
		menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent(context,BuildBoatActivity.class);
				intent.putExtra("id",getIntent().getIntExtra("id",0));
				startActivity(intent);
			}
		});
		findView_A();
	}
	private void findView_A(){
		vo_s=(TextView)findViewById(R.id.square_sail_point);
		vo_f=(TextView)findViewById(R.id.fore_sail_point);
		vo_tu=(TextView)findViewById(R.id.turn_point);
		vo_de=(TextView)findViewById(R.id.def_wave_point);
		vo_p=(TextView)findViewById(R.id.paddle_point);
		
		bt_h=(TextView)findViewById(R.id.health_boat_point);
		bt_p=(TextView)findViewById(R.id.people_number_point);
		bt_a=(TextView)findViewById(R.id.armor_point);
		bt_c=(TextView)findViewById(R.id.crenelle_point);
		bt_s=(TextView)findViewById(R.id.shipping_space_point);
	}
	private List<MenuItem> initMenuItem(){
		String yx=list.get(0).getAbility();
		String yy[]=yx.split(",");
		List<MenuItem> ab=new ArrayList<>();
		for (String aYy : yy) {
			MenuItem item = new MenuItem();
			item.name = aYy;
			item.pic = DataSelectUtil.getSkillPicByName(aYy, dao);
			ab.add(item);
		}
		return ab;
	}
	//事件监听
	private void setListener(){
		menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//截图保存
				Bitmap map=BitMapUtil.scrollView2Bitmap(content_s);
				BitMapUtil.savepic(map,getIntent().getIntExtra("id",0));
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
