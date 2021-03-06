package com.key.doltool.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.util.db.DataSelectUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 数据库处理工具
 */
public class DBUtil {
	/**单个小于1M的db拷贝方式 **/
	public static void copyDB(Context context, android.os.Handler handler){
		int tagert =0;
		boolean flag=true;
		List<Integer> list = new ArrayList<>();
		List<Integer> list_mission;
		Gson s=new Gson();
		String str;
		String str2;
		String fileName=FileManager.getSaveFilePath(context)+"backup.json";
		String fileName2=FileManager.getSaveFilePath(context)+"backup2.json";
		SystemInfo system=new SystemInfo(context);
		UpdataCount event=new UpdataCount(context);
		
		File install=new File(FileManager.getSaveFilePath(context)+FileManager.DOWNLOAD + "/"+"dol_new.apk");
		File dir=new File(FileManager.getSaveFilePath(context));
		//初始文件夹生成
		if(!dir.exists()){
			dir.mkdir();
		}
		//apk安装文件删除
		if(install.exists()){
			install.delete();
		}
		File f=context.getDatabasePath("demo.db");	
		if(!f.exists()){
			f.getParentFile().mkdir();
			flag=false;
		}
		else{
			DefaultDAO dao=SRPUtil.getDAO(context);
			Log.i("com", ""+CommonUtil.getAppVersionName(context));
			tagert=DataSelectUtil.dbVerion(dao);
			Log.i("data", ""+tagert);
			//缺点:每次更新都要复制数据库
			if(CommonUtil.getMetaDataInt(context,"DB_VERSION")==tagert){
				if(system.getOverFlag()==1){
					return ;
				}
			}else{
				system.setOverFlag(0);
			}
		}
		try{
			//保存发现物标记数量
			if(flag&&tagert>-1){
				handler.sendMessage(handler.obtainMessage(1));
				list=event.saveIdFlag();
				JsonUtil.writeFile(s.toJson(list), fileName, context);
				//写文本
				list_mission=event.saveMission(tagert);
				if(list_mission!=null){
					JsonUtil.writeFile(s.toJson(list_mission), fileName2, context);
				}else{
					tagert=0;
				}
				Log.i("记录数据", list.size()+"");
			}
			InputStream is = context.getResources().openRawResource(R.raw.demo);
			FileOutputStream fos = new FileOutputStream(f.getParent()+"/demo.db");
			byte[] buffer = new byte[8192];
			int count;
			Log.i("true", "s");
			// 开始复制testDatabase.db文件
			while ((count = is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			//还原发现物标记数量
			if(flag&&tagert>-1){
				//读文本
				str=JsonUtil.readFile(fileName, context);
				list=s.fromJson(str, new TypeToken<List<Integer>>(){}.getType());  
				event.backSave(list);
				if(tagert!=0){
					str2=JsonUtil.readFile(fileName2, context);
					list_mission=s.fromJson(str2, new TypeToken<List<Integer>>(){}.getType());  
					event.backSaveMission(list_mission);
				}
				File back_file=new File(fileName);
				if(back_file.exists()){
					back_file.delete();
				}
				File back_file2=new File(fileName2);
				if(back_file2.exists()){
					back_file2.delete();
				}
			}
			Log.i("成功","OK");
			//成功设置为1
			system.setOverFlag(1);
			list.clear();
		}catch(Exception e){
			e.printStackTrace();
			handler.sendMessage(handler.obtainMessage(2));
		}
	}
	/**多个小于1M的db拷贝方式（实现用工具分割数据库文件）**/
	public static void copyBigDataBase(Context context){
		File f=context.getDatabasePath("demo.db");
		if(!f.exists())
			f.getParentFile().mkdir();
		else{
			//(有Db去判断版本，版本相同则不复制，
		}
		try{
		InputStream is = context.getResources().openRawResource(R.raw.demo);
		FileOutputStream fos = new FileOutputStream(f.getParent()+"/demo.db");
		for (int i = 0; i < 100; i++) {
			is = context.getAssets().open("db/demo" + "." + i);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer))>0){
				fos.write(buffer, 0, length);
			}
			fos.flush();
			is.close();
		}
		fos.close();
		}catch(Exception e){
			Toast.makeText(context,"初始化数据库失败",Toast.LENGTH_SHORT).show(); 
		}
	}
}