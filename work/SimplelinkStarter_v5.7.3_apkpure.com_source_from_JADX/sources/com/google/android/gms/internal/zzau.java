package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.AccountChangeEventsRequest;
import com.google.android.gms.auth.AccountChangeEventsResponse;

public interface zzau extends IInterface {

    public static abstract class zza extends Binder implements zzau {

        /* renamed from: com.google.android.gms.internal.zzau$zza$zza reason: collision with other inner class name */
        private static class C1131zza implements zzau {
            private IBinder zznJ;

            C1131zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public Bundle zza(Account account) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle zza(Account account, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle zza(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle zza(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle zza(String str, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public AccountChangeEventsResponse zza(AccountChangeEventsRequest accountChangeEventsRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (accountChangeEventsRequest != null) {
                        obtain.writeInt(1);
                        accountChangeEventsRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (AccountChangeEventsResponse) AccountChangeEventsResponse.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzau zza(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.auth.IAuthManagerService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzau)) ? new C1131zza(iBinder) : (zzau) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v1, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v4, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v5, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v7, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v8, types: [com.google.android.gms.auth.AccountChangeEventsRequest] */
        /* JADX WARNING: type inference failed for: r1v10, types: [com.google.android.gms.auth.AccountChangeEventsRequest] */
        /* JADX WARNING: type inference failed for: r1v11, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v14, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v15, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v17, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r1v18, types: [android.accounts.Account] */
        /* JADX WARNING: type inference failed for: r1v20, types: [android.accounts.Account] */
        /* JADX WARNING: type inference failed for: r1v21 */
        /* JADX WARNING: type inference failed for: r1v22 */
        /* JADX WARNING: type inference failed for: r1v23 */
        /* JADX WARNING: type inference failed for: r1v24 */
        /* JADX WARNING: type inference failed for: r1v25 */
        /* JADX WARNING: type inference failed for: r1v26 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.os.Bundle, com.google.android.gms.auth.AccountChangeEventsRequest, android.accounts.Account]
          uses: [android.os.Bundle, com.google.android.gms.auth.AccountChangeEventsRequest, android.accounts.Account]
          mth insns count: 113
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
        /* JADX WARNING: Unknown variable types count: 7 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r6, android.os.Parcel r7, android.os.Parcel r8, int r9) throws android.os.RemoteException {
            /*
                r5 = this;
                r0 = 0
                r1 = 0
                java.lang.String r2 = "com.google.android.auth.IAuthManagerService"
                r3 = 1
                if (r6 == r3) goto L_0x00fa
                r4 = 2
                if (r6 == r4) goto L_0x00d0
                r4 = 3
                if (r6 == r4) goto L_0x00aa
                r4 = 5
                if (r6 == r4) goto L_0x0070
                r4 = 6
                if (r6 == r4) goto L_0x004a
                r4 = 7
                if (r6 == r4) goto L_0x0024
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r6 == r0) goto L_0x0020
                boolean r6 = super.onTransact(r6, r7, r8, r9)
                return r6
            L_0x0020:
                r8.writeString(r2)
                return r3
            L_0x0024:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x0036
                android.os.Parcelable$Creator r6 = android.accounts.Account.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                android.accounts.Account r1 = (android.accounts.Account) r1
            L_0x0036:
                android.os.Bundle r6 = r5.zza(r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x0046
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x0049
            L_0x0046:
                r8.writeInt(r0)
            L_0x0049:
                return r3
            L_0x004a:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x005c
                android.os.Parcelable$Creator r6 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                android.os.Bundle r1 = (android.os.Bundle) r1
            L_0x005c:
                android.os.Bundle r6 = r5.zza(r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x006c
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x006f
            L_0x006c:
                r8.writeInt(r0)
            L_0x006f:
                return r3
            L_0x0070:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x0082
                android.os.Parcelable$Creator r6 = android.accounts.Account.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                android.accounts.Account r6 = (android.accounts.Account) r6
                goto L_0x0083
            L_0x0082:
                r6 = r1
            L_0x0083:
                java.lang.String r9 = r7.readString()
                int r2 = r7.readInt()
                if (r2 == 0) goto L_0x0096
                android.os.Parcelable$Creator r1 = android.os.Bundle.CREATOR
                java.lang.Object r7 = r1.createFromParcel(r7)
                r1 = r7
                android.os.Bundle r1 = (android.os.Bundle) r1
            L_0x0096:
                android.os.Bundle r6 = r5.zza(r6, r9, r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x00a6
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x00a9
            L_0x00a6:
                r8.writeInt(r0)
            L_0x00a9:
                return r3
            L_0x00aa:
                r7.enforceInterface(r2)
                int r6 = r7.readInt()
                if (r6 == 0) goto L_0x00bc
                android.os.Parcelable$Creator<com.google.android.gms.auth.AccountChangeEventsRequest> r6 = com.google.android.gms.auth.AccountChangeEventsRequest.CREATOR
                java.lang.Object r6 = r6.createFromParcel(r7)
                r1 = r6
                com.google.android.gms.auth.AccountChangeEventsRequest r1 = (com.google.android.gms.auth.AccountChangeEventsRequest) r1
            L_0x00bc:
                com.google.android.gms.auth.AccountChangeEventsResponse r6 = r5.zza(r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x00cc
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x00cf
            L_0x00cc:
                r8.writeInt(r0)
            L_0x00cf:
                return r3
            L_0x00d0:
                r7.enforceInterface(r2)
                java.lang.String r6 = r7.readString()
                int r9 = r7.readInt()
                if (r9 == 0) goto L_0x00e6
                android.os.Parcelable$Creator r9 = android.os.Bundle.CREATOR
                java.lang.Object r7 = r9.createFromParcel(r7)
                r1 = r7
                android.os.Bundle r1 = (android.os.Bundle) r1
            L_0x00e6:
                android.os.Bundle r6 = r5.zza(r6, r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x00f6
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x00f9
            L_0x00f6:
                r8.writeInt(r0)
            L_0x00f9:
                return r3
            L_0x00fa:
                r7.enforceInterface(r2)
                java.lang.String r6 = r7.readString()
                java.lang.String r9 = r7.readString()
                int r2 = r7.readInt()
                if (r2 == 0) goto L_0x0114
                android.os.Parcelable$Creator r1 = android.os.Bundle.CREATOR
                java.lang.Object r7 = r1.createFromParcel(r7)
                r1 = r7
                android.os.Bundle r1 = (android.os.Bundle) r1
            L_0x0114:
                android.os.Bundle r6 = r5.zza(r6, r9, r1)
                r8.writeNoException()
                if (r6 == 0) goto L_0x0124
                r8.writeInt(r3)
                r6.writeToParcel(r8, r3)
                goto L_0x0127
            L_0x0124:
                r8.writeInt(r0)
            L_0x0127:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzau.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    Bundle zza(Account account) throws RemoteException;

    Bundle zza(Account account, String str, Bundle bundle) throws RemoteException;

    Bundle zza(Bundle bundle) throws RemoteException;

    Bundle zza(String str, Bundle bundle) throws RemoteException;

    Bundle zza(String str, String str2, Bundle bundle) throws RemoteException;

    AccountChangeEventsResponse zza(AccountChangeEventsRequest accountChangeEventsRequest) throws RemoteException;
}
