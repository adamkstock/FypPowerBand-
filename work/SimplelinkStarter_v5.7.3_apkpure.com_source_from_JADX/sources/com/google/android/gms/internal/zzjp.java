package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.appdatasearch.GetRecentContextCall.Request;
import com.google.android.gms.appdatasearch.UsageInfo;

public interface zzjp extends IInterface {

    public static abstract class zza extends Binder implements zzjp {

        /* renamed from: com.google.android.gms.internal.zzjp$zza$zza reason: collision with other inner class name */
        private static class C1132zza implements zzjp {
            private IBinder zznJ;

            C1132zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(Request request, zzjq zzjq) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    if (request != null) {
                        obtain.writeInt(1);
                        request.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzjq zzjq) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzjq zzjq, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzjq zzjq, String str, UsageInfo[] usageInfoArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeTypedArray(usageInfoArr, 0);
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzjq zzjq, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    obtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzjq zzjq) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
                    obtain.writeStrongBinder(zzjq != null ? zzjq.asBinder() : null);
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzjp zzag(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzjp)) ? new C1132zza(iBinder) : (zzjp) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch";
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        zza(com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()), parcel.readString(), (UsageInfo[]) parcel.createTypedArray(UsageInfo.CREATOR));
                        break;
                    case 2:
                        parcel.enforceInterface(str);
                        zza(com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()));
                        break;
                    case 3:
                        parcel.enforceInterface(str);
                        zzb(com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()));
                        break;
                    case 4:
                        parcel.enforceInterface(str);
                        zza(com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()), parcel.readInt() != 0);
                        break;
                    case 5:
                        parcel.enforceInterface(str);
                        zza(parcel.readInt() != 0 ? Request.CREATOR.createFromParcel(parcel) : null, com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()));
                        break;
                    case 6:
                        parcel.enforceInterface(str);
                        zza(com.google.android.gms.internal.zzjq.zza.zzah(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeNoException();
                return true;
            }
            parcel2.writeString(str);
            return true;
        }
    }

    void zza(Request request, zzjq zzjq) throws RemoteException;

    void zza(zzjq zzjq) throws RemoteException;

    void zza(zzjq zzjq, String str, String str2) throws RemoteException;

    void zza(zzjq zzjq, String str, UsageInfo[] usageInfoArr) throws RemoteException;

    void zza(zzjq zzjq, boolean z) throws RemoteException;

    void zzb(zzjq zzjq) throws RemoteException;
}
