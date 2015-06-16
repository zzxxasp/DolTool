package com.key.doltool.activity.squre;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.data.MapItem;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.MutilTouchImageView;
import com.key.doltool.view.flat.FlatButton;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 大航海时代ol世界地图
 * @author key
 * @version 1.0
 * 
 **/
public class MapActivity extends BaseActivity {
	private MutilTouchImageView img;
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
		getSimpleActionBar(true).initActionBar("世界地图",R.drawable.ic_more_vert_white);
		initViews();
	}
	private void initViews() {
		
		name=(TextView)findViewById(R.id.name);
		co_sea=(TextView)findViewById(R.id.co_sea);
		sea=(TextView)findViewById(R.id.sea);
		confrim=(FlatButton)findViewById(R.id.confrim);
		cancel=(FlatButton)findViewById(R.id.cancel);
		
		item=(MapItem)dao.select(MapItem.class,false,"id>?", 
				new String[]{"0"}, null, null, null, null).get(0);
		img=(MutilTouchImageView)findViewById(R.id.map);
		detail=(ViewGroup)findViewById(R.id.detail);
		btn=(Button)findViewById(R.id.btn);
		img.setImageRotateBitmapResetBase(BitMapUtil.readBitMap(this,R.raw.map),true ,true);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String [] seal=item.co_map.split(",");
				img.zoomToPoint(Integer.parseInt(seal[0]),Integer.parseInt(seal[1]));
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
		img.clear();
	}
}