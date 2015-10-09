package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Deck")
public class Deck {
    @Column(autoincrement=true,primary=true)
    //id
    private int id;
    @Column(name="name")
    //名称
    public String name;
    @Column(name="value")
    //价值
    public int value;
    @Column(name="card_list")
    //id,id,id（卡片列表）
    public String card_list;
    @Column(name="limit")
    //限制类型
    public String limit;
}
