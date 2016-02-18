package com.key.doltool.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.key.doltool.data.LuckyInfo;
import com.key.doltool.util.NumberUtil;
/**运势处理事件**/
public class FortuneEvent {
	private Context context;
	private double chance=50;
	private int good_time=0;
	private int bad_time=0;
	public static String[] type={
		"探险","远航","寻宝","采集","打捞",
		"生产","出售","购入","投资","实验",
		"收夺","讨伐","抢劫","练战","蓝海"
	};
	public FortuneEvent(Context context){
		this.context=context;
	}
	/**
	 * 规则:<br>
	 * ①每天获得一种运势，每天随机<br>
	 * ②50%获得凶吉,会随着出现的次数递减概率
	 * 例如：第一次出现吉，则第二次出现吉的概率为25%<br>
	 * ③出现吉或者凶的数量为随机数(0-6),另吉或者凶最多不能超过4
	 * 另外0的概率要下调，如果第一次为0会重新获得次,如果第二次为
	 * 0,1则在获得一次<br>
	 * ④特别事件（诸事不宜,诸事大顺）
	 **/
	public void getRandomChance(){
		//获得出现的总数
		int random_times=NumberUtil.getRandom(0,6);
		if(random_times<2){
			random_times=NumberUtil.getRandom(0,6);
		}
		if(random_times==0){
			random_times=NumberUtil.getRandom(0,6);
		}		
		if(random_times==0){
			random_times=NumberUtil.getRandom(0,6);
		}
		if(random_times==0){
			random_times=NumberUtil.getRandom(0,6);
		}
		//随机生成不同的n个数
		Set<String> set=new HashSet<>();
		Set<Integer> list_type = new HashSet<>();
		System.out.println(random_times);
		while (list_type.size() < random_times) {
			int temp=list_type.size();
			//获得本次的是吉还是凶
			int bool=getThisState();
			//获得本次出现的type是
			int random_type=NumberUtil.getRandom(0,14);
			list_type.add(random_type);
			//如果加入的类型之前存在则不存入
			if(temp!=list_type.size()){
				set.add(bool+"-"+random_type);
			}
		}
		Gson gson=new Gson();
		saveData(gson.toJson(set));
		System.out.println("set:"+gson.toJson(set));
	}
	private int getThisState(){
		int base=Math.abs(bad_time-good_time)+1;
		double now_chance=chance/base;
		if(good_time==4){
			now_chance=0;
		}
		if(bad_time==4){
			now_chance=100;
		}
		if(NumberUtil.getRandomByPercent(now_chance)){
			good_time++;
			return 1;
		}else{
			bad_time++;
			return 0;
		}
	}
	private void saveData(String str){
		//时间判断，如果存在今天的数据则不覆盖
		LuckyInfo info=new LuckyInfo(context);
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date curDate=new Date(System.currentTimeMillis());
		String time=formatter.format(curDate);
		Log.i("s",time);
		if(!info.getToday().equals(time)){
			info.setToday(time);
			info.setData(str);
		}
	}
}
