package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

public class zzae {
    public static String zza(String str, String str2, Context context, AttributeSet attributeSet, boolean z, boolean z2, String str3) {
        String attributeValue = attributeSet == null ? null : attributeSet.getAttributeValue(str, str2);
        if (attributeValue != null && attributeValue.startsWith("@string/") && z) {
            String substring = attributeValue.substring(8);
            String packageName = context.getPackageName();
            TypedValue typedValue = new TypedValue();
            try {
                Resources resources = context.getResources();
                StringBuilder sb = new StringBuilder();
                sb.append(packageName);
                sb.append(":string/");
                sb.append(substring);
                resources.getValue(sb.toString(), typedValue, true);
            } catch (NotFoundException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not find resource for ");
                sb2.append(str2);
                sb2.append(": ");
                sb2.append(attributeValue);
                Log.w(str3, sb2.toString());
            }
            if (typedValue.string != null) {
                attributeValue = typedValue.string.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Resource ");
                sb3.append(str2);
                sb3.append(" was not a string: ");
                sb3.append(typedValue);
                Log.w(str3, sb3.toString());
            }
        }
        if (z2 && attributeValue == null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Required XML attribute \"");
            sb4.append(str2);
            sb4.append("\" missing");
            Log.w(str3, sb4.toString());
        }
        return attributeValue;
    }
}
