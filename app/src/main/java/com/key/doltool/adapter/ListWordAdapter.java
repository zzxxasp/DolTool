package com.key.doltool.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
public class ListWordAdapter extends BaseAdapter{
	private String[] list;
	private Activity context;
	private static class ViewHolder{
		public TextView txt;
	} 
	
	public ListWordAdapter(String[] list,Activity context){
		this.list=new String[list.length];
		this.list=list;
		this.context=context;
	}
	
	public int getCount() {
		return list.length;
	}
	
	public Object getItem(int position) {
		return list[position];
	}
	
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
            // 导入布局并赋值给convertview
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.item_word, null);
			holder.txt=(TextView)convertView.findViewById(R.id.txt);
			
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		holder.txt.setText(list[position]);
		return convertView;
	}
}
