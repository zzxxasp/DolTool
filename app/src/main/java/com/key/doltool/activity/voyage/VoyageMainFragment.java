package com.key.doltool.activity.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.search.SearchActivity;
import com.key.doltool.adapter.VoyageAdapter;
import com.key.doltool.data.VoyageInfo;
import com.key.doltool.data.VoyageItem;
import com.key.doltool.event.VoyageEvent;

import java.util.List;

import butterknife.BindView;

public class VoyageMainFragment extends BaseFragment{
	@BindView(R.id.gridview) GridView gridView;
	@BindView(R.id.search_view) TextView search_view;
	private List<VoyageItem> list;

	@Override
	public int getContentViewId() {
		return R.layout.activity_voyage;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
		setListener();
	}

	private void findView(){
		VoyageInfo info=new VoyageInfo(context);
		list=VoyageEvent.getItemByString(info.getData());
		gridView.setAdapter(new VoyageAdapter(list,context));
	}
	private void setListener(){
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				VoyageEvent.jumpForVoyage(context,list.get(arg2));
			}
		});
		search_view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context,SearchActivity.class));
			}
		});
	}
}