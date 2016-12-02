package com.key.doltool.adapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ADCListAdapter extends BaseAdapter{
	public static int SIZE=30;
	static class ViewHolder{
		@BindView(R.id.name) TextView name;
		@BindView(R.id.pic) ImageView pic;
		@BindView(R.id.type) TextView type;
		@BindView(R.id.sex) TextView sex;
	} 
	private List<ADCInfo> list=new ArrayList<>();
	private Context context;
	public ADCListAdapter(List<ADCInfo> list,Context context){
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
			convertView = mInflater.inflate(R.layout.adc_simple_item, null);
			ButterKnife.bind(holder, convertView);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
				// 取出holder 
				holder = (ViewHolder) convertView.getTag();
		}
		//设置
		holder.name.setText(list.get(position).getName());
		try {
			holder.pic.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.ADC+list.get(position).getHead_img()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int types=list.get(position).getType();
		if(types==1){
			holder.type.setText("冒险");
		}else if(types==2){
			holder.type.setText("商业");
		}else{
			holder.type.setText("海事");
		}
		String sex=list.get(position).getSex()+"";
		if(sex.equals("男")){
			holder.sex.setBackgroundResource(R.drawable.theme_blue_btn_rate);
		}else{
			holder.sex.setBackgroundResource(R.drawable.theme_pink_btn_rate);
		}
		holder.sex.setText(sex);
		return convertView;
	}
}