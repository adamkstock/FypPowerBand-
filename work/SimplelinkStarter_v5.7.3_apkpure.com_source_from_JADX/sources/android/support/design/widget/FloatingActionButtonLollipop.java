package android.support.design.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.p000v4.graphics.drawable.DrawableCompat;
import android.support.p000v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

class FloatingActionButtonLollipop extends FloatingActionButtonHoneycombMr1 {
    private Drawable mBorderDrawable;
    private Interpolator mInterpolator;
    private RippleDrawable mRippleDrawable;
    private Drawable mShapeDrawable;

    /* access modifiers changed from: 0000 */
    public void jumpDrawableToCurrentState() {
    }

    /* access modifiers changed from: 0000 */
    public void onDrawableStateChanged(int[] iArr) {
    }

    FloatingActionButtonLollipop(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        if (!view.isInEditMode()) {
            this.mInterpolator = AnimationUtils.loadInterpolator(this.mView.getContext(), 17563661);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setBackgroundDrawable(Drawable drawable, ColorStateList colorStateList, Mode mode, int i, int i2) {
        Drawable drawable2;
        this.mShapeDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        if (i2 > 0) {
            this.mBorderDrawable = createBorderDrawable(i2, colorStateList);
            drawable2 = new LayerDrawable(new Drawable[]{this.mBorderDrawable, this.mShapeDrawable});
        } else {
            this.mBorderDrawable = null;
            drawable2 = this.mShapeDrawable;
        }
        this.mRippleDrawable = new RippleDrawable(ColorStateList.valueOf(i), drawable2, null);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mRippleDrawable);
        this.mShadowViewDelegate.setShadowPadding(0, 0, 0, 0);
    }

    /* access modifiers changed from: 0000 */
    public void setBackgroundTintList(ColorStateList colorStateList) {
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        Drawable drawable = this.mBorderDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, colorStateList);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setBackgroundTintMode(Mode mode) {
        DrawableCompat.setTintMode(this.mShapeDrawable, mode);
    }

    /* access modifiers changed from: 0000 */
    public void setRippleColor(int i) {
        this.mRippleDrawable.setColor(ColorStateList.valueOf(i));
    }

    public void setElevation(float f) {
        ViewCompat.setElevation(this.mView, f);
    }

    /* access modifiers changed from: 0000 */
    public void setPressedTranslationZ(float f) {
        StateListAnimator stateListAnimator = new StateListAnimator();
        String str = "translationZ";
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this.mView, str, new float[]{f})));
        stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this.mView, str, new float[]{f})));
        stateListAnimator.addState(EMPTY_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this.mView, str, new float[]{0.0f})));
        this.mView.setStateListAnimator(stateListAnimator);
    }

    private Animator setupAnimator(Animator animator) {
        animator.setInterpolator(this.mInterpolator);
        return animator;
    }

    /* access modifiers changed from: 0000 */
    public CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawableLollipop();
    }
}
