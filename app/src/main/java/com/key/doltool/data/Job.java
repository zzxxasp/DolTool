package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Job")
public class Job {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;
	//说明
	@Column(name="detail")
	private String detail;
	//类型
	@Column(name="type")
	private int type;
	//特技
	@Column(name="sp")
	private String sp;
	//优待技能
	@Column(name="good_list")
	private String good_list;
	//转职条件
	@Column(name="chang_if")
	private String chang_if;
	//转职证
	@Column(name="metal")
	private String metal;
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSp() {
		return sp;
	}
	public void setSp(String sp) {
		this.sp = sp;
	}
	public String getGood_list() {
		return good_list;
	}
	public void setGood_list(String good_list) {
		this.good_list = good_list;
	}
	public String getChang_if() {
		return chang_if;
	}
	public void setChang_if(String chang_if) {
		this.chang_if = chang_if;
	}
	public String getMetal() {
		return metal;
	}
	public void setMetal(String metal) {
		this.metal = metal;
	}
}
