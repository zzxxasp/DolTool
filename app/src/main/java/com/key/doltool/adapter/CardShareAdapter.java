package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.Deck;

import java.util.ArrayList;
import java.util.List;

public class CardShareAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public TextView value;
		public TextView userName;
		public TextView limit;
	}
	private List<Deck> list=new ArrayList<>();
	private Context context;
	public CardShareAdapter(List<Deck> list, Context context){
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
			convertView = mInflater.inflate(R.layout.item_card_share, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.value=(TextView)convertView.findViewById(R.id.value);
			holder.limit=(TextView)convertView.findViewById(R.id.type);
			holder.userName=(TextView)convertView.findViewById(R.id.creater);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		String point="战斗力："+list.get(position).getValue();
		holder.name.setText(list.get(position).getName());
		holder.value.setText(point);
		holder.limit.setText(list.get(position).getLimit());
		holder.userName.setText(list.get(position).getUserName());
		return convertView;
	}
}