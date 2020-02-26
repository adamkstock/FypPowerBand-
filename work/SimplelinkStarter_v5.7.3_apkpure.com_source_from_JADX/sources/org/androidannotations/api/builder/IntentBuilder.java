package org.androidannotations.api.builder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import org.androidannotations.api.builder.IntentBuilder;

public abstract class IntentBuilder<I extends IntentBuilder<I>> extends Builder {
    protected final Context context;
    protected final Intent intent;

    public IntentBuilder(Context context2, Class<?> cls) {
        this(context2, new Intent(context2, cls));
    }

    public IntentBuilder(Context context2, Intent intent2) {
        this.context = context2;
        this.intent = intent2;
    }

    public Context getContext() {
        return this.context;
    }

    public Intent get() {
        return this.intent;
    }

    public I flags(int i) {
        this.intent.setFlags(i);
        return this;
    }

    public I action(String str) {
        this.intent.setAction(str);
        return this;
    }

    public I type(String str) {
        this.intent.setType(str);
        return this;
    }

    public I category(String str) {
        this.intent.addCategory(str);
        return this;
    }

    public I data(Uri uri) {
        this.intent.setData(uri);
        return this;
    }

    public I extra(String str, boolean z) {
        this.intent.putExtra(str, z);
        return this;
    }

    public I extra(String str, byte b) {
        this.intent.putExtra(str, b);
        return this;
    }

    public I extra(String str, char c) {
        this.intent.putExtra(str, c);
        return this;
    }

    public I extra(String str, short s) {
        this.intent.putExtra(str, s);
        return this;
    }

    public I extra(String str, int i) {
        this.intent.putExtra(str, i);
        return this;
    }

    public I extra(String str, long j) {
        this.intent.putExtra(str, j);
        return this;
    }

    public I extra(String str, float f) {
        this.intent.putExtra(str, f);
        return this;
    }

    public I extra(String str, double d) {
        this.intent.putExtra(str, d);
        return this;
    }

    public I extra(String str, String str2) {
        this.intent.putExtra(str, str2);
        return this;
    }

    public I extra(String str, CharSequence charSequence) {
        this.intent.putExtra(str, charSequence);
        return this;
    }

    public I extra(String str, Parcelable parcelable) {
        this.intent.putExtra(str, parcelable);
        return this;
    }

    public I extra(String str, Parcelable[] parcelableArr) {
        this.intent.putExtra(str, parcelableArr);
        return this;
    }

    public I parcelableArrayListExtra(String str, ArrayList<? extends Parcelable> arrayList) {
        this.intent.putParcelableArrayListExtra(str, arrayList);
        return this;
    }

    public I integerArrayListExtra(String str, ArrayList<Integer> arrayList) {
        this.intent.putIntegerArrayListExtra(str, arrayList);
        return this;
    }

    public I stringArrayListExtra(String str, ArrayList<String> arrayList) {
        this.intent.putStringArrayListExtra(str, arrayList);
        return this;
    }

    public I extra(String str, Serializable serializable) {
        this.intent.putExtra(str, serializable);
        return this;
    }

    public I extra(String str, boolean[] zArr) {
        this.intent.putExtra(str, zArr);
        return this;
    }

    public I extra(String str, byte[] bArr) {
        this.intent.putExtra(str, bArr);
        return this;
    }

    public I extra(String str, short[] sArr) {
        this.intent.putExtra(str, sArr);
        return this;
    }

    public I extra(String str, char[] cArr) {
        this.intent.putExtra(str, cArr);
        return this;
    }

    public I extra(String str, int[] iArr) {
        this.intent.putExtra(str, iArr);
        return this;
    }

    public I extra(String str, long[] jArr) {
        this.intent.putExtra(str, jArr);
        return this;
    }

    public I extra(String str, float[] fArr) {
        this.intent.putExtra(str, fArr);
        return this;
    }

    public I extra(String str, double[] dArr) {
        this.intent.putExtra(str, dArr);
        return this;
    }

    public I extra(String str, String[] strArr) {
        this.intent.putExtra(str, strArr);
        return this;
    }

    public I extra(String str, Bundle bundle) {
        this.intent.putExtra(str, bundle);
        return this;
    }

    public I extras(Intent intent2) {
        this.intent.putExtras(intent2);
        return this;
    }
}
