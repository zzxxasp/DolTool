package com.key.doltool.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.adapter.util.ApdaterUtil;
import com.key.doltool.data.NPCInfo;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

public class NPCAdapter extends BaseAdapter{
	
	private static class ViewHolder{
		public TextView name;
		public TextView area,love_type,other;
		public RelativeLayout love_bar;
		public LinearLayout line1,line2,line3;
		public View line;
		public ImageView skill_img1,skill_img2,skill_img3;
		public TextView skill_txt1,skill_txt2,skill_txt3;
	} 
	public static int SIZE=20;
	private DefaultDAO dao;
	private ApdaterUtil util;
	private List<NPCInfo> list=new ArrayList<NPCInfo>();
	private Activity context;
	public NPCAdapter(List<NPCInfo> list,Activity context){
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
			convertView = mInflater.inflate(R.layout.npc_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.area=(TextView)convertView.findViewById(R.id.area);
			holder.line=(View)convertView.findViewById(R.id.line);
			holder.love_type=(TextView)convertView.findViewById(R.id.love_type);
			holder.other=(TextView)convertView.findViewById(R.id.other);
			
			holder.love_bar=(RelativeLayout)convertView.findViewById(R.id.love_bar);
			
			holder.line1=(LinearLayout)convertView.findViewById(R.id.line1);
			holder.line2=(LinearLayout)convertView.findViewById(R.id.line2);
			holder.line3=(LinearLayout)convertView.findViewById(R.id.line3);
			
			holder.skill_img1=(ImageView)convertView.findViewById(R.id.skill_img1);
			holder.skill_img2=(ImageView)convertView.findViewById(R.id.skill_img2);
			holder.skill_img3=(ImageView)convertView.findViewById(R.id.skill_img3);
			
			holder.skill_txt1=(TextView)convertView.findViewById(R.id.skill_txt1);
			holder.skill_txt2=(TextView)convertView.findViewById(R.id.skill_txt2);
			holder.skill_txt3=(TextView)convertView.findViewById(R.id.skill_txt3);
			
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder 
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		holder.area.setText(list.get(position).getCity());
		String love_type=list.get(position).getLove_type();
		String other=list.get(position).getOther();
		if(StringUtil.isNull(love_type)){
			holder.line.setVisibility(View.GONE);
			holder.love_bar.setVisibility(View.GONE);
		}else{
			holder.line.setVisibility(View.VISIBLE);
			holder.love_type.setText(love_type);
			holder.love_type.setTextColor(ResourcesUtil.getColor(R.color.Blue_SP, context));
			holder.love_bar.setVisibility(View.VISIBLE);
		}
		if(StringUtil.isNull(other)){
			holder.other.setVisibility(View.GONE);
		}else{
			holder.other.setVisibility(View.VISIBLE);
			holder.other.setText("备注:"+other);
		}
		String skill=list.get(position).getSkill_tech();
		String[] temp=skill.split(",");
		if(temp.length>2){
			holder.line3.setVisibility(View.VISIBLE);
			util.findByName(holder.skill_img3,temp[2],1);
			holder.skill_txt3.setText(temp[2]);
		}else{
			holder.line3.setVisibility(View.GONE);
		}
		if(temp.length>1){
			holder.line2.setVisibility(View.VISIBLE);
			util.findByName(holder.skill_img2,temp[1],1);
			holder.skill_txt2.setText(temp[1]);
		}else{
			holder.line2.setVisibility(View.GONE);
		}
		if(temp.length>0&&!StringUtil.isNull(temp[0])){
			holder.line1.setVisibility(View.VISIBLE);
			util.findByName(holder.skill_img1,temp[0],1);
			holder.skill_txt1.setText(temp[0]);
		}else{
			holder.line1.setVisibility(View.GONE);
			holder.line.setVisibility(View.GONE);
		}
		return convertView;
	}
}
