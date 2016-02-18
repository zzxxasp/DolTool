package com.key.doltool.activity.voyage.fishing;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.trade.TradeDetailActivity;
import com.key.doltool.adapter.TradeListAdapter;
import com.key.doltool.data.sqlite.TradeItem;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.db.SRPUtil;

import java.util.List;

public class FishingTradeFragment extends BaseFragment{
    private View main;
    //定义部分
    private Dialog alert;
    //船只列表页面
    private GridView gridview;
    private List<TradeItem> list;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.trade_list, container,false);
        init(view);
        findView();
        setListener();
        return view;
    }
    private void init(View view){
        main=view;
        new Thread(mTask).start();
    }

    private Runnable mTask=new Runnable() {
        @Override
        public void run() {
            //查询所有钓鱼发现物
            list= SRPUtil.getInstance(getActivity().getApplicationContext()).select(
                    TradeItem.class,false,"sp=?",new String[]{"钓鱼"},null, null,"name desc", null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    };
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新页面
            alert.dismiss();
            Parcelable state=gridview.onSaveInstanceState();
            TradeListAdapter adapter = new TradeListAdapter(list, getActivity());
            gridview.setAdapter(adapter);
            gridview.onRestoreInstanceState(state);
        }
    };

    private void findView(){
        alert=new DialogEvent().showLoading(getActivity());
        alert.show();
        gridview=(GridView)main.findViewById(R.id.listview);
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
