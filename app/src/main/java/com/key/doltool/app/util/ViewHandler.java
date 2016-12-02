package com.key.doltool.app.util;

import android.os.Handler;
import android.os.Message;

/**
 * 通用-ViewHandler
 */
public class ViewHandler extends Handler {
    private ViewCallBack viewCallBack;
    public interface ViewCallBack{
        void onHandleMessage(Message msg);
    }

    public ViewHandler(ViewCallBack viewCallBack) {
        this.viewCallBack=viewCallBack;
    }

    @Override
    public void handleMessage(Message msg) {
        viewCallBack.onHandleMessage(msg);
    }
}
