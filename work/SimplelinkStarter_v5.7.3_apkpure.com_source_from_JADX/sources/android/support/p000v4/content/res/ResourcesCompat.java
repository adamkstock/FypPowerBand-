package android.support.p000v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.p000v4.content.res.FontResourcesParserCompat.FamilyResourceEntry;
import android.support.p000v4.graphics.TypefaceCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v4.content.res.ResourcesCompat */
public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    public static Drawable getDrawable(Resources resources, int i, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawable(i, theme);
        }
        return resources.getDrawable(i);
    }

    public static Drawable getDrawableForDensity(Resources resources, int i, int i2, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(i, i2, theme);
        }
        if (VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(i, i2);
        }
        return resources.getDrawable(i);
    }

    public static int getColor(Resources resources, int i, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return resources.getColor(i, theme);
        }
        return resources.getColor(i);
    }

    public static ColorStateList getColorStateList(Resources resources, int i, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(i, theme);
        }
        return resources.getColorStateList(i);
    }

    public static Typeface getFont(Context context, int i) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, new TypedValue(), 0, null);
    }

    public static Typeface getFont(Context context, int i, TypedValue typedValue, int i2, TextView textView) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, typedValue, i2, textView);
    }

    private static Typeface loadFont(Context context, int i, TypedValue typedValue, int i2, TextView textView) {
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        Typeface loadFont = loadFont(context, resources, typedValue, i, i2, textView);
        if (loadFont != null) {
            return loadFont;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Font resource ID #0x");
        sb.append(Integer.toHexString(i));
        throw new NotFoundException(sb.toString());
    }

    private static Typeface loadFont(Context context, Resources resources, TypedValue typedValue, int i, int i2, TextView textView) {
        String str = TAG;
        if (typedValue.string != null) {
            String charSequence = typedValue.string.toString();
            if (!charSequence.startsWith("res/")) {
                return null;
            }
            Typeface findFromCache = TypefaceCompat.findFromCache(resources, i, i2);
            if (findFromCache != null) {
                return findFromCache;
            }
            try {
                if (!charSequence.toLowerCase().endsWith(".xml")) {
                    return TypefaceCompat.createFromResourcesFontFile(context, resources, i, charSequence, i2);
                }
                FamilyResourceEntry parse = FontResourcesParserCompat.parse(resources.getXml(i), resources);
                if (parse != null) {
                    return TypefaceCompat.createFromResourcesFamilyXml(context, parse, resources, i, i2, textView);
                }
                Log.e(str, "Failed to find font-family tag");
                return null;
            } catch (XmlPullParserException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to parse xml resource ");
                sb.append(charSequence);
                Log.e(str, sb.toString(), e);
                return null;
            } catch (IOException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to read xml resource ");
                sb2.append(charSequence);
                Log.e(str, sb2.toString(), e2);
                return null;
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Resource \"");
            sb3.append(resources.getResourceName(i));
            sb3.append("\" (");
            sb3.append(Integer.toHexString(i));
            sb3.append(") is not a Font: ");
            sb3.append(typedValue);
            throw new NotFoundException(sb3.toString());
        }
    }

    private ResourcesCompat() {
    }
}
