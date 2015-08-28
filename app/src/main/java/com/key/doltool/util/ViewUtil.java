package com.key.doltool.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseAdventureActivity;
import com.key.doltool.activity.adc.ADCListActivity;
import com.key.doltool.activity.adventure.CardComboFragment;
import com.key.doltool.activity.adventure.NPCFragment;
import com.key.doltool.activity.dockyard.DockYardFragment;
import com.key.doltool.activity.job.JobListActivity;
import com.key.doltool.activity.trade.TradeItemActivity;
import com.key.doltool.activity.trade.TradeItemFragment;
import com.key.doltool.activity.wiki.WikiListActivity;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.data.MenuItem;
import com.key.doltool.view.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 界面辅助处理工具
 * @author key
 * @version 0.1
 */
public class ViewUtil {
	/**焦点转移**/
	public static void setFocus(View v){
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		v.requestFocusFromTouch();	
	}
	
	/**船只类型处理**/
	public static String[] setDataForType(int type,int size,int way){
		String[] temp=new String[3];
		if(size==0)
			temp[0]="小型";
		else if(size==1)
			temp[0]="中型";
		else
			temp[0]="大型";
		if(way==0)
			temp[1]="冒船";
		else if(way==1)
			temp[1]="商船";
		else
			temp[1]="战船";
		if(type==0)
			temp[2]="帆船";
		else 
			temp[2]="桨船";
		return temp;
	}
	/**初始化Menu**/
	public static void setList(List<MenuItem> list,int type){
		String[] item_name={
				"详细查询","标记任务","批量标记","相关网页",
				"任务筛选","城市搜索","论战组合","回报发现",
				"类型搜索"
		};
		int[] item_pic_id={
				R.drawable.ic_menu_find,R.drawable.ic_tag,
				R.drawable.ic_menu_add,R.drawable.ic_chrome_white,
				R.drawable.ic_type,R.drawable.ic_city,
				R.drawable.ic_card,R.drawable.ic_npc,
				R.drawable.ic_type
		};
		switch(type){
			case 1:			
				for(int i=0;i<1;i++){
					MenuItem menuitem=new MenuItem();
					menuitem.name=item_name[i];
					menuitem.pic_id=item_pic_id[i];
					list.add(menuitem);
				}
				break;
			case 2:			
					MenuItem menuitem=new MenuItem();
					menuitem.name=item_name[0];
					menuitem.pic_id=item_pic_id[0];
					list.add(menuitem);
				break;
			case 3:
					MenuItem menuitem3=new MenuItem();
					menuitem3.name=item_name[0];
					menuitem3.pic_id=item_pic_id[0];
					MenuItem menuitem2=new MenuItem();
					menuitem2.name=item_name[2];
					menuitem2.pic_id=item_pic_id[2];
					list.add(menuitem3);
					list.add(menuitem2);
				break;
			case 4:
					MenuItem menuitem33=new MenuItem();
					menuitem33.name=item_name[0];
					menuitem33.pic_id=item_pic_id[0];
					MenuItem menuitem22=new MenuItem();
					menuitem22.name=item_name[2];
					menuitem22.pic_id=item_pic_id[2];
					list.add(menuitem33);
					list.add(menuitem22);
				break;
			case 5:
					MenuItem menuitem11=new MenuItem();
					menuitem11.name=item_name[0];
					menuitem11.pic_id=item_pic_id[0];
					MenuItem menuitem12=new MenuItem();
					menuitem12.name=item_name[3];
					menuitem12.pic_id=item_pic_id[3];
					list.add(menuitem11);
					list.add(menuitem12);
				break;
			case 6:
					MenuItem menuitem61=new MenuItem();
					menuitem61.name=item_name[5];
					menuitem61.pic_id=item_pic_id[5];
					list.add(menuitem61);
					MenuItem menuitem62=new MenuItem();
					menuitem62.name=item_name[8];
					menuitem62.pic_id=item_pic_id[8];
					list.add(menuitem62);
				break;
			case 7:
					MenuItem menuitem71=new MenuItem();
					menuitem71.name=item_name[4];
					menuitem71.pic_id=item_pic_id[0];
					MenuItem menuitem72=new MenuItem();
					menuitem72.name=item_name[1];
					menuitem72.pic_id=item_pic_id[1];
					list.add(menuitem71);
					list.add(menuitem72);
				break;
			case 10:
					MenuItem menuitem101=new MenuItem();
					menuitem101.name=item_name[6];
					menuitem101.pic_id=item_pic_id[6];
					MenuItem menuitem102=new MenuItem();
					menuitem102.name=item_name[7];
					menuitem102.pic_id=item_pic_id[7];
					list.add(menuitem101);
					list.add(menuitem102);
				break;
			}
	}
	
