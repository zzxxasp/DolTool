package com.key.doltool.event;

import android.content.Context;

import com.key.doltool.view.Toast;
import com.parse.ParseException;

public class ParseEvent {
	/**错误控制**/
	public static void error(Context context,ParseException e){
		String content="";
		e.printStackTrace();
		switch(e.getCode()){
			case 1:content="服务器网络异常";
			case 100:content="网络连接失败";
			case 208:content="用户已登录";
			case 108:content="操作不允许";
		}
		Toast.makeText(context,content+e.getCode(),Toast.LENGTH_SHORT).show();
	}
}
