package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Word")
public class WordItem {
	@Column(autoincrement=true,primary=true)
	//id
	public int id;
	//国服名称
	@Column(name="zh_name")
	public String zh_name;
	//台服名称
	@Column(name="tw_name")
	public String tw_name;
	//类型
	@Column(name="type")
	public String type;
}
