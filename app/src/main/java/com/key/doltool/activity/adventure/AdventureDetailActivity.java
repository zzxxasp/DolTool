package com.key.doltool.activity.adventure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.io.IOException;

import butterknife.BindView;

public class AdventureDetailActivity extends BaseActivity{

	@BindView(R.id.txt) TextView name;
	@BindView(R.id.feats) TextView feat;
	@BindView(R.id.card_point) TextView card;
	@BindView(R.id.need) TextView need;
	@BindView(R.id.misson) TextView misson;
	@BindView(R.id.details) TextView detail;
	@BindView(R.id.img) ImageView pic;
	@BindView(R.id.star) RatingBar rate;
	@BindView(R.id.goto_mission) FlatButton goto_mission;
	@BindView(R.id.need_txt) TextView need_txt;
	@BindView(R.id.misson_txt) TextView misson_txt;
	//数据
	private Trove item;
	private DefaultDAO dao;
	@Override
	public int getContentViewId() {
		return R.layout.adventure_details;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		dao=SRPUtil.getDAO(this);
		setListener();
		init();
	}

	private void setListener(){
		goto_mission.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AdventureDetailActivity.this, MissionDetailsActivity.class);
				intent.putExtra("find_item", item.getName());
				intent.putExtra("type", getStringByType(item.getGetWay()));
				startActivity(intent);
			}
		});
	}

	private void init(){
		String names=getIntent().getStringExtra("name");
		String names_tw=getIntent().getStringExtra("tw_name");
		if(StringUtil.isNull(names)){
			names=getIntent().getStringExtra("id")+"";
			item=(Trove)(dao.select(Trove.class, false, "id = ?",new String[]{names}, null, null, null, null).get(0));
		}else{
			item=(Trove)(dao.select(Trove.class, false, "name = ? or name = ?",new String[]{names,names_tw}, null, null, null, null).get(0));
		}
		if(item.getType().equals("港口")){
			need_txt.setText("港口区域");
			misson_txt.setText("许可证");
		}
		//区分获取类别
		if(item.getGetWay()==3){
			need_txt.setText("钓鱼地点");
		}else if(item.getGetWay()==2){
			need_txt.setText("书库地图");
		}else if(item.getGetWay()==5){
			need_txt.setText("地下城");
		}
		name.setText(item.getName());
		try {
			pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.TROVE+item.getPic_id()+".jpg")));
		} catch (IOException e) {
			pic.setImageResource(R.drawable.dol_trove_defalut);
		}
		rate.setRating(item.getRate());
		feat.setText(String.valueOf(item.getFeats()));
		card.setText(String.valueOf(item.getCard_point()));

		need.setText(item.getNeed());
		misson.setText(item.getMisson());
		detail.setText(item.getDetails());

	}
	private String getStringByType(int i){
		String str="";
		switch(i){
			case 1:str="冒险任务";break;
			case 2:str="书库地图";break;
			case 3:str="钓鱼发现";break;
			case 6:str="港口部落";break;
		}
		return str;
	}
}