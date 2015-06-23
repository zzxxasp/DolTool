package com.key.doltool.activity.help;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.adventure.AdventureDetailActivity;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.activity.trade.TradeCityDetailActivity;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.adapter.item.WordAdapter;
import com.key.doltool.data.WordItem;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.the9tcat.hadi.DefaultDAO;

public class WordListAcitivity extends BaseActivity{
	private List<WordItem> list=new ArrayList<>();
	private ListView listView;
	private ImageButton action;
	private DefaultDAO dao;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_word_main);
		dao=SRPUtil.getDAO(this);
		findView();
		setListener();
		getSimpleActionBar().setActionBar("航海单词表",0,true);
	}
	@SuppressWarnings("unchecked")
	private void findView(){
		listView=(ListView)findViewById(R.id.listView);
		action=(ImageButton)findViewById(R.id.action);
		list=(List<WordItem>)dao.select(WordItem.class,false,"id>?",new String[]{"0"},null,null,null,null);
		listView.setAdapter(new WordAdapter(list, context));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				jump(list.get(position).zh_name,
						list.get(position).tw_name,list.get(position).type);
			}
		});
	}
	
	private void jump(String name,String tw_name,String type){
		Class<?> c;
		Intent it=null;
		if(type.equals("交易品")){
			c=TradeDetailActivity.class;
			it=new Intent(this,c);
			it.putExtra("name",name);
			it.putExtra("tw_name",tw_name);
		}
		if(type.equals("城市")){
			c=TradeCityDetailActivity.class;
			it=new Intent(this,c);
			it.putExtra("city_name",name);
			it.putExtra("tw_name",tw_name);
		}
		if(type.equals("发现物")){
			c=AdventureDetailActivity.class;
			it=new Intent(this,c);
			it.putExtra("name",name);
			it.putExtra("tw_name",tw_name);
		}
		if(type.equals("任务")){
			c=MissionDetailsActivity.class;
			it=new Intent(this,c);
			it.putExtra("find_item",name);
			it.putExtra("tw_name",tw_name);
			it.putExtra("type","word");
		}
		startActivity(it);
	}
	
	private void setListener(){
		action.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	public void showDialog(){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.panel_material_word_dialog, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.getWindow().setWindowAnimations(R.style.dialog_up);
        updateDialog.setContentView(view,params);
        final EditText zh=(EditText)view.findViewById(R.id.zh);
        final EditText tw=(EditText)view.findViewById(R.id.tw);
        final FlatButton confrim=(FlatButton)view.findViewById(R.id.confrim);
        final FlatButton cancel=(FlatButton)view.findViewById(R.id.cancel);
        confrim.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addWordByParse(zh.getText().toString().trim(),
						tw.getText().toString().trim());
				updateDialog.dismiss();
			}
		});
        cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
        updateDialog.show();
	}
	private void addWordByParse(String zh_name,String tw_name){
		ParseObject word=new ParseObject("WordBack");
		word.put("zh_name",zh_name);
		word.put("tw_name",tw_name);
		word.saveInBackground(new SaveCallback(){
			public void done(ParseException e) {
				if(e==null){
					Toast.makeText(getApplicationContext(),"感谢您对单词表的维护，下次更新即可看到您录入的单词",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
