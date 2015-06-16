package com.key.doltool.activity.dockyard;
import java.io.IOException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.data.Part;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
public class PartActivity extends BaseActivity{
	private DefaultDAO dao;
	private Part part;
	private TextView name,type;
	private ImageView pic;
	private LinearLayout base;
	private TextView title_base;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dockyard_part_detail);
		dao=SRPUtil.getDAO(this);
		findView();
		init();
		setListener();
	}
	private void findView(){
		int id=getIntent().getIntExtra("id",0);
		String[] x={""+id};
		part=(Part)(dao.select(Part.class, false, "id=?", x, 
				null, null, null, null).get(0));
		name=(TextView)findViewById(R.id.name);
		type=(TextView)findViewById(R.id.type);
		pic=(ImageView)findViewById(R.id.pic);
		base=(LinearLayout)findViewById(R.id.details_base);
		title_base=(TextView)findViewById(R.id.base);
	}
	private void init(){
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.BOAT+part.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		name.setText(part.getName());
		type.setText(getType(part.getType()));
		//根据类型调整文字说明
		if(part.getZtype()!=0){
			title_base.setText("强化性能");
		}
		String[] map=part.getAdd().split(",");
		createByAdd(map);
		MyAnimations.fadein(base, 1000);
	}
	private void createByAdd(String[] map){
		for(int i=0;i<map.length;i++){
			LayoutInflater mInflater =this.getLayoutInflater();
			View v=mInflater.inflate(R.layout.part_base_item,null);
			TextView txt=(TextView)v;
			txt.setText(map[i]);
			LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0,5,0,5);
			txt.setLayoutParams(lp);
			base.addView(txt);
		}
	}
	private void setListener(){
		
	}
	private String getType(int i){
		String str="";
		switch(i){
			case 0:str="辅助帆";break;
			case 1:str="特殊装备";break;
			case 2:str="船炮";break;
			case 3:str="辅助装甲";break;
			case 4:str="船首像";break;
			case 5:str="船体";break;
			case 6:str="主帆";break;
			case 7:str="炮门";break;
			case 8:str="兵装";break;
		}
		return str;
	}
}
