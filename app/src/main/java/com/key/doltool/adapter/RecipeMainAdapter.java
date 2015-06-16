package com.key.doltool.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
public class RecipeMainAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public ImageView pic;
	}
	private String[] name;
	private int[] pics;
	private Context context;
	public RecipeMainAdapter(String[] name,int[] pics,Context context){
		this.name=name;
		this.pics=pics;
		this.context=context;
	}
	public int getCount() {
			return pics.length;
	}

	@Override
	public Object getItem(int arg0) {
		return name[arg0];
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
			convertView = mInflater.inflate(R.layout.item_recipe_main, null);
			holder.name=(TextView)convertView.findViewById(R.id.item);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(name[position]);
		holder.pic.setImageResource(pics[position]);
		return convertView;
	}
}