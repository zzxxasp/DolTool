package com.key.doltool.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
/**
 * 通用工具类
 * @author Administrator
 * @version 2013-4-22
 * 加入了判断SD卡，获取文件路径，检查网络状态
 * 返回屏幕长，宽的方法
 */
public class CommonUtil {
	public CommonUtil(){
		
	}
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}
	
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
	
	public static int getScreenWidth(Activity context) {
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		 context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}
	
	public static int getScreenHeight(Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.heightPixels;
	}
    public static boolean checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = conMan.getActiveNetworkInfo();
		return networkinfo != null && networkinfo.isAvailable();
	}
	public static String getAppVersionName(Context context) {   
	    String versionName = "";   
	    try {   
	        // ---get the package info---   
	        PackageManager pm = context.getPackageManager();   
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);   
	        versionName = pi.versionName; 
	    } catch (Exception e) {   
	        Log.e("VersionInfo", "Exception", e);   
	    }   
	    return versionName;   
	}

	/**
	 * 
	 * @Description 将app由data/app目录拷贝到sd卡下的指定目录中
	 * 
	 * @param packageName
	 *            应用程序的ID号，如com.wondertek.jttxl
	 * 
	 * @param dest
	 *            需要将应用程序拷贝的目标位置
	 * 
	 * @date 2013-7-24 下午3:32:12
	 */
	public static String backupApplication(Context context,String packageName, String dest) {
		if (packageName == null || packageName.length() == 0

		|| dest == null || dest.length() == 0) {
			return "illegal parameters";
		}
        PackageManager pm = context.getPackageManager();   
        PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}   
		// check file /data/app/appId-1.apk exists
		Log.i("",""+pi.applicationInfo.sourceDir);
		String apkPath = "/data/app/" + packageName + "-1.apk";

		File apkFile = new File(apkPath);

		if (!apkFile.exists()) {
			apkFile=new File(pi.applicationInfo.sourceDir);
			if(!apkFile.exists()){
				return apkPath + " doesn't exist!";
			}
		}

		FileInputStream in;

		try {
			in = new FileInputStream(apkFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return e.getMessage();
		}

		// create dest folder if necessary

		int i = dest.lastIndexOf('/');

		if (i != -1) {
			File dirs = new File(dest.substring(0, i));
			dirs.mkdirs();
		}

		// do file copy operation

		byte[] c = new byte[1024];

		int slen;

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(dest);

			while ((slen = in.read(c, 0, c.length)) != -1)
				out.write(c, 0, slen);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		return "success";
	}
	public static int getAppVersionCode(Context context) {   
	    int versionName = 0;   
	    try {   
	        // ---get the package info---   
	        PackageManager pm = context.getPackageManager();   
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);   
	        versionName = pi.versionCode; 
	    } catch (Exception e) {   
	        Log.e("VersionInfo", "Exception", e);
	    }   
	    return versionName;   
	}
	public static int getMetaDataInt(Context context, String name) {
		PackageManager pm = context.getPackageManager();

		try {
			ApplicationInfo e = pm.getApplicationInfo(context.getPackageName(), 128);
			return e.metaData.getInt(name);
		} catch (Exception var4) {
			Log.e("DataInt", "Exception", var4);
			return 0;
		}
	}
}