package com.key.doltool.activity.recipe;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.RecipeAdapter;
import com.key.doltool.data.sqlite.Book;
import com.key.doltool.data.sqlite.Recipe;
import com.key.doltool.event.BookEvent;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeForBookDetailsActivity extends BaseActivity{

	@BindView(R.id.listview) ListView list;
	@BindView(R.id.name) TextView name;
	@BindView(R.id.details) TextView desc_type;
	@BindView(R.id.rank) TextView rank;
	@BindView(R.id.get_way) TextView get_way;

	private Book book;
	private List<Recipe> mylist=new ArrayList<>();
	private DefaultDAO dao;
	private String id="";
	@Override
	public int getContentViewId() {
		return R.layout.recipe_list;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		initToolBar(null);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		init();
	}

	private void init(){
		book=(Book)dao.select(Book.class,false,"id=?",new String[]{id},null,null,null,null).get(0);
		mylist=SRPUtil.getInstance(this).select(Recipe.class,false,"parent_name=?",new String[]{""+book.getName()},null,null,null,null);
		list.setAdapter(new RecipeAdapter(mylist,this));
		name.setText(book.getName());
		desc_type.setText(book.getDesc_type());
		rank.setText(new BookEvent().TYPE_BASE[book.getType()]+":"+book.getRange());
		get_way.setText(book.getGet_way());
	}
}
