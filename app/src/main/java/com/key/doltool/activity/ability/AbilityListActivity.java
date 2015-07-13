package com.key.doltool.activity.ability;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.MenuItem;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SkillAdapter;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.data.Skill;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

public class AbilityListActivity extends BaseActivity {
    //定义部分
    private LinearLayout layout_alert;
    //列表
    private ListView listview;

    //数据temp变量
    private DefaultDAO dao;
    private List<Skill> list = new ArrayList<>();
    private SkillAdapter adapter;
    private int add = 0;
    private Thread mThread;    // 线程
    private boolean end_flag = true; //是否为最末标记
    //查询条件
    private String select_if = "id>?";
    private String[] select_if_x = {"0"};
    //创建Activity
    private ListScrollListener srollListener;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_combo_main);
        dao = SRPUtil.getDAO(this);
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle("技能锻炼场");
        getExtra();//外部搜索链接参数处理
        findView();
        setListener();
        selectshow("0," + SkillAdapter.SIZE);
    }

    private void getExtra() {
        if (!StringUtil.isNull(getIntent().getStringExtra("type"))) {
            select_if = getIntent().getStringExtra("type");
            select_if_x[0] = getIntent().getStringExtra("args");
        }
    }

    //通用findView
    private void findView() {
        layout_alert = (LinearLayout) findViewById(R.id.layout_alert);
        initPage();
    }

    //通用Listener
    private void setListener() {
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                if (list.get(arg2).getType() <= 5) {
                    Intent it = new Intent(AbilityListActivity.this, AbilityForNormalDetailActivity.class);
                    it.putExtra("id", list.get(arg2).getId() + "");
                    startActivity(it);
                } else {
                    Intent it = new Intent(AbilityListActivity.this, AbilityForBoatDetailActivity.class);
                    it.putExtra("id", list.get(arg2).getId() + "");
                    startActivity(it);
                }
            }
        });
    }

    private void initPage() {
        initPageItem();
    }

    private void initPageItem() {
        listview = (ListView) findViewById(R.id.listview);
        adapter = new SkillAdapter(list, this);
        srollListener = new ListScrollListener
                (end_flag, mThread, layout_alert, handler);
        listview.setOnScrollListener(srollListener);
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
        int size_before, size_after;
        size_before = list.size();
        list.addAll(((List<Skill>) dao.select(Skill.class, false, select_if, select_if_x,
                null, null, null, limit)));
        size_after = list.size();
        //数据返回判断
        if (size_after < SkillAdapter.SIZE) {
            //表示，小于
            end_flag = false;
            srollListener.changeFlag(false);
        }
        if (size_after == size_before && size_after != 0) {
            end_flag = false;
            srollListener.changeFlag(false);
            Toast.makeText(getApplicationContext(), "已经返回所有查询结果了", Toast.LENGTH_LONG).show();
        } else if (size_after == 0) {
            Toast.makeText(getApplicationContext(), "没有查到您想要的结果", Toast.LENGTH_LONG).show();
        }
    }

    //数据添加
    private void change() {
        add += SkillAdapter.SIZE;
        selectshow(add + "," + SkillAdapter.SIZE);
        adapter.notifyDataSetChanged();
    }

    //修改查询条件
    public void change_if(String if_s, String if_args) {
        //初始化所有数据
        select_if = if_s;
        select_if_x = new String[1];
        select_if_x[0] = if_args;
        list.clear();
        add = 0;
        selectshow("0," + SkillAdapter.SIZE);
        //重新setAdapter
        adapter = new SkillAdapter(list, this);
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
        selectshow("0," + SkillAdapter.SIZE);
        //重新setAdapter
        adapter = new SkillAdapter(list, this);
        listview.setAdapter(adapter);
    }

    //重置最末尾标记
    public void begin() {
        end_flag = true;
        srollListener.changeFlag(true);
    }

    /**
     * 华丽的分割线——以下是Handler,线程,系统按键等处理
     */
    //Handler——线程结束后更新界面
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            change();
            layout_alert.setVisibility(View.GONE);
        }
    };

    //系统按键监听覆写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按键返回
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //条件不是初始状态就重置
            if (!select_if.equals("id>?")) {
                end_flag = true;
                srollListener.changeFlag(true);
                change_if("id>?", "0");
                Toast.makeText(getApplicationContext(), "重置搜索条件", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.search:
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