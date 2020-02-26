package android.support.p003v7.internal.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.p000v4.util.ArrayMap;
import android.support.p003v7.appcompat.C0251R;
import android.support.p003v7.internal.view.ContextThemeWrapper;
import android.support.p003v7.widget.AppCompatAutoCompleteTextView;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatCheckBox;
import android.support.p003v7.widget.AppCompatCheckedTextView;
import android.support.p003v7.widget.AppCompatEditText;
import android.support.p003v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.p003v7.widget.AppCompatRadioButton;
import android.support.p003v7.widget.AppCompatRatingBar;
import android.support.p003v7.widget.AppCompatSpinner;
import android.support.p003v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.Map;

/* renamed from: android.support.v7.internal.app.AppCompatViewInflater */
public class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap();
    static final Class<?>[] sConstructorSignature = {Context.class, AttributeSet.class};
    private final Object[] mConstructorArgs = new Object[2];

    public final View createView(View view, String str, Context context, AttributeSet attributeSet, boolean z, boolean z2, boolean z3) {
        Context context2 = (!z || view == null) ? context : view.getContext();
        if (z2 || z3) {
            context2 = themifyContext(context2, attributeSet, z2, z3);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1946472170:
                if (str.equals("RatingBar")) {
                    c = 7;
                    break;
                }
                break;
            case -1455429095:
                if (str.equals("CheckedTextView")) {
                    c = 4;
                    break;
                }
                break;
            case -1346021293:
                if (str.equals("MultiAutoCompleteTextView")) {
                    c = 6;
                    break;
                }
                break;
            case -938935918:
                if (str.equals("TextView")) {
                    c = 9;
                    break;
                }
                break;
            case -339785223:
                if (str.equals("Spinner")) {
                    c = 1;
                    break;
                }
                break;
            case 776382189:
                if (str.equals("RadioButton")) {
                    c = 3;
                    break;
                }
                break;
            case 1413872058:
                if (str.equals("AutoCompleteTextView")) {
                    c = 5;
                    break;
                }
                break;
            case 1601505219:
                if (str.equals("CheckBox")) {
                    c = 2;
                    break;
                }
                break;
            case 1666676343:
                if (str.equals("EditText")) {
                    c = 0;
                    break;
                }
                break;
            case 2001146706:
                if (str.equals("Button")) {
                    c = 8;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new AppCompatEditText(context2, attributeSet);
            case 1:
                return new AppCompatSpinner(context2, attributeSet);
            case 2:
                return new AppCompatCheckBox(context2, attributeSet);
            case 3:
                return new AppCompatRadioButton(context2, attributeSet);
            case 4:
                return new AppCompatCheckedTextView(context2, attributeSet);
            case 5:
                return new AppCompatAutoCompleteTextView(context2, attributeSet);
            case 6:
                return new AppCompatMultiAutoCompleteTextView(context2, attributeSet);
            case 7:
                return new AppCompatRatingBar(context2, attributeSet);
            case 8:
                return new AppCompatButton(context2, attributeSet);
            case 9:
                return new AppCompatTextView(context2, attributeSet);
            default:
                if (context != context2) {
                    return createViewFromTag(context2, str, attributeSet);
                }
                return null;
        }
    }

    private View createViewFromTag(Context context, String str, AttributeSet attributeSet) {
        if (str.equals("view")) {
            str = attributeSet.getAttributeValue(null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 == str.indexOf(46)) {
                View createView = createView(context, str, "android.widget.");
                return createView;
            }
            View createView2 = createView(context, str, null);
            Object[] objArr = this.mConstructorArgs;
            objArr[0] = null;
            objArr[1] = null;
            return createView2;
        } catch (Exception unused) {
            return null;
        } finally {
            Object[] objArr2 = this.mConstructorArgs;
            objArr2[0] = null;
            objArr2[1] = null;
        }
    }

    private View createView(Context context, String str, String str2) throws ClassNotFoundException, InflateException {
        String str3;
        Constructor constructor = (Constructor) sConstructorMap.get(str);
        if (constructor == null) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                if (str2 != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str);
                    str3 = sb.toString();
                } else {
                    str3 = str;
                }
                constructor = classLoader.loadClass(str3).asSubclass(View.class).getConstructor(sConstructorSignature);
                sConstructorMap.put(str, constructor);
            } catch (Exception unused) {
                return null;
            }
        }
        constructor.setAccessible(true);
        return (View) constructor.newInstance(this.mConstructorArgs);
    }

    private static Context themifyContext(Context context, AttributeSet attributeSet, boolean z, boolean z2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0251R.styleable.View, 0, 0);
        int resourceId = z ? obtainStyledAttributes.getResourceId(C0251R.styleable.View_android_theme, 0) : 0;
        if (z2 && resourceId == 0) {
            resourceId = obtainStyledAttributes.getResourceId(C0251R.styleable.View_theme, 0);
            if (resourceId != 0) {
                Log.i(LOG_TAG, "app:theme is now deprecated. Please move to using android:theme instead.");
            }
        }
        obtainStyledAttributes.recycle();
        if (resourceId != 0) {
            return (!(context instanceof ContextThemeWrapper) || ((ContextThemeWrapper) context).getThemeResId() != resourceId) ? new ContextThemeWrapper(context, resourceId) : context;
        }
        return context;
    }
}
