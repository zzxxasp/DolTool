package com.key.doltool.adapter;
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
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
public class TagPicAdapter extends BaseAdapter{
	private String[] item_txt;
	private DefaultDAO dao;
	private ApdaterUtil util;
	private static class ViewHolder{
		public ViewGroup item;
		public TextView item_name;
		public ImageView pic;
	}
	private Context context;
	public TagPicAdapter(Context context,String[] item_txt){
		this.context=context;
		this.item_txt=new String[item_txt.length];
		this.item_txt=item_txt;
		dao=SRPUtil.getDAO(context);
		util=new ApdaterUtil(context, dao);
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
			convertView = mInflater.inflate(R.layout.item_layout_pic, null);
			holder.item_name=(TextView)convertView.findViewById(R.id.txt);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic);
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
		final int number=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,AbilityForNormalDetailActivity.class);
				it.putExtra("name",item_txt[number]);
				context.startActivity(it);
			}
		});
		util.findByName(holder.pic,item_txt[position],1);
		return convertView;
	}
}