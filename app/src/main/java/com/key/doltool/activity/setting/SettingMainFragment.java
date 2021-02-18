package com.key.doltool.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.data.SystemInfo;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.app.VersionManager;
import com.key.doltool.util.CommonUtil;

import butterknife.BindView;

public class SettingMainFragment extends BaseFragment{
	@BindView(R.id.function_2) RelativeLayout layout2;
	@BindView(R.id.function_3) RelativeLayout layout3;
	@BindView(R.id.checkbox) SwitchCompat check_box;
	@BindView(R.id.func_2_txt) TextView func_2_txt;

	@Override
	public int getContentViewId() {
		return R.layout.setting_main;
	}
	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
		setListener();
		VersionManager.getInstance().setActivity(getActivity());
	}
	private void findView(){
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
		        VersionManager.getInstance().checkVersion(true);
			}
		});
		layout3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DialogEvent().materialDialog(0,"退出","确认是否要退出？",getActivity(),0);
			}
		});
	}
}
