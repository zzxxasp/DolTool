package com.key.doltool.event;

import java.io.IOException;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
/**交易处理事件**/
public class TradeEvent {
	private String[] type_array={
			"食品","调味料","家畜","医药品","杂货",
			"酒类","嗜好品","香辛料","纤维","纺织品",
			"染料","贵金属","矿石","香料","宝石",
			"武器","火器","美术品","工艺品","工业制品"
	};
	private String[] pic_array={
			"dol_sk1s2","dol_sk2s2","dol_sk14s2","dol_sk6s2","dol_sk20s2",
			"dol_sk3s2","dol_sk4s2","dol_sk5s2","dol_sk7s2","dol_sk8s2",
			"dol_sk9s2","dol_sk10s2","dol_sk11s2","dol_sk12s2","dol_sk13s2",
			"dol_sk15s2","dol_sk16s2","dol_sk17s2","dol_sk18s2","dol_sk19s2"
	};
	private Context context;
	public TradeEvent(Context context) {
		this.context=context;
	}
	public void setNumber(int trade,ImageView tag_number,TextView number_txt){
		switch(trade){
			case 1:
				tag_number.setImageResource(R.drawable.trade_1);
				number_txt.setText("第一类");
				break;
			case 2:				
				tag_number.setImageResource(R.drawable.trade_2);
				number_txt.setText("第二类");
				break;
			case 3:
				tag_number.setImageResource(R.drawable.trade_3);
				number_txt.setText("第三类");
				break;
			case 4:
				tag_number.setImageResource(R.drawable.trade_4);
				number_txt.setText("第四类");
				break;
		}
	}
	public void setType(String type,ImageView tag_type,TextView type_txt){
		for(int i=0;i<type_array.length;i++){
			if(type_array[i].equals(type)){
				try {
					tag_type.setImageBitmap(BitMapUtil.getBitmapByInputStream(context.getAssets().open(FileManager.SKILL+pic_array[i]+".png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				type_txt.setText(type);
			}
		}
	}
	public String getRecipeTypeByIndex(int index){
		String type="";
		switch(index){
			case 1:type="烹饪";break;
			case 2:type="缝纫";break;
		}
		return type;
	}
}
