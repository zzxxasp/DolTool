package com.key.doltool.activity.adc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.data.sqlite.ADCInfo;
import com.key.doltool.event.AreaEvent;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ADCListActivity extends BaseActivity {
    //定义部分
    private Dialog alert;
    //列表
    @BindView(R.id.listview) ListView listview;
    //数据temp变量
    private DefaultDAO dao;
    private List<ADCInfo> list = new ArrayList<>();
    private ADCListAdapter adapter;
    private int add = 0;
    private boolean end_flag = true; //是否为最末标记
    private ListScrollListener scrollListener;
    //查询条件
    private String select_if = "id>?";
    private String[] select_if_x = {"0"};

    @Override
    public int getContentViewId() {
        return R.layout.card_combo_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        dao = SRPUtil.getDAO(this);
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle(R.string.adc_title);
        getExtra();//外部搜索链接参数处理
        findView();
        setListener();
        selectshow("0," + ADCListAdapter.SIZE);
    }

    private void getExtra() {
        if (!StringUtil.isNull(getIntent().getStringExtra("type"))) {
            select_if = getIntent().getStringExtra("type");
            select_if_x[0] = getIntent().getStringExtra("args");
        }
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
                it.putExtra("id", list.get(arg2).getId() + "");
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
        adapter = new ADCListAdapter(list, this);
        scrollListener = new ListScrollListener(end_flag, alert, handler);
        listview.setOnScrollListener(scrollListener);
        listview.setAdapter(adapter);
    }

    protected void onDestroy() {
        dao = null;
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("unchecked")
    //有限数据查询
    private void selectshow(String limit) {
        if (dao == null) {
            return;
        }
        //数据前后记录
        int size_before,size_after;
        size_before = list.size();
        list.addAll(((List<ADCInfo>) dao.select(ADCInfo.class, false, select_if, select_if_x,
                null, null, null, limit)));
        size_after = list.size();
        //数据返回判断
        if (size_after < ADCListAdapter.SIZE) {
            //表示，小于
            end_flag = false;
            scrollListener.changeFlag(false);
        }
        if (size_after == size_before && size_after != 0) {
            end_flag = false;
            scrollListener.changeFlag(false);
            Toast.makeText(getApplicationContext(),R.string.search_no_more, Toast.LENGTH_LONG).show();
        } else if (size_after == 0) {
            Toast.makeText(getApplicationContext(),R.string.search_no, Toast.LENGTH_LONG).show();
        }
    }

    //数据添加
    private void change() {
        add += ADCListAdapter.SIZE;
        selectshow(add + "," + ADCListAdapter.SIZE);
        adapter.notifyDataSetChanged();
    }

    private void jump() {
        View xc = getLayoutInflater().inflate(R.layout.select_adc, null);
        ViewUtil.popADCDialog(this, xc);
    }

    private void findObject() {
        new AreaEvent().showCityDialog(this, dao);
    }

    //修改查询条件
    public void change_if(String if_s, String if_args) {
        //初始化所有数据
        select_if = if_s;
        select_if_x = new String[1];
        select_if_x[0] = if_args;
        list.clear();
        add = 0;
        selectshow("0," + ADCListAdapter.SIZE);
        //重新setAdapter
        adapter = new ADCListAdapter(list, this);
        listview.setAdapter(adapter);
    }

    //修改查询条件
    public void change_if(String if_s, List<String> if_args) {
        //初始化所有数据
        select_if = if_s;
        select_if_x = new String[if_args.size()];
        for (int i = 0; i < select_if_x.length; i++) {
            select_if_x[i] = if_args.get(i);
        }
        list.clear();
        add = 0;
        selectshow("0," + ADCListAdapter.SIZE);
        //重新setAdapter
        adapter = new ADCListAdapter(list, this);
        listview.setAdapter(adapter);
    }

    //重置最末尾标记
    public void begin() {
        end_flag = true;
        scrollListener.changeFlag(true);
    }

    /**
     * 华丽的分割线——以下是Handler,线程,系统按键等处理
     */
    //Handler——线程结束后更新界面
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            change();
            alert.dismiss();
        }
    };

    //系统按键监听覆写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按键返回
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //条件不是初始状态就重置
            if (!select_if.equals("id>?")) {
                end_flag = true;
                scrollListener.changeFlag(true);
                change_if("id>?", "0");
                Toast.makeText(getApplicationContext(),R.string.search_rest, Toast.LENGTH_SHORT).show();
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
