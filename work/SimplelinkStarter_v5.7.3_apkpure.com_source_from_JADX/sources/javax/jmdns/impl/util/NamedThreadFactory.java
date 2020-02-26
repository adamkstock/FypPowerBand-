package javax.jmdns.impl.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private final ThreadFactory _delegate = Executors.defaultThreadFactory();
    private final String _namePrefix;

    public NamedThreadFactory(String str) {
        this._namePrefix = str;
    }

    public Thread newThread(Runnable runnable) {
        Thread newThread = this._delegate.newThread(runnable);
        StringBuilder sb = new StringBuilder();
        sb.append(this._namePrefix);
        sb.append(' ');
        sb.append(newThread.getName());
        newThread.setName(sb.toString());
        return newThread;
    }
}
