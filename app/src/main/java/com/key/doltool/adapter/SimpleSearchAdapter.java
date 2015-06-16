package com.key.doltool.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.SearchItem;
import com.key.doltool.util.StringUtil;
public class SimpleSearchAdapter extends BaseAdapter{
	private List<SearchItem> item_txt;
			
	private static class ViewHolder{
		public TextView item_name;
		private TextView type;
	}
	private Context context;
	public SimpleSearchAdapter(Context context,List<SearchItem> item_txt){
		this.context=context;
		this.item_txt=item_txt;
	}
	public int getCount() {
			return item_txt.size();
	}

	@Override
	public Object getItem(int arg0) {
		return item_txt.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_seach, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.name);
			holder.type=(TextView)convertView.findViewById(R.id.type);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.item_name.setText(item_txt.get(position).name);
		if(!StringUtil.isNull(item_txt.get(position).add_info)){
			holder.type.setVisibility(View.VISIBLE);
			holder.type.setText(item_txt.get(position).add_info);
		}else{
			holder.type.setVisibility(View.GONE);
		}

		return convertView;
	}
}