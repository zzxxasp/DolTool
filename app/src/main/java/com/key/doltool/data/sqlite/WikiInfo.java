package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Wiki")
public class WikiInfo {
	@Column(autoincrement=true,primary=true)
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="sid")
	private String sid;
	@Column(name="url")
	private String url;
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
