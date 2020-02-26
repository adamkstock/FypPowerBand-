package org.androidannotations.api.view;

import java.util.LinkedHashSet;
import java.util.Set;

public class OnViewChangedNotifier {
    private static OnViewChangedNotifier currentNotifier;
    private final Set<OnViewChangedListener> listeners = new LinkedHashSet();

    public static OnViewChangedNotifier replaceNotifier(OnViewChangedNotifier onViewChangedNotifier) {
        OnViewChangedNotifier onViewChangedNotifier2 = currentNotifier;
        currentNotifier = onViewChangedNotifier;
        return onViewChangedNotifier2;
    }

    public static void registerOnViewChangedListener(OnViewChangedListener onViewChangedListener) {
        OnViewChangedNotifier onViewChangedNotifier = currentNotifier;
        if (onViewChangedNotifier != null) {
            onViewChangedNotifier.listeners.add(onViewChangedListener);
        }
    }

    public void notifyViewChanged(HasViews hasViews) {
        for (OnViewChangedListener onViewChanged : this.listeners) {
            onViewChanged.onViewChanged(hasViews);
        }
    }
}
