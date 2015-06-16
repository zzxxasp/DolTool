package com.key.doltool.event.app;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.key.doltool.util.CommonUtil;
import com.key.doltool.view.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class VersionManager {

    private static VersionManager versionManager = null;
    private VersionUpdate versionUpdate;
    private Activity activity;
    public static Object getInstance() {
        if (versionManager == null) {
            versionManager = new VersionManager();
        }
        return versionManager;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void checkVersion(final boolean flag) {
    	final Version v=new Version();
    	String old_verion=CommonUtil.getAppVersionName(activity);
    	ParseQuery<ParseObject> query =ParseQuery.getQuery("update");
  	  	query.whereEqualTo("name","dol_tool");
  	  	query.whereEqualTo("verion_dest",old_verion);
  	  	v.setAppname("dol_tool");
  	  	query.findInBackground(new FindCallback<ParseObject>() {
  	  		public void done(List<ParseObject> item, ParseException e) {
  	  			if (e == null) {
  	  				if(item.size()==0&&flag){
  	  					Toast.makeText(activity, "已经是最新版本", Toast.LENGTH_SHORT).show();
  	  					return;
  	  				}
  	  				for(int i=0;i<item.size();i++){
  	  					String verion=item.get(i).getString("verion_dest");
  	  					String old_verion=CommonUtil.getAppVersionName(activity);
  	  	  				if(CommonUtil.checkNetworkInfo(activity)){  	  	  					
  	  	  					if(verion.equals(old_verion)){
  	  	  	  	  				v.setVerionCode(item.get(i).getString("verion"));
  	  	  	  	  				v.setUpdataMsg(item.get(i).getString("update_info"));
  	  	  	  	  				v.setUpdataUrl(item.get(i).getString("url"));
  	  	  	  	  				v.setApp_size(item.get(i).getString("app_size"));
  	  	  	  					Log.i("1", ""+old_verion);
  	  	  	  					Log.i("2", ""+v.getVerionCode());
  	  	  	  					if(v.getVerionCode()!=null){
  	  	  	  						versionUpdate = new VersionUpdate(activity);
  	  	  	  						if(isUpdate(old_verion,v.getVerionCode())){
  	  	  	  							versionUpdate.updateVersion("大航海时代",old_verion,v);
  	  	  	  							return;
  	  	  	  						}else{
  	  	  	  							if(flag){
  	  	  	  								Toast.makeText(activity, "已经是最新版本", Toast.LENGTH_SHORT).show();
  	  	  	  								return;
  	  	  	  							}
  	  	  	  						}
  	  	  	  					}
  	  	  					} 	  	  					
  	  	  				}
  	  				}
  	  			} else {
  	              Log.d("score", "Error: " + e.getMessage());
  	          }
  	      }
  	  	});
    }
    
    private boolean isUpdate(String localVersion, String remoteVersion) {
        if (localVersion.compareTo(remoteVersion) < 0)
            return true;
        else
            return false;
    }
}
