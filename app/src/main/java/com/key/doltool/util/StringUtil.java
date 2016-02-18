package com.key.doltool.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spreada.utils.chinese.ZHConverter;

public class StringUtil {
	 public static boolean isNumeric(String str){
		 Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher isNum = pattern.matcher(str);
		 return isNum.matches();
	 }
	  public static String TransToSimple(String st){
		  return ZHConverter.convert(st,1);
	  }
	  /** 输入:2014-1-5 返回:2014年1月5日 **/
	  public static String TransDateFormatterToChines(String date){
		  String[]temp_date=date.split("-");
		  return temp_date[0]+"年"+temp_date[1]+"月"+temp_date[2]+"日";
	  }
	  public static String dateFormat(Date date){
		  DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss",Locale.CHINA);
		  return df.format(date);
	  }
	  
	  public static boolean isNull(String str){
		  return str == null || str.equals("")|| str.equals("null");
	  }
	  public static String[] listToArray(List<String> list){
		String[] temp=new String[list.size()];
		for(int i=0;i<temp.length;i++){
			temp[i]=list.get(i);
		}
		return temp;
	  }
}