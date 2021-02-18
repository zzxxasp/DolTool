package com.key.doltool.event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.activity.adventure.card.CardListActivity;
import com.key.doltool.activity.mission.MissonListActivity;
import com.key.doltool.activity.recipe.RecipeForBookDetailsActivity;
import com.key.doltool.activity.squre.MapActivity;
import com.key.doltool.activity.squre.PortActivity;
import com.key.doltool.activity.useitem.UseItemShowListActivity;
import com.key.doltool.activity.voyage.TradeItemActivity;
import com.key.doltool.activity.voyage.fishing.FishingActivity;
import com.key.doltool.activity.wiki.WikiMainActivity;
import com.key.doltool.data.VoyageInfo;
import com.key.doltool.data.VoyageItem;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.view.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 发现界面生成相关规则和资源
 * **/
public class VoyageEvent {
    private Context context;
    private static final int WEEK=1000*60*60*24;
    public static String[] name={
        "世界地图","交易品","纺织品",
        "钓鱼","论战卡组","魔法之物",
        "牛之章","猪之章", "禽之章",
        "委托任务","航海学校", "定期船","羊之章"
    };
    public static String[] pic_id={
        "ic_map","ic_tradeitem","ic_roll",
        "ic_fishing","ic_gallery","ic_wizard",
        "ic_cow","ic_pig","ic_duck",
        "ic_diploma","ic_graduation","ic_cargo_ship","ic_sheep"
    };
    public static int[] type={
            0,0,0,
            0,0,0,
            1,1,1,
            0,2,0,1
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
        Log.i("WEEK",""+WEEK);
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
        Intent it;
        switch(item.name){
            case "世界地图":
                c=MapActivity.class;
                it=new Intent(context,c);
                break;
            case "交易品":
                c=TradeItemActivity.class;
                it=new Intent(context,c);
                break;
            case "委托任务":
                c= MissonListActivity.class;
                it=new Intent(context,c);
                break;
            case "论战卡组":
                c= CardListActivity.class;
                it=new Intent(context,c);
                break;
            case "纺织品":
                c=TradeItemActivity.class;
                it=new Intent(context,c);
                it.putExtra("type","type=?");
                it.putExtra("args","纺织品");
                break;
            case "钓鱼":
                c=FishingActivity.class;
                it=new Intent(context,c);
                break;
            case "定期船":
                c= PortActivity.class;
                it=new Intent(context,c);
                break;
            case "魔法之物":
                c=UseItemShowListActivity.class;
                it=new Intent(context,c);
                it.putExtra("type","魔法之物");
                break;
            default:
                if(item.type==1){
                    it=recipeSp(context,item.name);
                }else if(item.type==2){
                    it=wikiSp(context,item.name);
                }else{
                    it=null;
                }
                break;
        }
        if(it!=null){
            context.startActivity(it);
        }else{
            Toast.makeText(context.getApplicationContext(),"专题界面/快捷查询正在制作中",Toast.LENGTH_SHORT).show();
        }
    }
    private static Intent wikiSp(Context context,String name){
        Intent it=new Intent(context, WikiMainActivity.class);
        if(name.equals("航海学校")){
            it.putExtra("id","10");
            return it;
        }
        return null;
    }

    private static Intent recipeSp(Context context,String name){
        Intent it=new Intent(context, RecipeForBookDetailsActivity.class);
        if(name.equals("牛之章")){
            it.putExtra("id","215");
            return it;
        }
        if(name.equals("禽之章")){
            it.putExtra("id","213");
            return it;
        }
        if(name.equals("猪之章")){
            it.putExtra("id","214");
            return it;
        }
        if(name.equals("羊之章")){
            it.putExtra("id","212");
            return it;
        }
        return null;
    }
}
