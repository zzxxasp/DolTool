package com.key.doltool.util.jsoup;

import com.key.doltool.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
			for(int i=0;i<table.size();i++){
				Elements a=table.get(i).select("a[href]");
				for(int j=0;j<a.size();j++){
					String temp=a.get(j).attr("href");
					if(temp.startsWith("http")&&temp.contains("quality")){
						a.get(j).select("img.BDE_Image").get(0).attr("src",a.get(j).attr("href"));
					}
					a.get(j).removeAttr("href");
				}
			}
			result=table.html();
		} catch (Exception e) {
			HttpUtil.STATE=1;
			e.printStackTrace();
		}
		return result;
	}
}
