package com.p004ti.wifi.utils;

import android.content.Context;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

/* renamed from: com.ti.wifi.utils.MDnsHelper_ */
public final class MDnsHelper_ extends MDnsHelper {
    private Context context_;

    private void init_() {
    }

    private MDnsHelper_(Context context) {
        this.context_ = context;
        init_();
    }

    public static MDnsHelper_ getInstance_(Context context) {
        return new MDnsHelper_(context);
    }

    public void rebind(Context context) {
        this.context_ = context;
        init_();
    }

    public void startDiscovery() {
        C10311 r0 = new Task("", 0, "") {
            public void execute() {
                try {
                    MDnsHelper_.super.startDiscovery();
                } catch (Throwable th) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                }
            }
        };
        BackgroundExecutor.execute((Task) r0);
    }

    public void stopDiscovery() {
        C10322 r0 = new Task("", 0, "") {
            public void execute() {
                try {
                    MDnsHelper_.super.stopDiscovery();
                } catch (Throwable th) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                }
            }
        };
        BackgroundExecutor.execute((Task) r0);
    }

    public void restartDiscovery() {
        C10333 r0 = new Task("", 0, "") {
            public void execute() {
                try {
                    MDnsHelper_.super.restartDiscovery();
                } catch (Throwable th) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                }
            }
        };
        BackgroundExecutor.execute((Task) r0);
    }
}
