package com.google.android.gms.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Scope;
import java.util.List;

public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {
        public zza() {
            attachInterface(this, "com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = "com.google.android.gms.signin.internal.IOfflineAccessCallbacks";
            if (i == 2) {
                parcel.enforceInterface(str);
                zza(parcel.readString(), (List<Scope>) parcel.createTypedArrayList(Scope.CREATOR), com.google.android.gms.signin.internal.zzf.zza.zzdN(parcel.readStrongBinder()));
            } else if (i == 3) {
                parcel.enforceInterface(str);
                zza(parcel.readString(), parcel.readString(), com.google.android.gms.signin.internal.zzf.zza.zzdN(parcel.readStrongBinder()));
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

    void zza(String str, String str2, zzf zzf) throws RemoteException;

    void zza(String str, List<Scope> list, zzf zzf) throws RemoteException;
}
