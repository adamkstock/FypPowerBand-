package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class CredentialRequest implements SafeParcelable {
    public static final Creator<CredentialRequest> CREATOR = new zzc();
    final int mVersionCode;
    private final boolean zzSo;
    private final String[] zzSp;
    private final CredentialPickerConfig zzSq;
    private final CredentialPickerConfig zzSr;

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean zzSo;
        /* access modifiers changed from: private */
        public String[] zzSp;
        /* access modifiers changed from: private */
        public CredentialPickerConfig zzSq;
        /* access modifiers changed from: private */
        public CredentialPickerConfig zzSr;

        public CredentialRequest build() {
            if (this.zzSp == null) {
                this.zzSp = new String[0];
            }
            if (this.zzSo || this.zzSp.length != 0) {
                return new CredentialRequest(this);
            }
            throw new IllegalStateException("At least one authentication method must be specified");
        }

        public Builder setAccountTypes(String... strArr) {
            this.zzSp = strArr;
            return this;
        }

        public Builder setCredentialHintPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzSr = credentialPickerConfig;
            return this;
        }

        public Builder setCredentialPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzSq = credentialPickerConfig;
            return this;
        }

        public Builder setSupportsPasswordLogin(boolean z) {
            this.zzSo = z;
            return this;
        }
    }

    CredentialRequest(int i, boolean z, String[] strArr, CredentialPickerConfig credentialPickerConfig, CredentialPickerConfig credentialPickerConfig2) {
        this.mVersionCode = i;
        this.zzSo = z;
        this.zzSp = (String[]) zzx.zzw(strArr);
        if (credentialPickerConfig == null) {
            credentialPickerConfig = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzSq = credentialPickerConfig;
        if (credentialPickerConfig2 == null) {
            credentialPickerConfig2 = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzSr = credentialPickerConfig2;
    }

    private CredentialRequest(Builder builder) {
        this(2, builder.zzSo, builder.zzSp, builder.zzSq, builder.zzSr);
    }

    public int describeContents() {
        return 0;
    }

    public String[] getAccountTypes() {
        return this.zzSp;
    }

    public CredentialPickerConfig getCredentialHintPickerConfig() {
        return this.zzSr;
    }

    public CredentialPickerConfig getCredentialPickerConfig() {
        return this.zzSq;
    }

    public boolean getSupportsPasswordLogin() {
        return this.zzSo;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
