package com.key.doltool.event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.activity.mission.MissonListActivity;
import com.key.doltool.activity.person.PersonActivity;
import com.key.doltool.activity.search.SearchActivity;
import com.key.doltool.activity.search.SearchFragment;
import com.key.doltool.adapter.ListWordAdapter;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.DensityUtil;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;
public class DialogEvent {
	private PopupWindow pop;
	private SearchFragment context;
	private SearchActivity context_2;
	private String select_if;
	private List<String> select_args;
	public DialogEvent(){

	}
	public DialogEvent(String select_if,List<String> select_args){
		this.select_args=select_args;
		this.select_if=select_if;
	}
	public DialogEvent(PopupWindow pop,SearchFragment context){
		this.context=context;
		this.pop=pop;
	}
	public DialogEvent(PopupWindow pop,SearchActivity context){
		this.context_2=context;
		this.pop=pop;
	}
	@SuppressWarnings("deprecation")
	public void popMenuForShare(View anchor){
		if(pop==null){
			pop=new PopupWindow(context.getActivity());
		}
		if(!pop.isShowing()){
			LayoutInflater layoutinflater =context.getActivity().getLayoutInflater();
			View view = layoutinflater.inflate(R.layout.panel_search, null);
			LinearLayout item1=(LinearLayout)view.findViewById(R.id.list_item_type1);
			LinearLayout item2=(LinearLayout)view.findViewById(R.id.list_item_type2);
			LinearLayout item3=(LinearLayout)view.findViewById(R.id.list_item_type3);
			LinearLayout item4=(LinearLayout)view.findViewById(R.id.list_item_type4);
			LinearLayout item5=(LinearLayout)view.findViewById(R.id.list_item_type5);
			pop.setContentView(view);
			pop.setWidth(DensityUtil.dip2px(context.getActivity(),80));
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(true);
			pop.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
			pop.showAsDropDown(anchor,0,0);
			final TextView txt=(TextView)anchor;
			item1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("任务");
					context.setIndex(0);
					pop.dismiss();
				}
			});
			item2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("发现物");
					context.setIndex(1);
					pop.dismiss();
				}
			});
			item3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("副官");
					context.setIndex(2);
					pop.dismiss();
				}
			});
			item4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("生产书");
					context.setIndex(3);
					pop.dismiss();
				}
			});
			item5.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("城市");
					context.setIndex(4);
					pop.dismiss();
				}
			});
		}
		else{
			pop.dismiss();
		}
	}

	public void popMenuForSearch(View anchor){
		if(pop==null){
			pop=new PopupWindow(context_2);
		}
		if(!pop.isShowing()){
			LayoutInflater layoutinflater =context_2.getLayoutInflater();
			View view = layoutinflater.inflate(R.layout.panel_search, null);
			LinearLayout item1=(LinearLayout)view.findViewById(R.id.list_item_type1);
			LinearLayout item2=(LinearLayout)view.findViewById(R.id.list_item_type2);
			LinearLayout item3=(LinearLayout)view.findViewById(R.id.list_item_type3);
			LinearLayout item4=(LinearLayout)view.findViewById(R.id.list_item_type4);
			LinearLayout item5=(LinearLayout)view.findViewById(R.id.list_item_type5);
			pop.setContentView(view);
			pop.setWidth(DensityUtil.dip2px(context_2, 80));
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(true);
			pop.setBackgroundDrawable(new BitmapDrawable(context_2.getResources()));
			pop.showAsDropDown(anchor,0,0);
			final TextView txt=(TextView)anchor;
			item1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("任务");
					context_2.setIndex(0);
					pop.dismiss();
				}
			});
			item2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("发现物");
					context_2.setIndex(1);
					pop.dismiss();
				}
			});
			item3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("副官");
					context_2.setIndex(2);
					pop.dismiss();
				}
			});
			item4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("生产书");
					context_2.setIndex(3);
					pop.dismiss();
				}
			});
			item5.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					txt.setText("城市");
					context_2.setIndex(4);
					pop.dismiss();
				}
			});
		}
		else{
			pop.dismiss();
		}
	}


	/**
	 * 选择对话框
	 * @param context 所在界面的上下文
	 * **/
	public void itemDialog(final MissonListActivity context){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.panel_mission_tag, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(10,10,10,10);
        updateDialog.setContentView(view,params);
        updateDialog.show();
        LinearLayout layout1=(LinearLayout)view.findViewById(R.id.line1);
        LinearLayout layout2=(LinearLayout)view.findViewById(R.id.line2);
        layout1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				select_if+=" and tag=?";
				select_args.add("1");
				context.change_if(select_if,select_args);
				context.begin();
				updateDialog.dismiss();
			}
		});
        layout2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				select_if+=" and tag=?";
				select_args.add("0");
				context.change_if(select_if,select_args);
				context.begin();
				updateDialog.dismiss();
			}
		});
	}
	
	/**
	 * 选择对话框
	 * @param txt 要显示的TextView
	 * @param context 所在界面的上下文
	 * **/
	public Dialog itemDialog(Activity context,String txt){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.loading_layout, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        updateDialog.setContentView(view, params);
        TextView loading_txt=(TextView)view.findViewById(R.id.loaing_txt);
        if(!StringUtil.isNull(txt)){
            loading_txt.setText(txt);
        }
        return updateDialog;
	}
	
	/**
	 * 选择对话框
	 * @param txt 要显示的TextView
	 * @param context 所在界面的上下文
	 * **/
	public void itemDialog(final String[] word, final TextView txt,Activity context){
		txt.setTag(0);
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.pop_word, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(10, 10, 10, 10);
        updateDialog.setContentView(view, params);
        updateDialog.show();
        ListView listview=(ListView)view.findViewById(R.id.listview);
        listview.setAdapter(new ListWordAdapter(word,context));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				txt.setText(word[arg2]);
				txt.setTag(arg2);
				updateDialog.dismiss();
			}
		});
	}
	
	/**
	 *  确认对话框
	 * @param title 要显示的标题
	 * @param message 显示的内容
	 * @param anime 动画ID
	 * **/
	public void materialDialog(int anime,String title,String message,final Activity context,final int num){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.panel_material_dialog, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        if(anime!=0){
            updateDialog.getWindow().setWindowAnimations(anime);
        }
        updateDialog.setContentView(view, params);
        updateDialog.show();
        TextView title_view=(TextView)view.findViewById(R.id.title);
        TextView content=(TextView)view.findViewById(R.id.message);
        title_view.setText(title);
        content.setText(message);
        FlatButton confrim=(FlatButton)view.findViewById(R.id.confrim);
        FlatButton cancel=(FlatButton)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateDialog.dismiss();
			}
		});
        confrim.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (context instanceof MainActivity) {
					ExitApplication.getInstance().exit();
				}
				if (context instanceof PersonActivity) {
					judge(num, (PersonActivity) context);
				}
				updateDialog.dismiss();
			}
		});
	}
	
	/**
	 * 选择获取图片方式对话框
	 * @param temp_url 缓存路径
	 * @param context 所在界面的上下文
	 * **/
	public void pictureWayDialog(final String temp_url,final Activity context){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.pop_chose, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(10, 10, 10, 10);
        updateDialog.setContentView(view, params);
        if(!context.isFinishing()){
            updateDialog.show();
        }
        TextView photo_btn=(TextView)view.findViewById(R.id.photo_btn);
        TextView file_btn=(TextView)view.findViewById(R.id.file_btn);
        photo_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (CommonUtil.hasSDCard()) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(temp_url)));
					context.startActivityForResult(intent, 1);
					updateDialog.dismiss();
				} else {
					Toast.makeText(context, "没有检测到SD卡，无法进行拍照", Toast.LENGTH_LONG).show();
					updateDialog.dismiss();
				}
			}
		});
        file_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				context.startActivityForResult(galleryIntent, 3);
				updateDialog.dismiss();

			}
		});
	}
	
	
	private void judge(int num,PersonActivity context){
		switch(num){
			//0:数据同步
			case 0:
				context.syncInfo(ParseUser.getCurrentUser().getUsername());
				break;
			//1：重置密码
			case 1:
				context.resetPassword();
				break;
			//2：注销用户
			case 2:
				ParseUser.logOut();
				Intent intent = new Intent(context,MainActivity.class);
				context.setResult(Activity.RESULT_OK, intent);
				context.finish();
				break;
		}
	}

	public Dialog showLoading(Activity context){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.loading_layout, null);
		final Dialog updateDialog = new Dialog(context,R.style.updateDialog);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(view, params);
		return updateDialog;
	}

	public Dialog showLoading(Activity context,String title){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.loading_layout, null);
		final Dialog updateDialog = new Dialog(context,R.style.updateDialog);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
		updateDialog.setContentView(view, params);
		TextView txt=(TextView)view.findViewById(R.id.loaing_txt);
		txt.setText(title);
		return updateDialog;
	}
}
