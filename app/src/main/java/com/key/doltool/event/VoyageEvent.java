package com.key.doltool.event;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.activity.squre.MapActivity;
import com.key.doltool.activity.trade.TradeItemActivity;
import com.key.doltool.data.VoyageInfo;
import com.key.doltool.data.VoyageItem;
import com.key.doltool.util.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class VoyageEvent {
    private Context context;
    private static final int WEEK=1000*60*60*24*6;
    public static String[] name={
        "世界地图","交易品","纺织品",
        "钓鱼","画廊","魔法之物",
        "阀门","牛之章","猪之章",
        "禽之章","委托任务","航海学校",
    };
    public static String[] pic_id={
        "ic_map","ic_tradeitem","ic_roll",
        "ic_fishing","ic_gallery","ic_wizard",
        "ic_steam","ic_cow","ic_pig",
        "ic_duck","ic_diploma","ic_graduation",
    };
    /**
     * 1：核心跳转
     * 2：核心内容限定查询显示
     * 3：单一内容详情显示
     * 4：单独说明web页面
     * 5：独立原生界面
     * **/
    public static int[] type={
         1,1,2,
         1,1,1,
         1,1,1,
         1,1,1,
    };
    public static String[] value={
            "","","",
            "","","",
            "","","",
            "","","",
    };

    public VoyageEvent(Context context){
        this.context=context;
    }

    public void getRandomChance(){
        //获得出现的总数
        VoyageInfo info=new VoyageInfo(context);
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate=new Date(System.currentTimeMillis());
        Date before;
        String time=formatter.format(curDate);
        try{
            before=formatter.parse(info.getWeek());
        }catch (Exception e){
            e.printStackTrace();
            before=new Date(System.currentTimeMillis());
        }
        if(curDate.getTime()-before.getTime()>=WEEK) {
            int random_times = 9;
            Set<Integer> set = new HashSet<>();
            Set<Integer> list_type = new HashSet<>();
            System.out.println(random_times);
            while (list_type.size() < random_times) {
                int temp = list_type.size();
                //获得本次出现的type是
                int random_type = NumberUtil.getRandom(0, name.length);
                list_type.add(random_type);
                //如果加入的类型之前存在则不存入
                if (temp != list_type.size()) {
                    set.add(random_type);
                }
            }
            Gson gson = new Gson();
            info.setWeek(time);
            info.setData(gson.toJson(set));
            System.out.println("set:" +gson.toJson(set));
        }
    }
    public static List<VoyageItem> getItemByString(String str){
        List<VoyageItem> list=new ArrayList<>();
        Gson gson = new Gson();
        List<Integer> set=gson.fromJson(str, new TypeToken<List<Integer>>() {
        }.getType());
        if(set!=null){
            for(int i=0;i<set.size();i++){
                VoyageItem item=new VoyageItem();
                item.name=name[set.get(i)];
                item.pic_id=pic_id[set.get(i)];
                item.type=type[set.get(i)];
                list.add(item);
            }
        }
        return list;
    }
    public static void jumpForVoyage(Context context,VoyageItem item){
        Class<?> c;
        Intent it=null;
        switch(item.name){
            case "世界地图":
                c=MapActivity.class;
                it=new Intent(context,c);
                break;
            case "交易品":
                c=TradeItemActivity.class;
                it=new Intent(context,c);
                break;
        }
        if(it!=null){
            context.startActivity(it);
        }
    }
}