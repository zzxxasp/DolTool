package com.key.doltool.activity.core;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.key.doltool.R;
public class DevFragment extends BaseFragment {
	private ImageView main_menu;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.dev_main, container,false);
		init();
		return view;
	}
	private void init(){
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.clearAnimation();
		main_menu.setVisibility(View.GONE);
	}
}