package com.key.doltool.event;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

/**
 * 权限模型事件
 * 0.2
 * **/
public class PermissionEvent {
    private PermissionAllowListener allowListener;
    private Activity context;
    private int code;
    private String permission;
    private String[] permissions;
    /**单一权限申请**/
    public PermissionEvent(final Activity context,String permission,PermissionAllowListener allowListener,int code
    ,String info) {
        this.allowListener = allowListener;
        this.context=context;
        this.code=code;
        this.permission=permission;
        init(info);
    }
    /**多个权限一并申请**/
    public PermissionEvent(final Activity context,String[] permissions,PermissionAllowListener allowListener,int code
            ,String info) {
        this.allowListener = allowListener;
        this.context=context;
        this.code=code;
        this.permissions=permissions;
        init(info);
    }
    /**权限触发事件
     * onPermissionAllow 允许事件
     * onPermissionDenied 拒绝事件
     * **/
    public interface PermissionAllowListener{
        void onPermissionAllow(int code);
        void onPermissionDenied(int code);
    }
    /**放入调用权限的方法之中**/
    public void onRequest(int[] result){
        int grantTemp=0;
        for (int grantResult : result) {
            if (grantResult != 0) {
                grantTemp = grantResult;
            }
        }
        if (grantTemp == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            allowListener.onPermissionAllow(code);
        } else {
            // Permission Denied
            allowListener.onPermissionDenied(code);
        }
    }
    private void init(String info) {
        String[] temp_permissions=new String[]{permission};
        if(permissions!=null){
            temp_permissions=permissions;
        }
        Log.i("s",""+initSuccess(temp_permissions));
        switch (initSuccess(temp_permissions)){
            case 0:
                final String[] finalTemp_permissions = temp_permissions;
                showMessageOKCancel(context, info,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(context, finalTemp_permissions,
                                        code);
                            }
                        });
                break;
            case 1:
                ActivityCompat.requestPermissions(context,temp_permissions,
                        code);
                break;
            case 2:
                allowListener.onPermissionAllow(code);
                break;
        }
    }

    private int initSuccess(String[] temp_permission){
        int temp=0;
        for (String aTemp_permission : temp_permission) {
            if (checkPermission(context,aTemp_permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context,aTemp_permission)) {
                    temp=1;
                }else{
                    return 0;
                }
            }else{
                temp=2;
            }
        }
        return temp;
    }


    /**查询权限是否允许**/
    private boolean checkPermission(Context context,String permission){
        Log.i("a", ""+ContextCompat.checkSelfPermission(context, permission));
        Log.i("b", ""+PackageManager.PERMISSION_GRANTED);
        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**权限取消提示**/
    private void showMessageOKCancel(Context context,String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setTitle("权限提示")
                .setMessage(message)
                .setPositiveButton("确认", okListener)
                .create()
                .show();
    }
}
