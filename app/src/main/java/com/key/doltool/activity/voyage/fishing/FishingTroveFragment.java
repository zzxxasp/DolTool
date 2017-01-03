package com.key.doltool.activity.voyage.fishing;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.mission.MissionDetailsActivity;
import com.key.doltool.adapter.TroveAdapter;
import com.key.doltool.data.sqlite.Trove;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

import java.util.List;

import butterknife.BindView;

public class FishingTroveFragment extends BaseFragment {
    private List<Trove> list;
    //定义部分
    private Dialog alert;
    //船只列表页面
    @BindView(R.id.listview) GridView gridview;
    private TroveAdapter adapter;
    private DefaultDAO dao;
    private UpdataCount count;

    @Override
    public int getContentViewId() {
        return R.layout.trade_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        dao=SRPUtil.getDAO(context);
        findView();
        setListener();
        count=new UpdataCount(context);
        new Thread(mTask).start();
    }

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新页面
            alert.dismiss();
            Parcelable state=gridview.onSaveInstanceState();
            adapter=new TroveAdapter(list,context);
            gridview.setAdapter(adapter);
            gridview.onRestoreInstanceState(state);
        }
    };

    private Runnable mTask=new Runnable() {
        @Override
        public void run() {
            //查询所有钓鱼发现物
            list= SRPUtil.getInstance(context.getApplicationContext()).select(
                    Trove.class,false,"getWay=?",new String[]{"3"},null, null,"rate desc,feats desc", null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    };

    private void findView(){
        alert=new DialogEvent().showLoading(context);
        alert.show();
    }

    private void setListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(), MissionDetailsActivity.class);
                intent.putExtra("find_item", adapter.getItem(arg2).getName() + "");
                startActivity(intent);
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                if (adapter.getItem(position).getFind_flag() == 0) {
                    Trove trove = new Trove();
                    trove.setFind_flag(1);
                    dao.update(trove, new String[]{"flag"}, "id=?", new String[]{"" + adapter.getItem(position).getId()});
                    count.update_addMode("海洋生物", 1);
                } else {
                    Trove trove = new Trove();
                    trove.setFind_flag(0);
                    dao.update(trove, new String[]{"flag"}, "id=?", new String[]{"" + adapter.getItem(position).getId()});
                    count.update_addMode("海洋生物", -1);
                }
                if (!context.isFinishing()) {
                    alert.show();
                }
                new Thread(mTask).start();
                return true;
            }
        });
    }
}
