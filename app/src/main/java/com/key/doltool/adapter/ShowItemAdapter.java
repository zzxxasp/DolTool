package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.item.UseItem;
import com.key.doltool.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ShowItemAdapter extends BaseAdapter {
    private static class ViewHolder{
        public TextView name,info;
        public ImageView pic;
    }
    private List<UseItem> list=new ArrayList<>();
    private Context context;
    public ShowItemAdapter(List<UseItem> list, Context context){
        this.list=list;
        this.context=context;
    }
    public int getCount() {
        return list.size();
    }

    @Override
    public UseItem getItem(int arg0) {
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
            convertView = mInflater.inflate(R.layout.item_useitem_show, null);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.info=(TextView)convertView.findViewById(R.id.info);
            holder.pic=(ImageView)convertView.findViewById(R.id.pic);
            // 为view设置标签
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.info.setText(list.get(position).info);
        holder.name.setText(list.get(position).name);
        ViewUtil.setImageView(holder.pic,list.get(position).name,context);
        //设置
        return convertView;
    }
}