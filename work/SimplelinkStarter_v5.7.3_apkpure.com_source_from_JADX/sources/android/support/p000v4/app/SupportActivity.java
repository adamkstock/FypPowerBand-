package android.support.p000v4.app;

import android.app.Activity;
import android.support.p000v4.util.SimpleArrayMap;

/* renamed from: android.support.v4.app.SupportActivity */
public class SupportActivity extends Activity {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap = new SimpleArrayMap<>();

    /* renamed from: android.support.v4.app.SupportActivity$ExtraData */
    public static class ExtraData {
    }

    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }

    public <T extends ExtraData> T getExtraData(Class<T> cls) {
        return (ExtraData) this.mExtraDataMap.get(cls);
    }
}
