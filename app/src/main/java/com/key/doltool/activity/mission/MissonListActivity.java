package com.key.doltool.activity.mission;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.DockYardMenuAdapter;
import com.key.doltool.adapter.MissionItemAdapter;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.Mission;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SlideHolder;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

public class MissonListActivity extends BaseActivity implements OnScrollListener{
	//定义部分
	private LinearLayout layout_alert;
	
	private ViewGroup mission;
	
	private ImageView main_menu;
	private TextView main_title;
	//侧边栏
	private SlideHolder mSlideHolder;
	private ListView menu_list;
	//船只列表页面
	private ListView listview;
	private MissionItemAdapter adapter;
	//数据temp变量
	private int mode=0;
	private int add=0;
	private Thread mThread;	// 线程
	private boolean end_flag=true; //是否为最末标记
	
	private Spinner area,city;
	private Spinner type,adventure_type,sp_type;
	private FlatButton search;
	private AreaEvent event;
	
	private String select_if;
	private List<Mission> list_mission=new ArrayList<Mission>();
	private List<String> if_args=new ArrayList<String>();
	
	private ArrayAdapter<String> city_adapter;
	private DefaultDAO dao;
	private String[][] temp;
	private int temp_click;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mission_list_main);
		event=new AreaEvent();
		dao=SRPUtil.getDAO(this);
		selectInit();
		findView();
		setListener();		
		initSelectForCity();
		initSelectForType();
	}
	//通用findView
	private void findView() {
		initPage();
		mission=(ViewGroup)findViewById(R.id.mission);
		main_title=(TextView)findViewById(R.id.main_title);
		main_title.setText("任务委托所");
		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
		layout_alert=(LinearLayout)findViewById(R.id.layout_alert);
		main_menu=(ImageView)findViewById(R.id.main_menu);
		main_menu.setVisibility(View.VISIBLE);
	}
	//通用Listener
	private void setListener() {
		main_menu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mSlideHolder.toggle();
			}
		});
	}
	private void initPage(){
		//初始化边缘栏
		initMenu();
		initPageItem();
	}
	private void initPageItem(){
		listview=(ListView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		selectshow("0,"+MissionItemAdapter.SIZE);
		adapter=new MissionItemAdapter(list_mission,this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				jump(position);
			}
		});
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(list_mission.get(position).getTag()==0){
					Mission mission=new Mission();
					mission.setTag(1);
					list_mission.get(position).setTag(1);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{""+list_mission.get(position).getId()});
				}else{
					Mission mission=new Mission();
					mission.setTag(0);
					list_mission.get(position).setTag(0);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{""+list_mission.get(position).getId()});
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		temp=event.ADVENTURE_CITY;
	}
	protected void onDestroy() {
		dao=null;
		if(mSlideHolder.mCachedBitmap!=null){
			mSlideHolder.mCachedBitmap.recycle();
			mSlideHolder.mCachedBitmap=null;
			System.gc();
		}
		super.onDestroy();
	}

	
	@SuppressWarnings("unchecked")
	//有限数据查询
	private void selectshow(String limit){
		if(dao==null){
			return;
		}
		//数据前后记录
		int size_before=0,size_after=0;
			size_before=list_mission.size();
			list_mission.addAll(((List<Mission>) dao.select(Mission.class, false,select_if, StringUtil.listToArray(if_args), 
				null, null,"level asc,skill_need asc",limit)));
			size_after=list_mission.size();
		//数据返回判断
    	if(size_after==size_before&&size_after!=0){
    		end_flag=false;
    		Toast.makeText(this,"已经返回所有查询结果了", Toast.LENGTH_LONG).show();
    	}else if(size_after==0){
    		Toast.makeText(this,"没有查到您想要的结果", Toast.LENGTH_LONG).show();
    	}
	}
	//数据添加
	private void change(){
		add+=MissionItemAdapter.SIZE;
		selectshow(add+","+	MissionItemAdapter.SIZE);
		//更新adapter
		adapter.notifyDataSetChanged();
	}
	//初始化边缘菜单栏
	private void initMenu(){
		menu_list=(ListView)findViewById(R.id.menu_list);
		List<MenuItem> list=new ArrayList<MenuItem>();
		ViewUtil.setList(list,7);
		menu_list.setAdapter(new DockYardMenuAdapter(list,this));
		menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mSlideHolder.toggle();
				switch(position){
					case 0:changeMode();break;
					case 1:showDialog();break;
				}		
			}
		});
	}
	private void showDialog(){
		new DialogEvent(select_if,if_args).itemDialog(this);
	}
	
	private void jump(int position){
		Intent intent=new Intent(this,MissionDetailsActivity.class);
		intent.putExtra("find_item", list_mission.get(position).getId()+"");
		intent.putExtra("type","item");
		startActivity(intent);
	}
	//变更模式
	private void changeMode(){
		if(mode==0){
			mode=1;
		}else{
			mode=0;
		}
		//显示搜索域
		searchShow(mode);
	}	
	private void searchShow(int i){
		if(mode==1){
			mission.setVisibility(View.VISIBLE);
		}else{
			mission.setVisibility(View.GONE);
		}
	}
