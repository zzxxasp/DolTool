package com.key.doltool.adapter;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.key.doltool.R;
import com.key.doltool.activity.infobroad.NewsDetailActivity;
import com.key.doltool.data.NewsItem;
public class NewsAdapter extends BaseAdapter{
	private static class ViewHolder{
		public RelativeLayout item;
		public TextView name,time;
	}
	private List<NewsItem> list=new ArrayList<>();
	private Context context;
	public NewsAdapter(List<NewsItem> list,Context context){
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
			convertView = mInflater.inflate(R.layout.news_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.item_txt);
			holder.time=(TextView)convertView.findViewById(R.id.item_time);
			holder.item=(RelativeLayout)convertView.findViewById(R.id.item);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getTitle());
		holder.time.setText(list.get(position).getDate());
		final int x=position;
		holder.item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent(context,NewsDetailActivity.class);
				intent.putExtra("url",list.get(x).getUrl());
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
