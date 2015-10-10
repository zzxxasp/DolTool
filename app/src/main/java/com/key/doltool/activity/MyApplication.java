package com.key.doltool.activity;

import com.key.doltool.data.Deck;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.squareup.leakcanary.LeakCanary;
import com.the9tcat.hadi.HadiApplication;

public class MyApplication extends HadiApplication{
	@Override
	public void onCreate() {
		super.onCreate();
		ParseCrashReporting.enable(this);
		ParseObject.registerSubclass(Deck.class);
		Parse.initialize(this, "nQCl8tOqqeNdkVFHRn1xE7QK1Us5oP46KRClzL9R", "T1g0zv4ctATnB2aR08RYun0aCWSqKbC2gzKmOk6s");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseACL defaultACL = new ParseACL();
		LeakCanary.install(this);
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
