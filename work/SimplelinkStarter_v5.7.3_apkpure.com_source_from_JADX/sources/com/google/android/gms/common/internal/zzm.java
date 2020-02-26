package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.jmdns.impl.constants.DNSConstants;

final class zzm extends zzl implements Callback {
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public final HashMap<zza, zzb> zzafY = new HashMap<>();
    /* access modifiers changed from: private */
    public final com.google.android.gms.common.stats.zzb zzafZ;
    private final long zzaga;
    /* access modifiers changed from: private */
    public final Context zzqZ;

    private static final class zza {
        private final String zzPp;
        private final ComponentName zzagb;

        public zza(ComponentName componentName) {
            this.zzPp = null;
            this.zzagb = (ComponentName) zzx.zzw(componentName);
        }

        public zza(String str) {
            this.zzPp = zzx.zzcr(str);
            this.zzagb = null;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zza = (zza) obj;
            if (!zzw.equal(this.zzPp, zza.zzPp) || !zzw.equal(this.zzagb, zza.zzagb)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return zzw.hashCode(this.zzPp, this.zzagb);
        }

        public String toString() {
            String str = this.zzPp;
            return str == null ? this.zzagb.flattenToString() : str;
        }

        public Intent zzpm() {
            String str = this.zzPp;
            return str != null ? new Intent(str).setPackage("com.google.android.gms") : new Intent().setComponent(this.zzagb);
        }
    }

    private final class zzb {
        /* access modifiers changed from: private */
        public int mState = 2;
        /* access modifiers changed from: private */
        public IBinder zzaeJ;
        /* access modifiers changed from: private */
        public ComponentName zzagb;
        private final zza zzagc = new zza();
        /* access modifiers changed from: private */
        public final Set<ServiceConnection> zzagd = new HashSet();
        private boolean zzage;
        /* access modifiers changed from: private */
        public final zza zzagf;

        public class zza implements ServiceConnection {
            public zza() {
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (zzm.this.zzafY) {
                    zzb.this.zzaeJ = iBinder;
                    zzb.this.zzagb = componentName;
                    for (ServiceConnection onServiceConnected : zzb.this.zzagd) {
                        onServiceConnected.onServiceConnected(componentName, iBinder);
                    }
                    zzb.this.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (zzm.this.zzafY) {
                    zzb.this.zzaeJ = null;
                    zzb.this.zzagb = componentName;
                    for (ServiceConnection onServiceDisconnected : zzb.this.zzagd) {
                        onServiceDisconnected.onServiceDisconnected(componentName);
                    }
                    zzb.this.mState = 2;
                }
            }
        }

        public zzb(zza zza2) {
            this.zzagf = zza2;
        }

        public IBinder getBinder() {
            return this.zzaeJ;
        }

        public ComponentName getComponentName() {
            return this.zzagb;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.zzage;
        }

        public void zza(ServiceConnection serviceConnection, String str) {
            zzm.this.zzafZ.zza(zzm.this.zzqZ, serviceConnection, str, this.zzagf.zzpm());
            this.zzagd.add(serviceConnection);
        }

        public boolean zza(ServiceConnection serviceConnection) {
            return this.zzagd.contains(serviceConnection);
        }

        public void zzb(ServiceConnection serviceConnection, String str) {
            zzm.this.zzafZ.zzb(zzm.this.zzqZ, serviceConnection);
            this.zzagd.remove(serviceConnection);
        }

        public void zzcm(String str) {
            this.mState = 3;
            this.zzage = zzm.this.zzafZ.zza(zzm.this.zzqZ, str, this.zzagf.zzpm(), (ServiceConnection) this.zzagc, 129);
            if (!this.zzage) {
                this.mState = 2;
                try {
                    zzm.this.zzafZ.zza(zzm.this.zzqZ, this.zzagc);
                } catch (IllegalArgumentException unused) {
                }
            }
        }

        public void zzcn(String str) {
            zzm.this.zzafZ.zza(zzm.this.zzqZ, this.zzagc);
            this.zzage = false;
            this.mState = 2;
        }

        public boolean zzpn() {
            return this.zzagd.isEmpty();
        }
    }

    zzm(Context context) {
        this.zzqZ = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.zzafZ = com.google.android.gms.common.stats.zzb.zzqh();
        this.zzaga = DNSConstants.CLOSE_TIMEOUT;
    }

    private boolean zza(zza zza2, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        zzx.zzb(serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            zzb zzb2 = (zzb) this.zzafY.get(zza2);
            if (zzb2 == null) {
                zzb2 = new zzb(zza2);
                zzb2.zza(serviceConnection, str);
                zzb2.zzcm(str);
                this.zzafY.put(zza2, zzb2);
            } else {
                this.mHandler.removeMessages(0, zzb2);
                if (!zzb2.zza(serviceConnection)) {
                    zzb2.zza(serviceConnection, str);
                    int state = zzb2.getState();
                    if (state == 1) {
                        serviceConnection.onServiceConnected(zzb2.getComponentName(), zzb2.getBinder());
                    } else if (state == 2) {
                        zzb2.zzcm(str);
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(zza2);
                    throw new IllegalStateException(sb.toString());
                }
            }
            isBound = zzb2.isBound();
        }
        return isBound;
    }

    private void zzb(zza zza2, ServiceConnection serviceConnection, String str) {
        zzx.zzb(serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            zzb zzb2 = (zzb) this.zzafY.get(zza2);
            if (zzb2 == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Nonexistent connection status for service config: ");
                sb.append(zza2);
                throw new IllegalStateException(sb.toString());
            } else if (zzb2.zza(serviceConnection)) {
                zzb2.zzb(serviceConnection, str);
                if (zzb2.zzpn()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, zzb2), this.zzaga);
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                sb2.append(zza2);
                throw new IllegalStateException(sb2.toString());
            }
        }
    }

    public boolean handleMessage(Message message) {
        if (message.what != 0) {
            return false;
        }
        zzb zzb2 = (zzb) message.obj;
        synchronized (this.zzafY) {
            if (zzb2.zzpn()) {
                if (zzb2.isBound()) {
                    zzb2.zzcn("GmsClientSupervisor");
                }
                this.zzafY.remove(zzb2.zzagf);
            }
        }
        return true;
    }

    public boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        return zza(new zza(componentName), serviceConnection, str);
    }

    public boolean zza(String str, ServiceConnection serviceConnection, String str2) {
        return zza(new zza(str), serviceConnection, str2);
    }

    public void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        zzb(new zza(componentName), serviceConnection, str);
    }

    public void zzb(String str, ServiceConnection serviceConnection, String str2) {
        zzb(new zza(str), serviceConnection, str2);
    }
}
