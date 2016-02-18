package com.key.doltool.util.jsoup;

import android.util.Log;

import com.key.doltool.data.sqlite.TradeItem;
import com.the9tcat.hadi.DefaultDAO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 爬虫工具,主要用于爬取网络上的数据,再进行入库<br>
 * 因为是网络数据，因此所有方法不能在UI线程中加载。
 * 0.2-清理无用爬虫代码
 * @author key
 * @version 0.2
 */
public class JsoupForBaHa {
	public static String BASE_URL="http://static.hanghaimeng.com/src";
	public String tt_url="/6/66c2dccad78d9fd9e286f30ef0eb1647.html";
	/**钓鱼类型**/
	public void getTradeItemUrl(DefaultDAO dao){
		Document doc;
		try{
			doc = Jsoup.connect(BASE_URL+tt_url).timeout(30*1000).get();
			Elements wiki=doc.select("div.wikiContent");
			Elements table=wiki.select("div").select("table");
			Log.i("table-size", table.size() + "");
			Element sea_some=table.get(8);
			Element fish=table.get(9);
			Elements fish_a=fish.select("a");
			Elements sea_some_a=sea_some.select("a");
			for(int i=0;i<fish_a.size();i++){
				String url=fish_a.get(i).attr("abs:href");
				String pic_id="dol_t" + i + "s22";
				getTradeItem(url, pic_id, dao);
			}
			for(int i=0;i<sea_some_a.size();i++){
				String url=sea_some_a.get(i).attr("abs:href");
				String pic_id="dol_t" + i + "s23";
				getTradeItem(url, pic_id,dao);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getTradeItem(String url,String pic_id,DefaultDAO dao){
		Document doc;
		TradeItem items=new TradeItem();
		try{
			doc = Jsoup.connect(url).timeout(30*1000).get();
			Elements wiki=doc.select("div.wikiContent");
			Elements table=wiki.select("table");
			Element item;
			if(table.size()>0){
				if(table.size()>5){
					item=table.get(6);
				}else{
					item=table.get(table.size()-1);
				}
//				Element port=table.get(7);
				if(item.select("tr").size()>1){
					Elements tds=item.select("tr").get(1).select("td");
					if(tds.size()>3){
						if(tds.get(0).text().equals("名称")){
							tds=item.select("tr").get(2).select("td");
						}
						items.setName(tds.get(0).text());
						items.setDetail(tds.get(3).text());
						items.setPic_id(pic_id);
						items.setProducing_area("");
						if(pic_id.endsWith("s22")){
							items.setSp("钓鱼");
						}else{
							items.setSp("采集");
						}
						items.setPrice(0);
						items.setTrade(1);
						items.setType("食品");
						items.setRecipe_way("");
						dao.insert(items);
					}else{
						for(int j=0;j<table.size();j++){
							item=table.get(j);
							if(item.select("tr").size()>1){
								tds=item.select("tr").get(1).select("td");
								if(tds.size()>3){
									if(!tds.get(0).text().equals("")){
										items.setName(tds.get(0).text());
										items.setDetail(tds.get(3).text());
										items.setPic_id(pic_id);
										items.setProducing_area("");
										if(pic_id.endsWith("s22")){
											items.setSp("钓鱼");
										}else{
											items.setSp("采集");
										}
										items.setPrice(0);
										items.setTrade(1);
										items.setType("食品");
										items.setRecipe_way("");
										dao.insert(items);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}