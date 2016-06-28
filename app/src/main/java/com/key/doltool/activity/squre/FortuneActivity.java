package com.key.doltool.activity.squre;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.LuckyInfo;
import com.key.doltool.event.FortuneEvent;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.SystemBarTintManager;

import butterknife.BindView;

/**
 * 航海运势
 * @author key
 * @version 0.1
 * @since 2014-1-15 
 */
public class FortuneActivity extends BaseActivity{
	
	private List<String> total_list=new ArrayList<>();
	private List<Integer> good_list=new ArrayList<>();
	private List<Integer> bad_list=new ArrayList<>();

	@BindView(R.id.main_txt) TextView main_txt;
	@BindView(R.id.bottom_txt) TextView bottom_txt;

	@BindView(R.id.good_txt) TextView good_txt;
	@BindView(R.id.bad_txt) TextView bad_txt;

	@BindView(R.id.main_area) ViewGroup main_area;

	@Override
	public int getContentViewId() {
		return R.layout.magic_layout;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
		toolbar.setTitle("今日运势");
		initData();
	}

	private void initData(){
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		//step1:执行获取数据方法,填充List
		new FortuneEvent(this).getRandomChance();
		LuckyInfo info=new LuckyInfo(this);
		Gson g=new Gson();
		String date=StringUtil.TransDateFormatterToChines(info.getToday());
		total_list=g.fromJson(info.getData(), new TypeToken<List<String>>(){}.getType());
		for(int i=0;i<total_list.size();i++){
			String temp[]=total_list.get(i).split("-");
			if(temp[0].equals("1")){
				good_list.add(Integer.parseInt(temp[1]));
			}			
			if(temp[0].equals("0")){
				bad_list.add(Integer.parseInt(temp[1]));
			}
		}
		//step2:根据list数据填充界面
		String[] lucky_txt=ResourcesUtil.getArray(this,R.array.luck_level_txt);
		String txt=lucky_txt[good_list.size()-bad_list.size()+4];
		//填充主区域文字及背景颜色
		main_txt.setText(txt);
		if(good_list.size()-bad_list.size()>0){
			tintManager.setStatusBarTintResource(R.color.Crimson);
			toolbar.setBackgroundColor(ResourcesUtil.getColorId(R.color.Crimson, this));
			main_area.setBackgroundColor(ResourcesUtil.getColorId(R.color.Crimson, this));
			bottom_txt.setBackgroundColor(ResourcesUtil.getColorId(R.color.Crimson, this));
			bottom_txt.setText(date+" 吉");
		}else if(good_list.size()-bad_list.size()==0){
			tintManager.setStatusBarTintResource(R.color.Blue_SP);
			toolbar.setBackgroundColor(ResourcesUtil.getColorId(R.color.Blue_SP, this));
			main_area.setBackgroundColor(ResourcesUtil.getColorId(R.color.Blue_SP, this));
			bottom_txt.setBackgroundColor(ResourcesUtil.getColorId(R.color.Blue_SP, this));
			bottom_txt.setText(date+" 平");
		}else if(good_list.size()-bad_list.size()<0){
			tintManager.setStatusBarTintResource(R.color.SlateGray);
			toolbar.setBackgroundColor(ResourcesUtil.getColorId(R.color.SlateGray, this));
			main_area.setBackgroundColor(ResourcesUtil.getColorId(R.color.SlateGray, this));
			bottom_txt.setBackgroundColor(ResourcesUtil.getColorId(R.color.SlateGray, this));
			bottom_txt.setText(date+" 凶");
		}
		String good_temp="";
		String bad_temp="";
		//填充宜，忌列表项
		for(int i=0;i<good_list.size();i++){
			if(i==good_list.size()-1){
				good_temp+=FortuneEvent.type[good_list.get(i)];
			}else{
				good_temp+=FortuneEvent.type[good_list.get(i)]+" ";
			}
		}
		for(int i=0;i<bad_list.size();i++){
			if(i==bad_list.size()-1){
				bad_temp+=FortuneEvent.type[bad_list.get(i)];
			}else{
				bad_temp+=FortuneEvent.type[bad_list.get(i)]+" ";
			}
		}
		
		if(good_temp.equals("")){
			good_temp="诸事不宜";
		}
		if(bad_temp.equals("")){
			bad_temp="无事可忌";
		}
		good_txt.setText(good_temp);
		bad_txt.setText(bad_temp);
	}
}
