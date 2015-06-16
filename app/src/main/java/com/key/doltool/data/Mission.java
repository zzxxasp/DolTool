package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Mission")
public class Mission{
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//类型(冒，商，战)
	@Column(name="type")
	private String type;
	//技能需求:（搜索7，英语）
	@Column(name="skill_need")
	private String skill_need;
	//接受任务城市
	@Column(name="start_city")
	private String start_city;
	//发现物(限制类型:冒)
	@Column(name="find_item")
	private String find_item;
	//入手物
	@Column(name="get_item")
	private String get_item;
	//报酬
	@Column(name="money")
	private String money;
	//经验
	@Column(name="exp")
	private String exp;
	//等级
	@Column(name="level")
	private String level;
	//时间限制
	@Column(name="time_up")
	private String time_up;
	//任务流程
	@Column(name="daily")
	private String daily;
	//备注
	@Column(name="other")
	private String other;
	//前置任务
	@Column(name="before")
	private String before;
	//后续任务
	@Column(name="after")
	private String after;
	//完成标记
	@Column(name="tag",default_value="0")
	private int tag;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSkill_need() {
		return skill_need;
	}
	public void setSkill_need(String skill_need) {
		this.skill_need = skill_need;
	}
	public String getStart_city() {
		return start_city;
	}
	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}
	public String getFind_item() {
		return find_item;
	}
	public void setFind_item(String find_item) {
		this.find_item = find_item;
	}
	public String getGet_item() {
		return get_item;
	}
	public void setGet_item(String get_item) {
		this.get_item = get_item;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTime_up() {
		return time_up;
	}
	public void setTime_up(String time_up) {
		this.time_up = time_up;
	}
	public String getDaily() {
		return daily;
	}
	public void setDaily(String daily) {
		this.daily = daily;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
}
