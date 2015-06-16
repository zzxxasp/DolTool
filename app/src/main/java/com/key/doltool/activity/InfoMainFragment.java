package com.key.doltool.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import com.key.doltool.data.WikiInfo;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

public class InfoMainFragment extends BaseFragment{
	private ImageView main_menu;
	private String order="id desc";
	private Button mission,skill,adc,job,wiki;
	private ListView listview;
	private DefaultDAO dao;
	private List<WikiInfo> list=new ArrayList<WikiInfo>();
    private View main;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.info_main, container,false);
		 init(view);
		 findView();
		 init();
		 setListener();
		 return view; 
	}
	private void init(View view){
		main=view;
		dao=new DefaultDAO(getActivity());
	}
	
	private void findView(){
		mission=(Button)main.findViewById(R.id.mission);
		skill=(Button)main.findViewById(R.id.skill);
		adc=(Button)main.findViewById(R.id.adc);
		job=(Button)main.findViewById(R.id.job);
		wiki=(Button)main.findViewById(R.id.wiki_btn);
		listview=(ListView)main.findViewById(R.id.wiki_list);
		
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.setVisibility(View.GONE);
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
			default:Toast.makeText(getActivity(),"还在建设中",Toast.LENGTH_SHORT).show();return;
		}
		Intent intent=new Intent(getActivity(),c);
		startActivity(intent);
	}
}
