package com.key.doltool.event;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.key.doltool.activity.adc.ADCListActivity;
import com.key.doltool.activity.mission.MissonListActivity;
import com.key.doltool.activity.squre.MapActivity;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.activity.trade.TradeItemFragment;
/**Web处理事件**/
public class WebEvent {
	private Context context;
	public WebEvent(Context context){
		this.context=context;
	}
	/**
	 * html调用android-一般页面跳转事件
	 * */
	@JavascriptInterface
	public void jump(int i){
		Class<?> c;
		switch(i){
			case 0:c=MapActivity.class;break;
			case 1:c=TradeItemFragment.class;break;
			default:c=MissonListActivity.class;break;
		}
		Intent it=new Intent(context,c);
		context.startActivity(it);
	}
	/**
	 * html调用android-页面跳转事件(交易品详情页)
	 * */
	@JavascriptInterface
	public void trade(String txt){
		Intent it=new Intent(context,TradeDetailActivity.class);
		it.putExtra("name",txt);
		context.startActivity(it);
	}
	/**
	 * html调用android-页面跳转事件(副官列表页【带搜索条件】)
	 * */
	@JavascriptInterface
	public void adc(int type,String args){
		String type_txt;
		Intent it=new Intent(context,ADCListActivity.class);
		if(type==0){
			type_txt="city like ?";
			args="%"+args+"%";
		}else{
			type_txt="type = ?";
		}
		it.putExtra("type",type_txt);
		it.putExtra("args",args);
		context.startActivity(it);
	}
}
