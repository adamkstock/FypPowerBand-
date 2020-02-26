package org.androidannotations.api;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BackgroundExecutor {
    /* access modifiers changed from: private */
    public static final ThreadLocal<String> CURRENT_SERIAL = new ThreadLocal<>();
    public static final Executor DEFAULT_EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    public static final WrongThreadListener DEFAULT_WRONG_THREAD_LISTENER = new WrongThreadListener() {
        public void onUiExpected() {
            throw new IllegalStateException("Method invocation is expected from the UI thread");
        }

        public void onBgExpected(String... strArr) {
            if (strArr.length == 0) {
                throw new IllegalStateException("Method invocation is expected from a background thread, but it was called from the UI thread");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Method invocation is expected from one of serials ");
            sb.append(Arrays.toString(strArr));
            sb.append(", but it was called from the UI thread");
            throw new IllegalStateException(sb.toString());
        }

        public void onWrongBgSerial(String str, String... strArr) {
            if (str == null) {
                str = "anonymous";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Method invocation is expected from one of serials ");
            sb.append(Arrays.toString(strArr));
            sb.append(", but it was called from ");
            sb.append(str);
            sb.append(" serial");
            throw new IllegalStateException(sb.toString());
        }
    };
    private static final String TAG = "BackgroundExecutor";
    /* access modifiers changed from: private */
    public static final List<Task> TASKS = new ArrayList();
    private static Executor executor = DEFAULT_EXECUTOR;
    private static WrongThreadListener wrongThreadListener = DEFAULT_WRONG_THREAD_LISTENER;

    public static abstract class Task implements Runnable {
        /* access modifiers changed from: private */
        public boolean executionAsked;
        /* access modifiers changed from: private */
        public Future<?> future;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public String f89id;
        /* access modifiers changed from: private */
        public AtomicBoolean managed = new AtomicBoolean();
        /* access modifiers changed from: private */
        public long remainingDelay;
        /* access modifiers changed from: private */
        public String serial;
        private long targetTimeMillis;

        public abstract void execute();

        public Task(String str, long j, String str2) {
            String str3 = "";
            if (!str3.equals(str)) {
                this.f89id = str;
            }
            if (j > 0) {
                this.remainingDelay = j;
                this.targetTimeMillis = SystemClock.elapsedRealtime() + j;
            }
            if (!str3.equals(str2)) {
                this.serial = str2;
            }
        }

        public void run() {
            if (!this.managed.getAndSet(true)) {
                try {
                    BackgroundExecutor.CURRENT_SERIAL.set(this.serial);
                    execute();
                } finally {
                    postExecute();
                }
            }
        }

        /* access modifiers changed from: private */
        public void postExecute() {
            if (this.f89id != null || this.serial != null) {
                BackgroundExecutor.CURRENT_SERIAL.set(null);
                synchronized (BackgroundExecutor.class) {
                    BackgroundExecutor.TASKS.remove(this);
                    if (this.serial != null) {
                        Task access$900 = BackgroundExecutor.take(this.serial);
                        if (access$900 != null) {
                            if (access$900.remainingDelay != 0) {
                                access$900.remainingDelay = Math.max(0, access$900.targetTimeMillis - SystemClock.elapsedRealtime());
                            }
                            BackgroundExecutor.execute(access$900);
                        }
                    }
                }
            }
        }
    }

    public interface WrongThreadListener {
        void onBgExpected(String... strArr);

        void onUiExpected();

        void onWrongBgSerial(String str, String... strArr);
    }

    private BackgroundExecutor() {
    }

    private static Future<?> directExecute(Runnable runnable, long j) {
        if (j > 0) {
            Executor executor2 = executor;
            if (executor2 instanceof ScheduledExecutorService) {
                return ((ScheduledExecutorService) executor2).schedule(runnable, j, TimeUnit.MILLISECONDS);
            }
            throw new IllegalArgumentException("The executor set does not support scheduling");
        }
        Executor executor3 = executor;
        if (executor3 instanceof ExecutorService) {
            return ((ExecutorService) executor3).submit(runnable);
        }
        executor3.execute(runnable);
        return null;
    }

    public static synchronized void execute(Task task) {
        synchronized (BackgroundExecutor.class) {
            if (!(task.f89id == null && task.serial == null)) {
                TASKS.add(task);
            }
            if (task.serial == null || !hasSerialRunning(task.serial)) {
                task.executionAsked = true;
                task.future = directExecute(task, task.remainingDelay);
            }
        }
    }

    public static void execute(Runnable runnable, String str, long j, String str2) {
        final Runnable runnable2 = runnable;
        C10992 r0 = new Task(str, j, str2) {
            public void execute() {
                runnable2.run();
            }
        };
        execute((Task) r0);
    }

    public static void execute(Runnable runnable, long j) {
        directExecute(runnable, j);
    }

    public static void execute(Runnable runnable) {
        directExecute(runnable, 0);
    }

    public static void execute(Runnable runnable, String str, String str2) {
        execute(runnable, str, 0, str2);
    }

    public static void setExecutor(Executor executor2) {
        executor = executor2;
    }

    public static void setWrongThreadListener(WrongThreadListener wrongThreadListener2) {
        wrongThreadListener = wrongThreadListener2;
    }

    public static synchronized void cancelAll(String str, boolean z) {
        synchronized (BackgroundExecutor.class) {
            for (int size = TASKS.size() - 1; size >= 0; size--) {
                Task task = (Task) TASKS.get(size);
                if (str.equals(task.f89id)) {
                    if (task.future != null) {
                        task.future.cancel(z);
                        if (!task.managed.getAndSet(true)) {
                            task.postExecute();
                        }
                    } else if (task.executionAsked) {
                        String str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("A task with id ");
                        sb.append(task.f89id);
                        sb.append(" cannot be cancelled (the executor set does not support it)");
                        Log.w(str2, sb.toString());
                    } else {
                        TASKS.remove(size);
                    }
                }
            }
        }
    }

    public static void checkUiThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            wrongThreadListener.onUiExpected();
        }
    }

    public static void checkBgThread(String... strArr) {
        if (strArr.length == 0) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                wrongThreadListener.onBgExpected(strArr);
            }
            return;
        }
        String str = (String) CURRENT_SERIAL.get();
        if (str == null) {
            wrongThreadListener.onWrongBgSerial(null, strArr);
            return;
        }
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            if (!strArr[i].equals(str)) {
                i++;
            } else {
                return;
            }
        }
        wrongThreadListener.onWrongBgSerial(str, strArr);
    }

    private static boolean hasSerialRunning(String str) {
        for (Task task : TASKS) {
            if (task.executionAsked && str.equals(task.serial)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static Task take(String str) {
        int size = TASKS.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(((Task) TASKS.get(i)).serial)) {
                return (Task) TASKS.remove(i);
            }
        }
        return null;
    }
}
