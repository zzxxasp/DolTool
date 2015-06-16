package com.key.doltool.view;

import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class DrawToggle implements DrawerListener{
	private View toggle,menu;
	private int flag=0;
	public DrawToggle(View toggle,View menu){
		this.toggle=toggle;
		this.menu=menu;
	}
	
	public void onDrawerClosed(View arg0) {
		if(flag==0){
			menu.setVisibility(View.VISIBLE);
			menu.setEnabled(true);
		}
	}

	@Override
	public void onDrawerOpened(View arg0) {
		if(menu.getVisibility()==View.GONE){
			flag=1;
		}else{
			menu.setEnabled(false);
			flag=0;
		}
	}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		ViewHelper.setTranslationX(toggle, -arg1*10);
		ViewHelper.setAlpha(menu,1-arg1);
	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		
	}
}
