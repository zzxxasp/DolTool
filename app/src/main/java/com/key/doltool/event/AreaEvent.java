package com.key.doltool.event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.trade.TradeCityDetailActivity;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.data.sqlite.City;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;

import java.util.ArrayList;
import java.util.List;
/**地域处理事件**/
public class AreaEvent {
	/**冒险区域**/
	public static final String[] ADVENTURE_AREA={
		"全部","北欧","西欧","东欧",
		"非洲·印度","美洲","亚洲",
	};
	/**冒险城市**/
	public static final String[][] ADVENTURE_CITY={
			{},
			{"伦敦","阿姆斯特丹","斯德哥尔摩","圣彼得堡"},
			{"里斯本","塞维尔","马赛","热那亚","突尼斯"},
			{"威尼斯","拿坡里","雅典","伊斯坦堡","亚历山大"},
			{"圣乔治","开普敦","桑给巴尔","亚丁","卡利卡特"},
			{"圣多明哥","波多贝罗","里约热内卢","利马","圣地牙哥","旧金山"},
			{"雅加达","安平"},
	};
	/**书库城市**/
	public static final String[][] LIB_CITY={
			{},
			{"伦敦","阿姆斯特丹","斯德哥尔摩","圣彼得堡"},
			{"里斯本","塞维尔","马赛","热那亚","突尼斯"},
			{"威尼斯","拿坡里","雅典","伊斯坦堡","亚历山大"},
			{"卡利卡特"},
			{"南美开拓港","利马","旧金山"},
			{"东南亚开拓港","杭州","汉阳","堺市","安平"},
	};
	/**回报城市**/
	public static final String[][] BACK_CITY={
			{"全部"},
			{"波尔多","南特","加莱","安特卫普","阿姆斯特丹","伦敦","汉堡","卢贝克","圣彼得堡"},
			{"里斯本","塞维尔","马赛","热那亚","突尼斯","奥波多","瓦伦西亚","巴赛隆纳","比萨","的黎波里"},
			{"威尼斯","拿坡里","雅典","伊斯坦堡","亚历山大","拉古萨","锡拉库萨","贝鲁特","开罗"},
			{"圣乔治","索法拉","桑给巴尔","莫桑比克","荷姆兹","马斯喀特","亚丁","卡利卡特"},
			{"哈瓦那","波多贝罗","维拉克鲁斯","巴伊亚","布宜诺斯艾利斯","旧金山"},
			{"马尼拉","安平","长崎","汉阳","澳门"},
	};
	/**副官城市**/
	public static final String[][] ADC_CITY={
			{"全部"},
			{"伦敦","阿姆斯特丹","圣彼得堡"},
			{"里斯本","塞维尔","马赛","热那亚","突尼斯"},
			{"威尼斯","拿坡里","雅典","伊斯坦堡","亚历山大"},
			{"桑给巴尔","卡利卡特"},
			{"波多贝罗","里约热内卢","利马"},
			{"雅加达","杭州","安平","堺市","汉阳"},
	};
	public String getStringByIndex(int i){
		String str="";
		switch(i){
			case 0:break;
			case 1:break;
			case 2:break;
			case 3:str="沉船";break;
			case 4:str="执事";break;
			case 5:str="宠物";break;
			case 6:str="转职";break;
			case 7:str="掠夺";break;
			case 8:str="海上视认";break;
		}
		return str;
	}
	
