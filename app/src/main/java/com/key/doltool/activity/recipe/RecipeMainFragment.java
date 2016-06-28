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

import butterknife.BindView;

public class RecipeMainFragment extends BaseFragment{
	@BindView(R.id.gridView) GridView gridview;
	private BookEvent event;

	@Override
	public int getContentViewId() {
		return R.layout.recipe_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		event=new BookEvent();
		setListener();
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
