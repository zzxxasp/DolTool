package com.key.doltool.activity.adventure.card;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.key.doltool.R;
import com.key.doltool.activity.BaseAdventureActivity;
import com.key.doltool.adapter.CardAdapter;
import com.key.doltool.adapter.ListWordAdapter;
import com.key.doltool.adapter.item.NameValueAdapter;
import com.key.doltool.data.base.NameValueItem;
import com.key.doltool.data.parse.Deck;
import com.key.doltool.data.sqlite.Card;
import com.key.doltool.data.sqlite.CardCombo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CardListActivity extends BaseAdventureActivity{
	@BindView(R.id.null_txt) TextView txt;
	@BindView(R.id.gridview) GridView gridview;
	@BindView(R.id.card_point) TextView point_show;
	@BindView(R.id.card_number) TextView card_show;
	@BindView(R.id.card_bar) RelativeLayout card_bar;
	@BindView(R.id.cal_btn) FlatButton cal_btn;

	private Dialog alert,dialog;
	//总卡组列表
	private List<Card> list;
	//我的卡组列表
	private List<Card> temp;
	//搜索条件执行后的列表
	private List<Card> search;
	private DefaultDAO dao;
	private SRPUtil srp;
	private String select_txt="";
	private CardAdapter mGridAdapter;
	private boolean return_flag=false;
	private boolean back_flag=false;
	private boolean showFlag=true;
	/**comboList**/
	private List<CardCombo> comboList=new ArrayList<>();
	/**自己牌组的Combo总数**/
	private List<CardCombo> selfCombo=new ArrayList<>();
	private boolean MODE=false;
	private int limit=0;
	private int poiont_total;
	private int value;
	private List<NameValueItem> in_list=new ArrayList<>();
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					//更新页面
					if(!isFinishing()){
						alert.dismiss();
					}
					Parcelable state = gridview.onSaveInstanceState();
					mGridAdapter=new CardAdapter(list,CardListActivity.this);
					gridview.setAdapter(mGridAdapter);
					gridview.onRestoreInstanceState(state);
					if(return_flag){
						Toast.makeText(getApplicationContext(),R.string.search_rest, Toast.LENGTH_LONG).show();
						return_flag=false;
					}
					if(back_flag){
						barshow();
						back_flag=false;
					}
					break;
				case 1:
					//战斗力计算成功显示明细窗体
					showBattle(msg.obj);
					break;
				case 2:
					if(!isFinishing()){
						alert.dismiss();
					}
					if(list.size()==0){
						txt.setVisibility(View.VISIBLE);
					}else{
						txt.setVisibility(View.GONE);
					}
					mGridAdapter=new CardAdapter(search,CardListActivity.this);
					gridview.setAdapter(mGridAdapter);
					break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case 100:
				if(!isFinishing()){
					alert.show();
				}
				back_flag=true;
				new Thread(mTask).start();
				break;
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.card_list_show;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		srp=SRPUtil.getInstance(getApplicationContext());
		dao=SRPUtil.getDAO(getApplicationContext());
		dialog=new DialogEvent().itemDialog(this, getResources().getString(R.string.common_wait));
		list=new ArrayList<>();
		findView();
		setListener();
		new Thread(mTasks).start();
	}

	private void findView(){
		alert=new DialogEvent().showLoading(this);
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle(R.string.card_title);
		if(!isFinishing()){
			alert.show();
		}
	}

	@SuppressWarnings("unchecked")
	public void select(String select){
		//类型，点数范围，名称
		//全部~X~0-10
		select_txt=select;
		Log.i("s",""+select);
		String[] temp=select.split("~");
		String type="",name ="",range="",if_txt = "",range_temp[];
		List<String> if_list=new ArrayList<>();
		if(!temp[0].equals("全部")){
			type=temp[0];
		}
		if(!StringUtil.isNull(temp[1])){
			name=temp[1];
		}
		if(!temp[2].equals("0-10")){
			range=temp[2];
		}
		if(!StringUtil.isNull(name)){
			if_txt+="name like ?";
			if_list.add("%"+name+"%");
		}else{
			if_txt+="id>?";
			if_list.add("0");
		}
		if(!StringUtil.isNull(type)){
			if_txt+=" and type=?";
			if_list.add(type);
		}
		if(!StringUtil.isNull(range)){
			range_temp=range.split("-");
			int min=Integer.parseInt(range_temp[0]);
			int max=Integer.parseInt(range_temp[1]);
			if(min==max){
				if(min==0){
					min=1;
				}
				if_list.add(min+"");
				if_txt+=" and point=?";
			}else{
				max+=1;
				if_list.add(min+"");
				if_list.add(max+"");
				if_txt+=" and point>? and point<?";
			}
		}
		if(!isFinishing()){
			alert.show();
		}
		final String if_txt_final=if_txt;
		final List<String> if_final_list=if_list;
		new Thread(new Runnable() {
			@Override
			public void run() {
				search=(List<Card>)dao.select(Card.class,false,if_txt_final,StringUtil.listToArray(if_final_list),null, null,"flag desc,point desc,type desc", null);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(2));
			}
		}).start();
	}
	private void setListener(){
		mGridAdapter=new CardAdapter(list,this);
		gridview.setAdapter(mGridAdapter);
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
				if (mGridAdapter.getItem(position).flag == 0) {
					Card trove = new Card();
					trove.flag = 1;
					mGridAdapter.getItem(position).flag = 1;
					dao.update(trove, new String[]{"flag"}, "id=?", new String[]{"" + mGridAdapter.getItem(position).id});
				} else {
					Card trove = new Card();
					trove.flag = 0;
					mGridAdapter.getItem(position).flag = 0;
					dao.update(trove, new String[]{"flag"}, "id=?", new String[]{"" + mGridAdapter.getItem(position).id});
				}
				deckChange(position);
				mGridAdapter.notifyDataSetChanged();
				return true;
			}
		});
		point_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPointLimit();
			}
		});
		//计算战斗力，如果卡组满足三十张，合计点数没有超过设定点数
		cal_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(showFlag){
					showFlag=false;
					new Thread(mTasks_point).start();
				}
			}
		});
	}


	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!select_txt.equals("")){
			select_txt="";
			if(!isFinishing()){
				alert.show();
			}
			new Thread(mTasks).start();
			return_flag=true;
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}
	/**
	 * 回调更新任务
	 * **/
	private Runnable mTask =new Runnable(){
		public void run() {
			list=SRPUtil.getInstance(getApplicationContext()).select(Card.class, false, "id>?",new String[]{"0"}, null, null,"flag desc,point desc,type desc", null);
			temp=SRPUtil.getInstance(getApplicationContext()).select(Card.class, false, "flag=?",new String[]{"1"}, null, null,"flag desc,point desc,type desc", null);
			poiont_total=0;
			for(int i=0;i<temp.size();i++){
				poiont_total+=temp.get(i).point;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mHandler.sendMessage(mHandler.obtainMessage(0));
		}
	};
	@SuppressWarnings("unchecked")
	private Runnable mTasks =new Runnable(){
		public void run() {
			list=(List<Card>)dao.select(Card.class, false, "id>?",new String[]{"0"}, null, null,"flag desc,point desc,type desc", null);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mHandler.sendMessage(mHandler.obtainMessage(0));
		}
	};
	//战斗力计算任务
	private Runnable mTasks_point =new Runnable(){
		public void run() {
			int temp=initPoint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mHandler.sendMessage(mHandler.obtainMessage(1,temp));
		}
	};

	//开启自设卡组模式
	@SuppressWarnings("unchecked")
	private void startmode(){
		poiont_total=0;
		MODE=!MODE;
		if(!MODE){
			card_bar.setVisibility(View.GONE);
			return;
		}
		temp=(List<Card>)dao.select(Card.class, false, "flag=?",new String[]{"1"}, null, null,"flag desc,point desc,type desc", null);
		for(int i=0;i<temp.size();i++){
			poiont_total+=temp.get(i).point;
		}
		barshow();
		card_bar.setVisibility(View.VISIBLE);
	}
	private void barshow(){
		card_show.setText("卡组："+temp.size()+"/30");
		if(limit!=0){
			point_show.setText(poiont_total + "/" + limit);
		}else{
			point_show.setText("无限制");
		}
		//如果卡片数超过30则，编辑模式会不允许进行计算
		if(temp.size()!=30||(poiont_total>limit&&limit!=0)){
			cal_btn.setEnabled(false);
		}else{
			cal_btn.setEnabled(true);
		}
	}
	//卡组变更
	private void deckChange(int position){
		Card item=mGridAdapter.getItem(position);
		if(MODE){
			if(item.flag==1){
				poiont_total+=item.point;
				temp.add(item);
			}else{
				for(int i=0;i<temp.size();i++){
					if(temp.get(i).equals(item)){
						poiont_total-=item.point;
						temp.remove(item);
					}
				}
			}
			barshow();
		}
	}
	@SuppressWarnings("unchecked")
	private void getCombo(){
		if(comboList.size()==0){
			comboList=(List<CardCombo>)dao.select(CardCombo.class,true,"id>?",new String[]{"0"},null,null,null,null);
		}
		for(int i=0;i<comboList.size();i++){
			List<Card> temp=comboList.get(i).getCard();
			if(this.temp.containsAll(temp)) {
				selfCombo.add(comboList.get(i));
			}
		}
	}
	//初始化评估战力(原则)
	private int initPoint(){
		in_list.clear();
		selfCombo.clear();
		int type_add;
		int combo_add = 0;
		int random_add;
		int total = 0;
		getCombo();
		//基本点数叠加
		total +=poiont_total;
		//种类加成（2种类型为0 1种-5 每多一种Plus 5）
		int type_number = (int) srp.countByType(true, 2);
		type_add =(type_number -2)*2;
		in_list.add(new NameValueItem("卡组类型", type_add));
		total+= type_add;
		//combo加成(根据Combo的点数，除以所需卡片数，功能性combo均为10)
		for(int i=0;i<selfCombo.size();i++){
			combo_add +=selfCombo.get(i).value;
			in_list.add(new NameValueItem(selfCombo.get(i).getName(), selfCombo.get(i).value));
		}
		total+= combo_add;
		//随机点数加成[1~20]
		random_add =NumberUtil.getRandom(0,20);
		in_list.add(new NameValueItem("随机数", random_add));
		total += random_add;
		return total;
	}

	//显示点数限制，进行修改
	private void showPointLimit(){
		//选择类型0/100/150/200
		final String[] word=getResources().getStringArray(R.array.card_limit);
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.pop_word, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(view, params);
		updateDialog.show();
		ListView listview=(ListView)view.findViewById(R.id.listview);
		listview.setAdapter(new ListWordAdapter(word, context));
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if(arg2!=0){
					limit=Integer.parseInt(word[arg2]);
				}else{
					limit=0;
				}
				barshow();
				updateDialog.dismiss();
			}
		});
	}

	//显示牌组战斗力统计，提供保存功能
	private void showBattle(Object total){
		View layout=getLayoutInflater().inflate(R.layout.pop_card_cal, null);
		final Dialog showdialog = new Dialog(this,R.style.updateDialog);
		showdialog.setCancelable(true);
		showdialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(this)-30,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(10, 10, 10, 10);
		showdialog.setContentView(layout, params);
		showdialog.show();
		final TextView base=(TextView)layout.findViewById(R.id.base);
		final TextView message=(TextView)layout.findViewById(R.id.message);
		final ListView add_list=(ListView)layout.findViewById(R.id.add_list);
		final Button positive=(Button)layout.findViewById(R.id.confrim);
		final Button negative=(Button)layout.findViewById(R.id.cancel);
		value=Integer.parseInt(total.toString());
		base.setText("总点数:" + poiont_total + "(" + total + ")");
		if(limit!=0){
			message.setText(poiont_total + "/" + limit);
		}else{
			message.setText("无限制");
		}
		add_list.setAdapter(new NameValueAdapter(in_list,this));
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showdialog.dismiss();
			}
		});
		positive.setText("分享");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showdialog.dismiss();
				showEditDialog();
			}
		});
		showFlag=true;
	}
	public void showEditDialog(){
		View layout=getLayoutInflater().inflate(R.layout.select_trove, null);
		final Dialog updateDialog = new Dialog(this, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(this)-30,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(10,10,10,10);
		updateDialog.setContentView(layout,params);
		updateDialog.show();
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		name.setHint("请输入卡组的名称");
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveCardCombo(name.getText().toString());
				updateDialog.dismiss();
			}
		});
	}

	public void saveCardCombo(String name){
		//显示等待对话框
		if(!isFinishing()){
			dialog.show();
		}
		Deck item=new Deck();
		List<Integer> list=new ArrayList<>();
		for(int i=0;i<temp.size();i++){
			list.add(temp.get(i).id);
		}
		Gson g=new Gson();
		item.setCard_list(g.toJson(list));
		if(limit!=0){
			item.setLimit(value + "/" + limit);
		}else{
			item.setLimit("无限制");
		}
		item.setName(name);
		if(AVUser.getCurrentUser()!=null){
			item.setUserName(AVUser.getCurrentUser().getString("nickName"));
		}else{
			item.setUserName("游客");
		}
		item.setValue(poiont_total);
		item.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				if (e != null) {
					Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
				}
				if(!isFinishing()){
					dialog.dismiss();
				}
			}
		});
	}

	public void popWindow(){
		//对话框显示，输入返回搜索条件
		View xc=getLayoutInflater().inflate(R.layout.select_card, null);
		ViewUtil.popCardDialog(this, xc);
	}

	private void jump(){
		Intent it=new Intent(this,ShareCardDeckActivity.class);
		startActivityForResult(it,100);
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				//搜索
				case R.id.city_search:popWindow();break;
				//模式更改
				case R.id.type_search:startmode();break;
				//分享
				case R.id.share:jump();break;
			}
			return true;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.card_menu, menu);
		return true;
	}
}