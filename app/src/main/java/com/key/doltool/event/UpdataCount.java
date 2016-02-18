package com.key.doltool.event;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.key.doltool.data.sqlite.Card;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.data.sqlite.Trove_Count;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;
/**更新事件:主要复制初始化和备份数据的操作**/
public class UpdataCount {
	private DefaultDAO dao;
	private SRPUtil srp;
	public UpdataCount(Context context){
		dao=SRPUtil.getDAO(context);
		srp=SRPUtil.getInstance(context);
	}
	public static String[] name_type={
		"史迹","宗教建筑","历史遗物","宗教遗物","美术品",
		"财宝","化石","植物","昆虫","鸟类","海洋生物",
		"小型生物","中型生物","大型生物","港口","地理","天文"
	};
	
	public void init_adventure(){
		for(int i=1;i<=name_type.length;i++){
			//查询发现表
		    Trove_Count count=new Trove_Count();
		    count.setNow(dao.select(Trove.class, false, "type=? and flag=?",new String[]{name_type[i-1],"1"}, null, null, null, null).size());
	        //更新统计表
			dao.update(count, new String[]{"now"}, "type=?", new String[]{""+i});
		}
	}
	public void init_adventure(String type){
	    Trove_Count count=new Trove_Count();
	    count.setNow(dao.select(Trove.class, false, "type=? and flag=?",new String[]{type,"1"}, null, null, null, null).size());
        //更新统计表
	    for(int i=0;i<name_type.length;i++){
	    	if(type.equals(name_type[i])){
	    		dao.update(count, new String[]{"now"}, "type=?", new String[]{""+(i+1)});
	    	}
	    }
	}
	public void update_adventure(String type,int now){
		Trove_Count counts=new Trove_Count();
		counts.setNow(now);
	    for(int i=0;i<name_type.length;i++){
	    	if(type.equals(name_type[i])){
	    		dao.update(counts, new String[]{"now"}, "type=?", new String[]{""+(i+1)});
	    	}
	    }
	}
	public void update_addMode(String type,int now){
		Trove_Count counts;
	    for(int i=0;i<name_type.length;i++){
	    	if(type.equals(name_type[i])){
	    		counts=(Trove_Count)dao.select(Trove_Count.class, false, "type=?",new String[]{""+(i+1)}, null, null, null, null).get(0);
	    		counts.setNow(counts.getNow()+now);
	    		dao.update(counts, new String[]{"now"}, "type=?", new String[]{""+(i+1)});
	    	}
	    }
	}
	@SuppressWarnings("unchecked")
	//保存所有已发现的发现物
	public List<Integer> saveIdFlag(){
		List<Trove> list=(List<Trove>)dao.select(Trove.class, false, "flag=?",new String[]{"1"}, null, null, null, null);
		List<Integer> temp=new ArrayList<>();
		for(int i=0;i<list.size();i++){
			temp.add(list.get(i).getId());
		}
		return temp;
	}
	@SuppressWarnings("unchecked")
	public List<Integer> saveMission(int tagert){
		if(tagert>6){
			List<Mission> list=(List<Mission>)dao.select(Mission.class, false, "tag=?",new String[]{"1"}, null, null, null, null);
			List<Integer> temp=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				temp.add(list.get(i).getId());
			}
			return temp;
		}else{
			return null;
		}
	}
	//还原任务记录
	public void backSaveMission(List<Integer> list){
		List<Mission> temp=new ArrayList<>();
		for(int i=0;i<list.size();i++){
			Mission mission=new Mission();
			mission.setTag(1);
			mission.setId(list.get(i));
			temp.add(mission);
		}
		srp.update_Mission(temp);
		
	}
	//还原所有发现物的状态
	public void backSave(List<Integer> list){
		List<Trove> temp=new ArrayList<>();
		for(int i=0;i<list.size();i++){
			Trove trove=new Trove();
			trove.setFind_flag(1);
			trove.setId(list.get(i));
			temp.add(trove);
		}
		Log.i("size",""+srp.update_Trove(temp));
		//更新统计数据
		init_adventure();
	}
	//还原卡组
	public void backSaveCard(List<Integer> list){
		List<Card> temp=new ArrayList<>();
		List<Card> out_temp=srp.select(Card.class, false, "flag=?",new String[]{"1"}, null, null, null, null);
		for(int i=0;i<out_temp.size();i++){
			Card card = new Card();
			card.flag = 0;
			card.id = out_temp.get(i).id;
			temp.add(card);
		}

		for (int i=0;i<list.size();i++) {
			Card card = new Card();
			card.flag = 1;
			card.id = list.get(i);
			temp.add(card);
		}

		srp.update_Card(temp);
	}

	public void backSync(List<String> list){
		List<Integer> mList=new ArrayList<>();
		List<Integer> tList=new ArrayList<>();
		for(int i=0;i<list.size();i++){
			String temp[]=list.get(i).split("\\+");
			if(temp[0].equals("m")){
				mList.add(Integer.parseInt(temp[1]));
			}else if(temp[0].equals("t")){
				tList.add(Integer.parseInt(temp[1]));
			}
		}
		Log.i("size","任务："+mList.size()+"-发现物："+tList.size());
		backSaveMission(mList);
		backSave(tList);
	}



}
