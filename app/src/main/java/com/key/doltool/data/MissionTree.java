package com.key.doltool.data;

import java.util.List;

public class MissionTree {
	private String name;
	private int level;
	private List<MissionTree> before_mission;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<MissionTree> getBefore_mission() {
		return before_mission;
	}
	public void setBefore_mission(List<MissionTree> before_mission) {
		this.before_mission = before_mission;
	}
	
}
