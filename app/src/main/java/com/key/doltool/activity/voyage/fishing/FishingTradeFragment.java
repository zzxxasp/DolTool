package com.key.doltool.activity.voyage.fishing;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.adapter.TradeListAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.db.SRPUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 钓鱼交易品界面
 * **/
public class FishingTradeFragment extends BaseFragment{

    @BindView(R.id.listview) GridView gridview;
    private Dialog alert;
    private List<TradeItem> list;
    private ViewHandler viewHandler;

    @Override
    public int getContentViewId() {
        return R.layout.trade_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                DialogUtil.dismiss(context,alert);
                Parcelable state=gridview.onSaveInstanceState();
                TradeListAdapter adapter = new TradeListAdapter(list, getActivity());
                gridview.setAdapter(adapter);
                gridview.onRestoreInstanceState(state);
            }
        });
        findView();
        setListener();
        new Thread(mTask).start();
    }


    private Runnable mTask=new Runnable() {
        @Override
        public void run() {
            //查询所有钓鱼交易品
            list= SRPUtil.getInstance(context.getApplicationContext()).select(
                    TradeItem.class,false,"sp=?",new String[]{"钓鱼"},null, null,"name desc", null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewHandler.sendMessage(viewHandler.obtainMessage());
        }
    };


    private void findView(){
        alert=new DialogEvent().showLoading(getActivity());
        DialogUtil.show(context,alert);
    }


    private void setListener(){
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(), TradeDetailActivity.class);
                it.putExtra("id", list.get(position).getId() + "");
                startActivity(it);
            }
        });
    }
}
