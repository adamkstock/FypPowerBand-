package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class zza implements ServiceConnection {
    boolean zzZW = false;
    private final BlockingQueue<IBinder> zzZX = new LinkedBlockingQueue();

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.zzZX.add(iBinder);
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    public IBinder zzno() throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("BlockingServiceConnection.getService() called on main thread");
        } else if (!this.zzZW) {
            this.zzZW = true;
            return (IBinder) this.zzZX.take();
        } else {
            throw new IllegalStateException();
        }
    }
}
