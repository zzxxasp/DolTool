package com.key.doltool.activity.job;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.JobAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.Job;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.view.Toast;

import butterknife.BindView;

public class JobListActivity extends BaseActivity {

    @BindView(R.id.listview) ListView listview;
    //定义部分
    private Dialog alert;
    //数据temp变量
    private JobAdapter adapter;
    private ListFlowHelper<Job> listFlowHelper;
    @Override
    public int getContentViewId() {
        return R.layout.card_combo_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        findView();
        setListener();
        listFlowHelper.getExtra(getIntent());//外部搜索链接参数处理
        listFlowHelper.selectshow("0," + JobAdapter.SIZE);
    }


    //通用findView
    private void findView() {
        listFlowHelper=new ListFlowHelper<>(Job.class, context, new ListFlowHelper.ListFlowCallBack() {
            @Override
            public void showSelectToast(String msg) {
                Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void setAdapter() {
                adapter=new JobAdapter(listFlowHelper.list,context);
                listview.setAdapter(adapter);
            }

            @Override
            public void updateAdapter() {
                adapter.notifyDataSetChanged();
            }
        }, JobAdapter.SIZE);

        flag = false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle("职业介绍所");
        alert=new DialogEvent().showLoading(this);
        initPage();
    }

    //通用Listener
    private void setListener() {
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                Intent it = new Intent(JobListActivity.this, JobDetailsActivity.class);
                it.putExtra("id", adapter.getItem(arg2).getId() + "");
                startActivity(it);
            }
        });
    }

    private void initPage() {
        initPageItem();
    }

    private void initPageItem() {
        listview = (ListView) findViewById(R.id.listview);
        adapter = new JobAdapter(listFlowHelper.list, this);
        ViewHandler handler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                listFlowHelper.change();
                DialogUtil.dismiss(context,alert);
            }
        });
        ListScrollListener scrollListener = new ListScrollListener(alert,handler,this);
        listFlowHelper.setListScrollListener(scrollListener);
        listview.setOnScrollListener(scrollListener);
        listview.setAdapter(adapter);
    }

    private void jump() {
        View xc = getLayoutInflater().inflate(R.layout.select_job, null);
        ViewUtil.popJobDialog(listFlowHelper,context, xc);
    }


    //系统按键监听覆写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //条件:当菜单未关闭且搜索条件为初始态，允许退出
        if (listFlowHelper.isChange()) {
            super.onKeyDown(keyCode, event);
        } else {
            //按键返回
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //条件不是初始状态就重置
                listFlowHelper.reback();
            }
        }
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(android.view.MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.search:
                    jump();
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
