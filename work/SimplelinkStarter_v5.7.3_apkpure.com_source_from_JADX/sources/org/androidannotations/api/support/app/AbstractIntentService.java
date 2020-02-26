package org.androidannotations.api.support.app;

import android.app.IntentService;
import android.content.Intent;

public abstract class AbstractIntentService extends IntentService {
    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
    }

    public AbstractIntentService(String str) {
        super(str);
    }
}
