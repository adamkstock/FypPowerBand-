package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.design.C0010R;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.p000v4.view.OnApplyWindowInsetsListener;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.view.WindowInsetsCompat;
import android.support.p000v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@DefaultBehavior(Behavior.class)
public class AppBarLayout extends LinearLayout {
    private static final int INVALID_SCROLL_RANGE = -1;
    private static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    private static final int PENDING_ACTION_COLLAPSED = 2;
    private static final int PENDING_ACTION_EXPANDED = 1;
    private static final int PENDING_ACTION_NONE = 0;
    private int mDownPreScrollRange;
    private int mDownScrollRange;
    boolean mHaveChildWithInterpolator;
    private WindowInsetsCompat mLastInsets;
    /* access modifiers changed from: private */
    public final List<OnOffsetChangedListener> mListeners;
    private int mPendingAction;
    private float mTargetElevation;
    private int mTotalScrollRange;

    public static class Behavior extends ViewOffsetBehavior<AppBarLayout> {
        private static final int INVALID_POINTER = -1;
        private static final int INVALID_POSITION = -1;
        private int mActivePointerId = -1;
        private ValueAnimatorCompat mAnimator;
        private Runnable mFlingRunnable;
        private boolean mIsBeingDragged;
        private int mLastMotionY;
        private WeakReference<View> mLastNestedScrollingChildRef;
        private int mOffsetDelta;
        private int mOffsetToChildIndexOnLayout = -1;
        private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
        private float mOffsetToChildIndexOnLayoutPerc;
        /* access modifiers changed from: private */
        public ScrollerCompat mScroller;
        private boolean mSkipNestedPreScroll;
        private int mTouchSlop = -1;

        private class FlingRunnable implements Runnable {
            private final AppBarLayout mLayout;
            private final CoordinatorLayout mParent;

            FlingRunnable(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
                this.mParent = coordinatorLayout;
                this.mLayout = appBarLayout;
            }

            public void run() {
                if (this.mLayout != null && Behavior.this.mScroller != null && Behavior.this.mScroller.computeScrollOffset()) {
                    Behavior behavior = Behavior.this;
                    behavior.setAppBarTopBottomOffset(this.mParent, this.mLayout, behavior.mScroller.getCurrY());
                    ViewCompat.postOnAnimation(this.mLayout, this);
                }
            }
        }

        protected static class SavedState extends BaseSavedState {
            public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel parcel) {
                    return new SavedState(parcel);
                }

                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            boolean firstVisibileChildAtMinimumHeight;
            float firstVisibileChildPercentageShown;
            int firstVisibleChildIndex;

            public SavedState(Parcel parcel) {
                super(parcel);
                this.firstVisibleChildIndex = parcel.readInt();
                this.firstVisibileChildPercentageShown = parcel.readFloat();
                this.firstVisibileChildAtMinimumHeight = parcel.readByte() != 0;
            }

            public SavedState(Parcelable parcelable) {
                super(parcelable);
            }

