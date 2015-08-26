package com.key.doltool.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.key.doltool.R;
import com.key.doltool.view.Toast;

public class ResourcesUtil {
	/**通过R.color去拿颜色**/
	public static ColorStateList getColor(int colorId,Context context){
		Resources r=context.getResources();
		return r.getColorStateList(colorId);
	}
	public static int getColorId(int colorId,Context context){
		Resources r=context.getResources();
		return r.getColor(colorId);
	}
	/**通过R.drawable去拿图片**/
	public static Drawable getDrawable(String name,Context context){
			int id = context.getResources().getIdentifier( 
				name == null ? "no_picture" : name, "drawable", 
						"com.key.doltool .");
			Drawable image;
			if (id <= 0) { 
				image = context.getResources().getDrawable(R.drawable.ic_add_white_24dp);
			} else { 
				image = context.getResources().getDrawable(id); 
			} 
			return image; 
	}
	/**通过R.array去拿数组**/
	public static String[] getArray(Activity context,int arrayId)
	{
		Resources res =context.getResources();
		return res.getStringArray(arrayId);
	}
	
	public static int getInt(Activity context,int id)
	{
		Resources res =context.getResources();
		return res.getInteger(id);
	}
	
	public static String getHtmlByAsset(Context context,String url){
		String str="";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		InputStream inputStream = null;
		try {
			inputStream=context.getAssets().open(FileManager.WIKI+url);
			Log.i("x", ""+inputStream.toString());
         	int len;
         	byte[] buffer = new byte[1024];
         	while((len = inputStream.read(buffer)) != -1){
                 outputStream.write(buffer, 0, len);
         	}
		} catch (IOException e) {
			Log.i("x", ""+inputStream.toString());
			Log.i("x", ""+e.getMessage());
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
         try {
			str=new String(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
         return str;
	}
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
        return buffer;  
    }  
}
