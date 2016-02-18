package com.key.doltool.activity.mission;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.data.MissionTree;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

public class MissionLineActivity extends BaseActivity{
	private DefaultDAO dao;
	private MissionTree tree;
	private String name="石头的记忆";
	private int level=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	private void init(){
		dao=SRPUtil.getDAO(this);
		tree=new MissionTree();
		tree.setLevel(0);
		tree.setName(name);
		tree.setBefore_mission(getBeforeLineData(name));
		showTree(tree);
	}
	private List<MissionTree> getBeforeLineData(String name){
		Mission item ;
		List<?> list_mission=dao.select(Mission.class,false,"name=?",new String[]{name}, null, null, null, null);
		List<MissionTree> list=new ArrayList<>();
		if(list_mission.size()>0){
			level++;
			item=(Mission)list_mission.get(0);
			String[] temp=item.getBefore().split(",");
			for (String aTemp : temp) {
				MissionTree items = new MissionTree();
				items.setLevel(level);
				items.setName(aTemp);
				items.setBefore_mission(getBeforeLineData(aTemp));
				list.add(items);
			}
		}
		return list;
	}
	private void showTree(MissionTree tree){
		Log.i("x",tree.getName());
		Log.i("x",tree.getLevel()+"");
		for(int i=0;i<tree.getBefore_mission().size();i++){
			Log.i("x",tree.getBefore_mission().size()+"");
			showTree(tree.getBefore_mission().get(i));
		}
	}
}