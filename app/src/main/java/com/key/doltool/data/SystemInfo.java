package com.key.doltool.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
/**公共信息配置表**/
public class SystemInfo {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    /**防止初始化因为手机异常失败，中途失败**/
    public static final String OVER_FLAG="init_over";
    /**记录自动更新的状态，默认为开启[1]**/
    public static final String UPDATE_FLAG="update_start";
    @SuppressLint("CommitPrefEdits")
	public SystemInfo(Context context){
    	sp=context.getSharedPreferences("system", Context.MODE_PRIVATE);
    	editor=sp.edit();
    }
	public int getOverFlag() {
		return sp.getInt(OVER_FLAG, 0);
	}
	
	public int getUpdateFlag() {
		return sp.getInt(UPDATE_FLAG,1);
	}
	
	public void setOverFlag(int flag) {
		editor.putInt(OVER_FLAG, flag);
		editor.commit();
	}
	
	public void setUpdateFlag(int flag) {
		editor.putInt(UPDATE_FLAG, flag);
		editor.commit();
	}
	
	
	
	public void clear(){
		editor.clear();
		editor.commit();
	}
}