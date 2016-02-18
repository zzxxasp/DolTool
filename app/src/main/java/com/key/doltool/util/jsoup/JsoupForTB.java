package com.key.doltool.util.jsoup;

import android.util.Log;

import com.key.doltool.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupForTB {
	/**BAHA源**/
	public static String TB_BASE_URL="http://tieba.baidu.com/p/";
	/**获得数据**/
	public static String getData(String id){
		Document doc ;
		String result="";
		String str=TB_BASE_URL+id;
		try {
			doc = Jsoup.connect(str).timeout(30*1000).get();
			Elements table=doc.select("div.i");
			result=table.get(0).html();
			Log.i("result",result);
		} catch (IOException e) {
			HttpUtil.STATE=1;
			e.printStackTrace();
		}
		return result;
	}
}
