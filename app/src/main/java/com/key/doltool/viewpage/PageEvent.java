package com.key.doltool.viewpage;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.key.doltool.R;
import com.key.doltool.util.DensityUtil;
/**
 * ViewPage事件
 * @author key
 * @version 0.1
 */
public class PageEvent implements OnPageChangeListener{
	private int currIndex = 0;
	private int offset=0;
	private int bmpW=0;
	private int number=0;
	private ImageView line;
	private TextView[] txt;
	private Activity context;
	public PageEvent(TextView[] txt,ImageView line,int number,Activity context){
		this.context=context;
		this.txt=txt;
		this.line=line;
		this.number=number;
	}
	public void onPageScrollStateChanged(int arg0) {
		
	}
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	
	}
	public void onPageSelected(int position) {
		Resources resource = context.getBaseContext().getResources();
		ColorStateList gainsboro = resource.getColorStateList(R.color.Gainsboro);
		int one=offset*number+bmpW;
		int two=one*2;
		Animation animation = null;
		switch(position)
		{
		case 0:
		txt[0].setTextColor(Color.WHITE);
		txt[1].setTextColor(gainsboro);
		txt[2].setTextColor(gainsboro);
		if (currIndex == 1) {
			animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
			animation = new TranslateAnimation(two, 0, 0, 0);
		}
		break;
		case 1:	
		txt[1].setTextColor(Color.WHITE);
		txt[0].setTextColor(gainsboro);
		txt[2].setTextColor(gainsboro);
		if (currIndex == 0) {
			animation = new TranslateAnimation(offset, one, 0, 0);
		} else if (currIndex == 2) {
			animation = new TranslateAnimation(two, one, 0, 0);
		}
		break;
		case 2:	
		txt[2].setTextColor(Color.WHITE);
		txt[1].setTextColor(gainsboro);
		txt[0].setTextColor(gainsboro);
		if (currIndex == 0) {
			animation = new TranslateAnimation(offset, two, 0, 0);
		} else if (currIndex == 1) {
			animation = new TranslateAnimation(one, two, 0, 0);
		}
		break;
		}	
		currIndex = position;
		animation.setFillAfter(true);
		animation.setDuration(300);
		line.startAnimation(animation);
	}
	public void initImageView() {
		bmpW =DensityUtil.dip2px(context,90);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW /number - bmpW) / number;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		line.setImageMatrix(matrix);
	}
}