package com.key.doltool.activity.adventure;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.view.ActionMode;

import com.key.doltool.R;
import com.key.doltool.activity.BaseAdventureActivity;
import com.key.doltool.adapter.TroveAdapter;
import com.key.doltool.data.Trove;
import com.key.doltool.event.UpdataCount;
import com.key.doltool.event.UpdataList;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.the9tcat.hadi.DefaultDAO;

public class AdventureListNewApiActivity extends BaseAdventureActivity{
	private GridView gridview;
	private LinearLayout alert;
	private List<Trove> list;
	private List<Trove> temp_list=new ArrayList<>();
	private DefaultDAO dao;
	private SRPUtil srp;
	private String type;
	private UpdataCount count;
	private Parcelable state;
	private TextView txt;
	private String select_txt="";
	
	private TroveAdapter mGridAdapter;
    private ModeCallback mCallback;
    private boolean MODE_FLAG=false;
    private int keyCode=0;
    private int[] temp_staus;
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//更新页面
			alert.setVisibility(View.GONE);
			state=gridview.onSaveInstanceState();
			mGridAdapter=new TroveAdapter(list, AdventureListNewApiActivity.this,true);
			gridview.setAdapter(mGridAdapter);
			gridview.onRestoreInstanceState(state);
		}
	 };
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adventure_list_main);
		type=getIntent().getStringExtra("type");
		dao=SRPUtil.getDAO(this);
		srp=SRPUtil.getInstance(this);
		count=new UpdataCount(this);
		list=new ArrayList<>();
		findView();
		setListener();
		new Thread(mTasks).start();
	}
	private void findView(){
		alert=(LinearLayout)findViewById(R.id.layout_alert);
		flag=false;
		initToolBar(onMenuItemClick);
		toolbar.setTitle(type);
		txt=(TextView)findViewById(R.id.null_txt);
		alert.setVisibility(View.VISIBLE);
	}
	/**批量标记**/
	private void mutilMode() {
		startSupportActionMode(mCallback);
		//To-do
		toolbar.setVisibility(View.GONE);
		MODE_FLAG=true;
	}
	@SuppressWarnings("unchecked")
	//查询
	public void select(String select){
		select_txt=select;
		List<Trove>list=(List<Trove>)dao.select(Trove.class, false, "type=? and name like ?",new String[]{type,"%"+select_txt+"%"}, null, null,"rate desc,feats desc", null);
		if(list.size()==0){
			txt.setVisibility(View.VISIBLE);

		}else{
			txt.setVisibility(View.GONE);
		}
		mGridAdapter=new TroveAdapter(list, AdventureListNewApiActivity.this,true);
		gridview.setAdapter(mGridAdapter);
	}
	private void setListener(){
		mCallback = new ModeCallback();
		gridview=(GridView)findViewById(R.id.gridview);
		mGridAdapter=new TroveAdapter(list, AdventureListNewApiActivity.this,true);
		gridview.setAdapter(mGridAdapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(MODE_FLAG){
					int value = mGridAdapter.getItemState()[arg2] == 1 ? 0 : 1;
					mGridAdapter.getItemState()[arg2] = value ;
					mCallback.setSeletedCountShow();
					mGridAdapter.notifyDataSetChanged();
				}else{
					Intent intent=new Intent(AdventureListNewApiActivity.this,AdventureDetailActivity.class);
					intent.putExtra("id",mGridAdapter.getItem(arg2).getId());
					jump(intent);
				}
			}
		});
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
				if (MODE_FLAG) {
					return false;
				} else {
					if (mGridAdapter.getItem(position).getFind_flag() == 0) {
						Trove trove = new Trove();
						trove.setFind_flag(1);
						dao.update(trove, new String[]{"flag"}, "id=? and type=?", new String[]{"" + mGridAdapter.getItem(position).getId(), type});
						count.update_addMode(type, 1);
					} else {
						Trove trove = new Trove();
						trove.setFind_flag(0);
						dao.update(trove, new String[]{"flag"}, "id=? and type=?", new String[]{"" + mGridAdapter.getItem(position).getId(), type});
						count.update_addMode(type, -1);
					}
					alert.setVisibility(View.VISIBLE);
					new Thread(mTasks).start();
					return true;
				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		list.clear();
		UpdataList.FLAG_CHANGE_LIST=1;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		if(UpdataList.FLAG_CHANGE==1){
			alert.setVisibility(View.VISIBLE);
			new Thread(mTasks).start();
			UpdataList.FLAG_CHANGE=0;
		}
		super.onResume();
	}
	private void jump(Intent intent){
		UpdataList.FLAG_CHANGE=0;
		AdventureListNewApiActivity.this.startActivity(intent);
	}
	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!select_txt.equals("")){
			select_txt="";
			mGridAdapter=new TroveAdapter(list, this,true);
			gridview.setAdapter(mGridAdapter);
			Toast.makeText(getApplicationContext(), "重置搜索条件", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}
	@SuppressWarnings("unchecked")
	private Runnable mTasks =new Runnable(){
		public void run() {
			list=(List<Trove>)dao.select(Trove.class, false, "type=?",new String[]{type}, null, null,"rate desc,feats desc", null);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mHandler.sendMessage(mHandler.obtainMessage());
		}
	};
	private class ModeCallback implements ActionMode.Callback {
        private View mMultiSelectActionBarView;
        private TextView mSelectedConvCount;
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        	keyCode=0;
            // comes into MultiChoiceMode
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.multi_select_menu, menu);

            if (mMultiSelectActionBarView == null) {
                mMultiSelectActionBarView = LayoutInflater.from(AdventureListNewApiActivity.this)
                    .inflate(R.layout.list_multi_select_actionbar, null);

                mSelectedConvCount =
                    (TextView)mMultiSelectActionBarView.findViewById(R.id.selected_conv_count);
            }
            mode.setCustomView(mMultiSelectActionBarView);
            ((TextView)mMultiSelectActionBarView.findViewById(R.id.title))
                .setText("已选择");
            Log.i("mGridAdapter",mGridAdapter.getCount()+"");
            temp_staus=new int[mGridAdapter.getCount()];
            for(int i=0;i<mGridAdapter.getCount();i++){
                if (mGridAdapter.getItem(i).getFind_flag()==1) {
                	mGridAdapter.getItemState()[i] = 1;
                    temp_staus[i]=1;
                	gridview.setSelection(i);
                } else {
                	mGridAdapter.getItemState()[i] = 0;
                	temp_staus[i]=0;
                }
            }
            mGridAdapter.notifyDataSetChanged();
            setSeletedCountShow();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mMultiSelectActionBarView == null) {
                ViewGroup v = (ViewGroup)LayoutInflater.from(AdventureListNewApiActivity.this)
                    .inflate(R.layout.list_multi_select_actionbar, null);
                mode.setCustomView(v);
                mSelectedConvCount = (TextView)v.findViewById(R.id.selected_conv_count);
            }
            android.view.MenuItem mItem = menu.findItem(R.id.action_slelect);
            if(mGridAdapter.isAllChecked()){
            	mItem.setTitle("取消全选");
            }else{
            	mItem.setTitle("全部选中");
            }
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode,android.view.MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_slelect:
                    if(mGridAdapter.isAllChecked()){
                    	mGridAdapter.uncheckAll();
						//To-do
                    }else{
                    	mGridAdapter.checkAll();
                    	for(int i = 0;i<mGridAdapter.getCount();i++)
                    		gridview.setSelection(i);
                    }
                    mGridAdapter.notifyDataSetChanged();
                    mSelectedConvCount.setText(Integer.toString(mGridAdapter.getCheckedItemCount()));
                    break;

                default:
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        	MODE_FLAG=false;
			toolbar.setVisibility(View.VISIBLE);
        	if(keyCode==0){
            	//更新数据
            	updataForMutil();
        	}
        }
        
        public void setSeletedCountShow(){
        	mSelectedConvCount.setText(Integer.toString(mGridAdapter.getCheckedItemCount()));
        }

    }
	private void updataForMutil(){
		alert.setVisibility(View.VISIBLE);
		new Thread(mTask_Muti).start();
	}
	
	private Runnable mTask_Muti =new Runnable(){
		@SuppressWarnings("unchecked")
		public void run() {
            Log.i("start", "start");
			int total=0;
			boolean flag=false;
			for(int i=0;i<mGridAdapter.getCount();i++){
				if(mGridAdapter.getItemState()[i]==1&&mGridAdapter.getItemState()[i]!=temp_staus[i]){
					Trove trove=mGridAdapter.getItem(i);
					trove.setFind_flag(1);
					flag=true;
					temp_list.add(trove);
//					dao.update(trove, new String[]{"flag"}, "id=? and type=?", new String[]{""+mGridAdapter.getItem(i).getId(),type});
				}else if(mGridAdapter.getItemState()[i]==0&&mGridAdapter.getItemState()[i]!=temp_staus[i]){
					Trove trove=mGridAdapter.getItem(i);
					trove.setFind_flag(0);
					flag=true;
					temp_list.add(trove);
//					dao.update(trove, new String[]{"flag"}, "id=? and type=?", new String[]{""+mGridAdapter.getItem(i).getId(),type});
				}
				if(mGridAdapter.getItemState()[i]==1){
					total++;
				}
			}
			srp.update_list(temp_list);
			temp_list.clear();
			if(flag){
				count.update_adventure(type,total);
				list=(List<Trove>)dao.select(Trove.class, false, "type=?",new String[]{type}, null, null,"rate desc,feats desc", null);
			}
			mHandler.sendMessage(mHandler.obtainMessage());
            Log.i("over", "over");
		}
	};
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:popWindow();break;
				case R.id.type_search:mutilMode();break;

			}
			return true;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.trove_menu, menu);
		return true;
	}
}