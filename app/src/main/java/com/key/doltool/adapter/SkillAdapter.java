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
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
public class SkillAdapter extends BaseAdapter{
	private static class ViewHolder{
		public ImageView pic;
		public TextView name;
		public TextView type;
	} 
	public static int SIZE=30;
	private List<Skill> list=new ArrayList<>();
	private Context context;
	public SkillAdapter(List<Skill> list,Context context){
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
			convertView = mInflater.inflate(R.layout.item_skill, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.type=(TextView)convertView.findViewById(R.id.type);
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
			holder.pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.SKILL+list.get(position).getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int types=list.get(position).getType();
		if(types==1){
			holder.type.setText("冒险");
		}else if(types==2){
			holder.type.setText("商业");
		}else if(types==3){
			holder.type.setText("海事");
		}else if(types==4){
			holder.type.setText("语言");
		}else if(types==5){
			holder.type.setText("副官");
		}else{
			holder.type.setText("船只");
		}
		
		return convertView;
	}
}
