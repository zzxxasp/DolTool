package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Book")
public class Book {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//名称
	@Column(name="name")
	private String name;	
	//类型
	@Column(name="type")
	private int type;
	//技能范围
	@Column(name="range")
	private String range;
	//获取方式
	@Column(name="get_way")
	private String get_way;
	//类型描述
	@Column(name="desc_type")
	private String desc_type;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getGet_way() {
		return get_way;
	}
	public void setGet_way(String get_way) {
		this.get_way = get_way;
	}
	public String getDesc_type() {
		return desc_type;
	}
	public void setDesc_type(String desc_type) {
		this.desc_type = desc_type;
	}
}
