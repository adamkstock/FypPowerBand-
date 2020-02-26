package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.C0310R;
import com.google.android.gms.internal.zzmq;

public final class zzg {
    public static String zzc(Context context, int i, String str) {
        Resources resources = context.getResources();
        if (i != 1) {
            if (i == 2) {
                return resources.getString(C0310R.string.common_google_play_services_update_text, new Object[]{str});
            } else if (i == 3) {
                return resources.getString(C0310R.string.common_google_play_services_enable_text, new Object[]{str});
            } else if (i == 5) {
                return resources.getString(C0310R.string.common_google_play_services_invalid_account_text);
            } else {
                if (i == 7) {
                    return resources.getString(C0310R.string.common_google_play_services_network_error_text);
                }
                if (i == 9) {
                    return resources.getString(C0310R.string.common_google_play_services_unsupported_text, new Object[]{str});
                } else if (i != 42) {
                    switch (i) {
                        case 16:
                            return resources.getString(C0310R.string.common_google_play_services_api_unavailable_text, new Object[]{str});
                        case 17:
                            return resources.getString(C0310R.string.common_google_play_services_sign_in_failed_text);
                        case 18:
                            return resources.getString(C0310R.string.common_google_play_services_updating_text, new Object[]{str});
                        default:
                            return resources.getString(C0310R.string.common_google_play_services_unknown_issue);
                    }
                } else {
                    return resources.getString(C0310R.string.common_android_wear_update_text, new Object[]{str});
                }
            }
        } else if (zzmq.zzb(resources)) {
            return resources.getString(C0310R.string.common_google_play_services_install_text_tablet, new Object[]{str});
        } else {
            return resources.getString(C0310R.string.common_google_play_services_install_text_phone, new Object[]{str});
        }
    }

