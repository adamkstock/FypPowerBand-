package android.support.design.widget;

import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.p000v4.view.MotionEventCompat;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.widget.ViewDragHelper;
import android.support.p000v4.widget.ViewDragHelper.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SwipeDismissBehavior<V extends View> extends Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    /* access modifiers changed from: private */
    public float mAlphaEndSwipeDistance = 0.5f;
    /* access modifiers changed from: private */
    public float mAlphaStartSwipeDistance = 0.0f;
    private final Callback mDragCallback = new Callback() {
        private int mOriginalCapturedViewLeft;

        public boolean tryCaptureView(View view, int i) {
            this.mOriginalCapturedViewLeft = view.getLeft();
            return true;
        }

        public void onViewDragStateChanged(int i) {
            if (SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDragStateChanged(i);
            }
        }

        public void onViewReleased(View view, float f, float f2) {
            boolean z;
            int i;
            int width = view.getWidth();
            if (shouldDismiss(view, f)) {
                int left = view.getLeft();
                int i2 = this.mOriginalCapturedViewLeft;
                i = left < i2 ? i2 - width : i2 + width;
                z = true;
            } else {
                i = this.mOriginalCapturedViewLeft;
                z = false;
            }
            if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(i, view.getTop())) {
                ViewCompat.postOnAnimation(view, new SettleRunnable(view, z));
            } else if (z && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(view);
            }
        }

        private boolean shouldDismiss(View view, float f) {
            boolean z = false;
            int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            if (i != 0) {
                boolean z2 = ViewCompat.getLayoutDirection(view) == 1;
                if (SwipeDismissBehavior.this.mSwipeDirection == 2) {
                    return true;
                }
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    return z2 ? z : z;
                    z = true;
                }
                if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
                    if (z2) {
                    }
                    z = true;
                }
                return z;
            }
            if (Math.abs(view.getLeft() - this.mOriginalCapturedViewLeft) >= Math.round(((float) view.getWidth()) * SwipeDismissBehavior.this.mDragDismissThreshold)) {
                z = true;
            }
            return z;
        }

        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            int i3;
            int i4;
            int width;
            boolean z = ViewCompat.getLayoutDirection(view) == 1;
            if (SwipeDismissBehavior.this.mSwipeDirection != 0) {
                if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
                    i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                    i4 = view.getWidth() + this.mOriginalCapturedViewLeft;
                } else if (z) {
                    i3 = this.mOriginalCapturedViewLeft;
                    width = view.getWidth();
                } else {
                    i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                    i4 = this.mOriginalCapturedViewLeft;
                }
                return SwipeDismissBehavior.clamp(i3, i, i4);
            } else if (z) {
                i3 = this.mOriginalCapturedViewLeft - view.getWidth();
                i4 = this.mOriginalCapturedViewLeft;
                return SwipeDismissBehavior.clamp(i3, i, i4);
            } else {
                i3 = this.mOriginalCapturedViewLeft;
                width = view.getWidth();
            }
            i4 = width + i3;
            return SwipeDismissBehavior.clamp(i3, i, i4);
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return view.getTop();
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            float width = ((float) this.mOriginalCapturedViewLeft) + (((float) view.getWidth()) * SwipeDismissBehavior.this.mAlphaStartSwipeDistance);
            float width2 = ((float) this.mOriginalCapturedViewLeft) + (((float) view.getWidth()) * SwipeDismissBehavior.this.mAlphaEndSwipeDistance);
            float f = (float) i;
            if (f <= width) {
                ViewCompat.setAlpha(view, 1.0f);
            } else if (f >= width2) {
                ViewCompat.setAlpha(view, 0.0f);
            } else {
                ViewCompat.setAlpha(view, SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(width, width2, f), 1.0f));
            }
        }
    };
    /* access modifiers changed from: private */
    public float mDragDismissThreshold = 0.5f;
    private boolean mIgnoreEvents;
    /* access modifiers changed from: private */
    public OnDismissListener mListener;
    private float mSensitivity = 0.0f;
    private boolean mSensitivitySet;
    /* access modifiers changed from: private */
    public int mSwipeDirection = 2;
    /* access modifiers changed from: private */
    public ViewDragHelper mViewDragHelper;

    public interface OnDismissListener {
        void onDismiss(View view);

        void onDragStateChanged(int i);
    }

    private class SettleRunnable implements Runnable {
        private final boolean mDismiss;
        private final View mView;

        SettleRunnable(View view, boolean z) {
            this.mView = view;
            this.mDismiss = z;
        }

        public void run() {
            if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
            } else if (this.mDismiss && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
            }
        }
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.mListener = onDismissListener;
    }

    public void setSwipeDirection(int i) {
        this.mSwipeDirection = i;
    }

    public void setDragDismissDistance(float f) {
        this.mDragDismissThreshold = clamp(0.0f, f, 1.0f);
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.mAlphaStartSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.mAlphaEndSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setSensitivity(float f) {
        this.mSensitivity = f;
        this.mSensitivitySet = true;
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked != 1 && actionMasked != 3) {
            this.mIgnoreEvents = !coordinatorLayout.isPointInChildBounds(v, (int) motionEvent.getX(), (int) motionEvent.getY());
        } else if (this.mIgnoreEvents) {
            this.mIgnoreEvents = false;
            return false;
        }
        if (this.mIgnoreEvents) {
            return false;
        }
        ensureViewDragHelper(coordinatorLayout);
        return this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper = this.mViewDragHelper;
        if (viewDragHelper == null) {
            return false;
        }
        viewDragHelper.processTouchEvent(motionEvent);
        return true;
    }

    private void ensureViewDragHelper(ViewGroup viewGroup) {
        if (this.mViewDragHelper == null) {
            this.mViewDragHelper = this.mSensitivitySet ? ViewDragHelper.create(viewGroup, this.mSensitivity, this.mDragCallback) : ViewDragHelper.create(viewGroup, this.mDragCallback);
        }
    }

    /* access modifiers changed from: private */
    public static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    /* access modifiers changed from: private */
    public static int clamp(int i, int i2, int i3) {
        return Math.min(Math.max(i, i2), i3);
    }

    public int getDragState() {
        ViewDragHelper viewDragHelper = this.mViewDragHelper;
        if (viewDragHelper != null) {
            return viewDragHelper.getViewDragState();
        }
        return 0;
    }
}
