package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class zza<T extends LifecycleDelegate> {
    /* access modifiers changed from: private */
    public T zzapn;
    /* access modifiers changed from: private */
    public Bundle zzapo;
    /* access modifiers changed from: private */
    public LinkedList<C1128zza> zzapp;
    private final zzf<T> zzapq = new zzf<T>() {
        public void zza(T t) {
            zza.this.zzapn = t;
            Iterator it = zza.this.zzapp.iterator();
            while (it.hasNext()) {
                ((C1128zza) it.next()).zzb(zza.this.zzapn);
            }
            zza.this.zzapp.clear();
            zza.this.zzapo = null;
        }
    };

    /* renamed from: com.google.android.gms.dynamic.zza$zza reason: collision with other inner class name */
    private interface C1128zza {
        int getState();

        void zzb(LifecycleDelegate lifecycleDelegate);
    }

    private void zza(Bundle bundle, C1128zza zza) {
        T t = this.zzapn;
        if (t != null) {
            zza.zzb(t);
            return;
        }
        if (this.zzapp == null) {
            this.zzapp = new LinkedList<>();
        }
        this.zzapp.add(zza);
        if (bundle != null) {
            Bundle bundle2 = this.zzapo;
            if (bundle2 == null) {
                this.zzapo = (Bundle) bundle.clone();
            } else {
                bundle2.putAll(bundle);
            }
        }
        zza(this.zzapq);
    }

    public static void zzb(FrameLayout frameLayout) {
        final Context context = frameLayout.getContext();
        final int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        String zzc = zzg.zzc(context, isGooglePlayServicesAvailable, GooglePlayServicesUtil.zzaf(context));
        String zzh = zzg.zzh(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(zzc);
        linearLayout.addView(textView);
        if (zzh != null) {
            Button button = new Button(context);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(zzh);
            linearLayout.addView(button);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    context.startActivity(GooglePlayServicesUtil.zzbj(isGooglePlayServicesAvailable));
                }
            });
        }
    }

    private void zzer(int i) {
        while (!this.zzapp.isEmpty() && ((C1128zza) this.zzapp.getLast()).getState() >= i) {
            this.zzapp.removeLast();
        }
    }

    public void onCreate(final Bundle bundle) {
        zza(bundle, (C1128zza) new C1128zza() {
            public int getState() {
                return 1;
            }

            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onCreate(bundle);
            }
        });
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        final FrameLayout frameLayout2 = frameLayout;
        final LayoutInflater layoutInflater2 = layoutInflater;
        final ViewGroup viewGroup2 = viewGroup;
        final Bundle bundle2 = bundle;
        C05484 r0 = new C1128zza() {
            public int getState() {
                return 2;
            }

            public void zzb(LifecycleDelegate lifecycleDelegate) {
                frameLayout2.removeAllViews();
                frameLayout2.addView(zza.this.zzapn.onCreateView(layoutInflater2, viewGroup2, bundle2));
            }
        };
        zza(bundle, (C1128zza) r0);
        if (this.zzapn == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        T t = this.zzapn;
        if (t != null) {
            t.onDestroy();
        } else {
            zzer(1);
        }
    }

    public void onDestroyView() {
        T t = this.zzapn;
        if (t != null) {
            t.onDestroyView();
        } else {
            zzer(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle bundle, final Bundle bundle2) {
        zza(bundle2, (C1128zza) new C1128zza() {
            public int getState() {
                return 0;
            }

            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onInflate(activity, bundle, bundle2);
            }
        });
    }

    public void onLowMemory() {
        T t = this.zzapn;
        if (t != null) {
            t.onLowMemory();
        }
    }

    public void onPause() {
        T t = this.zzapn;
        if (t != null) {
            t.onPause();
        } else {
            zzer(5);
        }
    }

    public void onResume() {
        zza((Bundle) null, (C1128zza) new C1128zza() {
            public int getState() {
                return 5;
            }

            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onResume();
            }
        });
    }

    public void onSaveInstanceState(Bundle bundle) {
        T t = this.zzapn;
        if (t != null) {
            t.onSaveInstanceState(bundle);
            return;
        }
        Bundle bundle2 = this.zzapo;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
    }

    public void onStart() {
        zza((Bundle) null, (C1128zza) new C1128zza() {
            public int getState() {
                return 4;
            }

            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onStart();
            }
        });
    }

    public void onStop() {
        T t = this.zzapn;
        if (t != null) {
            t.onStop();
        } else {
            zzer(4);
        }
    }

    /* access modifiers changed from: protected */
    public void zza(FrameLayout frameLayout) {
        zzb(frameLayout);
    }

    /* access modifiers changed from: protected */
    public abstract void zza(zzf<T> zzf);

    public T zzrZ() {
        return this.zzapn;
    }
}
