package android.support.p003v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.appcompat.C0251R;
import android.support.p003v7.graphics.drawable.DrawableUtils;
import android.support.p003v7.internal.widget.TintInfo;
import android.support.p003v7.internal.widget.TintManager;
import android.util.AttributeSet;
import android.view.View;

/* renamed from: android.support.v7.widget.AppCompatBackgroundHelper */
class AppCompatBackgroundHelper {
    private TintInfo mBackgroundTint;
    private TintInfo mInternalBackgroundTint;
    private final TintManager mTintManager;
    private final View mView;

    AppCompatBackgroundHelper(View view, TintManager tintManager) {
        this.mView = view;
        this.mTintManager = tintManager;
    }

    /* access modifiers changed from: 0000 */
    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(attributeSet, C0251R.styleable.ViewBackgroundHelper, i, 0);
        try {
            if (obtainStyledAttributes.hasValue(C0251R.styleable.ViewBackgroundHelper_android_background)) {
                ColorStateList tintList = this.mTintManager.getTintList(obtainStyledAttributes.getResourceId(C0251R.styleable.ViewBackgroundHelper_android_background, -1));
                if (tintList != null) {
                    setInternalBackgroundTint(tintList);
                }
            }
            if (obtainStyledAttributes.hasValue(C0251R.styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, obtainStyledAttributes.getColorStateList(C0251R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (obtainStyledAttributes.hasValue(C0251R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(C0251R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: 0000 */
    public void onSetBackgroundResource(int i) {
        TintManager tintManager = this.mTintManager;
        setInternalBackgroundTint(tintManager != null ? tintManager.getTintList(i) : null);
    }

    /* access modifiers changed from: 0000 */
    public void onSetBackgroundDrawable(Drawable drawable) {
        setInternalBackgroundTint(null);
    }

    /* access modifiers changed from: 0000 */
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo.mTintList = colorStateList;
        tintInfo.mHasTintList = true;
        applySupportBackgroundTint();
    }

    /* access modifiers changed from: 0000 */
    public ColorStateList getSupportBackgroundTintList() {
        TintInfo tintInfo = this.mBackgroundTint;
        if (tintInfo != null) {
            return tintInfo.mTintList;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void setSupportBackgroundTintMode(Mode mode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo.mTintMode = mode;
        tintInfo.mHasTintMode = true;
        applySupportBackgroundTint();
    }

    /* access modifiers changed from: 0000 */
    public Mode getSupportBackgroundTintMode() {
        TintInfo tintInfo = this.mBackgroundTint;
        if (tintInfo != null) {
            return tintInfo.mTintMode;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void applySupportBackgroundTint() {
        if (this.mView.getBackground() != null) {
            TintInfo tintInfo = this.mBackgroundTint;
            if (tintInfo != null) {
                TintManager.tintViewBackground(this.mView, tintInfo);
                return;
            }
            TintInfo tintInfo2 = this.mInternalBackgroundTint;
            if (tintInfo2 != null) {
                TintManager.tintViewBackground(this.mView, tintInfo2);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setInternalBackgroundTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            TintInfo tintInfo = this.mInternalBackgroundTint;
            tintInfo.mTintList = colorStateList;
            tintInfo.mHasTintList = true;
        } else {
            this.mInternalBackgroundTint = null;
        }
        applySupportBackgroundTint();
    }
}
