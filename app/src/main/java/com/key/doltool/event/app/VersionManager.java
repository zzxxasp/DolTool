package com.key.doltool.event.app;

import android.app.Activity;

public class VersionManager {

    private static VersionManager versionManager = null;
    private Activity activity;
    public static VersionManager getInstance() {
        if (versionManager == null) {
            versionManager = new VersionManager();
        }
        return versionManager;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void checkVersion(final boolean flag) {
    }
}
