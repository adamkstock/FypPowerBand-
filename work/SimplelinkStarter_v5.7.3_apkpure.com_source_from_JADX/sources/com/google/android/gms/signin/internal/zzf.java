package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;

public interface zzf extends IInterface {

    public static abstract class zza extends Binder implements zzf {

        /* renamed from: com.google.android.gms.signin.internal.zzf$zza$zza reason: collision with other inner class name */
        private static class C1147zza implements zzf {
            private IBinder zznJ;

            C1147zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(int i, Account account, zze zze) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(i);
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(zze != null ? zze.asBinder() : null);
                    this.zznJ.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(AuthAccountRequest authAccountRequest, zze zze) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (authAccountRequest != null) {
                        obtain.writeInt(1);
                        authAccountRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(zze != null ? zze.asBinder() : null);
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(ResolveAccountRequest resolveAccountRequest, zzt zzt) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (resolveAccountRequest != null) {
                        obtain.writeInt(1);
                        resolveAccountRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(zzt != null ? zzt.asBinder() : null);
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzp zzp, int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeStrongBinder(zzp != null ? zzp.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(CheckServerAuthResult checkServerAuthResult) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (checkServerAuthResult != null) {
                        obtain.writeInt(1);
                        checkServerAuthResult.writeToParcel(obtain, 0);
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

            public void zza(RecordConsentRequest recordConsentRequest, zze zze) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (recordConsentRequest != null) {
                        obtain.writeInt(1);
                        recordConsentRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(zze != null ? zze.asBinder() : null);
                    this.zznJ.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zze zze) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeStrongBinder(zze != null ? zze.asBinder() : null);
                    this.zznJ.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzaq(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzjq(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(i);
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzf zzdN(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzf)) ? new C1147zza(iBinder) : (zzf) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v1, types: [com.google.android.gms.common.internal.AuthAccountRequest] */
        /* JADX WARNING: type inference failed for: r1v3, types: [com.google.android.gms.common.internal.AuthAccountRequest] */
        /* JADX WARNING: type inference failed for: r1v4, types: [com.google.android.gms.signin.internal.CheckServerAuthResult] */
        /* JADX WARNING: type inference failed for: r1v6, types: [com.google.android.gms.signin.internal.CheckServerAuthResult] */
        /* JADX WARNING: type inference failed for: r1v7, types: [com.google.android.gms.common.internal.ResolveAccountRequest] */
        /* JADX WARNING: type inference failed for: r1v9, types: [com.google.android.gms.common.internal.ResolveAccountRequest] */
        /* JADX WARNING: type inference failed for: r1v10, types: [android.accounts.Account] */
        /* JADX WARNING: type inference failed for: r1v12, types: [android.accounts.Account] */
        /* JADX WARNING: type inference failed for: r1v13, types: [com.google.android.gms.signin.internal.RecordConsentRequest] */
        /* JADX WARNING: type inference failed for: r1v15, types: [com.google.android.gms.signin.internal.RecordConsentRequest] */
        /* JADX WARNING: type inference failed for: r1v16 */
        /* JADX WARNING: type inference failed for: r1v17 */
        /* JADX WARNING: type inference failed for: r1v18 */
        /* JADX WARNING: type inference failed for: r1v19 */
        /* JADX WARNING: type inference failed for: r1v20 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.signin.internal.CheckServerAuthResult, com.google.android.gms.common.internal.AuthAccountRequest, com.google.android.gms.common.internal.ResolveAccountRequest, android.accounts.Account, com.google.android.gms.signin.internal.RecordConsentRequest]
          uses: [com.google.android.gms.common.internal.AuthAccountRequest, com.google.android.gms.signin.internal.CheckServerAuthResult, com.google.android.gms.common.internal.ResolveAccountRequest, android.accounts.Account, com.google.android.gms.signin.internal.RecordConsentRequest]
          mth insns count: 89
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
        public boolean onTransact(int r6, android.os.Parcel r7, android.os.Parcel r8, int r9) throws android.os.RemoteException {
            /*
                r5 = this;
                r0 = 2
                r1 = 0
                java.lang.String r2 = "com.google.android.gms.signin.internal.ISignInService"
                r3 = 1
                if (r6 == r0) goto L_0x00de
                r0 = 3
                if (r6 == r0) goto L_0x00c7
                r0 = 4
                r4 = 0
                if (r6 == r0) goto L_0x00b8
                r0 = 5
                if (r6 == r0) goto L_0x0099
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r6 == r0) goto L_0x0095
                switch(r6) {
                    case 7: goto L_0x008a;
                    case 8: goto L_0x0068;
                    case 9: goto L_0x004e;
                    case 10: goto L_0x0030;
                    case 11: goto L_0x001e;
                    default: goto L_0x0019;
                }
            L_0x0019:
                boolean r6 = super.onTransact(r6, r7, r8, r9)
                return r6
            L_0x001e:
                r7.enforceInterface(r2)
                android.os.IBinder r6 = r7.readStrongBinder()
                com.google.android.gms.signin.internal.zze r6 = com.google.android.gms.signin.internal.zze.zza.zzdM(r6)
                r5.zza(r6)
            L_0x002c:
                r8.writeNoException()
                return r3
            L_0x0030:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x0042
                android.os.Parcelable$Creator<com.google.android.gms.signin.internal.RecordConsentRequest> r6 = com.google.android.gms.signin.internal.RecordConsentRequest.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                com.google.android.gms.signin.internal.RecordConsentRequest r1 = (com.google.android.gms.signin.internal.RecordConsentRequest) r1
            L_0x0042:
                android.os.IBinder r6 = r7.readStrongBinder()
                com.google.android.gms.signin.internal.zze r6 = com.google.android.gms.signin.internal.zze.zza.zzdM(r6)
                r5.zza(r1, r6)
                goto L_0x002c
            L_0x004e:
                r7.enforceInterface(r2)
                android.os.IBinder r6 = r7.readStrongBinder()
                com.google.android.gms.common.internal.zzp r6 = com.google.android.gms.common.internal.zzp.zza.zzaH(r6)
                int r9 = r7.readInt()
                int r7 = r7.readInt()
                if (r7 == 0) goto L_0x0064
                r4 = 1
            L_0x0064:
                r5.zza(r6, r9, r4)
                goto L_0x002c
            L_0x0068:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                int r9 = r7.readInt()
                if (r9 == 0) goto L_0x007e
                android.os.Parcelable$Creator r9 = android.accounts.Account.CREATOR
                java.lang.Object r9 = r9.createFromParcel(r7)
                r1 = r9
                android.accounts.Account r1 = (android.accounts.Account) r1
            L_0x007e:
                android.os.IBinder r7 = r7.readStrongBinder()
                com.google.android.gms.signin.internal.zze r7 = com.google.android.gms.signin.internal.zze.zza.zzdM(r7)
                r5.zza(r6, r1, r7)
                goto L_0x002c
            L_0x008a:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                r5.zzjq(r6)
                goto L_0x002c
            L_0x0095:
                r8.writeString(r2)
                return r3
            L_0x0099:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x00ab
                android.os.Parcelable$Creator<com.google.android.gms.common.internal.ResolveAccountRequest> r6 = com.google.android.gms.common.internal.ResolveAccountRequest.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                com.google.android.gms.common.internal.ResolveAccountRequest r1 = (com.google.android.gms.common.internal.ResolveAccountRequest) r1
            L_0x00ab:
                android.os.IBinder r6 = r7.readStrongBinder()
                com.google.android.gms.common.internal.zzt r6 = com.google.android.gms.common.internal.zzt.zza.zzaL(r6)
                r5.zza(r1, r6)
                goto L_0x002c
            L_0x00b8:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x00c2
                r4 = 1
            L_0x00c2:
                r5.zzaq(r4)
                goto L_0x002c
            L_0x00c7:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x00d9
                android.os.Parcelable$Creator<com.google.android.gms.signin.internal.CheckServerAuthResult> r6 = com.google.android.gms.signin.internal.CheckServerAuthResult.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                com.google.android.gms.signin.internal.CheckServerAuthResult r1 = (com.google.android.gms.signin.internal.CheckServerAuthResult) r1
            L_0x00d9:
                r5.zza(r1)
                goto L_0x002c
            L_0x00de:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x00f0
                android.os.Parcelable$Creator<com.google.android.gms.common.internal.AuthAccountRequest> r6 = com.google.android.gms.common.internal.AuthAccountRequest.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                com.google.android.gms.common.internal.AuthAccountRequest r1 = (com.google.android.gms.common.internal.AuthAccountRequest) r1
            L_0x00f0:
                android.os.IBinder r6 = r7.readStrongBinder()
                com.google.android.gms.signin.internal.zze r6 = com.google.android.gms.signin.internal.zze.zza.zzdM(r6)
                r5.zza(r1, r6)
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.signin.internal.zzf.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(int i, Account account, zze zze) throws RemoteException;

    void zza(AuthAccountRequest authAccountRequest, zze zze) throws RemoteException;

    void zza(ResolveAccountRequest resolveAccountRequest, zzt zzt) throws RemoteException;

    void zza(zzp zzp, int i, boolean z) throws RemoteException;

    void zza(CheckServerAuthResult checkServerAuthResult) throws RemoteException;

    void zza(RecordConsentRequest recordConsentRequest, zze zze) throws RemoteException;

    void zza(zze zze) throws RemoteException;

    void zzaq(boolean z) throws RemoteException;

    void zzjq(int i) throws RemoteException;
}
