package com.key.doltool.activity.adventure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.adapter.AdventureAdapter;
import com.key.doltool.data.sqlite.Trove_Count;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.rx.RxBusEvent;
import com.key.doltool.util.db.SRPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AdventureMainFragment extends BaseFragment{
	@BindView(R.id.main_list)  ListView main_list;
	private List<Trove_Count> list_count=new ArrayList<>();
	private Disposable updateSubscription;
	@Override
	public int getContentViewId() {
		return R.layout.adventure_main_area;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		init();
		findView();
		setListener();
		Observable<Boolean> sailBoatObservable = RxBusEvent.get().register(RxBusEvent.UPDATE);
		updateSubscription=sailBoatObservable.subscribe(new Consumer<Boolean>() {
			@Override
			public void accept(Boolean flag) {
				if(flag) {
					init();
					main_list.setAdapter(new AdventureAdapter(list_count,context));
				}
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e("onDestroyView","onDestroyView");
		if(updateSubscription!=null){
			updateSubscription.dispose();
		}
		RxBusEvent.get().unregister(RxBusEvent.SAILMENU);
	}

	private void findView(){
		main_list.setAdapter(new AdventureAdapter(list_count,context));
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