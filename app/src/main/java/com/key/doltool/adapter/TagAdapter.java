package com.key.doltool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.recipe.RecipeForTradeDetailsActivity;
import com.key.doltool.activity.trade.TradeCityDetailActivity;
import com.key.doltool.util.StringUtil;

public class TagAdapter extends BaseAdapter{
	private String[] item_txt;
			
	private static class ViewHolder{
		public ViewGroup item;
		public TextView item_name;
	}
	private Context context;
	private String name="";
	private boolean clickable=true;
	public TagAdapter(Context context,String[] item_txt){
		this.context=context;
		this.item_txt=new String[item_txt.length];
		this.item_txt=item_txt;
	}
	public TagAdapter(Context context,String[] item_txt,boolean clickable){
		this.context=context;
		this.item_txt=new String[item_txt.length];
		this.item_txt=item_txt;
		this.clickable=clickable;
	}
	public TagAdapter(Context context,String[] item_txt,String name){
		this.context=context;
		this.item_txt=new String[item_txt.length];
		this.item_txt=item_txt;
		this.name=name;
	}
	public int getCount() {
			return item_txt.length;
	}

	@Override
	public Object getItem(int arg0) {
		return item_txt[arg0];
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
			convertView = mInflater.inflate(R.layout.item_layout, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.txt);
			holder.item=(ViewGroup)convertView.findViewById(R.id.item);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.item_name.setText(item_txt[position]);
		final int index=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(index);
			}
		});
		return convertView;
	}
	private void jump(int index){
		if(clickable){
			Intent intent;
			if(StringUtil.isNull(name)){
				intent=new Intent(context,TradeCityDetailActivity.class);
				intent.putExtra("city_name",item_txt[index]);
				context.startActivity(intent);
			}else{
				intent=new Intent(context,RecipeForTradeDetailsActivity.class);
				intent.putExtra("item",item_txt[index]);
				context.startActivity(intent);
			}
		}
	}
}