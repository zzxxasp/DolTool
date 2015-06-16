package com.key.doltool.adapter.item;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.WordItem;
public class WordAdapter extends BaseAdapter{
	public static int SIZE=20;
	private static class ViewHolder{
		public TextView zh_name;
		public TextView tw_name;
		public TextView type;
	}
	private List<WordItem> list=new ArrayList<>();
	private Context context;
	public WordAdapter(List<WordItem> list,Context context){
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
			convertView = mInflater.inflate(R.layout.item_word_list,null);
			holder.zh_name=(TextView)convertView.findViewById(R.id.zh_name);
			holder.tw_name=(TextView)convertView.findViewById(R.id.tw_name);
			holder.type=(TextView)convertView.findViewById(R.id.type);
			// 为view设置标签 
			convertView.setTag(holder);
		}else { 
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.zh_name.setText("国服："+list.get(position).zh_name);
		holder.tw_name.setText("台服："+list.get(position).tw_name);
		holder.type.setText(list.get(position).type);
		return convertView;
	}
}
