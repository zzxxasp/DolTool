package com.key.doltool.data.item;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="UseItem")
public class UseItem {
    @Column(autoincrement=true,primary=true)
    public int id;
    /**名称**/
    @Column(name="name")
    public String name;
    /**描述**/
    @Column(name="info")
    public String info;
    /**食物，灾难回避，陆战道具，海战道具，地下城冒险，魔法**/
    @Column(name="type")
    public String type;
    /**获取方式|战斗获得外**/
    @Column(name="get_way")
    public String get_way;
}
