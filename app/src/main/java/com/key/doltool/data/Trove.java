package com.key.doltool.data;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;
@Table(name="Trove")
//发现物
public class Trove {
	@Column(autoincrement=true,primary=true)
	private int id;
	@Column(name="pic_id")
	private String pic_id;
	@Column(name="name")
	private String name;
	//类型
	@Column(name="type")
	private String type;
	//星级
	@Column(name="rate")
	private int rate;
	//卡片点数
	@Column(name="card_point")
	private int card_point;
	//功绩
	@Column(name="feats")
	private int feats;
	//任务名称
	@Column(name="misson")
	private String misson;
	//介绍
	@Column(name="details")
	private String details;
	//需求
	@Column(name="need")
	private String need;
	//发现标记
	@Column(name="flag",default_value="0")
	private int find_flag;
	//获取方式
	@Column(name="getWay",default_value="0")
	private int getWay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getCard_point() {
		return card_point;
	}
	public void setCard_point(int card_point) {
		this.card_point = card_point;
	}
	public String getMisson() {
		return misson;
	}
	public void setMisson(String misson) {
		this.misson = misson;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getFeats() {
		return feats;
	}
	public void setFeats(int feats) {
		this.feats = feats;
	}
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public int getFind_flag() {
		return find_flag;
	}
	public void setFind_flag(int find_flag) {
		this.find_flag = find_flag;
	}
	public int getGetWay() {
		return getWay;
	}
	public void setGetWay(int getWay) {
		this.getWay = getWay;
	}
}
