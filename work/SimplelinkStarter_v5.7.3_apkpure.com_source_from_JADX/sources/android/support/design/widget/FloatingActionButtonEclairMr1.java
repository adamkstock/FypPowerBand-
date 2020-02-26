package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.C0010R;
import android.support.p000v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

class FloatingActionButtonEclairMr1 extends FloatingActionButtonImpl {
    private int mAnimationDuration;
    private Drawable mBorderDrawable;
    /* access modifiers changed from: private */
    public float mElevation;
    /* access modifiers changed from: private */
    public boolean mIsHiding;
    /* access modifiers changed from: private */
    public float mPressedTranslationZ;
    private Drawable mRippleDrawable;
    ShadowDrawableWrapper mShadowDrawable;
    private Drawable mShapeDrawable;
    private StateListAnimator mStateListAnimator = new StateListAnimator();

    private abstract class BaseShadowAnimation extends Animation {
        private float mShadowSizeDiff;
        private float mShadowSizeStart;

        /* access modifiers changed from: protected */
        public abstract float getTargetShadowSize();

        private BaseShadowAnimation() {
        }

        public void reset() {
            super.reset();
            this.mShadowSizeStart = FloatingActionButtonEclairMr1.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeDiff = getTargetShadowSize() - this.mShadowSizeStart;
        }

        /* access modifiers changed from: protected */
        public void applyTransformation(float f, Transformation transformation) {
            FloatingActionButtonEclairMr1.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + (this.mShadowSizeDiff * f));
        }
    }

    private class ElevateToTranslationZAnimation extends BaseShadowAnimation {
        private ElevateToTranslationZAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation + FloatingActionButtonEclairMr1.this.mPressedTranslationZ;
        }
    }

    private class ResetElevationAnimation extends BaseShadowAnimation {
        private ResetElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation;
        }
    }

    FloatingActionButtonEclairMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        this.mAnimationDuration = view.getResources().getInteger(17694720);
        this.mStateListAnimator.setTarget(view);
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, setupAnimation(new ResetElevationAnimation()));
    }

    /* access modifiers changed from: 0000 */
    public void setBackgroundDrawable(Drawable drawable, ColorStateList colorStateList, Mode mode, int i, int i2) {
        Drawable[] drawableArr;
        this.mShapeDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1);
        gradientDrawable.setCornerRadius(this.mShadowViewDelegate.getRadius());
        this.mRippleDrawable = DrawableCompat.wrap(gradientDrawable);
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(i));
        DrawableCompat.setTintMode(this.mRippleDrawable, Mode.MULTIPLY);
        if (i2 > 0) {
            this.mBorderDrawable = createBorderDrawable(i2, colorStateList);
            drawableArr = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            drawableArr = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        Resources resources = this.mView.getResources();
        LayerDrawable layerDrawable = new LayerDrawable(drawableArr);
        float radius = this.mShadowViewDelegate.getRadius();
        float f = this.mElevation;
        ShadowDrawableWrapper shadowDrawableWrapper = new ShadowDrawableWrapper(resources, layerDrawable, radius, f, f + this.mPressedTranslationZ);
        this.mShadowDrawable = shadowDrawableWrapper;
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
        updatePadding();
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
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(i));
    }

    /* access modifiers changed from: 0000 */
    public void setElevation(float f) {
        if (this.mElevation != f) {
            ShadowDrawableWrapper shadowDrawableWrapper = this.mShadowDrawable;
            if (shadowDrawableWrapper != null) {
                shadowDrawableWrapper.setShadowSize(f, this.mPressedTranslationZ + f);
                this.mElevation = f;
                updatePadding();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setPressedTranslationZ(float f) {
        if (this.mPressedTranslationZ != f) {
            ShadowDrawableWrapper shadowDrawableWrapper = this.mShadowDrawable;
            if (shadowDrawableWrapper != null) {
                this.mPressedTranslationZ = f;
                shadowDrawableWrapper.setMaxShadowSize(this.mElevation + f);
                updatePadding();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onDrawableStateChanged(int[] iArr) {
        this.mStateListAnimator.setState(iArr);
    }

    /* access modifiers changed from: 0000 */
    public void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }

    /* access modifiers changed from: 0000 */
    public void hide() {
        if (!this.mIsHiding && this.mView.getVisibility() == 0) {
            Animation loadAnimation = AnimationUtils.loadAnimation(this.mView.getContext(), C0010R.anim.design_fab_out);
            loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            loadAnimation.setDuration(200);
            loadAnimation.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationStart(Animation animation) {
                    FloatingActionButtonEclairMr1.this.mIsHiding = true;
                }

                public void onAnimationEnd(Animation animation) {
                    FloatingActionButtonEclairMr1.this.mIsHiding = false;
                    FloatingActionButtonEclairMr1.this.mView.setVisibility(8);
                }
            });
            this.mView.startAnimation(loadAnimation);
        }
    }

    /* access modifiers changed from: 0000 */
    public void show() {
        if (this.mView.getVisibility() != 0 || this.mIsHiding) {
            this.mView.clearAnimation();
            this.mView.setVisibility(0);
            Animation loadAnimation = AnimationUtils.loadAnimation(this.mView.getContext(), C0010R.anim.design_fab_in);
            loadAnimation.setDuration(200);
            loadAnimation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.mView.startAnimation(loadAnimation);
        }
    }

    private void updatePadding() {
        Rect rect = new Rect();
        this.mShadowDrawable.getPadding(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private Animation setupAnimation(Animation animation) {
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration((long) this.mAnimationDuration);
        return animation;
    }

    private static ColorStateList createColorStateList(int i) {
        return new ColorStateList(new int[][]{FOCUSED_ENABLED_STATE_SET, PRESSED_ENABLED_STATE_SET, new int[0]}, new int[]{i, i, 0});
    }
}
