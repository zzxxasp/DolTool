package com.key.doltool.activity.core;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.MenuItem;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.view.stick.StickyListHeadersAdapter;

public class MenuAdapter extends BaseAdapter implements
	StickyListHeadersAdapter, SectionIndexer {
	private static class ViewHolder{
		public TextView item_name;
	}
	private static class HeadViewHolder{
		public TextView item_name;
	}
    private List<MenuItem> list;
    private Context context;
    private LayoutInflater mInflater;
    private String[] head;
    private int index;
    public MenuAdapter(Context context,List<MenuItem> list,String[] head,int index) {
        this.context=context;
        this.list=list;
        this.head=head;
        this.index=index;
        mInflater = LayoutInflater.from(context);
    }

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = mInflater.inflate(R.layout.menu_item,parent,false);
			holder.item_name=(TextView)convertView.findViewById(R.id.content);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder 
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_name.setText(list.get(position).text);
		if(index==list.get(position).index){
			holder.item_name.setTextColor(ResourcesUtil.getColor(R.color.Blue_SP, context));
		}else{
			holder.item_name.setTextColor(ResourcesUtil.getColor(R.color.Black_SP, context));
		}
		return convertView;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeadViewHolder holder;
        if (convertView == null) {
            holder = new HeadViewHolder();
            convertView = mInflater.inflate(R.layout.menu_top, parent, false);
            holder.item_name = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (HeadViewHolder) convertView.getTag();
        }
        holder.item_name.setText(head[list.get(position).parent_id]);
        return convertView;
	}
	
	public void setIndex(int index){
		this.index=index;
	}
	@Override
	public long getHeaderId(int position) {
		return list.get(position).parent_id;
	}
}
