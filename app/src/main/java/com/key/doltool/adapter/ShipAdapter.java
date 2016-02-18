package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.sqlite.RegularShip;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ResourcesUtil;

import java.util.ArrayList;
import java.util.List;

public class ShipAdapter extends BaseAdapter{
	private static class ViewHolder{
		public RelativeLayout main;
		public TextView target,name,money,time;
	}
	public static int SIZE=60;
	private List<RegularShip> list=new ArrayList<>();
	private Context context;
	public ShipAdapter(List<RegularShip> list, Context context){
		this.list=list;
		this.context=context;
	}
	public int getCount() {
			return list.size();
	}

	@Override
	public RegularShip getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.item_shipway, null);
			holder.main=(RelativeLayout)convertView.findViewById(R.id.main);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.money=(TextView)convertView.findViewById(R.id.money);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.target=(TextView)convertView.findViewById(R.id.target);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).name);
		holder.money.setText(list.get(position).money);
		holder.time.setText(list.get(position).time);
		String target=list.get(position).start_city+"-"+list.get(position).end_city;
		holder.target.setText(target);
		return convertView;
	}
}
