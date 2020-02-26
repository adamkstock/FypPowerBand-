package org.androidannotations.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;

public final class UiThreadExecutor {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Runnable callback = message.getCallback();
            if (callback != null) {
                callback.run();
                UiThreadExecutor.decrementToken((Token) message.obj);
                return;
            }
            super.handleMessage(message);
        }
    };
    private static final Map<String, Token> TOKENS = new HashMap();

    private static final class Token {

        /* renamed from: id */
        final String f90id;
        int runnablesCount;

        private Token(String str) {
            this.runnablesCount = 0;
            this.f90id = str;
        }
    }

    private UiThreadExecutor() {
    }

    public static void runTask(String str, Runnable runnable, long j) {
        if ("".equals(str)) {
            HANDLER.postDelayed(runnable, j);
            return;
        }
        HANDLER.postAtTime(runnable, nextToken(str), SystemClock.uptimeMillis() + j);
    }

    private static Token nextToken(String str) {
        Token token;
        synchronized (TOKENS) {
            token = (Token) TOKENS.get(str);
            if (token == null) {
                token = new Token(str);
                TOKENS.put(str, token);
            }
            token.runnablesCount++;
        }
        return token;
    }

    /* access modifiers changed from: private */
    public static void decrementToken(Token token) {
        synchronized (TOKENS) {
            int i = token.runnablesCount - 1;
            token.runnablesCount = i;
            if (i == 0) {
                String str = token.f90id;
                Token token2 = (Token) TOKENS.remove(str);
                if (token2 != token) {
                    TOKENS.put(str, token2);
                }
            }
        }
    }

    public static void cancelAll(String str) {
        Token token;
        synchronized (TOKENS) {
            token = (Token) TOKENS.remove(str);
        }
        if (token != null) {
            HANDLER.removeCallbacksAndMessages(token);
        }
    }
}
