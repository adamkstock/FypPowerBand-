package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.p003v7.appcompat.C0251R;
import android.support.p003v7.internal.text.AllCapsTransformationMethod;
import android.support.p003v7.internal.widget.ThemeUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/* renamed from: android.support.v7.widget.AppCompatTextHelper */
class AppCompatTextHelper {
    private static final int[] TEXT_APPEARANCE_ATTRS = {C0251R.attr.textAllCaps};
    private static final int[] VIEW_ATTRS = {16842804};
    private final TextView mView;

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
    }

    /* access modifiers changed from: 0000 */
    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        int i2;
        Context context = this.mView.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, VIEW_ATTRS, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        if (resourceId != -1) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, C0251R.styleable.TextAppearance);
            if (obtainStyledAttributes2.hasValue(C0251R.styleable.TextAppearance_textAllCaps)) {
                setAllCaps(obtainStyledAttributes2.getBoolean(C0251R.styleable.TextAppearance_textAllCaps, false));
            }
            obtainStyledAttributes2.recycle();
        }
        TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, TEXT_APPEARANCE_ATTRS, i, 0);
        if (obtainStyledAttributes3.hasValue(0)) {
            setAllCaps(obtainStyledAttributes3.getBoolean(0, false));
        }
        obtainStyledAttributes3.recycle();
        ColorStateList textColors = this.mView.getTextColors();
        if (textColors != null && !textColors.isStateful()) {
            if (VERSION.SDK_INT < 21) {
                i2 = ThemeUtils.getDisabledThemeAttrColor(context, 16842808);
            } else {
                i2 = ThemeUtils.getThemeAttrColor(context, 16842808);
            }
            this.mView.setTextColor(ThemeUtils.createDisabledStateList(textColors.getDefaultColor(), i2));
        }
    }

    /* access modifiers changed from: 0000 */
    public void onSetTextAppearance(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, TEXT_APPEARANCE_ATTRS);
        if (obtainStyledAttributes.hasValue(0)) {
            setAllCaps(obtainStyledAttributes.getBoolean(0, false));
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: 0000 */
    public void setAllCaps(boolean z) {
        TextView textView = this.mView;
        textView.setTransformationMethod(z ? new AllCapsTransformationMethod(textView.getContext()) : null);
    }
}
