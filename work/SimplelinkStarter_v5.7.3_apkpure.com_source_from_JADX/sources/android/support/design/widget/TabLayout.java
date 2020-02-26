package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.C0010R;
import android.support.p000v4.view.GravityCompat;
import android.support.p000v4.view.PagerAdapter;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.view.ViewPager;
import android.support.p000v4.view.ViewPager.OnPageChangeListener;
import android.support.p003v7.internal.widget.TintManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class TabLayout extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int MOTION_NON_ADJACENT_OFFSET = 24;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private int mContentInsetStart;
    /* access modifiers changed from: private */
    public ValueAnimatorCompat mIndicatorAnimator;
    /* access modifiers changed from: private */
    public int mMode;
    private OnTabSelectedListener mOnTabSelectedListener;
    private final int mRequestedTabMaxWidth;
    private ValueAnimatorCompat mScrollAnimator;
    private Tab mSelectedTab;
    /* access modifiers changed from: private */
    public final int mTabBackgroundResId;
    private OnClickListener mTabClickListener;
    /* access modifiers changed from: private */
    public int mTabGravity;
    /* access modifiers changed from: private */
    public int mTabMaxWidth;
    /* access modifiers changed from: private */
    public final int mTabMinWidth;
    /* access modifiers changed from: private */
    public int mTabPaddingBottom;
    /* access modifiers changed from: private */
    public int mTabPaddingEnd;
    /* access modifiers changed from: private */
    public int mTabPaddingStart;
    /* access modifiers changed from: private */
    public int mTabPaddingTop;
    private final SlidingTabStrip mTabStrip;
    /* access modifiers changed from: private */
    public int mTabTextAppearance;
    /* access modifiers changed from: private */
    public ColorStateList mTabTextColors;
    private final ArrayList<Tab> mTabs;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public interface OnTabSelectedListener {
        void onTabReselected(Tab tab);

        void onTabSelected(Tab tab);

        void onTabUnselected(Tab tab);
    }

    private class SlidingTabStrip extends LinearLayout {
        private int mIndicatorLeft = -1;
        private int mIndicatorRight = -1;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        /* access modifiers changed from: private */
        public int mSelectedPosition = -1;
        /* access modifiers changed from: private */
        public float mSelectionOffset;

        SlidingTabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            this.mSelectedIndicatorPaint = new Paint();
        }

        /* access modifiers changed from: 0000 */
        public void setSelectedIndicatorColor(int i) {
            if (this.mSelectedIndicatorPaint.getColor() != i) {
                this.mSelectedIndicatorPaint.setColor(i);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setSelectedIndicatorHeight(int i) {
            if (this.mSelectedIndicatorHeight != i) {
                this.mSelectedIndicatorHeight = i;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean childrenNeedLayout() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void setIndicatorPositionFromTabPosition(int i, float f) {
            this.mSelectedPosition = i;
            this.mSelectionOffset = f;
            updateIndicatorPosition();
        }

        /* access modifiers changed from: 0000 */
        public float getIndicatorPosition() {
            return ((float) this.mSelectedPosition) + this.mSelectionOffset;
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (MeasureSpec.getMode(i) == 1073741824 && TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
                int childCount = getChildCount();
                int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                int i3 = 0;
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = getChildAt(i4);
                    childAt.measure(makeMeasureSpec, i2);
                    i3 = Math.max(i3, childAt.getMeasuredWidth());
                }
                if (i3 > 0) {
                    if (i3 * childCount <= getMeasuredWidth() - (TabLayout.this.dpToPx(16) * 2)) {
                        for (int i5 = 0; i5 < childCount; i5++) {
                            LayoutParams layoutParams = (LayoutParams) getChildAt(i5).getLayoutParams();
                            layoutParams.width = i3;
                            layoutParams.weight = 0.0f;
                        }
                    } else {
                        TabLayout.this.mTabGravity = 0;
                        TabLayout.this.updateTabViewsLayoutParams();
                    }
                    super.onMeasure(i, i2);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            updateIndicatorPosition();
        }

        private void updateIndicatorPosition() {
            int i;
            int i2;
            View childAt = getChildAt(this.mSelectedPosition);
            if (childAt == null || childAt.getWidth() <= 0) {
                i = -1;
                i2 = -1;
            } else {
                i = childAt.getLeft();
                i2 = childAt.getRight();
                if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                    View childAt2 = getChildAt(this.mSelectedPosition + 1);
                    float left = this.mSelectionOffset * ((float) childAt2.getLeft());
                    float f = this.mSelectionOffset;
                    i = (int) (left + ((1.0f - f) * ((float) i)));
                    i2 = (int) ((f * ((float) childAt2.getRight())) + ((1.0f - this.mSelectionOffset) * ((float) i2)));
                }
            }
            setIndicatorPosition(i, i2);
        }

        /* access modifiers changed from: private */
        public void setIndicatorPosition(int i, int i2) {
            if (i != this.mIndicatorLeft || i2 != this.mIndicatorRight) {
                this.mIndicatorLeft = i;
                this.mIndicatorRight = i2;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void animateIndicatorToPosition(final int i, int i2) {
            final int i3;
            final int i4;
            boolean z = ViewCompat.getLayoutDirection(this) == 1;
            View childAt = getChildAt(i);
            final int left = childAt.getLeft();
            final int right = childAt.getRight();
            if (Math.abs(i - this.mSelectedPosition) <= 1) {
                i4 = this.mIndicatorLeft;
                i3 = this.mIndicatorRight;
            } else {
                int access$1300 = TabLayout.this.dpToPx(24);
                i4 = (i >= this.mSelectedPosition ? !z : z) ? left - access$1300 : access$1300 + right;
                i3 = i4;
            }
            if (i4 != left || i3 != right) {
                ValueAnimatorCompat access$1502 = TabLayout.this.mIndicatorAnimator = ViewUtils.createAnimator();
                access$1502.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                access$1502.setDuration(i2);
                access$1502.setFloatValues(0.0f, 1.0f);
                C00411 r3 = new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        float animatedFraction = valueAnimatorCompat.getAnimatedFraction();
                        SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(i4, left, animatedFraction), AnimationUtils.lerp(i3, right, animatedFraction));
                    }
                };
                access$1502.setUpdateListener(r3);
                access$1502.setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = i;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }

                    public void onAnimationCancel(ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = i;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }
                });
                access$1502.start();
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            int i = this.mIndicatorLeft;
            if (i >= 0 && this.mIndicatorRight > i) {
                canvas.drawRect((float) i, (float) (getHeight() - this.mSelectedIndicatorHeight), (float) this.mIndicatorRight, (float) getHeight(), this.mSelectedIndicatorPaint);
            }
        }
    }

    public static final class Tab {
        public static final int INVALID_POSITION = -1;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        /* access modifiers changed from: private */
        public final TabLayout mParent;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;

        Tab(TabLayout tabLayout) {
            this.mParent = tabLayout;
        }

        public Object getTag() {
            return this.mTag;
        }

        public Tab setTag(Object obj) {
            this.mTag = obj;
            return this;
        }

        public View getCustomView() {
            return this.mCustomView;
        }

        public Tab setCustomView(View view) {
            this.mCustomView = view;
            int i = this.mPosition;
            if (i >= 0) {
                this.mParent.updateTab(i);
            }
            return this;
        }

        public Tab setCustomView(int i) {
            return setCustomView(LayoutInflater.from(this.mParent.getContext()).inflate(i, null));
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public int getPosition() {
            return this.mPosition;
        }

        /* access modifiers changed from: 0000 */
        public void setPosition(int i) {
            this.mPosition = i;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public Tab setIcon(Drawable drawable) {
            this.mIcon = drawable;
            int i = this.mPosition;
            if (i >= 0) {
                this.mParent.updateTab(i);
            }
            return this;
        }

        public Tab setIcon(int i) {
            return setIcon(TintManager.getDrawable(this.mParent.getContext(), i));
        }

        public Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            int i = this.mPosition;
            if (i >= 0) {
                this.mParent.updateTab(i);
            }
            return this;
        }

        public Tab setText(int i) {
            return setText(this.mParent.getResources().getText(i));
        }

        public void select() {
            this.mParent.selectTab(this);
        }

        public boolean isSelected() {
            return this.mParent.getSelectedTabPosition() == this.mPosition;
        }

        public Tab setContentDescription(int i) {
            return setContentDescription(this.mParent.getResources().getText(i));
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.mContentDesc = charSequence;
            int i = this.mPosition;
            if (i >= 0) {
                this.mParent.updateTab(i);
            }
            return this;
        }

        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }

    public static class TabLayoutOnPageChangeListener implements OnPageChangeListener {
        private int mPendingSelection = -1;
        private int mScrollState;
        private final WeakReference<TabLayout> mTabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        public void onPageScrollStateChanged(int i) {
            this.mScrollState = i;
        }

        public void onPageScrolled(int i, float f, int i2) {
            TabLayout tabLayout = (TabLayout) this.mTabLayoutRef.get();
            if (tabLayout != null) {
                if (this.mPendingSelection == -1 || tabLayout.getScrollPosition() != ((float) this.mPendingSelection)) {
                    tabLayout.setScrollPosition(i, f, true);
                }
                if (this.mScrollState == 0) {
                    int i3 = this.mPendingSelection;
                    if (i3 != -1) {
                        tabLayout.selectTab(tabLayout.getTabAt(i3));
                        this.mPendingSelection = -1;
                    }
                }
            }
        }

        public void onPageSelected(int i) {
            this.mPendingSelection = i;
        }
    }

    class TabView extends LinearLayout implements OnLongClickListener {
        private ImageView mCustomIconView;
        private TextView mCustomTextView;
        private View mCustomView;
        private ImageView mIconView;
        private final Tab mTab;
        private TextView mTextView;

        public TabView(Context context, Tab tab) {
            super(context);
            this.mTab = tab;
            if (TabLayout.this.mTabBackgroundResId != 0) {
                setBackgroundDrawable(TintManager.getDrawable(context, TabLayout.this.mTabBackgroundResId));
            }
            ViewCompat.setPaddingRelative(this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
            setGravity(17);
            update();
        }

        public void setSelected(boolean z) {
            boolean z2 = isSelected() != z;
            super.setSelected(z);
            if (z2 && z) {
                sendAccessibilityEvent(4);
                TextView textView = this.mTextView;
                if (textView != null) {
                    textView.setSelected(z);
                }
                ImageView imageView = this.mIconView;
                if (imageView != null) {
                    imageView.setSelected(z);
                }
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName(android.support.p003v7.app.ActionBar.Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(android.support.p003v7.app.ActionBar.Tab.class.getName());
        }

        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int measuredWidth = getMeasuredWidth();
            if (measuredWidth < TabLayout.this.mTabMinWidth || measuredWidth > TabLayout.this.mTabMaxWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(MathUtils.constrain(measuredWidth, TabLayout.this.mTabMinWidth, TabLayout.this.mTabMaxWidth), 1073741824), i2);
            }
        }

        /* access modifiers changed from: 0000 */
        public final void update() {
            Tab tab = this.mTab;
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(customView);
                    }
                    addView(customView);
                }
                this.mCustomView = customView;
                TextView textView = this.mTextView;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.mIconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                this.mCustomTextView = (TextView) customView.findViewById(16908308);
                this.mCustomIconView = (ImageView) customView.findViewById(16908294);
            } else {
                View view = this.mCustomView;
                if (view != null) {
                    removeView(view);
                    this.mCustomView = null;
                }
                this.mCustomTextView = null;
                this.mCustomIconView = null;
            }
            if (this.mCustomView == null) {
                if (this.mIconView == null) {
                    ImageView imageView2 = (ImageView) LayoutInflater.from(getContext()).inflate(C0010R.layout.design_layout_tab_icon, this, false);
                    addView(imageView2, 0);
                    this.mIconView = imageView2;
                }
                if (this.mTextView == null) {
                    TextView textView2 = (TextView) LayoutInflater.from(getContext()).inflate(C0010R.layout.design_layout_tab_text, this, false);
                    addView(textView2);
                    this.mTextView = textView2;
                }
                this.mTextView.setTextAppearance(getContext(), TabLayout.this.mTabTextAppearance);
                if (TabLayout.this.mTabTextColors != null) {
                    this.mTextView.setTextColor(TabLayout.this.mTabTextColors);
                }
                updateTextAndIcon(tab, this.mTextView, this.mIconView);
            } else if (this.mCustomTextView != null || this.mCustomIconView != null) {
                updateTextAndIcon(tab, this.mCustomTextView, this.mCustomIconView);
            }
        }

        private void updateTextAndIcon(Tab tab, TextView textView, ImageView imageView) {
            Drawable icon = tab.getIcon();
            CharSequence text = tab.getText();
            if (imageView != null) {
                if (icon != null) {
                    imageView.setImageDrawable(icon);
                    imageView.setVisibility(0);
                    setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
                imageView.setContentDescription(tab.getContentDescription());
            }
            boolean z = !TextUtils.isEmpty(text);
            if (textView != null) {
                if (z) {
                    textView.setText(text);
                    textView.setContentDescription(tab.getContentDescription());
                    textView.setVisibility(0);
                    setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText(null);
                }
            }
            if (z || TextUtils.isEmpty(tab.getContentDescription())) {
                setOnLongClickListener(null);
                setLongClickable(false);
                return;
            }
            setOnLongClickListener(this);
        }

        public boolean onLongClick(View view) {
            int[] iArr = new int[2];
            getLocationOnScreen(iArr);
            Context context = getContext();
            int width = getWidth();
            int height = getHeight();
            int i = context.getResources().getDisplayMetrics().widthPixels;
            Toast makeText = Toast.makeText(context, this.mTab.getContentDescription(), 0);
            makeText.setGravity(49, (iArr[0] + (width / 2)) - (i / 2), height);
            makeText.show();
            return true;
        }

        public Tab getTab() {
            return this.mTab;
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public void onTabReselected(Tab tab) {
        }

        public void onTabUnselected(Tab tab) {
        }

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        public void onTabSelected(Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition());
        }
    }

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTabs = new ArrayList<>();
        this.mTabMaxWidth = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        this.mTabStrip = new SlidingTabStrip(context);
        addView(this.mTabStrip, -2, -1);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.TabLayout, i, C0010R.style.Widget_Design_TabLayout);
        this.mTabStrip.setSelectedIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabIndicatorHeight, 0));
        this.mTabStrip.setSelectedIndicatorColor(obtainStyledAttributes.getColor(C0010R.styleable.TabLayout_tabIndicatorColor, 0));
        this.mTabTextAppearance = obtainStyledAttributes.getResourceId(C0010R.styleable.TabLayout_tabTextAppearance, C0010R.style.TextAppearance_Design_Tab);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabPadding, 0);
        this.mTabPaddingBottom = dimensionPixelSize;
        this.mTabPaddingEnd = dimensionPixelSize;
        this.mTabPaddingTop = dimensionPixelSize;
        this.mTabPaddingStart = dimensionPixelSize;
        this.mTabPaddingStart = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
        this.mTabPaddingTop = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
        this.mTabPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
        this.mTabPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
        this.mTabTextColors = loadTextColorFromTextAppearance(this.mTabTextAppearance);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TabLayout_tabTextColor)) {
            this.mTabTextColors = obtainStyledAttributes.getColorStateList(C0010R.styleable.TabLayout_tabTextColor);
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TabLayout_tabSelectedTextColor)) {
            this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), obtainStyledAttributes.getColor(C0010R.styleable.TabLayout_tabSelectedTextColor, 0));
        }
        this.mTabMinWidth = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabMinWidth, 0);
        this.mRequestedTabMaxWidth = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabMaxWidth, 0);
        this.mTabBackgroundResId = obtainStyledAttributes.getResourceId(C0010R.styleable.TabLayout_tabBackground, 0);
        this.mContentInsetStart = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TabLayout_tabContentStart, 0);
        this.mMode = obtainStyledAttributes.getInt(C0010R.styleable.TabLayout_tabMode, 1);
        this.mTabGravity = obtainStyledAttributes.getInt(C0010R.styleable.TabLayout_tabGravity, 0);
        obtainStyledAttributes.recycle();
        applyModeAndGravity();
    }

    public void setSelectedTabIndicatorColor(int i) {
        this.mTabStrip.setSelectedIndicatorColor(i);
    }

    public void setSelectedTabIndicatorHeight(int i) {
        this.mTabStrip.setSelectedIndicatorHeight(i);
    }

    public void setScrollPosition(int i, float f, boolean z) {
        ValueAnimatorCompat valueAnimatorCompat = this.mIndicatorAnimator;
        if ((valueAnimatorCompat == null || !valueAnimatorCompat.isRunning()) && i >= 0 && i < this.mTabStrip.getChildCount()) {
            this.mTabStrip.setIndicatorPositionFromTabPosition(i, f);
            scrollTo(calculateScrollXForTab(i, f), 0);
            if (z) {
                setSelectedTabView(Math.round(((float) i) + f));
            }
        }
    }

    /* access modifiers changed from: private */
    public float getScrollPosition() {
        return this.mTabStrip.getIndicatorPosition();
    }

    public void addTab(Tab tab) {
        addTab(tab, this.mTabs.isEmpty());
    }

    public void addTab(Tab tab, int i) {
        addTab(tab, i, this.mTabs.isEmpty());
    }

    public void addTab(Tab tab, boolean z) {
        if (tab.mParent == this) {
            addTabView(tab, z);
            configureTab(tab, this.mTabs.size());
            if (z) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void addTab(Tab tab, int i, boolean z) {
        if (tab.mParent == this) {
            addTabView(tab, i, z);
            configureTab(tab, i);
            if (z) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }

    public Tab newTab() {
        return new Tab(this);
    }

    public int getTabCount() {
        return this.mTabs.size();
    }

    public Tab getTabAt(int i) {
        return (Tab) this.mTabs.get(i);
    }

    public int getSelectedTabPosition() {
        Tab tab = this.mSelectedTab;
        if (tab != null) {
            return tab.getPosition();
        }
        return -1;
    }

    public void removeTab(Tab tab) {
        if (tab.mParent == this) {
            removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int i) {
        Tab tab = this.mSelectedTab;
        int position = tab != null ? tab.getPosition() : 0;
        removeTabViewAt(i);
        Tab tab2 = (Tab) this.mTabs.remove(i);
        if (tab2 != null) {
            tab2.setPosition(-1);
        }
        int size = this.mTabs.size();
        for (int i2 = i; i2 < size; i2++) {
            ((Tab) this.mTabs.get(i2)).setPosition(i2);
        }
        if (position == i) {
            selectTab(this.mTabs.isEmpty() ? null : (Tab) this.mTabs.get(Math.max(0, i - 1)));
        }
    }

    public void removeAllTabs() {
        this.mTabStrip.removeAllViews();
        Iterator it = this.mTabs.iterator();
        while (it.hasNext()) {
            ((Tab) it.next()).setPosition(-1);
            it.remove();
        }
        this.mSelectedTab = null;
    }

    public void setTabMode(int i) {
        if (i != this.mMode) {
            this.mMode = i;
            applyModeAndGravity();
        }
    }

    public int getTabMode() {
        return this.mMode;
    }

    public void setTabGravity(int i) {
        if (this.mTabGravity != i) {
            this.mTabGravity = i;
            applyModeAndGravity();
        }
    }

    public int getTabGravity() {
        return this.mTabGravity;
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.mTabTextColors != colorStateList) {
            this.mTabTextColors = colorStateList;
            updateAllTabs();
        }
    }

    public ColorStateList getTabTextColors() {
        return this.mTabTextColors;
    }

    public void setTabTextColors(int i, int i2) {
        setTabTextColors(createColorStateList(i, i2));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            setTabsFromPagerAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this));
            setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));
            if (adapter.getCount() > 0) {
                int currentItem = viewPager.getCurrentItem();
                if (getSelectedTabPosition() != currentItem) {
                    selectTab(getTabAt(currentItem));
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
    }

    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        removeAllTabs();
        int count = pagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            addTab(newTab().setText(pagerAdapter.getPageTitle(i)));
        }
    }

    private void updateAllTabs() {
        int childCount = this.mTabStrip.getChildCount();
        for (int i = 0; i < childCount; i++) {
            updateTab(i);
        }
    }

    private TabView createTabView(Tab tab) {
        TabView tabView = new TabView(getContext(), tab);
        tabView.setFocusable(true);
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new OnClickListener() {
                public void onClick(View view) {
                    ((TabView) view).getTab().select();
                }
            };
        }
        tabView.setOnClickListener(this.mTabClickListener);
        return tabView;
    }

    private void configureTab(Tab tab, int i) {
        tab.setPosition(i);
        this.mTabs.add(i, tab);
        int size = this.mTabs.size();
        while (true) {
            i++;
            if (i < size) {
                ((Tab) this.mTabs.get(i)).setPosition(i);
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateTab(int i) {
        TabView tabView = (TabView) this.mTabStrip.getChildAt(i);
        if (tabView != null) {
            tabView.update();
        }
    }

    private void addTabView(Tab tab, boolean z) {
        TabView createTabView = createTabView(tab);
        this.mTabStrip.addView(createTabView, createLayoutParamsForTabs());
        if (z) {
            createTabView.setSelected(true);
        }
    }

    private void addTabView(Tab tab, int i, boolean z) {
        TabView createTabView = createTabView(tab);
        this.mTabStrip.addView(createTabView, i, createLayoutParamsForTabs());
        if (z) {
            createTabView.setSelected(true);
        }
    }

    private LayoutParams createLayoutParamsForTabs() {
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private void updateTabViewLayoutParams(LayoutParams layoutParams) {
        if (this.mMode == 1 && this.mTabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
            return;
        }
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
    }

    /* access modifiers changed from: private */
    public int dpToPx(int i) {
        return Math.round(getResources().getDisplayMetrics().density * ((float) i));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int dpToPx = dpToPx(48) + getPaddingTop() + getPaddingBottom();
        int mode = MeasureSpec.getMode(i2);
        if (mode == Integer.MIN_VALUE) {
            i2 = MeasureSpec.makeMeasureSpec(Math.min(dpToPx, MeasureSpec.getSize(i2)), 1073741824);
        } else if (mode == 0) {
            i2 = MeasureSpec.makeMeasureSpec(dpToPx, 1073741824);
        }
        super.onMeasure(i, i2);
        if (this.mMode == 1 && getChildCount() == 1) {
            View childAt = getChildAt(0);
            int measuredWidth = getMeasuredWidth();
            if (childAt.getMeasuredWidth() > measuredWidth) {
                childAt.measure(MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), childAt.getLayoutParams().height));
            }
        }
        int i3 = this.mRequestedTabMaxWidth;
        int measuredWidth2 = getMeasuredWidth() - dpToPx(56);
        if (i3 == 0 || i3 > measuredWidth2) {
            i3 = measuredWidth2;
        }
        if (this.mTabMaxWidth != i3) {
            this.mTabMaxWidth = i3;
            super.onMeasure(i, i2);
        }
    }

    private void removeTabViewAt(int i) {
        this.mTabStrip.removeViewAt(i);
        requestLayout();
    }

    private void animateToTab(int i) {
        if (i != -1) {
            if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.mTabStrip.childrenNeedLayout()) {
                setScrollPosition(i, 0.0f, true);
                return;
            }
            int scrollX = getScrollX();
            int calculateScrollXForTab = calculateScrollXForTab(i, 0.0f);
            if (scrollX != calculateScrollXForTab) {
                if (this.mScrollAnimator == null) {
                    this.mScrollAnimator = ViewUtils.createAnimator();
                    this.mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                    this.mScrollAnimator.setDuration(300);
                    this.mScrollAnimator.setUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                            TabLayout.this.scrollTo(valueAnimatorCompat.getAnimatedIntValue(), 0);
                        }
                    });
                }
                this.mScrollAnimator.setIntValues(scrollX, calculateScrollXForTab);
                this.mScrollAnimator.start();
            }
            this.mTabStrip.animateIndicatorToPosition(i, 300);
        }
    }

    private void setSelectedTabView(int i) {
        int childCount = this.mTabStrip.getChildCount();
        if (i < childCount && !this.mTabStrip.getChildAt(i).isSelected()) {
            int i2 = 0;
            while (i2 < childCount) {
                this.mTabStrip.getChildAt(i2).setSelected(i2 == i);
                i2++;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    /* access modifiers changed from: 0000 */
    public void selectTab(Tab tab, boolean z) {
        Tab tab2 = this.mSelectedTab;
        if (tab2 != tab) {
            int position = tab != null ? tab.getPosition() : -1;
            setSelectedTabView(position);
            if (z) {
                Tab tab3 = this.mSelectedTab;
                if ((tab3 == null || tab3.getPosition() == -1) && position != -1) {
                    setScrollPosition(position, 0.0f, true);
                } else {
                    animateToTab(position);
                }
            }
            Tab tab4 = this.mSelectedTab;
            if (tab4 != null) {
                OnTabSelectedListener onTabSelectedListener = this.mOnTabSelectedListener;
                if (onTabSelectedListener != null) {
                    onTabSelectedListener.onTabUnselected(tab4);
                }
            }
            this.mSelectedTab = tab;
            Tab tab5 = this.mSelectedTab;
            if (tab5 != null) {
                OnTabSelectedListener onTabSelectedListener2 = this.mOnTabSelectedListener;
                if (onTabSelectedListener2 != null) {
                    onTabSelectedListener2.onTabSelected(tab5);
                }
            }
        } else if (tab2 != null) {
            OnTabSelectedListener onTabSelectedListener3 = this.mOnTabSelectedListener;
            if (onTabSelectedListener3 != null) {
                onTabSelectedListener3.onTabReselected(tab2);
            }
            animateToTab(tab.getPosition());
        }
    }

    private int calculateScrollXForTab(int i, float f) {
        int i2 = 0;
        if (this.mMode != 0) {
            return 0;
        }
        View childAt = this.mTabStrip.getChildAt(i);
        int i3 = i + 1;
        View childAt2 = i3 < this.mTabStrip.getChildCount() ? this.mTabStrip.getChildAt(i3) : null;
        int width = childAt != null ? childAt.getWidth() : 0;
        if (childAt2 != null) {
            i2 = childAt2.getWidth();
        }
        return ((childAt.getLeft() + ((int) ((((float) (width + i2)) * f) * 0.5f))) + (childAt.getWidth() / 2)) - (getWidth() / 2);
    }

    private void applyModeAndGravity() {
        ViewCompat.setPaddingRelative(this.mTabStrip, this.mMode == 0 ? Math.max(0, this.mContentInsetStart - this.mTabPaddingStart) : 0, 0, 0, 0);
        int i = this.mMode;
        if (i == 0) {
            this.mTabStrip.setGravity(GravityCompat.START);
        } else if (i == 1) {
            this.mTabStrip.setGravity(1);
        }
        updateTabViewsLayoutParams();
    }

    /* access modifiers changed from: private */
    public void updateTabViewsLayoutParams() {
        for (int i = 0; i < this.mTabStrip.getChildCount(); i++) {
            View childAt = this.mTabStrip.getChildAt(i);
            updateTabViewLayoutParams((LayoutParams) childAt.getLayoutParams());
            childAt.requestLayout();
        }
    }

    private static ColorStateList createColorStateList(int i, int i2) {
        return new ColorStateList(new int[][]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{i2, i});
    }

    private ColorStateList loadTextColorFromTextAppearance(int i) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(i, C0010R.styleable.TextAppearance);
        try {
            return obtainStyledAttributes.getColorStateList(C0010R.styleable.TextAppearance_android_textColor);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }
}
