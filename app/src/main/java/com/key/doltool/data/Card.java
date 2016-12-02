package com.key.doltool.data;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Card")
public class Card {
    @Column(autoincrement=true,primary=true)
    public int id;
    @Column(name="name")
    public String name;
    @Column(name="point")
    public int point;
    @Column(name="type")
    public String type;
    @Column(name="flag")
    public int flag;
    @Override
    public boolean equals(Object o) {
        Card c=(Card)o;
        return c.name.equals(name);
    }
}
