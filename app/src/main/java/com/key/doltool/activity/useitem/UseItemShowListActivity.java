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

import butterknife.BindView;

/**
 *
 * **/
public class UseItemShowListActivity extends BaseActivity {
    @BindView(R.id.listview) ListView listview;
    private List<UseItem> list=new ArrayList<>();
    private String order="name desc";
    private String type="";

    @Override
    public int getContentViewId() {
        return R.layout.user_item_show;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        findView();
        init();
    }

    private void findView(){
        type=getIntent().getStringExtra("type");
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
