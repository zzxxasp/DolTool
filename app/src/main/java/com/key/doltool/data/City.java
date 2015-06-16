package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="City")
public class City {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//地区
	@Column(name="area")
	private String area;
	//文化圈
	@Column(name="culture")
	private String culture;
	//语言
	@Column(name="lang")
	private String lang;
	//投资
	@Column(name="invest")
	private String invest;
	//商品清单
	@Column(name="trade_list")
	private String trade_list;
	//type(城市类型)
	@Column(name="type")
	private String type;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCulture() {
		return culture;
	}
	public void setCulture(String culture) {
		this.culture = culture;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getTrade_list() {
		return trade_list;
	}
	public void setTrade_list(String trade_list) {
		this.trade_list = trade_list;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
