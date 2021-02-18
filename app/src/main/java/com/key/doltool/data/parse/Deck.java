package com.key.doltool.data.parse;

import cn.leancloud.AVObject;
import cn.leancloud.annotation.AVClassName;

@AVClassName("CardShare")
public class Deck extends AVObject {
    public Deck(){
        super();
    }
    // 名称
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }
    //价值
    public int getValue() {
        return getInt("value");
    }

    public void setValue(int value) {
        put("value",value);
    }
    //id,id,id（卡片列表）
    public String getCard_list() {
        return getString("cardList");
    }

    public void setCard_list(String card_list) {
        put("cardList",card_list);
    }
    //限制类型
    public String getLimit() {
        return getString("limit");
    }

    public void setLimit(String limit) {
        put("limit",limit);
    }
    //创建者
    public String getUserName() {
        return getString("userName");
    }

    public void setUserName(String userName) {
        put("userName",userName);
    }
}
