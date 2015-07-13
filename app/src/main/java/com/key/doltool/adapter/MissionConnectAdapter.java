package com.key.doltool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.view.SlideHolder;
public class MissionConnectAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView item_name;
	}
	private String[] item_txt;
	private Context context;
	private int type;
	private String name;
	public MissionConnectAdapter(String[] item_txt,Context context,int type,String name){
		this.item_txt=item_txt;
		this.context=context;
		this.type=type;
		this.name=name;
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
			convertView = mInflater.inflate(R.layout.mission_item_detail_flow, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.item_name);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.item_name.setText(item_txt[position]);
		final int index=position;
		holder.item_name.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,MissionDetailsActivity.class);
				it.putExtra("find_item",item_txt[index]);
				it.putExtra("souce_type",type);
				it.putExtra("souce_name",""+name);
				it.putExtra("type","about");
				it.putExtra("index",index);
				context.startActivity(it);
			}
		});
		return convertView;
	}
}