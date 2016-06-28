package com.key.doltool.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;

import com.key.doltool.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
@SuppressWarnings("deprecation")
public class ResourcesUtil {
	/**通过R.color去拿颜色**/
	public static ColorStateList getColor(int colorId,Context context){
		Resources r=context.getResources();
		if (android.os.Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
			return r.getColorStateList(colorId);
		}else {
			return r.getColorStateList(colorId,null);
		}
	}
	public static int getColorId(int colorId,Context context){
		Resources r=context.getResources();
		if (android.os.Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
			return r.getColor(colorId);
		}else {
			return r.getColor(colorId, null);
		}
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
	public static String[] getArray(Activity context,@ArrayRes int arrayId)
	{
		Resources res =context.getResources();
		return res.getStringArray(arrayId);
	}
	
	public static int getInt(Activity context,int id)
	{
		Resources res =context.getResources();
		return res.getInteger(id);
	}
	
	public static byte[] getHtmlByAsset(Context context,String url){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		InputStream inputStream = null;
		try {
			inputStream=context.getAssets().open(url);
         	int len;
         	byte[] buffer = new byte[1024];
         	while((len = inputStream.read(buffer)) != -1){
                 outputStream.write(buffer, 0, len);
         	}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		finally{
         	try {
         		if(inputStream!=null) {
					outputStream.close();
					inputStream.close();
         		}
			} catch (IOException e) {
				e.printStackTrace();
			}
         }
		return outputStream.toByteArray();
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
