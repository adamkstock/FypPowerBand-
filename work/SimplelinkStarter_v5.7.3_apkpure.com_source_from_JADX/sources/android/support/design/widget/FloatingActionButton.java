package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.C0010R;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import java.util.List;

@DefaultBehavior(Behavior.class)
public class FloatingActionButton extends ImageView {
    private static final int SIZE_MINI = 1;
    private static final int SIZE_NORMAL = 0;
    private ColorStateList mBackgroundTint;
    private Mode mBackgroundTintMode;
    private int mBorderWidth;
    /* access modifiers changed from: private */
    public int mContentPadding;
    private final FloatingActionButtonImpl mImpl;
    private int mRippleColor;
    /* access modifiers changed from: private */
    public final Rect mShadowPadding;
    private int mSize;

    public static class Behavior extends android.support.design.widget.CoordinatorLayout.Behavior<FloatingActionButton> {
        private static final boolean SNACKBAR_BEHAVIOR_ENABLED = (VERSION.SDK_INT >= 11);
        private Rect mTmpRect;

        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            return SNACKBAR_BEHAVIOR_ENABLED && (view instanceof SnackbarLayout);
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof SnackbarLayout) {
                updateFabTranslationForSnackbar(coordinatorLayout, floatingActionButton, view);
            } else if (view instanceof AppBarLayout) {
                updateFabVisibility(coordinatorLayout, (AppBarLayout) view, floatingActionButton);
            }
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if ((view instanceof SnackbarLayout) && ViewCompat.getTranslationY(floatingActionButton) != 0.0f) {
                ViewCompat.animate(floatingActionButton).translationY(0.0f).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null);
            }
        }

        private boolean updateFabVisibility(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (((LayoutParams) floatingActionButton.getLayoutParams()).getAnchorId() != appBarLayout.getId()) {
                return false;
            }
            if (this.mTmpRect == null) {
                this.mTmpRect = new Rect();
            }
            Rect rect = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide();
            } else {
                floatingActionButton.show();
            }
            return true;
        }

        private void updateFabTranslationForSnackbar(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (floatingActionButton.getVisibility() == 0) {
                ViewCompat.setTranslationY(floatingActionButton, getFabTranslationYForSnackbar(coordinatorLayout, floatingActionButton));
            }
        }

        private float getFabTranslationYForSnackbar(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            List dependencies = coordinatorLayout.getDependencies(floatingActionButton);
            int size = dependencies.size();
            float f = 0.0f;
            for (int i = 0; i < size; i++) {
                View view = (View) dependencies.get(i);
                if ((view instanceof SnackbarLayout) && coordinatorLayout.doViewsOverlap(floatingActionButton, view)) {
                    f = Math.min(f, ViewCompat.getTranslationY(view) - ((float) view.getHeight()));
                }
            }
            return f;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i) {
            List dependencies = coordinatorLayout.getDependencies(floatingActionButton);
            int size = dependencies.size();
            for (int i2 = 0; i2 < size; i2++) {
                View view = (View) dependencies.get(i2);
                if ((view instanceof AppBarLayout) && updateFabVisibility(coordinatorLayout, (AppBarLayout) view, floatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.onLayoutChild(floatingActionButton, i);
            offsetIfNeeded(coordinatorLayout, floatingActionButton);
            return true;
        }

        private void offsetIfNeeded(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            Rect access$000 = floatingActionButton.mShadowPadding;
            if (access$000 != null && access$000.centerX() > 0 && access$000.centerY() > 0) {
                LayoutParams layoutParams = (LayoutParams) floatingActionButton.getLayoutParams();
                int i = 0;
                int i2 = floatingActionButton.getRight() >= coordinatorLayout.getWidth() - layoutParams.rightMargin ? access$000.right : floatingActionButton.getLeft() <= layoutParams.leftMargin ? -access$000.left : 0;
                if (floatingActionButton.getBottom() >= coordinatorLayout.getBottom() - layoutParams.bottomMargin) {
                    i = access$000.bottom;
                } else if (floatingActionButton.getTop() <= layoutParams.topMargin) {
                    i = -access$000.top;
                }
                floatingActionButton.offsetTopAndBottom(i);
                floatingActionButton.offsetLeftAndRight(i2);
            }
        }
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShadowPadding = new Rect();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.FloatingActionButton, i, C0010R.style.Widget_Design_FloatingActionButton);
        Drawable drawable = obtainStyledAttributes.getDrawable(C0010R.styleable.FloatingActionButton_android_background);
        this.mBackgroundTint = obtainStyledAttributes.getColorStateList(C0010R.styleable.FloatingActionButton_backgroundTint);
        this.mBackgroundTintMode = parseTintMode(obtainStyledAttributes.getInt(C0010R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.mRippleColor = obtainStyledAttributes.getColor(C0010R.styleable.FloatingActionButton_rippleColor, 0);
        this.mSize = obtainStyledAttributes.getInt(C0010R.styleable.FloatingActionButton_fabSize, 0);
        this.mBorderWidth = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.FloatingActionButton_borderWidth, 0);
        float dimension = obtainStyledAttributes.getDimension(C0010R.styleable.FloatingActionButton_elevation, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(C0010R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        obtainStyledAttributes.recycle();
        C00211 r7 = new ShadowViewDelegate() {
            public float getRadius() {
                return ((float) FloatingActionButton.this.getSizeDimension()) / 2.0f;
            }

            public void setShadowPadding(int i, int i2, int i3, int i4) {
                FloatingActionButton.this.mShadowPadding.set(i, i2, i3, i4);
                FloatingActionButton floatingActionButton = FloatingActionButton.this;
                floatingActionButton.setPadding(i + floatingActionButton.mContentPadding, i2 + FloatingActionButton.this.mContentPadding, i3 + FloatingActionButton.this.mContentPadding, i4 + FloatingActionButton.this.mContentPadding);
            }

            public void setBackgroundDrawable(Drawable drawable) {
                FloatingActionButton.super.setBackgroundDrawable(drawable);
            }
        };
        int i2 = VERSION.SDK_INT;
        if (i2 >= 21) {
            this.mImpl = new FloatingActionButtonLollipop(this, r7);
        } else if (i2 >= 12) {
            this.mImpl = new FloatingActionButtonHoneycombMr1(this, r7);
        } else {
            this.mImpl = new FloatingActionButtonEclairMr1(this, r7);
        }
        this.mContentPadding = (getSizeDimension() - ((int) getResources().getDimension(C0010R.dimen.design_fab_content_size))) / 2;
        this.mImpl.setBackgroundDrawable(drawable, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        this.mImpl.setElevation(dimension);
        this.mImpl.setPressedTranslationZ(dimension2);
        setClickable(true);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int sizeDimension = getSizeDimension();
        int min = Math.min(resolveAdjustedSize(sizeDimension, i), resolveAdjustedSize(sizeDimension, i2));
        setMeasuredDimension(this.mShadowPadding.left + min + this.mShadowPadding.right, min + this.mShadowPadding.top + this.mShadowPadding.bottom);
    }

    public void setRippleColor(int i) {
        if (this.mRippleColor != i) {
            this.mRippleColor = i;
            this.mImpl.setRippleColor(i);
        }
    }

    public ColorStateList getBackgroundTintList() {
        return this.mBackgroundTint;
    }

    public void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.mBackgroundTint != colorStateList) {
            this.mBackgroundTint = colorStateList;
            this.mImpl.setBackgroundTintList(colorStateList);
        }
    }

    public Mode getBackgroundTintMode() {
        return this.mBackgroundTintMode;
    }

    public void setBackgroundTintMode(Mode mode) {
        if (this.mBackgroundTintMode != mode) {
            this.mBackgroundTintMode = mode;
            this.mImpl.setBackgroundTintMode(mode);
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        FloatingActionButtonImpl floatingActionButtonImpl = this.mImpl;
        if (floatingActionButtonImpl != null) {
            floatingActionButtonImpl.setBackgroundDrawable(drawable, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        }
    }

    public void show() {
        this.mImpl.show();
    }

    public void hide() {
        this.mImpl.hide();
    }

    /* access modifiers changed from: 0000 */
    public final int getSizeDimension() {
        if (this.mSize != 1) {
            return getResources().getDimensionPixelSize(C0010R.dimen.design_fab_size_normal);
        }
        return getResources().getDimensionPixelSize(C0010R.dimen.design_fab_size_mini);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.mImpl.onDrawableStateChanged(getDrawableState());
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mImpl.jumpDrawableToCurrentState();
    }

    private static int resolveAdjustedSize(int i, int i2) {
        int mode = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i2);
        if (mode != Integer.MIN_VALUE) {
            return (mode == 0 || mode != 1073741824) ? i : size;
        }
        return Math.min(i, size);
    }

    static Mode parseTintMode(int i, Mode mode) {
        if (i == 3) {
            return Mode.SRC_OVER;
        }
        if (i == 5) {
            return Mode.SRC_IN;
        }
        if (i == 9) {
            return Mode.SRC_ATOP;
        }
        if (i != 14) {
            return i != 15 ? mode : Mode.SCREEN;
        }
        return Mode.MULTIPLY;
    }
}
