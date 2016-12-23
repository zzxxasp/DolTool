package com.key.doltool.viewpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager-Adapter数据准备
 * @author key
 * @version 0.1
 */
public class FPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	private String[] title;
	public FPagerAdapter(List<Fragment> list,String[] title,FragmentManager fm){
		super(fm);
		this.title=title;
		this.list=list;
	}
	//获得总页面数
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}