	public void showCityDialog(final Activity context,final DefaultDAO dao){
        LayoutInflater layoutinflater = context.getLayoutInflater();
        View view = layoutinflater.inflate(R.layout.enter_city, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
		updateDialog.setCancelable(true);
		updateDialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.setMargins(10,10,10,10);
		updateDialog.setContentView(view,params);
		updateDialog.show();
        final Spinner sp_area=(Spinner)view.findViewById(R.id.sp_area);
        final Spinner sp_city=(Spinner)view.findViewById(R.id.sp_city);
        FlatButton enter=(FlatButton)view.findViewById(R.id.enter_btn);
		FlatButton cancel=(FlatButton)view.findViewById(R.id.cancel_btn);
        List<?> list=dao.select(City.class,false,"id>?",new String[]{"0"},"area",null, null, null);
        List<String> list_area=new ArrayList<>();
        for(int i=0;i<list.size();i++){
        	City city=(City)list.get(i);
        	list_area.add(city.getArea());
        }
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
		(context, StringUtil.listToArray(list_area));
        sp_area.setAdapter(adapter);
		//地区-城市联动事件
        sp_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				List<?> list = dao.select(City.class, false, "area=?", new String[]{(String) sp_area.getSelectedItem()}, null, null, null, null);
				List<String> list_area = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					City city = (City) list.get(i);
					list_area.add(city.getName());
				}
				ArrayAdapter<String> adapter = new SpinnerArrayAdapter
						(context, StringUtil.listToArray(list_area));
				sp_city.setAdapter(adapter);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
        enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(context, TradeCityDetailActivity.class);
				it.putExtra("city_name", (String) sp_city.getSelectedItem());
				context.startActivity(it);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}
	public void showNPCCityDialog(final ListFlowHelper listFlowHelper,final Activity activity){
        LayoutInflater layoutinflater = activity.getLayoutInflater();
        View view = layoutinflater.inflate(R.layout.enter_city, null);
        final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(view,params);
        updateDialog.show();
        final Spinner sp_area=(Spinner)view.findViewById(R.id.sp_area);
        final Spinner sp_city=(Spinner)view.findViewById(R.id.sp_city);
        FlatButton enter=(FlatButton)view.findViewById(R.id.enter_btn);
		FlatButton cancel=(FlatButton)view.findViewById(R.id.cancel_btn);
        TextView title=(TextView)view.findViewById(R.id.title_txt);
        title.setText("回报地点搜索");
        enter.setText("搜索");
            
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
		(activity,ADVENTURE_AREA);
        sp_area.setAdapter(adapter);
		//地区-城市联动事件
        sp_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
		        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
				(activity,BACK_CITY[arg2]);
					sp_city.setAdapter(adapter);
				}
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
        enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String temp = (String) sp_city.getSelectedItem();
				if (!temp.equals("全部")) {
					listFlowHelper.change_if("city=?", (String) sp_city.getSelectedItem());
				} else {
					listFlowHelper.change_if("id>?", "0");
				}
				updateDialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}
	
	public void showADCCityDialog(final ListFlowHelper listFlowHelper,final Activity activity){
        LayoutInflater layoutinflater = activity.getLayoutInflater();
        View view = layoutinflater.inflate(R.layout.enter_city, null);
        final Dialog updateDialog = new Dialog(activity, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(activity)-30,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(view,params);
        updateDialog.show();
        final Spinner sp_area=(Spinner)view.findViewById(R.id.sp_area);
        final Spinner sp_city=(Spinner)view.findViewById(R.id.sp_city);
        FlatButton enter=(FlatButton)view.findViewById(R.id.enter_btn);
		FlatButton cancel=(FlatButton)view.findViewById(R.id.cancel_btn);
        TextView title=(TextView)view.findViewById(R.id.title_txt);
        title.setText("副官城市搜索");
        enter.setText("搜索");
            
        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
        		(activity,ADVENTURE_AREA);
        sp_area.setAdapter(adapter);
		//地区-城市联动事件
        sp_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
		        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
				(activity,ADC_CITY[arg2]);
					sp_city.setAdapter(adapter);
				}
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
        enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String temp = (String) sp_city.getSelectedItem();
				if (!temp.equals("全部")) {
					listFlowHelper.change_if("city like ?", "%" + sp_city.getSelectedItem() + "%");
				} else {
					listFlowHelper.change_if("id>?", "0");
				}
				updateDialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
	}
}
