package com.key.doltool.activity.adventure;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseAdventureActivity;
import com.key.doltool.adapter.DockYardMenuAdapter;
import com.key.doltool.adapter.TroveAdapter;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.Trove;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SlideHolder;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;
public class AdventureListActivity extends BaseAdventureActivity{
	private GridView gridview;
	private LinearLayout alert;
	private List<Trove> list;
	private DefaultDAO dao;
	private String type;
	private UpdataCount count;
	private Parcelable state;
	private ImageView main_menu;
	//侧边栏
	private SlideHolder mSlideHolder;
	private ListView menu_list;
	private TextView txt;
	private TroveAdapter mGridAdapter;
	private String select_txt="";
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//更新页面
			alert.setVisibility(View.GONE);
			state=gridview.onSaveInstanceState();
			mGridAdapter=new TroveAdapter(list, AdventureListActivity.this,true);
			gridview.setAdapter(mGridAdapter);
			gridview.onRestoreInstanceState(state);
		}
	 };
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adventure_list_main);
		type=getIntent().getStringExtra("type");
		dao=SRPUtil.getDAO(this);
		count=new UpdataCount(this);
		list=new ArrayList<>();
		findView();
		setListener();
		initMenu();
		new Thread(mTasks).start();
	}
	private void findView(){
		alert=(LinearLayout)findViewById(R.id.layout_alert);
		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
		getSimpleActionBar(true).initActionBar(type,R.drawable.ic_more_vert_white);
		main_menu=(ImageView)findViewById(R.id.main_menu);
		main_menu.setVisibility(View.VISIBLE);
		txt=(TextView)findViewById(R.id.null_txt);
		alert.setVisibility(View.VISIBLE);
	}
	private void initMenu(){
		menu_list=(ListView)findViewById(R.id.menu_list);
		List<MenuItem> list=new ArrayList<>();
		ViewUtil.setList(list,2);
		menu_list.setAdapter(new DockYardMenuAdapter(list,this));
		menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mSlideHolder.toggle();
				switch(position){
					//查询
					case 0:popWindow();break;
					//批量标记
					case 1:break;
				}		
			}
		});
	}
	@SuppressWarnings("unchecked")
	//查询
	public void select(String select){
		select_txt=select;
		List<Trove>list=(List<Trove>)dao.select(Trove.class, false, "type=? and name like ?",new String[]{type,"%"+select_txt+"%"}, null, null,"rate desc,feats desc", null);
		if(list.size()==0){
			txt.setVisibility(View.VISIBLE);
		}else{
			txt.setVisibility(View.GONE);
		}
		mGridAdapter=new TroveAdapter(list,this,true);
		gridview.setAdapter(mGridAdapter);
	}
	private void setListener(){
		main_menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mSlideHolder.toggle();
			}
		});
		gridview=(GridView)findViewById(R.id.gridview);
		gridview.setAdapter(new TroveAdapter(list, this,false));
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
		        ActivityOptionsCompat options =
		                ActivityOptionsCompat.makeSceneTransitionAnimation(
		                		context,arg1.findViewById(R.id.img),"name");
		        Intent it = new Intent(context,AdventureDetailActivity.class);
		        it.putExtra("id", list.get(arg2).getId());
		        UpdataList.FLAG_CHANGE=0;
		        ActivityCompat.startActivity(context,it,options.toBundle());
			}
		});
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(list.get(position).getFind_flag()==0){
					Trove trove=new Trove();
					trove.setFind_flag(1);
					dao.update(trove, new String[]{"flag"}, "id=?", new String[]{""+list.get(position).getId()});
				}else{
					Trove trove=new Trove();
					trove.setFind_flag(0);
					dao.update(trove, new String[]{"flag"}, "id=?", new String[]{""+list.get(position).getId()});
				}
				count.init_adventure(list.get(position).getType());
				alert.setVisibility(View.VISIBLE);
				new Thread(mTasks).start();
				return true;
			}
		});
	}
	@Override
	protected void onDestroy() {
		list.clear();
		if(mSlideHolder.mCachedBitmap!=null){
			mSlideHolder.mCachedBitmap.recycle();
			mSlideHolder.mCachedBitmap=null;
			System.gc();
		}
		UpdataList.FLAG_CHANGE_LIST=1;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		if(UpdataList.FLAG_CHANGE==1){
			alert.setVisibility(View.VISIBLE);
			new Thread(mTasks).start();
			UpdataList.FLAG_CHANGE=0;
		}
		super.onResume();
	}
	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //菜单键覆写，调用边缘栏菜单
		 if(keyCode==KeyEvent.KEYCODE_MENU){
			 mSlideHolder.toggle();
			 return true;
		 }
		 if(!select_txt.equals("")){
			 select_txt="";
			 txt.setVisibility(View.GONE);
			 gridview.setAdapter(new TroveAdapter(list, this,false));
			 Toast.makeText(AdventureListActivity.this, "重置搜索条件", Toast.LENGTH_LONG).show();
		 }else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK)
			{
				//开启就关闭
				if(mSlideHolder.isOpened()){
					mSlideHolder.toggle();
				}else{
					super.onKeyDown(keyCode, event);
				}
			}
		}
		 return true;
	};
	@SuppressWarnings("unchecked")
	private Runnable mTasks =new Runnable(){
		public void run() {
			list=(List<Trove>)dao.select(Trove.class, false, "type=?",new String[]{type}, null, null,"rate desc,feats desc", null);
			mHandler.sendMessage(mHandler.obtainMessage());
		}
	};
}