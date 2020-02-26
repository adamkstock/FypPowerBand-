package com.google.android.gms.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;

public interface zze extends IInterface {

    public static abstract class zza extends Binder implements zze {

        /* renamed from: com.google.android.gms.signin.internal.zze$zza$zza reason: collision with other inner class name */
        private static class C1146zza implements zze {
            private IBinder zznJ;

            C1146zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(ConnectionResult connectionResult, AuthAccountResult authAccountResult) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (connectionResult != null) {
                        obtain.writeInt(1);
                        connectionResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (authAccountResult != null) {
                        obtain.writeInt(1);
                        authAccountResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(Status status, GoogleSignInAccount googleSignInAccount) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (googleSignInAccount != null) {
                        obtain.writeInt(1);
                        googleSignInAccount.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzbd(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzbe(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.signin.internal.ISignInCallbacks");
        }

        public static zze zzdM(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zze)) ? new C1146zza(iBinder) : (zze) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r3v0 */
        /* JADX WARNING: type inference failed for: r3v1, types: [com.google.android.gms.signin.internal.AuthAccountResult] */
        /* JADX WARNING: type inference failed for: r3v3, types: [com.google.android.gms.signin.internal.AuthAccountResult] */
        /* JADX WARNING: type inference failed for: r3v4, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r3v6, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r3v7, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r3v9, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r3v10, types: [com.google.android.gms.auth.api.signin.GoogleSignInAccount] */
        /* JADX WARNING: type inference failed for: r3v12, types: [com.google.android.gms.auth.api.signin.GoogleSignInAccount] */
        /* JADX WARNING: type inference failed for: r3v13 */
        /* JADX WARNING: type inference failed for: r3v14 */
        /* JADX WARNING: type inference failed for: r3v15 */
        /* JADX WARNING: type inference failed for: r3v16 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.common.api.Status, com.google.android.gms.signin.internal.AuthAccountResult, com.google.android.gms.auth.api.signin.GoogleSignInAccount]
          uses: [com.google.android.gms.signin.internal.AuthAccountResult, com.google.android.gms.common.api.Status, com.google.android.gms.auth.api.signin.GoogleSignInAccount]
          mth insns count: 61
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
                r0 = 3
                r1 = 1
                java.lang.String r2 = "com.google.android.gms.signin.internal.ISignInCallbacks"
                r3 = 0
                if (r5 == r0) goto L_0x0073
                r0 = 4
                if (r5 == r0) goto L_0x005d
                r0 = 6
                if (r5 == r0) goto L_0x0047
                r0 = 7
                if (r5 == r0) goto L_0x001e
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x001a
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x001a:
                r7.writeString(r2)
                return r1
            L_0x001e:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0030
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.common.api.Status r5 = (com.google.android.gms.common.api.Status) r5
                goto L_0x0031
            L_0x0030:
                r5 = r3
            L_0x0031:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0040
                android.os.Parcelable$Creator<com.google.android.gms.auth.api.signin.GoogleSignInAccount> r8 = com.google.android.gms.auth.api.signin.GoogleSignInAccount.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                com.google.android.gms.auth.api.signin.GoogleSignInAccount r3 = (com.google.android.gms.auth.api.signin.GoogleSignInAccount) r3
            L_0x0040:
                r4.zza(r5, r3)
            L_0x0043:
                r7.writeNoException()
                return r1
            L_0x0047:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0059
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r3 = r5
                com.google.android.gms.common.api.Status r3 = (com.google.android.gms.common.api.Status) r3
            L_0x0059:
                r4.zzbe(r3)
                goto L_0x0043
            L_0x005d:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x006f
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r3 = r5
                com.google.android.gms.common.api.Status r3 = (com.google.android.gms.common.api.Status) r3
            L_0x006f:
                r4.zzbd(r3)
                goto L_0x0043
            L_0x0073:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0085
                android.os.Parcelable$Creator<com.google.android.gms.common.ConnectionResult> r5 = com.google.android.gms.common.ConnectionResult.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.common.ConnectionResult r5 = (com.google.android.gms.common.ConnectionResult) r5
                goto L_0x0086
            L_0x0085:
                r5 = r3
            L_0x0086:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0095
                android.os.Parcelable$Creator<com.google.android.gms.signin.internal.AuthAccountResult> r8 = com.google.android.gms.signin.internal.AuthAccountResult.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                com.google.android.gms.signin.internal.AuthAccountResult r3 = (com.google.android.gms.signin.internal.AuthAccountResult) r3
            L_0x0095:
                r4.zza(r5, r3)
                goto L_0x0043
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.signin.internal.zze.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(ConnectionResult connectionResult, AuthAccountResult authAccountResult) throws RemoteException;

    void zza(Status status, GoogleSignInAccount googleSignInAccount) throws RemoteException;

    void zzbd(Status status) throws RemoteException;

    void zzbe(Status status) throws RemoteException;
}
