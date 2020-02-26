package com.p004ti.device_selector;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/* renamed from: com.ti.device_selector.AppLifeCycleHandler */
public class AppLifeCycleHandler implements ActivityLifecycleCallbacks, ComponentCallbacks2 {
    public final String TAG = AppLifeCycleHandler.class.getSimpleName();
    boolean appInForeground;
    AppLifeCycleCallback appLifeCycleCallback;

    /* renamed from: com.ti.device_selector.AppLifeCycleHandler$AppLifeCycleCallback */
    interface AppLifeCycleCallback {
        void onAppBackground();

        void onAppForeground();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onLowMemory() {
    }

    public AppLifeCycleHandler(AppLifeCycleCallback appLifeCycleCallback2) {
        this.appLifeCycleCallback = appLifeCycleCallback2;
    }

    public void onActivityResumed(Activity activity) {
        if (!this.appInForeground) {
            this.appInForeground = true;
            this.appLifeCycleCallback.onAppForeground();
            Log.d(this.TAG, "appInForeground");
        }
    }

    public void onTrimMemory(int i) {
        if (i == 20) {
            this.appInForeground = false;
            this.appLifeCycleCallback.onAppBackground();
            Log.d(this.TAG, "appInBackground");
        }
    }
}
