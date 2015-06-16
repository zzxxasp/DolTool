package com.key.doltool.util.jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.key.doltool.data.NewsItem;
import com.key.doltool.util.HttpUtil;
import com.key.doltool.view.Toast;
public class JsoupForTX {
	/**BAHA源**/
	public static String TX_BASE_URL="http://www.dahanghaiol.com/index.shtml";
	public static String CARD_URL="http://static.hanghaimeng.com/src/4/4c25ae16c6b2e9cac14d5b8f884d91c4.html";
	/**新闻url**/
	public static List<NewsItem> list1=new ArrayList<>();
	public static List<NewsItem> list2=new ArrayList<>();
	public static List<NewsItem> list3=new ArrayList<>();
	/**获得·News**/
	public static void getUrl(Context context){
		Document doc;
		try {
			doc = Jsoup.connect(TX_BASE_URL).timeout(30*1000).get();
			Elements table=doc.select("div.ui-news-list");
			//新闻
			Elements news_2=table.select("#news-2").select("ul");
			//活动
			Elements news_3=table.select("#news-3").select("ul");
			//公告
			Elements news_4=table.select("#news-4").select("ul");
			Log.i("1",""+table.size());
			Log.i("2",""+news_2.size());
			if(news_2.size()!=0){
				set(news_2,list1);
			}
			if(news_3.size()!=0){
				set(news_3,list2);
			}
			if(news_4.size()!=0){
				set(news_4,list3);
			}

		} catch (IOException e) {
			HttpUtil.STATE=1;
			Toast.makeText(context,"网络异常", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	public static String getCard(Context context){
		Document doc ;
		String content="";
		try {
			doc = Jsoup.connect(CARD_URL).timeout(30*1000).get();
			Elements table=doc.select("div.wikiContent");
			table.select("a.wiki").removeAttr("href");
			content=table.html();
		} catch (IOException e) {
			HttpUtil.STATE=1;
			Toast.makeText(context,"网络异常", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return content;
	}
	public static String getNews(String str,Context context){
		Document doc ;
		String result="";
		if(!str.endsWith("shtml")||str.startsWith("http://news")){
			HttpUtil.STATE=3;
			return str;
		}
		try {
			doc = Jsoup.connect(str).timeout(30*1000).get();
			Elements table=doc.select("div.content");
			if(table.size()<1){
				return str;
			}
			result=table.html();
		} catch (IOException e) {
			HttpUtil.STATE=1;
			Toast.makeText(context,"网络异常", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return result;
	}
	//设置新闻资源
	private static void set(Elements news,List<NewsItem> list){
		for(int i=0;i<7;i++){
			NewsItem item=new NewsItem();
			String news_title=news.select("li").select("span.news-tit").get(i).select("a").get(1).attr("title");
			String news_url=news.select("li").select("span.news-tit").get(i).select("a").get(1).attr("abs:href"); 
			String news_date=news.select("li").select("span.news-date").get(i).text();
			item.setTitle(news_title);
			item.setContent("");
			item.setDate(news_date);
			item.setUrl(news_url);
			list.add(item);
			Log.i("news","title:"+news_title+"\n"+"date:"+news_date+"\n"+"ur:"+news_url);
		}
	}
}