            public void writeToParcel(Parcel parcel, int i) {
                super.writeToParcel(parcel, i);
                parcel.writeInt(this.firstVisibleChildIndex);
                parcel.writeFloat(this.firstVisibileChildPercentageShown);
                parcel.writeByte(this.firstVisibileChildAtMinimumHeight ? (byte) 1 : 0);
            }
        }

        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i) {
            return super.setLeftAndRightOffset(i);
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i) {
            boolean z = (i & 2) != 0 && appBarLayout.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight();
            if (z) {
                ValueAnimatorCompat valueAnimatorCompat = this.mAnimator;
                if (valueAnimatorCompat != null) {
                    valueAnimatorCompat.cancel();
                }
            }
            this.mLastNestedScrollingChildRef = null;
            return z;
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i, int i2, int[] iArr) {
            int i3;
            int i4;
            if (i2 != 0 && !this.mSkipNestedPreScroll) {
                if (i2 < 0) {
                    int i5 = -appBarLayout.getTotalScrollRange();
                    i4 = i5;
                    i3 = appBarLayout.getDownNestedPreScrollRange() + i5;
                } else {
                    i4 = -appBarLayout.getUpNestedPreScrollRange();
                    i3 = 0;
                }
                iArr[1] = scroll(coordinatorLayout, appBarLayout, i2, i4, i3);
            }
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i, int i2, int i3, int i4) {
            if (i4 < 0) {
                scroll(coordinatorLayout, appBarLayout, i4, -appBarLayout.getDownNestedScrollRange(), 0);
                this.mSkipNestedPreScroll = true;
                return;
            }
            this.mSkipNestedPreScroll = false;
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view) {
            this.mSkipNestedPreScroll = false;
            this.mLastNestedScrollingChildRef = new WeakReference<>(view);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
            if (r0 != 3) goto L_0x0076;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout r5, android.support.design.widget.AppBarLayout r6, android.view.MotionEvent r7) {
            /*
                r4 = this;
                int r0 = r4.mTouchSlop
                if (r0 >= 0) goto L_0x0012
                android.content.Context r0 = r5.getContext()
                android.view.ViewConfiguration r0 = android.view.ViewConfiguration.get(r0)
                int r0 = r0.getScaledTouchSlop()
                r4.mTouchSlop = r0
            L_0x0012:
                int r0 = r7.getAction()
                r1 = 2
                r2 = 1
                if (r0 != r1) goto L_0x001f
                boolean r0 = r4.mIsBeingDragged
                if (r0 == 0) goto L_0x001f
                return r2
            L_0x001f:
                int r0 = android.support.p000v4.view.MotionEventCompat.getActionMasked(r7)
                r3 = 0
                if (r0 == 0) goto L_0x0056
                r5 = -1
                if (r0 == r2) goto L_0x0051
                if (r0 == r1) goto L_0x002f
                r6 = 3
                if (r0 == r6) goto L_0x0051
                goto L_0x0076
            L_0x002f:
                int r6 = r4.mActivePointerId
                if (r6 != r5) goto L_0x0034
                goto L_0x0076
            L_0x0034:
                int r6 = android.support.p000v4.view.MotionEventCompat.findPointerIndex(r7, r6)
                if (r6 != r5) goto L_0x003b
                goto L_0x0076
            L_0x003b:
                float r5 = android.support.p000v4.view.MotionEventCompat.getY(r7, r6)
                int r5 = (int) r5
                int r6 = r4.mLastMotionY
                int r6 = r5 - r6
                int r6 = java.lang.Math.abs(r6)
                int r7 = r4.mTouchSlop
                if (r6 <= r7) goto L_0x0076
                r4.mIsBeingDragged = r2
                r4.mLastMotionY = r5
                goto L_0x0076
            L_0x0051:
                r4.mIsBeingDragged = r3
                r4.mActivePointerId = r5
                goto L_0x0076
            L_0x0056:
                r4.mIsBeingDragged = r3
                float r0 = r7.getX()
                int r0 = (int) r0
                float r1 = r7.getY()
                int r1 = (int) r1
                boolean r5 = r5.isPointInChildBounds(r6, r0, r1)
                if (r5 == 0) goto L_0x0076
                boolean r5 = r4.canDragAppBarLayout()
                if (r5 == 0) goto L_0x0076
                r4.mLastMotionY = r1
                int r5 = android.support.p000v4.view.MotionEventCompat.getPointerId(r7, r3)
                r4.mActivePointerId = r5
            L_0x0076:
                boolean r5 = r4.mIsBeingDragged
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.AppBarLayout.Behavior.onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout, android.support.design.widget.AppBarLayout, android.view.MotionEvent):boolean");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
            if (r2 != 3) goto L_0x007f;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouchEvent(android.support.design.widget.CoordinatorLayout r11, android.support.design.widget.AppBarLayout r12, android.view.MotionEvent r13) {
            /*
                r10 = this;
                int r0 = r10.mTouchSlop
                if (r0 >= 0) goto L_0x0012
                android.content.Context r0 = r11.getContext()
                android.view.ViewConfiguration r0 = android.view.ViewConfiguration.get(r0)
                int r0 = r0.getScaledTouchSlop()
                r10.mTouchSlop = r0
            L_0x0012:
                float r0 = r13.getX()
                int r0 = (int) r0
                float r1 = r13.getY()
                int r1 = (int) r1
                int r2 = android.support.p000v4.view.MotionEventCompat.getActionMasked(r13)
                r3 = 1
                r4 = 0
                if (r2 == 0) goto L_0x006b
                r0 = -1
                if (r2 == r3) goto L_0x0066
                r1 = 2
                if (r2 == r1) goto L_0x002e
                r11 = 3
                if (r2 == r11) goto L_0x0066
                goto L_0x007f
            L_0x002e:
                int r1 = r10.mActivePointerId
                int r1 = android.support.p000v4.view.MotionEventCompat.findPointerIndex(r13, r1)
                if (r1 != r0) goto L_0x0037
                return r4
            L_0x0037:
                float r13 = android.support.p000v4.view.MotionEventCompat.getY(r13, r1)
                int r13 = (int) r13
                int r0 = r10.mLastMotionY
                int r0 = r0 - r13
                boolean r1 = r10.mIsBeingDragged
                if (r1 != 0) goto L_0x0052
                int r1 = java.lang.Math.abs(r0)
                int r2 = r10.mTouchSlop
                if (r1 <= r2) goto L_0x0052
                r10.mIsBeingDragged = r3
                if (r0 <= 0) goto L_0x0051
                int r0 = r0 - r2
                goto L_0x0052
            L_0x0051:
                int r0 = r0 + r2
            L_0x0052:
                r7 = r0
                boolean r0 = r10.mIsBeingDragged
                if (r0 == 0) goto L_0x007f
                r10.mLastMotionY = r13
                int r13 = r12.getDownNestedScrollRange()
                int r8 = -r13
                r9 = 0
                r4 = r10
                r5 = r11
                r6 = r12
                r4.scroll(r5, r6, r7, r8, r9)
                goto L_0x007f
            L_0x0066:
                r10.mIsBeingDragged = r4
                r10.mActivePointerId = r0
                goto L_0x007f
            L_0x006b:
                boolean r11 = r11.isPointInChildBounds(r12, r0, r1)
                if (r11 == 0) goto L_0x0080
                boolean r11 = r10.canDragAppBarLayout()
                if (r11 == 0) goto L_0x0080
                r10.mLastMotionY = r1
                int r11 = android.support.p000v4.view.MotionEventCompat.getPointerId(r13, r4)
                r10.mActivePointerId = r11
            L_0x007f:
                return r3
            L_0x0080:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.AppBarLayout.Behavior.onTouchEvent(android.support.design.widget.CoordinatorLayout, android.support.design.widget.AppBarLayout, android.view.MotionEvent):boolean");
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, float f, float f2, boolean z) {
            int i;
            if (!z) {
                return fling(coordinatorLayout, appBarLayout, -appBarLayout.getTotalScrollRange(), 0, -f2);
            }
            if (f2 < 0.0f) {
                i = (-appBarLayout.getTotalScrollRange()) + appBarLayout.getDownNestedPreScrollRange();
                if (getTopBottomOffsetForScrollingSibling() > i) {
                    return false;
                }
            } else {
                i = -appBarLayout.getUpNestedPreScrollRange();
                if (getTopBottomOffsetForScrollingSibling() < i) {
                    return false;
                }
            }
            if (getTopBottomOffsetForScrollingSibling() == i) {
                return false;
            }
            animateOffsetTo(coordinatorLayout, appBarLayout, i);
            return true;
        }

        private void animateOffsetTo(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, int i) {
            ValueAnimatorCompat valueAnimatorCompat = this.mAnimator;
            if (valueAnimatorCompat == null) {
                this.mAnimator = ViewUtils.createAnimator();
                this.mAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.mAnimator.setUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        Behavior.this.setAppBarTopBottomOffset(coordinatorLayout, appBarLayout, valueAnimatorCompat.getAnimatedIntValue());
                    }
                });
            } else {
                valueAnimatorCompat.cancel();
            }
            this.mAnimator.setIntValues(getTopBottomOffsetForScrollingSibling(), i);
            this.mAnimator.start();
        }

        private boolean fling(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i, int i2, float f) {
            Runnable runnable = this.mFlingRunnable;
            if (runnable != null) {
                appBarLayout.removeCallbacks(runnable);
            }
            if (this.mScroller == null) {
                this.mScroller = ScrollerCompat.create(appBarLayout.getContext());
            }
            this.mScroller.fling(0, getTopBottomOffsetForScrollingSibling(), 0, Math.round(f), 0, 0, i, i2);
            if (this.mScroller.computeScrollOffset()) {
                this.mFlingRunnable = new FlingRunnable(coordinatorLayout, appBarLayout);
                ViewCompat.postOnAnimation(appBarLayout, this.mFlingRunnable);
                return true;
            }
            this.mFlingRunnable = null;
            return false;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i) {
            int i2;
            boolean onLayoutChild = super.onLayoutChild(coordinatorLayout, appBarLayout, i);
            int pendingAction = appBarLayout.getPendingAction();
            if (pendingAction != 0) {
                boolean z = (pendingAction & 4) != 0;
                if ((pendingAction & 2) != 0) {
                    int i3 = -appBarLayout.getUpNestedPreScrollRange();
                    if (z) {
                        animateOffsetTo(coordinatorLayout, appBarLayout, i3);
                    } else {
                        setAppBarTopBottomOffset(coordinatorLayout, appBarLayout, i3);
                    }
                } else if ((pendingAction & 1) != 0) {
                    if (z) {
                        animateOffsetTo(coordinatorLayout, appBarLayout, 0);
                    } else {
                        setAppBarTopBottomOffset(coordinatorLayout, appBarLayout, 0);
                    }
                }
                appBarLayout.resetPendingAction();
            } else {
                int i4 = this.mOffsetToChildIndexOnLayout;
                if (i4 >= 0) {
                    View childAt = appBarLayout.getChildAt(i4);
                    int i5 = -childAt.getBottom();
                    if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
                        i2 = ViewCompat.getMinimumHeight(childAt);
                    } else {
                        i2 = Math.round(((float) childAt.getHeight()) * this.mOffsetToChildIndexOnLayoutPerc);
                    }
                    setTopAndBottomOffset(i5 + i2);
                    this.mOffsetToChildIndexOnLayout = -1;
                }
            }
            dispatchOffsetUpdates(appBarLayout);
            return onLayoutChild;
        }

        private int scroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i, int i2, int i3) {
            return setAppBarTopBottomOffset(coordinatorLayout, appBarLayout, getTopBottomOffsetForScrollingSibling() - i, i2, i3);
        }

        private boolean canDragAppBarLayout() {
            WeakReference<View> weakReference = this.mLastNestedScrollingChildRef;
            if (weakReference == null) {
                return false;
            }
            View view = (View) weakReference.get();
            if (view == null || !view.isShown() || ViewCompat.canScrollVertically(view, -1)) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: 0000 */
        public final int setAppBarTopBottomOffset(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i) {
            return setAppBarTopBottomOffset(coordinatorLayout, appBarLayout, i, Integer.MIN_VALUE, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        }

        /* access modifiers changed from: 0000 */
        public final int setAppBarTopBottomOffset(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i, int i2, int i3) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            if (i2 != 0 && topBottomOffsetForScrollingSibling >= i2 && topBottomOffsetForScrollingSibling <= i3) {
                int constrain = MathUtils.constrain(i, i2, i3);
                if (topBottomOffsetForScrollingSibling != constrain) {
                    int interpolateOffset = appBarLayout.hasChildWithInterpolator() ? interpolateOffset(appBarLayout, constrain) : constrain;
                    boolean topAndBottomOffset = setTopAndBottomOffset(interpolateOffset);
                    int i4 = topBottomOffsetForScrollingSibling - constrain;
                    this.mOffsetDelta = constrain - interpolateOffset;
                    if (!topAndBottomOffset && appBarLayout.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
                    }
                    dispatchOffsetUpdates(appBarLayout);
                    return i4;
                }
            }
            return 0;
        }

        private void dispatchOffsetUpdates(AppBarLayout appBarLayout) {
            List access$200 = appBarLayout.mListeners;
            int size = access$200.size();
            for (int i = 0; i < size; i++) {
                OnOffsetChangedListener onOffsetChangedListener = (OnOffsetChangedListener) access$200.get(i);
                if (onOffsetChangedListener != null) {
                    onOffsetChangedListener.onOffsetChanged(appBarLayout, getTopAndBottomOffset());
                }
            }
        }

        private int interpolateOffset(AppBarLayout appBarLayout, int i) {
            int abs = Math.abs(i);
            int childCount = appBarLayout.getChildCount();
            int i2 = 0;
            int i3 = 0;
            while (true) {
                if (i3 >= childCount) {
                    break;
                }
                View childAt = appBarLayout.getChildAt(i3);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Interpolator scrollInterpolator = layoutParams.getScrollInterpolator();
                if (abs < childAt.getTop() || abs > childAt.getBottom()) {
                    i3++;
                } else if (scrollInterpolator != null) {
                    int scrollFlags = layoutParams.getScrollFlags();
                    if ((scrollFlags & 1) != 0) {
                        i2 = 0 + childAt.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                        if ((scrollFlags & 2) != 0) {
                            i2 -= ViewCompat.getMinimumHeight(childAt);
                        }
                    }
                    if (i2 > 0) {
                        float f = (float) i2;
                        return Integer.signum(i) * (childAt.getTop() + Math.round(f * scrollInterpolator.getInterpolation(((float) (abs - childAt.getTop())) / f)));
                    }
                }
            }
            return i;
        }

        /* access modifiers changed from: 0000 */
        public final int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + this.mOffsetDelta;
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            Parcelable onSaveInstanceState = super.onSaveInstanceState(coordinatorLayout, appBarLayout);
            int topAndBottomOffset = getTopAndBottomOffset();
            int childCount = appBarLayout.getChildCount();
            boolean z = false;
            int i = 0;
            while (i < childCount) {
                View childAt = appBarLayout.getChildAt(i);
                int bottom = childAt.getBottom() + topAndBottomOffset;
                if (childAt.getTop() + topAndBottomOffset > 0 || bottom < 0) {
                    i++;
                } else {
                    SavedState savedState = new SavedState(onSaveInstanceState);
                    savedState.firstVisibleChildIndex = i;
                    if (bottom == ViewCompat.getMinimumHeight(childAt)) {
                        z = true;
                    }
                    savedState.firstVisibileChildAtMinimumHeight = z;
                    savedState.firstVisibileChildPercentageShown = ((float) bottom) / ((float) childAt.getHeight());
                    return savedState;
                }
            }
            return onSaveInstanceState;
        }

        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, Parcelable parcelable) {
            if (parcelable instanceof SavedState) {
                SavedState savedState = (SavedState) parcelable;
                super.onRestoreInstanceState(coordinatorLayout, appBarLayout, savedState.getSuperState());
                this.mOffsetToChildIndexOnLayout = savedState.firstVisibleChildIndex;
                this.mOffsetToChildIndexOnLayoutPerc = savedState.firstVisibileChildPercentageShown;
                this.mOffsetToChildIndexOnLayoutIsMinHeight = savedState.firstVisibileChildAtMinimumHeight;
                return;
            }
            super.onRestoreInstanceState(coordinatorLayout, appBarLayout, parcelable);
            this.mOffsetToChildIndexOnLayout = -1;
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        static final int FLAG_QUICK_RETURN = 5;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_SCROLL = 1;
        int mScrollFlags = 1;
        Interpolator mScrollInterpolator;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollFlags {
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.AppBarLayout_LayoutParams);
            this.mScrollFlags = obtainStyledAttributes.getInt(C0010R.styleable.AppBarLayout_LayoutParams_layout_scrollFlags, 0);
            if (obtainStyledAttributes.hasValue(C0010R.styleable.AppBarLayout_LayoutParams_layout_scrollInterpolator)) {
                this.mScrollInterpolator = AnimationUtils.loadInterpolator(context, obtainStyledAttributes.getResourceId(C0010R.styleable.AppBarLayout_LayoutParams_layout_scrollInterpolator, 0));
            }
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2, f);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(android.widget.LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.mScrollFlags = layoutParams.mScrollFlags;
            this.mScrollInterpolator = layoutParams.mScrollInterpolator;
        }

        public void setScrollFlags(int i) {
            this.mScrollFlags = i;
        }

        public int getScrollFlags() {
            return this.mScrollFlags;
        }

        public void setScrollInterpolator(Interpolator interpolator) {
            this.mScrollInterpolator = interpolator;
        }

        public Interpolator getScrollInterpolator() {
            return this.mScrollInterpolator;
        }
    }

    public interface OnOffsetChangedListener {
        void onOffsetChanged(AppBarLayout appBarLayout, int i);
    }

    public static class ScrollingViewBehavior extends ViewOffsetBehavior<View> {
        private int mOverlayTop;

        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            return super.onLayoutChild(coordinatorLayout, view, i);
        }

        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i) {
            return super.setLeftAndRightOffset(i);
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.ScrollingViewBehavior_Params);
            this.mOverlayTop = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.ScrollingViewBehavior_Params_behavior_overlapTop, 0);
            obtainStyledAttributes.recycle();
        }

        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return view2 instanceof AppBarLayout;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int i4) {
            int i5 = view.getLayoutParams().height;
            if (i5 == -1 || i5 == -2) {
                List dependencies = coordinatorLayout.getDependencies(view);
                if (dependencies.isEmpty()) {
                    return false;
                }
                AppBarLayout findFirstAppBarLayout = findFirstAppBarLayout(dependencies);
                if (findFirstAppBarLayout != null && ViewCompat.isLaidOut(findFirstAppBarLayout)) {
                    if (ViewCompat.getFitsSystemWindows(findFirstAppBarLayout)) {
                        View view2 = view;
                        ViewCompat.setFitsSystemWindows(view, true);
                    } else {
                        View view3 = view;
                    }
                    int size = MeasureSpec.getSize(i3);
                    if (size == 0) {
                        size = coordinatorLayout.getHeight();
                    }
                    coordinatorLayout.onMeasureChild(view, i, i2, MeasureSpec.makeMeasureSpec((size - findFirstAppBarLayout.getMeasuredHeight()) + findFirstAppBarLayout.getTotalScrollRange(), i5 == -1 ? 1073741824 : Integer.MIN_VALUE), i4);
                    return true;
                }
            }
            return false;
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) view2.getLayoutParams()).getBehavior();
            if (behavior instanceof Behavior) {
                int topBottomOffsetForScrollingSibling = ((Behavior) behavior).getTopBottomOffsetForScrollingSibling();
                int height = view2.getHeight() - this.mOverlayTop;
                int height2 = coordinatorLayout.getHeight() - view.getHeight();
                if (this.mOverlayTop == 0 || !(view2 instanceof AppBarLayout)) {
                    setTopAndBottomOffset((view2.getHeight() - this.mOverlayTop) + topBottomOffsetForScrollingSibling);
                } else {
                    setTopAndBottomOffset(AnimationUtils.lerp(height, height2, ((float) Math.abs(topBottomOffsetForScrollingSibling)) / ((float) ((AppBarLayout) view2).getTotalScrollRange())));
                }
            }
            return false;
        }

        public void setOverlayTop(int i) {
            this.mOverlayTop = i;
        }

        public int getOverlayTop() {
            return this.mOverlayTop;
        }

        private static AppBarLayout findFirstAppBarLayout(List<View> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                View view = (View) list.get(i);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout) view;
                }
            }
            return null;
        }
    }

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
        this.mPendingAction = 0;
        setOrientation(1);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.AppBarLayout, 0, C0010R.style.Widget_Design_AppBarLayout);
        this.mTargetElevation = (float) obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.AppBarLayout_elevation, 0);
        setBackgroundDrawable(obtainStyledAttributes.getDrawable(C0010R.styleable.AppBarLayout_android_background));
        if (obtainStyledAttributes.hasValue(C0010R.styleable.AppBarLayout_expanded)) {
            setExpanded(obtainStyledAttributes.getBoolean(C0010R.styleable.AppBarLayout_expanded, false));
        }
        obtainStyledAttributes.recycle();
        ViewUtils.setBoundsViewOutlineProvider(this);
        this.mListeners = new ArrayList();
        ViewCompat.setElevation(this, this.mTargetElevation);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                AppBarLayout.this.setWindowInsets(windowInsetsCompat);
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (onOffsetChangedListener != null && !this.mListeners.contains(onOffsetChangedListener)) {
            this.mListeners.add(onOffsetChangedListener);
        }
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (onOffsetChangedListener != null) {
            this.mListeners.remove(onOffsetChangedListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mHaveChildWithInterpolator = false;
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            if (((LayoutParams) getChildAt(i5).getLayoutParams()).getScrollInterpolator() != null) {
                this.mHaveChildWithInterpolator = true;
                return;
            }
        }
    }

    public void setOrientation(int i) {
        if (i == 1) {
            super.setOrientation(i);
            return;
        }
        throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
    }

    public void setExpanded(boolean z) {
        setExpanded(z, ViewCompat.isLaidOut(this));
    }

    public void setExpanded(boolean z, boolean z2) {
        this.mPendingAction = (z ? 1 : 2) | (z2 ? 4 : 0);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof android.widget.LinearLayout.LayoutParams) {
            return new LayoutParams((android.widget.LinearLayout.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: 0000 */
    public final boolean hasChildWithInterpolator() {
        return this.mHaveChildWithInterpolator;
    }

    public final int getTotalScrollRange() {
        int i = this.mTotalScrollRange;
        if (i != -1) {
            return i;
        }
        int childCount = getChildCount();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= childCount) {
                break;
            }
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int height = ViewCompat.isLaidOut(childAt) ? childAt.getHeight() : childAt.getMeasuredHeight();
            int i5 = layoutParams.mScrollFlags;
            if ((i5 & 1) == 0) {
                break;
            }
            i4 += height + layoutParams.topMargin + layoutParams.bottomMargin;
            if ((i5 & 2) != 0) {
                i4 -= ViewCompat.getMinimumHeight(childAt);
                break;
            }
            i3++;
        }
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        if (windowInsetsCompat != null) {
            i2 = windowInsetsCompat.getSystemWindowInsetTop();
        }
        int i6 = i4 - i2;
        this.mTotalScrollRange = i6;
        return i6;
    }

    /* access modifiers changed from: 0000 */
    public final boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    /* access modifiers changed from: 0000 */
    public final int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    /* access modifiers changed from: 0000 */
    public final int getDownNestedPreScrollRange() {
        int i = this.mDownPreScrollRange;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int height = ViewCompat.isLaidOut(childAt) ? childAt.getHeight() : childAt.getMeasuredHeight();
            int i3 = layoutParams.mScrollFlags;
            if ((i3 & 5) == 5) {
                int i4 = i2 + layoutParams.topMargin + layoutParams.bottomMargin;
                i2 = (i3 & 8) != 0 ? i4 + ViewCompat.getMinimumHeight(childAt) : i4 + height;
            } else if (i2 > 0) {
                break;
            }
        }
        this.mDownPreScrollRange = i2;
        return i2;
    }

    /* access modifiers changed from: 0000 */
    public final int getDownNestedScrollRange() {
        int i = this.mDownScrollRange;
        if (i != -1) {
            return i;
        }
        int childCount = getChildCount();
        int i2 = 0;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int height = (ViewCompat.isLaidOut(childAt) ? childAt.getHeight() : childAt.getMeasuredHeight()) + layoutParams.topMargin + layoutParams.bottomMargin;
            int i4 = layoutParams.mScrollFlags;
            if ((i4 & 1) == 0) {
                break;
            }
            i2 += height;
            if ((i4 & 2) != 0) {
                return i2 - ViewCompat.getMinimumHeight(childAt);
            }
        }
        this.mDownScrollRange = i2;
        return i2;
    }

    /* access modifiers changed from: 0000 */
    public final int getMinimumHeightForVisibleOverlappingContent() {
        WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
        int i = 0;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        if (minimumHeight != 0) {
            return (minimumHeight * 2) + systemWindowInsetTop;
        }
        int childCount = getChildCount();
        if (childCount >= 1) {
            i = (ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) * 2) + systemWindowInsetTop;
        }
        return i;
    }

    public void setTargetElevation(float f) {
        this.mTargetElevation = f;
    }

    public float getTargetElevation() {
        return this.mTargetElevation;
    }

    /* access modifiers changed from: 0000 */
    public int getPendingAction() {
        return this.mPendingAction;
    }

    /* access modifiers changed from: 0000 */
    public void resetPendingAction() {
        this.mPendingAction = 0;
    }

    /* access modifiers changed from: private */
    public void setWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        this.mTotalScrollRange = -1;
        this.mLastInsets = windowInsetsCompat;
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            windowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(getChildAt(i), windowInsetsCompat);
            if (!windowInsetsCompat.isConsumed()) {
                i++;
            } else {
                return;
            }
        }
    }
}
