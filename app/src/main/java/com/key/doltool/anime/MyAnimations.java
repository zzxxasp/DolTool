package com.key.doltool.anime;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;



/**
 * 各类动画存储类，说明:<br>
 * 里面的所有动画都是补间动画，没有帧动画有单例和复合动画，实现比较简单，主要
 * 是动画的和其他的设置，同样建议在使用动画时，将所有动画效果加入此类，或新建一个类
 * 进行管理，并进行说明动画的效果（怎么使用倒是没那么困难，基本上是哪个动画，多少毫秒）
 * @author key
 */
public class MyAnimations {
	/**
	 * @param view 要旋转的ViewGroup
	 * @param view_g 消失的View
	 * @param view_v 显示的View
	 * @param durationMillis 时间
	 **/
	public static void rotate3D(final ViewGroup view,final ViewGroup view_g,final ViewGroup view_v,final int durationMillis){
		ObjectAnimator gone=ObjectAnimator.ofFloat(view,"alpha",1f,0f);
		gone.setDuration(durationMillis);
		gone.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationCancel(final Animator animation) {
			}

			@Override
			public void onAnimationEnd(final Animator animation) {
				view_g.setVisibility(View.GONE);
				view_v.setVisibility(View.VISIBLE);
				ObjectAnimator show =
						ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
				show.setDuration(durationMillis);
				show.start();
			}

			@Override
			public void onAnimationRepeat(final Animator animation) {
			}

			@Override
			public void onAnimationStart(final Animator animation) {
			}
		});
		gone.start();
	}
	/** 淡入动画，适用于子布局的一个个淡入的动画
	 * @param viewgroup 要淡入的ViewGroup(不是他自己，而是子布局的动画)
	 * @param durationMillis 时间
	 **/
	public static void fadein(ViewGroup viewgroup ,int durationMillis){
		for(int i=viewgroup.getChildCount()-1;i>0;i--){
			View layout=viewgroup.getChildAt(viewgroup.getChildCount()-i);
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

	public static void wordAnime(View view,int durationMillis,Animator.AnimatorListener listener){
		ObjectAnimator anime1=ObjectAnimator.ofFloat(view, "translationX", 0, -240);
		ObjectAnimator anime2=ObjectAnimator.ofFloat(view, "translationY", 0, -400);
		ObjectAnimator anime3=ObjectAnimator.ofFloat(view, "ScaleX", 1,2);
		ObjectAnimator anime4=ObjectAnimator.ofFloat(view, "ScaleY", 1,2);
		ObjectAnimator anime5=ObjectAnimator.ofFloat(view, "alpha",1,0);
		anime1.setDuration(durationMillis);
		anime2.setDuration(durationMillis);
		anime3.setDuration(durationMillis/2+100);
		anime4.setDuration(durationMillis/2+100);
		anime5.setDuration(durationMillis/2+100);
		AnimatorSet set=new AnimatorSet();
		set.play(anime1).with(anime2).before(anime3);
		set.play(anime3).with(anime4).with(anime5);
		set.setInterpolator(new AccelerateInterpolator());
		set.addListener(listener);
		set.start();
	}
	public static void wordAnime2(View view,int durationMillis){
		ObjectAnimator anime1=ObjectAnimator.ofFloat(view, "translationX", -360,0);
		ObjectAnimator anime2=ObjectAnimator.ofFloat(view, "translationY", -600,0);
		ObjectAnimator anime3=ObjectAnimator.ofFloat(view, "ScaleX", 2,1);
		ObjectAnimator anime4=ObjectAnimator.ofFloat(view, "ScaleY", 2,1);
		ObjectAnimator anime5=ObjectAnimator.ofFloat(view, "alpha",0,1);
		anime1.setDuration(durationMillis);
		anime2.setDuration(durationMillis);
		anime3.setDuration(durationMillis/2+100);
		anime4.setDuration(durationMillis/2+100);
		anime5.setDuration(durationMillis/2+100);
		AnimatorSet set=new AnimatorSet();
		set.play(anime3).with(anime4).with(anime5).before(anime2);
		set.play(anime2).with(anime1);
		set.setInterpolator(new AccelerateInterpolator());
		set.start();
	}
}