package com.key.doltool.activity.mission;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ADCListAdapter;
import com.key.doltool.adapter.MissionItemAdapter;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MissonListActivity extends BaseActivity{
	//定义部分
	@BindView(R.id.mission) ViewGroup mission;
	@BindView(R.id.listview) ListView listview;
	@BindView(R.id.sp_area) Spinner area;
	@BindView(R.id.sp_city) Spinner	city;
	@BindView(R.id.sp_type) Spinner type;
	@BindView(R.id.adventure_type) Spinner adventure_type;
	@BindView(R.id.sp_mission_type) Spinner sp_type;
	@BindView(R.id.search_btn) FlatButton search;
	private Dialog alert;
	private MissionItemAdapter adapter;
	//数据temp变量
	private int mode=0;
	private AreaEvent event;
	private String select_if;

	private List<String> if_args=new ArrayList<>();

	private ArrayAdapter<String> city_adapter;
	private String[][] temp;
	private int temp_click;
	private ListFlowHelper<Mission> listFlowHelper;
	private ViewHandler viewHandler;
	private DefaultDAO dao;
	@Override
	public int getContentViewId() {
		return R.layout.mission_list_main;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		event=new AreaEvent();
		dao=SRPUtil.getDAO(this);
		selectInit();
		findView();
		initSelectForCity();
		initSelectForType();
	}

	//通用findView
	private void findView() {
		listFlowHelper=new ListFlowHelper<>(Mission.class, context, new ListFlowHelper.ListFlowCallBack() {
			@Override
			public void showSelectToast(String msg) {
				Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
			}

			@Override
			public void setAdapter() {
				adapter=new MissionItemAdapter(listFlowHelper.list,context);
				listview.setAdapter(adapter);
			}

			@Override
			public void updateAdapter() {
				adapter.notifyDataSetChanged();
			}
		}, ADCListAdapter.SIZE,"level asc,skill_need asc");

		alert=new DialogEvent().showLoading(this);
		initPage();
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle("任务委托所");
	}
	private void initPage(){
		viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
			@Override
			public void onHandleMessage(Message msg) {
				listFlowHelper.change();
				alert.dismiss();
			}
		});
		initPageItem();
	}
	private void initPageItem(){
		ListScrollListener listScrollListener = new ListScrollListener(alert, viewHandler, context);
		listview.setOnScrollListener(listScrollListener);
		listFlowHelper.setListScrollListener(listScrollListener);
		listFlowHelper.selectshow("0,"+MissionItemAdapter.SIZE);
		adapter=new MissionItemAdapter(listFlowHelper.list,this);
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
				if(listFlowHelper.list.get(position).getTag()==0){
					Mission mission=new Mission();
					mission.setTag(1);
					listFlowHelper.list.get(position).setTag(1);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{""+listFlowHelper.list.get(position).getId()});
				}else{
					Mission mission=new Mission();
					mission.setTag(0);
					listFlowHelper.list.get(position).setTag(0);
					dao.update(mission, new String[]{"tag"}, "id=?", new String[]{""+listFlowHelper.list.get(position).getId()});
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		temp=AreaEvent.ADVENTURE_CITY;
	}

	private void showDialog(){
		new DialogEvent(select_if,if_args).itemDialog(listFlowHelper,context);
	}

	private void jump(int position){
		Intent intent=new Intent(this,MissionDetailsActivity.class);
		intent.putExtra("find_item", listFlowHelper.list.get(position).getId()+"");
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
		searchShow();
	}
	private void searchShow(){
		if(mode==1){
			mission.setVisibility(View.VISIBLE);
		}else{
			mission.setVisibility(View.GONE);
		}
	}
	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(listFlowHelper.isChange()&&mode==0){
			super.onKeyDown(keyCode, event);
		}
		//其他
		else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK)
			{
				//取消搜索模式
				if(mode==1){
					changeMode();
				}
				//条件不是初始状态就重置
				listFlowHelper.reback();
			}
		}
		return true;
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
			switch (temp) {
				case "":
					select_if += " and type = ?";
					if_args.add(sp_type.getSelectedItem().toString());
					break;
				case "沉船":
				case "掠夺":
					select_if += " and start_city like ?";
					if_args.add("%" + temp + "%");
					break;
				case "海上视认":
					select_if += " and start_city like ?";
					if_args.add("%" + temp + "%");
					break;
				default:
					select_if += " and get_item like ?";
					if_args.add("%" + temp + "%");
					break;
			}
		}
		//获取技能需求附加条件
		if(adventure_type.getVisibility()!=View.GONE&&adventure_type.getSelectedItemPosition()!=0){
			select_if+=" and skill_need like ?";
			String temp=adventure_type.getSelectedItem().toString();
			if_args.add("%"+temp+"%");
		}
		listFlowHelper.change_if(select_if,if_args);
	}

	private void selectInit(){
		select_if="id>?";
		if_args.clear();
		if_args.add("0");
	}
	//初始化-搜索栏
	private void initSelectForCity(){
		ArrayAdapter<String> adapter=new SpinnerArrayAdapter
				(this,AreaEvent.ADVENTURE_AREA);
		area.setAdapter(adapter);
		//地区-城市联动事件
		area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				if (arg2 == 0) {
					city.setVisibility(View.GONE);
				} else {
					city.setVisibility(View.VISIBLE);
					city_adapter = new SpinnerArrayAdapter(MissonListActivity.this, temp[arg2]);
					city.setAdapter(city_adapter);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}
	//初始化-搜索栏
	private void initSelectForType(){
		ArrayAdapter<String> adapter3=new SpinnerArrayAdapter
				(this,ResourcesUtil.getArray(this,R.array.mission_type));
		type.setAdapter(adapter3);
		search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setSelectArgs();
			}
		});
		ArrayAdapter<String> adapter=new SpinnerArrayAdapter
				(this,ResourcesUtil.getArray(this,R.array.adventure_type));
		adventure_type.setAdapter(adapter);

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
					temp=AreaEvent.LIB_CITY;
					area.setSelection(0);
					city_adapter=new ArrayAdapter<>(MissonListActivity.this,android.R.layout.simple_spinner_item,temp[0]);
					city.setAdapter(city_adapter);
				}
				if(temp_click==4){
					temp=AreaEvent.ADVENTURE_CITY;
					area.setSelection(0);
					city_adapter=new ArrayAdapter<>(MissonListActivity.this,android.R.layout.simple_spinner_item,temp[0]);
					city.setAdapter(city_adapter);
				}
				temp_click=arg2;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:changeMode();break;
				case R.id.type_search:showDialog();break;

			}
			return true;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.adc_menu, menu);
		return true;
	}
}