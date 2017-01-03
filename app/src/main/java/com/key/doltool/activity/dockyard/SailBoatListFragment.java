package com.key.doltool.activity.dockyard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SailBoat;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.view.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 船只列表界面
 * Created by Administrator on 2016/12/16.
 */
public class SailBoatListFragment extends BaseFragment{

    @BindView(R.id.listview)ListView listview;
    private SailBoatListAdapter adapter;
    private ViewHandler viewHandler;
    private Dialog alert;
    private ListFlowHelper<SailBoat> listFlowHelper;

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

    private void initData(){
        //初始化流程
        String order = "name desc";
        listFlowHelper=new ListFlowHelper<>(SailBoat.class, context, new ListFlowHelper.ListFlowCallBack() {
            @Override
            public void showSelectToast(String msg) {
                Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void setAdapter() {
                adapter=new SailBoatListAdapter(listFlowHelper.list,context);
                listview.setAdapter(adapter);
            }

            @Override
            public void updateAdapter() {
                adapter.notifyDataSetChanged();
            }
        }, SailBoatListAdapter.SIZE, order);

        //基本设置
        alert=new DialogEvent().showLoading(context);
        DialogUtil.show(context,alert);
        if(listFlowHelper.list.size()==0){
            new Thread(mTasks).start();
        }else{
            DialogUtil.dismiss(context,alert);
        }
    }

    private void setListener(){
        viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        listFlowHelper.change(true);
                        break;
                    case 1:
                        listFlowHelper.change();
                        break;
                }
                DialogUtil.dismiss(context,alert);
            }
        });

        ListScrollListener listScrollListener = new ListScrollListener(alert,viewHandler,context);
        adapter=new SailBoatListAdapter(listFlowHelper.list,getActivity());
        listview.setAdapter(adapter);

        listview.setOnScrollListener(listScrollListener);
        listFlowHelper.setListScrollListener(listScrollListener);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent=new Intent(getActivity(),SailBoatActivity.class);
                intent.putExtra("id",adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                Toast.makeText(context.getApplicationContext(),"挑选为造船白板", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(adapter.getItem(position));
                return true;
            }
        });
    }

    private void findObject(){
        //弹出对话框
        LayoutInflater mInflater=getActivity().getLayoutInflater();
        ViewUtil.popDialog(listFlowHelper,getActivity(),mInflater.inflate(R.layout.select_boat, null));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void menuEvent(MenuItem item){
        if(item.index==0){
            findObject();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //菜单键覆写，调用边缘栏菜单
        if(keyCode==KeyEvent.KEYCODE_MENU){
            return true;
        }
        //条件:当菜单未关闭且搜索条件为初始态，允许退出
        if(listFlowHelper.isChange()){
            return false;
        }else{
            //按键返回
            if(keyCode==KeyEvent.KEYCODE_BACK) {
                //条件不是初始状态就重置
                listFlowHelper.reback();
            }
        }
        return true;
    }

    private Runnable mTasks =new Runnable(){
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message1 = new Message();
            message1.what = 0;
            viewHandler.sendMessage(message1);
        }
    };
}
