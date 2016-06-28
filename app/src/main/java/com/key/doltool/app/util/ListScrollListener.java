package com.key.doltool.app.util;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
/**
 * 通用滚动方法
 * **/
public class ListScrollListener implements OnScrollListener{
	private boolean end_flag;
	private Thread mThread;
	private Dialog layout_alert;
	private Handler handler;
	public ListScrollListener(boolean end_flag,Dialog layout_alert
			,Handler handler){
		this.end_flag=end_flag;
		this.handler=handler;
		this.layout_alert=layout_alert;
	}
	public void changeFlag(boolean flag){
		this.end_flag=flag;
	}
	//滚动监听① - useless
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	//滚动监听②
	public void onScrollStateChanged(final AbsListView view, int scrollState) {
        //当不滚动时
		boolean flag;
		flag=end_flag;
        if(scrollState == SCROLL_STATE_IDLE){  
                System.out.println(view.getFirstVisiblePosition()+"===" + view.getLastVisiblePosition()+"==="+view.getCount());
                //判断滚动到底部   
                if(view.getLastVisiblePosition()==(view.getCount()-1)){
                	//没有线程且不为最末时
                    if ((mThread == null || !mThread.isAlive())&&flag) {
                    	//显示进度条，区域操作控制
                    	layout_alert.show();
                        mThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }  
                        };
                        mThread.start();                
                    }
                }
        	}
	}
}
