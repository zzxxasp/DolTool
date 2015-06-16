package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="ADC")
public class ADCInfo {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//技能表
	@Column(name="head_img")
	private String head_img;
	//技能表
	@Column(name="skill_list")
	private String skill_list;
	//所在城市
	@Column(name="city")
	private String city;
	//性别
	@Column(name="sex")
	private String sex;
	//类型
	@Column(name="type")
	private int type;
	//国籍
	@Column(name="country")
	private String country;
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
	public String getSkill_list() {
		return skill_list;
	}
	public void setSkill_list(String skill_list) {
		this.skill_list = skill_list;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	
}
