package com.key.doltool.activity.adventure.card;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.CardShareAdapter;
import com.key.doltool.data.Deck;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示云平台分享的论战组合
 * 带命令的跳转至该界面，如果什么没做则不进行回传更新
 * 如果更新了数据则回到上个界面同时更新显示数据
 * **/
public class ShareCardDeckActivity extends BaseActivity {
    private ListView listview;
    private List<Deck> list=new ArrayList<>();
    private ParseQuery<Deck> query;
    private Dialog alert;
    private SRPUtil srp;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_share_show);
        srp=SRPUtil.getInstance(getApplicationContext());
        findView();
        setListener();
        postMessage();
    }
    private void findView(){
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle("卡组分享");
        listview=(ListView)findViewById(R.id.list);
        alert=new DialogEvent().showLoading(this);
        alert.show();
    }
    private void setListener(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击后获取以下列表数据进行数据更新读取
                list.get(position).getCard_list();
//                srp.update_Card();
            }
        });
    }

    private void show(){
        //是否读取此卡组列表，现有的列表会被覆盖

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(android.view.MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.city_search:break;
                case R.id.type_search:break;
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trove_menu, menu);
        return true;
    }

    private void show(List<Deck> objects){
        for(int i=0;i<objects.size();i++){
            list.add(objects.get(i));
        }
        listview.setAdapter(new CardShareAdapter(list, this));
        listview.setVisibility(View.VISIBLE);
    }

    private void postMessage(){
        query=ParseQuery.getQuery("CardShare");
        query.whereExists("name").orderByDescending("value");
        query.findInBackground(new FindCallback<Deck>() {
            public void done(List<Deck> objects, ParseException e) {
                if (e == null) {
                    show(objects);
                } else {
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
                }
                alert.dismiss();
            }
        });
        query.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.cancel();
    }
}
