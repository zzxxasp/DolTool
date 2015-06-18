package com.key.doltool.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import android.util.Log;

public class NumberUtil {
	public static int TEMP=0;
	/**三个数求最大，m最大就0，s最大就1，j最大就2**/
	public static int getMaxForThree(int m,int s,int j){
		if(m>=s&&m>=j)
			return 0;
		else if(s>=m&&s>=j)
			return 1;
		else
			return 2;
	}
	/**判断为0,是就为0，不是就为1**/
	public static int isZero(int x){
		if(x==0)
			return 0;
		else
			return 1;
	}
	/**判断3个数有几个true 3位2进值数计算**/
	public static int threeZero(boolean s,boolean m,boolean l){
		int total=0;
		if(s){
			total+=4;
		}
		if(m){
			total+=2;
		}
		if(l){
			total+=1;
		}
		return total;
	}
	public static int wordToNumber(String str){
		int number=0;
		if(str.contains("一"))
			number=1;
		else if(str.contains("二"))
			number=2;
		else if(str.contains("三"))
			number=3;
		else if(str.contains("四"))
			number=4;
		else if(str.contains("五"))
			number=5;
		return number;
	}
	 public static int compare_date(String DATE1, String DATE2) {
		 Log.i("DATE1",""+DATE1);
		 if(StringUtil.isNull(DATE1)){
			 return -1;
		 }
		 DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss",Locale.CHINA);
		 try {
			 Date dt1 = df.parse(DATE1);
			 Date dt2 = df.parse(DATE2);
			 Log.i("DATE1",""+DATE1);
			 Log.i("DATE2",""+DATE2);
			 if (dt1.getTime() > dt2.getTime()) {
				 System.out.println("dt1 在dt2前");
				 return 1;
			 } else if (dt1.getTime() < dt2.getTime()) {
				 System.out.println("dt1在dt2后");
				 return -1;
			 } else {
				 return 0;
			 }
		 } catch (Exception exception) {
			 exception.printStackTrace();
		 }
		 return -1;
	}
	
	
	/**处理add信息**/
	public static HashMap<String,Integer> excute_add_info(String base){
		String[] cute=base.split(",");
		HashMap<String,Integer> map=new HashMap<>();
		for (String aCute : cute) {
			String[] number_temp = aCute.split(":");
			String[] number = number_temp[1].split("~");
			int min = Integer.parseInt(number[0]);
			int max = Integer.parseInt(number[1]);
			int random = getRandom(min, max);
			map.put(number_temp[0], random);
		}
		return map;
	}
	public static int getRandom(int min,int max){
		Random ran=new Random();
		return min+(int)((ran.nextDouble())*(max-min));
	}
	public static boolean getRandomByPercent(double percent){
		Random ran=new Random();
		double max=ran.nextDouble()*100;
		return max <= percent;
	}
}
