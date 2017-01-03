package com.key.doltool.app.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 通用滚动方法
 **/
public class ListScrollListener implements OnScrollListener {
    private boolean end_flag=true;
    private Thread mThread;
    private Dialog layout_alert;
    private Handler handler;
    private Activity activity;
    public ListScrollListener(Dialog layout_alert
            , Handler handler, Activity activity) {
        this.handler = handler;
        this.layout_alert = layout_alert;
        this.activity=activity;
    }

    public void changeFlag(boolean flag) {
        this.end_flag = flag;
    }

    //滚动监听① - useless
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    //滚动监听②
    public void onScrollStateChanged(final AbsListView view, int scrollState) {
        //当不滚动时
        System.out.println("" + end_flag);
        if (scrollState == SCROLL_STATE_IDLE && end_flag) {
            //判断滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                //没有线程且不为最末时
                if (mThread == null || !mThread.isAlive()) {
                    //显示进度条，区域操作控制
                    DialogUtil.show(activity,layout_alert);
                    mThread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
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
