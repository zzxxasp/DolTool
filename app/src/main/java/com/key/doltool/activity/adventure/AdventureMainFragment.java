package com.key.doltool.activity.adventure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.AdventureAdapter;
import com.key.doltool.data.sqlite.Trove_Count;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.util.db.SRPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AdventureMainFragment extends BaseFragment{
	@BindView(R.id.main_list)  ListView main_list;
	private List<Trove_Count> list_count=new ArrayList<>();;

	@Override
	public int getContentViewId() {
		return R.layout.adventure_main_area;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		init();
		findView();
		setListener();
	}


	private void findView(){
		main_list.setAdapter(new AdventureAdapter(list_count,context));
	}
	@Override
	public void onResume() {
		if(UpdataList.FLAG_CHANGE_LIST==1){
			init();
			main_list.setAdapter(new AdventureAdapter(list_count,context));
			UpdataList.FLAG_CHANGE_LIST=0;
		}
		super.onResume();
	}
	private void setListener(){
		main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				jump(list_count.get(arg2).getType());
			}
		});
	}
	private void jump(int index){
		Intent it=new Intent(context,AdventureListNewApiActivity.class);
		it.putExtra("type", UpdataCount.name_type[index-1]);
		startActivity(it);
	}
	private void init(){
		list_count=SRPUtil.getInstance(context).select(Trove_Count.class, false, "type > ?",new String[]{"0"}, null, null, null, null);
	}
}