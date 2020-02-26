package com.google.android.gms.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.widget.ProgressBar;
import com.google.android.gms.C0310R;
import com.google.android.gms.common.internal.zzn;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class GoogleApiAvailability {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailability zzaab = new GoogleApiAvailability();

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzaab;
    }

    private String zzk(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("gcore_");
        sb.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        String str2 = "-";
        sb.append(str2);
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
        }
        sb.append(str2);
        if (context != null) {
            sb.append(context.getPackageName());
        }
        sb.append(str2);
        if (context != null) {
            try {
                sb.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            } catch (NameNotFoundException unused) {
            }
        }
        return sb.toString();
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2) {
        return GooglePlayServicesUtil.getErrorDialog(i, activity, i2);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        return GooglePlayServicesUtil.getErrorDialog(i, activity, i2, onCancelListener);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return zza(context, i, i2, null);
    }

    public final String getErrorString(int i) {
        return GooglePlayServicesUtil.getErrorString(i);
    }

    public String getOpenSourceSoftwareLicenseInfo(Context context) {
        return GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(context);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.zzd(context, isGooglePlayServicesAvailable)) {
            return 18;
        }
        return isGooglePlayServicesAvailable;
    }

    public final boolean isUserResolvableError(int i) {
        return GooglePlayServicesUtil.isUserRecoverableError(i);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2) {
        return GooglePlayServicesUtil.showErrorDialogFragment(i, activity, i2);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        return GooglePlayServicesUtil.showErrorDialogFragment(i, activity, i2, onCancelListener);
    }

    public void showErrorNotification(Context context, int i) {
        GooglePlayServicesUtil.showErrorNotification(i, context);
    }

    public Dialog zza(Activity activity, OnCancelListener onCancelListener) {
        ProgressBar progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        Builder builder = new Builder(activity);
        builder.setView(progressBar);
        String zzaf = GooglePlayServicesUtil.zzaf(activity);
        builder.setMessage(activity.getResources().getString(C0310R.string.common_google_play_services_updating_text, new Object[]{zzaf}));
        builder.setTitle(C0310R.string.common_google_play_services_updating_title);
        builder.setPositiveButton("", null);
        AlertDialog create = builder.create();
        GooglePlayServicesUtil.zza(activity, onCancelListener, "GooglePlayServicesUpdatingDialog", create);
        return create;
    }

    public PendingIntent zza(Context context, int i, int i2, String str) {
        Intent zza = zza(context, i, str);
        if (zza == null) {
            return null;
        }
        return PendingIntent.getActivity(context, i2, zza, ClientDefaults.MAX_MSG_SIZE);
    }

    public Intent zza(Context context, int i, String str) {
        String str2 = "com.google.android.gms";
        if (i == 1 || i == 2) {
            return zzn.zzw(str2, zzk(context, str));
        }
        if (i == 3) {
            return zzn.zzco(str2);
        }
        if (i != 42) {
            return null;
        }
        return zzn.zzpo();
    }

    public void zzab(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtil.zzaa(context);
    }

    public void zzac(Context context) {
        GooglePlayServicesUtil.zzac(context);
    }

    @Deprecated
    public Intent zzbi(int i) {
        return zza(null, i, null);
    }

    public boolean zzd(Context context, int i) {
        return GooglePlayServicesUtil.zzd(context, i);
    }

    public boolean zzj(Context context, String str) {
        return GooglePlayServicesUtil.zzj(context, str);
    }
}
