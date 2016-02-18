package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;

import java.util.ArrayList;
import java.util.List;

public class ShipTimeAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView start,middle,end;
	}
	private List<String> list=new ArrayList<>();
	private Context context;
	public ShipTimeAdapter(List<String> list, Context context){
		this.list=list;
		this.context=context;
	}
	public int getCount() {
			return list.size();
	}

	@Override
	public String getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.item_time, null);
			holder.start=(TextView)convertView.findViewById(R.id.start);
			holder.middle=(TextView)convertView.findViewById(R.id.middle);
			holder.end=(TextView)convertView.findViewById(R.id.end);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		String[] temp=list.get(position).split("w");
		switch (temp[0]){
			case "0":
				holder.start.setText(temp[1]);
				holder.middle.setText("");
				holder.end.setText("");
				break;
			case "1":
				holder.start.setText("");
				holder.middle.setText(temp[1]);
				holder.end.setText("");
				break;
			case "2":
				holder.start.setText("");
				holder.middle.setText("");
				holder.end.setText(temp[1]);
				break;
		}
		//设置
		return convertView;
	}
}
