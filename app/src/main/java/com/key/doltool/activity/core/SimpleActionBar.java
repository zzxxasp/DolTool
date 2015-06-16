package com.key.doltool.activity.core;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.util.StringUtil;


public class SimpleActionBar {
	private Activity context;
	private boolean back_flag;
	private ImageView ico,menu_img;
	private TextView title,title_logo;
	private LinearLayout logo_area;
	public SimpleActionBar(Activity context,boolean back_flag){
		this.context=context;
		this.back_flag=back_flag;
		init();
	}
	private void init(){
		logo_area=(LinearLayout)context.findViewById(R.id.main_logo);
		ico=(ImageView)context.findViewById(R.id.main_back);
		title=(TextView)context.findViewById(R.id.main_title);
		title_logo=(TextView)context.findViewById(R.id.title_logo);
		if(title_logo!=null){
			title_logo.setTypeface(Typeface.createFromAsset(context.getAssets(),
	                "wiki_html/fonts/Wendy.ttf"));
		}
		menu_img=(ImageView)context.findViewById(R.id.main_menu);
	}
	
	/**返回标签事件**/
	public void setBackIcon(){
		if(back_flag){
			if(logo_area!=null){
				logo_area.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						context.finish();
					}
				});
			}
		}
	}
	/**
	 * 初始化actionBar
	 * @param txt 标题文字
	 * @param resId R.drawable (0为隐藏) 菜单图标
	 * **/
	public void initActionBar(String txt,int resId){
		title.setText(txt);
		if(resId!=0){
			menu_img.setImageResource(resId);
		}else{
			menu_img.setVisibility(View.GONE);
		}
		if(back_flag){
			ico.setImageResource(R.drawable.ic_up_holo_light);
		}else{
			ico.setImageResource(R.drawable.ic_drawer);
		}
	}
	/**
	 * 设置actionBar
	 * @param txt 标题文字
	 * @param resId R.drawable (0为隐藏) 菜单图标
	 * @param back_flag 是否允许返回操作
	 * **/	
	public void setActionBar(String txt,int resId,boolean back_flag){
		this.back_flag=back_flag;
		if(!StringUtil.isNull(txt)){
			title.setText(txt);
		}
		if(resId!=0){
			menu_img.setVisibility(View.VISIBLE);
			menu_img.setImageResource(resId);
		}else{
			menu_img.setVisibility(View.GONE);
		}
		if(this.back_flag){
			ico.setImageResource(R.drawable.ic_up_holo_light);
		}else{
			ico.setImageResource(R.drawable.ic_drawer);
		}
	}
	public void setTitle(String txt){
		if(!StringUtil.isNull(txt)){
			title.setText(txt);
		}
	}
}
