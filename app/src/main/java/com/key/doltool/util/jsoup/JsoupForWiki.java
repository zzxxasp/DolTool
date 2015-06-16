package com.key.doltool.util.jsoup;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.util.Log;
public class JsoupForWiki {
	/**BAHA源**/
	public static String BAHA_BASE_URL="http://wiki.dahanghai.org/";
	/**获得一级url**/
	public String getUrl(int index){
		String url="";
		Document doc;
		try {
		doc = Jsoup.connect(BAHA_BASE_URL).timeout(30*1000).get();
		//学校任务地址
		Elements table=doc.select("div.BH-lbox ACG-mster_box2");
		Elements ul=table.select("div.ACG-wikibox");
		Elements scholl_link=ul.get(index).select("a");
		url=scholl_link.attr("abs:href");
		Log.i("abs_url",url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}
}
