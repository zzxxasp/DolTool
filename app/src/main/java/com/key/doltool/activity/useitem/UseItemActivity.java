package com.key.doltool.activity.useitem;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.TagAdapter;
import com.key.doltool.data.item.UseItem;
import com.key.doltool.util.StringUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.FlowLayout;

import butterknife.BindView;

public class UseItemActivity extends BaseActivity{

    @BindView(R.id.name) TextView name;
    @BindView(R.id.main_txt) TextView main_txt;
    @BindView(R.id.city_txt) TextView city_txt;
    @BindView(R.id.city_array) FlowLayout array;
    @BindView(R.id.pic) ImageView pic;

    private String id="";
    private String name_txt="";
    private String tw_name="";
    private UseItem item;

    @Override
    public int getContentViewId() {
        return R.layout.useitem_detail;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        id=getIntent().getStringExtra("id");
        name_txt=getIntent().getStringExtra("name");
        tw_name=getIntent().getStringExtra("tw_name");
        flag=false;
        init();
    }

    private void init(){
        if(!StringUtil.isNull(id)){
            item= SRPUtil.getInstance(getApplicationContext()).select
                    (UseItem.class,false,"id=?",new String[]{id},null,null,null,null).get(0);
        }
        if(!StringUtil.isNull(name_txt)){
            item= SRPUtil.getInstance(getApplicationContext()).select
                    (UseItem.class, false, "name=? or name=?", new String[]{name_txt,tw_name}, null, null, null, null).get(0);
        }
        flag=false;
        initToolBar(null);
        toolbar.setTitle(item.name);
        name.setText(item.name);
        main_txt.setText(item.info);
        ViewUtil.setImageView(pic,item.name,this);
        if(StringUtil.isNull(item.type)){
            city_txt.setVisibility(View.GONE);
            array.setVisibility(View.GONE);
        }else{
            String[] city=item.type.split(",");
            if(city.length==0){
                city=new String[1];
                city[0]=item.type;
            }
            array.setAdapter(new TagAdapter(this,city,false));
        }
    }
}
