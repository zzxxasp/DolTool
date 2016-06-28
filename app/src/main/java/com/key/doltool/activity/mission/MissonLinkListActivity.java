package com.key.doltool.activity.mission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.MissionItemAdapter;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MissonLinkListActivity extends BaseActivity{

	@BindView(R.id.listview) ListView listview;
	private MissionItemAdapter adapter;
	private String select_if;
	private List<Mission> list_mission=new ArrayList<>();
	private List<String> if_args=new ArrayList<>();

	private DefaultDAO dao;

	@Override
	public int getContentViewId() {
		return R.layout.mission_list_main;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		dao=SRPUtil.getDAO(this);
		selectInit();
		findView();
	}

	//通用findView
	private void findView() {
		initPage();
		flag=false;
		initToolBar(null);
		toolbar.setTitle("任务委托所");
	}
	private void initPage(){
		selectshow();
		adapter=new MissionItemAdapter(list_mission,this);
		listview.setAdapter(adapter);
		initPageItem();
	}
	private void initPageItem(){
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				jump(position);
			}
		});
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				if (list_mission.get(position).getTag() == 0) {
					Mission mission = new Mission();
					mission.setTag(1);
					list_mission.get(position).setTag(1);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{"" + list_mission.get(position).getId()});
				} else {
					Mission mission = new Mission();
					mission.setTag(0);
					list_mission.get(position).setTag(0);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{"" + list_mission.get(position).getId()});
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});
	}

	private void selectshow(){
		list_mission=SRPUtil.getInstance(getApplicationContext()).select(Mission.class, false,select_if, StringUtil.listToArray(if_args),
				null, null,"level asc,skill_need asc",null);
	}

	private void jump(int position){
		Intent intent=new Intent(this,MissionDetailsActivity.class);
		intent.putExtra("find_item", list_mission.get(position).getId() + "");
		intent.putExtra("type","item");
		startActivity(intent);
	}

	private void selectInit(){
		select_if="id>?";
		if_args.clear();
		if_args.add("0");
	}
}