package com.key.doltool.adapter;
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

import com.key.doltool.R;
import com.key.doltool.activity.ability.AbilityForNormalDetailActivity;
import com.key.doltool.adapter.util.ApdaterUtil;
import com.key.doltool.data.ADCSkill;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
public class ADCSkillListAdapter extends BaseAdapter{
	private static class ViewHolder{
		public ViewGroup item;
		public TextView name;
		public ImageView pic;
		public TextView need;
	} 
	private List<ADCSkill> list=new ArrayList<>();
	private Context context;
	private DefaultDAO dao;
	private ApdaterUtil util;
	public ADCSkillListAdapter(List<ADCSkill> list,Context context){
		this.list=list;
		this.context=context;
		dao=SRPUtil.getDAO(context);
		util=new ApdaterUtil(context, dao);
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
			convertView = mInflater.inflate(R.layout.adc_simpleskill_item, null);
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
		util.findByName(holder.pic,list.get(position).getName(),1);
		holder.need.setText(list.get(position).getNeed().replaceAll(" ", ""));
		//新增技能跳转:
		final int number=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,AbilityForNormalDetailActivity.class);
				it.putExtra("name", list.get(number).getName());
				context.startActivity(it);
			}
		});
		return convertView;
	}
}