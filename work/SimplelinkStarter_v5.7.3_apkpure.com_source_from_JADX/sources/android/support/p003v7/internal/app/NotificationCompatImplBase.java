package android.support.p003v7.internal.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.p000v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.p000v4.app.NotificationCompatBase.Action;
import android.support.p003v7.appcompat.C0251R;
import android.widget.RemoteViews;
import java.util.List;

/* renamed from: android.support.v7.internal.app.NotificationCompatImplBase */
public class NotificationCompatImplBase {
    static final int MAX_MEDIA_BUTTONS = 5;
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;

    public static <T extends Action> void overrideContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, Bitmap bitmap, CharSequence charSequence4, boolean z, long j, List<T> list, int[] iArr, boolean z2, PendingIntent pendingIntent) {
        notificationBuilderWithBuilderAccessor.getBuilder().setContent(generateContentView(context, charSequence, charSequence2, charSequence3, i, bitmap, charSequence4, z, j, list, iArr, z2, pendingIntent));
        if (z2) {
            notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
        }
    }

    private static <T extends Action> RemoteViews generateContentView(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, Bitmap bitmap, CharSequence charSequence4, boolean z, long j, List<T> list, int[] iArr, boolean z2, PendingIntent pendingIntent) {
        int i2;
        int[] iArr2 = iArr;
        RemoteViews applyStandardTemplate = applyStandardTemplate(context, charSequence, charSequence2, charSequence3, i, bitmap, charSequence4, z, j, C0251R.layout.notification_template_media, true);
        int size = list.size();
        if (iArr2 == null) {
            i2 = 0;
        } else {
            i2 = Math.min(iArr2.length, 3);
        }
        applyStandardTemplate.removeAllViews(C0251R.C0253id.media_actions);
        if (i2 > 0) {
            int i3 = 0;
            while (i3 < i2) {
                if (i3 < size) {
                    Context context2 = context;
                    applyStandardTemplate.addView(C0251R.C0253id.media_actions, generateMediaActionButton(context, (Action) list.get(iArr2[i3])));
                    i3++;
                } else {
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[]{Integer.valueOf(i3), Integer.valueOf(size - 1)}));
                }
            }
        }
        Context context3 = context;
        if (z2) {
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.end_padder, 8);
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.cancel_action, 0);
            applyStandardTemplate.setOnClickPendingIntent(C0251R.C0253id.cancel_action, pendingIntent);
            applyStandardTemplate.setInt(C0251R.C0253id.cancel_action, "setAlpha", context.getResources().getInteger(C0251R.integer.cancel_button_image_alpha));
        } else {
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.end_padder, 0);
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.cancel_action, 8);
        }
        return applyStandardTemplate;
    }

    public static <T extends Action> void overrideBigContentView(Notification notification, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, Bitmap bitmap, CharSequence charSequence4, boolean z, long j, List<T> list, boolean z2, PendingIntent pendingIntent) {
        notification.bigContentView = generateBigContentView(context, charSequence, charSequence2, charSequence3, i, bitmap, charSequence4, z, j, list, z2, pendingIntent);
        if (z2) {
            notification.flags |= 2;
        }
    }

    private static <T extends Action> RemoteViews generateBigContentView(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, Bitmap bitmap, CharSequence charSequence4, boolean z, long j, List<T> list, boolean z2, PendingIntent pendingIntent) {
        int min = Math.min(list.size(), 5);
        RemoteViews applyStandardTemplate = applyStandardTemplate(context, charSequence, charSequence2, charSequence3, i, bitmap, charSequence4, z, j, getBigLayoutResource(min), false);
        applyStandardTemplate.removeAllViews(C0251R.C0253id.media_actions);
        if (min > 0) {
            for (int i2 = 0; i2 < min; i2++) {
                Context context2 = context;
                applyStandardTemplate.addView(C0251R.C0253id.media_actions, generateMediaActionButton(context, (Action) list.get(i2)));
            }
        }
        Context context3 = context;
        if (z2) {
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.cancel_action, 0);
            applyStandardTemplate.setInt(C0251R.C0253id.cancel_action, "setAlpha", context.getResources().getInteger(C0251R.integer.cancel_button_image_alpha));
            applyStandardTemplate.setOnClickPendingIntent(C0251R.C0253id.cancel_action, pendingIntent);
        } else {
            applyStandardTemplate.setViewVisibility(C0251R.C0253id.cancel_action, 8);
        }
        return applyStandardTemplate;
    }

    private static RemoteViews generateMediaActionButton(Context context, Action action) {
        boolean z = action.getActionIntent() == null;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C0251R.layout.notification_media_action);
        remoteViews.setImageViewResource(C0251R.C0253id.action0, action.getIcon());
        if (!z) {
            remoteViews.setOnClickPendingIntent(C0251R.C0253id.action0, action.getActionIntent());
        }
        if (VERSION.SDK_INT >= 15) {
            remoteViews.setContentDescription(C0251R.C0253id.action0, action.getTitle());
        }
        return remoteViews;
    }

    private static int getBigLayoutResource(int i) {
        if (i <= 3) {
            return C0251R.layout.notification_template_big_media_narrow;
        }
        return C0251R.layout.notification_template_big_media;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0106  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.widget.RemoteViews applyStandardTemplate(android.content.Context r14, java.lang.CharSequence r15, java.lang.CharSequence r16, java.lang.CharSequence r17, int r18, android.graphics.Bitmap r19, java.lang.CharSequence r20, boolean r21, long r22, int r24, boolean r25) {
        /*
            r0 = r15
            r1 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            r5 = r20
            r6 = r22
            android.widget.RemoteViews r8 = new android.widget.RemoteViews
            java.lang.String r9 = r14.getPackageName()
            r10 = r24
            r8.<init>(r9, r10)
            r9 = 16
            r10 = 8
            if (r4 == 0) goto L_0x0028
            int r11 = android.os.Build.VERSION.SDK_INT
            if (r11 < r9) goto L_0x0028
            int r11 = android.support.p003v7.appcompat.C0251R.C0253id.icon
            r8.setImageViewBitmap(r11, r4)
            goto L_0x002d
        L_0x0028:
            int r4 = android.support.p003v7.appcompat.C0251R.C0253id.icon
            r8.setViewVisibility(r4, r10)
        L_0x002d:
            if (r0 == 0) goto L_0x0034
            int r4 = android.support.p003v7.appcompat.C0251R.C0253id.title
            r8.setTextViewText(r4, r15)
        L_0x0034:
            r0 = 1
            r4 = 0
            if (r1 == 0) goto L_0x003f
            int r11 = android.support.p003v7.appcompat.C0251R.C0253id.text
            r8.setTextViewText(r11, r1)
            r11 = 1
            goto L_0x0040
        L_0x003f:
            r11 = 0
        L_0x0040:
            if (r2 == 0) goto L_0x004e
            int r3 = android.support.p003v7.appcompat.C0251R.C0253id.info
            r8.setTextViewText(r3, r2)
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.info
            r8.setViewVisibility(r2, r4)
        L_0x004c:
            r11 = 1
            goto L_0x0085
        L_0x004e:
            if (r3 <= 0) goto L_0x0080
            android.content.res.Resources r2 = r14.getResources()
            int r11 = android.support.p003v7.appcompat.C0251R.integer.status_bar_notification_info_maxnum
            int r2 = r2.getInteger(r11)
            if (r3 <= r2) goto L_0x006c
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.info
            android.content.res.Resources r3 = r14.getResources()
            int r11 = android.support.p003v7.appcompat.C0251R.string.status_bar_notification_info_overflow
            java.lang.String r3 = r3.getString(r11)
            r8.setTextViewText(r2, r3)
            goto L_0x007a
        L_0x006c:
            java.text.NumberFormat r2 = java.text.NumberFormat.getIntegerInstance()
            int r11 = android.support.p003v7.appcompat.C0251R.C0253id.info
            long r12 = (long) r3
            java.lang.String r2 = r2.format(r12)
            r8.setTextViewText(r11, r2)
        L_0x007a:
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.info
            r8.setViewVisibility(r2, r4)
            goto L_0x004c
        L_0x0080:
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.info
            r8.setViewVisibility(r2, r10)
        L_0x0085:
            if (r5 == 0) goto L_0x00a3
            int r2 = android.os.Build.VERSION.SDK_INT
            if (r2 < r9) goto L_0x00a3
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.text
            r8.setTextViewText(r2, r5)
            if (r1 == 0) goto L_0x009e
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.text2
            r8.setTextViewText(r2, r1)
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.text2
            r8.setViewVisibility(r1, r4)
            r1 = 1
            goto L_0x00a4
        L_0x009e:
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.text2
            r8.setViewVisibility(r1, r10)
        L_0x00a3:
            r1 = 0
        L_0x00a4:
            if (r1 == 0) goto L_0x00cf
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r9) goto L_0x00cf
            if (r25 == 0) goto L_0x00bc
            android.content.res.Resources r1 = r14.getResources()
            int r2 = android.support.p003v7.appcompat.C0251R.dimen.notification_subtext_size
            int r1 = r1.getDimensionPixelSize(r2)
            float r1 = (float) r1
            int r2 = android.support.p003v7.appcompat.C0251R.C0253id.text
            r8.setTextViewTextSize(r2, r4, r1)
        L_0x00bc:
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.line1
            r2 = 0
            r3 = 0
            r5 = 0
            r9 = 0
            r14 = r8
            r15 = r1
            r16 = r2
            r17 = r3
            r18 = r5
            r19 = r9
            r14.setViewPadding(r15, r16, r17, r18, r19)
        L_0x00cf:
            r1 = 0
            int r3 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r3 == 0) goto L_0x0101
            if (r21 == 0) goto L_0x00f5
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.chronometer
            r8.setViewVisibility(r1, r4)
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.chronometer
            long r2 = android.os.SystemClock.elapsedRealtime()
            long r12 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r12
            long r2 = r2 + r6
            java.lang.String r5 = "setBase"
            r8.setLong(r1, r5, r2)
            int r1 = android.support.p003v7.appcompat.C0251R.C0253id.chronometer
            java.lang.String r2 = "setStarted"
            r8.setBoolean(r1, r2, r0)
            goto L_0x0101
        L_0x00f5:
            int r0 = android.support.p003v7.appcompat.C0251R.C0253id.time
            r8.setViewVisibility(r0, r4)
            int r0 = android.support.p003v7.appcompat.C0251R.C0253id.time
            java.lang.String r1 = "setTime"
            r8.setLong(r0, r1, r6)
        L_0x0101:
            int r0 = android.support.p003v7.appcompat.C0251R.C0253id.line3
            if (r11 == 0) goto L_0x0106
            goto L_0x0108
        L_0x0106:
            r4 = 8
        L_0x0108:
            r8.setViewVisibility(r0, r4)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.internal.app.NotificationCompatImplBase.applyStandardTemplate(android.content.Context, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, int, android.graphics.Bitmap, java.lang.CharSequence, boolean, long, int, boolean):android.widget.RemoteViews");
    }
}
