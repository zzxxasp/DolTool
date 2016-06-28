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

import butterknife.BindView;

//import com.key.doltool.util.DataSelectUtil;
public class SailBoatActivity extends BaseActivity{
	//数据
	private List<SailBoat> list=new ArrayList<>();
	private DefaultDAO dao;
	private String[] temp=new String[3];
	//控件显示(区域一:Base-基本)
	@BindView(R.id.boat_name)  TextView ship_name;
	@BindView(R.id.boat_need)  TextView need;
	@BindView(R.id.boat_pic)  ImageView boat_pic;
	@BindView(R.id.type_1)  TextView type1;
	@BindView(R.id.type_2)  TextView type2;
	@BindView(R.id.type_3)  TextView type3;
	//控件显示(区域二:Ability-能力)
	@BindView(R.id.square_sail_point)  TextView vo_s;
	@BindView(R.id.fore_sail_point)  TextView vo_f;
	@BindView(R.id.turn_point)  TextView vo_tu;
	@BindView(R.id.def_wave_point)  TextView vo_de;
	@BindView(R.id.paddle_point)  TextView vo_p;
	@BindView(R.id.health_boat_point)  TextView bt_h;
	@BindView(R.id.people_number_point)  TextView bt_p;
	@BindView(R.id.armor_point)  TextView bt_a;
	@BindView(R.id.crenelle_point)  TextView bt_c;
	@BindView(R.id.shipping_space_point)  TextView bt_s;
	//控件显示(区域三:GetWay-获得途径)
	@BindView(R.id.line) TableLayout line;
	//布局显示
	@BindView(R.id.equip) RelativeLayout equip;

	@Override
	public int getContentViewId() {
		return R.layout.dockyard_boat_detail;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		dao=SRPUtil.getDAO(this);
		init();
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
		TextView[] equip_txt = new TextView[equip.getChildCount()];
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
}
