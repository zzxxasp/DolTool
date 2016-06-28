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

import butterknife.BindView;

public class RecipeForTradeDetailsActivity extends BaseActivity{
	@BindView(R.id.listview) LinearLayoutForListView list;
	private List<Recipe> mylist=new ArrayList<>();
	private String need="";
	private int index=0;

	@Override
	public int getContentViewId() {
		return R.layout.recipe_trade_list;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
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
