package com.google.android.gms.appindexing;

import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.internal.zzx;

public class Thing {
    final Bundle zzRi;

    public static class Builder {
        final Bundle zzRj = new Bundle();

        public Thing build() {
            return new Thing(this.zzRj);
        }

        public Builder put(String str, Thing thing) {
            zzx.zzw(str);
            if (thing != null) {
                this.zzRj.putParcelable(str, thing.zzRi);
            }
            return this;
        }

        public Builder put(String str, String str2) {
            zzx.zzw(str);
            if (str2 != null) {
                this.zzRj.putString(str, str2);
            }
            return this;
        }

        public Builder setDescription(String str) {
            put("description", str);
            return this;
        }

        public Builder setId(String str) {
            if (str != null) {
                put("id", str);
            }
            return this;
        }

        public Builder setName(String str) {
            zzx.zzw(str);
            put("name", str);
            return this;
        }

        public Builder setType(String str) {
            put("type", str);
            return this;
        }

        public Builder setUrl(Uri uri) {
            zzx.zzw(uri);
            put("url", uri.toString());
            return this;
        }
    }

    Thing(Bundle bundle) {
        this.zzRi = bundle;
    }

    public Bundle zzlx() {
        return this.zzRi;
    }
}
