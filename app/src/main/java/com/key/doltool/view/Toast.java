package com.key.doltool.view;
import android.content.Context;
import androidx.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.key.doltool.R;
/** 
 *自定义Toast
 *@author key 
 **/
public class Toast {
	public final static int LENGTH_LONG=1;
	public final static int LENGTH_SHORT=0;
	private static android.widget.Toast mToast;
	public static android.widget.Toast makeText(Context context ,String msg,int duration){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.toast_view, null);
		TextView txt=(TextView)view.findViewById(R.id.toast_txt);
		txt.setText(msg);
		
		if(mToast==null){
			mToast=new android.widget.Toast(context);
			mToast.setView(view);
			mToast.setDuration(duration);
		}else{
			mToast.setView(view);
			mToast.setDuration(duration);
		}
		return mToast;
	}
	public static android.widget.Toast makeText(Context context ,@StringRes int res_id,int duration){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.toast_view, null);
		TextView txt=(TextView)view.findViewById(R.id.toast_txt);
		txt.setText(res_id);
		if(mToast==null){
			mToast=new android.widget.Toast(context);
			mToast.setView(view);
			mToast.setDuration(duration);
		}else{
			mToast.setView(view);
			mToast.setDuration(duration);
		}
		return mToast;
	}
	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
}
