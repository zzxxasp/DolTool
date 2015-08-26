package com.key.doltool.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**公共信息配置表**/
public class VoyageInfo {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    /**今天的日期**/
    public static final String WEEK="week";
    /**今日的运势**/
    public static final String DATA="data";
    @SuppressLint("CommitPrefEdits")
	public VoyageInfo(Context context){
    	sp=context.getSharedPreferences("voyage", Context.MODE_PRIVATE);
    	editor=sp.edit();
    }
	public String getWeek() {
		return sp.getString(WEEK,"1989-09-08");
	}
	public void setWeek(String date) {
		editor.putString(WEEK, date);
		editor.commit();
	}
	
	public String getData() {
		return sp.getString(DATA,"");
	}
	public void setData(String data) {
		editor.putString(DATA, data);
		editor.commit();
	}
	
	public void clear(){
		editor.clear();
		editor.commit();
	}
}