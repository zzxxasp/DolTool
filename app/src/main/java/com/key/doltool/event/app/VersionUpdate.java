package com.key.doltool.event.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cundong.utils.PatchUtils;
import com.key.doltool.R;
import com.key.doltool.util.CommonUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.view.NumberProgressBar;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;




/**
 * @ClassName VersionUpdate
 * @Description 用于版本更新
 * @author Administrator
 * @date 2011-8-23 下午03:54:16
 * @version 1.0
 */
public class VersionUpdate {
	static {
		System.loadLibrary("apkpatch");
	}
    private final Activity context;
    private NumberProgressBar downloadProgressBar;
    private Dialog updateDialog;
    private Version version;
    private DownloadThread downloadThread;
    private TextView msgText;
    private String appname;

    private ViewGroup layout1,layout2;
    private FlatButton sureBt,cannelBt,cannelBt2;
    private File apkFile;
    private String old_path=FileManager.getSaveFilePath()+FileManager.DOWNLOAD + "/"+"dol_old.apk";
    private String new_path=FileManager.getSaveFilePath()+FileManager.DOWNLOAD + "/"+"dol_new.apk";
    private String patch_path=FileManager.getSaveFilePath()+FileManager.DOWNLOAD + "/"+"dol_patch.apk";
    public VersionUpdate(Context context) {
        this.context = (Activity) context;
    }


    public void updateVersion(String appname, String oldversioncode, Version version) {
        this.version = version;
        this.appname = appname;
        context.runOnUiThread(new UpdateDialogRunnable());
    }