	/**For DockYard Select PopDialog(船只搜索对话框)**/
	public static void popDialog(final DockYardFragment activity,View layout,final int page){
		final Dialog updateDialog = new Dialog(activity.getActivity(), R.style.updateDialog);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		//第二页无弹窗
		if(page==2){
			Toast.makeText(activity.getActivity(),"造船页面无资源搜索", Toast.LENGTH_SHORT).show();
			return ;
		//第一页内容
		}else if(page==1){
	        updateDialog.setCancelable(true);
	        updateDialog.setCanceledOnTouchOutside(true);
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity.getActivity())-30,
	                LayoutParams.MATCH_PARENT);
	        params.setMargins(10,10,10,10);
	        updateDialog.setContentView(layout,params);
	        updateDialog.show();
			
			final EditText name=(EditText)layout.findViewById(R.id.boat_name);
			final CheckBox s_box=(CheckBox)layout.findViewById(R.id.s_box);
			final CheckBox m_box=(CheckBox)layout.findViewById(R.id.m_box);
			final CheckBox l_box=(CheckBox)layout.findViewById(R.id.l_box);
			
			positive.setText("搜索");
			positive.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String str=name.getText().toString();
					String if_str;
					List<String> if_list=new ArrayList<>();
					String if_args;
					//不为空才修改条件
					if(!str.equals("")){
						if_str="name like ?";
						if_args="%"+str+"%";
						if_list.add(if_args);
					}
					else{
						if_str="id>?";
						if_args="0";
						if_list.add(if_args);
					}
					NumberUtil.TEMP=NumberUtil.threeZero(s_box.isChecked(),m_box.isChecked(),l_box.isChecked());
					switch(NumberUtil.TEMP){
						case 1:if_str=if_str+" and size=?";if_args="2";break;
						case 2:if_str=if_str+" and size=?";if_args="1";break;
						case 3:if_str=if_str+" and size>?";if_args="0";break;
						case 4:if_str=if_str+" and size=?";if_args="0";break;
						case 5:if_str=if_str+" and size<>?";if_args="1";break;
						case 6:if_str=if_str+" and size<>?";if_args="2";break;
						case 7:if_str=if_str+" and size>?";if_args="-1";break;
					}
					if(NumberUtil.TEMP!=0)
						if_list.add(if_args);
					activity.change_if(if_str,if_list,page);
					activity.begin(1);
					updateDialog.dismiss();
				}
			});
		}
		//第三页内容
		if(page==3){
	        updateDialog.setCancelable(true);
	        updateDialog.setCanceledOnTouchOutside(true);
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity.getActivity())-30,
	                LayoutParams.MATCH_PARENT);
	        params.setMargins(10,10,10,10);
	        updateDialog.setContentView(layout,params);
	        updateDialog.show();
			
			final EditText name=(EditText)layout.findViewById(R.id.boat_name);
			final CheckBox[] t=new CheckBox[9];
			int id[]={R.id.t_0,R.id.t_3,R.id.t_1,R.id.t_4,R.id.t_2,
					R.id.t_5,R.id.t_6,R.id.t_7,R.id.t_8};
			for(int i=0;i<9;i++){
				t[i]=(CheckBox)layout.findViewById(id[i]);
			}
			name.setHint(R.string.hint_name2);
			
			positive.setText("搜索");
			positive.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String str=name.getText().toString();
					String if_str;
					List<String> if_list=new ArrayList<>();
					String if_args;
					//不为空才修改条件
					if(!str.equals("")){
						if_str="name like ?";
						if_args="%"+str+"%";
						if_list.add(if_args);
					}
					else{
						if_str="id>?";
						if_args="0";
						if_list.add(if_args);
					}
					for(int j=0;j<9;j++){
						if(t[j].isChecked()&&if_list.size()==1){
							if_str+=" and  (type = ?";
							if_args=""+j;
							if_list.add(if_args);
						}else if(t[j].isChecked()&&if_list.size()!=1){
							if_str+=" or type = ?";
							if_args=""+j;
							if_list.add(if_args);
						}
						if(if_list.size()>1&&j==8){
							if_str+=")";
						}
					}
					activity.change_if(if_str,if_list,page);
					activity.begin(2);
					updateDialog.dismiss();
				}
			});
		}
	}
	/**For PopDialog Common **/
	public static void popDialog(final BaseAdventureActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
		updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				activity.select_txt=name.getText().toString().trim();
				activity.select(name.getText().toString().trim());
				updateDialog.dismiss();
			}
		});
	}
	/**For PopDialog Common **/
	public static void popCardDialog(final CardComboFragment activity,View layout){
		final Dialog updateDialog = new Dialog(activity.getActivity(), R.style.updateDialog);
		updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity.getActivity())-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity.getActivity()
        		,ResourcesUtil.getArray(activity.getActivity(),R.array.card_type));
        type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}					
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=0){
					if(type.getSelectedItemPosition()==1){
						if(select_if.equals("")){
							select_if+="effect like ?";
						}else{
							select_if+="and effect like ?";
						}
						String if_s="%"+"点数+"+"%";
						Log.i("s",if_s+"");
						select_args.add(if_s);
					}else{
						if(select_if.equals("")){
							select_if+="effect like ?";
						}else{
							select_if+="and effect like ?";
						}
						String if_s="%"+type.getSelectedItem().toString()+"%";
						Log.i("s",if_s+"");
						select_args.add(if_s);
					}
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
	}
	public static void popTradeDialog(final TradeItemFragment activity,View layout){
		final Dialog updateDialog = new Dialog(activity.getActivity(), R.style.updateDialog);
		updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity.getActivity())-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity.getActivity()
        		,ResourcesUtil.getArray(activity.getActivity(),R.array.trade_type));
        type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}					
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=1000){
					if(select_if.equals("")){
						select_if+="type like ?";
					}else{
						select_if+="and type like ?";
					}
					String if_s="%"+type.getSelectedItem().toString()+"%";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
	}

	public static void popTradeDialog(final TradeItemActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
				LayoutParams.MATCH_PARENT);
		params.setMargins(10,10,10,10);
		updateDialog.setContentView(layout,params);
		updateDialog.show();

		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
		ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity
				,ResourcesUtil.getArray(activity,R.array.trade_type));
		type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=1000){
					if(select_if.equals("")){
						select_if+="type like ?";
					}else{
						select_if+="and type like ?";
					}
					String if_s="%"+type.getSelectedItem().toString()+"%";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
	}


	
	public static void popWikiDialog(final WikiListActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
		updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity
        		,ResourcesUtil.getArray(activity,R.array.trade_type));
        type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}					
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=1000){
					if(select_if.equals("")){
						select_if+="type like ?";
					}else{
						select_if+="and type like ?";
					}
					String if_s="%"+type.getSelectedItem().toString()+"%";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
	}
	
	public static void popJobDialog(final JobListActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
		updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity
        		,ResourcesUtil.getArray(activity,R.array.adc_type_txt));
        type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}					
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=1000){
					if(select_if.equals("")){
						select_if+="type like ?";
					}else{
						select_if+="and type like ?";
					}
					String if_s="%"+type.getSelectedItem().toString()+"%";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
	}
	
	public static void popADCDialog(final ADCListActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		
		final RadioButton box_m=(RadioButton)layout.findViewById(R.id.sex_box1);
		final RadioButton box_w=(RadioButton)layout.findViewById(R.id.sex_box2);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if="";
				List<String> select_args=new ArrayList<>();
				if(box_m.isChecked()){
					select_if="sex = ? ";
					select_args.add(box_m.getText().toString());
				}					
				else if(box_w.isChecked()){
					select_if="sex = ? ";
					select_args.add(box_w.getText().toString());
				}
				if(type.getSelectedItemId()!=1000&&type.getSelectedItemId()!=0){
					if(select_if.equals("")){
						select_if+="type = ?";
					}else{
						select_if+="and type = ?";
					}
					String if_s=type.getSelectedItemId()+"";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}
	
	
	public static void popNPCDialog(final NPCFragment activity,View layout){
		final Dialog updateDialog = new Dialog(activity.getActivity(), R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity.getActivity())-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(layout,params);
        updateDialog.show();
		
		final EditText name=(EditText)layout.findViewById(R.id.boat_name);
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter(activity.getActivity()
        		,ResourcesUtil.getArray(activity.getActivity(),R.array.back_type));
        type.setAdapter(adapter);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String select_if;
				List<String> select_args=new ArrayList<>();
				if(!name.getText().toString().trim().equals("")){
					select_if="name like ? ";
					select_args.add("%"+name.getText().toString().trim()+"%");
				}					
				else{
					select_if="id>?";
					select_args.add("0");
				}
				if(type.getSelectedItemPosition()!=100){
					if(select_if.equals("")){
						select_if+="love_type like ?";
					}else{
						select_if+="and love_type like ?";
					}
					String if_s="%"+type.getSelectedItem().toString()+"%";
					Log.i("s",if_s+"");
					select_args.add(if_s);
				}
				activity.change_if(select_if,select_args);
				activity.begin();
				updateDialog.dismiss();
			}
		});
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}
	/**
	 * 遍历布局，并控制所有子控件可控与否
	 * @param viewGroup 布局对象
	 * @param flag 启用与否
	 */
	public static void disableSubControls(ViewGroup viewGroup,boolean flag) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View v = viewGroup.getChildAt(i);
			v.setEnabled(flag);
			if (v instanceof ViewGroup) {
				if (v instanceof Spinner) {
					Spinner spinner = (Spinner) v;
					spinner.setClickable(flag);
					spinner.setEnabled(flag);
				} else if (v instanceof ListView) {
					v.setClickable(flag);
					v.setEnabled(flag);
				} else {
					disableSubControls((ViewGroup) v,flag);
				}
			}else if (v instanceof EditText) {
				v.setEnabled(flag);
				v.setClickable(flag);
			}else if (v instanceof Button) {
				v.setEnabled(flag);
			}else {
				v.setEnabled(flag);
			}
		}
	}
	/**根据技能数量生成布局**/
	public static void fill_boat_detail(final Activity context,List<MenuItem> ab,TableLayout father){
		int y=0;
		int x=0;
		int dip_10=DensityUtil.dip2px(context,10);
		int dip_5=DensityUtil.dip2px(context,5);
		if(ab.size()<=3)
			x=ab.size();
		else
			x=3;
		for(int i=0;i<ab.size();i=i+3){
			TableRow row=new TableRow(context);
			row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			row.setWeightSum(3);
			row.setPadding(dip_10,dip_10,dip_10,dip_10);
			for(int j=y;j<x;j++){
				final String number=ab.get(j).name;
				LinearLayout item=new LinearLayout(context);
				item.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1.0f));
				item.setGravity(Gravity.CENTER);
				item.setOrientation(LinearLayout.VERTICAL);
				ImageView img=new ImageView(context);
				img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				try {
					img.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(ab.get(j).pic+".png")));
				} catch (IOException e) {
					e.printStackTrace();
					img.setImageResource(R.drawable.item_defalut);
				}
				img.setPadding(dip_5, dip_5, dip_5, dip_5);
				TextView txt=new TextView(context);
				txt.setText(ab.get(j).name);
				txt.setTextColor(Color.WHITE);
				txt.setTextSize(12);
				txt.setPadding(dip_5, 0, dip_5, dip_5);
				txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				item.addView(img);
				item.addView(txt);
				item.setBackgroundResource(R.drawable.theme_blue_btn);
//				item.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View v) {
//						Intent it=new Intent(context,AbilityForBoatDetail.class);
//						it.putExtra("name",number);
//						context.startActivity(it);
//					}
//				});
				row.addView(item);
			}
			y+=3;
			x+=3;
			if(x>ab.size())
				x=ab.size();
			father.addView(row);
		}
	}
}