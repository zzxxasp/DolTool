package com.key.doltool.activity.squre;

import android.app.Dialog;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.sqlite.MapItem;
import com.key.doltool.event.DialogEvent;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SystemBarTintManager;
import com.key.doltool.view.flat.FlatButton;
import com.key.doltool.view.img.ImageSource;
import com.key.doltool.view.img.PinView;
import com.key.doltool.view.img.SubsamplingScaleImageView;
import com.the9tcat.hadi.DefaultDAO;

import butterknife.BindView;

/**
 * 大航海时代ol世界地图
 * @author key
 * @version 1.0
 * 
 **/
public class MapActivity extends BaseActivity {

	@BindView(R.id.map) PinView img;
	@BindView(R.id.btn) Button btn;
	@BindView(R.id.detail) ViewGroup detail;
	@BindView(R.id.name) TextView name;
	@BindView(R.id.co_sea) TextView co_sea;
	@BindView(R.id.sea) TextView sea;
	@BindView(R.id.confrim) FlatButton confrim;
	@BindView(R.id.cancel) FlatButton cancel;

	private MapItem item;
	private Dialog dialog;
	private DefaultDAO dao;

	@Override
	public int getContentViewId() {
		return R.layout.dol_map;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		dao=SRPUtil.getDAO(this);
		flag=false;
		initToolBar(null);
		toolbar.setTitle("世界地图");
		toolbar.setBackgroundColor(getResources().getColor(R.color.Black_SP));
		initViews();
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.Black_SP);
		img.setImage(ImageSource.resource(R.raw.map));
		dialog=new DialogEvent().showLoading(this);
		dialog.show();
	}

	private void initViews() {

		item=(MapItem)dao.select(MapItem.class,false,"id>?", 
				new String[]{"0"}, null, null, null, null).get(0);

		img.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
			@Override
			public void onReady() {
				dialog.dismiss();
			}

			@Override
			public void onImageLoaded() {

			}

			@Override
			public void onPreviewLoadError(Exception e) {

			}

			@Override
			public void onImageLoadError(Exception e) {

			}

			@Override
			public void onTileLoadError(Exception e) {

			}
		});

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String [] seal=item.co_map.split(",");
				img.setPanEnabled(true);
				img.animateScaleAndCenter(1.5f,new PointF(Integer.parseInt(seal[0]),Integer.parseInt(seal[1]))).start();
				img.setPin(new PointF(Integer.parseInt(seal[0]),Integer.parseInt(seal[1])));
				showDialog();
			}
		});
        cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				detail.setVisibility(View.GONE);
			}
		});
        confrim.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				detail.setVisibility(View.GONE);
			}
        });
	}
	
	private void showDialog(){
        name.setText("地域名称："+item.name);
        co_sea.setText("所在坐标："+item.co_sea);
        sea.setText("所属海域："+item.sea_name);
        detail.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		img.recycle();
	}
}