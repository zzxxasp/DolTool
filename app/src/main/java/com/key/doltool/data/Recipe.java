package com.key.doltool.data;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;
@Table(name="Recipe")
public class Recipe {
	@Column(autoincrement=true,primary=true)
	//ID
	private int id;
	@Column(name="name")
	//名称
	private String name;
	@Column(name="need")
	//需求
	private String need;
	@Column(name="result")
	//结果
	private String result_number;
	@Column(name="parent_name")
	//父书籍名称
	private String parent_name;
	@Column(name="level_need")
	//技能需求
	private String level_need;
	@Column(name="other")
	//其他消费
	private String other;
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
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public String getResult_number() {
		return result_number;
	}
	public void setResult_number(String result_number) {
		this.result_number = result_number;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public String getLevel_need() {
		return level_need;
	}
	public void setLevel_need(String level_need) {
		this.level_need = level_need;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}	
}
