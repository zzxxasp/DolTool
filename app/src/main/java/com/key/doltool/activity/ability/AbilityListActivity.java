package com.key.doltool.activity.ability;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SkillAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.app.util.ListScrollListener;
import com.key.doltool.app.util.ViewHandler;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.view.Toast;

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
    private SkillAdapter adapter;
    private ListFlowHelper<Skill> listFlowHelper;
    @Override
    public int getContentViewId() {
        return R.layout.card_combo_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        listFlowHelper=new ListFlowHelper<>(Skill.class, context, new ListFlowHelper.ListFlowCallBack() {
            @Override
            public void showSelectToast(String msg) {
                Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void setAdapter() {
                adapter=new SkillAdapter(listFlowHelper.list,context);
                listview.setAdapter(adapter);
            }

            @Override
            public void updateAdapter() {
                adapter.notifyDataSetChanged();
            }
        }, SkillAdapter.SIZE);
        flag=false;
        initToolBar(onMenuItemClick);
        findView();
        toolbar.setTitle(R.string.skill_title);
        listFlowHelper.getExtra(getIntent());//外部搜索链接参数处理
        listFlowHelper.selectshow("0," + SkillAdapter.SIZE);
    }


    //通用findView
    private void findView() {
        alert=new DialogEvent().showLoading(this);
        initPage();
    }
    @OnItemClick(R.id.listview) void itemClick(int position) {
        if (adapter.getItem(position).getType() <= 5) {
            Intent it = new Intent(AbilityListActivity.this, AbilityForNormalDetailActivity.class);
            it.putExtra("id", adapter.getItem(position).getId() + "");
            startActivity(it);
        } else {
            Intent it = new Intent(AbilityListActivity.this, AbilityForBoatDetailActivity.class);
            it.putExtra("id", adapter.getItem(position).getId() + "");
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
                listFlowHelper.change();
                DialogUtil.dismiss(context,alert);
            }
        });
        ListScrollListener srollListener = new ListScrollListener
                (alert,handler,this);
        listFlowHelper.setListScrollListener(srollListener);
        adapter = new SkillAdapter(listFlowHelper.list, this);
        listview.setOnScrollListener(srollListener);
        listview.setAdapter(adapter);
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
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.search:
                    View xc = getLayoutInflater().inflate(R.layout.select_skill, null);
                    ViewUtil.popSkillDialog(listFlowHelper,context,xc);
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