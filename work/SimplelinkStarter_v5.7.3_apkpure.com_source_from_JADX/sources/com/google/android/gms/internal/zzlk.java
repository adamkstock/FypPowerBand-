package com.google.android.gms.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class zzlk {
    private static final ExecutorService zzacD = Executors.newFixedThreadPool(2, new zza());

    private static final class zza implements ThreadFactory {
        private final ThreadFactory zzacE;
        private AtomicInteger zzacF;

        private zza() {
            this.zzacE = Executors.defaultThreadFactory();
            this.zzacF = new AtomicInteger(0);
        }

        public Thread newThread(Runnable runnable) {
            Thread newThread = this.zzacE.newThread(runnable);
            StringBuilder sb = new StringBuilder();
            sb.append("GAC_Executor[");
            sb.append(this.zzacF.getAndIncrement());
            sb.append("]");
            newThread.setName(sb.toString());
            return newThread;
        }
    }

    public static ExecutorService zzoj() {
        return zzacD;
    }
}
