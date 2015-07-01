package com.key.doltool.activity.core;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.doltool.R;
public class DevFragment extends BaseFragment {
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dev_main, container,false);
	}
}