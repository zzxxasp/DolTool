package com.key.doltool.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class BitMapUtil {

	/**View to Bitmap**/
	public static Bitmap view2Bitmap(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		Bitmap bitmap= v.getDrawingCache(); 
		bitmap = Bitmap.createBitmap(bitmap);
		if(bitmap!=null){    
			return bitmap;    
		}else{    
			return null;
		}    
	}
	/** ScrollView to Bitmap **/
	public static Bitmap scrollView2Bitmap(ScrollView scrollView){
		int h = 0;for (int i = 0; i < scrollView.getChildCount(); i++) 
		{
			h += scrollView.getChildAt(i).getHeight();
		}
		Bitmap bitmap = Bitmap.createBitmap
				(scrollView.getWidth(),h,Bitmap.Config.ARGB_8888);   
		// Bitmap bitmap = scrollView.getDrawingCache(true);   
		final Canvas c = new Canvas(bitmap);     
		scrollView.draw(c);
		return bitmap;
	}
	/** WebView to Bitmap **/
	@SuppressWarnings("deprecation")
	public static Bitmap webView2Bitmap(WebView webView){
		Picture snapShot = webView.capturePicture();
		Bitmap bitmap = Bitmap.createBitmap
				(webView.getWidth(),snapShot.getHeight(),Bitmap.Config.ARGB_8888);   
		// Bitmap bitmap = scrollView.getDrawingCache(true);   
		final Canvas c = new Canvas(bitmap);     
		snapShot.draw(c);
		return bitmap;
	}
	/** Bitmap  to  png**/
	public static void savepic(Bitmap bitmap,int id){
		String str=FileManager.getSaveFilePath()+"pic/"+"fa"+id+".png";
	  	File f=new File(str);
        FileOutputStream fos;
		try {
			if(!f.exists()){
				f.getParentFile().mkdir();
	   	  		f.createNewFile();
			}
			fos = new FileOutputStream(str);
			bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable){ 
		int width = drawable.getIntrinsicWidth(); 
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,  drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565);  
		Canvas canvas = new Canvas(bitmap);  
		drawable.setBounds(0,0,width,height);  
		drawable.draw(canvas); 
		return bitmap; 
	}  
	/** file to Bitmap**/
	public static Bitmap getBitmapByFile(String pathName){
		return BitmapFactory.decodeFile(pathName);
	}

	/** InputStream to Bitmap**/
	public static Bitmap getBitmapByInputStream(InputStream in){
		Bitmap bitmap=BitmapFactory.decodeStream(in);
		bitmap.setDensity(android.util.DisplayMetrics.DENSITY_HIGH);
		return bitmap;
	}

	public static Bitmap getBitmapByInputStream(InputStream in,int density){
		Bitmap bitmap=BitmapFactory.decodeStream(in);
		bitmap.setDensity(density);
		return bitmap;
	}

	public static Bitmap getBitmapByInputStream(InputStream in,boolean bool){
		return BitmapFactory.decodeStream(in);
	}
	
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapByInputStream(byte[] data){
		int inSampleSize=1;
		if(data.length>512*1024){
			inSampleSize=data.length/(512*1024);
			if(inSampleSize>4){
				inSampleSize=4;
			}
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = false; 
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		if (android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.LOLLIPOP){
			opt.inPurgeable = true;  
			opt.inInputShareable = true; 
		}
		opt.inSampleSize=inSampleSize;
		InputStream in =new ByteArrayInputStream(data);
		return BitmapFactory.decodeStream(in,null,opt);
	}

	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapByInputStream(byte[] data,int sampleSize){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		if (android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.LOLLIPOP){
			opt.inPurgeable = true;
			opt.inInputShareable = true;
		}
		opt.inSampleSize= sampleSize;
		InputStream in =new ByteArrayInputStream(data);
		return BitmapFactory.decodeStream(in,null,opt);
	}
}
