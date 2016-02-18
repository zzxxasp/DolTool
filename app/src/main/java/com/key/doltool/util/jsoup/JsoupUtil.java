package com.key.doltool.util.jsoup;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.key.doltool.data.ADCSkill;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.TradeCityItem;
import com.key.doltool.data.item.UseItem;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.data.sqlite.City;
import com.key.doltool.data.sqlite.Job;
import com.key.doltool.data.sqlite.NPCInfo;
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.data.sqlite.Recipe;
import com.key.doltool.data.sqlite.RegularShip;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 爬虫工具,主要用于爬取网络上的数据,再进行入库<br>
 * 因为是网络数据，因此所有方法不能在UI线程中加载。
 * @author key
 * @version 0.1
 */
public class JsoupUtil {
	/**DB源**/
	public static String DB_BASE_URL="http://www.uwodb.com/tw/main.php?";
	private final static String BOAT="id=145";
	private final static String PART="id=146";
	private final static String TRADE="id=141";
	private final static String CITY="id=140";
	private final static String JOB="id=142";
	private final static String RECIPE="id=152";
	private final static String SKILL="id=143";
	private final static String ADC="id=144";
	private final static String Z_PART="id=85000050";
	private final static String NPC="id=85000016";
	private final static String REGULAR_SHIP="id=85000018";
	private final static String ME_ITEM="id=85000023";
	private List<Integer> number=new ArrayList<>();
	private String temp="";
	public JsoupUtil(Context context){
		
	}

