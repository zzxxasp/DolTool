package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Verion")
public class Verion {
	@Column(name="verion")
	public int verion;
	@Column(name="update_time")
	public String update_time;
}
