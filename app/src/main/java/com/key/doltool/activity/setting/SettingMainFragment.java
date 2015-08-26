package com.key.doltool.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.app.VersionManager;
import com.key.doltool.util.CommonUtil;

public class SettingMainFragment extends BaseFragment{
	private RelativeLayout layout2,layout3;
	private SwitchCompat check_box;
    private View main;
    private TextView func_2_txt;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.setting_main, container,false);
		 init(view);
		 findView();
		 setListener();
		 ((VersionManager)VersionManager.getInstance()).setActivity(getActivity());
		 return view; 
	}
	private void init(View view){
		main=view;
	}
	private void findView(){
		layout2=(RelativeLayout)main.findViewById(R.id.function_2);
		layout3=(RelativeLayout)main.findViewById(R.id.function_3);
		check_box=(SwitchCompat)main.findViewById(R.id.checkbox);
		func_2_txt=(TextView)main.findViewById(R.id.func_2_txt);
		func_2_txt.setText("当前应用的版本:"+CommonUtil.getAppVersionName(getActivity()));
	}
	private void setListener(){
		final SystemInfo info=new SystemInfo(getActivity());
		if(info.getUpdateFlag()==1){
			check_box.setChecked(true);
		}else{
			check_box.setChecked(false);
		}
		check_box.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					info.setUpdateFlag(1);
				}else{
					info.setUpdateFlag(0);
				}
			}
		});
		layout2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        ((VersionManager)VersionManager.getInstance()).checkVersion(true);
			}
		});
		layout3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DialogEvent().materialDialog(0,"退出","确认是否要退出？",getActivity(),0);
			}
		});
	}
}
