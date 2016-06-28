package com.key.doltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.ability.AbilityListActivity;
import com.key.doltool.activity.adc.ADCListActivity;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.job.JobListActivity;
import com.key.doltool.activity.mission.MissonListActivity;
import com.key.doltool.activity.wiki.WikiListActivity;
import com.key.doltool.activity.wiki.WikiMainActivity;
import com.key.doltool.adapter.WikiAdapter;
import com.key.doltool.data.sqlite.WikiInfo;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InfoMainFragment extends BaseFragment{
	private String order="id desc";
	@BindView(R.id.mission) Button mission;
	@BindView(R.id.skill) Button skill;
	@BindView(R.id.adc) Button adc;
	@BindView(R.id.job) Button job;
	@BindView(R.id.wiki_btn) Button wiki;
	@BindView(R.id.wiki_list) ListView listview;
	private DefaultDAO dao;
	private List<WikiInfo> list=new ArrayList<>();

	@Override
	public int getContentViewId() {
		return R.layout.info_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		dao=new DefaultDAO(getActivity());
		init();
		setListener();
	}

	private void setListener(){
		listview.setAdapter(new WikiAdapter(list, getActivity()));
		mission.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(1);
			}
		});
		skill.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(2);
			}
		});
		job.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(3);
			}
		});
		adc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(4);
			}
		});
		wiki.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(5);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent it=new Intent(getActivity(),WikiMainActivity.class);
				it.putExtra("id",list.get(arg2).getId()+"");
				startActivity(it);
			}
		});
	}
	@SuppressWarnings("unchecked")
	private void init(){
		list=(List<WikiInfo>)dao.select(WikiInfo.class,false,"id>?",new String[]{"0"},null, null,order,"0,5");
	}
	private void jump(int index){
		Class<?> c;
		switch(index){
			case 1:c=MissonListActivity.class;break;
			case 2:c=AbilityListActivity.class;break;
			case 3:c=JobListActivity.class;break;
			case 4:c=ADCListActivity.class;break;
			case 5:c=WikiListActivity.class;break;
			default:Toast.makeText(getActivity().getApplicationContext(),"还在建设中",Toast.LENGTH_SHORT).show();return;
		}
		Intent intent=new Intent(getActivity(),c);
		startActivity(intent);
	}
}
