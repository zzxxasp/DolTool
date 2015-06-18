package com.key.doltool.activity.dockyard;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.view.flat.FlatButton;

/**
 * New 造船厂界面
 * **/
public class BuildBoatActivity extends BaseActivity{
	private TextView info_txt;
	private LinearLayout show_layout,choose_part;
	private RelativeLayout ability;

	private TextView vo_s,vo_f,vo_tu,vo_de,vo_p;
	private TextView bt_h,bt_p,bt_a,bt_c,bt_s;

	private TextView vo_s_a,vo_f_a,vo_tu_a,vo_de_a,vo_p_a;
	private TextView bt_h_a,bt_a_a,bt_c_a,bt_s_a;

	private FlatButton plus_btn;
	private FlatButton btn;
	private TextView sp_s,sp_c,sp_eq,sp_eq2;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
	}
	private void findView(){
		info_txt=(TextView)findViewById(R.id.info_txt);
		show_layout=(LinearLayout)findViewById(R.id.show_broad);
		choose_part=(LinearLayout)findViewById(R.id.choose_part);
		ability=(RelativeLayout)findViewById(R.id.ability);
		findViewInfo();
	}
	//造船界面FindView
	private void findViewInfo(){
		vo_s=(TextView)findViewById(R.id.square_sail_point);
		vo_f=(TextView)findViewById(R.id.fore_sail_point);
		vo_tu=(TextView)findViewById(R.id.turn_point);
		vo_de=(TextView)findViewById(R.id.def_wave_point);
		vo_p=(TextView)findViewById(R.id.paddle_point);
		bt_h=(TextView)findViewById(R.id.health_boat_point);
		bt_p=(TextView)findViewById(R.id.people_number_point);
		bt_a=(TextView)findViewById(R.id.armor_point);
		bt_c=(TextView)findViewById(R.id.crenelle_point);
		bt_s=(TextView)findViewById(R.id.shipping_space_point);

		vo_s_a=(TextView)findViewById(R.id.square_sail_add);
		vo_f_a=(TextView)findViewById(R.id.fore_sail_add);
		vo_tu_a=(TextView)findViewById(R.id.turn_add);
		vo_de_a=(TextView)findViewById(R.id.def_wave_add);
		vo_p_a=(TextView)findViewById(R.id.paddle_add);

		bt_h_a=(TextView)findViewById(R.id.health_boat_add);
		bt_a_a=(TextView)findViewById(R.id.armor_add);
		bt_c_a=(TextView)findViewById(R.id.crenelle_add);
		bt_s_a=(TextView)findViewById(R.id.shipping_space_add);

		plus_btn=(FlatButton)findViewById(R.id.build_btn);
		btn=(FlatButton)findViewById(R.id.btn);
		//旋转切换页面
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(ability.getVisibility()==View.VISIBLE)
					MyAnimations.rotate3D(show_layout, ability, choose_part, 1000);
				else
					MyAnimations.rotate3D(show_layout, choose_part, ability,1000);
			}
		});
		sp_s=(TextView)findViewById(R.id.sp_s);
		sp_c=(TextView)findViewById(R.id.sp_c);
		sp_eq=(TextView)findViewById(R.id.sp_eq);
		sp_eq2=(TextView)findViewById(R.id.sp_eq2);
	}
}
