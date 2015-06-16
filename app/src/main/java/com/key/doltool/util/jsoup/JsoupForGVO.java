package com.key.doltool.util.jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

import com.key.doltool.data.CardCombo;
import com.key.doltool.data.Mission;
import com.key.doltool.data.Trove;
import com.key.doltool.util.StringUtil;
import com.the9tcat.hadi.DefaultDAO;
public class JsoupForGVO {
	/**GVO源**/
	public static String GVO_BASE_URL="http://gvo.cbo.com.tw/adv_missionDetail.aspx?MID=";
	public static String GVO_CARD="http://gvo.cbo.com.tw/CardCombo.aspx";
	public static String GVO_TW="http://gvo.cbo.com.tw/Adv_Discover.aspx?Type=17";
	public static int index=1;
	/**获得一级url**/
	public void getMission(int id,DefaultDAO dao){
		Document doc = null;
		try {
			doc = Jsoup.connect(GVO_BASE_URL+id).timeout(30*1000).get();
			Elements name=doc.select("span#ctl00_CP1_FormView1_NameLabel");
			Elements type=doc.select("span#ctl00_CP1_FormView1_LKindName");
			Elements level=doc.select("span#ctl00_CP1_FormView1_StarLabel");
			Elements start_city=doc.select("span#ctl00_CP1_FormView1_DataList1");
			
			Elements prop=doc.select("span#ctl00_CP1_FormView1_PropLabel");
			Elements remum=doc.select("span#ctl00_CP1_FormView1_RemuMLabel");

			Elements exp=doc.select("span#ctl00_CP1_FormView1_iExpLabel");
			Elements feat=doc.select("span#ctl00_CP1_FormView1_PopuLabel");
			
			Elements find=doc.select("span#ctl00_CP1_FormView1_FormView發現物_NameLabel");
			Elements flow=doc.select("span#ctl00_CP1_FormView1_FlowLabel");
			Elements get_item=doc.select("span#ctl00_CP1_FormView1_RemuILabel");
			Elements time=doc.select("span#ctl00_CP1_FormView1_Label3");
			
			Elements mission_list=doc.select("div#ctl00_CP1_FormView1_PanelMission");
			Elements before_list = null;
			Elements after_list = null;
			if(mission_list.size()>0){
				before_list=mission_list.get(0).select("table#ctl00_CP1_FormView1_GridView2");
				after_list=mission_list.get(0).select("table#ctl00_CP1_FormView1_GridView1");
			}
			Elements img1=doc.select("img#ctl00_CP1_FormView1_Image1");
			Elements img2=doc.select("img#ctl00_CP1_FormView1_Image2");
			Elements img3=doc.select("img#ctl00_CP1_FormView1_Image3");
			Elements img4=doc.select("img#ctl00_CP1_FormView1_Image4");
			
			Elements span2=doc.select("span#ctl00_CP1_FormView1_Label2");
			Elements span5=doc.select("span#ctl00_CP1_FormView1_Label5");
			Elements span6=doc.select("span#ctl00_CP1_FormView1_Label6");
			Elements span7=doc.select("span#ctl00_CP1_FormView1_Label7");
			
			String before_mission="";
			String after_mission="";
			if(before_list!=null&&before_list.size()>0){
				for(int i=0;i<before_list.get(0).select("a").size();i++){
					String temp=before_list.get(0).select("a").get(i).text();
					if(i!=before_list.get(0).select("a").size()-1&&before_list.get(0).select("a").size()-1!=0){
						before_mission+=temp+",";
					}else if(before_list.get(0).select("a").size()-1!=0){
						before_mission+=temp;
					}else{
						before_mission+=temp;
					}
				}
			}
			if(after_list!=null&&after_list.size()>0){
				for(int i=0;i<after_list.get(0).select("a").size();i++){
					String temp=after_list.get(0).select("a").get(i).text();
					if(i!=after_list.get(0).select("a").size()-1&&after_list.get(0).select("a").size()!=1){
						after_mission+=temp+",";
					}else if(after_list.get(0).select("a").size()==1){
						after_mission+=temp;
					}
					else{
						after_mission+=temp;
					}
				}
			}
			String find_name="";
			if(find.size()>0){
				find_name=find.get(0).text();
			}
			String detail="";
			if(flow.size()>0){
				detail=flow.get(0).text();
			}
			String item="";
			if(get_item.size()>0){
				item=get_item.get(0).text();
			}
			String skill="";
			int step=0;
			if(img1.size()>0){
				skill=img1.attr("alt");
				step=1;
			}
			if(span2.size()>0&&step==1){
				if(!span2.get(0).text().equals("")){
					skill=skill+":"+span2.get(0).text();
				}
				step=2;
			}
			
			if(img2.size()>0&&step==2){
				skill+=","+img2.attr("alt");
				step=3;
			}
			if(span5.size()>0&&step==3){
				if(!span5.get(0).text().equals("")){
					skill=skill+":"+span5.get(0).text();
				}
				step=4;
			}
			
			if(img3.size()>0&&step==4){
				skill+=","+img3.attr("alt");
				step=5;
			}
			if(span6.size()>0&&step==5){
				if(!span6.get(0).text().equals("")){
					skill=skill+":"+span6.get(0).text();
				}
				step=6;
			}

			if(img4.size()>0&&step==6){
				skill+=","+img4.attr("alt");
				step=7;
			}
			if(span7.size()>0&&step==7){
				if(!span7.get(0).text().equals("")){
					skill=skill+":"+span7.get(0).text();
				}
				step=7;
			}
			if(name.size()==0){
				return;
			}
			String name_txt=name.get(0).text();
			String type_txt=type.get(0).text();
			String level_txt=level.get(0).text();
			String start_city_txt="";
			if(start_city.size()!=0){
				start_city_txt=start_city.get(0).text();
			}
			String money=prop.get(0).text()+"/"+remum.get(0).text();
			String exptotal=exp.get(0).text().replace(" 點","")+"/"+feat.get(0).text().replace(" 點","");
			String time_up=time.get(0).text();
			
			String name_t=StringUtil.TransToSimple(name_txt);
			String type_t=StringUtil.TransToSimple(type_txt);
			String skill_t=StringUtil.TransToSimple(skill);
			String time_up_t=StringUtil.TransToSimple(time_up);
			String find_name_t=StringUtil.TransToSimple(find_name);
			String item_t=StringUtil.TransToSimple(item);
			String detail_t=StringUtil.TransToSimple(detail);
			String start_city_txt_t=StringUtil.TransToSimple(start_city_txt);
			String before_mission_t=StringUtil.TransToSimple(before_mission);
			String after_mission_t=StringUtil.TransToSimple(after_mission);
			
			Log.i("name","任务名称:"+name_t);
			Log.i("name","类型:"+type_t);
			Log.i("name","难度:"+level_txt);
			Log.i("name","定金/报酬:"+money);
			Log.i("name","经验/声望:"+exptotal);
			Log.i("name","需要技能:"+skill_t);
			Log.i("name","时间限制:"+time_up_t);
			Log.i("name","发现物:"+find_name_t);
			Log.i("name","获得物品:"+item_t);
			Log.i("name","任务流程:"+detail_t);
			Log.i("name","接受任务地点:"+start_city_txt_t);
			Log.i("name","前置任务:"+before_mission_t);
			Log.i("name","后续任务:"+after_mission_t);
			Log.i("name","其他:"+(GVO_BASE_URL+id));
			
			Mission misson=new Mission();
			misson.setAfter(after_mission_t);
			misson.setBefore(before_mission_t);
			misson.setDaily(detail_t);
			misson.setExp(exptotal);
			misson.setGet_item(item_t);
			misson.setFind_item(find_name_t);
			misson.setLevel(level_txt);
			misson.setMoney(money);
			misson.setName(name_t);
			misson.setSkill_need(skill_t);
			misson.setStart_city(start_city_txt_t);
			misson.setTime_up(time_up_t);
			misson.setType(type_t);
			misson.setOther(GVO_BASE_URL+id);
			onlyOneInsertMission(misson,dao);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private  void onlyOneInsertMission(Mission mission,DefaultDAO dao){
		   if(mission!=null){
			   String[] x={mission.getOther()+""};
			   List<Mission>list=(List<Mission>) dao.select(Mission.class, 
		        		false, "other=?", x, 
						null, null, null, null); 
		        if(list.size()==0)
		        	dao.insert(mission);
		        else
		        	dao.update_by_primary(mission);
		   }
	}
	public void getCardCombo(DefaultDAO dao){
		Document doc = null;
		try {
			doc = Jsoup.connect(GVO_CARD).timeout(30*1000).get();
			Elements name=null;
			for(int id=0;id<=126;id++){
				CardCombo card=new CardCombo();
				String number="";
				if(id<8){
					number="0"+(id+2);
				}else{
					number=(id+2)+"";
				}
				String span="span#ctl00_CP1_GridView1_ctl"+number+"_Label1";
				name=doc.select(span);
				Elements effect=doc.select("td[width=90]");
				Elements card_1_type=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo1_DataList1_ctl00_Label6");
				Elements card_1_name=doc.select("a#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo1_DataList1_ctl00_HName");
				Elements card_1_point=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo1_DataList1_ctl00_LPoint");
			
				Elements card_2_type=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo2_DataList1_ctl00_Label6");
				Elements card_2_name=doc.select("a#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo2_DataList1_ctl00_HName");
				Elements card_2_point=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo2_DataList1_ctl00_LPoint");
		
				Elements card_3_type=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo3_DataList1_ctl00_Label6");
				Elements card_3_name=doc.select("a#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo3_DataList1_ctl00_HName");
				Elements card_3_point=doc.select("span#ctl00_CP1_GridView1_ctl"+number+"_WebUserCardCombo3_DataList1_ctl00_LPoint");
			
				//"color:#C00000";
				card.setName(StringUtil.TransToSimple(name.get(0).text()));
				Log.i("name", "名称:"+name.get(0).text());
				if(effect.size()>0){
					card.setEffect(StringUtil.TransToSimple(effect.get(id).text()));
					Log.i("name", "效果:"+effect.get(id).text());
				}
				if(card_1_type.size()>0){
					String card1=card_1_type.get(0).text()+"-"+card_1_name.get(0).text()+"("+card_1_point.get(0).text()+")";
					card.setCard_1(StringUtil.TransToSimple(card1));
					Log.i("name", "卡片一:"+card_1_type.get(0).text()+"-"+card_1_name.get(0).text()+"("+card_1_point.get(0).text()+")");
				}
				if(card_2_type.size()>0){
					String card1=card_2_type.get(0).text()+"-"+card_2_name.get(0).text()+"("+card_2_point.get(0).text()+")";
					card.setCard_2(StringUtil.TransToSimple(card1));
					Log.i("name", "卡片二:"+card_2_type.get(0).text()+"-"+card_2_name.get(0).text()+"("+card_2_point.get(0).text()+")");	
				}
				if(card_3_type.size()>0){
					String card1=card_3_type.get(0).text()+"-"+card_3_name.get(0).text()+"("+card_3_point.get(0).text()+")";
					card.setCard_3(StringUtil.TransToSimple(card1));
					Log.i("name", "卡片三"+card_3_type.get(0).text()+"-"+card_3_name.get(0).text()+"("+card_3_point.get(0).text()+")");	
				}
				onlyOneInsertCardCombo(card,dao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private  void onlyOneInsertCardCombo(CardCombo card,DefaultDAO dao){
		   if(card!=null){
			   String[] x={card.getName()+""};
			   List<CardCombo>list=(List<CardCombo>) dao.select(CardCombo.class, 
		        		false, "name=?", x, 
						null, null, null, null); 
		        if(list.size()==0)
		        	dao.insert(card);
		        else{
//		        	Log.i("s", ""+dao);
//		        	Log.i("x", ""+card);
//		        	dao.update(card,new String[]{"effect","card_1","card_2","card_3"},"name=?", x);
		        }
		   }
	}
	public void do_TIANWEN(DefaultDAO dao){
		Document doc = null;
		List<String> a_list=new ArrayList<String>();
		try {
			doc = Jsoup.connect(GVO_TW).timeout(30*1000).get();
			Elements table=doc.select("table#ctl00_CP1_GridView發現物");
			Elements a=table.select("tr");
			Log.i("a",a.size()+"");
			for(int i=1;i<a.size();i++){
				Log.i("i",i+"");
				a_list.add(a.get(i).select("a").get(0).attr("href"));
			}
			for(int j=0;j<a_list.size();j++){
				doc = Jsoup.connect("http://gvo.cbo.com.tw/"+a_list.get(j)).timeout(30*1000).get();
				Elements mission=doc.select("span#ctl00_CP1_FormView1_NameLabel");
				Elements name=doc.select("span#ctl00_CP1_FormView1_FormView發現物_NameLabel");
				Elements feat=doc.select("span#ctl00_CP1_FormView1_FormView發現物_ExpLabel");
				Elements card_point=doc.select("span#ctl00_CP1_FormView1_FormView發現物_Label4");
				
				Elements details=doc.select("span#ctl00_CP1_FormView1_FormView發現物_Label18");
				Elements rank=doc.select("span#ctl00_CP1_FormView1_FormView發現物_StarRankLabel");
										
				Trove trove=new Trove();
				trove.setCard_point(Integer.parseInt(card_point.get(0).text()));
				trove.setDetails(StringUtil.TransToSimple(details.get(0).text()));
				trove.setFeats(Integer.parseInt(feat.get(0).text()));
				trove.setFind_flag(0);
				trove.setGetWay(1);
				trove.setMisson(StringUtil.TransToSimple(mission.get(0).text()));
				trove.setName(StringUtil.TransToSimple(name.get(0).text()));
				trove.setPic_id("dol_t_ast"+(j+1));
				int len = rank.get(0).text().length();
				int count = 0;
				for (int i=0;i<len;i++){
				    if (rank.get(0).text().charAt(i) == '★') 
				    		count++;
				}
				trove.setRate(count);
				trove.setType("天文");
				trove.setNeed("视认:10,天文学:6");
				//任务判断
				Log.i("name",trove.getName());
				onlyOneInsertTrove(trove,dao);
			}
		}catch(Exception e){
			
		}
	}
	@SuppressWarnings("unchecked")
	private  void onlyOneInsertTrove(Trove trove,DefaultDAO dao){
		   String[] x={trove.getName()+""};
		   List<Trove>list=(List<Trove>) dao.select(Trove.class, 
	        		false, "name=?", x, 
					null, null, null, null); 
	        if(list.size()==0)
	        	dao.insert(trove);
	}
}
