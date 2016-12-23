package com.key.doltool.activity.dockyard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.adapter.PartListAdapter;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 * Created by Administrator on 2016/12/16.
 */
public class PartListFragment extends BaseFragment {
    @BindView(R.id.listview)ListView listview;
    private PartListAdapter adapter;
    private List<Part> list=new ArrayList<>();
    private SRPUtil dao;
    private boolean end_flag=true;
    private int add=-20;
    private String select_if="id>?";
    private String[] select_if_x={"0"};
    private ViewHandler viewHandler;
    private Dialog alert;
    private ListScrollListener listScrollListener;
    public int getContentViewId() {
        return R.layout.dockyard_main_item_layout1;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initData();
        setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void menuEvent(MenuItem item){
        if(item.index==2){
            findObject();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initData(){
        dao=SRPUtil.getInstance(context);
        alert=new DialogEvent().showLoading(getActivity());
        alert.show();
        if(list.size()==0){
            new Thread(mTasks).start();
        }else{
            alert.dismiss();
        }
    }

    private void setListener(){
        viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                change();
                alert.dismiss();
            }
        });

        listScrollListener=new ListScrollListener(end_flag,alert,viewHandler);
        adapter=new PartListAdapter(list,getActivity());
        listview.setOnScrollListener(listScrollListener);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent=new Intent(getActivity(),PartActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void begin(){
        end_flag=true;
    }

    private void selectshow(String limit){
        if(dao==null){
            return;
        }
        //数据前后记录
        int size_before,size_after;
        size_before=list.size();
        String order = "name desc";
        list.addAll(dao.select(Part.class, false,select_if, select_if_x,
                null, null, order,limit));
        size_after=list.size();
        //数据返回判断
        if(size_after==size_before&&size_after!=0) {
            end_flag=false;
            listScrollListener.changeFlag(false);
            Toast.makeText(context.getApplicationContext(),"已经返回所有查询结果了", Toast.LENGTH_LONG).show();
        }else if(size_after==0){
            Toast.makeText(context.getApplicationContext(),"没有查到您想要的结果", Toast.LENGTH_LONG).show();
        }
    }

    private void change(){
        //1.为船只信息，2.为配件信息
        add+=PartListAdapter.SIZE;
        selectshow(add+","+	PartListAdapter.SIZE);
        adapter.notifyDataSetChanged();
    }

    private void findObject(){
        //弹出对话框
        LayoutInflater mInflater=getActivity().getLayoutInflater();
        ViewUtil.popDialog(this,mInflater.inflate(R.layout.select_part, null));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //菜单键覆写，调用边缘栏菜单
        if(keyCode==KeyEvent.KEYCODE_MENU){
            return true;
        }
        //条件:当菜单未关闭且搜索条件为初始态，允许退出
        if(select_if.equals("id>?")){
            return false;
        }else{
            //按键返回
            if(keyCode==KeyEvent.KEYCODE_BACK) {
                //条件不是初始状态就重置
                if(!select_if.equals("id>?")){
                    end_flag=true;
                    change_if("id>?","0");
                    Toast.makeText(context.getApplicationContext(),"重置搜索条件", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    //修改查询条件
    public void change_if(String if_s,String if_args){
        //初始化所有数据
        select_if=if_s;
        select_if_x=new String[1];
        select_if_x[0]=if_args;
        list.clear();
        add=0;
        selectshow("0,"+PartListAdapter.SIZE);
        //重新setAdapter
        adapter=new PartListAdapter(list,getActivity());
        listview.setAdapter(adapter);
    }

    public void change_if(String if_s,List<String> if_args){
        //初始化所有数据
        select_if=if_s;
        select_if_x=new String[if_args.size()];
        for(int i=0;i<select_if_x.length;i++){
            select_if_x[i]=if_args.get(i);
        }
        list.clear();
        add=0;
        selectshow("0,"+PartListAdapter.SIZE);
        //重新setAdapter
        adapter=new PartListAdapter(list,getActivity());
        listview.setAdapter(adapter);
    }

    private Runnable mTasks =new Runnable(){
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message1 = new Message();
            message1.what = 1;
            viewHandler.sendMessage(message1);
        }
    };
}
