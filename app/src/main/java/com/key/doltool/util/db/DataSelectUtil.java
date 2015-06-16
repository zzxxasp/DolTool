package com.key.doltool.util.db;

import java.util.List;

import com.key.doltool.data.Skill;
import com.key.doltool.data.TradeItem;
import com.key.doltool.data.Trove;
import com.key.doltool.data.Verion;
import com.key.doltool.util.FileManager;
import com.the9tcat.hadi.DefaultDAO;
/**---关联查询---**/
public class DataSelectUtil {
	public static String getSkillPicByName(String name,DefaultDAO dao){
		List<?> list=dao.select(Skill.class, false,"name=?", new String[]{name}, 
				null, null, null,null);
		if(list.size()>0){
			Skill item=(Skill)list.get(0);
			return FileManager.SKILL+item.getPic_id();
		}
		return "";
	}
	public static int getItemPIDByName(String name,DefaultDAO dao){
//		List<Item> list1=new ArrayList<Item>();
//		List<TradeItem> list2=new ArrayList<TradeItem>();
//		list1=(List<Item>) dao.select(Item.class, false,"name=?", new String[]{name}, 
//				null, null, null,null);
//		list2=(List<TradeItem>) dao.select(TradeItem.class, false,"name=?", new String[]{name}, 
//		null, null, null,null);
//     if(list1.size!=0)
//	    return getPicById(list1.get(0).getPid(),dao);
//	    else
//     return getPicById(list2.get(0).getPid(),dao);
		return 0;
	}
	//返回当前版本
	public static int dbVerion(DefaultDAO dao){
		Verion v=(Verion)dao.select(Verion.class, false, "verion>?",new String[]{"0"}, null, null,null, null).get(0);
		return v.verion;
	}
	
	public static Verion UpdateTime(DefaultDAO dao){
		Verion v=(Verion)dao.select(Verion.class, false, "verion>?",new String[]{"0"}, null, null,null, null).get(0);
		return v;
	}
	
	public static String getTradePicByName(String name,DefaultDAO dao){
		List<?> list=dao.select(TradeItem.class, false, "name=?",new String[]{name}, null, null, null, null);
		if(list.size()>0){
			TradeItem item=(TradeItem)list.get(0);
			return FileManager.TRADE+item.getPic_id();
		}else{
			return "";
		}
	}
	public static String getTrovePicByName(String name,DefaultDAO dao){
		List<?> list=dao.select(Trove.class, false, "name=?",new String[]{name}, null, null, null, null);
		if(list.size()>0){
			Trove item=(Trove)list.get(0);
			return item.getPic_id();
		}else{
			return "";
		}
	}
	public static String getRecipePicByName(String name,DefaultDAO dao){
		List<?> list=dao.select(TradeItem.class, false, "name=?",new String[]{name}, null, null, null, null);
		if(list.size()>0){
			TradeItem item=(TradeItem)list.get(0);
			return FileManager.TRADE+item.getPic_id();
		}
//		list=dao.select(EquipItem.class, false, "name=?",new String[]{name}, null, null, null, null);
//		if(list.size()>0){
//			EquipItem item=(EquipItem)list.get(0);
//			return item.toString();	
//		}
//		list=dao.select(UseItem.class, false, "name=?",new String[]{name}, null, null, null, null);
//		if(list.size()>0){
//			UseItem item=(UseItem)list.get(0);
//			return item.toString();	
//		}
		return "";
	}
}
