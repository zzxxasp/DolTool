package com.key.doltool.app.util;

import android.app.Activity;
import android.app.Dialog;

/**
 * 对话框显示/隐藏泛用
 * Created by key on 2016/12/26.
 */
public class DialogUtil {
    public static void show(Activity activity,Dialog dialog){
        if(activity!=null&&!activity.isFinishing()){
            if(dialog!=null&&!dialog.isShowing()){
                dialog.show();
            }
        }
    }
    public static void dismiss(Activity activity,Dialog dialog){
        if(activity!=null&&!activity.isFinishing()){
            if(dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
