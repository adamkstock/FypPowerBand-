package android.support.p000v4.media;

import android.media.browse.MediaBrowser;
import android.media.browse.MediaBrowser.MediaItem;
import android.os.Bundle;
import java.util.List;

/* renamed from: android.support.v4.media.MediaBrowserCompatApi24 */
class MediaBrowserCompatApi24 {

    /* renamed from: android.support.v4.media.MediaBrowserCompatApi24$SubscriptionCallback */
    interface SubscriptionCallback extends SubscriptionCallback {
        void onChildrenLoaded(String str, List<?> list, Bundle bundle);

        void onError(String str, Bundle bundle);
    }

    /* renamed from: android.support.v4.media.MediaBrowserCompatApi24$SubscriptionCallbackProxy */
    static class SubscriptionCallbackProxy<T extends SubscriptionCallback> extends SubscriptionCallbackProxy<T> {
        public SubscriptionCallbackProxy(T t) {
            super(t);
        }

        public void onChildrenLoaded(String str, List<MediaItem> list, Bundle bundle) {
            ((SubscriptionCallback) this.mSubscriptionCallback).onChildrenLoaded(str, list, bundle);
        }

        public void onError(String str, Bundle bundle) {
            ((SubscriptionCallback) this.mSubscriptionCallback).onError(str, bundle);
        }
    }

    MediaBrowserCompatApi24() {
    }

    public static Object createSubscriptionCallback(SubscriptionCallback subscriptionCallback) {
        return new SubscriptionCallbackProxy(subscriptionCallback);
    }

    public static void subscribe(Object obj, String str, Bundle bundle, Object obj2) {
        ((MediaBrowser) obj).subscribe(str, bundle, (android.media.browse.MediaBrowser.SubscriptionCallback) obj2);
    }

    public static void unsubscribe(Object obj, String str, Object obj2) {
        ((MediaBrowser) obj).unsubscribe(str, (android.media.browse.MediaBrowser.SubscriptionCallback) obj2);
    }
}
