package com.p004ti.wifi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

/* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeAPModeProvisioning_ */
public final class ConfigureWiFiActivityStageThreeAPModeProvisioning_ extends ConfigureWiFiActivityStageThreeAPModeProvisioning implements HasViews {
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    /* renamed from: com.ti.wifi.ConfigureWiFiActivityStageThreeAPModeProvisioning_$IntentBuilder_ */
    public static class IntentBuilder_ extends ActivityIntentBuilder<IntentBuilder_> {
        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, ConfigureWiFiActivityStageThreeAPModeProvisioning_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            super((Context) fragment.getActivity(), ConfigureWiFiActivityStageThreeAPModeProvisioning_.class);
            this.fragmentSupport_ = fragment;
        }

        public PostActivityStarter startForResult(int i) {
            Fragment fragment = this.fragmentSupport_;
            if (fragment != null) {
                fragment.startActivityForResult(this.intent, i);
            } else if (this.context instanceof Activity) {
                ActivityCompat.startActivityForResult((Activity) this.context, this.intent, i, this.lastOptions);
            } else {
                this.context.startActivity(this.intent);
            }
            return new PostActivityStarter(this.context);
        }
    }

    private void init_(Bundle bundle) {
    }

    public void onCreate(Bundle bundle) {
        OnViewChangedNotifier replaceNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(bundle);
        super.onCreate(bundle);
        OnViewChangedNotifier.replaceNotifier(replaceNotifier);
    }

    public <T extends View> T internalFindViewById(int i) {
        return findViewById(i);
    }

    public void setContentView(int i) {
        super.setContentView(i);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public void setContentView(View view) {
        super.setContentView(view);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static IntentBuilder_ intent(Context context) {
        return new IntentBuilder_(context);
    }

    public static IntentBuilder_ intent(Fragment fragment) {
        return new IntentBuilder_(fragment);
    }

    public void scanForSensorTagDevices() {
        C09991 r0 = new Task("", 0, "") {
            public void execute() {
                try {
                    ConfigureWiFiActivityStageThreeAPModeProvisioning_.super.scanForSensorTagDevices();
                } catch (Throwable th) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                }
            }
        };
        BackgroundExecutor.execute((Task) r0);
    }

    public void stopScanningForSensorTagDevices() {
        C10002 r0 = new Task("", 0, "") {
            public void execute() {
                try {
                    ConfigureWiFiActivityStageThreeAPModeProvisioning_.super.stopScanningForSensorTagDevices();
                } catch (Throwable th) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                }
            }
        };
        BackgroundExecutor.execute((Task) r0);
    }
}
