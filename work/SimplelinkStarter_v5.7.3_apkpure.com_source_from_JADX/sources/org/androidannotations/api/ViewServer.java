package org.androidannotations.api;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViewServer implements Runnable {
    private static final String BUILD_TYPE_USER = "user";
    private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final String COMMAND_SERVER_VERSION = "SERVER";
    private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final String LOG_TAG = "ViewServer";
    private static final String VALUE_PROTOCOL_VERSION = "4";
    private static final String VALUE_SERVER_VERSION = "4";
    private static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private static ViewServer sServer;
    /* access modifiers changed from: private */
    public final ReentrantReadWriteLock mFocusLock;
    /* access modifiers changed from: private */
    public View mFocusedWindow;
    private final List<WindowListener> mListeners;
    private final int mPort;
    private ServerSocket mServer;
    private Thread mThread;
    private ExecutorService mThreadPool;
    /* access modifiers changed from: private */
    public final Map<View, String> mWindows;
    /* access modifiers changed from: private */
    public final ReentrantReadWriteLock mWindowsLock;

    private static final class NoopViewServer extends ViewServer {
        public void addWindow(Activity activity) {
        }

        public void addWindow(View view, String str) {
        }

        public boolean isRunning() {
            return false;
        }

        public void removeWindow(Activity activity) {
        }

        public void removeWindow(View view) {
        }

        public void run() {
        }

        public void setFocusedWindow(Activity activity) {
        }

        public void setFocusedWindow(View view) {
        }

        public boolean start() throws IOException {
            return false;
        }

        public boolean stop() {
            return false;
        }

        private NoopViewServer() {
            super();
        }
    }

    private static class UncloseableOutputStream extends OutputStream {
        private final OutputStream mStream;

        public void close() throws IOException {
        }

        UncloseableOutputStream(OutputStream outputStream) {
            this.mStream = outputStream;
        }

        public boolean equals(Object obj) {
            return this.mStream.equals(obj);
        }

        public void flush() throws IOException {
            this.mStream.flush();
        }

        public int hashCode() {
            return this.mStream.hashCode();
        }

        public String toString() {
            return this.mStream.toString();
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.mStream.write(bArr, i, i2);
        }

        public void write(byte[] bArr) throws IOException {
            this.mStream.write(bArr);
        }

        public void write(int i) throws IOException {
            this.mStream.write(i);
        }
    }

    private class ViewServerWorker implements Runnable, WindowListener {
        private Socket mClient;
        private final Object[] mLock;
        private boolean mNeedFocusedWindowUpdate;
        private boolean mNeedWindowListUpdate;

        private ViewServerWorker(Socket socket) {
            this.mLock = new Object[0];
            this.mClient = socket;
            this.mNeedWindowListUpdate = false;
            this.mNeedFocusedWindowUpdate = false;
        }

        /* JADX WARNING: Removed duplicated region for block: B:45:0x00b8 A[SYNTHETIC, Splitter:B:45:0x00b8] */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00c4 A[SYNTHETIC, Splitter:B:51:0x00c4] */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x00d0 A[SYNTHETIC, Splitter:B:57:0x00d0] */
        /* JADX WARNING: Removed duplicated region for block: B:63:0x00dc A[SYNTHETIC, Splitter:B:63:0x00dc] */
        /* JADX WARNING: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r7 = this;
                java.lang.String r0 = "ViewServer"
                r1 = 0
                java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                java.net.Socket r4 = r7.mClient     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                java.io.InputStream r4 = r4.getInputStream()     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                r3.<init>(r4)     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                r4 = 1024(0x400, float:1.435E-42)
                r2.<init>(r3, r4)     // Catch:{ IOException -> 0x00ad, all -> 0x00aa }
                java.lang.String r1 = r2.readLine()     // Catch:{ IOException -> 0x00a8 }
                r3 = 32
                int r3 = r1.indexOf(r3)     // Catch:{ IOException -> 0x00a8 }
                r4 = -1
                if (r3 != r4) goto L_0x0025
                java.lang.String r3 = ""
                goto L_0x0031
            L_0x0025:
                r4 = 0
                java.lang.String r4 = r1.substring(r4, r3)     // Catch:{ IOException -> 0x00a8 }
                int r3 = r3 + 1
                java.lang.String r3 = r1.substring(r3)     // Catch:{ IOException -> 0x00a8 }
                r1 = r4
            L_0x0031:
                java.lang.String r4 = "PROTOCOL"
                boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ IOException -> 0x00a8 }
                java.lang.String r5 = "4"
                if (r4 == 0) goto L_0x0042
                java.net.Socket r3 = r7.mClient     // Catch:{ IOException -> 0x00a8 }
                boolean r3 = org.androidannotations.api.ViewServer.writeValue(r3, r5)     // Catch:{ IOException -> 0x00a8 }
                goto L_0x0082
            L_0x0042:
                java.lang.String r4 = "SERVER"
                boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ IOException -> 0x00a8 }
                if (r4 == 0) goto L_0x0051
                java.net.Socket r3 = r7.mClient     // Catch:{ IOException -> 0x00a8 }
                boolean r3 = org.androidannotations.api.ViewServer.writeValue(r3, r5)     // Catch:{ IOException -> 0x00a8 }
                goto L_0x0082
            L_0x0051:
                java.lang.String r4 = "LIST"
                boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ IOException -> 0x00a8 }
                if (r4 == 0) goto L_0x0060
                java.net.Socket r3 = r7.mClient     // Catch:{ IOException -> 0x00a8 }
                boolean r3 = r7.listWindows(r3)     // Catch:{ IOException -> 0x00a8 }
                goto L_0x0082
            L_0x0060:
                java.lang.String r4 = "GET_FOCUS"
                boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ IOException -> 0x00a8 }
                if (r4 == 0) goto L_0x006f
                java.net.Socket r3 = r7.mClient     // Catch:{ IOException -> 0x00a8 }
                boolean r3 = r7.getFocusedWindow(r3)     // Catch:{ IOException -> 0x00a8 }
                goto L_0x0082
            L_0x006f:
                java.lang.String r4 = "AUTOLIST"
                boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ IOException -> 0x00a8 }
                if (r4 == 0) goto L_0x007c
                boolean r3 = r7.windowManagerAutolistLoop()     // Catch:{ IOException -> 0x00a8 }
                goto L_0x0082
            L_0x007c:
                java.net.Socket r4 = r7.mClient     // Catch:{ IOException -> 0x00a8 }
                boolean r3 = r7.windowCommand(r4, r1, r3)     // Catch:{ IOException -> 0x00a8 }
            L_0x0082:
                if (r3 != 0) goto L_0x0098
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a8 }
                r3.<init>()     // Catch:{ IOException -> 0x00a8 }
                java.lang.String r4 = "An error occurred with the command: "
                r3.append(r4)     // Catch:{ IOException -> 0x00a8 }
                r3.append(r1)     // Catch:{ IOException -> 0x00a8 }
                java.lang.String r1 = r3.toString()     // Catch:{ IOException -> 0x00a8 }
                android.util.Log.w(r0, r1)     // Catch:{ IOException -> 0x00a8 }
            L_0x0098:
                r2.close()     // Catch:{ IOException -> 0x009c }
                goto L_0x00a0
            L_0x009c:
                r0 = move-exception
                r0.printStackTrace()
            L_0x00a0:
                java.net.Socket r0 = r7.mClient
                if (r0 == 0) goto L_0x00cc
                r0.close()     // Catch:{ IOException -> 0x00c8 }
                goto L_0x00cc
            L_0x00a8:
                r1 = move-exception
                goto L_0x00b1
            L_0x00aa:
                r0 = move-exception
                r2 = r1
                goto L_0x00ce
            L_0x00ad:
                r2 = move-exception
                r6 = r2
                r2 = r1
                r1 = r6
            L_0x00b1:
                java.lang.String r3 = "Connection error: "
                android.util.Log.w(r0, r3, r1)     // Catch:{ all -> 0x00cd }
                if (r2 == 0) goto L_0x00c0
                r2.close()     // Catch:{ IOException -> 0x00bc }
                goto L_0x00c0
            L_0x00bc:
                r0 = move-exception
                r0.printStackTrace()
            L_0x00c0:
                java.net.Socket r0 = r7.mClient
                if (r0 == 0) goto L_0x00cc
                r0.close()     // Catch:{ IOException -> 0x00c8 }
                goto L_0x00cc
            L_0x00c8:
                r0 = move-exception
                r0.printStackTrace()
            L_0x00cc:
                return
            L_0x00cd:
                r0 = move-exception
            L_0x00ce:
                if (r2 == 0) goto L_0x00d8
                r2.close()     // Catch:{ IOException -> 0x00d4 }
                goto L_0x00d8
            L_0x00d4:
                r1 = move-exception
                r1.printStackTrace()
            L_0x00d8:
                java.net.Socket r1 = r7.mClient
                if (r1 == 0) goto L_0x00e4
                r1.close()     // Catch:{ IOException -> 0x00e0 }
                goto L_0x00e4
            L_0x00e0:
                r1 = move-exception
                r1.printStackTrace()
            L_0x00e4:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.ViewServerWorker.run():void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:38:0x00ba A[SYNTHETIC, Splitter:B:38:0x00ba] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean windowCommand(java.net.Socket r12, java.lang.String r13, java.lang.String r14) {
            /*
                r11 = this;
                r0 = 32
                r1 = 0
                r2 = 1
                r3 = 0
                int r0 = r14.indexOf(r0)     // Catch:{ Exception -> 0x0093 }
                r4 = -1
                if (r0 != r4) goto L_0x0010
                int r0 = r14.length()     // Catch:{ Exception -> 0x0093 }
            L_0x0010:
                java.lang.String r4 = r14.substring(r3, r0)     // Catch:{ Exception -> 0x0093 }
                r5 = 16
                long r4 = java.lang.Long.parseLong(r4, r5)     // Catch:{ Exception -> 0x0093 }
                int r5 = (int) r4     // Catch:{ Exception -> 0x0093 }
                int r4 = r14.length()     // Catch:{ Exception -> 0x0093 }
                if (r0 >= r4) goto L_0x0027
                int r0 = r0 + r2
                java.lang.String r14 = r14.substring(r0)     // Catch:{ Exception -> 0x0093 }
                goto L_0x0029
            L_0x0027:
                java.lang.String r14 = ""
            L_0x0029:
                android.view.View r0 = r11.findWindow(r5)     // Catch:{ Exception -> 0x0093 }
                if (r0 != 0) goto L_0x0030
                return r3
            L_0x0030:
                java.lang.Class<android.view.ViewDebug> r4 = android.view.ViewDebug.class
                java.lang.String r5 = "dispatchCommand"
                r6 = 4
                java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x0093 }
                java.lang.Class<android.view.View> r8 = android.view.View.class
                r7[r3] = r8     // Catch:{ Exception -> 0x0093 }
                java.lang.Class<java.lang.String> r8 = java.lang.String.class
                r7[r2] = r8     // Catch:{ Exception -> 0x0093 }
                java.lang.Class<java.lang.String> r8 = java.lang.String.class
                r9 = 2
                r7[r9] = r8     // Catch:{ Exception -> 0x0093 }
                java.lang.Class<java.io.OutputStream> r8 = java.io.OutputStream.class
                r10 = 3
                r7[r10] = r8     // Catch:{ Exception -> 0x0093 }
                java.lang.reflect.Method r4 = r4.getDeclaredMethod(r5, r7)     // Catch:{ Exception -> 0x0093 }
                r4.setAccessible(r2)     // Catch:{ Exception -> 0x0093 }
                java.lang.Object[] r5 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x0093 }
                r5[r3] = r0     // Catch:{ Exception -> 0x0093 }
                r5[r2] = r13     // Catch:{ Exception -> 0x0093 }
                r5[r9] = r14     // Catch:{ Exception -> 0x0093 }
                org.androidannotations.api.ViewServer$UncloseableOutputStream r0 = new org.androidannotations.api.ViewServer$UncloseableOutputStream     // Catch:{ Exception -> 0x0093 }
                java.io.OutputStream r6 = r12.getOutputStream()     // Catch:{ Exception -> 0x0093 }
                r0.<init>(r6)     // Catch:{ Exception -> 0x0093 }
                r5[r10] = r0     // Catch:{ Exception -> 0x0093 }
                r4.invoke(r1, r5)     // Catch:{ Exception -> 0x0093 }
                boolean r0 = r12.isOutputShutdown()     // Catch:{ Exception -> 0x0093 }
                if (r0 != 0) goto L_0x0089
                java.io.BufferedWriter r0 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0093 }
                java.io.OutputStreamWriter r4 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x0093 }
                java.io.OutputStream r12 = r12.getOutputStream()     // Catch:{ Exception -> 0x0093 }
                r4.<init>(r12)     // Catch:{ Exception -> 0x0093 }
                r0.<init>(r4)     // Catch:{ Exception -> 0x0093 }
                java.lang.String r12 = "DONE\n"
                r0.write(r12)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
                r0.flush()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
                goto L_0x008a
            L_0x0083:
                r12 = move-exception
                r1 = r0
                goto L_0x00b8
            L_0x0086:
                r12 = move-exception
                r1 = r0
                goto L_0x0094
            L_0x0089:
                r0 = r1
            L_0x008a:
                if (r0 == 0) goto L_0x008f
                r0.close()     // Catch:{ IOException -> 0x00b7 }
            L_0x008f:
                r3 = 1
                goto L_0x00b7
            L_0x0091:
                r12 = move-exception
                goto L_0x00b8
            L_0x0093:
                r12 = move-exception
            L_0x0094:
                java.lang.String r0 = "ViewServer"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0091 }
                r2.<init>()     // Catch:{ all -> 0x0091 }
                java.lang.String r4 = "Could not send command "
                r2.append(r4)     // Catch:{ all -> 0x0091 }
                r2.append(r13)     // Catch:{ all -> 0x0091 }
                java.lang.String r13 = " with parameters "
                r2.append(r13)     // Catch:{ all -> 0x0091 }
                r2.append(r14)     // Catch:{ all -> 0x0091 }
                java.lang.String r13 = r2.toString()     // Catch:{ all -> 0x0091 }
                android.util.Log.w(r0, r13, r12)     // Catch:{ all -> 0x0091 }
                if (r1 == 0) goto L_0x00b7
                r1.close()     // Catch:{ IOException -> 0x00b7 }
            L_0x00b7:
                return r3
            L_0x00b8:
                if (r1 == 0) goto L_0x00bd
                r1.close()     // Catch:{ IOException -> 0x00bd }
            L_0x00bd:
                throw r12
            */
            throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.ViewServerWorker.windowCommand(java.net.Socket, java.lang.String, java.lang.String):boolean");
        }

        private View findWindow(int i) {
            if (i == -1) {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    return ViewServer.this.mFocusedWindow;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            } else {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    for (Entry entry : ViewServer.this.mWindows.entrySet()) {
                        if (System.identityHashCode(entry.getKey()) == i) {
                            return (View) entry.getKey();
                        }
                    }
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    return null;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:19:0x008b A[SYNTHETIC, Splitter:B:19:0x008b] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x009f A[SYNTHETIC, Splitter:B:27:0x009f] */
        /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean listWindows(java.net.Socket r5) {
            /*
                r4 = this;
                r0 = 0
                r1 = 0
                org.androidannotations.api.ViewServer r2 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                java.util.concurrent.locks.ReentrantReadWriteLock r2 = r2.mWindowsLock     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r2.readLock()     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                r2.lock()     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                java.io.OutputStream r5 = r5.getOutputStream()     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                r3.<init>(r5)     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                r5 = 8192(0x2000, float:1.14794E-41)
                r2.<init>(r3, r5)     // Catch:{ Exception -> 0x008f, all -> 0x007a }
                org.androidannotations.api.ViewServer r5 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.util.Map r5 = r5.mWindows     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.util.Set r5 = r5.entrySet()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.util.Iterator r5 = r5.iterator()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
            L_0x002d:
                boolean r1 = r5.hasNext()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                if (r1 == 0) goto L_0x005c
                java.lang.Object r1 = r5.next()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.lang.Object r3 = r1.getKey()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                int r3 = java.lang.System.identityHashCode(r3)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.lang.String r3 = java.lang.Integer.toHexString(r3)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                r2.write(r3)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                r3 = 32
                r2.write(r3)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.lang.Object r1 = r1.getValue()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                r2.append(r1)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                r1 = 10
                r2.write(r1)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                goto L_0x002d
            L_0x005c:
                java.lang.String r5 = "DONE.\n"
                r2.write(r5)     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                r2.flush()     // Catch:{ Exception -> 0x0078, all -> 0x0076 }
                org.androidannotations.api.ViewServer r5 = org.androidannotations.api.ViewServer.this
                java.util.concurrent.locks.ReentrantReadWriteLock r5 = r5.mWindowsLock
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r5 = r5.readLock()
                r5.unlock()
                r2.close()     // Catch:{ IOException -> 0x00a2 }
                r0 = 1
                goto L_0x00a2
            L_0x0076:
                r5 = move-exception
                goto L_0x007c
            L_0x0078:
                goto L_0x0090
            L_0x007a:
                r5 = move-exception
                r2 = r1
            L_0x007c:
                org.androidannotations.api.ViewServer r0 = org.androidannotations.api.ViewServer.this
                java.util.concurrent.locks.ReentrantReadWriteLock r0 = r0.mWindowsLock
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()
                r0.unlock()
                if (r2 == 0) goto L_0x008e
                r2.close()     // Catch:{ IOException -> 0x008e }
            L_0x008e:
                throw r5
            L_0x008f:
                r2 = r1
            L_0x0090:
                org.androidannotations.api.ViewServer r5 = org.androidannotations.api.ViewServer.this
                java.util.concurrent.locks.ReentrantReadWriteLock r5 = r5.mWindowsLock
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r5 = r5.readLock()
                r5.unlock()
                if (r2 == 0) goto L_0x00a2
                r2.close()     // Catch:{ IOException -> 0x00a2 }
            L_0x00a2:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.ViewServerWorker.listWindows(java.net.Socket):boolean");
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x00a7 A[SYNTHETIC, Splitter:B:31:0x00a7] */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00ae A[SYNTHETIC, Splitter:B:38:0x00ae] */
        /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean getFocusedWindow(java.net.Socket r5) {
            /*
                r4 = this;
                r0 = 0
                r1 = 0
                java.io.OutputStream r5 = r5.getOutputStream()     // Catch:{ Exception -> 0x00ab, all -> 0x00a3 }
                java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x00ab, all -> 0x00a3 }
                java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x00ab, all -> 0x00a3 }
                r3.<init>(r5)     // Catch:{ Exception -> 0x00ab, all -> 0x00a3 }
                r5 = 8192(0x2000, float:1.14794E-41)
                r2.<init>(r3, r5)     // Catch:{ Exception -> 0x00ab, all -> 0x00a3 }
                org.androidannotations.api.ViewServer r5 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r5 = r5.mFocusLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r5 = r5.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r5.lock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                org.androidannotations.api.ViewServer r5 = org.androidannotations.api.ViewServer.this     // Catch:{ all -> 0x0090 }
                android.view.View r5 = r5.mFocusedWindow     // Catch:{ all -> 0x0090 }
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.mFocusLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r1.unlock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                if (r5 == 0) goto L_0x0083
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.mWindowsLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r1.lock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this     // Catch:{ all -> 0x0074 }
                java.util.Map r1 = r1.mWindows     // Catch:{ all -> 0x0074 }
                org.androidannotations.api.ViewServer r3 = org.androidannotations.api.ViewServer.this     // Catch:{ all -> 0x0074 }
                android.view.View r3 = r3.mFocusedWindow     // Catch:{ all -> 0x0074 }
                java.lang.Object r1 = r1.get(r3)     // Catch:{ all -> 0x0074 }
                java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0074 }
                org.androidannotations.api.ViewServer r3 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r3 = r3.mWindowsLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r3.unlock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                int r5 = java.lang.System.identityHashCode(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.lang.String r5 = java.lang.Integer.toHexString(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r2.write(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r5 = 32
                r2.write(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r2.append(r1)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                goto L_0x0083
            L_0x0074:
                r5 = move-exception
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.mWindowsLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r1.unlock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                throw r5     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
            L_0x0083:
                r5 = 10
                r2.write(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r2.flush()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r2.close()     // Catch:{ IOException -> 0x00b1 }
                r0 = 1
                goto L_0x00b1
            L_0x0090:
                r5 = move-exception
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock r1 = r1.mFocusLock     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                r1.unlock()     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
                throw r5     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
            L_0x009f:
                r5 = move-exception
                goto L_0x00a5
            L_0x00a1:
                goto L_0x00ac
            L_0x00a3:
                r5 = move-exception
                r2 = r1
            L_0x00a5:
                if (r2 == 0) goto L_0x00aa
                r2.close()     // Catch:{ IOException -> 0x00aa }
            L_0x00aa:
                throw r5
            L_0x00ab:
                r2 = r1
            L_0x00ac:
                if (r2 == 0) goto L_0x00b1
                r2.close()     // Catch:{ IOException -> 0x00b1 }
            L_0x00b1:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.ViewServerWorker.getFocusedWindow(java.net.Socket):boolean");
        }

        public void windowsChanged() {
            synchronized (this.mLock) {
                this.mNeedWindowListUpdate = true;
                this.mLock.notifyAll();
            }
        }

        public void focusChanged() {
            synchronized (this.mLock) {
                this.mNeedFocusedWindowUpdate = true;
                this.mLock.notifyAll();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0068, code lost:
            if (r2 == null) goto L_0x006d;
         */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0076 A[SYNTHETIC, Splitter:B:49:0x0076] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean windowManagerAutolistLoop() {
            /*
                r7 = this;
                org.androidannotations.api.ViewServer r0 = org.androidannotations.api.ViewServer.this
                r0.addWindowListener(r7)
                r0 = 1
                r1 = 0
                java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x005d, all -> 0x005a }
                java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x005d, all -> 0x005a }
                java.net.Socket r4 = r7.mClient     // Catch:{ Exception -> 0x005d, all -> 0x005a }
                java.io.OutputStream r4 = r4.getOutputStream()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
                r3.<init>(r4)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
                r2.<init>(r3)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            L_0x0017:
                boolean r1 = java.lang.Thread.interrupted()     // Catch:{ Exception -> 0x0058 }
                if (r1 != 0) goto L_0x006a
                java.lang.Object[] r1 = r7.mLock     // Catch:{ Exception -> 0x0058 }
                monitor-enter(r1)     // Catch:{ Exception -> 0x0058 }
            L_0x0020:
                boolean r3 = r7.mNeedWindowListUpdate     // Catch:{ all -> 0x0055 }
                if (r3 != 0) goto L_0x002e
                boolean r3 = r7.mNeedFocusedWindowUpdate     // Catch:{ all -> 0x0055 }
                if (r3 != 0) goto L_0x002e
                java.lang.Object[] r3 = r7.mLock     // Catch:{ all -> 0x0055 }
                r3.wait()     // Catch:{ all -> 0x0055 }
                goto L_0x0020
            L_0x002e:
                boolean r3 = r7.mNeedWindowListUpdate     // Catch:{ all -> 0x0055 }
                r4 = 0
                if (r3 == 0) goto L_0x0037
                r7.mNeedWindowListUpdate = r4     // Catch:{ all -> 0x0055 }
                r3 = 1
                goto L_0x0038
            L_0x0037:
                r3 = 0
            L_0x0038:
                boolean r5 = r7.mNeedFocusedWindowUpdate     // Catch:{ all -> 0x0055 }
                if (r5 == 0) goto L_0x003f
                r7.mNeedFocusedWindowUpdate = r4     // Catch:{ all -> 0x0055 }
                r4 = 1
            L_0x003f:
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                if (r3 == 0) goto L_0x004a
                java.lang.String r1 = "LIST UPDATE\n"
                r2.write(r1)     // Catch:{ Exception -> 0x0058 }
                r2.flush()     // Catch:{ Exception -> 0x0058 }
            L_0x004a:
                if (r4 == 0) goto L_0x0017
                java.lang.String r1 = "FOCUS UPDATE\n"
                r2.write(r1)     // Catch:{ Exception -> 0x0058 }
                r2.flush()     // Catch:{ Exception -> 0x0058 }
                goto L_0x0017
            L_0x0055:
                r3 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0055 }
                throw r3     // Catch:{ Exception -> 0x0058 }
            L_0x0058:
                r1 = move-exception
                goto L_0x0061
            L_0x005a:
                r0 = move-exception
                r2 = r1
                goto L_0x0074
            L_0x005d:
                r2 = move-exception
                r6 = r2
                r2 = r1
                r1 = r6
            L_0x0061:
                java.lang.String r3 = "ViewServer"
                java.lang.String r4 = "Connection error: "
                android.util.Log.w(r3, r4, r1)     // Catch:{ all -> 0x0073 }
                if (r2 == 0) goto L_0x006d
            L_0x006a:
                r2.close()     // Catch:{ IOException -> 0x006d }
            L_0x006d:
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this
                r1.removeWindowListener(r7)
                return r0
            L_0x0073:
                r0 = move-exception
            L_0x0074:
                if (r2 == 0) goto L_0x0079
                r2.close()     // Catch:{ IOException -> 0x0079 }
            L_0x0079:
                org.androidannotations.api.ViewServer r1 = org.androidannotations.api.ViewServer.this
                r1.removeWindowListener(r7)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.ViewServerWorker.windowManagerAutolistLoop():boolean");
        }
    }

    private interface WindowListener {
        void focusChanged();

        void windowsChanged();
    }

    public static ViewServer get(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (!BUILD_TYPE_USER.equals(Build.TYPE) || (applicationInfo.flags & 2) == 0) {
            sServer = new NoopViewServer();
        } else {
            if (sServer == null) {
                sServer = new ViewServer((int) VIEW_SERVER_DEFAULT_PORT);
            }
            if (!sServer.isRunning()) {
                try {
                    sServer.start();
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Error:", e);
                }
            }
        }
        return sServer;
    }

    private ViewServer() {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = -1;
    }

    private ViewServer(int i) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = i;
    }

    public boolean start() throws IOException {
        if (this.mThread != null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Local View Server [port=");
        sb.append(this.mPort);
        sb.append("]");
        this.mThread = new Thread(this, sb.toString());
        this.mThreadPool = Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    /* JADX INFO: finally extract failed */
    public boolean stop() {
        Thread thread = this.mThread;
        if (thread != null) {
            thread.interrupt();
            ExecutorService executorService = this.mThreadPool;
            String str = LOG_TAG;
            if (executorService != null) {
                try {
                    executorService.shutdownNow();
                } catch (SecurityException unused) {
                    Log.w(str, "Could not stop all view server threads");
                }
            }
            this.mThreadPool = null;
            this.mThread = null;
            try {
                this.mServer.close();
                this.mServer = null;
                return true;
            } catch (IOException unused2) {
                Log.w(str, "Could not close the view server");
            }
        }
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.clear();
            this.mWindowsLock.writeLock().unlock();
            this.mFocusLock.writeLock().lock();
            try {
                this.mFocusedWindow = null;
                this.mFocusLock.writeLock().unlock();
                return false;
            } catch (Throwable th) {
                this.mFocusLock.writeLock().unlock();
                throw th;
            }
        } catch (Throwable th2) {
            this.mWindowsLock.writeLock().unlock();
            throw th2;
        }
    }

    public boolean isRunning() {
        Thread thread = this.mThread;
        return thread != null && thread.isAlive();
    }

    public void addWindow(Activity activity) {
        String str;
        String charSequence = activity.getTitle().toString();
        if (TextUtils.isEmpty(charSequence)) {
            StringBuilder sb = new StringBuilder();
            sb.append(activity.getClass().getCanonicalName());
            sb.append("/0x");
            sb.append(System.identityHashCode(activity));
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(charSequence);
            sb2.append("(");
            sb2.append(activity.getClass().getCanonicalName());
            sb2.append(")");
            str = sb2.toString();
        }
        addWindow(activity.getWindow().getDecorView(), str);
    }

    public void removeWindow(Activity activity) {
        removeWindow(activity.getWindow().getDecorView());
    }

    /* JADX INFO: finally extract failed */
    public void addWindow(View view, String str) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.put(view.getRootView(), str);
            this.mWindowsLock.writeLock().unlock();
            fireWindowsChangedEvent();
        } catch (Throwable th) {
            this.mWindowsLock.writeLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public void removeWindow(View view) {
        this.mWindowsLock.writeLock().lock();
        try {
            View rootView = view.getRootView();
            this.mWindows.remove(rootView);
            this.mWindowsLock.writeLock().unlock();
            this.mFocusLock.writeLock().lock();
            try {
                if (this.mFocusedWindow == rootView) {
                    this.mFocusedWindow = null;
                }
                this.mFocusLock.writeLock().unlock();
                fireWindowsChangedEvent();
            } catch (Throwable th) {
                this.mFocusLock.writeLock().unlock();
                throw th;
            }
        } catch (Throwable th2) {
            this.mWindowsLock.writeLock().unlock();
            throw th2;
        }
    }

    public void setFocusedWindow(Activity activity) {
        setFocusedWindow(activity.getWindow().getDecorView());
    }

    public void setFocusedWindow(View view) {
        View view2;
        this.mFocusLock.writeLock().lock();
        if (view == null) {
            view2 = null;
        } else {
            try {
                view2 = view.getRootView();
            } catch (Throwable th) {
                this.mFocusLock.writeLock().unlock();
                throw th;
            }
        }
        this.mFocusedWindow = view2;
        this.mFocusLock.writeLock().unlock();
        fireFocusChangedEvent();
    }

    public void run() {
        String str = LOG_TAG;
        try {
            this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        } catch (Exception e) {
            Log.w(str, "Starting ServerSocket error: ", e);
        }
        while (this.mServer != null && Thread.currentThread() == this.mThread) {
            try {
                Socket accept = this.mServer.accept();
                if (this.mThreadPool != null) {
                    this.mThreadPool.submit(new ViewServerWorker(accept));
                } else {
                    try {
                        accept.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                Log.w(str, "Connection error: ", e3);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002b A[SYNTHETIC, Splitter:B:15:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0032 A[SYNTHETIC, Splitter:B:21:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeValue(java.net.Socket r4, java.lang.String r5) {
        /*
            r0 = 0
            r1 = 0
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch:{ Exception -> 0x002f, all -> 0x0028 }
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x002f, all -> 0x0028 }
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x002f, all -> 0x0028 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x002f, all -> 0x0028 }
            r4 = 8192(0x2000, float:1.14794E-41)
            r2.<init>(r3, r4)     // Catch:{ Exception -> 0x002f, all -> 0x0028 }
            r2.write(r5)     // Catch:{ Exception -> 0x0026, all -> 0x0023 }
            java.lang.String r4 = "\n"
            r2.write(r4)     // Catch:{ Exception -> 0x0026, all -> 0x0023 }
            r2.flush()     // Catch:{ Exception -> 0x0026, all -> 0x0023 }
            r4 = 1
            r2.close()     // Catch:{ IOException -> 0x0035 }
            r0 = 1
            goto L_0x0035
        L_0x0023:
            r4 = move-exception
            r1 = r2
            goto L_0x0029
        L_0x0026:
            r1 = r2
            goto L_0x0030
        L_0x0028:
            r4 = move-exception
        L_0x0029:
            if (r1 == 0) goto L_0x002e
            r1.close()     // Catch:{ IOException -> 0x002e }
        L_0x002e:
            throw r4
        L_0x002f:
        L_0x0030:
            if (r1 == 0) goto L_0x0035
            r1.close()     // Catch:{ IOException -> 0x0035 }
        L_0x0035:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.androidannotations.api.ViewServer.writeValue(java.net.Socket, java.lang.String):boolean");
    }

    private void fireWindowsChangedEvent() {
        for (WindowListener windowsChanged : this.mListeners) {
            windowsChanged.windowsChanged();
        }
    }

    private void fireFocusChangedEvent() {
        for (WindowListener focusChanged : this.mListeners) {
            focusChanged.focusChanged();
        }
    }

    /* access modifiers changed from: private */
    public void addWindowListener(WindowListener windowListener) {
        if (!this.mListeners.contains(windowListener)) {
            this.mListeners.add(windowListener);
        }
    }

    /* access modifiers changed from: private */
    public void removeWindowListener(WindowListener windowListener) {
        this.mListeners.remove(windowListener);
    }
}
