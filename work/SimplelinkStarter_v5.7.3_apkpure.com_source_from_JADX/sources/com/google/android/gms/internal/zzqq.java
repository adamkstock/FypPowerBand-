package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.search.GoogleNowAuthState;

public interface zzqq extends IInterface {

    public static abstract class zza extends Binder implements zzqq {

        /* renamed from: com.google.android.gms.internal.zzqq$zza$zza reason: collision with other inner class name */
        private static class C1142zza implements zzqq {
            private IBinder zznJ;

            C1142zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(Status status, GoogleNowAuthState googleNowAuthState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.search.internal.ISearchAuthCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (googleNowAuthState != null) {
                        obtain.writeInt(1);
                        googleNowAuthState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzbb(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.search.internal.ISearchAuthCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.search.internal.ISearchAuthCallbacks");
        }

        public static zzqq zzdJ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.search.internal.ISearchAuthCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzqq)) ? new C1142zza(iBinder) : (zzqq) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r0v0 */
        /* JADX WARNING: type inference failed for: r0v1, types: [com.google.android.gms.search.GoogleNowAuthState] */
        /* JADX WARNING: type inference failed for: r0v3, types: [com.google.android.gms.search.GoogleNowAuthState] */
        /* JADX WARNING: type inference failed for: r0v4, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v6, types: [com.google.android.gms.common.api.Status] */
        /* JADX WARNING: type inference failed for: r0v7 */
        /* JADX WARNING: type inference failed for: r0v8 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.common.api.Status, com.google.android.gms.search.GoogleNowAuthState]
          uses: [com.google.android.gms.search.GoogleNowAuthState, com.google.android.gms.common.api.Status]
          mth insns count: 35
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
        /* JADX WARNING: Unknown variable types count: 3 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 0
                java.lang.String r1 = "com.google.android.gms.search.internal.ISearchAuthCallbacks"
                r2 = 1
                if (r5 == r2) goto L_0x002d
                r3 = 2
                if (r5 == r3) goto L_0x0017
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                if (r5 == r0) goto L_0x0013
                boolean r5 = super.onTransact(r5, r6, r7, r8)
                return r5
            L_0x0013:
                r7.writeString(r1)
                return r2
            L_0x0017:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x0029
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                r0 = r5
                com.google.android.gms.common.api.Status r0 = (com.google.android.gms.common.api.Status) r0
            L_0x0029:
                r4.zzbb(r0)
                return r2
            L_0x002d:
                r6.enforceInterface(r1)
                int r5 = r6.readInt()
                if (r5 == 0) goto L_0x003f
                android.os.Parcelable$Creator<com.google.android.gms.common.api.Status> r5 = com.google.android.gms.common.api.Status.CREATOR
                java.lang.Object r5 = r5.createFromParcel(r6)
                com.google.android.gms.common.api.Status r5 = (com.google.android.gms.common.api.Status) r5
                goto L_0x0040
            L_0x003f:
                r5 = r0
            L_0x0040:
                int r7 = r6.readInt()
                if (r7 == 0) goto L_0x004f
                android.os.Parcelable$Creator<com.google.android.gms.search.GoogleNowAuthState> r7 = com.google.android.gms.search.GoogleNowAuthState.CREATOR
                java.lang.Object r6 = r7.createFromParcel(r6)
                r0 = r6
                com.google.android.gms.search.GoogleNowAuthState r0 = (com.google.android.gms.search.GoogleNowAuthState) r0
            L_0x004f:
                r4.zza(r5, r0)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzqq.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void zza(Status status, GoogleNowAuthState googleNowAuthState) throws RemoteException;

    void zzbb(Status status) throws RemoteException;
}
