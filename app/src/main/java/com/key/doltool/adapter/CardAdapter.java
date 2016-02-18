package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.sqlite.Card;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.imageUtil.ImageLoader;
import com.key.doltool.util.ResourcesUtil;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends BaseAdapter{
	private static class ViewHolder{
		public RelativeLayout main;
		public ImageView pic;
		public TextView name;
		public TextView point;
	}
	public static int SIZE=60;
	private List<Card> list=new ArrayList<>();
	private Context context;
	public CardAdapter(List<Card> list, Context context){
		this.list=list;
		this.context=context;
	}
	public int getCount() {
			return list.size();
	}

	@Override
	public Card getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.item_card, null);
			holder.main=(RelativeLayout)convertView.findViewById(R.id.main);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.point=(TextView)convertView.findViewById(R.id.point);
			holder.pic=(ImageView)convertView.findViewById(R.id.img);
			// 为view设置标签 
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.picassoLoad(context,
				FileManager.ROOT + FileManager.TROVE + list.get(position).pic_id + ".jpg",
				holder.pic);
		//设置
		holder.name.setText(list.get(position).name);
		updateBackground(position, holder);
		String point=list.get(position).type+":"+list.get(position).point;
		holder.point.setText(point);
		return convertView;
	}
	public void updateBackground(int position,ViewHolder holder) {
		if(list.get(position).flag==0){
			holder.name.setTextColor(ResourcesUtil.getColor(R.color.Black_SP,context));
			holder.point.setTextColor(ResourcesUtil.getColor(R.color.Black_SP,context));
			holder.main.setBackgroundResource(R.drawable.material_card);
		}else{
			holder.name.setTextColor(ResourcesUtil.getColor(R.color.white,context));
			holder.point.setTextColor(ResourcesUtil.getColor(R.color.white,context));
			holder.main.setBackgroundResource(R.drawable.material_card_rex);
		}
	}
}
