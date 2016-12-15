package com.key.doltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.app.util.AutoAdapterHelper;

import java.util.ArrayList;
import java.util.List;

public class AutoTextViewAdapter extends BaseAdapter implements Filterable {

    public List<String> mList;
    private Context mContext;
    private MyFilter mFilter;
    private AutoAdapterHelper autoAdapterHelper;
    private static class ViewHolder{
        public TextView name;
    }
    public AutoTextViewAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        autoAdapterHelper=new AutoAdapterHelper();
    }

    public AutoAdapterHelper getAutoAdapterHelper(){
        return autoAdapterHelper;
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_login_email, null);
            holder.name=(TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }
        else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position));
        return convertView;
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (mList == null) {
                mList = new ArrayList<>();
            }
            results.values = mList;
            results.count = mList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}