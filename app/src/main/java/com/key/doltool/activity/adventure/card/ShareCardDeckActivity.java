package com.key.doltool.activity.adventure.card;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.CardShareAdapter;
import com.key.doltool.app.util.DialogUtil;
import com.key.doltool.data.parse.Deck;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.view.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.leancloud.AVException;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 展示云平台分享的论战组合
 * 带命令的跳转至该界面，如果什么没做则不进行回传更新
 * 如果更新了数据则回到上个界面同时更新显示数据
 * **/
public class ShareCardDeckActivity extends BaseActivity {
    @BindView(R.id.list) ListView listview;
    private List<Deck> list=new ArrayList<>();
    private AVQuery<Deck> query;
    private Dialog alert;

    @Override
    public int getContentViewId() {
        return R.layout.card_share_show;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        findView();
        setListener();
        postMessage();
    }

    private void findView(){
        flag=false;
        initToolBar(onMenuItemClick);
        toolbar.setTitle("卡组分享");
        alert=new DialogEvent().showLoading(this);
        DialogUtil.show(context,alert);
    }
    private void setListener(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击后获取以下列表数据进行数据更新读取
                List<Integer> temp=
                        new Gson().fromJson(list.get(position).getCard_list(),new TypeToken<List<Integer>>(){}.getType());
                show2(temp,list.get(position).getLimit());
            }
        });
    }

    private void show2(final List<Integer> temp,final String limit){
        AlertDialog.Builder b=new AlertDialog.Builder(context);
        b.setTitle("替换卡组");
        b.setMessage("替换现有的卡组，会覆盖现有的卡组");
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new UpdataCount(getApplicationContext()).backSaveCard(temp);
                Intent intent=new Intent(context,CardListActivity.class);
                intent.putExtra("limit",limit);
                setResult(RESULT_OK,intent);
                dialog.dismiss();
                finish();
            }
        });
        b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog mDialog=b.create();
        mDialog.show();
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
        query=AVQuery.getQuery("CardShare");
        query.whereExists("name").orderByDescending("value");
        query.findInBackground().subscribe(new Observer<List<Deck>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Deck> decks) {
                show(decks);
                DialogUtil.dismiss(context,alert);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
                DialogUtil.dismiss(context,alert);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
