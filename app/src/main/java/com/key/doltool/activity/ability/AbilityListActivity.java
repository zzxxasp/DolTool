package com.key.doltool.activity.ability;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SkillAdapter;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 技能列表界面
 * **/
public class AbilityListActivity extends BaseActivity {
    //定义部分
    private Dialog alert;
    //列表
    @BindView(R.id.listview) ListView listview;
    //数据temp变量
    private SRPUtil dao;
    private List<Skill> list = new ArrayList<>();
    private SkillAdapter adapter;
    private int add = 0;
    private boolean end_flag = true; //是否为最末标记
    //查询条件
    private String select_if = "id>?";
    private String[] select_if_x = {"0"};
    //创建Activity
    private ListScrollListener srollListener;

    @Override
    public int getContentViewId() {
        return R.layout.card_combo_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        dao = SRPUtil.getInstance(getApplicationContext());
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle(R.string.skill_title);
        getExtra();//外部搜索链接参数处理
        findView();
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
        alert=new DialogEvent().showLoading(this);
        initPage();
    }
    @OnItemClick(R.id.listview) void itemClick(int position) {
        if (list.get(position).getType() <= 5) {
            Intent it = new Intent(AbilityListActivity.this, AbilityForNormalDetailActivity.class);
            it.putExtra("id", list.get(position).getId() + "");
            startActivity(it);
        } else {
            Intent it = new Intent(AbilityListActivity.this, AbilityForBoatDetailActivity.class);
            it.putExtra("id", list.get(position).getId() + "");
            startActivity(it);
        }
    }

    private void initPage() {
        initPageItem();
    }

    private void initPageItem() {
        ViewHandler handler=new ViewHandler(new ViewHandler.ViewCallBack() {
            @Override
            public void onHandleMessage(Message msg) {
                change();
                alert.dismiss();
            }
        });
        adapter = new SkillAdapter(list, this);
        srollListener = new ListScrollListener
                (end_flag, alert, handler);
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

    //有限数据查询
    private void selectshow(String limit) {
        if (dao == null) {
            return;
        }
        //数据前后记录
        int size_before, size_after;
        size_before = list.size();
        list.addAll((dao.select(Skill.class, false, select_if, select_if_x,
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
            Toast.makeText(getApplicationContext(),R.string.search_no_more, Toast.LENGTH_LONG).show();
        } else if (size_after == 0) {
            Toast.makeText(getApplicationContext(),R.string.search_no, Toast.LENGTH_LONG).show();
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

    //系统按键监听覆写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //条件:当菜单未关闭且搜索条件为初始态，允许退出
        if (select_if.equals("id>?")) {
            super.onKeyDown(keyCode, event);
        } else {
            //按键返回
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //条件不是初始状态就重置
                if (!select_if.equals("id>?")) {
                    end_flag = true;
                    srollListener.changeFlag(true);
                    change_if("id>?", "0");
                    Toast.makeText(getApplicationContext(),R.string.search_rest, Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.search:
                    View xc = getLayoutInflater().inflate(R.layout.select_skill, null);
                    ViewUtil.popSkillDialog(AbilityListActivity.this, xc);
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