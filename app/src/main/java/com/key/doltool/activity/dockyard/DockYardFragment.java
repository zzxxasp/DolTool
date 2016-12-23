package com.key.doltool.activity.dockyard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.activity.voyage.fishing.FishingTradeFragment;
import com.key.doltool.data.MenuItem;
import com.key.doltool.view.SlidingTabLayout;
import com.key.doltool.viewpage.FPagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 造船厂容器界面
 */
public class DockYardFragment extends BaseFragment{
	//ViewPager定义部分
	@BindView(R.id.main_viewpagers) ViewPager main_ViewPage;
	@BindView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;

	@Override
	public int getContentViewId() {
		return R.layout.dockyard_main_area;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.search:
					intentMenu(main_ViewPage.getCurrentItem());
					break;
			}
			return true;
		}
	};

	private void intentMenu(int index){
		MenuItem item=new MenuItem();
		item.index=index;
		EventBus.getDefault().post(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	//通用findView
	private void findView() {
		BaseFragmentActivity a=(BaseFragmentActivity)getActivity();
		a.toolbar.setOnMenuItemClickListener(onMenuItemClick);
		//初始化ViewPager相关
		List<Fragment> main_list = new ArrayList<>();
		main_list.add(new SailBoatListFragment());
		main_list.add(new BuildBoatFragment());
		main_list.add(new PartListFragment());
		FPagerAdapter main_adapter = new FPagerAdapter(main_list, new String[]{"船只列表", "造船模拟", "船只配件"},
				getChildFragmentManager());
		main_ViewPage.setAdapter(main_adapter);
		main_ViewPage.setCurrentItem(0);
		main_ViewPage.setOffscreenPageLimit(2);
		//初始化PageEvent相关
		mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.White));
		mSlidingTabLayout.setBackgroundResource(R.drawable.theme_dark_blue);
		mSlidingTabLayout.setViewPager(main_ViewPage);
	}
}