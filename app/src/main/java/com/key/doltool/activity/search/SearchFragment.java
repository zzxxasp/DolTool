package com.key.doltool.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.adc.ADCDetailsActivity;
import com.key.doltool.activity.adventure.AdventureDetailActivity;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.activity.recipe.RecipeForBookDetailsActivity;
import com.key.doltool.activity.trade.TradeCityDetailActivity;
import com.key.doltool.activity.useitem.UseItemActivity;
import com.key.doltool.adapter.SimpleSearchAdapter;
import com.key.doltool.data.item.UseItem;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.data.sqlite.Book;
import com.key.doltool.data.sqlite.City;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.data.SearchItem;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.data.sqlite.WordItem;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.TradeEvent;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速查询界面
 * **/
public class SearchFragment extends BaseFragment{
	private DefaultDAO dao;
	private List<SearchItem> list;
	private EditText search_txt;
	private ListView content_list;
	private TextView type;
	private int index=0;
	private PopupWindow pop;
	private DialogEvent dialogEvent;
    private View main;
	private boolean end=true;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.search_main, container,false);
		 init(view);
		 findView();
		 setListener();
		 return view; 
	}
	private void init(View view){
		main=view;
		dao=SRPUtil.getDAO(getActivity());
		dialogEvent=new DialogEvent(pop,SearchFragment.this);
	}
	
	private void findView(){
		search_txt=(EditText)main.findViewById(R.id.search);
		content_list=(ListView)main.findViewById(R.id.content_list);
		type=(TextView)main.findViewById(R.id.type);
	}

	
	private void setListener(){
		search_txt.addTextChangedListener(watcher);
		content_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				jump(index,list.get(position).id);
			}
		});
		type.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogEvent.popMenuForShare(type);
			}
		});
	}
	
	private void jump(int index,int id){
		Class<?> c;
		Intent it=null;
		switch(index){
			case 0:
				c=MissionDetailsActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("find_item",id+"");
				it.putExtra("type","item");
				break;
			case 1:
				c=AdventureDetailActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("id",id+"");
				break;
			case 2:
				c=ADCDetailsActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("id",id+"");
				break;
			case 3:
				c=RecipeForBookDetailsActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("id",id+"");
				break;
			case 4:
				c=TradeCityDetailActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("id",id+"");
				break;
			case 5:
				c=UseItemActivity.class;
				it=new Intent(getActivity(),c);
				it.putExtra("id",id+"");
				break;
		}
		startActivity(it);
	}
	
	private List<SearchItem> getListBySelect(){
		List<SearchItem> result=new ArrayList<>();
		Class<?> souces;
		switch(index){
			case 0:souces=Mission.class;break;
			case 1:souces=Trove.class;break;
			case 2:souces=ADCInfo.class;break;
			case 3:souces=Book.class;break;
			case 4:souces=City.class;break;
			case 5:souces=UseItem.class;break;
			default:souces=City.class;break;
		}
		String search_str="%" + search_txt.getText().toString().trim() + "%";
		List<WordItem> wordList=SRPUtil.getInstance(getActivity()).select
				(WordItem.class, false, "zh_name like ? or tw_name like ?", new String[]{search_str, search_str}, null, null, null, null);
		List<?> temp;
		if(wordList.size()!=0){
			temp=dao.select(souces,false,"name like ? or name = ? or name = ?",new String[]{search_str,wordList.get(0).tw_name,wordList.get(0).zh_name}, null, null, null, null);
		}else{
			temp=dao.select(souces, false,"name like ?", new String[]{search_str}, null, null, null, null);
		}
		for(int i=0;i<temp.size();i++){
			result.add(getNameByType(temp.get(i),index));
		}
		return result;
	}
	private SearchItem getNameByType(Object obj,int type){
		SearchItem result=new SearchItem();
		switch(type){
			case 0:
				Mission a=(Mission)obj;
				result.name=a.getName();
				result.id=a.getId();
				result.add_info=a.getFind_item();
				break;
			case 1:
				Trove b=(Trove)obj;
				result.name=b.getName();
				result.id=b.getId();
				result.add_info=b.getType();
				break;
			case 2:
				ADCInfo c=(ADCInfo)obj;
				result.name=c.getName();
				result.id=c.getId();
				result.add_info=c.getCity();
				break;
			case 3:
				Book d=(Book)obj;
				result.name=d.getName();
				result.id=d.getId();
				String temp3=new TradeEvent(getActivity()).getRecipeTypeByIndex(d.getType());
				result.add_info=temp3+":"+d.getRange();
				break;
			case 4:
				City e=(City)obj;
				result.name=e.getName();
				result.id=e.getId();
				result.add_info=e.getArea();
				break;
			case 5:
				UseItem f=(UseItem)obj;
				result.name=f.name;
				result.id=f.id;
				result.add_info=f.type.split(",")[0];
				break;
		}
		return result;
	}
	public void setIndex(int number){
		index=number;
	}


	private Runnable mTask=new Runnable() {
		@Override
		public void run() {
			list=getListBySelect();
			updateUi.sendMessage(updateUi.obtainMessage());
		}
	};

	private Handler updateUi=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			content_list.setAdapter(new SimpleSearchAdapter(getActivity(),list));
			end=true;
		}
	};

	private TextWatcher watcher = new TextWatcher() {
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	if(search_txt.getText().toString().trim().length()>=2){
				if(end){
					end=false;
					new Thread(mTask).start();
				}
	    	}
	    }
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    	
	    }
	    public void afterTextChanged(Editable s) {

	    }
	};
}
