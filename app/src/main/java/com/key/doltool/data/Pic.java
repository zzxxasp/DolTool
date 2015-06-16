package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Pic")
public class Pic {
	@Column(autoincrement=true,primary=true)
	//ID
	private int id;
	@Column(name="Rid",default_value="0")
	//RID
	private int Rid;
	@Column(name="pid",default_value="0")
	//PID
	private String pid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRid() {
		return Rid;
	}
	public void setRid(int rid) {
		Rid = rid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
}
