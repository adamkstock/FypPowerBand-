package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyResponse;

public interface zzkj extends IInterface {

    public static abstract class zza extends Binder implements zzkj {

        /* renamed from: com.google.android.gms.internal.zzkj$zza$zza reason: collision with other inner class name */
        private static class C1137zza implements zzkj {
            private IBinder zznJ;

            C1137zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            public IBinder asBinder() {
                return this.zznJ;
            }

            public void zza(ProxyResponse proxyResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthCallbacks");
                    if (proxyResponse != null) {
                        obtain.writeInt(1);
                        proxyResponse.writeToParcel(obtain, 0);
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
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.auth.api.internal.IAuthCallbacks");
        }

        public static zzkj zzav(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.internal.IAuthCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzkj)) ? new C1137zza(iBinder) : (zzkj) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = "com.google.android.gms.auth.api.internal.IAuthCallbacks";
            if (i == 1) {
                parcel.enforceInterface(str);
                zza(parcel.readInt() != 0 ? (ProxyResponse) ProxyResponse.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void zza(ProxyResponse proxyResponse) throws RemoteException;
}
