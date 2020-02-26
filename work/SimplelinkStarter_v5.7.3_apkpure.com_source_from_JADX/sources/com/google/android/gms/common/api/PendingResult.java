package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import java.util.concurrent.TimeUnit;

public abstract class PendingResult<R extends Result> {

    public interface zza {
        void zzt(Status status);
    }

    public abstract R await();

    public abstract R await(long j, TimeUnit timeUnit);

    public abstract void cancel();

    public abstract boolean isCanceled();

    public abstract void setResultCallback(ResultCallback<? super R> resultCallback);

    public abstract void setResultCallback(ResultCallback<? super R> resultCallback, long j, TimeUnit timeUnit);

    public void zza(zza zza2) {
        throw new UnsupportedOperationException();
    }

    public Integer zznF() {
        throw new UnsupportedOperationException();
    }
}
