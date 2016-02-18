package com.key.doltool.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.imageUtil.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class TroveAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		public ImageView pic;
		public RatingBar rate;
		public RelativeLayout main;
	} 
	private List<Trove> list=new ArrayList<>();
	private int[] itemState;
	private Context context;
	public TroveAdapter(List<Trove> list,Context context){
		itemState=new int[list.size()];
		for(int i=0;i<list.size();i++){
			itemState[i]=0;
		}
		this.list=list;
		this.context=context;
	}
	public int getCount() {
			return list.size();
	}

	@Override
	public Trove getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.adventure_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.txt);
			holder.pic=(ImageView)convertView.findViewById(R.id.img);
			holder.rate=(RatingBar)convertView.findViewById(R.id.star);
			holder.main=(RelativeLayout)convertView.findViewById(R.id.main);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		if(list.get(position).getFind_flag()==1){
			holder.name.setBackgroundColor(0xff0099cc);
		}else{
			holder.name.setBackgroundColor(0xffC0C0C0);
		}
		holder.name.setText(list.get(position).getName());
		updateBackground(position, holder.main);
		ImageLoader.picassoLoad(context,
				FileManager.ROOT + FileManager.TROVE + list.get(position).getPic_id() + ".jpg",
				holder.pic);
		holder.rate.setRating(list.get(position).getRate());
		return convertView;
	}
	@SuppressWarnings("deprecation")
	public void updateBackground(int position, View view) {
		int backgroundId;
		if (itemState[position] == 1) {
			backgroundId = R.drawable.frame_light;
		} else {
			backgroundId = R.drawable.frame_white;
		}
		Drawable background = context.getResources().getDrawable(backgroundId);
		view.setBackgroundDrawable(background);
	}
	
	public void uncheckAll(){
		for(int i=0;i<itemState.length;i++){
			itemState[i] = 0;
		}
	}
	public boolean isAllChecked(){
		for(int i :itemState){
			if(i ==0) return false;
		}
		return true;
	}
	public void checkAll(){
		for(int i=0;i<itemState.length;i++){
			itemState[i] = 1;
		}
	}
	public int[] getItemState(){
		return itemState;
	}
	public int getCheckedItemCount(){
		int count = 0;
		for(int i :itemState){
			if(i ==1) count++;
		}
		return count;
	}
}