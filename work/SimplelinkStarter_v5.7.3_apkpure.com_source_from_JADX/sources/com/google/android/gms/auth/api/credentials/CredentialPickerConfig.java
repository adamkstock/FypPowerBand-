package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class CredentialPickerConfig implements SafeParcelable {
    public static final Creator<CredentialPickerConfig> CREATOR = new zzb();
    private final boolean mShowCancelButton;
    final int mVersionCode;
    private final boolean zzSn;

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean mShowCancelButton = true;
        /* access modifiers changed from: private */
        public boolean zzSn = false;

        public CredentialPickerConfig build() {
            return new CredentialPickerConfig(this);
        }

        public Builder setShowAddAccountButton(boolean z) {
            this.zzSn = z;
            return this;
        }

        public Builder setShowCancelButton(boolean z) {
            this.mShowCancelButton = z;
            return this;
        }
    }

    CredentialPickerConfig(int i, boolean z, boolean z2) {
        this.mVersionCode = i;
        this.zzSn = z;
        this.mShowCancelButton = z2;
    }

    private CredentialPickerConfig(Builder builder) {
        this(1, builder.zzSn, builder.mShowCancelButton);
    }

    public int describeContents() {
        return 0;
    }

    public boolean shouldShowAddAccountButton() {
        return this.zzSn;
    }

    public boolean shouldShowCancelButton() {
        return this.mShowCancelButton;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }
}
