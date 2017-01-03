package com.key.doltool.activity.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment{
	public abstract int getContentViewId();
	protected Activity context;
	protected View mRootView;
	private Unbinder unbinder;
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView =inflater.inflate(getContentViewId(),container,false);
		unbinder=ButterKnife.bind(this,mRootView);//绑定framgent
		this.context = getActivity();
		initAllMembersView(savedInstanceState);
		return mRootView;
	}
	protected abstract void initAllMembersView(Bundle savedInstanceState);

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
