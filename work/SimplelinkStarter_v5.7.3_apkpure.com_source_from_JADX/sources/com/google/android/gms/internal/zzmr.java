package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public final class zzmr {
    private static IntentFilter zzail = new IntentFilter("android.intent.action.BATTERY_CHANGED");

    public static int zzao(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }
        Intent registerReceiver = context.getApplicationContext().registerReceiver(null, zzail);
        int i = 0;
        if (((registerReceiver == null ? 0 : registerReceiver.getIntExtra("plugged", 0)) & 7) != 0) {
            i = 1;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        return ((zzmx.zzqC() ? powerManager.isInteractive() : powerManager.isScreenOn() ? 1 : 0) << true) | i;
    }

    public static float zzap(Context context) {
        Intent registerReceiver = context.getApplicationContext().registerReceiver(null, zzail);
        if (registerReceiver == null) {
            return Float.NaN;
        }
        return ((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1));
    }
}