/**
 * 华丽的分割线——以下是Handler,线程,系统按键等处理 
 */
	//Handler——线程结束后更新界面
	private Handler handler = new Handler() {
		 public void handleMessage(Message msg) {
				change();
				layout_alert.setVisibility(View.GONE);
				ViewUtil.disableSubControls(mSlideHolder, true);
		 }
	 };

	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //菜单键覆写，调用边缘栏菜单
		 if(keyCode==KeyEvent.KEYCODE_MENU){
			 mSlideHolder.toggle();
			 return true;
		 }
		 //条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(select_if.equals("id>?")&&!mSlideHolder.isOpened()&&mode==0){
			super.onKeyDown(keyCode, event);
		}
		//其他
		else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK)
			{
				//开启就关闭
				if(mSlideHolder.isOpened()){
					mSlideHolder.toggle();
				}
				//取消搜索模式
				if(mode==1){
					changeMode();
				}
				//条件不是初始状态就重置
				if(!select_if.equals("id>?")){
					selectInit();
					end_flag=true;
					add=0;
					list_mission.clear();
					selectshow("0,"+MissionItemAdapter.SIZE);
					adapter=new MissionItemAdapter(list_mission,MissonListActivity.this);
					listview.setAdapter(adapter);
					Toast.makeText(this,"重置搜索条件", Toast.LENGTH_SHORT).show();
				}
			}
		}
		 return true;
	 };
	//滚动监听① - useless
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	//滚动监听②
	public void onScrollStateChanged(final AbsListView view, int scrollState) {
        //当不滚动时
		boolean flag=end_flag;
        if(scrollState == SCROLL_STATE_IDLE){  
                System.out.println(view.getFirstVisiblePosition()+"===" + view.getLastVisiblePosition()+"==="+view.getCount());
                //判断滚动到底部   
                if(view.getLastVisiblePosition()==(view.getCount()-1)){
                	//没有线程且不为最末时
                    if (mThread == null || !mThread.isAlive()&&flag) {
                    	//显示进度条，区域操作控制
                    	layout_alert.setVisibility(View.VISIBLE);
                    	ViewUtil.disableSubControls(mSlideHolder, false);
                        mThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }  
                        };
                        mThread.start();                
                    }
                }
        	}
	}
	//获取条件下的搜索串
	private void setSelectArgs(){
		//初始化搜索条件
		selectInit();
		//获取城市附加条件
		if(city.getVisibility()!=View.GONE){
			select_if+=" and start_city like ?";
			String temp=city.getSelectedItem().toString();
			if_args.add("%"+temp+"%");
		}
		//获取类型附加条件
		if(type.getSelectedItemPosition()!=0&&type.getSelectedItemPosition()!=5){
			select_if+=" and type = ?";
			if_args.add(type.getSelectedItem().toString());
		}else if(type.getSelectedItemPosition()==5){
			String temp=event.getStringByIndex(sp_type.getSelectedItemPosition());
			if(temp.equals("")){
				select_if+=" and type = ?";
				if_args.add(sp_type.getSelectedItem().toString());
			}else if(temp.equals("沉船")||temp.equals("掠夺")){
				select_if+=" and start_city like ?";
				if_args.add("%"+temp+"%");
			}else if(temp.equals("海上视认")){
				select_if+=" and start_city like ?";
				if_args.add("%"+temp+"%");
			}
			else{
				select_if+=" and get_item like ?";
				if_args.add("%"+temp+"%");
			}
		}
		//获取技能需求附加条件
		if(adventure_type.getVisibility()!=View.GONE&&adventure_type.getSelectedItemPosition()!=0){
			select_if+=" and skill_need like ?";
			String temp=adventure_type.getSelectedItem().toString();
			if_args.add("%"+temp+"%");
		}
	}
	
	//修改查询条件
	public void change_if(String if_s,List<String> if_args){
		//初始化所有数据
		select_if=if_s;
		this.if_args=if_args;
		list_mission.clear();
		add=0;
		selectshow("0,"+SailBoatListAdapter.SIZE);
		//重新setAdapter
		adapter=new MissionItemAdapter(list_mission,this);
		listview.setAdapter(adapter);
	}
	public void begin(){
		end_flag=true;
	}
	
	private void selectInit(){
		select_if="id>?";
		if_args.clear();
		if_args.add("0");
	}
	//初始化-搜索栏
	private void initSelectForCity(){
		area=(Spinner)findViewById(R.id.sp_area);
		city=(Spinner)findViewById(R.id.sp_city);
		ArrayAdapter<String> adapter=new SpinnerArrayAdapter
		(this,event.ADVENTURE_AREA);
		area.setAdapter(adapter);
		//地区-城市联动事件
		area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==0){
					city.setVisibility(View.GONE);
				}else{
					city.setVisibility(View.VISIBLE);
					city_adapter=new SpinnerArrayAdapter(MissonListActivity.this,temp[arg2]);
					city.setAdapter(city_adapter);
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
	}
	//初始化-搜索栏
	private void initSelectForType(){
		type=(Spinner)findViewById(R.id.sp_type);
		ArrayAdapter<String> adapter3=new SpinnerArrayAdapter
		(this,ResourcesUtil.getArray(this,R.array.mission_type));
		type.setAdapter(adapter3);
		search=(FlatButton)findViewById(R.id.search_btn);
		search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setSelectArgs();
				Log.i("select_if", select_if);
				Log.i("size()", if_args.size()+"");
				list_mission.clear();
				end_flag=true;
				add=0;
				selectshow(add+","+MissionItemAdapter.SIZE);
				adapter=new MissionItemAdapter(list_mission,MissonListActivity.this);
				listview.setAdapter(adapter);
			}
		});
		adventure_type=(Spinner)findViewById(R.id.adventure_type);
		ArrayAdapter<String> adapter=new SpinnerArrayAdapter
		(this,ResourcesUtil.getArray(this,R.array.adventure_type));
		adventure_type.setAdapter(adapter);
		sp_type=(Spinner)findViewById(R.id.sp_mission_type);
		ArrayAdapter<String> adapter2=new SpinnerArrayAdapter
		(this,ResourcesUtil.getArray(this,R.array.sp_mission_type));
		sp_type.setAdapter(adapter2);
		//地区-城市联动事件
		type.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==1||arg2==4){
					area.setEnabled(true);
					city.setEnabled(true);
					sp_type.setVisibility(View.GONE);
					adventure_type.setVisibility(View.VISIBLE);
				}else if(arg2==5){
					area.setSelection(0);
					area.setEnabled(false);
					city.setEnabled(false);
					sp_type.setVisibility(View.VISIBLE);
					adventure_type.setVisibility(View.GONE);
				}
				else{
					area.setEnabled(true);
					city.setEnabled(true);
					sp_type.setVisibility(View.GONE);
					adventure_type.setVisibility(View.GONE);
				}
				if(arg2==4){
					temp=event.LIB_CITY;
					area.setSelection(0);
					city_adapter=new ArrayAdapter<String>(MissonListActivity.this,android.R.layout.simple_spinner_item,temp[0]);
					city.setAdapter(city_adapter);
				}
				if(temp_click==4){
					temp=event.ADVENTURE_CITY;
					area.setSelection(0);
					city_adapter=new ArrayAdapter<String>(MissonListActivity.this,android.R.layout.simple_spinner_item,temp[0]);
					city.setAdapter(city_adapter);
				}
				temp_click=arg2;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
}