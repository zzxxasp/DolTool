package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Skill")
public class Skill {
	@Column(autoincrement=true,primary=true)
	//ID
	private int id;
	@Column(name="pic_id")
	//图片ID
	private String pic_id;
	@Column(name="type",default_value="0")
	//类型(冒,商,战,语言,船,副官)
	private int type;
	@Column(name="name")
	//名称
	private String name;
	@Column(name="detail")
	//效果
	private String detail;
	@Column(name="need")
	//需求
	private String need;
	@Column(name="do_point")
	//行动力消耗
	private String do_point;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public String getDo_point() {
		return do_point;
	}
	public void setDo_point(String do_point) {
		this.do_point = do_point;
	}
}
