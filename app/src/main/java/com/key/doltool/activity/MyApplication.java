package com.key.doltool.activity;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.key.doltool.data.parse.Deck;
import com.key.doltool.event.CrashHandler;
import com.key.doltool.util.FontManager;
import com.squareup.leakcanary.LeakCanary;
import com.the9tcat.hadi.HadiApplication;

public class MyApplication extends HadiApplication{
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init(this);
		AVObject.registerSubclass(Deck.class);
		AVOSCloud.initialize(this, "D6rvDcHFKqPHsxuhn3CSaIzl-gzGzoHsz", "l4dMbKQEpcNleHu2rL90bmYd");
		AVInstallation.getCurrentInstallation().saveInBackground();
		LeakCanary.install(this);
		FontManager.setDefaultFont(this, "monospace",FontManager.DEFAULT_FONT);
	}
}
