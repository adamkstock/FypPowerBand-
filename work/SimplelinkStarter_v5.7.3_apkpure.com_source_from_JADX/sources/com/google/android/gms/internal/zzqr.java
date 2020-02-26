package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface zzqr extends IInterface {

    public static abstract class zza extends Binder implements zzqr {

        /* renamed from: com.google.android.gms.internal.zzqr$zza$zza reason: collision with other inner class name */
        private static class C1143zza implements zzqr {
            private IBinder zznJ;

            C1143zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(zzqq zzqq, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.search.internal.ISearchAuthService");
                    obtain.writeStrongBinder(zzqq != null ? zzqq.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzqq zzqq, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.search.internal.ISearchAuthService");
                    obtain.writeStrongBinder(zzqq != null ? zzqq.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzqr zzdK(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.search.internal.ISearchAuthService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzqr)) ? new C1143zza(iBinder) : (zzqr) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = "com.google.android.gms.search.internal.ISearchAuthService";
            if (i == 1) {
                parcel.enforceInterface(str);
                zza(com.google.android.gms.internal.zzqq.zza.zzdJ(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
            } else if (i == 2) {
                parcel.enforceInterface(str);
                zzb(com.google.android.gms.internal.zzqq.zza.zzdJ(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
            parcel2.writeNoException();
            return true;
        }
    }

    void zza(zzqq zzqq, String str, String str2) throws RemoteException;

    void zzb(zzqq zzqq, String str, String str2) throws RemoteException;
}
