package com.key.doltool.app.util;

import android.content.Context;
import android.content.Intent;

import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表流程辅助
 * Created by key on 2016/12/22.
 */
public class ListFlowHelper<T> {
    private SRPUtil dao;
    public List<T> list=new ArrayList<>();
    public boolean end_flag=true; //是否为最末标记
    private int add=0;
    private int size=0;
    //查询条件
    private String select_if="id>?";
    private List<String> select_if_x=new ArrayList<>();
    private String order=null;
    private Class<T> itemClass;
    private ListFlowCallBack listFlowCallBack;
    private ListScrollListener listScrollListener;
    public interface ListFlowCallBack{
        void showSelectToast(String msg);
        void setAdapter();
        void updateAdapter();
    }
    public ListFlowHelper(Class<T> itemClass,Context context,ListFlowCallBack listFlowCallBack,int size){
        dao=SRPUtil.getInstance(context);
        this.itemClass=itemClass;
        this.listFlowCallBack=listFlowCallBack;
        this.size=size;
        select_if_x.add("0");
    }

    public ListFlowHelper(Class<T> itemClass,Context context,ListFlowCallBack listFlowCallBack,int size,String order){
        dao=SRPUtil.getInstance(context);
        this.itemClass=itemClass;
        this.listFlowCallBack=listFlowCallBack;
        this.size=size;
        this.order=order;
        select_if_x.add("0");
    }

    public void setListScrollListener(ListScrollListener listScrollListener){
        this.listScrollListener=listScrollListener;
    }

    /**设置来源值**/
    public void getExtra(Intent intent) {
        if (!StringUtil.isNull(intent.getStringExtra("type"))) {
            select_if = intent.getStringExtra("type");
            select_if_x.clear();
            select_if_x.add(intent.getStringExtra("args"));
        }
    }

    /**查询流**/
    public void selectshow(String limit){
        //数据前后记录
        int size_before,size_after;
        size_before=list.size();
        list.addAll(dao.select(itemClass, false, select_if,StringUtil.listToArray(select_if_x),
                null,null,order,limit));
        size_after=list.size();
        //数据返回判断
        if(size_after==size){
            end_flag=true;
            listScrollListener.changeFlag(true);
        }else if(size_after<size){
            //表示，小于
            end_flag=false;
            listScrollListener.changeFlag(false);
        }else if(size_after==size_before&&size_after!=0){
            end_flag=false;
            listScrollListener.changeFlag(false);
            listFlowCallBack.showSelectToast("已经返回所有查询结果了");
        }else if(size_after==0){
            listFlowCallBack.showSelectToast("没有查到您想要的结果");
        }
    }

    //修改查询条件
    public void change_if(String if_s,String if_args){
        //初始化所有数据
        select_if=if_s;
        select_if_x.clear();
        select_if_x.add(if_args);
        list.clear();
        add=0;
        selectshow("0,"+size);
        //重新setAdapter
        listFlowCallBack.setAdapter();
    }

    //修改查询条件
    public void change_if(String if_s,List<String> if_args){
        //初始化所有数据
        select_if=if_s;
        select_if_x=if_args;
        list.clear();
        add=0;
        selectshow("0,"+size);
        //重新setAdapter
        listFlowCallBack.setAdapter();
    }

    public void change(){
        add+=size;
        selectshow(add+","+	size);
        listFlowCallBack.updateAdapter();
    }

    public void change(boolean start){
        if(start){
            add=-size;
        }
        add+=size;
        selectshow(add+","+	size);
        listFlowCallBack.updateAdapter();
    }

    public boolean isChange(){
        return select_if.equals("id>?");
    }

    public void reback(){
        //条件不是初始状态就重置
        if(!select_if.equals("id>?")){
            end_flag=true;
            listScrollListener.changeFlag(true);
            change_if("id>?","0");
            listFlowCallBack.showSelectToast("重置搜索条件");
        }
    }
}
