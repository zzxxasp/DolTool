package com.key.doltool.activity.core;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
	public abstract int getContentViewId();
	protected Activity context;
	protected View mRootView;
	private Unbinder unbinder;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView =inflater.inflate(getContentViewId(),container,false);
		unbinder=ButterKnife.bind(this,mRootView);
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
