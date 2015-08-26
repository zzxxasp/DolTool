package com.key.doltool.activity.voyage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.VoyageAdapter;
import com.key.doltool.data.VoyageInfo;
import com.key.doltool.data.VoyageItem;
import com.key.doltool.event.app.VoyageEvent;

import java.util.List;

public class VoyageMainFragment extends BaseFragment{
	private GridView gridView;
	private View main;
	private List<VoyageItem> list;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.activity_voyage, container,false);
		 init(view);
		 findView();
		 setListener();
		 return view; 
	}
	private void init(View view){
		main=view;
	}

	private void findView(){
		gridView=(GridView)main.findViewById(R.id.gridview);
		VoyageInfo info=new VoyageInfo(getActivity());
		list=VoyageEvent.getItemByString(info.getData());
		gridView.setAdapter(new VoyageAdapter(list, getActivity()));
	}
	private void setListener(){
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				jump(list.get(arg2).type);
			}
		});
	}
	private void jump(int index){

	}
}