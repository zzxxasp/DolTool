package com.key.doltool.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.job.JobDetailsActivity;
import com.key.doltool.data.Job;
public class TagSkillAdapter extends BaseAdapter{
	private List<Job> list;	
	private String skill_name;
	private static class ViewHolder{
		public ViewGroup item;
		public TextView item_name;
		public TextView effect;
	}
	private Context context;
	public TagSkillAdapter(Context context,List<Job> list,String skill_name){
		this.context=context;
		this.list=list;
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
			convertView = mInflater.inflate(R.layout.item_skill_sp, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.tag_name);
			holder.effect=(TextView)convertView.findViewById(R.id.tag_details);
			holder.item=(ViewGroup)convertView.findViewById(R.id.sp_layout);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.item_name.setText(list.get(position).getName());
		final int number=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,JobDetailsActivity.class);
				it.putExtra("name", list.get(number).getName());
				context.startActivity(it);
			}
		});
		
		String sp=list.get(position).getSp();
		if(!sp.equals(skill_name)){
			holder.effect.setVisibility(View.GONE);
		}else{
			holder.effect.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
}