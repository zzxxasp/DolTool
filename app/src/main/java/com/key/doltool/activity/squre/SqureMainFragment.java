package com.key.doltool.activity.squre;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.view.Toast;
/**
 * 广场的人们
 * **/
public class SqureMainFragment extends BaseFragment{
	private ImageView main_menu;
	private LinearLayout map,fortune;
    private View main;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.squre_main, container,false);
		 init(view);
		 findView();
		 setListener();
		 return view; 
	}
	private void init(View view){
		main=view;
	}
	
	private void findView(){
		map=(LinearLayout)main.findViewById(R.id.map);
		fortune=(LinearLayout)main.findViewById(R.id.fortune);
		
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.setVisibility(View.GONE);
	}
	private void setListener(){
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(1);
			}
		});
		fortune.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(2);
			}
		});
	}
	private void jump(int index){
		Class<?> c = null;
		switch(index){
			case 1:c=MapActivity.class;break;
			case 2:c=FortuneActivity.class;break;
			default:Toast.makeText(getActivity(),"还在建设中",Toast.LENGTH_SHORT).show();return;
		}
		Intent intent=new Intent(getActivity(),c);
		startActivity(intent);
	}
}
