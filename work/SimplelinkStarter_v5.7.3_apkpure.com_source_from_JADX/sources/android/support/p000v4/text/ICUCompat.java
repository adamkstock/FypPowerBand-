package android.support.p000v4.text;

import android.os.Build.VERSION;
import java.util.Locale;

/* renamed from: android.support.v4.text.ICUCompat */
public final class ICUCompat {
    private static final ICUCompatBaseImpl IMPL;

    /* renamed from: android.support.v4.text.ICUCompat$ICUCompatApi21Impl */
    static class ICUCompatApi21Impl extends ICUCompatBaseImpl {
        ICUCompatApi21Impl() {
        }

        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatApi21.maximizeAndGetScript(locale);
        }
    }

    /* renamed from: android.support.v4.text.ICUCompat$ICUCompatBaseImpl */
    static class ICUCompatBaseImpl {
        ICUCompatBaseImpl() {
        }

        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }

    static {
        if (VERSION.SDK_INT >= 21) {
            IMPL = new ICUCompatApi21Impl();
        } else {
            IMPL = new ICUCompatBaseImpl();
        }
    }

    public static String maximizeAndGetScript(Locale locale) {
        return IMPL.maximizeAndGetScript(locale);
    }

    private ICUCompat() {
    }
}
