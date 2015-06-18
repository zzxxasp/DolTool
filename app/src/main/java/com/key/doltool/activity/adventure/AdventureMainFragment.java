package com.key.doltool.activity.adventure;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.AdventureAdapter;
import com.key.doltool.data.Trove_Count;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
public class AdventureMainFragment extends BaseFragment{
	private DefaultDAO dao;
	private ListView main_list;
	private ImageView main_menu;
	private List<Trove_Count> list_count;
	private View main;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.adventure_main_area, container,false);
		 init(view);
		 findView();
		 setListener();
		 return view; 
	}
	private void init(View view){
		list_count=new ArrayList<>();
		main=view;
		dao=SRPUtil.getDAO(getActivity());
	}

	private void findView(){
		main_list=(ListView)main.findViewById(R.id.main_list);
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.clearAnimation();
		main_menu.setVisibility(View.GONE);
		init();
		main_list.setAdapter(new AdventureAdapter(list_count,getActivity()));
	}
	@Override
	public void onResume() {
		if(UpdataList.FLAG_CHANGE_LIST==1){
			init();
			main_list.setAdapter(new AdventureAdapter(list_count,getActivity()));
			UpdataList.FLAG_CHANGE_LIST=0;
		}
		super.onResume();
	}
	private void setListener(){
		main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				jump(list_count.get(arg2).getType());
			}
		});
	}
	private void jump(int index){
		if(android.os.Build.VERSION.SDK_INT>=11){
			Intent it=new Intent(getActivity(),AdventureListNewApiActivity.class);
			it.putExtra("type", UpdataCount.name_type[index-1]);
			startActivity(it);
		}else{
			Intent it=new Intent(getActivity(),AdventureListActivity.class);
			it.putExtra("type", UpdataCount.name_type[index-1]);
			startActivity(it);
		}
	}
	@SuppressWarnings("unchecked")
	private void init(){
		list_count=(List<Trove_Count>)dao.select(Trove_Count.class, false, "type > ?",new String[]{"0"}, null, null, null, null);
	}
}