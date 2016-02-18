package com.key.doltool.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.key.doltool.activity.ability.AbilityListActivity;
import com.key.doltool.activity.adc.ADCListActivity;
import com.key.doltool.activity.adventure.NPCFragment;
import com.key.doltool.activity.adventure.card.CardComboFragment;
import com.key.doltool.activity.dockyard.DockYardFragment;
import com.key.doltool.activity.job.JobListActivity;
import com.key.doltool.activity.trade.TradeItemFragment;
import com.key.doltool.activity.voyage.TradeItemActivity;
import com.key.doltool.activity.wiki.WikiListActivity;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.data.MenuItem;
import com.key.doltool.view.Toast;
import com.key.doltool.view.range.RangeBar;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
/**
 * 界面辅助处理工具
 * @author key
 * @version 0.1
 */
public class ViewUtil {
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
	public static void popCardDialog(final BaseAdventureActivity activity,View layout){
		final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
				LayoutParams.MATCH_PARENT);
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(layout, params);
		updateDialog.show();
		final Spinner type=(Spinner)layout.findViewById(R.id.type);
		final TextView number=(TextView)layout.findViewById(R.id.min_point);
		final RangeBar bar=(RangeBar)layout.findViewById(R.id.rangebar1);
		final EditText name=(EditText)layout.findViewById(R.id.card_name);
		final Button positive=(Button)layout.findViewById(R.id.btn_confirm);
		final Button negative=(Button)layout.findViewById(R.id.btn_cancel);
		bar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
			@Override
			public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
				number.setText(leftThumbIndex+"-"+rightThumbIndex);
			}
		});
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
		positive.setText("搜索");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				activity.select_txt= type.getSelectedItem()+"~"+
						name.getText().toString().trim()+"~"+number.getText().toString();
				activity.select(activity.select_txt);
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
				String select_if = "";
				List<String> select_args = new ArrayList<>();
				if (box_m.isChecked()) {
					select_if = "sex = ? ";
					select_args.add(box_m.getText().toString());
				} else if (box_w.isChecked()) {
					select_if = "sex = ? ";
					select_args.add(box_w.getText().toString());
				}
				if (type.getSelectedItemId() != 1000 && type.getSelectedItemId() != 0) {
					if (select_if.equals("")) {
						select_if += "type = ?";
					} else {
						select_if += "and type = ?";
					}
					String if_s = type.getSelectedItemId() + "";
					Log.i("s", if_s + "");
					select_args.add(if_s);
				}
				activity.change_if(select_if, select_args);
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

	public static void popSkillDialog(final AbilityListActivity activity,View layout){
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
				,ResourcesUtil.getArray(activity,R.array.skill_type));
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
						select_if+="type = ?";
					}else{
						select_if+="and type = ?";
					}
					String if_s=(type.getSelectedItemPosition()+1)+"";
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

	public static void setImageView(ImageView view,String name,Context context){
		try {
			view.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.ITEM +MD5(name)+ ".png")));
		} catch (IOException e) {
			view.setImageResource(R.drawable.item_defalut);
		}
	}
	public static String MD5(String plainText) {
		StringBuilder buf = new StringBuilder("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			for (byte aB : b) {
				i = aB;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().substring(8,24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}