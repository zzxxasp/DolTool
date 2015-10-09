package com.key.doltool.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.Mission;
public class MissionDetailsAdapter extends BaseAdapter{
	private String[] item_txt={
			"难度","技能需求","定金/报酬","经验/声望","时间限制","发现物","获得物品",
			"接受任务地点","任务流程","前置任务","后续任务"
	};
	private static class ViewHolder{
		public TextView item_name;
		public TextView item_content;
	}
	private Mission item;
	private Context context;
	public MissionDetailsAdapter(Mission item,Context context){
		this.item=item;
		this.context=context;
	}
	public int getCount() {
			return item_txt.length;
	}

	@Override
	public Object getItem(int arg0) {
		return item_txt[arg0];
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
			convertView = mInflater.inflate(R.layout.mission_item_detail, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.item_name);
			holder.item_content=(TextView)convertView.findViewById(R.id.item_content);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.item_name.setText(item_txt[position]);
		holder.item_content.setText(getTextByPosition(position));
		if(getTextByPosition(position).equals("")){
			convertView.setVisibility(View.GONE);
		}else{
			convertView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	private String getTextByPosition(int position){
		String str="";
		switch(position){
			case 0:str=item.getLevel();break;
			case 1:str=item.getSkill_need();break;
			case 2:str=item.getMoney();break;
			case 3:str=item.getExp();break;
			case 4:str=item.getTime_up();break;
			case 5:str=item.getFind_item();break;
			case 6:str=item.getGet_item();break;
			case 7:str=item.getStart_city();break;
			case 8:str=stringChange(item.getDaily(),8);break;
			case 9:str=stringChange(item.getBefore(),9);break;
			case 10:str=stringChange(item.getAfter(),9);break;
		}
		return str;
	}
	private String stringChange(String str,int index){
		if(index==8){
			str=str.replaceAll(" ", "\n");
		}else if(index==9){
			str=str.replaceAll(",", "\n");
		}else{
			if(str==null){
				str="";
			}
		}
		return str;
	}
}