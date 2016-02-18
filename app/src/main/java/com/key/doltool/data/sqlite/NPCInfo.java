package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="NPCInfo")
public class NPCInfo {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//技能教授
	@Column(name="skill_tech")
	private String skill_tech;
	//汇报类型
	@Column(name="love_type")
	private String love_type;
	//所在城市
	@Column(name="city")
	private String city;
	//备注
	@Column(name="other")
	private String other;
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
	public String getSkill_tech() {
		return skill_tech;
	}
	public void setSkill_tech(String skill_tech) {
		this.skill_tech = skill_tech;
	}
	public String getLove_type() {
		return love_type;
	}
	public void setLove_type(String love_type) {
		this.love_type = love_type;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
