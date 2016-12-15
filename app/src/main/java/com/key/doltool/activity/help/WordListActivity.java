package com.key.doltool.activity.help;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.activity.adventure.AdventureDetailActivity;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.activity.trade.TradeCityDetailActivity;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.adapter.item.WordAdapter;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.data.sqlite.WordItem;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WordListActivity extends BaseActivity{
	private List<WordItem> list=new ArrayList<>();
	@BindView(R.id.listView) ListView listView;
	@BindView(R.id.action) FloatingActionButton action;

	@Override
	public int getContentViewId() {
		return R.layout.help_word_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
		setListener();
		flag=false;
		initToolBar(null);
		toolbar.setTitle("航海单词表");
	}

	private void findView(){
		list=SRPUtil.getInstance(getApplicationContext()).
				select(WordItem.class, false, "id>?", new String[]{"0"}, null, null, null, null);
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
				MyAnimations.wordAnime(action,500,listener);
			}
		});
	}

	private Animator.AnimatorListener listener=new Animator.AnimatorListener(){

		@Override
		public void onAnimationStart(Animator animator) {

		}

		@Override
		public void onAnimationEnd(Animator animator) {
				showDialog();
		}

		@Override
		public void onAnimationCancel(Animator animator) {

		}

		@Override
		public void onAnimationRepeat(Animator animator) {

		}
	};

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
		updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
			public void onDismiss(DialogInterface dialog) {
				MyAnimations.wordAnime2(action, 500);
			}
		});
        updateDialog.show();
	}
	private void addWordByParse(String zh_name,String tw_name){
		AVObject word=new AVObject("WordBack");
		word.put("zh_name",zh_name);
		word.put("tw_name",tw_name);
		word.saveInBackground(new SaveCallback(){
			public void done(AVException e) {
				if(e==null){
					Toast.makeText(getApplicationContext(),"感谢您对单词表的维护，下次更新即可看到您录入的单词",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
