package com.key.doltool.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.adapter.util.AdapterUtil;
import com.key.doltool.data.TradeCityItem;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
public class TradeSimpleListAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public ImageView pic;
		public TextView price;
	} 
	private List<TradeCityItem> list=new ArrayList<>();
	private Context context;
	private DefaultDAO dao;
	private AdapterUtil util;
	public TradeSimpleListAdapter(List<TradeCityItem> list,Context context){
		this.list=list;
		this.context=context;
		dao=SRPUtil.getDAO(context);
		util=new AdapterUtil(context, dao);
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
			convertView = mInflater.inflate(R.layout.trade_simple_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic);
			holder.price=(TextView)convertView.findViewById(R.id.price);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).name);
		util.findByName(holder.pic,list.get(position).name,2);
		if(list.get(position).invest.equals("")){
			holder.price.setText(list.get(position).price);
		}else{
			holder.price.setText(list.get(position).price+"("+list.get(position).invest+")");
		}
		return convertView;
	}
}