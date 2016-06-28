package com.key.doltool.activity.squre;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.ShipAdapter;
import com.key.doltool.adapter.ShipTimeAdapter;
import com.key.doltool.data.sqlite.RegularShip;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.db.SRPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 定期船
 * @author key
 * @version 1.0
 **/
public class PortActivity extends BaseActivity {

	@BindView(R.id.listview) ListView listView;
	private List<RegularShip> shipList=new ArrayList<>();
	private Dialog alert;
	@Override
	public int getContentViewId() {
		return R.layout.activity_port_ship;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		flag=false;
		initToolBar(null);
		toolbar.setTitle("港口定期船");
		findView();
		setListener();
		init();
	}

	private void findView(){
		alert=new DialogEvent().showLoading(this);
	}

	private void setListener(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showTimeList(shipList.get(position));
			}
		});
	}

	private void init(){
		shipList=SRPUtil.getInstance(getApplicationContext())
				.select(RegularShip.class,false,"id>?",new String[]{"0"},
						null,null,null,null);
		listView.setAdapter(new ShipAdapter(shipList,this));
	}

	private void showTimeList(RegularShip item){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.panel_material_time_list_dialog, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(view, params);
		updateDialog.show();
		TextView title_view=(TextView)view.findViewById(R.id.title);
		String title=item.start_city+"-"+item.end_city;
		List<String> list=new Gson().fromJson(item.time_list, new TypeToken<List<String>>() {}.getType());
		title_view.setText(title);
		ListView listView=(ListView)view.findViewById(R.id.listview);
		listView.setAdapter(new ShipTimeAdapter(list,this));
	}
}