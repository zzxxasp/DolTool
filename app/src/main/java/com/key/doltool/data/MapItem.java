package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Map")
public class MapItem {
	@Column(autoincrement=true,primary=true)
	//id
	public int id;
	//名称
	@Column(name="name")
	public String name;
	//海域坐标
	@Column(name="co_sea")
	public String co_sea;
	//地图坐标
	@Column(name="co_map")
	public String co_map;
	//类型(1:同盟港 2:根据地 3:探险地  4:开拓)
	@Column(name="type")
	public int type;
	//海域名称
	@Column(name="sea_name")
	public String sea_name;
}
