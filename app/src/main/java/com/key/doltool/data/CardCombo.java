package com.key.doltool.data;

import com.key.doltool.util.StringUtil;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name="CardCombo")
public class CardCombo {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//效果
	@Column(name="effect")
	private String effect;
	//第一张卡
	@Column(name="card_1")
	private String card_1;
	//第二张卡
	@Column(name="card_2")
	private String card_2;
	//第三张卡
	@Column(name="card_3")
	private String card_3;
	@Column(name="value")
	public int value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public String getCard_1() {
		return card_1;
	}
	public void setCard_1(String card_1) {
		this.card_1 = card_1;
	}
	public String getCard_2() {
		return card_2;
	}
	public void setCard_2(String card_2) {
		this.card_2 = card_2;
	}
	public String getCard_3() {
		return card_3;
	}
	public void setCard_3(String card_3) {
		this.card_3 = card_3;
	}

	public List<Card> getCard(){
		List<Card> list=new ArrayList<>();
		Card card1=new Card();
		String temp1[]=card_1.split("-");
		card1.type=temp1[0];
		String temp2[]=temp1[1].split("\\(");
		card1.name=temp2[0];
		String temp3[]=temp2[1].split("\\)");
		card1.point=Integer.parseInt(temp3[0]);
		list.add(card1);
		Card card2=new Card();
		String temp11[]=card_2.split("-");
		card2.type=temp11[0];
		String temp22[]=temp11[1].split("\\(");
		card2.name=temp22[0];
		String temp33[]=temp22[1].split("\\)");
		card2.point=Integer.parseInt(temp33[0]);
		list.add(card2);
		if(!StringUtil.isNull(card_3)){
			Card card3=new Card();
			String temp13[]=card_3.split("-");
			card3.type=temp13[0];
			String temp23[]=temp13[1].split("\\(");
			card3.name=temp23[0];
			String temp3_3[]=temp23[1].split("\\)");
			card3.point=Integer.parseInt(temp3_3[0]);
			list.add(card3);
		}
		return list;
	}
}
