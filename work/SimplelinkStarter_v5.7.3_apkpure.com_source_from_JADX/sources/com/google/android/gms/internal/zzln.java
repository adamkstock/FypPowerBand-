package com.google.android.gms.internal;

import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import java.util.concurrent.TimeUnit;

public final class zzln<R extends Result> extends OptionalPendingResult<R> {
    private final zzlc<R> zzacI;

    public zzln(PendingResult<R> pendingResult) {
        if (pendingResult instanceof zzlc) {
            this.zzacI = (zzlc) pendingResult;
            return;
        }
        throw new IllegalArgumentException("OptionalPendingResult can only wrap PendingResults generated by an API call.");
    }

    public R await() {
        return this.zzacI.await();
    }

    public R await(long j, TimeUnit timeUnit) {
        return this.zzacI.await(j, timeUnit);
    }

    public void cancel() {
        this.zzacI.cancel();
    }

    public R get() {
        if (isDone()) {
            return await(0, TimeUnit.MILLISECONDS);
        }
        throw new IllegalStateException("Result is not available. Check that isDone() returns true before calling get().");
    }

    public boolean isCanceled() {
        return this.zzacI.isCanceled();
    }

    public boolean isDone() {
        return this.zzacI.isReady();
    }

    public void setResultCallback(ResultCallback<? super R> resultCallback) {
        this.zzacI.setResultCallback(resultCallback);
    }

    public void setResultCallback(ResultCallback<? super R> resultCallback, long j, TimeUnit timeUnit) {
        this.zzacI.setResultCallback(resultCallback, j, timeUnit);
    }

    public void zza(zza zza) {
        this.zzacI.zza(zza);
    }

    public Integer zznF() {
        return this.zzacI.zznF();
    }
}
