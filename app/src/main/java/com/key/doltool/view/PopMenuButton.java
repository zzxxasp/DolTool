package com.key.doltool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class PopMenuButton extends ImageView {

	    private Context mContext;
	    /** 下拉PopupWindow */
	    private UMSpinnerDropDownItems mPopupWindow;
	    /** 下拉布局文件ResourceId */
	    private int mResId;
	    /** 下拉布局文件创建监听�?*/
	    private ViewCreatedListener mViewCreatedListener;
	    private int type=1;
	    public PopMenuButton(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initButton(context);
	    }

	    public PopMenuButton(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initButton(context);
	    }
	    public PopMenuButton(Context context, final int resourceId,
	            ViewCreatedListener mViewCreatedListener) {
	        super(context);
	        setResIdAndViewCreatedListener(resourceId, mViewCreatedListener);
	        initButton(context);
	    }

	    private void initButton(Context context) {
	        this.mContext = context;
	        // UMSpinnerButton监听事件
	        setOnClickListener(new UMSpinnerButtonOnClickListener());
	    }

	    public PopupWindow getPopupWindow() {
	        return mPopupWindow;
	    }

	    public void setPopupWindow(UMSpinnerDropDownItems mPopupWindow) {
	        this.mPopupWindow = mPopupWindow;
	    }

	    public int getResId() {
	        return mResId;
	    }

	    public void dismiss(){
	        mPopupWindow.dismiss();
	    }

	    public void setResIdAndViewCreatedListener(int mResId, ViewCreatedListener mViewCreatedListener) {
	        this.mViewCreatedListener = mViewCreatedListener;
	        // 下拉布局文件id
	        this.mResId = mResId;
	        // 初始化PopupWindow
	        mPopupWindow = new UMSpinnerDropDownItems(mContext);
	    }

	    class UMSpinnerButtonOnClickListener implements View.OnClickListener {

	        @Override
	        public void onClick(View v) {
	            if (mPopupWindow != null) {
	                if (!mPopupWindow.isShowing()) {
	                    // 设置PopupWindow弹出,�?��样式
	                   // mPopupWindow.setAnimationStyle(android.R.style.);
	                    // 计算popupWindow下拉x轴的位置
//	                    int lx = (PopMenuButton.this.getWidth()
//	                            - mPopupWindow.getmViewWidth() - 7) / 2;
	                    // showPopupWindow
	                    int ly=-5;
	                    if(type==0)
	                    {
	                    ly=-mPopupWindow.getmViewHeight()+5;
	                    }
	                    mPopupWindow.showAsDropDown(PopMenuButton.this, 0, ly);
	                }
	            }
	        }
	    }

	    public class UMSpinnerDropDownItems extends PopupWindow {

	        private Context mContext;
	        /** 下拉视图的宽�?*/
	        private int mViewWidth;
	        /** 下拉视图的高�?*/
	        private int mViewHeight;

	        public UMSpinnerDropDownItems(Context context) {
	            super(context);
	            this.mContext = context;
	            loadViews();
	        }

	        private void loadViews() {
	            // 布局加载器加载布�?���?
	            LayoutInflater inflater = LayoutInflater.from(mContext);
	            final View v = inflater.inflate(mResId, null);
	            // 计算view宽高
	            onMeasured(v);

	            // 必须设置
	            setWidth(LayoutParams.WRAP_CONTENT);
	            setHeight(LayoutParams.WRAP_CONTENT);
	            setContentView(v);
	            setFocusable(true);
	            
	            // 设置布局创建监听器，以便在实例化布局控件对象
	            if (mViewCreatedListener != null) {
	                mViewCreatedListener.onViewCreated(v);
	            }
	        }

	        private void onMeasured(View v) {
	            int w = View.MeasureSpec.makeMeasureSpec(0,
	                    View.MeasureSpec.UNSPECIFIED);
	            int h = View.MeasureSpec.makeMeasureSpec(0,
	                    View.MeasureSpec.UNSPECIFIED);
	            v.measure(w, h);
	            mViewWidth = v.getMeasuredWidth();
	            mViewHeight = v.getMeasuredHeight();
	        }

	        public int getmViewWidth() {
	            return mViewWidth;
	        }

	        public void setmViewWidth(int mViewWidth) {
	            this.mViewWidth = mViewWidth;
	        }

	        public int getmViewHeight() {
	            return mViewHeight;
	        }

	        public void setmViewHeight(int mViewHeight) {
	            this.mViewHeight = mViewHeight;
	        }

	    }

	    public interface ViewCreatedListener {
	        void onViewCreated(View v);
	    }
	    public void settype(int type){
	    	this.type=type;
	    }
	}
