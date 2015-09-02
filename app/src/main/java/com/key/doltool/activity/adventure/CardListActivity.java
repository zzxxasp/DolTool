package com.key.doltool.activity.adventure;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseAdventureActivity;
import com.key.doltool.adapter.CardAdapter;
import com.key.doltool.data.Card;
import com.key.doltool.data.CardCombo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends BaseAdventureActivity{
	private GridView gridview;
	private Dialog alert;
	//总卡组列表
	private List<Card> list;
	//我的卡组列表
	private List<Card> temp;
	//搜索条件执行后的列表
	private List<Card> search;
	private DefaultDAO dao;
	private SRPUtil srp;
	private TextView txt;
	private String select_txt="";
	private CardAdapter mGridAdapter;

	private TextView point_show,card_show;
	private FlatButton cal_btn;

	/**comboList**/
	private List<CardCombo> comboList=new ArrayList<>();
	/**自己牌组的Combo总数**/
	private List<CardCombo> selfCombo=new ArrayList<>();
	private boolean MODE=false;
	private int limit=0;
	private int total;
	private int poiont_total;
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
					break;
				case 1:
					//战斗力计算成功显示明细窗体
					break;
			}
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_list_show);
		srp=SRPUtil.getInstance(getApplicationContext());
		dao=SRPUtil.getDAO(getApplicationContext());
		list=new ArrayList<>();
		findView();
		setListener();
		new Thread(mTasks).start();
	}
	private void findView(){
		alert=new DialogEvent().showLoading(this);
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle("论战卡片");
		txt=(TextView)findViewById(R.id.null_txt);
		if(!isFinishing()){
			alert.show();
		}
	}
	//查询
	public void select(String select){
		//类型，点数范围，名称
//		select_txt=select;
//		search=(List<Trove>)dao.select(Trove.class, false, "type=? and name like ?",new String[]{type,"%"+select_txt+"%"}, null, null,"rate desc,feats desc", null);
//		if(list.size()==0){
//			txt.setVisibility(View.VISIBLE);
//
//		}else{
//			txt.setVisibility(View.GONE);
//		}
//		mGridAdapter=new CardAdapter(search,this);
//		gridview.setAdapter(mGridAdapter);
	}
	private void setListener(){
		gridview=(GridView)findViewById(R.id.gridview);
		mGridAdapter=new CardAdapter(list,this);
		gridview.setAdapter(mGridAdapter);
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
				if (mGridAdapter.getItem(position).flag == 0) {
					Card trove = new Card();
					trove.flag = 1;
					mGridAdapter.getItem(position).flag=1;
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


		//计算战斗力，如果卡组满足三十张，合计点数没有超过设定点数
	}
	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!select_txt.equals("")){
			select_txt="";
			mGridAdapter=new CardAdapter(list,this);
			gridview.setAdapter(mGridAdapter);
			Toast.makeText(getApplicationContext(), "重置搜索条件", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}
	@SuppressWarnings("unchecked")
	private Runnable mTasks =new Runnable(){
		public void run() {
			list=(List<Card>)dao.select(Card.class, false, "id>?",new String[]{"0"}, null, null,"point desc,type desc", null);
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
		MODE=true;
		temp=(List<Card>)dao.select(Card.class, false, "flag=?",new String[]{"1"}, null, null,"point desc,type desc", null);
		for(int i=0;i<temp.size();i++){
			poiont_total+=temp.get(i).point;
		}
		//show Bar
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
		}
		//显示卡组数量
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
		getCombo();
		//基本点数叠加
		total+=poiont_total;
		//种类加成（2种类型为0 1种-5 每多一种Plus 5）
		total+=((int)srp.countByType(false,2)-2)*2;
		//combo加成(根据Combo的点数，除以所需卡片数，功能性combo均为10)
		for(int i=0;i<selfCombo.size();i++){
			total+=selfCombo.get(i).value;
			Log.i("alist", "" + selfCombo.get(i).getName() + selfCombo.get(i).getEffect());
		}
		//随机点数加成[1~20]
		total+= NumberUtil.getRandom(0,20);
		return total;
	}

	private void cardLimit(){
		if(temp.size()>=30){
			mGridAdapter=new CardAdapter(temp,context);
		}
	}

	public void popWindow(){
		//对话框显示，输入返回搜索条件
		View xc=getLayoutInflater().inflate(R.layout.select_trove, null);
		ViewUtil.popDialog(this, xc);
	}


	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:popWindow();break;
				case R.id.type_search:startmode();break;
			}
			return true;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.trove_menu, menu);
		return true;
	}
}