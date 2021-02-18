package com.key.doltool.activity;

import android.graphics.Typeface;

import androidx.multidex.MultiDex;

import com.key.doltool.data.parse.Deck;
import com.key.doltool.event.CrashHandler;
import com.key.doltool.util.FontManager;
import com.the9tcat.hadi.HadiApplication;

import cn.leancloud.AVInstallation;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;

public class MyApplication extends HadiApplication{
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init(this);
		AVObject.registerSubclass(Deck.class);
		AVOSCloud.initialize(this, "d7iXxubw81WQ0vojBtnwpxqn-MdYXbMMI", "bbEOe1mKAgp7C96DGrO7WTnK");
		AVInstallation.getCurrentInstallation().saveInBackground();
		FontManager.setDefaultFont(this, Typeface.DEFAULT.toString(), FontManager.DEFAULT_FONT);
	}

	@Override
	protected void attachBaseContext(android.content.Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
