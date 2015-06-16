package com.key.doltool.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
/**公共信息配置表**/
public class LuckyInfo {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    /**今天的日期**/
    public static final String TODAY="today";
    /**今日的运势**/
    public static final String DATA="data";
    @SuppressLint("CommitPrefEdits")
	public LuckyInfo(Context context){
    	sp=context.getSharedPreferences("lucky", Context.MODE_PRIVATE);
    	editor=sp.edit();
    }
	public String getToday() {
		return sp.getString(TODAY, "1989-9-8");
	}
	public void setToday(String date) {
		editor.putString(TODAY, date);
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