package com.key.doltool.activity.voyage.fishing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.view.SlidingTabLayout;
import com.key.doltool.viewpage.FPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 钓鱼专题界面
 * 钓鱼发现物，显示钓鱼地点，钓鱼交易品
 * **/
public class FishingActivity extends BaseActivity {
    private ViewPager main_ViewPage;
    private SlidingTabLayout mSlidingTabLayout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_viewpage_area);
        flag=false;
        initToolBar(null);
        toolbar.setTitle("钓鱼");
        initPage();
    }
    private void initPage(){
        //初始化layout相关
        List<Fragment> main_list = new ArrayList<>();
        main_list.add(new FishingTroveFragment());
        main_list.add(new FishingTradeFragment());
        //初始化ViewPager相关
        FPagerAdapter main_adapter = new FPagerAdapter(
                main_list,new String[]{"钓鱼发现物","钓鱼交易品"},getSupportFragmentManager());
        main_ViewPage = (ViewPager)findViewById(R.id.main_viewpagers);
        main_ViewPage.setAdapter(main_adapter);
        main_ViewPage.setCurrentItem(0);
        //初始化PageEvent相关
        mSlidingTabLayout=(SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.White));
        mSlidingTabLayout.setBackgroundResource(R.drawable.theme_dark_blue);
        mSlidingTabLayout.setViewPager(main_ViewPage);
    }
}
