package com.key.doltool.adapter;

import com.key.doltool.R;
import com.key.doltool.util.ResourcesUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerArrayAdapter extends ArrayAdapter<String> {
  private Context mContext;
  private String [] mStringArray;
  private int type=0;
  public SpinnerArrayAdapter(Context context, String[] stringArray) {
	    super(context, android.R.layout.simple_spinner_item, stringArray);
	    mContext = context;
	    mStringArray=stringArray;
	  }
  public SpinnerArrayAdapter(Context context, String[] stringArray,int type) {
    super(context, android.R.layout.simple_spinner_item, stringArray);
    mContext = context;
    mStringArray=stringArray;
    this.type=type;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //修改Spinner展开后的字体颜色
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
    }
    //此处text1是Spinner默认的用来显示文字的TextView
    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
    tv.setText(mStringArray[position]);
    tv.setTextSize(16);
    tv.setTextColor(ResourcesUtil.getColor(R.color.white, mContext));
    return convertView;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // 修改Spinner选择后结果的字体颜色
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
    }
    //此处text1是Spinner默认的用来显示文字的TextView
    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
    tv.setText(mStringArray[position]);
    tv.setTextSize(16);
    if(type==0){
    	tv.setTextColor(Color.WHITE);
    }else{
    	tv.setTextColor(ResourcesUtil.getColor(R.color.Black_SP, mContext));
    }
    return convertView;
  }
}