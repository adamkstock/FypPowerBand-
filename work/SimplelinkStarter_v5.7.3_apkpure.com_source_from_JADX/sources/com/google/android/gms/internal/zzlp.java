package com.google.android.gms.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzlp extends Fragment implements OnCancelListener {
    /* access modifiers changed from: private */
    public static final GoogleApiAvailability zzacJ = GoogleApiAvailability.getInstance();
    /* access modifiers changed from: private */
    public boolean mStarted;
    /* access modifiers changed from: private */
    public boolean zzacK;
    /* access modifiers changed from: private */
    public int zzacL = -1;
    /* access modifiers changed from: private */
    public ConnectionResult zzacM;
    /* access modifiers changed from: private */
    public final Handler zzacN = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public zzll zzacO;
    private final SparseArray<zza> zzacP = new SparseArray<>();

    private class zza implements OnConnectionFailedListener {
        public final int zzacQ;
        public final GoogleApiClient zzacR;
        public final OnConnectionFailedListener zzacS;

        public zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzacQ = i;
            this.zzacR = googleApiClient;
            this.zzacS = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.append(str).append("GoogleApiClient #").print(this.zzacQ);
            printWriter.println(":");
            GoogleApiClient googleApiClient = this.zzacR;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("  ");
            googleApiClient.dump(sb.toString(), fileDescriptor, printWriter, strArr);
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            zzlp.this.zzacN.post(new zzb(this.zzacQ, connectionResult));
        }

        public void zzom() {
            this.zzacR.unregisterConnectionFailedListener(this);
            this.zzacR.disconnect();
        }
    }

    private class zzb implements Runnable {
        private final int zzacU;
        private final ConnectionResult zzacV;

        public zzb(int i, ConnectionResult connectionResult) {
            this.zzacU = i;
            this.zzacV = connectionResult;
        }

        public void run() {
            if (zzlp.this.mStarted && !zzlp.this.zzacK) {
                zzlp.this.zzacK = true;
                zzlp.this.zzacL = this.zzacU;
                zzlp.this.zzacM = this.zzacV;
                if (this.zzacV.hasResolution()) {
                    try {
                        this.zzacV.startResolutionForResult(zzlp.this.getActivity(), ((zzlp.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzlp.this) + 1) << 16) + 1);
                    } catch (SendIntentException unused) {
                        zzlp.this.zzok();
                    }
                } else if (zzlp.zzacJ.isUserResolvableError(this.zzacV.getErrorCode())) {
                    int errorCode = this.zzacV.getErrorCode();
                    FragmentActivity activity = zzlp.this.getActivity();
                    zzlp zzlp = zzlp.this;
                    GooglePlayServicesUtil.showErrorDialogFragment(errorCode, activity, zzlp, 2, zzlp);
                } else if (this.zzacV.getErrorCode() == 18) {
                    final Dialog zza = zzlp.zzacJ.zza(zzlp.this.getActivity(), zzlp.this);
                    zzlp zzlp2 = zzlp.this;
                    zzlp2.zzacO = zzll.zza(zzlp2.getActivity().getApplicationContext(), new zzll() {
                        /* access modifiers changed from: protected */
                        public void zzoi() {
                            zzlp.this.zzok();
                            zza.dismiss();
                        }
                    });
                } else {
                    zzlp.this.zza(this.zzacU, this.zzacV);
                }
            }
        }
    }

    public static zzlp zza(FragmentActivity fragmentActivity) {
        zzx.zzci("Must be called from main thread of process");
        try {
            zzlp zzlp = (zzlp) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFragment");
            if (zzlp == null || zzlp.isRemoving()) {
                return null;
            }
            return zzlp;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", e);
        }
    }

    /* access modifiers changed from: private */
    public void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        zza zza2 = (zza) this.zzacP.get(i);
        if (zza2 != null) {
            zzbp(i);
            OnConnectionFailedListener onConnectionFailedListener = zza2.zzacS;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzok();
    }

    public static zzlp zzb(FragmentActivity fragmentActivity) {
        zzlp zza2 = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zza2 != null) {
            return zza2;
        }
        zzlp zzlp = new zzlp();
        supportFragmentManager.beginTransaction().add((Fragment) zzlp, "GmsSupportLifecycleFragment").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        return zzlp;
    }

    /* access modifiers changed from: private */
    public void zzok() {
        this.zzacK = false;
        this.zzacL = -1;
        this.zzacM = null;
        zzll zzll = this.zzacO;
        if (zzll != null) {
            zzll.unregister();
            this.zzacO = null;
        }
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).zzacR.connect();
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0011, code lost:
        if (zzacJ.isGooglePlayServicesAvailable(getActivity()) != 0) goto L_0x0024;
     */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r1, int r2, android.content.Intent r3) {
        /*
            r0 = this;
            r3 = 1
            if (r1 == r3) goto L_0x0014
            r2 = 2
            if (r1 == r2) goto L_0x0007
            goto L_0x0024
        L_0x0007:
            com.google.android.gms.common.GoogleApiAvailability r1 = zzacJ
            android.support.v4.app.FragmentActivity r2 = r0.getActivity()
            int r1 = r1.isGooglePlayServicesAvailable(r2)
            if (r1 != 0) goto L_0x0024
            goto L_0x0025
        L_0x0014:
            r1 = -1
            if (r2 != r1) goto L_0x0018
            goto L_0x0025
        L_0x0018:
            if (r2 != 0) goto L_0x0024
            com.google.android.gms.common.ConnectionResult r1 = new com.google.android.gms.common.ConnectionResult
            r2 = 13
            r3 = 0
            r1.<init>(r2, r3)
            r0.zzacM = r1
        L_0x0024:
            r3 = 0
        L_0x0025:
            if (r3 == 0) goto L_0x002b
            r0.zzok()
            goto L_0x0032
        L_0x002b:
            int r1 = r0.zzacL
            com.google.android.gms.common.ConnectionResult r2 = r0.zzacM
            r0.zza(r1, r2)
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlp.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzacL, new ConnectionResult(13, null));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzacK = bundle.getBoolean("resolving_error", false);
            this.zzacL = bundle.getInt("failed_client_id", -1);
            if (this.zzacL >= 0) {
                this.zzacM = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzacK);
        int i = this.zzacL;
        if (i >= 0) {
            bundle.putInt("failed_client_id", i);
            bundle.putInt("failed_status", this.zzacM.getErrorCode());
            bundle.putParcelable("failed_resolution", this.zzacM.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzacK) {
            for (int i = 0; i < this.zzacP.size(); i++) {
                ((zza) this.zzacP.valueAt(i)).zzacR.connect();
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzacP.size(); i++) {
            ((zza) this.zzacP.valueAt(i)).zzacR.disconnect();
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb(googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        boolean z = this.zzacP.indexOfKey(i) < 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Already managing a GoogleApiClient with id ");
        sb.append(i);
        zzx.zza(z, (Object) sb.toString());
        this.zzacP.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzacK) {
            googleApiClient.connect();
        }
    }

    public void zzbp(int i) {
        zza zza2 = (zza) this.zzacP.get(i);
        this.zzacP.remove(i);
        if (zza2 != null) {
            zza2.zzom();
        }
    }
}
