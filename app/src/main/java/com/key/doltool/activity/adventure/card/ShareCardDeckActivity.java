package com.key.doltool.activity.adventure.card;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.CardShareAdapter;
import com.key.doltool.data.Deck;
import com.key.doltool.view.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示云平台分享的论战组合
 * **/
public class ShareCardDeckActivity extends BaseActivity {
    private ListView listview;
    private List<Deck> list=new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_share_show);
        findView();
        setListener();
        postMessage();
    }
    private void findView(){
        flag=false;
        initToolBar(onMenuItemClick);
        listview=(ListView)findViewById(R.id.list);
    }
    private void setListener(){

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
        ParseQuery<Deck> query=ParseQuery.getQuery("CardShare");
        query.whereExists("name").orderByDescending("value");
        query.findInBackground(new FindCallback<Deck>() {
            public void done(List<Deck> objects, ParseException e) {
                if (e == null) {
                    show(objects);
                } else {
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
