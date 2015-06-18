package com.key.doltool.activity.person;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SpinnerArrayAdapter;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.event.UpdataList;
import com.key.doltool.event.UserEvent;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.StringUtil;
import com.key.doltool.view.BootstrapCircleThumbnail;
import com.key.doltool.view.Toast;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditUserInfoActivity extends BaseActivity{
	//编辑
	private TextView nickName;
	private TextView area_server;
	private BootstrapCircleThumbnail head;
	private String server_name="0-0";
	private Bitmap photo;
	private RelativeLayout area_1,area_2,area_3;
    private static final int TAKE_PICTURE = 1;
    private static final int LOCAL_PICTURE = 3;
    private Uri imageUri = Uri.parse(FileManager.IMAGE_FILE_LOCATION);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		findView();
		setListener();
		init();
		getSimpleActionBar(true).initActionBar("修改信息",0);
	}
	private void findView(){
		area_1=(RelativeLayout)findViewById(R.id.area_1);
		area_2=(RelativeLayout)findViewById(R.id.area_2);
		area_3=(RelativeLayout)findViewById(R.id.area_3);
		
		head=(BootstrapCircleThumbnail)findViewById(R.id.head_img);
		nickName=(TextView)findViewById(R.id.nick_name);
		area_server=(TextView)findViewById(R.id.area_server);
	}
	private void setListener(){
		area_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getPhotoEvent();
			}
		});
		area_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogPop(R.layout.select_trove);
			}
		});
		area_3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogPop(R.layout.select_area);
			}
		});
	}
	private void dialogPop(final int layout){
		final Dialog updateDialog = new Dialog(this,R.style.updateDialog);
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(this)-30,
                LayoutParams.MATCH_PARENT);
        params.setMargins(10,10,10,10);
		LayoutInflater layoutinflater = context.getLayoutInflater();
		View view = layoutinflater.inflate(layout, null);
        updateDialog.setContentView(view,params);
        updateDialog.show();
		final EditText name=(EditText)view.findViewById(R.id.boat_name);
		if(name!=null){
			name.setHint("输入你要修改的名称");
		}
		final Spinner type1=(Spinner)view.findViewById(R.id.type1);
		final Spinner type2=(Spinner)view.findViewById(R.id.type2);
		if(type1!=null){
	        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
	        		(EditUserInfoActivity.this,UserEvent.AREA);
	                type1.setAdapter(adapter);
	                type1.setOnItemSelectedListener(new OnItemSelectedListener() {
	        			public void onItemSelected(AdapterView<?> arg0, View arg1,
	        					int arg2, long arg3) {
	        		        ArrayAdapter<String> adapter=new SpinnerArrayAdapter
	        				(EditUserInfoActivity.this, UserEvent.HOST[arg2]);
	        		        type2.setAdapter(adapter);
	        				}
	        			public void onNothingSelected(AdapterView<?> arg0) {
	        			
	        			}
	        		});
		}
		final Button positive=(Button)view.findViewById(R.id.btn_confirm);
		final Button negative=(Button)view.findViewById(R.id.btn_cancel);
		positive.setText("修改");
		positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(layout==R.layout.select_trove){
					changeNickName(name.getText().toString().trim());
				}else{
					changeArea(type1,type2);
				}
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
	
	private void changeNickName(String name){
        if (!StringUtil.isNull(name)) {
        	if(name.length()<3&&name.length()<=12){
        		Toast.makeText(this,"昵称在3~12个字符之间",Toast.LENGTH_SHORT).show();
        		return;
        	}
        	ParseUser currentUser = ParseUser.getCurrentUser();
        	nickName.setText(name);
        	currentUser.put("nickName",name);
        	currentUser.saveInBackground(new SaveCallback(){
				public void done(ParseException e) {
					if(e==null){
			        	UpdataList.PIC_CHANGE=1;
					}
				}
        	});
        }else{
        	Toast.makeText(this,"昵称不能为空",Toast.LENGTH_SHORT).show();
        }
	}
	private void changeArea(Spinner area,Spinner server){
		server_name=area.getSelectedItemPosition()+"-"+server.getSelectedItemPosition();
		ParseUser currentUser = ParseUser.getCurrentUser();
		area_server.setText(area.getSelectedItem()+" "+server.getSelectedItem());
    	currentUser.put("server",server_name);
    	currentUser.saveInBackground(new SaveCallback(){
			public void done(ParseException e) {
				if(e==null){
		        	UpdataList.PIC_CHANGE=1;
				}
			}
    	});
	}
	
	private void init(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		//如果有用户则
		if (currentUser != null) {
			ParseFile headImg=currentUser.getParseFile("headPic");
			if(headImg!=null){
				headImg.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, ParseException e) {
						if (e == null) {
							head.setImageBitmap(BitMapUtil.getBitmapByInputStream(data,3));
						} else {
							head.setImageResource(R.drawable.dol_trove_defalut);
						}
					}
				});
			}else{
				head.setImageResource(R.drawable.dol_trove_defalut);
			}
			
			if(!StringUtil.isNull(currentUser.getString("nickName"))){
				nickName.setText(currentUser.getString("nickName"));
			}else{
				nickName.setText(currentUser.getUsername());
			}
			
			if(!StringUtil.isNull(currentUser.getString("server"))){
				String temp[]=currentUser.getString("server").split("-");
				String area=UserEvent.AREA[Integer.parseInt(temp[0])];
				String server=UserEvent.HOST[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])];
				area_server.setText(area+" "+server);

			}else{
				area_server.setText("未填写");
			}
		}
	}
    private void getPhotoEvent() {
    	new DialogEvent().pictureWayDialog(FileManager.IMAGE_FILE_LOCATION,this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            imageUri = Uri.parse(FileManager.IMAGE_FILE_LOCATION);
            return;
        }
        switch (requestCode) {
        case LOCAL_PICTURE:
            imageUri = data.getData();
            test(imageUri);
            break;
        case TAKE_PICTURE:
            cropImageUri(FileManager.IMAGE_FILE_LOCATION);
            break;
        }
    }
    private void test(Uri uri){
        try {
        	String[] pojo = { MediaStore.Images.Media.DATA };
        	Cursor cursor = getContentResolver().query(uri, pojo, null, null, null);
        	if (cursor != null) {
        		int colunm_index = cursor
        				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        		cursor.moveToFirst();
        		String path = cursor.getString(colunm_index);
        		/***
        		 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
        		 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
        		 */
        		if (path.endsWith("jpg") || path.endsWith("png")) {
        			cropImageUri(path);
        		} else {
        			Toast.makeText(this, "格式不支持", Toast.LENGTH_LONG).show();
        		}
        	} else {
        	}
        } catch (Exception e) {
        }
    }
    private void cropImageUri(String path_file) {
        if (imageUri != null) {
        	photo = BitMapUtil.getBitmapByFile(path_file);
        	head.setImageBitmap(photo);
        	ParseUser currentUser = ParseUser.getCurrentUser();
        	ParseFile headImg=new ParseFile("head.png",ResourcesUtil.getBytes(path_file));
        	headImg.saveInBackground();
        	currentUser.put("headPic",headImg);
        	currentUser.saveInBackground(new SaveCallback(){
				public void done(ParseException e) {
					if(e!=null){
			        	UpdataList.PIC_CHANGE=1;
					}
				}
        	});
        }
    }
    @Override
    protected void onDestroy() {
    	if(head.mBitmap!=null){
    		head.mBitmap.recycle();
    		head.mBitmap=null;
    		System.gc();
    	}
    	if(photo!=null){
    		photo.recycle();
    		photo=null;
    		System.gc();
    	}
    	super.onDestroy();
    }
}
