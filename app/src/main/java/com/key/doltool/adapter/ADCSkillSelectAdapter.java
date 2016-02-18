package com.key.doltool.adapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.adc.ADCDetailsActivity;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.data.ADCSkill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
public class ADCSkillSelectAdapter extends BaseAdapter{
	private static class ViewHolder{
		public ViewGroup item;
		public TextView name;
		public ImageView pic;
		public TextView need;
	} 
	private List<ADCInfo> list=new ArrayList<>();
	private Context context;
	private String skill_name;
	public ADCSkillSelectAdapter(String skill_name,List<ADCInfo> list,Context context){
		this.list=list;
		this.context=context;
		this.skill_name=skill_name;
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
			convertView = mInflater.inflate(R.layout.adc_simpleskill_item_flow, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic);
			holder.need=(TextView)convertView.findViewById(R.id.need);
			holder.item=(ViewGroup)convertView.findViewById(R.id.item);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		final int number=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,ADCDetailsActivity.class);
				it.putExtra("name", list.get(number).getName());
				context.startActivity(it);
			}
		});
		try {
			holder.pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.ADC+list.get(position).getHead_img()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<ADCSkill> skill_list=new Gson().fromJson(list.get(position).getSkill_list(), new TypeToken<List<ADCSkill>>(){}.getType());
		for(int i=0;i<skill_list.size();i++){
			if(skill_list.get(i).getName().equals(skill_name)){
				holder.need.setText(skill_list.get(i).getNeed());
			}
		}
		return convertView;
	}
}