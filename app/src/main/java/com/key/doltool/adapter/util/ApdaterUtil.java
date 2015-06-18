package com.key.doltool.adapter.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.ImageView;

import com.key.doltool.R;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.db.DataSelectUtil;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 列表图片关联缓存类<br>
 * 用处：缓存列表中存在的关联图片id，以便加速载入
 * **/
public class ApdaterUtil {
	private static HashMap<String,String> name_list=new HashMap<>();
	private Context context;
	private DefaultDAO dao;
	public ApdaterUtil(Context context,DefaultDAO dao){
		this.context=context;
		this.dao=dao;
	}
	private String getPicByType(int type,String name){
		/**类型1**/
		String temp="";
		if(type==1){
			temp=DataSelectUtil.getSkillPicByName(name, dao);
		}else if(type==2){
			temp=DataSelectUtil.getTradePicByName(name, dao);
		}else if(type==3){
			temp=DataSelectUtil.getRecipePicByName(name, dao);
		}
		return temp;
	}	
	public void findByName(ImageView view,String name,int type){
		String url="";
		boolean temp=true;
        for(Map.Entry<String, String> parm :name_list.entrySet()){
        	if(parm.getKey().equals(name)){
        		url=parm.getValue();
        		temp=false;
        		break;
        	}else{
        		temp=true;
        	}
        }
        if(temp){
    		String temp_name=getPicByType(type,name);
    		url=temp_name;
    		name_list.put(name,temp_name);
        }
		try {
			view.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(url+".png")));
		} catch (IOException e) {
			view.setImageResource(R.drawable.item_defalut);
		}
	}
	public void clear(){
		if(name_list!=null){
			name_list.clear();
		}else{
			name_list=new HashMap<>();
		}
	}
}
