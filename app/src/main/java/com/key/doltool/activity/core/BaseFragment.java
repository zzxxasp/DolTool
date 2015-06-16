package com.key.doltool.activity.core;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;


public abstract class BaseFragment extends Fragment{
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
