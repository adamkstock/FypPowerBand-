package com.google.android.gms.playlog.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface zza extends IInterface {

    /* renamed from: com.google.android.gms.playlog.internal.zza$zza reason: collision with other inner class name */
    public static abstract class C1144zza extends Binder implements zza {

        /* renamed from: com.google.android.gms.playlog.internal.zza$zza$zza reason: collision with other inner class name */
        private static class C1145zza implements zza {
            private IBinder zznJ;

            C1145zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (logEvent != null) {
                        obtain.writeInt(1);
                        logEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList(list);
                    this.zznJ.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    this.zznJ.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zza zzdz(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.playlog.internal.IPlayLogService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zza)) ? new C1145zza(iBinder) : (zza) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v1, types: [com.google.android.gms.playlog.internal.LogEvent] */
        /* JADX WARNING: type inference failed for: r1v2, types: [com.google.android.gms.playlog.internal.LogEvent] */
        /* JADX WARNING: type inference failed for: r1v3, types: [com.google.android.gms.playlog.internal.PlayLoggerContext] */
        /* JADX WARNING: type inference failed for: r1v4, types: [com.google.android.gms.playlog.internal.PlayLoggerContext] */
        /* JADX WARNING: type inference failed for: r1v5, types: [com.google.android.gms.playlog.internal.PlayLoggerContext] */
        /* JADX WARNING: type inference failed for: r1v6, types: [com.google.android.gms.playlog.internal.PlayLoggerContext] */
        /* JADX WARNING: type inference failed for: r1v7 */
        /* JADX WARNING: type inference failed for: r1v8 */
        /* JADX WARNING: type inference failed for: r1v9 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.playlog.internal.PlayLoggerContext, com.google.android.gms.playlog.internal.LogEvent]
          uses: [com.google.android.gms.playlog.internal.LogEvent, com.google.android.gms.playlog.internal.PlayLoggerContext]
          mth insns count: 45
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 2
                r1 = 0
                r2 = 1
                java.lang.String r3 = "com.google.android.gms.playlog.internal.IPlayLogService"
                if (r5 == r0) goto L_0x0053
                r0 = 3
                if (r5 == r0) goto L_0x0036
                r0 = 4
                if (r5 == r0) goto L_0x001b
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0017
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x0017:
                r7.writeString(r3)
                return r2
            L_0x001b:
                r6.enforceInterface(r3)
                java.lang.String r5 = r6.readString()
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x002e
                com.google.android.gms.playlog.internal.zze r7 = com.google.android.gms.playlog.internal.PlayLoggerContext.CREATOR
                com.google.android.gms.playlog.internal.PlayLoggerContext r1 = r7.createFromParcel(r6)
            L_0x002e:
                byte[] r6 = r6.createByteArray()
                r4.zza(r5, r1, r6)
                return r2
            L_0x0036:
                r6.enforceInterface(r3)
                java.lang.String r5 = r6.readString()
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0049
                com.google.android.gms.playlog.internal.zze r7 = com.google.android.gms.playlog.internal.PlayLoggerContext.CREATOR
                com.google.android.gms.playlog.internal.PlayLoggerContext r1 = r7.createFromParcel(r6)
            L_0x0049:
                com.google.android.gms.playlog.internal.zzc r7 = com.google.android.gms.playlog.internal.LogEvent.CREATOR
                java.util.ArrayList r6 = r6.createTypedArrayList(r7)
                r4.zza(r5, r1, r6)
                return r2
            L_0x0053:
                r6.enforceInterface(r3)
                java.lang.String r5 = r6.readString()
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0067
                com.google.android.gms.playlog.internal.zze r7 = com.google.android.gms.playlog.internal.PlayLoggerContext.CREATOR
                com.google.android.gms.playlog.internal.PlayLoggerContext r7 = r7.createFromParcel(r6)
                goto L_0x0068
            L_0x0067:
                r7 = r1
            L_0x0068:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0074
                com.google.android.gms.playlog.internal.zzc r8 = com.google.android.gms.playlog.internal.LogEvent.CREATOR
                com.google.android.gms.playlog.internal.LogEvent r1 = r8.createFromParcel(r6)
            L_0x0074:
                r4.zza(r5, r7, r1)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.playlog.internal.zza.C1144zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException;
}
