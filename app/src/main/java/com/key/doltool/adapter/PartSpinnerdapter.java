package com.key.doltool.adapter;
import java.io.IOException;
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
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
public class PartSpinnerdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public ImageView pic;
	}
	private List<Part> list=new ArrayList<>();
	private Context context;
	public PartSpinnerdapter(List<Part> list,Context context){
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
			convertView = mInflater.inflate(R.layout.dockyard_main_listview_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		try {
			holder.pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.BOAT+list.get(position).getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
