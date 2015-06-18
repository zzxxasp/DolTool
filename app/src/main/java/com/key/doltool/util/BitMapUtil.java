package com.key.doltool.util;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
public class BitMapUtil {
	
	/**
	 * 用于网络读取图片(调用afinal.jar包方法)
	 * @param context	Context对象
	 * @param resid_load 读取中显示图片R.drawable.XX
	 * @param resid_fail 读取失败显示图片R.drawable.XX
	 * @param url	url地址
	 * @param image 所要显示的imageView
	 */
	public static void BitmapLoader(Context context,int resid_load,int resid_fail,String url,ImageView image){
		FinalBitmap.create(context).configLoadfailImage(resid_load)
		.configDiskCachePath(FileManager.getSaveFilePath())
		.configLoadingImage(resid_load).display(image, url);
	}
	
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
	
	/** file to Bitmap**/
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapSmallByFile(String pathName){
		File file=new File(pathName);
		int inSampleSize=1;
		if(file.length()>512*1024){
			inSampleSize=(int)file.length()/(512*1024);
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
		return BitmapFactory.decodeFile(pathName,opt);
	}
	
	/** InputStream to Bitmap**/
	public static Bitmap getBitmapByInputStream(InputStream in){
		Bitmap bitmap=BitmapFactory.decodeStream(in);
		bitmap.setDensity(android.util.DisplayMetrics.DENSITY_HIGH);
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
		int inSampleSize=sampleSize;
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

	public static Bitmap readBitMap(Context context, int resId){  
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;    
		//获取资源图片  
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);  
	}
}
