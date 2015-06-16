package com.key.doltool.data;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="SailBoat")
public class SailBoat{
	@Column(autoincrement=true,primary=true)
	//ID
	private int id;
	@Column(name="pic_id")
	//图片ID
	private String pic_id;
	@Column(name="type",default_value="0")
	//类型
	private int type;
	@Column(name="size",default_value="0")
	//大小
	private int size;
	@Column(name="way_id",default_value="0")
	//用途
	private int way_id;
	@Column(name="name")
	//名称
	private String name;
	@Column(name="health_boat")
	//耐久
	private int health_boat;
	@Column(name="square_sail")
	//横帆
	private int square_sail;
	@Column(name="fore_sail")
	//纵帆
	private int fore_sail;
	@Column(name="paddle",default_value="0")
	//桨力
	private int paddle;
	@Column(name="turn")
	//转向
	private int turn;
	@Column(name="def_wave")
	//抗浪
	private int def_wave;
	@Column(name="armor")
	//装甲
	private int armor;
	@Column(name="people_number")
	//人数
	private int people_number;
	@Column(name="crenelle")
	//炮门
	private int crenelle;
	@Column(name="shipping_space")
	//仓位
	private int shipping_space;
	@Column(name="level_m")
	//冒等
	private int level_m;
	@Column(name="level_s")
	//商等
	private int level_s;
	@Column(name="level_j")
	//军等
	private int level_j;
	@Column(name="number_part")
	//船装备
	private String number_part;
	@Column(name="people_must")
	//必要人数
	private int people_must;
	@Column(name="ability")
	//技能
	private String ability;
	@Column(name="get_way")
	//获取方式
	private String get_way;
	@Column(name="plus_point")
	//强化次数
	private int plus_point;
	@Column(name="recipe_way")
	//生产方式
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getWay_id() {
		return way_id;
	}
	public void setWay_id(int way_id) {
		this.way_id = way_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSquare_sail() {
		return square_sail;
	}
	public void setSquare_sail(int square_sail) {
		this.square_sail = square_sail;
	}
	public int getFore_sail() {
		return fore_sail;
	}
	public void setFore_sail(int fore_sail) {
		this.fore_sail = fore_sail;
	}
	public int getPaddle() {
		return paddle;
	}
	public void setPaddle(int paddle) {
		this.paddle = paddle;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getDef_wave() {
		return def_wave;
	}
	public void setDef_wave(int def_wave) {
		this.def_wave = def_wave;
	}
	public int getArmor() {
		return armor;
	}
	public void setArmor(int armor) {
		this.armor = armor;
	}
	public int getPeople_number() {
		return people_number;
	}
	public void setPeople_number(int people_number) {
		this.people_number = people_number;
	}
	public int getCrenelle() {
		return crenelle;
	}
	public void setCrenelle(int crenelle) {
		this.crenelle = crenelle;
	}
	public int getShipping_space() {
		return shipping_space;
	}
	public void setShipping_space(int shipping_space) {
		this.shipping_space = shipping_space;
	}
	public int getLevel_m() {
		return level_m;
	}
	public void setLevel_m(int level_m) {
		this.level_m = level_m;
	}
	public int getLevel_s() {
		return level_s;
	}
	public void setLevel_s(int level_s) {
		this.level_s = level_s;
	}
	public int getLevel_j() {
		return level_j;
	}
	public void setLevel_j(int level_j) {
		this.level_j = level_j;
	}
	public String getNumber_part() {
		return number_part;
	}
	public void setNumber_part(String number_part) {
		this.number_part = number_part;
	}
	public int getPeople_must() {
		return people_must;
	}
	public void setPeople_must(int people_must) {
		this.people_must = people_must;
	}
	public int getHealth_boat() {
		return health_boat;
	}
	public void setHealth_boat(int health_boat) {
		this.health_boat = health_boat;
	}
	public String getGet_way() {
		return get_way;
	}
	public void setGet_way(String get_way) {
		this.get_way = get_way;
	}
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	public String getRecipe_way() {
		return recipe_way;
	}
	public void setRecipe_way(String recipe_way) {
		this.recipe_way = recipe_way;
	}
	public int getPlus_point() {
		return plus_point;
	}
	public void setPlus_point(int plus_point) {
		this.plus_point = plus_point;
	}
	
}