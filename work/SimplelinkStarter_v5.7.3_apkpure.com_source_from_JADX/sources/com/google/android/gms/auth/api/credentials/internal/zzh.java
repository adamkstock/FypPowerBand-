package com.google.android.gms.auth.api.credentials.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.CredentialRequest;

public interface zzh extends IInterface {

    public static abstract class zza extends Binder implements zzh {

        /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzh$zza$zza reason: collision with other inner class name */
        private static class C1118zza implements zzh {
            private IBinder zznJ;

            C1118zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(zzg zzg) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(zzg != null ? zzg.asBinder() : null);
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzg zzg, CredentialRequest credentialRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(zzg != null ? zzg.asBinder() : null);
                    if (credentialRequest != null) {
                        obtain.writeInt(1);
                        credentialRequest.writeToParcel(obtain, 0);
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

            public void zza(zzg zzg, DeleteRequest deleteRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(zzg != null ? zzg.asBinder() : null);
                    if (deleteRequest != null) {
                        obtain.writeInt(1);
                        deleteRequest.writeToParcel(obtain, 0);
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

            public void zza(zzg zzg, SaveRequest saveRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    obtain.writeStrongBinder(zzg != null ? zzg.asBinder() : null);
                    if (saveRequest != null) {
                        obtain.writeInt(1);
                        saveRequest.writeToParcel(obtain, 0);
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
        }

        public static zzh zzat(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzh)) ? new C1118zza(iBinder) : (zzh) queryLocalInterface;
        }

        /* JADX WARNING: type inference failed for: r0v0 */
        /* JADX WARNING: type inference failed for: r0v1, types: [com.google.android.gms.auth.api.credentials.CredentialRequest] */
        /* JADX WARNING: type inference failed for: r0v3, types: [com.google.android.gms.auth.api.credentials.CredentialRequest] */
        /* JADX WARNING: type inference failed for: r0v4, types: [com.google.android.gms.auth.api.credentials.internal.SaveRequest] */
        /* JADX WARNING: type inference failed for: r0v6, types: [com.google.android.gms.auth.api.credentials.internal.SaveRequest] */
        /* JADX WARNING: type inference failed for: r0v7, types: [com.google.android.gms.auth.api.credentials.internal.DeleteRequest] */
        /* JADX WARNING: type inference failed for: r0v9, types: [com.google.android.gms.auth.api.credentials.internal.DeleteRequest] */
        /* JADX WARNING: type inference failed for: r0v10 */
        /* JADX WARNING: type inference failed for: r0v11 */
        /* JADX WARNING: type inference failed for: r0v12 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.auth.api.credentials.internal.SaveRequest, com.google.android.gms.auth.api.credentials.CredentialRequest, com.google.android.gms.auth.api.credentials.internal.DeleteRequest]
          uses: [com.google.android.gms.auth.api.credentials.CredentialRequest, com.google.android.gms.auth.api.credentials.internal.SaveRequest, com.google.android.gms.auth.api.credentials.internal.DeleteRequest]
          mth insns count: 50
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
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 0
                java.lang.String r1 = "com.google.android.gms.auth.api.credentials.internal.ICredentialsService"
                r2 = 1
                if (r5 == r2) goto L_0x006b
                r3 = 2
                if (r5 == r3) goto L_0x004d
                r3 = 3
                if (r5 == r3) goto L_0x002f
                r0 = 4
                if (r5 == r0) goto L_0x001d
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0019
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x0019:
                r7.writeString(r1)
                return r2
            L_0x001d:
                r6.enforceInterface(r1)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.google.android.gms.auth.api.credentials.internal.zzg r5 = com.google.android.gms.auth.api.credentials.internal.zzg.zza.zzas(r5)
                r4.zza(r5)
            L_0x002b:
                r7.writeNoException()
                return r2
            L_0x002f:
                r6.enforceInterface(r1)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.google.android.gms.auth.api.credentials.internal.zzg r5 = com.google.android.gms.auth.api.credentials.internal.zzg.zza.zzas(r5)
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0049
                android.os.Parcelable$Creator<com.google.android.gms.auth.api.credentials.internal.DeleteRequest> r8 = com.google.android.gms.auth.api.credentials.internal.DeleteRequest.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r0 = r6
                com.google.android.gms.auth.api.credentials.internal.DeleteRequest r0 = (com.google.android.gms.auth.api.credentials.internal.DeleteRequest) r0
            L_0x0049:
                r4.zza(r5, r0)
                goto L_0x002b
            L_0x004d:
                r6.enforceInterface(r1)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.google.android.gms.auth.api.credentials.internal.zzg r5 = com.google.android.gms.auth.api.credentials.internal.zzg.zza.zzas(r5)
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0067
                android.os.Parcelable$Creator<com.google.android.gms.auth.api.credentials.internal.SaveRequest> r8 = com.google.android.gms.auth.api.credentials.internal.SaveRequest.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r0 = r6
                com.google.android.gms.auth.api.credentials.internal.SaveRequest r0 = (com.google.android.gms.auth.api.credentials.internal.SaveRequest) r0
            L_0x0067:
                r4.zza(r5, r0)
                goto L_0x002b
            L_0x006b:
                r6.enforceInterface(r1)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.google.android.gms.auth.api.credentials.internal.zzg r5 = com.google.android.gms.auth.api.credentials.internal.zzg.zza.zzas(r5)
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0085
                android.os.Parcelable$Creator<com.google.android.gms.auth.api.credentials.CredentialRequest> r8 = com.google.android.gms.auth.api.credentials.CredentialRequest.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r0 = r6
                com.google.android.gms.auth.api.credentials.CredentialRequest r0 = (com.google.android.gms.auth.api.credentials.CredentialRequest) r0
            L_0x0085:
                r4.zza(r5, r0)
                goto L_0x002b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.credentials.internal.zzh.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(zzg zzg) throws RemoteException;

    void zza(zzg zzg, CredentialRequest credentialRequest) throws RemoteException;

    void zza(zzg zzg, DeleteRequest deleteRequest) throws RemoteException;

    void zza(zzg zzg, SaveRequest saveRequest) throws RemoteException;
}
