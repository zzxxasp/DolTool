package com.key.doltool.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.Mission;
import com.key.doltool.util.ResourcesUtil;

public class MissionItemAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name,type;
		public TextView rank,skill_need;
		public View line;
	} 
	public static int SIZE=30;
	private List<Mission> list=new ArrayList<>();
	private Context context;
	public MissionItemAdapter(List<Mission> list,Context context){
		this.list=list;
		this.context=context;
	}
	public int getCount() {
			return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
            // 导入布局并赋值给convertView
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.mission_list_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.type=(TextView)convertView.findViewById(R.id.type);
			holder.line=convertView.findViewById(R.id.line);
			holder.rank=(TextView)convertView.findViewById(R.id.rank);
			holder.skill_need=(TextView)convertView.findViewById(R.id.skill_need);
			
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		if(list.get(position).getTag()==1){
			holder.name.setTextColor(ResourcesUtil.getColor(R.color.Blue_SP, context));
			holder.rank.setTextColor(ResourcesUtil.getColor(R.color.Blue_SP, context));
			holder.skill_need.setTextColor(ResourcesUtil.getColor(R.color.Blue_SP, context));
		}else{
			holder.name.setTextColor(ResourcesUtil.getColor(R.color.Black_SP, context));
			holder.rank.setTextColor(ResourcesUtil.getColor(R.color.Black_SP, context));
			holder.skill_need.setTextColor(ResourcesUtil.getColor(R.color.Black_SP, context));
		}
		holder.type.setText(list.get(position).getType());
		if(list.get(position).getType().equals("港口部落")){
			holder.rank.setVisibility(View.GONE);
			holder.skill_need.setVisibility(View.GONE);
			holder.line.setVisibility(View.GONE);
		}else{
			holder.rank.setVisibility(View.VISIBLE);
			holder.skill_need.setVisibility(View.VISIBLE);
			holder.line.setVisibility(View.VISIBLE);
		}
		String rank=getRankByMission(list.get(position));
		String skill_need=list.get(position).getSkill_need();
		if(!rank.equals("")){
			holder.rank.setText("Rank:"+rank);
			holder.skill_need.setVisibility(View.VISIBLE);
		}else{
			holder.rank.setVisibility(View.GONE);
		}
		if(!skill_need.equals("")){
			holder.skill_need.setText(skill_need);
			holder.skill_need.setVisibility(View.VISIBLE);
		}else{
			holder.skill_need.setVisibility(View.GONE);
		}
		return convertView;
	}
	private String getRankByMission(Mission item){
		int i = 0;
		String str="";
		switch (item.getType()) {
			case "书库地图":
				if (!item.getSkill_need().equals("")) {
					String[] item1 = item.getSkill_need().split(",");
					str = item1[0].split(":")[1];
				}
				break;
			case "港口部落":
				return str;
			default:
				String star = "★";
				Matcher m = Pattern.compile(Pattern.quote(star)).matcher(item.getLevel());
				while (m.find()) {
					i++;
				}
				if (i != 0) {
					str = i + "";
				}
				break;
		}
		return str;
	}
}
