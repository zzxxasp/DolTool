package com.key.doltool.event;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.data.TradeCityItem;
import com.key.doltool.data.sqlite.City;
import com.key.doltool.util.db.SRPUtil;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/12/2.
 */
public class DataUpdateEvent {
    private Context context;

    public DataUpdateEvent(Context context){
        this.context=context;
    }
    /**更新城市-交易品信息**/
    public void updateCityTradeItem(){
        AVQuery<AVObject> p=new AVQuery<>("trade");
        p.whereEqualTo("flag",0);
        p.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list.size() != 0) {
                    for(int i=0;i<list.size();i++){
                        AVObject object=list.get(i);
                        object.put("flag", 1);
                        object.saveInBackground();
                        City city=selectCity(object);
                        if(city!=null){
                            SRPUtil.getDAO(context).update(city,
                                    new String[]{"trade_list"},"name=?",new String[]{city.getName()});
                        }
                    }
                }
            }
        });
    }

    private City selectCity(AVObject object){
        boolean bool=true;
        Gson g=new Gson();
        List<City> list=SRPUtil.getInstance(context).select(City.class, false, "name=?", new String[]{object.getString("name")}, null, null, null, null);
        if(list.size()==1){
            City item=list.get(0);
            List<TradeCityItem> temp=g.fromJson(item.getTrade_list(), new TypeToken<List<TradeCityItem>>() {
            }.getType());
            for(int i=0;i<temp.size();i++){
                if(temp.get(i).name.equals(object.getString("trade_name"))){
                    TradeCityItem item1=new TradeCityItem();
                    item1.name=object.getString("trade_name");
                    item1.price=object.getString("price");
                    item1.invest=object.getString("invest");
                    temp.set(i, item1);
                    bool=false;
                    break;
                }
            }
            if(bool){
                TradeCityItem item1=new TradeCityItem();
                item1.name=object.getString("trade_name");
                item1.price=object.getString("price");
                item1.invest=object.getString("invest");
                temp.add(item1);
            }
            item.setTrade_list(g.toJson(temp));
            return item;
        }else{
            return null;
        }
    }
}