    public static String zzd(Context context, int i, String str) {
        Resources resources = context.getResources();
        if (i != 1) {
            if (i == 2) {
                return resources.getString(C0310R.string.common_google_play_services_update_text, new Object[]{str});
            } else if (i == 3) {
                return resources.getString(C0310R.string.common_google_play_services_enable_text, new Object[]{str});
            } else if (i == 5) {
                return resources.getString(C0310R.string.common_google_play_services_invalid_account_text);
            } else {
                if (i == 7) {
                    return resources.getString(C0310R.string.common_google_play_services_network_error_text);
                }
                if (i == 9) {
                    return resources.getString(C0310R.string.common_google_play_services_unsupported_text, new Object[]{str});
                } else if (i != 42) {
                    switch (i) {
                        case 16:
                            return resources.getString(C0310R.string.common_google_play_services_api_unavailable_text, new Object[]{str});
                        case 17:
                            return resources.getString(C0310R.string.common_google_play_services_sign_in_failed_text);
                        case 18:
                            return resources.getString(C0310R.string.common_google_play_services_updating_text, new Object[]{str});
                        default:
                            return resources.getString(C0310R.string.common_google_play_services_unknown_issue);
                    }
                } else {
                    return resources.getString(C0310R.string.common_android_wear_notification_needs_update_text, new Object[]{str});
                }
            }
        } else if (zzmq.zzb(resources)) {
            return resources.getString(C0310R.string.common_google_play_services_install_text_tablet, new Object[]{str});
        } else {
            return resources.getString(C0310R.string.common_google_play_services_install_text_phone, new Object[]{str});
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0022, code lost:
        android.util.Log.e(r1, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0025, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String zzg(android.content.Context r3, int r4) {
        /*
            android.content.res.Resources r3 = r3.getResources()
            r0 = 42
            if (r4 == r0) goto L_0x0063
            r0 = 0
            java.lang.String r1 = "GoogleApiAvailability"
            switch(r4) {
                case 1: goto L_0x0060;
                case 2: goto L_0x005d;
                case 3: goto L_0x005a;
                case 4: goto L_0x0059;
                case 5: goto L_0x0051;
                case 6: goto L_0x0059;
                case 7: goto L_0x0049;
                case 8: goto L_0x0046;
                case 9: goto L_0x003e;
                case 10: goto L_0x003b;
                case 11: goto L_0x0038;
                default: goto L_0x000e;
            }
        L_0x000e:
            switch(r4) {
                case 16: goto L_0x0035;
                case 17: goto L_0x002d;
                case 18: goto L_0x0026;
                default: goto L_0x0011;
            }
        L_0x0011:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r2 = "Unexpected error code "
            r3.append(r2)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
        L_0x0022:
            android.util.Log.e(r1, r3)
            return r0
        L_0x0026:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_updating_title
        L_0x0028:
            java.lang.String r3 = r3.getString(r4)
            return r3
        L_0x002d:
            java.lang.String r4 = "The specified account could not be signed in."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_sign_in_failed_title
            goto L_0x0028
        L_0x0035:
            java.lang.String r3 = "One of the API components you attempted to connect to is not available."
            goto L_0x0022
        L_0x0038:
            java.lang.String r3 = "The application is not licensed to the user."
            goto L_0x0022
        L_0x003b:
            java.lang.String r3 = "Developer error occurred. Please see logs for detailed information"
            goto L_0x0022
        L_0x003e:
            java.lang.String r4 = "Google Play services is invalid. Cannot recover."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_unsupported_title
            goto L_0x0028
        L_0x0046:
            java.lang.String r3 = "Internal error occurred. Please see logs for detailed information"
            goto L_0x0022
        L_0x0049:
            java.lang.String r4 = "Network error occurred. Please retry request later."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_network_error_title
            goto L_0x0028
        L_0x0051:
            java.lang.String r4 = "An invalid account was specified when connecting. Please provide a valid account."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_invalid_account_title
            goto L_0x0028
        L_0x0059:
            return r0
        L_0x005a:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_enable_title
            goto L_0x0028
        L_0x005d:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_update_title
            goto L_0x0028
        L_0x0060:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_install_title
            goto L_0x0028
        L_0x0063:
            int r4 = com.google.android.gms.C0310R.string.common_android_wear_update_title
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzg.zzg(android.content.Context, int):java.lang.String");
    }

    public static String zzh(Context context, int i) {
        int i2;
        Resources resources = context.getResources();
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    i2 = C0310R.string.common_google_play_services_enable_button;
                } else if (i != 42) {
                    i2 = 17039370;
                }
            }
            i2 = C0310R.string.common_google_play_services_update_button;
        } else {
            i2 = C0310R.string.common_google_play_services_install_button;
        }
        return resources.getString(i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0022, code lost:
        android.util.Log.e(r1, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0025, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String zzi(android.content.Context r3, int r4) {
        /*
            android.content.res.Resources r3 = r3.getResources()
            r0 = 42
            if (r4 == r0) goto L_0x0063
            r0 = 0
            java.lang.String r1 = "GoogleApiAvailability"
            switch(r4) {
                case 1: goto L_0x0060;
                case 2: goto L_0x005d;
                case 3: goto L_0x005a;
                case 4: goto L_0x0059;
                case 5: goto L_0x0051;
                case 6: goto L_0x0059;
                case 7: goto L_0x0049;
                case 8: goto L_0x0046;
                case 9: goto L_0x003e;
                case 10: goto L_0x003b;
                case 11: goto L_0x0038;
                default: goto L_0x000e;
            }
        L_0x000e:
            switch(r4) {
                case 16: goto L_0x0035;
                case 17: goto L_0x002d;
                case 18: goto L_0x0026;
                default: goto L_0x0011;
            }
        L_0x0011:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r2 = "Unexpected error code "
            r3.append(r2)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
        L_0x0022:
            android.util.Log.e(r1, r3)
            return r0
        L_0x0026:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_updating_title
        L_0x0028:
            java.lang.String r3 = r3.getString(r4)
            return r3
        L_0x002d:
            java.lang.String r4 = "The specified account could not be signed in."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_sign_in_failed_title
            goto L_0x0028
        L_0x0035:
            java.lang.String r3 = "One of the API components you attempted to connect to is not available."
            goto L_0x0022
        L_0x0038:
            java.lang.String r3 = "The application is not licensed to the user."
            goto L_0x0022
        L_0x003b:
            java.lang.String r3 = "Developer error occurred. Please see logs for detailed information"
            goto L_0x0022
        L_0x003e:
            java.lang.String r4 = "Google Play services is invalid. Cannot recover."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_unsupported_title
            goto L_0x0028
        L_0x0046:
            java.lang.String r3 = "Internal error occurred. Please see logs for detailed information"
            goto L_0x0022
        L_0x0049:
            java.lang.String r4 = "Network error occurred. Please retry request later."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_network_error_title
            goto L_0x0028
        L_0x0051:
            java.lang.String r4 = "An invalid account was specified when connecting. Please provide a valid account."
            android.util.Log.e(r1, r4)
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_invalid_account_title
            goto L_0x0028
        L_0x0059:
            return r0
        L_0x005a:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_enable_title
            goto L_0x0028
        L_0x005d:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_update_title
            goto L_0x0028
        L_0x0060:
            int r4 = com.google.android.gms.C0310R.string.common_google_play_services_install_title
            goto L_0x0028
        L_0x0063:
            int r4 = com.google.android.gms.C0310R.string.common_android_wear_update_title
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzg.zzi(android.content.Context, int):java.lang.String");
    }
}
