package com.key.doltool.data.parse;

import android.os.Parcel;

import cn.leancloud.AVObject;
import cn.leancloud.annotation.AVClassName;


/**钓场分享**/
@AVClassName("FishingPort")
public class FishingPort extends AVObject {
    public FishingPort(){
        super();
    }

    // 钓场的名称
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    //地理位置-渔场坐标记录
    public String getGeo() {
        return getString("geoPoint");
    }

    public void setGeo(String geoPoint) {
        put("geoPoint",geoPoint);
    }

    //钓场能钓的鱼种
    public String getFishingList() {
        return getString("fishList");
    }

    public void setFishingList(String fishList) {
        put("fishList",fishList);
    }

    //创建者
    public String getUserName() {
        return getString("userName");
    }

    public void setUserName(String userName) {
        put("userName",userName);
    }
}
