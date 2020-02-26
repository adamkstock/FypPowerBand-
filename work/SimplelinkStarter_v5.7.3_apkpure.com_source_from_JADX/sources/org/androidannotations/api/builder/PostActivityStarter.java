package org.androidannotations.api.builder;

import android.app.Activity;
import android.content.Context;

public final class PostActivityStarter {
    private Context context;

    public PostActivityStarter(Context context2) {
        this.context = context2;
    }

    public void withAnimation(int i, int i2) {
        Context context2 = this.context;
        if (context2 instanceof Activity) {
            ((Activity) context2).overridePendingTransition(i, i2);
        }
    }
}
