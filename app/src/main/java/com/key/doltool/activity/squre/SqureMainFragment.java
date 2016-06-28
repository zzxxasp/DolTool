package com.key.doltool.activity.squre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.view.Toast;

import butterknife.BindView;

/**
 * 广场的人们
 * **/
public class SqureMainFragment extends BaseFragment{
	@BindView(R.id.map) LinearLayout map;
	@BindView(R.id.fortune) LinearLayout fortune;
	@BindView(R.id.port) LinearLayout port;

	@Override
	public int getContentViewId() {
		return R.layout.squre_main;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		setListener();
	}


	private void setListener(){
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(1);
			}
		});
		fortune.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jump(2);
			}
		});
		port.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jump(3);
			}
		});
	}
	private void jump(int index){
		Class<?> c ;
		switch(index){
			case 1:c=MapActivity.class;break;
			case 2:c=FortuneActivity.class;break;
			case 3:c=PortActivity.class;break;
			default:Toast.makeText(getActivity().getApplicationContext(),"还在建设中",Toast.LENGTH_SHORT).show();return;
		}
		Intent intent=new Intent(getActivity(),c);
		startActivity(intent);
	}
}
