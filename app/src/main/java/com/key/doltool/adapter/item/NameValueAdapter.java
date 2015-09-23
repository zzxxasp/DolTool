package com.key.doltool.adapter.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.base.NameValueItem;

import java.util.ArrayList;
import java.util.List;

public class NameValueAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView add_txt;
	}
	private List<NameValueItem> list=new ArrayList<>();
	private Context context;
	public NameValueAdapter(List<NameValueItem> list, Context context){
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
			convertView = mInflater.inflate(R.layout.item_add,null);
			holder.add_txt=(TextView)convertView.findViewById(R.id.add_txt);
			// 为view设置标签 
			convertView.setTag(holder);
		}else { 
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.add_txt.setText(list.get(position).name+"+"+list.get(position).value);
		return convertView;
	}
}
