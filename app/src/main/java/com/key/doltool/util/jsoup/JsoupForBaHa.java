package com.key.doltool.util.jsoup;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.key.doltool.data.Mission;
import com.key.doltool.data.Trove;
import com.key.doltool.util.NumberUtil;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 爬虫工具,主要用于爬取网络上的数据,再进行入库<br>
 * 因为是网络数据，因此所有方法不能在UI线程中加载。
 * @author key
 * @version 0.1
 */
public class JsoupForBaHa {
	/**BAHA源**/
	public static String BASE_URL_SRC="http://static.hanghaimeng.com/";
	
	public static String BASE_URL="http://static.hanghaimeng.com/src";
	//发现物
	//海洋生物0
	public static String C_SEA="/a/aa5d897ff8696aec5b6b9d5dfbe653c6.html";
	//史迹 1
	public static String H_B="/f/f249e9783571ff4b6c70adcb42e06c67.html";
	//宗教建筑2
	public static String R_B="/2/24c1aa9d72615772c5bf8f2742c39f83.html";
	//美术品3
	public static String A_W="/d/d206e927108aa8955f79dd8052c8bcd2.html";
	//化石4
	public static String FOSSIL="/6/6021c5763aff248ace79e9a9d2d7d04c.html";
	//昆虫5
	public static String INSECT="/4/4db138be3ed8659087929553ba95d4c4.html";
	//小型生物6
	public static String C_S="/0/0b90bd88f1f1509790b0f91c4b87427f.html";
	//大型生物7
	public static String C_B="/e/e4ff499fca41e1e870bc34249f5996d3.html";
	//港口8
	public static String PORT="/1/1cc66635fcb926eeeda08823c25b93f8.html";
	//历史遗物1 9
	public static String H_I_1="/1/1864b2575de5aba2382db66393a6446c.html";
	//历史遗物2 10
	public static String H_I_2="/c/ce9dc3fe3a6ae45301e7e9d3f80564e6.html";
	//宗教遗物 11
	public static String R_I="/2/28935b14a70b761247d2d946d30eff8d.html";
	//宝藏 12  
	public static String MINE="/b/b1464c7ef67820ef98b4ff56eab7809e.html";
	public static String MINE_1="/c/c8d96186587456292655eda530f68a62.html";
	public static String MINE_2="/0/012e75e6e0ddfdecebbf8e2dd63207b0.html";
	//植物 13
	public static String PLANT="/7/755d8f4c0dc45587869239dcb201850e.html";
	//中型生物 14
	public static String C_M="/c/cdd3d75257ad7160da6f8fbc20fbb989.html";
	//地理1 15
	public static String GEOG1="/8/84fe22ec07da5006dccd8645e079c365.html";
	//地理2 16
	public static String GEOG2="/f/fd1f34a0945c0f0b0f857d2ca9f08a46.html";
	public static String BIRD="/f/f88d605084afc85173b51d4f4dbcf6e3.html";
	public static String[] FIND={C_SEA,H_B,R_B,A_W,FOSSIL,INSECT,C_S,C_B,
		PORT,H_I_1,H_I_2,R_I,MINE,PLANT,C_M,GEOG1,GEOG2,BIRD};
	public String diaoyu_url=BASE_URL+"/8/8e5899b8d4bce7e6daefb8d4c6697674.html";
	public String c_url=BASE_URL+"/e/e82bf8bfd84d3b3714bbc84947769e8a.html";
	public String h_url=BASE_URL+"/5/58c2a995e60313311a9d1a51bb34c0fd.html";
	public String a_url=BASE_URL+"/e/e028fbea51e3bfbfa561e8c6cd9eb8b4.html";
	public String m_url=BASE_URL+"/f/fc9d1b2afe428246068711bc4a1a30cb.html";
	public String ms_url=BASE_URL+"/c/cba62b1c25dbf0785fbe2835001da9a2.html";
	public String g_url=BASE_URL+"/b/b34e7e93ab398557850f8cedf4cf004f.html";
	public String r_url=BASE_URL+"/f/f8d36ecf34998e065af9db48d56ef06b.html";
	/**发现物**/
	public void getFind(String url,DefaultDAO dao){
			Document doc = null;
			try {
				doc = Jsoup.connect(BASE_URL+url).timeout(30*1000).get();
				Elements wiki=doc.select("div.wikiContent");
				Element table;
				Elements tr = null;
				if(url!=MINE_1||url!=MINE_2){
//					table=wiki.select("div").select("table[cellpadding=3]").get(1);
//					tr=table.select("tr");
				}
				if(url==FIND[0]){
					do_c_sea(tr,dao);
				}
				else if(url==FIND[1]){
					do_H_B(tr,dao);
				}
				else if(url==FIND[2]){
					do_R_B(tr,dao);
				}
				else if(url==FIND[3]){
					do_A_W(wiki,dao);
				}
				else if(url==FIND[4]){
					do_FOSSIL(wiki,dao);
				}
				else if(url==FIND[5]){
					do_INSECT(tr,dao);
				}
				else if(url==FIND[6]){
					do_C_S(tr,dao);
				}
				else if(url==FIND[7]){
					do_C_B(tr,dao);
				}
				else if(url==FIND[8]){
					do_PORT(wiki,dao);
				}
				else if(url==FIND[9]){
					do_H_I_1(tr,dao);
				}
				else if(url==FIND[10]){
					do_H_I_2(tr,dao);
				}
				else if(url==FIND[11]){
					do_R_I(wiki,dao);
				}
				else if(url==FIND[12]||url==MINE_1||url==MINE_2){
					do_MINE(wiki,dao);
				}				
				else if(url==FIND[13]){
					do_PLANT(tr,dao);
				}				
				else if(url==FIND[14]){
					do_C_M(tr,dao);
				}
				else if(url==FIND[15]){
					do_GEOG1(wiki,dao);
				}
				else if(url==FIND[16]){
					do_GEOG2(wiki,dao);
				}				
				else if(url==FIND[17]){
					do_BIRD(wiki,dao);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void getMisson(String name,String url,DefaultDAO dao){
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(30*1000).get();
			Elements wiki=doc.select("div.wikiContent");
			Elements table=wiki.select("div").select("table");
			Elements tr=table.select("tr");
			if(table.size()<=1){
				tr=table.get(0).select("tr");
			}else if(table.size()>5){
				tr=table.get(6).select("tr");
			}
			else{
				tr=table.get(1).select("tr");
			}
			Mission mission=new Mission();
			mission.setName(name);
			for(int i=0;i<tr.size();i++){
				if(tr.get(i).select("td").size()<2){
					i++;
					if(i==tr.size()){
						i--;
						continue;
					}
				}
				Element td=tr.get(i).select("td").get(1);
				switch(i){
					case 0:
						System.out.println("任务接受地点:"+td.text());
						mission.setStart_city(td.text());
						break;
					case 1:
						System.out.println("等级:"+td.text());
						mission.setLevel(td.text());
						break;
					case 2:
						System.out.println("时限:"+td.text());
						mission.setTime_up(td.text());
						break;
					case 3:
						System.out.println("必要技能:"+td.text());
						mission.setSkill_need(td.text());
						break;
					case 4:
						System.out.println("定金/报酬:"+td.text());
						mission.setMoney(td.text());
						break;
					case 5:
						System.out.println("经验/声望:"+td.text());
						mission.setExp(td.text());
						break;
				}				
				if(tr.get(i).select("td").get(0).text().contains("入手物")){
					System.out.println("入手物:"+td.text());
					mission.setGet_item(td.text());
				}
				if(tr.get(i).select("td").get(0).text().contains("发现物")){
					System.out.println("类别/发现物/经验:"+td.text());
					mission.setFind_item(td.text());
				}
				if(tr.get(i).select("td").get(0).text().contains("流程")){
					System.out.println("任务流程:"+td.text());
					mission.setDaily(td.text());
				}
				if(tr.get(i).select("td").get(0).text().contains("备")){
					System.out.println("备注:"+td.text());
					mission.setOther(td.text());
				}
				if(tr.get(i).select("td").get(0).text().contains("委讬")){
					System.out.println("委讬内容:"+td.text());
				}
				if(tr.get(i).select("td").get(0).text().contains("对话")){
					System.out.println("对话内容:"+td.text());
				}
			}
			onlyOneInsertMission(mission,dao);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//海洋生物处理
	private void do_c_sea(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
			trove.setType("海洋生物");
			trove.setPic_id("dol_t_"+"sea"+i);
			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
			trove.setCard_point(Integer.parseInt(td.get(8).text()));
			trove.setMisson(td.get(6).text().replaceAll("#", ":"));
			trove.setFeats(Integer.parseInt(td.get(7).text()));
			System.out.print("海洋生物");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).select("a").text().replaceAll("『发现物』", ""));
			//链接:td.get(2).select("a").attr("abs:href");
			if(!td.get(3).text().matches("[0-9]*")||td.get(3).text().equals("")||td.get(3).text().equals(" ")||td.get(3).text().equals("　")){
				System.out.println("钓鱼:"+td.get(5).text());
				trove.setNeed("钓鱼:"+td.get(5).text());
			}else{
				System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
				trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
			}
			System.out.println("任务/地图："+td.get(6).select("a").text().replaceAll("#", ":"));
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(diaoyu_url)){
				System.out.println("类型："+"钓鱼-3");
				trove.setGetWay(3);
			}
			else if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				System.out.println("类型："+"任务-1");
				trove.setGetWay(1);
				getMisson(td.get(6).select("a").text().replaceAll("#", ":"),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
//			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_H_B(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
			trove.setType("史迹");
			trove.setPic_id("dol_t_"+"hb"+i);
			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
			trove.setCard_point(Integer.parseInt(td.get(8).text()));
			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
			trove.setFeats(Integer.parseInt(td.get(7).text()));
			System.out.print("史迹");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).select("a").text().replaceAll("『发现物』", ""));
			if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("　")||td.get(5).text().equals("")){
				System.out.println("视认:"+td.get(3).text()+","+"考古学:"+td.get(4).text());
				trove.setNeed("视认:"+td.get(3).text()+","+"考古学:"+td.get(4).text());
			}else{
				System.out.println("视认:"+td.get(3).text()+","+"考古学:"+td.get(4).text()+","+"宗教学:"+td.get(5).text());
				trove.setNeed("视认:"+td.get(3).text()+","+"考古学:"+td.get(4).text()+","+"宗教学:"+td.get(5).text());
			}
			System.out.println("任务/地图："+td.get(6).select("a").text());
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(h_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).select("a").text(),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
//			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_R_B(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
			trove.setType("宗教建筑");
			trove.setPic_id("dol_t_"+"rb"+i);
			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
			trove.setCard_point(Integer.parseInt(td.get(8).text()));
			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
			trove.setFeats(Integer.parseInt(td.get(7).text()));
			trove.setNeed("视认:"+td.get(3).text()+","+"宗教学:"+td.get(4).text());
			System.out.print("宗教建筑");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).select("a").text().replaceAll("『发现物』", ""));
			System.out.println("视认:"+td.get(3).text()+","+"宗教学:"+td.get(4).text());
			System.out.println("任务/地图："+td.get(6).select("a").text());
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(h_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).select("a").text(),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
//			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_A_W(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
			trove.setType("美术品");
			trove.setPic_id("dol_t_"+"aw"+i);
			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
			trove.setCard_point(Integer.parseInt(td.get(8).text()));
			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
			int t=0;
			try {
				t=Integer.parseInt(td.get(7).text());
			}catch(Exception e){
				
			}
			trove.setFeats(t);
			System.out.print("美术品");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
			if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("　")||td.get(5).text().equals("")){
				System.out.println("搜索:"+td.get(3).text()+","+"美术:"+td.get(4).text());
				trove.setNeed("搜索:"+td.get(3).text()+","+"美术:"+td.get(4).text());
			}else{
				System.out.println("搜索:"+td.get(3).text()+","+"美术:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
				trove.setNeed("搜索:"+td.get(3).text()+","+"美术:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
			}
			System.out.println("任务/地图："+td.get(6).text());
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(a_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text(),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
//			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_FOSSIL(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("化石");
//			trove.setPic_id("dol_t_"+"fossol"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("搜索:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("化石");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).select("a").text().replaceAll("『发现物』", ""));
//			System.out.println("搜索:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).select("a").text());
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).select("a").text(),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
//			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_INSECT(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("昆虫");
//			trove.setPic_id("dol_t_"+"insect"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("昆虫");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).select("a").text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).select("a").text());
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_C_S(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("小型生物");
//			trove.setPic_id("dol_t_"+"cs"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("小型生物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text());
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_C_B(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("大型生物");
//			trove.setPic_id("dol_t_"+"cb"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("大型生物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_PORT(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
			trove.setType("港口");
			trove.setPic_id("dol_t_"+"port"+i);
			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
			trove.setCard_point(Integer.parseInt(td.get(6).text()));
			trove.setMisson(td.get(4).text().replaceAll("图：", ""));
			int t=0;
			try {
				t=Integer.parseInt(td.get(5).text());
			}catch(Exception e){
				
			}
			trove.setFeats(t);
			trove.setNeed(td.get(3).text());
			System.out.print("港口");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
			System.out.println("许可证:"+td.get(3).text());
			System.out.println("港口区域:"+td.get(4).text());
			System.out.println("功绩："+td.get(5).text());
			System.out.println("卡片点数："+td.get(6).text());
			trove.setGetWay(3);
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_H_I_1(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("历史遗物");
//			trove.setPic_id("dol_t_"+"h1i"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0,k=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//				k=Integer.parseInt(td.get(8).text());
//			}catch(Exception e){
//				
//			}
//			trove.setCard_point(k);
//			trove.setFeats(t);
//			System.out.print("历史遗物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			if(td.get(3).text().equals("　")||td.get(3).text().equals("")){
//				System.out.println("A:1");
//			}
//			else if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("　")||td.get(5).text().equals("")||td.get(5).select("div").size()!=0){
//				trove.setNeed("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", ""));
//				System.out.println("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", ""));
//			}else{
//				trove.setNeed("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", "")+","+"开锁:"+td.get(5).text().replaceAll("　", ""));
//				System.out.println("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", "")+","+"开锁:"+td.get(5).text().replaceAll("　", ""));
//			}
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(h_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_H_I_2(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("历史遗物");
//			trove.setPic_id("dol_t_"+"h2i"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0,k=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//				k=Integer.parseInt(td.get(8).text());
//			}catch(Exception e){
//				
//			}
//			trove.setCard_point(k);
//			trove.setFeats(t);
//			System.out.print("历史遗物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			if(td.get(3).text().equals("　")||td.get(3).text().equals("")){
//				System.out.println("A:1");
//			}
//			else if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("　")||td.get(5).text().equals("")||td.get(5).select("div").size()!=0){
//				trove.setNeed("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", ""));
//				System.out.println("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", ""));
//			}else{
//				trove.setNeed("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", "")+","+"开锁:"+td.get(5).text().replaceAll("　", ""));
//				System.out.println("搜索:"+td.get(3).text().replaceAll("　", "")+","+"考古学:"+td.get(4).text().replaceAll("　", "")+","+"开锁:"+td.get(5).text().replaceAll("　", ""));
//			}
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(h_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_R_I(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("宗教遗物");
//			trove.setPic_id("dol_t_"+"ri"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			System.out.print("宗教遗物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("")||td.get(5).text().equals(" ")||td.get(5).text().equals("　")){
//				trove.setNeed("搜索:"+td.get(3).text().trim()+","+"宗教学:"+td.get(4).text().trim());
//				System.out.println("搜索:"+td.get(3).text().trim()+","+"宗教学:"+td.get(4).text().trim());
//			}else{
//				trove.setNeed("搜索:"+td.get(3).text()+","+"宗教学:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
//				System.out.println("搜索:"+td.get(3).text()+","+"宗教学:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
//			}
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(r_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_MINE(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=1]").get(0);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.size()<3){
				System.out.print(tr.size());
				continue;
			}
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("财宝");
//			trove.setPic_id("dol_t_"+"mine"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0,c=0;
			System.out.print("财宝");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
			if(!td.get(3).text().matches("[0-9]*")||td.get(3).text().equals("")||td.get(3).text().equals(" ")||td.get(3).text().equals("　")){
				System.out.println("无须条件");
				trove.setNeed("无须条件");
			}
			else if(!td.get(5).text().matches("[0-9]*")||td.get(5).text().equals("")||td.get(5).text().equals(" ")||td.get(5).text().equals("　")){
				System.out.println("搜索:"+td.get(3).text().trim()+","+"财宝鉴定:"+td.get(4).text().trim());
				trove.setNeed("搜索:"+td.get(3).text().trim()+","+"财宝鉴定:"+td.get(4).text().trim());
			}else{
				System.out.println("搜索:"+td.get(3).text()+","+"财宝鉴定:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
				trove.setNeed("搜索:"+td.get(3).text()+","+"财宝鉴定:"+td.get(4).text()+","+"开锁:"+td.get(5).text());
			}
			if(td.size()<=9){
//				try {
//					t=Integer.parseInt(td.get(7).text());
//					c=Integer.parseInt(td.get(8).text());
//				}catch(Exception e){
//					
//				}
//				trove.setFeats(t);
//				trove.setCard_point(c);
//				System.out.println("功绩："+td.get(7).text());
//				System.out.println("卡片点数："+td.get(8).text());
			}
			else{
//				try {
//					t=Integer.parseInt(td.get(8).text());
//					c=Integer.parseInt(td.get(9).text());
//				}catch(Exception e){
//					
//				}
//				trove.setFeats(t);
//				trove.setCard_point(c);
//				System.out.println("功绩："+td.get(8).text());
//				System.out.println("卡片点数："+td.get(9).text());
			}
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(m_url)||td.get(6).select("a").attr("abs:href").equals(ms_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")&&!td.get(6).text().contains("图：")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_PLANT(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("植物");
//			trove.setPic_id("dol_t_"+"plant"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("植物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_C_M(Elements tr,DefaultDAO dao){
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("中型生物");
//			trove.setPic_id("dol_t_"+"cm"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("中型生物");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}	
	private void do_BIRD(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("鸟类");
//			trove.setPic_id("dol_t_"+"bird"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setNeed("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.print("鸟类");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("生态调查:"+td.get(3).text()+","+"生物学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			if(td.get(6).select("a").attr("abs:href").equals(c_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_GEOG1(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("地理");
//			trove.setPic_id("dol_t_"+"geo1g"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			int t=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setCard_point(Integer.parseInt(td.get(8).text()));
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			trove.setNeed("视认:"+td.get(3).text()+","+"地理学:"+td.get(4).text());
//			System.out.print("地理");
//			System.out.println("发现物等级："+td.get(1).text());
//			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
//			System.out.println("视认:"+td.get(3).text()+","+"地理学:"+td.get(4).text());
//			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
//			System.out.println("功绩："+td.get(7).text());
//			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(g_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private void do_GEOG2(Elements wiki,DefaultDAO dao){
		Element table=wiki.select("div").select("table[cellpadding=3]").get(2);
		Elements tr=table.select("tr");
		for(int i=1;i<tr.size();i++){
			Elements td=tr.get(i).select("td");
			if(td.get(1).text().equals("★")){
				continue;
			}
			Trove trove=new Trove();
			//Base-Get
//			trove.setType("地理");
//			trove.setPic_id("dol_t_"+"geo2g"+i);
//			trove.setRate(NumberUtil.wordToNumber(td.get(1).text()));
//			int t=0,c=0;
//			try {
//				t=Integer.parseInt(td.get(7).text());
//				c=Integer.parseInt(td.get(8).text());
//			}catch(Exception e){
//				
//			}
//			trove.setFeats(t);
//			trove.setCard_point(c);
//			trove.setName(td.get(2).text().replaceAll("『发现物』", ""));
//			trove.setMisson(td.get(6).text().replaceAll("图：", ""));
//			trove.setNeed("视认:"+td.get(3).text()+","+"地理学:"+td.get(4).text());
			System.out.print("地理");
			System.out.println("发现物等级："+td.get(1).text());
			System.out.println("发现物名称："+td.get(2).text().replaceAll("『发现物』", ""));
			System.out.println("视认:"+td.get(3).text()+","+"地理学:"+td.get(4).text());
			System.out.println("任务/地图："+td.get(6).text().replaceAll("图：", ""));
			System.out.println("功绩："+td.get(7).text());
			System.out.println("卡片点数："+td.get(8).text());
			//任务判断
			if(td.get(6).select("a").attr("abs:href").equals(g_url)){
				System.out.println("类型："+"地图-2");
				trove.setGetWay(2);
			}else if(!td.get(6).select("a.wiki").attr("abs:href").equals("")){
				trove.setGetWay(1);
				getMisson(td.get(6).text().replaceAll("图：", ""),td.get(6).select("a.wiki").attr("abs:href"),dao);
			}
			onlyOneInsertTrove(trove,dao);
		}
	}
	private  void onlyOneInsertTrove(Trove trove,DefaultDAO dao){
//		   String[] x={trove.getName()+""};
//		   List<SailBoat>list=(List<SailBoat>) dao.select(Trove.class, 
//	        		false, "name=?", x, 
//					null, null, null, null); 
//	        if(list.size()==0)
//	        	dao.insert(trove);
			//dao.update(trove,new String[]{"pic_id"},"name=? and misson=?",new String[]{trove.getName(),trove.getMisson()});
	}
	@SuppressWarnings("unchecked")
	private  void onlyOneInsertMission(Mission mission,DefaultDAO dao){
		   if(mission!=null){
			   String[] x={mission.getName()+""};
			   List<Mission>list=(List<Mission>) dao.select(Mission.class, 
		        		false, "name=?", x, 
						null, null, null, null); 
		        if(list.size()==0)
		        	dao.insert(mission);
		   }
	}
}