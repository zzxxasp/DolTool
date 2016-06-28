package com.key.doltool.activity.dockyard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.TradeCityItem;
import com.key.doltool.data.sqlite.City;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.util.jsoup.JsoupForBaHa;
import com.key.doltool.util.jsoup.JsoupForGVO;
import com.key.doltool.util.jsoup.JsoupUtil;
import com.key.doltool.view.flat.FlatButton;

import com.the9tcat.hadi.DefaultDAO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

public class DataBaseInsertFragment extends BaseFragment {
	private DefaultDAO dao;
	private JsoupUtil jsoup;
	private JsoupForBaHa jsoupB=new JsoupForBaHa();
	private JsoupForGVO jsoupG=new JsoupForGVO();
	@BindView(R.id.btn)  FlatButton btn;
	@BindView(R.id.btn2)  FlatButton btn2;
	private ExecutorService fixedThreadPool ;
	private List<Trove> list;
	private int index=-1;
	private Trove trove;

	@Override
	public int getContentViewId() {
		return R.layout.init;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		init(mRootView);
	}

	private void init(View view){
		dao=SRPUtil.getDAO(getActivity());
		jsoup=new JsoupUtil(getActivity());
		fixedThreadPool= Executors.newFixedThreadPool(20);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				updateCityTradeItem();
				initParse();
//				update_download();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBUtil.copyDB_SD(getActivity());
			}
		});
	}


	private void updateCityTradeItem(){
		AVQuery<AVObject> p=new AVQuery<>("trade");
		p.whereEqualTo("flag",0);
		p.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if (e == null && list.size() != 0) {
					for(int i=0;i<list.size();i++){
						AVObject object=list.get(i);
						object.put("flag", 1);
						object.saveInBackground();
						City city=selectCity(object);
						if(city!=null){
							SRPUtil.getDAO(getActivity()).update(city,
									new String[]{"trade_list"},"name=?",new String[]{city.getName()});
						}
					}
				}
			}
		});
	}

	private City selectCity(AVObject object){
		boolean bool=true;
		Gson g=new Gson();
		List<City> list=SRPUtil.getInstance(getActivity()).select(City.class, false, "name=?", new String[]{object.getString("name")}, null, null, null, null);
		if(list.size()==1){
			City item=list.get(0);
			List<TradeCityItem> temp=g.fromJson(item.getTrade_list(), new TypeToken<List<TradeCityItem>>() {
			}.getType());
			for(int i=0;i<temp.size();i++){
				if(temp.get(i).name.equals(object.getString("trade_name"))){
					TradeCityItem item1=new TradeCityItem();
					item1.name=object.getString("trade_name");
					item1.price=object.getString("price");
					item1.invest=object.getString("invest");
					temp.set(i, item1);
					bool=false;
					break;
				}
			}
			if(bool){
				TradeCityItem item1=new TradeCityItem();
				item1.name=object.getString("trade_name");
				item1.price=object.getString("price");
				item1.invest=object.getString("invest");
				temp.add(item1);
			}
			item.setTrade_list(g.toJson(temp));
			return item;
		}else{
			return null;
		}
	}

	private void initParse(){
		new Thread(){
			@Override
			public void run() {
				list=SRPUtil.getInstance(getActivity()).select(Trove.class, false, "id>?", new String[]{"0"}, null, null, null,"0,100");
				dd();
			}
		}.start();
	}

	private void dd(){
		index++;
		if(index>=list.size()){
			return ;
		}
		trove=list.get(index);
		AVQuery<AVObject> p=new AVQuery<>("Trove");
		p.whereEqualTo("index_id", trove.getId());
		p.findInBackground(new FindCallback<AVObject>() {
		@Override
		public void done(List<AVObject> list, AVException e) {
			if(e==null&&list.size()==0){
					initPP();
				}
			}
		});
	}

	private void initPP(){
		AVObject item=new AVObject("Trove");
		byte[] file= ResourcesUtil.getHtmlByAsset(getActivity(),FileManager.TROVE+trove.getPic_id()+".jpg");
		if(file!=null&&file.length>0){
			AVFile file_item=new AVFile(trove.getPic_id()+".jpg",file);
			item.put("pic",file_item);
			file_item.saveInBackground();
		}
		item.put("index_id",trove.getId());
		item.put("details",trove.getDetails());
		item.put("card_point",trove.getCard_point());
		item.put("feats",trove.getFeats());
		item.put("getWay",trove.getGetWay());
		item.put("misson",trove.getMisson());
		item.put("name",trove.getName());
		if(trove.getNeed()!=null){
			item.put("need",trove.getNeed());
		}
		item.put("type",trove.getType());
		item.put("rate",trove.getRate());
		item.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				if(e==null){
					dd();
				}
			}
		});
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	public void update_download(){
		fixedThreadPool.execute(new Task(0));
	}
	//更新测试
	public void update(){
	    SailBoat sail=new SailBoat();
        sail.setName("小型卡尔维帆船");
        dao.update(sail,new String[]{"name"}, "id=?",new String[]{"2"});
	}
	private class Task implements Runnable{
		public int id;
		public Task(int id){
			this.id=id;
		}
		public void run() {
			//海洋生物测试
//			jsoupB.getFind(JsoupForBaHa.C_SEA,dao);
//			jsoupB.getFind(JsoupForBaHa.H_B,dao);
//			jsoupB.getFind(JsoupForBaHa.R_B,dao);
//			jsoupB.getFind(JsoupForBaHa.A_W,dao);
//			jsoupB.getFind(JsoupForBaHa.FOSSIL, dao);
//			jsoupB.getFind(JsoupForBaHa.INSECT, dao);
//			jsoupB.getFind(JsoupForBaHa.PLANT, dao);
//			jsoupB.getFind(JsoupForBaHa.C_S, dao);
//			jsoupB.getFind(JsoupForBaHa.C_M, dao);
//			jsoupB.getFind(JsoupForBaHa.C_B, dao);
//			jsoupB.getFind(JsoupForBaHa.MINE, dao);
//			jsoupB.getFind(JsoupForBaHa.BIRD, dao);
//			jsoupB.getFind(JsoupForBaHa.PORT, dao);
//			jsoupB.getFind(JsoupForBaHa.R_I, dao);
//			jsoupB.getFind(JsoupForBaHa.H_I_1, dao);
//			jsoupB.getFind(JsoupForBaHa.H_I_2, dao);
//			jsoupB.getFind(JsoupForBaHa.GEOG1, dao);
//			jsoupB.getFind(JsoupForBaHa.GEOG2, dao);
//			jsoupB.getFind(JsoupForBaHa.MINE_1, dao);
//			jsoupB.getFind(JsoupForBaHa.MINE_2, dao);
			//taskEvent(id);
//			jsoup.getTradeItem(dao,1,1);
//			jsoup.getTradeItem(dao,2,1);
//			jsoup.getTradeItem(dao,6,1);
//			jsoup.getTradeItem(dao,10,1);
//			jsoup.getTradeItem(dao,20,1);
//			jsoup.getTradeItem(dao,7,2);
//			jsoup.getTradeItem(dao,12,2);
//			jsoup.getTradeItem(dao,4,2);
//			jsoup.getTradeItem(dao,5,2);
//			jsoup.getTradeItem(dao,8,2);
			
//			jsoup.getTradeItem(dao,3,3);
//			jsoup.getTradeItem(dao,11,3);
//			jsoup.getTradeItem(dao,16,3);
//			jsoup.getTradeItem(dao,17,3);
//			jsoup.getTradeItem(dao,19,3);
//			jsoup.getTradeItem(dao,18,3);
//			
//			jsoup.getTradeItem(dao,9,4);
//			jsoup.getTradeItem(dao,13,4);
//			jsoup.getTradeItem(dao,14,4);
//			jsoup.getTradeItem(dao,15,4);
	
//			jsoup.getCity(1,dao);
//			jsoup.getCity(2,dao);
//			jsoup.getCity(3,dao);
//			jsoup.getCity(4,dao);
//			jsoup.getCity(5,dao);
//			jsoup.getCity(6,dao);
//			jsoup.getCity(7,dao);
//			jsoup.getCity(8,dao);
//			jsoup.getCity(9,dao);
//			jsoup.getCity(10,dao);
//			jsoup.getCity(11,dao);
//			jsoup.getCity(12,dao);
//			jsoup.getCity(13,dao);
//			jsoup.getCity(14,dao);
//			jsoup.getCity(15,dao);
//			jsoup.getADC(1, dao);
//			jsoup.getADC(2, dao);
//			jsoup.getADC(3, dao);
//			for(int i=4664;i<4752;i++){
//				jsoupG.getMission(i, dao);
//			}
			jsoup.getPicMe(SRPUtil.getInstance(getActivity()));
//			jsoupB.getTradeItemUrl(dao);
//			jsoup.getship(dao);
//			jsoupG.do_TIANWEN(dao,1);
//			jsoupG.do_TIANWEN(dao,2);
//			jsoupG.do_TIANWEN(dao,3);
//			jsoupG.do_TIANWEN(dao,4);
//			jsoupG.do_TIANWEN(dao,5);
//			jsoupG.do_TIANWEN(dao,6);
//			jsoupG.do_TIANWEN(dao,7);
//			jsoupG.do_TIANWEN(dao,8);
//			jsoupG.do_TIANWEN(dao,9);
//			jsoupG.do_TIANWEN(dao,10);
//			jsoupG.do_TIANWEN(dao,11);
//			jsoupG.do_TIANWEN(dao,12);
//			jsoupG.do_TIANWEN(dao,13);
//			jsoupG.do_TIANWEN(dao,14);
//			jsoupG.do_TIANWEN(dao,15);
//			jsoupG.do_TIANWEN(dao,16);
//			jsoupG.do_TIANWEN(dao,17);
//			jsoup.getSkill(1,dao);
//			jsoup.getSkill(2,dao);
//			jsoup.getSkill(3,dao);
			
//			jsoup.getSkill(6,dao);
			
//			jsoup.getNPC(dao);
			
//			jsoup.getRecipe(dao,70);
//			jsoup.getRecipe(dao,60);
//			jsoup.getRecipe(dao,51);
//			jsoup.getRecipe(dao,81);
//			jsoup.getRecipe(dao,82);
//			jsoup.getRecipe(dao,83);
//			jsoup.getRecipe(dao,84);
//			jsoup.getRecipe(dao,85);
//			jsoup.getRecipe(dao,85);
//			jsoup.getRecipe(dao,86);
//			jsoup.getRecipe(dao,87);
//			jsoup.getRecipe(dao,88);
//			jsoup.getRecipe(dao,89);
//			jsoup.getRecipe(dao,91);
			
//			jsoup.getRecipe(dao,21);
//			jsoup.getRecipe(dao,22);
//			jsoup.getRecipe(dao,23);
//			jsoup.getRecipe(dao,24);
//			jsoupG.getCardCombo(dao);
//			jsoup.getJob(1,dao);
//			jsoup.getJob(2,dao);
//			jsoup.getJob(3,dao);
		}
	}
	//任务线程
	private void taskEvent(int id){
		switch(id){
		case 0:{
//			jsoup.getBoat(dao);
//			jsoup.getSail(dao);
//			jsoup.getCannon(dao);
//			jsoup.getDef(dao);
//			jsoup.getEquip(dao);
//			jsoup.getTop(dao);
//			jsoup.getBody(dao);
//			jsoup.getMainSail(dao);
//			jsoup.getMainCannon(dao);
//			jsoup.getEQ(dao);
			break;
			}
		}
	}
}