package com.key.doltool.activity.squre;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.sqlite.MapItem;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SystemBarTintManager;
import com.key.doltool.view.flat.FlatButton;
import com.key.doltool.view.img.ImageSource;
import com.key.doltool.view.img.SubsamplingScaleImageView;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 大航海时代ol世界地图
 * @author key
 * @version 1.0
 * 
 **/
public class MapActivity extends BaseActivity {
	private SubsamplingScaleImageView img;
	private Button btn;
	private ViewGroup detail;
	private DefaultDAO dao;
	private MapItem item;
	private TextView name,co_sea,sea;
	private FlatButton confrim,cancel;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dol_map);
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
	}
	private void initViews() {
		name=(TextView)findViewById(R.id.name);
		co_sea=(TextView)findViewById(R.id.co_sea);
		sea=(TextView)findViewById(R.id.sea);
		confrim=(FlatButton)findViewById(R.id.confrim);
		cancel=(FlatButton)findViewById(R.id.cancel);
		
		item=(MapItem)dao.select(MapItem.class,false,"id>?", 
				new String[]{"0"}, null, null, null, null).get(0);
		img=(SubsamplingScaleImageView)findViewById(R.id.map);
		img.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
			@Override
			public void onReady() {
				//加载显示对话框停止显示
				Log.i("Ready","Ready is over");
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
		detail=(ViewGroup)findViewById(R.id.detail);
		btn=(Button)findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String [] seal=item.co_map.split(",");
//				img.setScaleAndCenter(1,new PointF(Integer.parseInt(seal[0]),Integer.parseInt(seal[1])));
				img.setPanEnabled(true);
//				img.setPin(new PointF(Integer.parseInt(seal[0]),Integer.parseInt(seal[1])));
				img.animateScaleAndCenter(1,new PointF(Integer.parseInt(seal[0]),Integer.parseInt(seal[1]))).start();
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