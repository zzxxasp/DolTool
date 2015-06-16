package com.key.doltool.activity.dockyard;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.data.SailBoat;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.util.jsoup.JsoupForBaHa;
import com.key.doltool.util.jsoup.JsoupForGVO;
import com.key.doltool.util.jsoup.JsoupUtil;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;
public class DataBaseInsertFragment extends BaseFragment {
	private DefaultDAO dao;
	private JsoupUtil jsoup;
	private JsoupForBaHa jsoupB=new JsoupForBaHa();
	private JsoupForGVO jsoupG=new JsoupForGVO();
	private FlatButton btn,btn2;
	private ExecutorService fixedThreadPool ;
	
    private View main;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.init, container,false);
		 init(view);
		 return view; 
	}
	
	private void init(View view){
		main=view;
		dao=SRPUtil.getDAO(getActivity());
		jsoup=new JsoupUtil(getActivity());
		btn=(FlatButton)main.findViewById(R.id.btn);
		btn2=(FlatButton)main.findViewById(R.id.btn2);
		fixedThreadPool= Executors.newFixedThreadPool(20);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				update_download();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBUtil.copyDB_SD(getActivity());
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
//			for(int i=4454;i<4460;i++){
//				jsoupG.getMission(i,dao);
//			}
			jsoupG.do_TIANWEN(dao);
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