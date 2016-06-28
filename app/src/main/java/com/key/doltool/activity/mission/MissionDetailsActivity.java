package com.key.doltool.activity.mission;

import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.MissionConnectAdapter;
import com.key.doltool.adapter.MissionDetailsAdapter;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;
import com.key.doltool.view.LinearLayoutForListView;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import butterknife.BindView;

public class MissionDetailsActivity extends BaseActivity{
	@BindView(R.id.details) LinearLayoutForListView detail_list;
	@BindView(R.id.type) TextView type;
	@BindView(R.id.name) TextView name;
	@BindView(R.id.null_txt) TextView txt;
	private DefaultDAO dao;
	private String find_item="";
	private String tw_name="";
	private String type_txt="";
	private int souce_type=0;
	private String souce="";
	private int index=0;
	private List<Mission> list;

	private int temp=0;

	@Override
	public int getContentViewId() {
		return R.layout.mission_detail;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		find_item=getIntent().getStringExtra("find_item");
		tw_name=getIntent().getStringExtra("tw_name");
		type_txt=getIntent().getStringExtra("type");
		souce_type=getIntent().getIntExtra("souce_type", 0);
		index=getIntent().getIntExtra("index", 0);
		souce=getIntent().getStringExtra("souce_name");
		dao=SRPUtil.getDAO(this);
		findView();
		init();
	}

	private void findView(){
		flag=false;
		initToolBar(onMenuItemClick);
	}

	
	private void initMenu(){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.mission_connect, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(view, params);
		FlowLayout before_list=(FlowLayout)view.findViewById(R.id.before_mission_list);
		TextView tag1=(TextView)view.findViewById(R.id.before_mission_tag);
		FlowLayout after_list=(FlowLayout)view.findViewById(R.id.after_mission_list);
		TextView tag2=(TextView)view.findViewById(R.id.after_mission_tag);
		if(list.size()>0){
			int total=0;
			if(!list.get(0).getBefore().equals("")){
				String[] temp=list.get(0).getBefore().split(",");
				before_list.setAdapter(new MissionConnectAdapter(temp,this,1,name.getText().toString()));
			}else{
				tag1.setVisibility(View.GONE);
				before_list.setVisibility(View.GONE);
				total++;
			}
			if(!list.get(0).getAfter().equals("")){
				String[] temp=list.get(0).getAfter().split(",");
				after_list.setAdapter(new MissionConnectAdapter(temp,this,2,name.getText().toString()));
			}else{
				tag2.setVisibility(View.GONE);
				after_list.setVisibility(View.GONE);
				total++;
			}
			if(total==2){
				temp=total;
				Toast.makeText(getApplicationContext(),"无关联任务",Toast.LENGTH_SHORT).show();
			}else{
				temp=total;
				updateDialog.show();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void init(){
		Log.i("type",type_txt+"");
		if(StringUtil.isNull(type_txt)){
			list=(List<Mission>)dao.select(Mission.class, false, "find_item=? ",new String[]{find_item}, null, null,null, null);
		} else if(type_txt.equals("about")){
			if(souce_type==1){
				list=(List<Mission>)dao.select(Mission.class, false, "name=? and after like ?",new String[]{find_item,"%"+souce+"%"}, null, null,null, null);
			}else if(souce_type==2){
				list=(List<Mission>)dao.select(Mission.class, false, "name=? and before like ?",new String[]{find_item,"%"+souce+"%"}, null, null,null, null);
			}	
		}else if(type_txt.equals("item")){
			list=(List<Mission>)dao.select(Mission.class, false, "id=?",new String[]{find_item}, null, null,null, null);
		}else if(type_txt.equals("word")){
			list=(List<Mission>)dao.select(Mission.class, false, "name=? or name=?",new String[]{find_item,tw_name}, null, null,null, null);
		}else if(type_txt.equals("link")){
			list=(List<Mission>)dao.select(Mission.class, false, "name=?",new String[]{find_item}, null, null,null, null);
		}
		else if(!StringUtil.isNull(type_txt)){
			list=(List<Mission>)dao.select(Mission.class, false, "find_item=? and type=?",new String[]{find_item,type_txt}, null, null,null, null);
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
		}
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:initMenu();break;
			}
			return true;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mission_connect, menu);
		return true;
	}
}
