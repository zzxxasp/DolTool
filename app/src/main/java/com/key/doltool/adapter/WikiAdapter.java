package com.key.doltool.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.WikiInfo;
public class WikiAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public TextView type;
	} 
	public static int SIZE=20;
	private List<WikiInfo> list=new ArrayList<>();
	private Context context;
	public WikiAdapter(List<WikiInfo> list,Context context){
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
			convertView = mInflater.inflate(R.layout.item_wiki, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.type=(TextView)convertView.findViewById(R.id.type);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		holder.type.setText(list.get(position).getType());		
		return convertView;
	}
}
