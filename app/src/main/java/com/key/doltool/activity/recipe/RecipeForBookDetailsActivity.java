package com.key.doltool.activity.recipe;
import java.util.ArrayList;
import java.util.List;

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
public class RecipeForBookDetailsActivity extends BaseActivity{
	private ListView list;
	private TextView name,desc_type,rank,get_way;
	private Book book;
	private List<Recipe> mylist=new ArrayList<>();
	private DefaultDAO dao;
	private String id="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		initToolBar(null);
		dao=SRPUtil.getDAO(this);
		id=getIntent().getStringExtra("id");
		findViewById();
		init();
	}
	private void findViewById(){
		list=(ListView)findViewById(R.id.listview);
		name=(TextView)findViewById(R.id.name);
		desc_type=(TextView)findViewById(R.id.details);
		rank=(TextView)findViewById(R.id.rank);
		get_way=(TextView)findViewById(R.id.get_way);		
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
