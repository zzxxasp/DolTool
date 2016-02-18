package com.key.doltool.data.sqlite;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

/**定期船**/
@Table(name="RegularShip")
public class RegularShip {
    @Column(autoincrement=true,primary=true)
    public int id;
    @Column(name="name")
    public String name;
    @Column(name="start_city")
    public String start_city;
    @Column(name="end_city")
    public String end_city;
    @Column(name="time_list")
    public String time_list;
    @Column(name="money")
    public String money;
    @Column(name="time")
    public String time;
}
