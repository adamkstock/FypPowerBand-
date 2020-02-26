package android.support.p000v4.p002os;

import android.os.AsyncTask;

@Deprecated
/* renamed from: android.support.v4.os.AsyncTaskCompat */
public final class AsyncTaskCompat {
    @Deprecated
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(AsyncTask<Params, Progress, Result> asyncTask, Params... paramsArr) {
        if (asyncTask != null) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paramsArr);
            return asyncTask;
        }
        throw new IllegalArgumentException("task can not be null");
    }

    private AsyncTaskCompat() {
    }
}
