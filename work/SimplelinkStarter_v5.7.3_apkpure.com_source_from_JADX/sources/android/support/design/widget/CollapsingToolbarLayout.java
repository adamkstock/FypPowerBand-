package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.C0010R;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.view.OnApplyWindowInsetsListener;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.view.WindowInsetsCompat;
import android.support.p003v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class CollapsingToolbarLayout extends FrameLayout {
    private static final int SCRIM_ANIMATION_DURATION = 600;
    /* access modifiers changed from: private */
    public final CollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    /* access modifiers changed from: private */
    public Drawable mContentScrim;
    /* access modifiers changed from: private */
    public int mCurrentOffset;
    private View mDummyView;
    private int mExpandedMarginBottom;
    private int mExpandedMarginLeft;
    private int mExpandedMarginRight;
    private int mExpandedMarginTop;
    /* access modifiers changed from: private */
    public WindowInsetsCompat mLastInsets;
    private OnOffsetChangedListener mOnOffsetChangedListener;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private ValueAnimatorCompat mScrimAnimator;
    private boolean mScrimsAreShown;
    /* access modifiers changed from: private */
    public Drawable mStatusBarScrim;
    private final Rect mTmpRect;
    private Toolbar mToolbar;
    private int mToolbarId;

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int mCollapseMode = 0;
        float mParallaxMult = DEFAULT_PARALLAX_MULTIPLIER;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.CollapsingAppBarLayout_LayoutParams);
            this.mCollapseMode = obtainStyledAttributes.getInt(C0010R.styleable.CollapsingAppBarLayout_LayoutParams_layout_collapseMode, 0);
            setParallaxMultiplier(obtainStyledAttributes.getFloat(C0010R.styleable.f5xad49a364, DEFAULT_PARALLAX_MULTIPLIER));
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public void setCollapseMode(int i) {
            this.mCollapseMode = i;
        }

        public int getCollapseMode() {
            return this.mCollapseMode;
        }

        public void setParallaxMultiplier(float f) {
            this.mParallaxMult = f;
        }

        public float getParallaxMultiplier() {
            return this.mParallaxMult;
        }
    }

    private class OffsetUpdateListener implements OnOffsetChangedListener {
        private OffsetUpdateListener() {
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            CollapsingToolbarLayout.this.mCurrentOffset = i;
            int systemWindowInsetTop = CollapsingToolbarLayout.this.mLastInsets != null ? CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop() : 0;
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            int childCount = CollapsingToolbarLayout.this.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = CollapsingToolbarLayout.this.getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                ViewOffsetHelper access$400 = CollapsingToolbarLayout.getViewOffsetHelper(childAt);
                int i3 = layoutParams.mCollapseMode;
                if (i3 != 1) {
                    if (i3 == 2) {
                        access$400.setTopAndBottomOffset(Math.round(((float) (-i)) * layoutParams.mParallaxMult));
                    }
                } else if ((CollapsingToolbarLayout.this.getHeight() - systemWindowInsetTop) + i >= childAt.getHeight()) {
                    access$400.setTopAndBottomOffset(-i);
                }
            }
            if (!(CollapsingToolbarLayout.this.mContentScrim == null && CollapsingToolbarLayout.this.mStatusBarScrim == null)) {
                if (CollapsingToolbarLayout.this.getHeight() + i < CollapsingToolbarLayout.this.getScrimTriggerOffset() + systemWindowInsetTop) {
                    CollapsingToolbarLayout.this.showScrim();
                } else {
                    CollapsingToolbarLayout.this.hideScrim();
                }
            }
            if (CollapsingToolbarLayout.this.mStatusBarScrim != null && systemWindowInsetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
            }
            CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(((float) Math.abs(i)) / ((float) ((CollapsingToolbarLayout.this.getHeight() - ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this)) - systemWindowInsetTop)));
            if (Math.abs(i) == totalScrollRange) {
                ViewCompat.setElevation(appBarLayout, appBarLayout.getTargetElevation());
            } else {
                ViewCompat.setElevation(appBarLayout, 0.0f);
            }
        }
    }

    public CollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        this.mCollapsingTextHelper = new CollapsingTextHelper(this);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.CollapsingToolbarLayout, i, C0010R.style.Widget_Design_CollapsingToolbar);
        this.mCollapsingTextHelper.setExpandedTextGravity(obtainStyledAttributes.getInt(C0010R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
        this.mCollapsingTextHelper.setCollapsedTextGravity(obtainStyledAttributes.getInt(C0010R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = dimensionPixelSize;
        this.mExpandedMarginRight = dimensionPixelSize;
        this.mExpandedMarginTop = dimensionPixelSize;
        this.mExpandedMarginLeft = dimensionPixelSize;
        boolean z = ViewCompat.getLayoutDirection(this) == 1;
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
            int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
            if (z) {
                this.mExpandedMarginRight = dimensionPixelSize2;
            } else {
                this.mExpandedMarginLeft = dimensionPixelSize2;
            }
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
            int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
            if (z) {
                this.mExpandedMarginLeft = dimensionPixelSize3;
            } else {
                this.mExpandedMarginRight = dimensionPixelSize3;
            }
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
            this.mExpandedMarginTop = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
            this.mExpandedMarginBottom = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
        }
        this.mCollapsingTitleEnabled = obtainStyledAttributes.getBoolean(C0010R.styleable.CollapsingToolbarLayout_titleEnabled, true);
        setTitle(obtainStyledAttributes.getText(C0010R.styleable.CollapsingToolbarLayout_title));
        this.mCollapsingTextHelper.setExpandedTextAppearance(C0010R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
        this.mCollapsingTextHelper.setCollapsedTextAppearance(C0010R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setExpandedTextAppearance(obtainStyledAttributes.getResourceId(C0010R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setCollapsedTextAppearance(obtainStyledAttributes.getResourceId(C0010R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0));
        }
        setContentScrim(obtainStyledAttributes.getDrawable(C0010R.styleable.CollapsingToolbarLayout_contentScrim));
        setStatusBarScrim(obtainStyledAttributes.getDrawable(C0010R.styleable.CollapsingToolbarLayout_statusBarScrim));
        this.mToolbarId = obtainStyledAttributes.getResourceId(C0010R.styleable.CollapsingToolbarLayout_toolbarId, -1);
        obtainStyledAttributes.recycle();
        setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                CollapsingToolbarLayout.this.mLastInsets = windowInsetsCompat;
                CollapsingToolbarLayout.this.requestLayout();
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ViewParent parent = getParent();
        OnOffsetChangedListener onOffsetChangedListener = this.mOnOffsetChangedListener;
        if (onOffsetChangedListener != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(onOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        ensureToolbar();
        if (this.mToolbar == null) {
            Drawable drawable = this.mContentScrim;
            if (drawable != null && this.mScrimAlpha > 0) {
                drawable.mutate().setAlpha(this.mScrimAlpha);
                this.mContentScrim.draw(canvas);
            }
        }
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.draw(canvas);
        }
        if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
            WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            if (systemWindowInsetTop > 0) {
                this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), systemWindowInsetTop - this.mCurrentOffset);
                this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
                this.mStatusBarScrim.draw(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        ensureToolbar();
        if (view == this.mToolbar) {
            Drawable drawable = this.mContentScrim;
            if (drawable != null && this.mScrimAlpha > 0) {
                drawable.mutate().setAlpha(this.mScrimAlpha);
                this.mContentScrim.draw(canvas);
            }
        }
        return super.drawChild(canvas, view, j);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Drawable drawable = this.mContentScrim;
        if (drawable != null) {
            drawable.setBounds(0, 0, i, i2);
        }
    }

    private void ensureToolbar() {
        if (this.mRefreshToolbar) {
            int childCount = getChildCount();
            Toolbar toolbar = null;
            Toolbar toolbar2 = null;
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = getChildAt(i);
                if (childAt instanceof Toolbar) {
                    int i2 = this.mToolbarId;
                    if (i2 == -1) {
                        toolbar = (Toolbar) childAt;
                        break;
                    } else if (i2 == childAt.getId()) {
                        toolbar = (Toolbar) childAt;
                        break;
                    } else if (toolbar2 == null) {
                        toolbar2 = (Toolbar) childAt;
                    }
                }
                i++;
            }
            if (toolbar == null) {
                toolbar = toolbar2;
            }
            this.mToolbar = toolbar;
            updateDummyView();
            this.mRefreshToolbar = false;
        }
    }

    private void updateDummyView() {
        if (!this.mCollapsingTitleEnabled) {
            View view = this.mDummyView;
            if (view != null) {
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(this.mDummyView);
                }
            }
        }
        if (this.mCollapsingTitleEnabled && this.mToolbar != null) {
            if (this.mDummyView == null) {
                this.mDummyView = new View(getContext());
            }
            if (this.mDummyView.getParent() == null) {
                this.mToolbar.addView(this.mDummyView, -1, -1);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        ensureToolbar();
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (this.mLastInsets != null && !ViewCompat.getFitsSystemWindows(childAt)) {
                int systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
                if (childAt.getTop() < systemWindowInsetTop) {
                    childAt.offsetTopAndBottom(systemWindowInsetTop);
                }
            }
            getViewOffsetHelper(childAt).onViewLayout();
        }
        if (this.mCollapsingTitleEnabled) {
            View view = this.mDummyView;
            if (view != null) {
                ViewGroupUtils.getDescendantRect(this, view, this.mTmpRect);
                this.mCollapsingTextHelper.setCollapsedBounds(this.mTmpRect.left, i4 - this.mTmpRect.height(), this.mTmpRect.right, i4);
                this.mCollapsingTextHelper.setExpandedBounds(this.mExpandedMarginLeft, this.mTmpRect.bottom + this.mExpandedMarginTop, (i3 - i) - this.mExpandedMarginRight, (i4 - i2) - this.mExpandedMarginBottom);
                this.mCollapsingTextHelper.recalculate();
            }
        }
        if (this.mToolbar != null) {
            if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
                this.mCollapsingTextHelper.setText(this.mToolbar.getTitle());
            }
            setMinimumHeight(this.mToolbar.getHeight());
        }
    }

    /* access modifiers changed from: private */
    public static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper viewOffsetHelper = (ViewOffsetHelper) view.getTag(C0010R.C0012id.view_offset_helper);
        if (viewOffsetHelper != null) {
            return viewOffsetHelper;
        }
        ViewOffsetHelper viewOffsetHelper2 = new ViewOffsetHelper(view);
        view.setTag(C0010R.C0012id.view_offset_helper, viewOffsetHelper2);
        return viewOffsetHelper2;
    }

    public void setTitle(CharSequence charSequence) {
        this.mCollapsingTextHelper.setText(charSequence);
    }

    public CharSequence getTitle() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getText();
        }
        return null;
    }

    public void setTitleEnabled(boolean z) {
        if (z != this.mCollapsingTitleEnabled) {
            this.mCollapsingTitleEnabled = z;
            updateDummyView();
            requestLayout();
        }
    }

    public boolean isTitleEnabled() {
        return this.mCollapsingTitleEnabled;
    }

    /* access modifiers changed from: private */
    public void showScrim() {
        if (!this.mScrimsAreShown) {
            if (!ViewCompat.isLaidOut(this) || isInEditMode()) {
                setScrimAlpha(255);
            } else {
                animateScrim(255);
            }
            this.mScrimsAreShown = true;
        }
    }

    /* access modifiers changed from: private */
    public void hideScrim() {
        if (this.mScrimsAreShown) {
            if (!ViewCompat.isLaidOut(this) || isInEditMode()) {
                setScrimAlpha(0);
            } else {
                animateScrim(0);
            }
            this.mScrimsAreShown = false;
        }
    }

    private void animateScrim(int i) {
        ensureToolbar();
        ValueAnimatorCompat valueAnimatorCompat = this.mScrimAnimator;
        if (valueAnimatorCompat == null) {
            this.mScrimAnimator = ViewUtils.createAnimator();
            this.mScrimAnimator.setDuration(SCRIM_ANIMATION_DURATION);
            this.mScrimAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.setUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                    CollapsingToolbarLayout.this.setScrimAlpha(valueAnimatorCompat.getAnimatedIntValue());
                }
            });
        } else if (valueAnimatorCompat.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setIntValues(this.mScrimAlpha, i);
        this.mScrimAnimator.start();
    }

    /* access modifiers changed from: private */
    public void setScrimAlpha(int i) {
        if (i != this.mScrimAlpha) {
            if (this.mContentScrim != null) {
                Toolbar toolbar = this.mToolbar;
                if (toolbar != null) {
                    ViewCompat.postInvalidateOnAnimation(toolbar);
                }
            }
            this.mScrimAlpha = i;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = this.mContentScrim;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            this.mContentScrim = drawable;
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.setCallback(this);
            drawable.mutate().setAlpha(this.mScrimAlpha);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(int i) {
        setContentScrim(new ColorDrawable(i));
    }

    public void setContentScrimResource(int i) {
        setContentScrim(ContextCompat.getDrawable(getContext(), i));
    }

    public Drawable getContentScrim() {
        return this.mContentScrim;
    }

    public void setStatusBarScrim(Drawable drawable) {
        Drawable drawable2 = this.mStatusBarScrim;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            this.mStatusBarScrim = drawable;
            drawable.setCallback(this);
            drawable.mutate().setAlpha(this.mScrimAlpha);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarScrimColor(int i) {
        setStatusBarScrim(new ColorDrawable(i));
    }

    public void setStatusBarScrimResource(int i) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), i));
    }

    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }

    public void setCollapsedTitleTextAppearance(int i) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(i);
    }

    public void setCollapsedTitleTextColor(int i) {
        this.mCollapsingTextHelper.setCollapsedTextColor(i);
    }

    public void setCollapsedTitleGravity(int i) {
        this.mCollapsingTextHelper.setExpandedTextGravity(i);
    }

    public int getCollapsedTitleGravity() {
        return this.mCollapsingTextHelper.getCollapsedTextGravity();
    }

    public void setExpandedTitleTextAppearance(int i) {
        this.mCollapsingTextHelper.setExpandedTextAppearance(i);
    }

    public void setExpandedTitleColor(int i) {
        this.mCollapsingTextHelper.setExpandedTextColor(i);
    }

    public void setExpandedTitleGravity(int i) {
        this.mCollapsingTextHelper.setExpandedTextGravity(i);
    }

    public int getExpandedTitleGravity() {
        return this.mCollapsingTextHelper.getExpandedTextGravity();
    }

    /* access modifiers changed from: 0000 */
    public final int getScrimTriggerOffset() {
        return ViewCompat.getMinimumHeight(this) * 2;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    public android.widget.FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public android.widget.FrameLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }
}
