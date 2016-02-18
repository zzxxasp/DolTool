package com.key.doltool.data.sqlite;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;
@Table(name="Part")
public class Part {
	@Column(autoincrement=true,primary=true)
	//id
	private int id;
	//图片ID
	@Column(name="pic_id")
	private String pic_id;
	//名称
	@Column(name="name")
	private String name;
	//船件类型(0:炮,1:帆)
	@Column(name="type")
	private int type;
	//船件类型(0:装备,1:造船)
	@Column(name="ztype")
	private int ztype;
	//获取方式
	@Column(name="get_way")
	private String get_way;
	//生产方式
	@Column(name="recipe_way")
	private String recipe_way;
	//效果
	@Column(name="plus")
	private String add;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getZtype() {
		return ztype;
	}
	public void setZtype(int ztype) {
		this.ztype = ztype;
	}
	public String getGet_way() {
		return get_way;
	}
	public void setGet_way(String get_way) {
		this.get_way = get_way;
	}
	public String getRecipe_way() {
		return recipe_way;
	}
	public void setRecipe_way(String recipe_way) {
		this.recipe_way = recipe_way;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
}
