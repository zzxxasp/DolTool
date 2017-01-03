package com.key.doltool.activity.adc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ADCListAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;

import butterknife.BindView;

public class ADCListActivity extends BaseActivity {
    //定义部分
    private Dialog alert;
    //列表
    @BindView(R.id.listview) ListView listview;
    //数据temp变量
    private ADCListAdapter adapter;
    private ListFlowHelper<ADCInfo> listFlowHelper;
    private ViewHandler viewHandler;
    @Override
    public int getContentViewId() {
        return R.layout.card_combo_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        listFlowHelper=new ListFlowHelper<>(ADCInfo.class, context, new ListFlowHelper.ListFlowCallBack() {
            @Override
            public void showSelectToast(String msg) {
                Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void setAdapter() {
                adapter=new ADCListAdapter(listFlowHelper.list,context);
                listview.setAdapter(adapter);
            }

            @Override
            public void updateAdapter() {
                adapter.notifyDataSetChanged();
            }
        }, ADCListAdapter.SIZE);
        viewHandler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                listFlowHelper.change();
                DialogUtil.dismiss(context,alert);
            }
        });
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle(R.string.adc_title);

        findView();
        setListener();

        listFlowHelper.getExtra(getIntent());//外部搜索链接参数处理
        listFlowHelper.selectshow("0," + ADCListAdapter.SIZE);
    }


    //通用findView
    private void findView() {
        alert=new DialogEvent().showLoading(this);
        initPage();
    }

    //通用Listener
    private void setListener() {
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                Intent it = new Intent(ADCListActivity.this, ADCDetailsActivity.class);
                it.putExtra("id", adapter.getItem(arg2).getId() + "");
                startActivity(it);
            }
        });
    }

    private void initPage() {
        //初始化边缘栏
        initPageItem();
    }

    private void initPageItem() {
        listview = (ListView) findViewById(R.id.listview);
        adapter = new ADCListAdapter(listFlowHelper.list, this);
        ListScrollListener scrollListener = new ListScrollListener(alert, viewHandler, context);
        listFlowHelper.setListScrollListener(scrollListener);
        listview.setOnScrollListener(scrollListener);
        listview.setAdapter(adapter);
    }


    private void jump() {
        View xc = getLayoutInflater().inflate(R.layout.select_adc, null);
        ViewUtil.popADCDialog(listFlowHelper,context,xc);
    }

    private void findObject() {
        new AreaEvent().showADCCityDialog(listFlowHelper,context);
    }


    //系统按键监听覆写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按键返回
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
                case R.id.city_search:
                    findObject();
                    break;
                case R.id.type_search:
                    jump();
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adc_menu, menu);
        return true;
    }
}