	public void getPicMe(SRPUtil dao){
		Document doc;
		try {
			doc = Jsoup.connect(DB_BASE_URL+ME_ITEM).timeout(30*1000).get();
			Elements img=doc.select("a").select("img");
			for(int i=0;i<img.size();i++){
				if(img.get(i).attr("title")!=null&&!img.get(i).attr("title").equals("")){
					String urls=img.get(i).attr("src");
					if(urls.contains("ITEM")){
						String aurl=img.get(i).parents().select("a").attr("abs:href");
						getItem(aurl,dao);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UseItem getItem(String url,SRPUtil dao){
		Document doc;
		UseItem useitem = null;
		try {
			doc = Jsoup.connect(url).timeout(30*1000).get();
			Elements table=doc.select("div.INFO_BG_C0");
			Elements item=table.select("li.item0");
			Elements name=item.select("img");
			Elements brs=item.select("br");
			String temp="";
			for(int i=0;i<brs.size();i++){
				temp+=brs.get(i).nextSibling().outerHtml().trim();
			}
			if(!temp.contains("<a")&&!temp.contains("級別")&&!temp.contains("船体")){
				List<String> type_temp=new ArrayList<>();
				if(temp.contains("行動力")){
					type_temp.add("食物");
				}
				if(temp.contains("消除疲勞")){
					type_temp.add("消除疲劳");
				}
				if(temp.contains("壞血病")){
					type_temp.add("坏血病");
				}
				if(temp.contains("實驗")){
					type_temp.add("炼金");type_temp.add("实验");
				}
				if(temp.contains("親愛之證")){
					type_temp.add("兑换");type_temp.add("酒馆礼物");
				}
				if(temp.contains("備忘錄")){
					type_temp.add("翻译");
				}
				if(temp.contains("裝材")){
					type_temp.add("装材");
				}
				if(temp.contains("營養套餐")){
					type_temp.add("副料");
				}
				if(temp.contains("探險獎賞")){
					type_temp.add("兑换");type_temp.add("地下城");
				}
				if(temp.contains("海戰")){
					type_temp.add("海战");
				}
				if(temp.contains("肉搏戰")){
					type_temp.add("肉搏战");
				}
				temp=temp.replace("・","·");
				useitem=new UseItem();
				useitem.name=StringUtil.TransToSimple(name.attr("title"));
				useitem.info=StringUtil.TransToSimple(temp.replace("&middot;","·").replace("‧", "·"));
				String stemp="";
				for(int k=0;k<type_temp.size();k++){
					stemp+=type_temp.get(k)+",";
				}
				if(type_temp.size()==0){
					stemp="其他,";
				}
				useitem.type=StringUtil.TransToSimple(stemp.substring(0,stemp.length()-1));
				useitem.get_way="";
				System.out.println(url);
				System.out.println(useitem.name);
				System.out.println(useitem.info);
				System.out.println(useitem.type);
				onlyOneInsertItem(useitem, dao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return useitem;
	}

	/**连接url**/
	public List<String> getUrl(String url){
		List<String> list=new ArrayList<>();
		Document doc ;
		try {
			doc = Jsoup.connect(url).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli0");
			for(int i=1;i<ul.size();i++){
				Elements li=ul.get(i).select("li.item0");
				Elements link =li.select("a");
				for(int j=0;j<link.size();j++)
				{
					String abs_url=link.attr("abs:href");
					if(!abs_url.equals("http://uwodbmirror.ivyro.net/cn/main.php?id="))
					{
						list.add(abs_url);
						Log.i("abs_url",abs_url);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**连接url**/
	public List<String> getUrlBoat(String url){
		List<String> list=new ArrayList<>();
		Document doc;
		try {
		doc = Jsoup.connect(url).timeout(30*1000).get();
		Elements table=doc.select("div.table0");
		Elements ul=table.select("ul.unli0");
		for(int i=1;i<ul.size();i++){
			Elements li=ul.get(i).select("li.item3");
			Elements link =li.select("a");
			for(int j=0;j<link.size();j++)
			{
				String abs_url=link.attr("abs:href");
				if(!abs_url.equals(DB_BASE_URL+"id="))
				{
					list.add(abs_url);
					Log.i("abs_url",abs_url);
				}
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**炮要特殊处理**/
	public List<String> getCannonUrl(){
		number=new ArrayList<>();
		List<String> list=new ArrayList<>();
		for(int k=6;k<=11;k++){
			Document doc;
			try {
			doc = Jsoup.connect(DB_BASE_URL+PART+"&chp="+k).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli0");
			for(int i=1;i<ul.size();i++){
				Elements li=ul.get(i).select("li.item0");
				Elements link =li.select("a");
				for(int j=0;j<link.size();j++){
					String abs_url=link.attr("abs:href");
					list.add(abs_url);
					number.add(k);
					Log.i("abs_url",abs_url);
				}
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}	
	public void getBoat(DefaultDAO dao){
		int x=0;
		//获得小型船链接
		List<String> list1=getUrl(DB_BASE_URL+BOAT+"&chp=4");
		//获得中型船链接
		List<String> list2=getUrl(DB_BASE_URL+BOAT+"&chp=5");
		//获得大型船链接
		List<String> list3=getUrl(DB_BASE_URL+BOAT+"&chp=6");
		//第一步:小型船获取
		for(int i=0;i<list1.size();i++){
			Document doc;
			try {
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			Elements tables=doc.select("div.table0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//强化次数
			Log.i("ss", ""+tables.last().select("li.item11:contains(No.)"));
			String plus=tables.last().select("li.item11:contains(No.)").text();
			int point=getPlusPoint(plus);
			Log.i("",point+"");
//---------------------------------------------------------------------------------
			//名称
			Elements img =li0.select("img");
			//技能
			Elements a=li0.select("a");
			String ab="";
			//去除专用舰的那个
			for(int a_i=0;a_i<a.size()-1;a_i++){
				Elements img2=a.get(a_i).select("img");
				if(a_i!=a.size()-2)
					ab+=img2.attr("title")+",";
				else
					ab+=img2.attr("title");
			}
			//纵帆
			Elements state0=li1.select("div.state0");	
			//横帆
			Elements state1=li1.select("div.state1");	
			//划船
			Elements state2=li1.select("div.state2");	
			//转向
			Elements state3=li1.select("div.state3");	
			//抗波
			Elements state4=li1.select("div.state4");	
			//装甲
			Elements state5=li1.select("div.state5");	
			//耐久
			Elements state6=li1.select("div.state6");
			//人数
			Elements state7=li1.select("div.state7a");
			//炮门
			Elements state8=li1.select("div.state8");	
			//仓位
			Elements state9=li1.select("div.state9");
			//需求等级
			Elements level1=li1.select("div.level1");	
			Elements level2=li1.select("div.level2");	
			Elements level3=li1.select("div.level3");
			//part
			Elements br=li1.select("br");
			SailBoat sailboat=new SailBoat();
			//some tmp
			String people[]=state7.text().trim().split("/");
			int must=Integer.parseInt(people[0]);
			int max=Integer.parseInt(people[1]);
			int level_m=0,level_s=0,level_j=0,paddle=0;
			try{
				level_m=Integer.parseInt(level1.text().trim());
			}catch(Exception e){
				level_m=0;
			}
			try{
				level_s=Integer.parseInt(level2.text().trim());
			}catch(Exception e){
				level_s=0;
			}
			try{
				level_j=Integer.parseInt(level3.text().trim());
			}catch(Exception e){
				level_j=0;
			}
			try{
				paddle=Integer.parseInt(state2.text().trim());
			}catch(Exception e){
				paddle=0;
			}
			sailboat.setAbility(ab);
			sailboat.setArmor(Integer.parseInt(state5.text().trim()));
			sailboat.setCrenelle(Integer.parseInt(state8.text().trim()));
			sailboat.setDef_wave(Integer.parseInt(state4.text().trim()));
			sailboat.setFore_sail(Integer.parseInt(state0.text().trim()));
			sailboat.setGet_way("");
			sailboat.setRecipe_way("");
			sailboat.setHealth_boat(Integer.parseInt(state6.text().trim()));
			sailboat.setLevel_j(level_j);
			sailboat.setLevel_s(level_s);
			sailboat.setLevel_m(level_m);
			sailboat.setName(img.attr("title"));
			sailboat.setPaddle(paddle);
			sailboat.setPlus_point(point);
			sailboat.setPic_id("dol_"+"b"+i+"s"+x);
			sailboat.setPeople_must(must);
			sailboat.setPeople_number(max);
			sailboat.setShipping_space(Integer.parseInt(state9.text().trim()));
			sailboat.setNumber_part(getPart(br));
			sailboat.setSize(x);
			sailboat.setSquare_sail(Integer.parseInt(state1.text().trim()));
			sailboat.setTurn(Integer.parseInt(state3.text().trim()));
			sailboat.setWay_id(NumberUtil.getMaxForThree(level_m, level_s, level_j));
			sailboat.setType(NumberUtil.isZero(paddle));
			//避免重复
			onlyOneInsertBoat(sailboat,dao);
			//如果第一遍执行完了开始执行第二次
			if(i==list1.size()-1&&x==0){
				x=1;
				list1=list2;
				i=-1;
			}
			//如果第二遍执行完了开始执行第三次
			if(i==list1.size()-1&&x==1)
			{
				x=2;
				list1=list3;
				i=-1;
			}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**获得所有帆**/
	public void getSail(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+PART+"&chp=3");
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//纵帆
			Elements state0=li1.select("div.state0");	
			//横帆
			Elements state1=li1.select("div.state1");	
			//转向
			Elements state3=li1.select("div.state3");
			//耐久
			Elements state10=li1.select("div.state10");
			//TEMP
			String uid="dol_"+"eb"+i+"s";
			//part部分
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(0);
			part.setZtype(0);
			part.setAdd(
			"纵帆:"+state0.text().trim()+","+"横帆:"+state1.text().trim()+","+
			"转向:"+state3.text().trim()+","+"耐久:"+state10.text().trim()
			);
			//避免重复
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**装备**/
	public void getEquip(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+PART+"&chp=1");
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//耐久
			Elements state10=li1.select("div.state10");
			//效果
			Elements tab=li1.select("div.INFO_TAB");
			//TEMP
			String uid="dol_"+"eb"+i+"e";
			String ab=tab.first().nextSibling().toString();
			//part部分
			part.setPic_id(uid);
			Log.i("x", ab);
			part.setName(img.attr("title"));
			part.setType(1);
			part.setZtype(0);
			part.setAdd(
					"效果:"+ab+","+"耐久:"+state10.text().trim()
			);
			//避免重复
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**炮**/
	public void getCannon(DefaultDAO dao){
		List<String> list1=getCannonUrl();
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//耐久
			Elements state10=li1.select("div.state10");
			//贯穿力
			Elements state17=li1.select("div.state17");
			//射程
			Elements state18=li1.select("div.state18");
			//弹速
			Elements state19=li1.select("div.state19");
			//爆炸范围
			Elements state20=li1.select("div.state20");
			//装填速度
			Elements state21=li1.select("div.state21");
			//炮种类
			String type=getCannonType(number.get(i));
			//TEMP
			String uid="dol_"+"eb"+i+"c";
			//part部分
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(2);
			part.setZtype(0);
			part.setAdd(
					"炮弹种类:" + type + "," + "贯穿力:" + state17.text().trim() + "," +
							"射程:" + state18.text().trim() + "," + "弹速:" + state19.text().trim() + "," +
							"爆炸范围:" + state20.text().trim() + "," + "装填速度:" + state21.text().trim() + "," +
							"耐久:" + state10.text().trim()
			);
			Log.i("x", "id=" + i + " " + part.getAdd());
			//避免重复
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**板子**/
	public void getDef(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL + PART + "&chp=" + 2);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//耐久
			Elements state10=li1.select("div.state10");
			//装甲
			Elements state11=li1.select("div.state11");
			//航行速度
			Elements state12=li1.select("div.state12");
			//TEMP
			String uid="dol_"+"eb"+i+"d";
			//part部分
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(3);
			part.setZtype(0);
			part.setAdd(
					"装甲:"+state11.text().trim()+","+"航行速度:"+state12.text().trim()+","+
					"耐久:"+state10.text().trim()					
			);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**首**/
	public void getTop(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+PART+"&chp="+4);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//耐久
			Elements state10=li1.select("div.state10");
			//灾害守护
			Elements state13=li1.select("div.state13");
			//减轻疲劳
			Elements state14=li1.select("div.state14");
			//掌握船员
			Elements state15=li1.select("div.state15");
			//回避炮弹
			Elements state16=li1.select("div.state16");
			//效果
			Elements tab=li1.select("img");
			//TEMP
			String add="";
			String uid="dol_"+"eb"+i+"t";
			if(tab.attr("title").equals("")||tab.attr("title")==null){
				add="";		
			}else{
				add=","+"使用效果:"+tab.attr("title");
			}
			//part部分
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(4);
			part.setZtype(0);
			part.setAdd(
					"灾害守护:"+state13.text().trim()+","+"减轻疲劳:"+state14.text().trim()+","+
					"掌握船员:"+state15.text().trim()+","+"回避炮弹:"+state16.text().trim()+","+
					"耐久:"+state10.text().trim()+add
			);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void getBody(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+Z_PART+"&chp="+12);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();	
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//TEMP
			String uid="dol_"+"zb"+i+"b";
			//耐久
			Elements hp_add=li1.select("div.state6");
			//划船
			Elements paddle_add=li1.select("div.state2");
			String str="";
			if(hp_add.size()==2){
				str+="耐久:"+hp_add.get(0).text().trim()+"~"+hp_add.get(1).text().trim();
			}
			if(paddle_add.size()==2){
				str+=",桨力:"+paddle_add.get(0).text().trim()+"~"+paddle_add.get(1).text().trim();
			}
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(5);
			part.setZtype(1);
			part.setAdd(
					str
			);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void getMainSail(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+Z_PART+"&chp="+10);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//TEMP
			String uid="dol_"+"zb"+i+"s";
			//纵帆性能
			Elements sail=li1.select("div.state0");
			//横帆性能
			Elements fore=li1.select("div.state1");
			//转向性能
			Elements turn=li1.select("div.state3");
			//抗波
			Elements def_wave=li1.select("div.state4");
			String str="";
			//纵帆的情况下
			if(sail.size()==2){
				str+="纵帆性能:"+sail.get(0).text().trim()+"~"+sail.get(1).text().trim();
				if(fore.size()==2){
					str+=",横帆性能:"+fore.get(0).text().trim()+"~"+fore.get(1).text().trim();
				}
				if(turn.size()==2){
					str+=",转向:"+turn.get(0).text().trim()+"~"+turn.get(1).text().trim();
				}
				if(def_wave.size()==2){
					str+=",抗波:"+turn.get(0).text().trim()+"~"+turn.get(1).text().trim();
				}
			}else{
				str+="横帆性能:"+fore.get(0).text().trim()+"~"+fore.get(1).text().trim();
				if(turn.size()==2){
					str+=",转向:"+turn.get(0).text().trim()+"~"+turn.get(1).text().trim();
				}
				if(def_wave.size()==2){
					str+=",抗波:"+def_wave.get(0).text().trim()+"~"+def_wave.get(1).text().trim();
				}
			}
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(6);
			part.setZtype(1);
			part.setAdd(str);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void getMainCannon(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+Z_PART+"&chp="+11);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//TEMP
			String uid="dol_"+"zb"+i+"c";
			//炮门
			Elements cannon=li1.select("div.state8");
			String str="";
			//纵帆的情况下
			if(cannon.size()==2){
				str+="炮门:"+cannon.get(0).text().trim()+"~"+cannon.get(1).text().trim();
			}
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(7);
			part.setZtype(1);
			part.setAdd(str);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void getEQ(DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+Z_PART+"&chp="+13);
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			Part part=new Part();
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			//TEMP
			String uid="dol_"+"zb"+i+"e";
			//耐久
			Elements hp_add=li1.select("div.state6");
			//划船
			Elements paddle_add=li1.select("div.state2");
			//纵帆性能
			Elements sail=li1.select("div.state0");
			//横帆性能
			Elements fore=li1.select("div.state1");
			//转向性能
			Elements turn=li1.select("div.state3");
			//抗波
			Elements def_wave=li1.select("div.state4");
			//装甲
			Elements armor=li1.select("div.state5");
			//炮门
			Elements cannon=li1.select("div.state8");
			//炮门
			Elements space=li1.select("div.state9");
			String str="";
			int x=0;
			//如果有耐久
			if(hp_add.size()==2){
				str+="耐久:"+hp_add.get(0).text().trim()+"~"+hp_add.get(1).text().trim();
				x=0;
			}else if(sail.size()==2){
				str+="纵帆性能:"+sail.get(0).text().trim()+"~"+sail.get(1).text().trim();
				x=1;
			}else if(fore.size()==2){
				str+="横帆性能:"+fore.get(0).text().trim()+"~"+fore.get(1).text().trim();
				x=2;
			}else if(paddle_add.size()==2){
				str+="桨力:"+paddle_add.get(0).text().trim()+"~"+paddle_add.get(1).text().trim();
				x=3;
			}else if(turn.size()==2){
				str+="转向:"+turn.get(0).text().trim()+"~"+turn.get(1).text().trim();
				x=4;
			}else if(def_wave.size()==2){
				str+="抗波:"+def_wave.get(0).text().trim()+"~"+def_wave.get(1).text().trim();
				x=5;
			}else if(armor.size()==2){
				str+="装甲:"+armor.get(0).text().trim()+"~"+armor.get(1).text().trim();
				x=6;
			}else if(cannon.size()==2){
				str+="炮门:"+cannon.get(0).text().trim()+"~"+cannon.get(1).text().trim();
				x=7;
			}else if(space.size()==2){
				str+="仓位:"+space.get(0).text().trim()+"~"+space.get(1).text().trim();
				x=8;
			}
			str+=setEq(hp_add,sail,fore,paddle_add,turn,def_wave,armor,cannon,space,x);
			part.setPic_id(uid);
			part.setName(img.attr("title"));
			part.setType(8);
			part.setZtype(1);
			part.setAdd(str);
			Log.i("x","id="+i+" "+part.getAdd());
			onlyOneInsertPart(part,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**交易品**/
	public void getTradeItem(DefaultDAO dao,int type,int number){
		List<String> list1=getUrl(DB_BASE_URL+TRADE+"&chp="+type);
		for(int i=0;i<list1.size();i++){
			TradeItem trade=new TradeItem();
			Document doc = null;
			try {
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			String name=img.attr("title");
			String part1=li0.select("br").get(0).nextSibling().outerHtml();
			String part2="";
			if(li0.select("br").size()>1){
				part2=li0.select("br").get(1).nextSibling().outerHtml();
			}
			String detail=part1.replaceAll(" ","")+part2;
			String uid="dol_"+"t"+i+"s"+type;
			String types=li1.select("img").get(0).attr("title");
			String sp="";
			Log.i("name",name);
			Log.i("detail",detail);
			Log.i("type",types);
			if(li1.select("img").size()>1){
				sp=li1.select("img").get(1).attr("title");
				Log.i("sp",sp);			
			}
			//生产，购买
			Elements doc_ul=doc.select("ul.unli0");
			String area="";
			if(doc_ul.size()>1){
				for(int j=1;j<doc_ul.size();j++){
					String item=doc_ul.get(j).select("li.item0").select("img").attr("title");
					if(j==doc_ul.size()-1){
						if(!item.equals(""))
							area+=item;
					}else{
						if(!item.equals(""))
							area+=item+",";
					}	
				}
				if(area.endsWith(",")){
					area=area.substring(0,area.length()-1);
				}
				Log.i("area",area+"");	
			}
			String recipe="";
			for(int d=1;d<table.size();d++){
				recipe+=table.get(d).select("div").get(3).text().replaceAll(" ","")+" ";
			}
			recipe=recipe.replaceAll("\\d","").trim().replace(" ",",");
			Log.i("item3","---"+recipe+"---");
			//添加数据
			trade.setName(StringUtil.TransToSimple(name));//名称
			trade.setDetail(StringUtil.TransToSimple(detail));//描述
			trade.setTrade(number);//商品类型
			trade.setType(StringUtil.TransToSimple(types));//类型
			trade.setPic_id(uid);//图片名
			trade.setProducing_area(area);//生产地
			trade.setSp(StringUtil.TransToSimple(sp));//特产文化圈
			trade.setRecipe_way(StringUtil.TransToSimple(recipe));//生产方式
			onlyOneInsertTrade(trade, dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public List<String> getRecipe(DefaultDAO dao,int type){
		List<String> list=new ArrayList<>();
		Document doc ;
		try {
			doc = Jsoup.connect(DB_BASE_URL+RECIPE+"&chp="+type).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli0");
			for(int i=1;i<ul.size();i++){
				Recipe re=new Recipe();
				Elements item0=ul.get(i).select("li.item0");
				String parent=item0.select("img").attr("title");
				Log.i("", parent);
				if(!parent.equals("")){
						temp=parent;
				}
				Elements item2=ul.get(i).select("li.item2");
				Elements item3=ul.get(i).select("li.item3");
				Elements item5=ul.get(i).select("li.item5");
				Elements a_item2=item2.select("a").select("img");
				Log.i("", a_item2.html());
				String need="";
				for(int j=0;j<a_item2.size();j++){
					if(j!=a_item2.size()-1)
						need+=a_item2.get(j).attr("title")+",";
					else
						need+=a_item2.get(j).attr("title");
				}
				re.setParent_name(temp);
				re.setNeed(need);
				Log.i("item:"+temp,need);
				Elements skill=item3.select("img");
				String number="";
				String[] temp=number.split(" ");
				if(item3.select("br").size()>0){
					number= item3.select("br").get(0).siblingElements().text();
					temp=number.split(" ");
					Log.i("item3",number);
				}
				String skill_txt="";
				for(int p=0;p<skill.size();p++){
					Log.i("k",skill.get(p).attr("title")+temp[p]+"|");
					if(p!=skill.size()-1){
						skill_txt+=skill.get(p).attr("title")+" "+temp[p]+",";
					}else{
						skill_txt+=skill.get(p).attr("title")+" "+temp[p];
					}
				}
				re.setLevel_need(skill_txt);
				Log.i("item3",item3.text().replace(number,""));
				re.setName(item3.text().replace(number,""));
				Log.i("item3", item5.text().replace(" ","\n"));
				re.setResult_number( item5.text().replace(" ","\n"));
				onlyOneInsertRecipe(re,dao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void getNPC(DefaultDAO dao){
		Document doc = null;
		try {
			doc = Jsoup.connect(DB_BASE_URL+NPC).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli1");
			for(int i=1;i<ul.size();i++){
				NPCInfo npc=new NPCInfo();
				String area="";
				String name="";
				String love_type="";
				String skill_tech="";
				Elements li5=ul.get(i).select("li.item5");
				area=li5.select("span").text();
				name=li5.select("center").text();
				Elements li6=ul.get(i).select("li.item6");
				love_type=li6.select("img").attr("title");
				Elements li7=ul.get(i).select("li.item7");
				for(int j=0;j<li7.size();j++){
					if(!li7.get(j).select("img").attr("title").equals("")){
						skill_tech+=li7.get(j).select("img").attr("title")+",";
					}
				}
				Elements li8=ul.get(i).select("li.item8");
				if(!li8.select("img").attr("title").equals("")){
					skill_tech+=li8.select("img").attr("title");
				}
				if(skill_tech.endsWith(",")){
					skill_tech=skill_tech.substring(0,skill_tech.length()-1);
				}
				Log.i("x", area);
				Log.i("x", name);
				Log.i("x", love_type);
				Log.i("x", skill_tech);
				npc.setCity(area);
				npc.setLove_type(love_type);
				npc.setName(name);
				npc.setSkill_tech(skill_tech);
				onlyOneInsertNPC(npc, dao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getCity(int index,DefaultDAO dao){
		List<String> list=getUrl(DB_BASE_URL+CITY+"&chp="+index);
		Gson	 gson=new Gson();
		for(int i=0;i<list.size();i++){
			Document doc = null;
			try {
				City city=new City();
				doc = Jsoup.connect(list.get(i)).timeout(30*1000).get();
				Elements table=doc.select("div.table0");
				//获得上部信息
				Elements ul=table.select("div.INFO_BG_C0");
				Elements ul0=table.select("ul.unli0");
				Elements abox =table.select("a.BOX");
				//左边信息
				Elements li0=ul.select("li.item0");
				//右边信息
				Elements li1=ul.select("li.item1");
				//名称
				Elements img0 =li0.select("img");
				//文化圈
				Elements img1 =li1.select("img");
				String name=img0.attr("title");
				String culture=img1.attr("title");
				String area=abox.get(0).text();
				String lang=abox.get(1).select("img").attr("title");
				String invest="";
				if(abox.size()>2){
					String p=abox.get(abox.size()-1).nextSibling().outerHtml()+"";
					if(!p.contains("b")){
						invest=abox.get(abox.size()-1).select("img").attr("title")+p;
					}
				}
				if(abox.size()>2){
					if(!invest.equals("")){
						for(int k=2;k<abox.size()-1;k++){
							lang+=","+abox.get(k).select("img").attr("title");
						}
					}else{
						for(int k=2;k<abox.size();k++){
							lang+=","+abox.get(k).select("img").attr("title");
						}
					}
				}
				List<TradeCityItem> trade_list=new ArrayList<>();
				for(int j=1;j<ul0.size();j++){
					TradeCityItem item=new TradeCityItem();
					Elements item2=ul0.get(j).select("li.item2");
					Elements item4=ul0.get(j).select("li.item4");
					
					String name_item=item2.select("img").attr("title");
					String money=item4.text();
					Log.i("city", money);
					
					String[] temp=money.split(" ");
					String gold_item=temp[0];
					String invest_item="";
					if(temp.length==5){
						invest_item=temp[4];
					}
					if(!StringUtil.isNull(name_item)&&!StringUtil.isNull(gold_item)){
						item.invest=invest_item;
						item.name=name_item;
						item.price=gold_item;
						trade_list.add(item);
					}
				}
				String trade=gson.toJson(trade_list);
				Log.i("city", name);
				Log.i("city", culture);
				Log.i("city", area);
				Log.i("city", lang);
				Log.i("city", invest);
				Log.i("city", trade);
				
				city.setArea(StringUtil.TransToSimple(area));
				city.setCulture(StringUtil.TransToSimple(culture));
				city.setInvest(StringUtil.TransToSimple(invest));
				city.setLang(StringUtil.TransToSimple(lang));
				city.setName(StringUtil.TransToSimple(name));
				city.setTrade_list(StringUtil.TransToSimple(trade));
				if(index==1){
					updateCity(i+1,city,dao);
				}
				if(index==2){
					updateCity(i+12,city,dao);
				}
				if(index==3){
					updateCity(i+27,city,dao);
				}
				if(index==4){
					updateCity(i+49,city,dao);
				}
				if(index==5){
					updateCity(i+74,city,dao);
				}
				if(index==6){
					updateCity(i+81,city,dao);
				}
				if(index==7){
					updateCity(i+96,city,dao);
				}
				if(index==8){
					updateCity(i+104,city,dao);
				}
				if(index==9){
					updateCity(i+108,city,dao);
				}
				if(index==10){
					updateCity(i+112,city,dao);
				}
				if(index==11){
					updateCity(i+127,city,dao);
				}
				if(index==12){
					updateCity(i+135,city,dao);
				}
				if(index==13){
					updateCity(i+156,city,dao);
				}
				if(index==14){
					updateCity(i+164,city,dao);
				}
				if(index==15){
					updateCity(i+175,city,dao);
				}
//				onlyOneInsertCity(city,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void getSkill(int type,DefaultDAO dao){
		List<String> list1;
		if(type!=6){
			list1=getUrl(DB_BASE_URL+SKILL+"&chp="+type);
		}else{
			list1=getUrlBoat(DB_BASE_URL+SKILL+"&chp="+type);
		}
		for(int i=0;i<list1.size();i++){
			Skill skill=new Skill();
			Document doc = null;
			try {
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//名称
			Elements img =li0.select("img");
			String uid="dol_"+"sk"+i+"s"+type;
			String name=img.attr("title");
			String detail=li0.text();
			Elements li1=ul.select("li.item1");
			Elements lv1=li1.select("div.level1");
			Elements lv2=li1.select("div.level2");
			Elements lv3=li1.select("div.level3");
			Elements lv4=li1.select("div.level4");
			Elements job=ul.select("img");
			detail=detail.replaceAll(" ","").replace(name,"");
			if(type==5){
				String jobs=job.get(1).attr("title");
				skill.setDetail(StringUtil.TransToSimple(detail));
				skill.setDo_point("副");
				skill.setName(StringUtil.TransToSimple(name));
				skill.setNeed(StringUtil.TransToSimple(jobs));
				skill.setPic_id(uid);
				skill.setType(type);
			}else if(type==6){
				Log.i("s",""+list1.get(i));
				String disgess=li1.select("div").get(0).nextSibling().outerHtml();
				skill.setDetail(StringUtil.TransToSimple(detail));
				skill.setDo_point(StringUtil.TransToSimple(disgess.trim()));
				skill.setName(StringUtil.TransToSimple(name));
				String skill_need="技能需求:";
				Elements a=li1.select("a");
				for(int s=0;s<a.size();s++){
					skill_need+=a.get(s).select("img").attr("title")+a.get(s).nextSibling().outerHtml().replace("级别","");
				}
				skill.setNeed(StringUtil.TransToSimple(skill_need));
				skill.setPic_id(uid);
				skill.setType(type);
			}
			else{
				String disgess=li1.select("div").get(0).nextSibling().outerHtml();
				String lev1=lv1.text();
				String lev2=lv2.text();
				String lev3=lv3.text();
				String lev4=lv4.text();
				String lv_need="等级需求:"+lev1+"/"+lev2+"/"+lev3;
				String pay="学习费用:"+lev4;
				String skill_need="技能需求:";
				Elements a=li1.select("a");
				for(int s=0;s<a.size();s++){
					skill_need+=a.get(s).select("img").attr("title")+a.get(s).nextSibling().outerHtml().replace("级别","");
				}
				String need=lv_need+"|"+pay+"|"+skill_need;
				skill.setDetail(StringUtil.TransToSimple(detail));
				skill.setDo_point(StringUtil.TransToSimple(disgess.trim()));
				skill.setName(StringUtil.TransToSimple(name));
				skill.setNeed(StringUtil.TransToSimple(need));
				skill.setPic_id(uid);
				skill.setType(type);
			}
			Log.i("2",name);
			Log.i("3",detail);
			onlyOneInsertSkill(skill,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getJob(int type,DefaultDAO dao){
		List<String> list1=getUrl(DB_BASE_URL+JOB+"&chp="+type);
		Gson	 gson=new Gson();
		for(int i=0;i<list1.size();i++){
			Document doc = null;
			try {
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item2");
			Elements all0=doc.select("li.item0");
			//名称
			Elements img =li0.select("img");
			String name=img.attr("title");
			String detail=li0.text();
			Elements li1=ul.select("li.item2");
			
			Elements lv1=li1.select("div.level1");
			Elements lv2=li1.select("div.level2");
			Elements lv3=li1.select("div.level3");
			Elements lv4=li1.select("div.level4");
			Elements lv0=li1.select("div.level0");
			String lev1=lv1.text();
			String lev2=lv2.text();
			String lev3=lv3.text();
			String lev4=lv4.text();
			String lev0=lv0.text();
			String lv_need="等级需求:"+lev1+"/"+lev2+"/"+lev3+lev0.replace("Total ","合计:");
			String pay="学习费用:"+lev4;
			String need=lv_need+"|"+pay+"|";
			detail=detail.replaceAll(" ","").replace(name,"").split("轉職")[0];
			String sp="";
			List<String> list=new ArrayList<String>();
			for(int j=0;j<all0.size()-1;j++){
				Elements item0=all0.get(j).select("li.item0");
				if(j==0){
					if(item0.select("img").get(0).attr("title").contains("專")){
						sp=item0.select("img").get(1).attr("title");
					}
				}
				if(sp.equals("")){
					list.add(item0.select("img").get(1).attr("title"));
				}else{
					if(j!=0){
						list.add(item0.select("img").get(1).attr("title"));
					}
				}
			}
			String metal =all0.get(all0.size()-1).select("img").get(0).attr("title");
			String good_list=gson.toJson(list);
			Job job=new Job();
			job.setDetail(StringUtil.TransToSimple(detail));
			job.setGood_list(StringUtil.TransToSimple(good_list));
			job.setName(StringUtil.TransToSimple(name));
			job.setMetal(StringUtil.TransToSimple(metal));
			job.setSp(StringUtil.TransToSimple(sp));
			job.setChang_if(StringUtil.TransToSimple(need));
			job.setType(type);
			Log.i("1",name);
			Log.i("2",detail);
			Log.i("3",need);
			Log.i("4",metal);
			Log.i("5",sp);
			Log.i("6",good_list);
			onlyOneInsertJob(job,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getADC(int type,DefaultDAO dao){
		Gson	 gson=new Gson();
		List<String> list1=getUrl(DB_BASE_URL+ADC+"&chp="+type);
		for(int i=0;i<list1.size();i++){
			ADCInfo adc=new ADCInfo();
			Document doc = null;
			try {
			//遍历链接
			doc = Jsoup.connect(list1.get(i)).timeout(30*1000).get();
			//获得总体
			Elements table=doc.select("div.INFO_BG_C0");
			//获得上部信息
			Elements ul=table.select("ul.unli0");
			Elements ulx=doc.select("div.table0").select("ul.unli0");
			//左边信息
			Elements li0=ul.select("li.item0");
			//右边信息
			Elements li1=ul.select("li.item1");
			//名称
			Elements img =li0.select("img");
			String name=img.attr("title");
			
			Elements span=li1.select("span");
			String sex=span.get(0).text();
			String city=span.get(1).text();
			List<ADCSkill> skill_list=new ArrayList<>();
			Log.i("",""+ulx.size());
			for(int j=1;j<ulx.size();j++){
				ADCSkill adc_skill=new ADCSkill();
				Elements item0=ulx.get(j).select("li.item0");
				adc_skill.setName(	item0.select("a").text());
				Log.i("",""+adc_skill.getName());
				Elements item1=ulx.get(j).select("li.item1");
				
				adc_skill.setNeed(StringUtil.TransToSimple(item1.text()));
				skill_list.add(adc_skill);
			}
			String skill_list_txt= gson.toJson(skill_list);
			String uid="dol_"+"adc"+i+"s"+type;
			adc.setCity(city);
			adc.setName(name);
			adc.setSex(sex);
			adc.setSkill_list(skill_list_txt);
			adc.setType(type);
			adc.setHead_img(uid);
			Log.i("1", city);
			Log.i("1", name);
			Log.i("1", sex);
			Log.i("1", skill_list_txt);
			Log.i("1", type+"");
			onlyOneInsertADC(adc,dao);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void onlyOneInsertADC(ADCInfo item,DefaultDAO dao){
		   String[] x={item.getName()+""};
		   List<ADCInfo>list=(List<ADCInfo>) dao.select(ADCInfo.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(item);
	}
	
	@SuppressWarnings("unchecked")
	private void onlyOneInsertJob(Job item,DefaultDAO dao){
		   String[] x={item.getName()+""};
		   List<Job>list=(List<Job>) dao.select(Job.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(item);
	}
	
	@SuppressWarnings("unchecked")
	private void onlyOneInsertSkill(Skill item,DefaultDAO dao){
		   String[] x={item.getName()+""};
		   List<Skill>list=(List<Skill>) dao.select(Skill.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(item);
	}
	
	@SuppressWarnings("unchecked")
	private void onlyOneInsertTrade(TradeItem item,DefaultDAO dao){
		   String[] x={item.getName()+""};
		   List<TradeItem>list=(List<TradeItem>) dao.select(TradeItem.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(item);
	        else{
	        	Log.i("s", item.getName());
	        	dao.update(item,new String[]{"type","recipe_way","sp"} ,"name=?",x);
	        }
	}
	@SuppressWarnings("unchecked")
	private void onlyOneInsertCity(City city,DefaultDAO dao){
		   String[] x={city.getName()+""};
		   List<City>list=(List<City>) dao.select(City.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(city);
	        else 
	        	dao.update(list.get(0),new String[]{"trade_list"} ,"name=?",x);
	}
	private void updateCity(int i,City city,DefaultDAO dao){
			   String[] x={i+""};
			   dao.update(city,new String[]{"trade_list"} ,"id=?",x);
	}
	@SuppressWarnings("unchecked")
	private void onlyOneInsertNPC(NPCInfo npc,DefaultDAO dao){
		   String[] x={npc.getName()+"",npc.getCity()+""};
		   List<NPCInfo>list=(List<NPCInfo>) dao.select(NPCInfo.class, 
	        		false, "name=? and city=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(npc);
	}
	@SuppressWarnings("unchecked")
	private void onlyOneInsertBoat(SailBoat boat,DefaultDAO dao){
		   String[] x={boat.getName()+""};
		   List<SailBoat>list=(List<SailBoat>) dao.select(SailBoat.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(boat);
	}
	@SuppressWarnings("unchecked")
	private void onlyOneInsertPart(Part part,DefaultDAO dao){
		   String[] x={part.getName()+""};
		   List<Part>list=(List<Part>) dao.select(Part.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(part);
	}

	@SuppressWarnings("unchecked")
	private void onlyOneInsertItem(UseItem part,SRPUtil dao){
		String[] x={part.name+""};
		Log.i("s",""+x[0]);
		List<UseItem>list=dao.select(UseItem.class,
				false, "name=?", x,
				null, null, null, null);
		if(list.size()==0){
			dao.insert(part);
		}
	}

	@SuppressWarnings("unchecked")
	private void onlyOneInsertRecipe(Recipe part,DefaultDAO dao){
		   String[] x={part.getName()+""};
		   List<Recipe>list=(List<Recipe>) dao.select(Recipe.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0&&!StringUtil.isNull(part.getName()))
	        	dao.insert(part);
	}
	private String getCannonType(int item){
		String x="";
		if(item==6)
			x="一般炮弹";
		else if(item==7)
			x="二连发炮弹";
		else if(item==8)
			x="葡萄炮弹";
		else if(item==9)
			x="锁链炮弹";
		else if(item==10)
			x="火焰弹";
		else if(item==11)
			x="烟幕弹";
		return x;
	}
	private String getPart(Elements br){
		//辅助帆
		char part1=br.get(0).nextSibling().outerHtml().replaceAll("\\D","").charAt(0);
		//船身炮
		char part4=br.get(0).nextSibling().outerHtml().replaceAll("\\D","").charAt(1);
		//特殊装备
		char part5=br.get(1).nextSibling().outerHtml().replaceAll("\\D","").charAt(0);
		//船首炮
		char part2=br.get(1).nextSibling().outerHtml().replaceAll("\\D","").charAt(1);
		//追加装甲
		char part3=br.get(2).nextSibling().outerHtml().replaceAll("\\D","").charAt(0);
		//船尾炮
		char part6=br.get(2).nextSibling().outerHtml().replaceAll("\\D","").charAt(1);
		String part=part1+","+part2+","+part3+","+part4+","+part5+","+part6;
		return part;
	}
	private int getPlusPoint(String str){
		//默认七
		int temp=5+2;
		if(str!=null){
			if(!str.equals(""))
			{
				temp=Integer.parseInt(str.replaceAll("\\D",""));
			}
			if(temp>20)
			{
				String s=temp+"";
				int x=Integer.parseInt(s.charAt(0)+"");
				int y=Integer.parseInt(s.charAt(1)+"");
				temp=x+y;
			}
		}
		return temp;
	}
	private String setEq(Elements hp_add,Elements sail,Elements fore,Elements paddle_add,
			Elements turn,Elements def_wave,Elements armor,Elements cannon,Elements space,int x){
		String str="";
		if(hp_add.size()==2&&x!=0){
			str+=",耐久:"+hp_add.get(0).text().trim()+"~"+hp_add.get(1).text().trim();
		}
	    if(sail.size()==2&&x!=1){
	    	str+=",纵帆性能:"+sail.get(0).text().trim()+"~"+sail.get(1).text().trim();
		}
	    if(fore.size()==2&&x!=2){
	    	str+=",横帆性能:"+fore.get(0).text().trim()+"~"+fore.get(1).text().trim();
		}
	    if(paddle_add.size()==2&&x!=3){
	    	str+=",桨力:"+paddle_add.get(0).text().trim()+"~"+paddle_add.get(1).text().trim();
		}
	    if(turn.size()==2&&x!=4){
	    	str+=",转向:"+turn.get(0).text().trim()+"~"+turn.get(1).text().trim();
		}
	    if(def_wave.size()==2&&x!=5){
	    	str+=",抗波:"+def_wave.get(0).text().trim()+"~"+def_wave.get(1).text().trim();
		}
	    if(armor.size()==2&&x!=6){
	    	str+=",装甲:"+armor.get(0).text().trim()+"~"+armor.get(1).text().trim();
		}
	    if(cannon.size()==2&&x!=7){
	    	str+=",炮门:"+cannon.get(0).text().trim()+"~"+cannon.get(1).text().trim();
		}
	    if(space.size()==2&&x!=8){
	    	str+=",仓位:"+space.get(0).text().trim()+"~"+space.get(1).text().trim();
		}
		return str;
	}
	
	public void insertTest(DefaultDAO dao){
		City city=new City();
		city.setArea(StringUtil.TransToSimple("北美西岸"));
		city.setCulture(StringUtil.TransToSimple(""));
		city.setInvest(StringUtil.TransToSimple("抽奖券（No.5） 500,000"));
		city.setLang(StringUtil.TransToSimple("北美诸语"));
		city.setName(StringUtil.TransToSimple("旧金山"));
		TradeCityItem item=new TradeCityItem();
		city.setTrade_list(StringUtil.TransToSimple(""));
		onlyOneInsertCity(city, dao);
	}

	public void getship(DefaultDAO dao){
		try {
			Document doc = Jsoup.connect(DB_BASE_URL+REGULAR_SHIP).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli0");
			Log.i("1", "" + ul.size());
			for(int i=1;i<ul.size();i++){
				RegularShip regularShip=new RegularShip();
				Elements li0=ul.get(i).select("li.item0");
				Elements li1_1=ul.get(i).select("li.item1");
				Elements li1_2=ul.get(i).select("li.item1");
				Elements li2_1=ul.get(i).select("li.item2");
				Elements li2_2=ul.get(i).select("li.item2");
				String abs_url=li0.select("a").attr("abs:href");
				List<String> list=getUrlTimeList(abs_url);
				String name=StringUtil.TransToSimple(li0.text());
				String start_city=StringUtil.TransToSimple(li1_1.get(0).text());
				String end_city=StringUtil.TransToSimple(li1_2.get(1).text());
				String money=StringUtil.TransToSimple(li2_1.get(0).text());
				String time=StringUtil.TransToSimple(li2_2.get(1).text());
				Log.i("1",name);
				Log.i("2",start_city);
				Log.i("3",end_city);
				Log.i("4",money);
				Log.i("5",time);
				regularShip.name=name;
				regularShip.start_city=start_city;
				regularShip.end_city=end_city;
				regularShip.money=money;
				regularShip.time=time;
				regularShip.time_list=new Gson().toJson(list);
				dao.insert(regularShip);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<String> getUrlTimeList(String url){
		List<String> list=new ArrayList<>();
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(30*1000).get();
			Elements table=doc.select("div.table0");
			Elements ul=table.select("ul.unli1");
			Log.i("s",ul.size()+"");
			for(int i=1;i<ul.size();i++){
				Elements li=ul.get(i).select("li.item0");
				for(int j=0;j<li.size();j++){
					String str=li.get(j).text().trim().replace("  ", "");
					if(!str.equals("")){
						list.add(j+"w"+str);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}