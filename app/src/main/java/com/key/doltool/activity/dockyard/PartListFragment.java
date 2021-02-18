package com.key.doltool.activity.dockyard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.PartListAdapter;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.rx.RxBusEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.view.Toast;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 *
 * Created by Administrator on 2016/12/16.
 */
public class PartListFragment extends BaseFragment {
    @BindView(R.id.listview)ListView listview;
    private PartListAdapter adapter;
    private ListFlowHelper<Part> listFlowHelper;
    private ViewHandler viewHandler;
    private Dialog alert;
    private Disposable subscription;
    public int getContentViewId() {
        return R.layout.dockyard_main_item_layout1;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initData();
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBusEvent.get().unregister(RxBusEvent.SAILMENU);
        if(subscription!=null){
            subscription.dispose();
        }
    }

    private void initData(){
        Observable<MenuItem> menuItemObservable = RxBusEvent.get().register(RxBusEvent.SAILMENU);
        subscription=menuItemObservable.subscribe(new Consumer<MenuItem>() {
            @Override
            public void accept(MenuItem item) {
                if(item.index==2){
                    findObject();
                }
            }
        });

        //初始化流程
        listFlowHelper=new ListFlowHelper<>(Part.class, context, new ListFlowHelper.ListFlowCallBack() {
            @Override
            public void showSelectToast(String msg) {
                Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void setAdapter() {
                adapter=new PartListAdapter(listFlowHelper.list,context);
                listview.setAdapter(adapter);
            }

            @Override
            public void updateAdapter() {
                adapter.notifyDataSetChanged();
            }
        }, SailBoatListAdapter.SIZE);

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
                listFlowHelper.change();
                DialogUtil.dismiss(context,alert);
            }
        });

        ListScrollListener listScrollListener = new ListScrollListener(alert, viewHandler, context);
        adapter=new PartListAdapter(listFlowHelper.list,getActivity());
        listview.setOnScrollListener(listScrollListener);
        listFlowHelper.setListScrollListener(listScrollListener);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent=new Intent(getActivity(),PartActivity.class);
                intent.putExtra("id",adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }

    private void findObject(){
        //弹出对话框
        LayoutInflater mInflater=getActivity().getLayoutInflater();
        ViewUtil.popPartDialog(listFlowHelper,context,mInflater.inflate(R.layout.select_part, null));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
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
            message1.what = 1;
            viewHandler.sendMessage(message1);
        }
    };
}
