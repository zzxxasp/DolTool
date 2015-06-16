package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

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
	
	
}
