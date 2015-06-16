package com.key.doltool.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

import com.key.doltool.view.Toast;
/**
 * 用于解析Json数据的工具类
 * @author key
 * @version 2013-5-31
 */
public class JsonUtil {
	public static String ERRINFO="";
	//读取文本文件
	 public static String readFile(String fileName,Context context){
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
         FileInputStream inputStream = null;
         try{
         	inputStream = new FileInputStream(fileName);
         	int len;
         	byte[] buffer = new byte[1024];
         	while((len = inputStream.read(buffer)) != -1){
                 outputStream.write(buffer, 0, len);
         	}
         }catch(IOException e){
        	 Toast.makeText(context,"找不到文件", Toast.LENGTH_SHORT).show();
         	e.printStackTrace();
         }
         finally{
         	try {
         		if(inputStream!=null)
         		{
					outputStream.close();
					inputStream.close();
         		}
				} catch (IOException e) {
					Toast.makeText(context,"未知错误", Toast.LENGTH_SHORT).show();
	            	e.printStackTrace();
				}
         }
         byte[] data = outputStream.toByteArray();
         return new String(data);
 } 
	 public static void writeFile(String str,String fileName,Context context){
         ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
         FileOutputStream outputStream = null;
         try{
        	outputStream = new FileOutputStream(fileName);
         	int len;
         	byte[] buffer = new byte[1024];
         	while((len = inputStream.read(buffer)) != -1){
                 outputStream.write(buffer, 0, len);
         	}
         }catch(IOException e){
        	 Toast.makeText(context,"找不到文件", Toast.LENGTH_SHORT).show();
         	e.printStackTrace();
         }
         finally{
         	try {
				if (outputStream != null) {
					outputStream.close();
				}
				inputStream.close();
			} catch (IOException e) {
					Toast.makeText(context,"未知错误", Toast.LENGTH_SHORT).show();
	            	e.printStackTrace();
				}
         }
 }
}