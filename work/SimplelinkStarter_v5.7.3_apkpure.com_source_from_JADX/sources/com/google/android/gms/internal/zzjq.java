package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.appdatasearch.GetRecentContextCall.Response;
import com.google.android.gms.common.api.Status;

public interface zzjq extends IInterface {

    public static abstract class zza extends Binder implements zzjq {

        /* renamed from: com.google.android.gms.internal.zzjq$zza$zza reason: collision with other inner class name */
        private static class C1133zza implements zzjq {
            private IBinder zznJ;

            C1133zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(Response response) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    if (response != null) {
                        obtain.writeInt(1);
                        response.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(Status status, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (parcelFileDescriptor != null) {
                        obtain.writeInt(1);
                        parcelFileDescriptor.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(Status status, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    int i = 0;
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zznJ.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzc(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
        }

        public static zzjq zzah(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzjq)) ? new C1133zza(iBinder) : (zzjq) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r0v0 */
        /* JADX WARNING: type inference failed for: r0v1, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v3, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v4, types: [android.os.ParcelFileDescriptor] */
        /* JADX WARNING: type inference failed for: r0v6, types: [android.os.ParcelFileDescriptor] */
        /* JADX WARNING: type inference failed for: r0v7, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v9, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v10, types: [com.google.android.gms.appdatasearch.GetRecentContextCall$Response] */
        /* JADX WARNING: type inference failed for: r0v11, types: [com.google.android.gms.appdatasearch.GetRecentContextCall$Response] */
        /* JADX WARNING: type inference failed for: r0v12 */
        /* JADX WARNING: type inference failed for: r0v13 */
        /* JADX WARNING: type inference failed for: r0v14 */
        /* JADX WARNING: type inference failed for: r0v15 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.os.ParcelFileDescriptor, com.google.android.gms.common.api.Status, com.google.android.gms.appdatasearch.GetRecentContextCall$Response]
          uses: [com.google.android.gms.common.api.Status, android.os.ParcelFileDescriptor, com.google.android.gms.appdatasearch.GetRecentContextCall$Response]
          mth insns count: 59
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
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 5 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 0
                java.lang.String r1 = "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearchCallbacks"
                r2 = 1
                if (r5 == r2) goto L_0x0075
                r3 = 2
                if (r5 == r3) goto L_0x004f
                r3 = 3
                if (r5 == r3) goto L_0x0030
                r3 = 4
                if (r5 == r3) goto L_0x001d
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0019
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x0019:
                r7.writeString(r1)
                return r2
            L_0x001d:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x002c
                com.google.android.gms.appdatasearch.zzg r5 = com.google.android.gms.appdatasearch.GetRecentContextCall.Response.CREATOR
                com.google.android.gms.appdatasearch.GetRecentContextCall$Response r0 = r5.createFromParcel(r6)
            L_0x002c:
                r4.zza(r0)
                return r2
            L_0x0030:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0042
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                com.google.android.gms.common.api.Status r0 = (com.google.android.gms.common.api.Status) r0
            L_0x0042:
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x004a
                r5 = 1
                goto L_0x004b
            L_0x004a:
                r5 = 0
            L_0x004b:
                r4.zza(r0, r5)
                return r2
            L_0x004f:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0061
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.common.api.Status r5 = (com.google.android.gms.common.api.Status) r5
                goto L_0x0062
            L_0x0061:
                r5 = r0
            L_0x0062:
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x0071
                android.os.Parcelable$Creator r7 = android.os.ParcelFileDescriptor.CREATOR
                java.lang.Object r6 = r7.createFromParcel(r6)
                r0 = r6
                android.os.ParcelFileDescriptor r0 = (android.os.ParcelFileDescriptor) r0
            L_0x0071:
                r4.zza(r5, r0)
                return r2
            L_0x0075:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0087
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                com.google.android.gms.common.api.Status r0 = (com.google.android.gms.common.api.Status) r0
            L_0x0087:
                r4.zzc(r0)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzjq.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(Response response) throws RemoteException;

    void zza(Status status, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void zza(Status status, boolean z) throws RemoteException;

    void zzc(Status status) throws RemoteException;
}
