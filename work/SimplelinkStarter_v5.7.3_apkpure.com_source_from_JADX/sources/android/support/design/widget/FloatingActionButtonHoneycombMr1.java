package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.p000v4.view.ViewCompat;
import android.view.View;

class FloatingActionButtonHoneycombMr1 extends FloatingActionButtonEclairMr1 {
    /* access modifiers changed from: private */
    public boolean mIsHiding;

    FloatingActionButtonHoneycombMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
    }

    /* access modifiers changed from: 0000 */
    public void hide() {
        if (!this.mIsHiding && this.mView.getVisibility() == 0) {
            if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
                this.mView.setVisibility(8);
            } else {
                this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = true;
                        FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
                    }

                    public void onAnimationCancel(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                    }

                    public void onAnimationEnd(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                        FloatingActionButtonHoneycombMr1.this.mView.setVisibility(8);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void show() {
        if (this.mView.getVisibility() == 0) {
            return;
        }
        if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
            this.mView.setVisibility(0);
            this.mView.setAlpha(1.0f);
            this.mView.setScaleY(1.0f);
            this.mView.setScaleX(1.0f);
            return;
        }
        this.mView.setAlpha(0.0f);
        this.mView.setScaleY(0.0f);
        this.mView.setScaleX(0.0f);
        this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
            }
        });
    }
}
