package com.key.doltool.activity.core;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.key.doltool.R;
import com.key.doltool.activity.help.HintListActivity;
import com.key.doltool.activity.help.WordListAcitivity;
import com.key.doltool.activity.setting.MessagePostActivity;
import com.key.doltool.activity.setting.MessageShowActivity;
/**
 * 单词表维护
 * 一些hint
 * **/
public class HelpCenterFragment extends BaseFragment {
	private ImageView main_menu;
	private RelativeLayout fun1,fun2,fun3,fun4;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.help_main,container,false);
		init(view);
		return view;
	}
	private void init(View view){
		main_menu=(ImageView)getActivity().findViewById(R.id.main_menu);
		main_menu.clearAnimation();
		main_menu.setVisibility(View.GONE);
		fun1=(RelativeLayout)view.findViewById(R.id.function_1);
		fun2=(RelativeLayout)view.findViewById(R.id.function_2);
		fun3=(RelativeLayout)view.findViewById(R.id.function_3);
		fun4=(RelativeLayout)view.findViewById(R.id.function_4);
		fun2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(getActivity(),HintListActivity.class);
				getActivity().startActivity(it);
			}
		});
		fun1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(getActivity(),WordListAcitivity.class);
				getActivity().startActivity(it);
			}
		});
		fun3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(getActivity(),MessagePostActivity.class);
				startActivity(it);
			}
		});
		fun4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(getActivity(),MessageShowActivity.class);
				startActivity(it);
			}
		});
	}
}