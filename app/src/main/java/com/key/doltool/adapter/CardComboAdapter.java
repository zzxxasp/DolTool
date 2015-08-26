package com.key.doltool.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.CardCombo;
import com.key.doltool.util.StringUtil;

public class CardComboAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public TextView effect;
		public LinearLayout card_1,card_2,card_3;
		public TextView card_1_name,card_2_name,card_3_name;
	} 
	public static int SIZE=30;
	private List<CardCombo> list=new ArrayList<>();
	private Context context;
	public CardComboAdapter(List<CardCombo> list,Context context){
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
			convertView = mInflater.inflate(R.layout.cardcombo_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.effect=(TextView)convertView.findViewById(R.id.effect);
			
			holder.card_1=(LinearLayout)convertView.findViewById(R.id.card_1);
			holder.card_2=(LinearLayout)convertView.findViewById(R.id.card_2);
			holder.card_3=(LinearLayout)convertView.findViewById(R.id.card_3);
			
			holder.card_1_name=(TextView)convertView.findViewById(R.id.card_1_name);
			holder.card_2_name=(TextView)convertView.findViewById(R.id.card_2_name);
			holder.card_3_name=(TextView)convertView.findViewById(R.id.card_3_name);
			
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		holder.effect.setText("效果:"+list.get(position).getEffect());
		if(StringUtil.isNull(list.get(position).getCard_1())){
			holder.card_1.setVisibility(View.GONE);
		}else{
			holder.card_1.setVisibility(View.VISIBLE);
			holder.card_1_name.setText(list.get(position).getCard_1());
		}
		if(StringUtil.isNull(list.get(position).getCard_2())){
			holder.card_2.setVisibility(View.GONE);
		}else{
			holder.card_2.setVisibility(View.VISIBLE);
			holder.card_2_name.setText(list.get(position).getCard_2());
		}
		if(StringUtil.isNull(list.get(position).getCard_3())){
			holder.card_3.setVisibility(View.GONE);
		}else{
			holder.card_3.setVisibility(View.VISIBLE);
			holder.card_3_name.setText(list.get(position).getCard_3());
		}
		return convertView;
	}
}
