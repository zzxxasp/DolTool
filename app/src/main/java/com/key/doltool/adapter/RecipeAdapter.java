package com.key.doltool.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.adapter.util.AdapterUtil;
import com.key.doltool.data.sqlite.Recipe;
import com.key.doltool.util.db.SRPUtil;
import com.the9tcat.hadi.DefaultDAO;

public class RecipeAdapter extends BaseAdapter{
	private static class ViewHolder{
		public TextView name;
		
		public RelativeLayout item1,item2,item3;
		public ImageView item_pic1,item_pic2,item_pic3;
		public TextView item_number1,item_number2,item_number3;
		public TextView item_name1,item_name2,item_name3;
		
		public ImageView skill1,skill2;
		public TextView skill1_number,skill2_number;
		public TextView skill1_name,skill2_name;
		
		public ImageView result_pic;
		public TextView result_name;
		public TextView result_txt;
	} 
	
	private List<Recipe> list=new ArrayList<>();
	private Context context;
	private DefaultDAO dao;
	private AdapterUtil util;
	public RecipeAdapter(List<Recipe> list,Context context){
		this.list=list;
		this.context=context;
		dao=SRPUtil.getDAO(context);
		util=new AdapterUtil(context, dao);
	}
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
            // 导入布局并赋值给convertView
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.item_recipe, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			
			holder.item1=(RelativeLayout)convertView.findViewById(R.id.item_1);
			holder.item2=(RelativeLayout)convertView.findViewById(R.id.item_2);
			holder.item3=(RelativeLayout)convertView.findViewById(R.id.item_3);
			
			holder.item_pic1=(ImageView)convertView.findViewById(R.id.img1);
			holder.item_pic2=(ImageView)convertView.findViewById(R.id.img2);
			holder.item_pic3=(ImageView)convertView.findViewById(R.id.img3);
			
			holder.item_number1=(TextView)convertView.findViewById(R.id.txt1);
			holder.item_number2=(TextView)convertView.findViewById(R.id.txt2);
			holder.item_number3=(TextView)convertView.findViewById(R.id.txt3);
			
			holder.item_name1=(TextView)convertView.findViewById(R.id.name1);
			holder.item_name2=(TextView)convertView.findViewById(R.id.name2);
			holder.item_name3=(TextView)convertView.findViewById(R.id.name3);
			
			holder.result_pic=(ImageView)convertView.findViewById(R.id.result_pic);
			holder.result_txt=(TextView)convertView.findViewById(R.id.result_number);
			holder.result_name=(TextView)convertView.findViewById(R.id.result_name);
			
			holder.skill1=(ImageView)convertView.findViewById(R.id.skill1);
			holder.skill2=(ImageView)convertView.findViewById(R.id.skill2);
			
			holder.skill1_number=(TextView)convertView.findViewById(R.id.skill1_number);
			holder.skill2_number=(TextView)convertView.findViewById(R.id.skill2_number);
			
			holder.skill1_name=(TextView)convertView.findViewById(R.id.skill1_name);
			holder.skill2_name=(TextView)convertView.findViewById(R.id.skill2_name);
			// 为view设置标签 
			convertView.setTag(holder);
		} 
		else { 
			// 取出holder 
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		
		holder.item_number1.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "wiki_html/fonts/shift_bold.ttf"));
		holder.item_number2.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "wiki_html/fonts/shift_bold.ttf"));
		holder.item_number3.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "wiki_html/fonts/shift_bold.ttf"));
		
		holder.name.setText(list.get(position).getName());
		String[] temp=list.get(position).getNeed().split(",");
		if(temp.length<1){
			holder.item1.setVisibility(View.GONE);
			holder.item_name1.setVisibility(View.GONE);
		}else{
			holder.item1.setVisibility(View.VISIBLE);
			holder.item_name1.setVisibility(View.VISIBLE);
			String[] message=temp[0].split(" ");
			holder.item_number1.setText(""+message[1]);
			holder.item_name1.setText(""+message[0]);
			util.findByName(holder.item_pic1,message[0],3);
		}
		if(temp.length<2){
			holder.item2.setVisibility(View.GONE);
			holder.item_name2.setVisibility(View.GONE);
		}else{
			holder.item2.setVisibility(View.VISIBLE);
			holder.item_name2.setVisibility(View.VISIBLE);
			String[] message=temp[1].split(" ");
			holder.item_number2.setText(""+message[1]);
			holder.item_name2.setText(""+message[0]);
			util.findByName(holder.item_pic2,message[0],3);
		}
		if(temp.length<3){
			holder.item3.setVisibility(View.GONE);
			holder.item_name3.setVisibility(View.GONE);
		}else{
			holder.item3.setVisibility(View.VISIBLE);
			holder.item_name3.setVisibility(View.VISIBLE);
			String[] message=temp[2].split(" ");
			holder.item_number3.setText(""+message[1]);
			holder.item_name3.setText(""+message[0]);
			util.findByName(holder.item_pic3,message[0],3);
		}
		String[] message_re=list.get(position).getResult_number().split("\n");
		util.findByName(holder.result_pic,message_re[0].trim(),3);
		if(message_re.length>=2){
			holder.result_txt.setVisibility(View.VISIBLE);
			holder.result_txt.setText(""+message_re[1]);
		}else{
			holder.result_txt.setVisibility(View.GONE);
		}
		holder.result_name.setText(""+message_re[0]);
		String[] skill=list.get(position).getLevel_need().split(",");
		if(skill.length==0){
			holder.skill1.setVisibility(View.GONE);
			holder.skill1_name.setVisibility(View.GONE);
			holder.skill1_number.setVisibility(View.GONE);
		}else{
			holder.skill1.setVisibility(View.VISIBLE);
			holder.skill1_number.setVisibility(View.VISIBLE);
			holder.skill1_name.setVisibility(View.VISIBLE);
			String[] message=skill[0].split(" ");
			if(message.length>1){
				holder.skill1_number.setText(""+message[1]);
			}else{
				holder.skill1_number.setText("");
			}
			holder.skill1_name.setText(message[0]);
			util.findByName(holder.skill1,message[0],1);
		}
		if(skill.length==1){
			holder.skill2.setVisibility(View.GONE);
			holder.skill2_number.setVisibility(View.GONE);
			holder.skill2_name.setVisibility(View.GONE);
		}else{
			holder.skill2.setVisibility(View.VISIBLE);
			holder.skill2_name.setVisibility(View.VISIBLE);
			holder.skill2_number.setVisibility(View.VISIBLE);
			String[] message=skill[1].split(" ");
			holder.skill2_name.setText(message[0]);
			holder.skill2_number.setText(""+message[1]);
			util.findByName(holder.skill2,message[0],1);
		}	
		return convertView;
	}
}
