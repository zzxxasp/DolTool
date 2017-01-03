package com.key.doltool.event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.key.doltool.R;
import com.key.doltool.activity.core.MainActivity;
import com.key.doltool.activity.mission.MissonListActivity;
import com.key.doltool.activity.person.PersonActivity;
import com.key.doltool.app.util.ListFlowHelper;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.ExitApplication;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import java.io.File;
import java.util.List;
public class DialogEvent {
	private String select_if;
	private List<String> select_args;
	public DialogEvent(){

	}
	public DialogEvent(String select_if,List<String> select_args){
		this.select_args=select_args;
		this.select_if=select_if;
	}

	/**
	 * 选择对话框
	 * @param context 所在界面的上下文
	 * **/
	public void itemDialog(final ListFlowHelper listFlowHelper, Activity context){
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(R.layout.panel_mission_tag, null);
		final Dialog updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(10, 10, 10, 10);
        updateDialog.setContentView(view,params);
        updateDialog.show();
        LinearLayout layout1=(LinearLayout)view.findViewById(R.id.line1);
        LinearLayout layout2=(LinearLayout)view.findViewById(R.id.line2);
        layout1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				select_if+=" and tag=?";
				select_args.add("1");
				listFlowHelper.change_if(select_if,select_args);
				updateDialog.dismiss();
			}
		});
        layout2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				select_if+=" and tag=?";
				select_args.add("0");
				listFlowHelper.change_if(select_if,select_args);
				updateDialog.dismiss();
			}
		});
	}
	
	/**
	 * loading对话框
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
					Toast.makeText(context.getApplicationContext(), "没有检测到SD卡，无法进行拍照", Toast.LENGTH_LONG).show();
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
				context.syncInfo(AVUser.getCurrentUser().getUsername());
				break;
			//1：重置密码
			case 1:
				context.resetPassword();
				break;
			//2：注销用户
			case 2:
				AVUser.logOut();
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
		updateDialog.setCancelable(true);
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
