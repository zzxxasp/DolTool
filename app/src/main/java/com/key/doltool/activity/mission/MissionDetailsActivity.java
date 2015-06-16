package com.key.doltool.activity.mission;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.MissionConnectAdapter;
import com.key.doltool.adapter.MissionDetailsAdapter;
import com.key.doltool.data.Mission;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.LinearLayoutForListView;
import com.key.doltool.view.SlideHolder;
import com.the9tcat.hadi.DefaultDAO;

public class MissionDetailsActivity extends BaseActivity{
	private LinearLayoutForListView detail_list;
	private TextView type,name;
	private DefaultDAO dao;
	private String find_item="";
	private String tw_name="";
	private String type_txt="";
	private int souce_type=0;
	private String souce="";
	private int index=0;
	private List<Mission> list;
	private TextView txt;
	private ImageView main_menu;
	private LinearLayoutForListView before_list,after_list;
	private TextView tag1,tag2;
	private SlideHolder mSlideHolder;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mission_detail_main);
		find_item=getIntent().getStringExtra("find_item");
		tw_name=getIntent().getStringExtra("tw_name");
		type_txt=getIntent().getStringExtra("type");
		souce_type=getIntent().getIntExtra("souce_type", 0);
		index=getIntent().getIntExtra("index", 0);
		souce=getIntent().getStringExtra("souce_name");
		Log.i("find_item",""+find_item);
		Log.i("find_item",""+souce_type);
		Log.i("find_item",""+souce);
		Log.i("find_item",""+index);
		dao=SRPUtil.getDAO(this);
		findView();
		setListener();
		init();
		initMenu();
	}
	private void findView(){
		type=(TextView)findViewById(R.id.type);
		name=(TextView)findViewById(R.id.name);
		detail_list=(LinearLayoutForListView)findViewById(R.id.details);
		txt=(TextView)findViewById(R.id.null_txt);
		main_menu=(ImageView)findViewById(R.id.main_menu);
		main_menu.setVisibility(View.VISIBLE);
		before_list=(LinearLayoutForListView)findViewById(R.id.before_mission_list);
		tag1=(TextView)findViewById(R.id.before_mission_tag);
		
		after_list=(LinearLayoutForListView)findViewById(R.id.after_mission_list);
		tag2=(TextView)findViewById(R.id.after_mission_tag);
		
		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
	}
	private void setListener(){
		main_menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mSlideHolder.toggle();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		if(mSlideHolder.mCachedBitmap!=null){
			mSlideHolder.mCachedBitmap.recycle();
			mSlideHolder.mCachedBitmap=null;
			System.gc();
		}
		super.onDestroy();
	}
	
	private void initMenu(){
		if(list.size()>0){
			int total=0;
			if(!list.get(0).getBefore().equals("")){
				String[]	temp=list.get(0).getBefore().split(",");
				before_list.setAdapter(new MissionConnectAdapter(mSlideHolder,temp, this,1,name.getText().toString()));
			}else{
				tag1.setVisibility(View.GONE);
				total++;
			}
			if(!list.get(0).getAfter().equals("")){
				String[]	temp=list.get(0).getAfter().split(",");
				after_list.setAdapter(new MissionConnectAdapter(mSlideHolder,temp, this,2,name.getText().toString()));
			}else{
				tag2.setVisibility(View.GONE);
				total++;
			}
			if(total==2){
				main_menu.setVisibility(View.GONE);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void init(){
		Log.i("type",type_txt);
		if(type_txt.equals("about")){
			if(souce_type==1){
				list=(List<Mission>)dao.select(Mission.class, false, "name=? and after like ?",new String[]{find_item,"%"+souce+"%"}, null, null,null, null);
			}else if(souce_type==2){
				list=(List<Mission>)dao.select(Mission.class, false, "name=? and before like ?",new String[]{find_item,"%"+souce+"%"}, null, null,null, null);
			}	
		}else if(type_txt.equals("item")){
			list=(List<Mission>)dao.select(Mission.class, false, "id=?",new String[]{find_item}, null, null,null, null);
		}else if(type_txt.equals("word")){
			list=(List<Mission>)dao.select(Mission.class, false, "name=? or name=?",new String[]{find_item,tw_name}, null, null,null, null);
		}else if(!StringUtil.isNull(type_txt)){
			list=(List<Mission>)dao.select(Mission.class, false, "find_item=? and type=?",new String[]{find_item,type_txt}, null, null,null, null);
		}else{
			list=(List<Mission>)dao.select(Mission.class, false, "find_item=? ",new String[]{find_item}, null, null,null, null);
		}
		if(list.size()>0){
			if(list.size()==1){
				type.setText(list.get(0).getType()+"");
				name.setText(list.get(0).getName()+"");
				detail_list.setAdapter(new MissionDetailsAdapter(list.get(0), this));
			}else{
				if(list.size()>index){
					type.setText(list.get(index).getType()+"");
					name.setText(list.get(index).getName()+"");
					detail_list.setAdapter(new MissionDetailsAdapter(list.get(index), this));
				}else{
					type.setText(list.get(list.size()-1).getType()+"");
					name.setText(list.get(list.size()-1).getName()+"");
					detail_list.setAdapter(new MissionDetailsAdapter(list.get(list.size()-1), this));
				}
			}
		}else{
			txt.setVisibility(View.VISIBLE);
			main_menu.setVisibility(View.GONE);
		}
	}
}
