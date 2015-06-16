package com.key.doltool.anime;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
/**
 * 各类动画存储类，说明:<br>
 * 里面的所有动画都是补间动画，没有帧动画有单例和复合动画，实现比较简单，主要
 * 是动画的和其他的设置，同样建议在使用动画时，将所有动画效果加入此类，或新建一个类
 * 进行管理，并进行说明动画的效果（怎么使用倒是没那么困难，基本上是哪个动画，多少毫秒）
 * @author key
 */
public class MyAnimations {
	// 用来适配不同的分辨率
	private static int x=0;
	public static void initOffset(Context context) {
		x = (int) (x*context.getResources().getDisplayMetrics().density);
	}
	/**
	 * 3D旋转，使用于一张纸牌两面的显示切换，即:一个LinearLayout 横向填充<br>
	 * 有两个子布局，都为width:fill,其中一个设为GONE
	 * @param view 要旋转的ViewGroup
	 * @param view_g 消失的View
	 * @param view_v 显示的View
	 * @param durationMillis 时间
	 **/
	public static void rotate3D(final ViewGroup view,final ViewGroup view_g,final ViewGroup view_v,final int durationMillis){
		final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        Rotate3dAnimation rotation =
            new Rotate3dAnimation(0, -90, centerX, centerY, 0.0f, true);
        rotation.setDuration(durationMillis);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new AnimationListener() {
        public void onAnimationEnd(Animation arg0) {
        	view_g.setVisibility(View.GONE);
        	view_v.setVisibility(View.VISIBLE);
            Rotate3dAnimation rotation =
                    new Rotate3dAnimation(90,0, centerX, centerY, 0.0f, true);
                rotation.setDuration(durationMillis);
                rotation.setInterpolator(new DecelerateInterpolator());
                view.startAnimation(rotation);
        }
        public void onAnimationRepeat(Animation arg0) {
        }
        public void onAnimationStart(Animation arg0) {
        }
        });
        view.startAnimation(rotation);
	}
	/** 淡入动画，适用于子布局的一个个淡入的动画
	 * @param viewgroup 要淡入的ViewGroup(不是他自己，而是子布局的动画)
	 * @param durationMillis 时间
	 **/
	public static void fadein(ViewGroup viewgroup ,int durationMillis){
		for(int i=viewgroup.getChildCount()-1;i>0;i--){
			View layout=(View)viewgroup.getChildAt(viewgroup.getChildCount()-i);
			AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1);
			animation1.setFillAfter(true);
			animation1.setDuration(durationMillis);
			animation1.setStartOffset((viewgroup.getChildCount() - i) * 200/ 2);// 下一个动画的偏移时间
			layout.startAnimation(animation1);
			layout.setVisibility(View.VISIBLE);
		}
	}
	public void roate(Context context,View view,int durationMillis){
		RotateAnimation animation1=new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation1.setRepeatMode(Animation.INFINITE);
		animation1.setRepeatCount(Animation.INFINITE);
		animation1.setInterpolator(context, android.R.anim.linear_interpolator);
		animation1.setDuration(durationMillis);
		view.startAnimation(animation1);
	}
}