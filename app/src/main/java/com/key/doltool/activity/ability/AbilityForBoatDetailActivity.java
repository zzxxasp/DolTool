package com.key.doltool.activity.ability;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.LinearLayoutForListView;
import com.the9tcat.hadi.DefaultDAO;

public class AbilityForBoatDetailActivity extends BaseActivity{
	private Skill item;
	private String id="";
	private DefaultDAO dao;
	private LinearLayoutForListView boat_array;
	private ImageView img;
	private TextView name,details;
	private TextView skill_need;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		setContentView(R.layout.skill_details_boat);
		findView();
		init();
	}
	private void findView(){
		name=(TextView)findViewById(R.id.name);
		details=(TextView)findViewById(R.id.details);
		img=(ImageView)findViewById(R.id.pic);
		skill_need=(TextView)findViewById(R.id.skill_need);
		boat_array=(LinearLayoutForListView)findViewById(R.id.boat_array);
	}

	private void init(){
		item=(Skill)dao.select(Skill.class,false,"id=?",new String[]{""+id},null,null,null,null).get(0);
		name.setText(item.getName());
		details.setText(item.getDetail());
		skill_need.setText(item.getNeed());
		try {
			img.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.SKILL+item.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<SailBoat> sail= SRPUtil.getInstance(this).select(SailBoat.class,false,"ability like ?"
				,new String[]{"%"+item.getName()+"%"},null,null,null,null);
		boat_array.setAdapter(new SailBoatListAdapter(sail, this));
	}
}
