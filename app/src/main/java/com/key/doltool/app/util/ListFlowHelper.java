package com.key.doltool.app.util;

import android.content.Context;

import com.key.doltool.adapter.WikiAdapter;
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
    private String[] select_if_x={"0"};
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
    }

    public ListFlowHelper(Class<T> itemClass,Context context,ListFlowCallBack listFlowCallBack,int size,String order){
        dao=SRPUtil.getInstance(context);
        this.itemClass=itemClass;
        this.listFlowCallBack=listFlowCallBack;
        this.size=size;
        this.order=order;
    }

    public void setListScrollListener(ListScrollListener listScrollListener){
        this.listScrollListener=listScrollListener;
    }
    /**查询流**/
    public void selectshow(String limit){
        //数据前后记录
        int size_before,size_after;
        size_before=list.size();
        list.addAll(dao.select(itemClass, false, select_if, select_if_x,
                null,null,order,limit));
        size_after=list.size();
        //数据返回判断
        if(size_after< WikiAdapter.SIZE){
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
        select_if_x=new String[1];
        select_if_x[0]=if_args;
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
        select_if_x=new String[if_args.size()];
        for(int i=0;i<select_if_x.length;i++){
            select_if_x[i]=if_args.get(i);
        }
        list.clear();
        add=0;
        selectshow("0,"+size);
        //重新setAdapter
        listFlowCallBack.setAdapter();
    }

    //重置最末尾标记
    public void begin(){
        end_flag=true;
        listScrollListener.changeFlag(true);
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
