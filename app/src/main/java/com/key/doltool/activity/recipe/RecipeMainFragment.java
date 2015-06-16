package com.key.doltool.activity.recipe;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.RecipeMainAdapter;
import com.key.doltool.event.BookEvent;

public class RecipeMainFragment extends BaseFragment{
	private GridView gridview;
	private BookEvent event;
	private View main;
	private ImageView main_menu;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.recipe_main, container,false);
		 init(view);
		 findView();
		 setListener();
		 return view; 
	}
	private void init(View view){
		main=view;
	}
	private void findView(){
		event=new BookEvent();
		gridview=(GridView)main.findViewById(R.id.gridView);
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.setVisibility(View.GONE);
	}
	private void setListener(){
		gridview.setAdapter(new RecipeMainAdapter(event.TYPE_BASE2,event.TYPE_BASE_PIC,getActivity()));
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				jump(position);
			}
		});
	}
	private void jump(int position){
		Intent it=new Intent(getActivity(),RecipeBookListActivity.class);
		it.putExtra("types",position+1);
		startActivity(it);
	}
}
