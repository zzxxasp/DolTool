package com.key.doltool.activity.useitem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ShowItemAdapter;
import com.key.doltool.data.item.UseItem;
import com.key.doltool.util.db.SRPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * **/
public class UseItemShowListActivity extends BaseActivity {
    private ListView listview;
    private List<UseItem> list=new ArrayList<>();
    private String order="name desc";
    private String type="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_item_show);
        findView();
        init();
    }
    private void findView(){
        type=getIntent().getStringExtra("type");
        listview=(ListView)findViewById(R.id.listview);
        flag=false;
        initToolBar(null);
        toolbar.setTitle(type);
    }

    private void init(){
        Log.i("type", ":"+type);
        list= SRPUtil.getInstance(getApplicationContext()).select(
                UseItem.class,false,"type like ?",new String[]{"%"+type+"%"},null,null,order,null);
        ShowItemAdapter itemAdapter=new ShowItemAdapter(list,this);
        listview.setAdapter(itemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jump(position);
            }
        });
    }
    private void jump(int index){
        Intent it=new Intent(this,UseItemActivity.class);
        it.putExtra("id",""+list.get(index).id);
        startActivity(it);
    }
}
