package com.key.doltool.activity.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.key.doltool.R;
import com.key.doltool.activity.help.HintListActivity;
import com.key.doltool.activity.help.WordListAcitivity;
import com.key.doltool.activity.setting.MessagePostActivity;
import com.key.doltool.activity.setting.MessageShowActivity;

import butterknife.BindView;

/**
 * 单词表维护
 * 一些hint
 * **/
public class HelpCenterFragment extends BaseFragment {
	@BindView(R.id.function_1)  RelativeLayout fun1;
	@BindView(R.id.function_2)  RelativeLayout fun2;
	@BindView(R.id.function_3)  RelativeLayout fun3;
	@BindView(R.id.function_4)  RelativeLayout fun4;

	@Override
	public int getContentViewId() {
		return R.layout.help_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		init();
	}

	private void init(){
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