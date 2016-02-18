package com.key.doltool.util.db;

import java.util.List;

import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.data.sqlite.Verion;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ViewUtil;
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

	//返回当前版本
	public static int dbVerion(DefaultDAO dao){
		Verion v=(Verion)dao.select(Verion.class, false, "verion>?",new String[]{"0"}, null, null,null, null).get(0);
		return v.verion;
	}
	
	public static Verion UpdateTime(DefaultDAO dao){
		return (Verion)dao.select(Verion.class, false, "verion>?",new String[]{"0"}, null, null,null, null).get(0);
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
		return FileManager.ITEM+ViewUtil.MD5(name);
	}
}
