package com.key.doltool.activity.recipe;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.RecipeAdapter;
import com.key.doltool.data.sqlite.Recipe;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.LinearLayoutForListView;
public class RecipeForTradeDetailsActivity extends BaseActivity{
	private LinearLayoutForListView list;
	private List<Recipe> mylist=new ArrayList<>();
	private String need="";
	private int index=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_trade_list);
		flag=false;
		initToolBar(null);
		list=(LinearLayoutForListView)findViewById(R.id.listview);
		need=getIntent().getStringExtra("item");
		if(need!=null){
			toolbar.setTitle(need);
			index=1;
		}else{
			index=2;
			need=getIntent().getStringExtra("name");
			toolbar.setTitle(need+"相关配方");
		}
		init();
	}
	private void init(){
		if(index==1){
			mylist=SRPUtil.getInstance(this).select(Recipe.class,false,"name like ?",new String[]{"%"+need+"%"},null,null,null,null);
		}else if(index==2){
			mylist=SRPUtil.getInstance(this).select(Recipe.class,false,"need like ? or result like ?",new String[]{"%"+need+"%","%"+need+"%"},null,null,null,null);
		}
		list.setAdapter(new RecipeAdapter(mylist,this));
	}
}
