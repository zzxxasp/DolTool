package com.key.doltool.activity.recipe;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.RecipeAdapter;
import com.key.doltool.data.Recipe;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.LinearLayoutForListView;
import com.the9tcat.hadi.DefaultDAO;
public class RecipeForTradeDetailsActivity extends BaseActivity{
	private LinearLayoutForListView list;
	private List<Recipe> mylist=new ArrayList<>();
	private DefaultDAO dao;
	private String need="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		list=(LinearLayoutForListView)findViewById(R.id.listview);
		need=getIntent().getStringExtra("");
		dao=SRPUtil.getDAO(this);
		init();
		list.setAdapter(new RecipeAdapter(mylist,this));
	}
	@SuppressWarnings("unchecked")
	private void init(){
		mylist=(List<Recipe>)dao.select(Recipe.class,false,"need like ?",new String[]{"%"+need+"%"},null,null,null,null);	
	}
}
