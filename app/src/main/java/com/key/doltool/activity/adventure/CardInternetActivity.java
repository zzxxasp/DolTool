package com.key.doltool.activity.adventure;

import android.os.Bundle;
import android.util.Log;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.Card;
import com.key.doltool.data.CardCombo;
import com.key.doltool.data.Trove;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡组一览，战力评估
 */
public class CardInternetActivity extends BaseActivity{
	/**comboList**/
	private List<CardCombo> comboList=new ArrayList<>();
	/**自己牌组的Combo总数**/
	private List<CardCombo> selfCombo=new ArrayList<>();
	/**自己牌组的卡片数:小于30张则显示添加，等于三十张则可以进行战力评估**/
	private List<Card> list=new ArrayList<>();
	private int total;
	private int poiont_total;
	private DefaultDAO dao;
	private SRPUtil srp;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_card);
		srp=SRPUtil.getInstance(getApplicationContext());
		dao=SRPUtil.getDAO(getApplicationContext());
		findView();
//		getCombo();
//		genCard();
	}
	//初始化控件
	private void findView(){
		Card card=new Card();
		card.name="亚速尔";
		card.type="12";
		card.point=1;
		list.add(card);
		Card card2=new Card();
		card2.type="12";
		card2.point=1;
		card2.name="亚速尔群岛";
		list.add(card2);
		comboList=(List<CardCombo>)dao.select(CardCombo.class,true,"id>?",new String[]{"0"},null,null,null,null);
	}
	//根据卡组内容获得所有Combo数量
	private void getCombo(){
		for(int i=0;i<comboList.size();i++){
			List<Card> temp=comboList.get(i).getCard();
			if(list.containsAll(temp)) {
				selfCombo.add(comboList.get(i));
			}
		}
	}
	private void randomCardCombo(){

	}
	//初始化评估战力(原则)
	private void initPoint(){
		//基本点数叠加
		for(int i=0;i<list.size();i++){
			poiont_total+=list.get(i).point;
		}
		total+=poiont_total;
		//种类加成（2种类型为0 1种-5 每多一种Plus 5）
		total+=((int)srp.countByType(false,2)-2)*2;
		//combo加成(根据Combo的点数，除以所需卡片数，功能性combo均为10)
		for(int i=0;i<selfCombo.size();i++){
			total+=selfCombo.get(i).value;
			Log.i("alist",""+selfCombo.get(i).getName()+selfCombo.get(i).getEffect());
		}
		//随机点数加成[1~20]
		total+=NumberUtil.getRandom(1,20);
	}

	private void genCard(){
		List<Trove>list=(List<Trove>)dao.select(Trove.class,true,"id>?",new String[]{"0"},null,null,null,null);
		for(int i=0;i<list.size();i++){
			Card c=new Card();
			c.name=list.get(i).getName();
			c.point=list.get(i).getCard_point();
			c.type=list.get(i).getType();
			c.pic_id=list.get(i).getPic_id();
			c.flag=0;
			dao.insert(c);
		}
	}
}