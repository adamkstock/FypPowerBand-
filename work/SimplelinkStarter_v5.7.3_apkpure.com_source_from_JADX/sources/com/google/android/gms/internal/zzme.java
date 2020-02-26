package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class zzme<K, V> extends zzmi<K, V> implements Map<K, V> {
    zzmh<K, V> zzagz;

    private zzmh<K, V> zzpx() {
        if (this.zzagz == null) {
            this.zzagz = new zzmh<K, V>() {
                /* access modifiers changed from: protected */
                public void colClear() {
                    zzme.this.clear();
                }

                /* access modifiers changed from: protected */
                public Object colGetEntry(int i, int i2) {
                    return zzme.this.mArray[(i << 1) + i2];
                }

                /* access modifiers changed from: protected */
                public Map<K, V> colGetMap() {
                    return zzme.this;
                }

                /* access modifiers changed from: protected */
                public int colGetSize() {
                    return zzme.this.mSize;
                }

                /* access modifiers changed from: protected */
                public int colIndexOfKey(Object obj) {
                    return obj == null ? zzme.this.indexOfNull() : zzme.this.indexOf(obj, obj.hashCode());
                }

                /* access modifiers changed from: protected */
                public int colIndexOfValue(Object obj) {
                    return zzme.this.indexOfValue(obj);
                }

                /* access modifiers changed from: protected */
                public void colPut(K k, V v) {
                    zzme.this.put(k, v);
                }

                /* access modifiers changed from: protected */
                public void colRemoveAt(int i) {
                    zzme.this.removeAt(i);
                }

                /* access modifiers changed from: protected */
                public V colSetValue(int i, V v) {
                    return zzme.this.setValueAt(i, v);
                }
            };
        }
        return this.zzagz;
    }

    public Set<Entry<K, V>> entrySet() {
        return zzpx().getEntrySet();
    }

    public Set<K> keySet() {
        return zzpx().getKeySet();
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Collection<V> values() {
        return zzpx().getValues();
    }
}
