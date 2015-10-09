package com.key.doltool.activity.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.search.SearchActivity;
import com.key.doltool.adapter.VoyageAdapter;
import com.key.doltool.data.VoyageInfo;
import com.key.doltool.data.VoyageItem;
import com.key.doltool.event.VoyageEvent;

import java.util.List;

public class VoyageMainFragment extends BaseFragment{
	private GridView gridView;
	private TextView search_view;
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
		search_view=(TextView)main.findViewById(R.id.search_view);
		VoyageInfo info=new VoyageInfo(getActivity());
		list=VoyageEvent.getItemByString(info.getData());
		gridView.setAdapter(new VoyageAdapter(list, getActivity()));
	}
	private void setListener(){
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				VoyageEvent.jumpForVoyage(getActivity(),list.get(arg2));
			}
		});
		search_view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),SearchActivity.class));
			}
		});
	}
}