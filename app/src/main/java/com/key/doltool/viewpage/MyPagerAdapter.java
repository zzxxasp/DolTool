package com.key.doltool.viewpage;
import java.util.List;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager-Adapter数据准备
 * @author key
 * @version 0.1
 */
public class MyPagerAdapter extends PagerAdapter{
	private List<View> mListViews;
	public MyPagerAdapter(List<View> list){
		mListViews=list;
	}
	//除去页面中的View
	public void destroyItem(ViewGroup view, int position, Object arg2) {
			view.removeView(mListViews.get(position));
	}
	public void finishUpdate(ViewGroup arg0) {
		
	}
	//获得总页面数
	public int getCount() {
		return mListViews.size();
	}
	//添加View
	public Object instantiateItem(ViewGroup view, int position) {
		view.addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		
	}
	public Parcelable saveState() {
		return null;
	}
	public void startUpdate(ViewGroup arg0) {
	}
}