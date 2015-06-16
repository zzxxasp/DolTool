package com.key.doltool.data;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;
@Table(name="TradeItem")
public class TradeItem {
	@Column(autoincrement=true,primary=true)
	//ID
	private int id;
	@Column(name="pic_id")
	//图片ID
	private String pic_id;
	@Column(name="name")
	//名称
	private String name;
	@Column(name="detail")
	//描述
	private String detail;
	@Column(name="type")
	//商品种类
	private String type;
	@Column(name="trade",default_value="0")
	//交易品类别
	private int trade;
	@Column(name="price",default_value="0")
	//Base价格
	private int price;
	@Column(name="sp")
	//特产与否(默认不是)
	private String sp;
	@Column(name="producing_area")
	//出产地
	private String producing_area;
	@Column(name="recipe_way")
	//配方名称
	private String recipe_way;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTrade() {
		return trade;
	}
	public void setTrade(int trade) {
		this.trade = trade;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getSp() {
		return sp;
	}
	public void setSp(String sp) {
		this.sp = sp;
	}
	public String getProducing_area() {
		return producing_area;
	}
	public void setProducing_area(String producing_area) {
		this.producing_area = producing_area;
	}
	public String getRecipe_way() {
		return recipe_way;
	}
	public void setRecipe_way(String recipe_way) {
		this.recipe_way = recipe_way;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
