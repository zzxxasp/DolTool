package com.key.doltool.activity.adventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.event.UpdataList;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.io.IOException;
public class AdventureDetailActivity extends BaseActivity{
	private DefaultDAO dao;
	//信息
	private TextView feat,card,need,misson,name,detail;
	private ImageView pic;
	private RatingBar rate;
	//数据
	private Trove item;
	//附加
	private TextView need_txt,misson_txt;
	private int flag=5;
	private FlatButton goto_mission;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initToolBar(null);
		setContentView(R.layout.adventure_details);
		dao=SRPUtil.getDAO(this);
		findView();
		setListener();
		init();
	}
	private void findView(){
		name=(TextView)findViewById(R.id.txt);
		pic=(ImageView)findViewById(R.id.img);
		ViewCompat.setTransitionName(pic,"image");
		rate=(RatingBar)findViewById(R.id.star);
		feat=(TextView)findViewById(R.id.feats);
		card=(TextView)findViewById(R.id.card_point);
		need=(TextView)findViewById(R.id.need);
		misson=(TextView)findViewById(R.id.misson);
		detail=(TextView)findViewById(R.id.details);
		
		need_txt=(TextView)findViewById(R.id.misson_txt);
		misson_txt=(TextView)findViewById(R.id.need_txt);
		goto_mission=(FlatButton)findViewById(R.id.goto_mission);
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

	@Override
	public void finish() {
		if(flag!=item.getFind_flag()){
			UpdataList.FLAG_CHANGE=1;
		}else{
			UpdataList.FLAG_CHANGE=0;
		}
		super.finish();
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
		flag=item.getFind_flag();
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
		feat.setText(item.getFeats()+"");
		card.setText(item.getCard_point()+"");
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