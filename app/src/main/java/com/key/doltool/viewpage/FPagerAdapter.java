package com.key.doltool.viewpage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * ViewPager-Adapter数据准备
 * @author key
 * @version 0.1
 */
public class FPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	private String[] title;
	public FPagerAdapter(List<Fragment> list, String[] title, FragmentManager fm){
		super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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