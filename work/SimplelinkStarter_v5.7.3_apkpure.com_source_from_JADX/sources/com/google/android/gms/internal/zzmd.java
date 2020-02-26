package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface zzmd extends IInterface {

    public static abstract class zza extends Binder implements zzmd {

        /* renamed from: com.google.android.gms.internal.zzmd$zza$zza reason: collision with other inner class name */
        private static class C1140zza implements zzmd {
            private IBinder zznJ;

            C1140zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(zzmc zzmc) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.service.ICommonService");
                    obtain.writeStrongBinder(zzmc != null ? zzmc.asBinder() : null);
                    this.zznJ.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zzmd zzaQ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzmd)) ? new C1140zza(iBinder) : (zzmd) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = "com.google.android.gms.common.internal.service.ICommonService";
            if (i == 1) {
                parcel.enforceInterface(str);
                zza(com.google.android.gms.internal.zzmc.zza.zzaP(parcel.readStrongBinder()));
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void zza(zzmc zzmc) throws RemoteException;
}
