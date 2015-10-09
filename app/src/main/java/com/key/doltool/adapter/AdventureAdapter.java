package com.key.doltool.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.Trove_Count;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
public class AdventureAdapter extends BaseAdapter{
	private String[] pics={
		"dol_t_hb34.jpg","dol_t_rb25.jpg","dol_t_h2i25.jpg",
		"dol_t_ri10.jpg","dol_t_aw6.jpg","dol_t_mine24.jpg",
		"dol_t_fossol1.jpg","dol_t_plant74.jpg","dol_t_insect1.jpg",
		"dol_t_bird76.jpg","dol_t_sea107.jpg","dol_t_cs75.jpg",
		"dol_t_cm19.jpg","dol_t_cb48.jpg","dol_t_port140.jpg",
		"dol_t_geo1g9.jpg","dol_t_ast5.jpg",
	};
	private static class ViewHolder{
		public TextView name;
		public TextView size;
		public TextView now;
		public ImageView pic;
	}
	private List<Trove_Count> list;
	private Context context;
	public AdventureAdapter(List<Trove_Count> list,Context context){
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
			convertView = mInflater.inflate(R.layout.adventure_main_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.pic=(ImageView)convertView.findViewById(R.id.img);
			holder.now=(TextView)convertView.findViewById(R.id.now);
			holder.size=(TextView)convertView.findViewById(R.id.size);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder 
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(UpdataCount.name_type[list.get(position).getType()-1]);
		String now=list.get(position).getNow()+"";
		String size=list.get(position).getSize()+"";
		holder.now.setText(now);
		holder.size.setText(size);
		try {
			holder.pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.TROVE+pics[list.get(position).getType()-1])));
		} catch (IOException e) {
			holder.pic.setImageResource(R.drawable.dol_trove_defalut);
		}
		return convertView;
	}
}