    private void showUpdateUI() {
        LayoutInflater layoutinflater = context.getLayoutInflater();
        View view = layoutinflater.inflate(R.layout.update_progress_dialog, null);

        downloadProgressBar = (NumberProgressBar) view.findViewById(R.id.upprogress);
        sureBt = (FlatButton) view.findViewById(R.id.update_btn);
        cannelBt = (FlatButton) view.findViewById(R.id.cancel_btn);
        cannelBt2=(FlatButton) view.findViewById(R.id.cancel_btn2);
        layout1=(ViewGroup) view.findViewById(R.id.layout1);
        layout2=(ViewGroup) view.findViewById(R.id.layout2);


        msgText = (TextView) view.findViewById(R.id.detail_txt);
        
        TextView version_txt = (TextView) view.findViewById(R.id.verion_now);
        version_txt.setText("版本:"+version.getVerionCode());
        TextView down_mb = (TextView) view.findViewById(R.id.down_mb);
        down_mb.setText("大小:"+version.getApp_size());
        msgText.setText(version.getUpdataMsg());
        cannelBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDownload();
            }
        });
        cannelBt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDownload();
            }
        });

        sureBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sureBtClicked();
            }
        });

        updateDialog = new Dialog(context, R.style.updateDialog);
        updateDialog.setCancelable(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(context)-30,
                LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        updateDialog.setContentView(view, params);
        updateDialog.getWindow().setWindowAnimations(R.style.update_dialog);
        updateDialog.show();
    }


    private void sureBtClicked() {
        downloadProgressBar.setVisibility(ProgressBar.VISIBLE);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        downloadThread = new DownloadThread();
        downloadThread.start();
    }


    private void cancelDownload() {
        if (downloadThread != null) {
            downloadThread.stopThread(true);
        }
        if (updateDialog != null) {
            updateDialog.dismiss();
            if (null != apkFile && apkFile.exists()) {
                apkFile.delete();
            }
        }
    }


    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(
                Uri.fromFile(new File(new_path)), UpdateConst.PACKAGE_ARCHIVE);

        updateDialog.cancel();

        context.startActivity(intent);

    }


    private class DownloadThread extends Thread {

        private boolean isStop = false;
        private HttpResponse httpResponse = null;

        @Override
        public void run() {

            final HttpClient httpClient = new DefaultHttpClient();

            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 2000);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 4000);
            final HttpGet httpGet = new HttpGet(version.getUpdataUrl());

            try {
                // 启动新线程无法找到资源停滞现�?
                httpResponse = httpClient.execute(httpGet);
            } catch (ClientProtocolException e) {
                httpResponse = null;
                e.printStackTrace();
                context.runOnUiThread(updateMsg);
            } catch (IOException e) {
                httpResponse = null;
                e.printStackTrace();
                context.runOnUiThread(updateMsg);
            } catch (Exception e) {
                httpResponse = null;
                e.printStackTrace();
                context.runOnUiThread(updateMsg);
            }

            if (httpResponse != null
                    && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                new CopyFileThread().start();

            } else {
            	Log.i("",""+httpResponse.getStatusLine().getStatusCode());
                context.runOnUiThread(updateMsg);
            }

        }

        private class CopyFileThread extends Thread {

            @Override
            public void run() {
                try {
                    HttpEntity entity = httpResponse.getEntity();
                    long contentLength = entity.getContentLength();
                    setMaxProgress(contentLength);
                    File ffFile = new File(FileManager.getSaveFilePath()+FileManager.DOWNLOAD);
                    if (!ffFile.exists())
                        ffFile.mkdirs();
                    apkFile = new File(FileManager.getSaveFilePath()+FileManager.DOWNLOAD + "/" + appname + "tem"
                            + UpdateConst.APK_EXTENSION);

                    if (apkFile.exists()) {
                        apkFile.delete();
                    }
                    FileOutputStream outputStream = new FileOutputStream(apkFile);
                    InputStream inputStream = entity.getContent();
                    byte[] bytes = new byte[1024];

                    int alreadyReadCount = 0;
                    int readCount = 0;
                    while ((readCount = inputStream.read(bytes)) > 0 && !isStop) {
                        outputStream.write(bytes, 0, readCount);
                        alreadyReadCount += readCount;
                        updateProgress(alreadyReadCount);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    File file = new File(patch_path);
                    if (file.exists())
                        file.delete();
                    Looper.prepare();
                    Toast.makeText(context, "下载完成，请等待安装.", Toast.LENGTH_SHORT).show();
                    Thread.sleep(1000);
                    apkFile.renameTo(file);
                    Thread.sleep(500);
                    if(!CommonUtil.backupApplication(context,"com.key.doltool",old_path).equals("success")){
                    	Toast.makeText(context, "您的手机并不支持增量更新，今后请尝试直接从网络下载", Toast.LENGTH_SHORT).show();
                    }
        			int ret = PatchUtils.patch(old_path,new_path,patch_path);
        			if(ret!=0){
        				Toast.makeText(context, "补丁安装失败，请尝试从网站上直接下载更新", Toast.LENGTH_SHORT).show();
        			}else{
                        checkInstall(apkFile);
        			}        			
        			//无论成功与否都删除除安装apk外所有文件
        			File file_patch = new File(patch_path);
        			File file_old = new File(old_path);
                    if (file_patch.exists())
                    	file_patch.delete();
                    if (file_old.exists())
                    	file_old.delete();		
                } catch (Exception e) {
                    e.printStackTrace();
                    context.runOnUiThread(updateMsg);
                }

                super.run();
            }

        }

        Runnable updateMsg = new Runnable() {
            @Override
            public void run() {
                downloadProgressBar.setVisibility(View.GONE);
                msgText.setVisibility(View.VISIBLE);
                msgText.setText("下载失败，请稍后再试");
            }
        };


        public void stopThread(boolean isStop) {
            this.isStop = isStop;
        }


        private void checkInstall(File apkFile) {
            if (isStop) {
                apkFile.delete();
            } else {
                installApk();
            }
        }


        private void updateProgress(final int alreadyReadCount) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadProgressBar.setProgress(alreadyReadCount / 1000);
                }
            });
        }


        private void setMaxProgress(final long contentLength) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadProgressBar.setMax(Integer.parseInt((contentLength / 1000) + ""));
                }
            });
        }
    }

    private class UpdateDialogRunnable implements Runnable {
        @Override
        public void run() {
            showUpdateUI();
        }
    }

    public void showUpdateTip() {
        Toast.makeText(context,"", Toast.LENGTH_SHORT).show();
    }
}
