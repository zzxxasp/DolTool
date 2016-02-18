package com.key.doltool.data.sqlite;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;
@Table(name="Trove_Count")
//发现物统计
public class Trove_Count {
	@Column(name="size")
	private int size;
	@Column(name="type")
	private int type;
	@Column(name="now")
	private int now;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNow() {
		return now;
	}
	public void setNow(int now) {
		this.now = now;
	}
}
