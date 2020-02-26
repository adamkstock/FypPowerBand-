package com.google.android.gms.auth.api.signin.internal;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {

        /* renamed from: com.google.android.gms.auth.api.signin.internal.zzd$zza$zza reason: collision with other inner class name */
        private static class C1119zza implements zzd {
            private IBinder zznJ;

            C1119zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(GoogleSignInAccount googleSignInAccount, Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (googleSignInAccount != null) {
                        obtain.writeInt(1);
                        googleSignInAccount.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(Status status, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzk(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzl(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzm(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzd zzay(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzd)) ? new C1119zza(iBinder) : (zzd) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v1, types: [android.content.Intent] */
        /* JADX WARNING: type inference failed for: r1v3, types: [android.content.Intent] */
        /* JADX WARNING: type inference failed for: r1v4, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v6, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v7, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v9, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v10, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v12, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v13, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v15, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r1v16 */
        /* JADX WARNING: type inference failed for: r1v17 */
        /* JADX WARNING: type inference failed for: r1v18 */
        /* JADX WARNING: type inference failed for: r1v19 */
        /* JADX WARNING: type inference failed for: r1v20 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.common.api.Status, android.content.Intent]
          uses: [android.content.Intent, com.google.android.gms.common.api.Status]
          mth insns count: 69
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
        /* JADX WARNING: Unknown variable types count: 6 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                java.lang.String r0 = "com.google.android.gms.auth.api.signin.internal.ISignInCallbacks"
                r1 = 0
                r2 = 1
                if (r5 == r2) goto L_0x0085
                r3 = 2
                if (r5 == r3) goto L_0x006f
                r3 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r3) goto L_0x006b
                switch(r5) {
                    case 101: goto L_0x0045;
                    case 102: goto L_0x002f;
                    case 103: goto L_0x0016;
                    default: goto L_0x0011;
                }
            L_0x0011:
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x0016:
                r6.enforceInterface(r0)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0028
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r1 = r5
                com.google.android.gms.common.api.Status r1 = (com.google.android.gms.common.api.Status) r1
            L_0x0028:
                r4.zzm(r1)
            L_0x002b:
                r7.writeNoException()
                return r2
            L_0x002f:
                r6.enforceInterface(r0)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0041
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r1 = r5
                com.google.android.gms.common.api.Status r1 = (com.google.android.gms.common.api.Status) r1
            L_0x0041:
                r4.zzl(r1)
                goto L_0x002b
            L_0x0045:
                r6.enforceInterface(r0)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0057
                android.os.Parcelable$Creator<com.google.android.gms.auth.api.signin.GoogleSignInAccount> r5 = com.google.android.gms.auth.api.signin.GoogleSignInAccount.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.auth.api.signin.GoogleSignInAccount r5 = (com.google.android.gms.auth.api.signin.GoogleSignInAccount) r5
                goto L_0x0058
            L_0x0057:
                r5 = r1
            L_0x0058:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0067
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r8 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r1 = r6
                com.google.android.gms.common.api.Status r1 = (com.google.android.gms.common.api.Status) r1
            L_0x0067:
                r4.zza(r5, r1)
                goto L_0x002b
            L_0x006b:
                r7.writeString(r0)
                return r2
            L_0x006f:
                r6.enforceInterface(r0)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0081
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r1 = r5
                com.google.android.gms.common.api.Status r1 = (com.google.android.gms.common.api.Status) r1
            L_0x0081:
                r4.zzk(r1)
                goto L_0x002b
            L_0x0085:
                r6.enforceInterface(r0)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0097
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.common.api.Status r5 = (com.google.android.gms.common.api.Status) r5
                goto L_0x0098
            L_0x0097:
                r5 = r1
            L_0x0098:
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x00a7
                android.os.Parcelable$Creator r8 = android.content.Intent.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r1 = r6
                android.content.Intent r1 = (android.content.Intent) r1
            L_0x00a7:
                r4.zza(r5, r1)
                goto L_0x002b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.zzd.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(GoogleSignInAccount googleSignInAccount, Status status) throws RemoteException;

    void zza(Status status, Intent intent) throws RemoteException;

    void zzk(Status status) throws RemoteException;

    void zzl(Status status) throws RemoteException;

    void zzm(Status status) throws RemoteException;
}
