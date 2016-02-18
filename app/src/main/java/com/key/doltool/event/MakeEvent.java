package com.key.doltool.event;

import java.util.List;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.dockyard.DockYardFragment;
import com.key.doltool.adapter.PartSpinnerdapter;
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.util.CommonUtil;
/**造船控件处理事件**/
public class MakeEvent {
	private DockYardFragment context;
	public MakeEvent(DockYardFragment context){
		this.context=context;
	}
	public void showPartDialog(final List<Part> list,final TextView txt,final int number){
        LayoutInflater layoutinflater = context.getActivity().getLayoutInflater();
        View view = layoutinflater.inflate(R.layout.dockyard_chose_layout, null);
        final Dialog updateDialog = new Dialog(context.getActivity(), R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(context.getActivity())-30,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(view,params);
        updateDialog.show();
        Button btn=(Button)view.findViewById(R.id.clear_btn);
        ListView listview=(ListView)view.findViewById(R.id.listview);
        listview.setAdapter(new PartSpinnerdapter(list, context.getActivity()));
        btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int r=0;
				switch(number){
				case 0:r=R.string.sp_hint1;break;
				case 1:r=R.string.sp_hint2;break;
				case 2:r=R.string.sp_hint3;break;
				case 3:r=R.string.sp_hint4;break;
				}
				txt.setText(r);
				updateDialog.dismiss();
				context.setPartChose(null,number);
			}
		});
        listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				txt.setText(list.get(arg2).getName()+"");
				updateDialog.dismiss();
				context.setPartChose(list.get(arg2),number);
			}
		});
	}
}
