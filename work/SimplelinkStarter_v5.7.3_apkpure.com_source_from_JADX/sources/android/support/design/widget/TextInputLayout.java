package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.C0010R;
import android.support.p000v4.view.AccessibilityDelegateCompat;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.p000v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p003v7.internal.widget.TintManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextInputLayout extends LinearLayout {
    private static final int ANIMATION_DURATION = 200;
    private ValueAnimatorCompat mAnimator;
    /* access modifiers changed from: private */
    public final CollapsingTextHelper mCollapsingTextHelper;
    private ColorStateList mDefaultTextColor;
    /* access modifiers changed from: private */
    public EditText mEditText;
    private boolean mErrorEnabled;
    private int mErrorTextAppearance;
    /* access modifiers changed from: private */
    public TextView mErrorView;
    private ColorStateList mFocusedTextColor;
    private CharSequence mHint;
    private boolean mHintAnimationEnabled;
    private Paint mTmpPaint;

    private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat {
        private TextInputAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName(TextInputLayout.class.getSimpleName());
        }

        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityEvent.getText().add(text);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(TextInputLayout.class.getSimpleName());
            CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityNodeInfoCompat.setText(text);
            }
            if (TextInputLayout.this.mEditText != null) {
                accessibilityNodeInfoCompat.setLabelFor(TextInputLayout.this.mEditText);
            }
            CharSequence text2 = TextInputLayout.this.mErrorView != null ? TextInputLayout.this.mErrorView.getText() : null;
            if (!TextUtils.isEmpty(text2)) {
                accessibilityNodeInfoCompat.setContentInvalid(true);
                accessibilityNodeInfoCompat.setError(text2);
            }
        }
    }

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mCollapsingTextHelper = new CollapsingTextHelper(this);
        setOrientation(1);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mCollapsingTextHelper.setPositionInterpolator(new AccelerateInterpolator());
        this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.TextInputLayout, i, C0010R.style.Widget_Design_TextInputLayout);
        this.mHint = obtainStyledAttributes.getText(C0010R.styleable.TextInputLayout_android_hint);
        this.mHintAnimationEnabled = obtainStyledAttributes.getBoolean(C0010R.styleable.TextInputLayout_hintAnimationEnabled, true);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TextInputLayout_android_textColorHint)) {
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C0010R.styleable.TextInputLayout_android_textColorHint);
            this.mFocusedTextColor = colorStateList;
            this.mDefaultTextColor = colorStateList;
        }
        if (obtainStyledAttributes.getResourceId(C0010R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            setHintTextAppearance(obtainStyledAttributes.getResourceId(C0010R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        this.mErrorTextAppearance = obtainStyledAttributes.getResourceId(C0010R.styleable.TextInputLayout_errorTextAppearance, 0);
        boolean z = obtainStyledAttributes.getBoolean(C0010R.styleable.TextInputLayout_errorEnabled, false);
        obtainStyledAttributes.recycle();
        setErrorEnabled(z);
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        ViewCompat.setAccessibilityDelegate(this, new TextInputAccessibilityDelegate());
    }

    public void addView(View view, int i, LayoutParams layoutParams) {
        if (view instanceof EditText) {
            setEditText((EditText) view);
            super.addView(view, 0, updateEditTextMargin(layoutParams));
            return;
        }
        super.addView(view, i, layoutParams);
    }

    public void setTypeface(Typeface typeface) {
        this.mCollapsingTextHelper.setTypeface(typeface);
    }

    private void setEditText(EditText editText) {
        if (this.mEditText == null) {
            this.mEditText = editText;
            this.mCollapsingTextHelper.setTypeface(this.mEditText.getTypeface());
            this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
            this.mCollapsingTextHelper.setExpandedTextGravity(this.mEditText.getGravity());
            this.mEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    TextInputLayout.this.updateLabelVisibility(true);
                }
            });
            if (this.mDefaultTextColor == null) {
                this.mDefaultTextColor = this.mEditText.getHintTextColors();
            }
            if (TextUtils.isEmpty(this.mHint)) {
                setHint(this.mEditText.getHint());
                this.mEditText.setHint(null);
            }
            TextView textView = this.mErrorView;
            if (textView != null) {
                ViewCompat.setPaddingRelative(textView, ViewCompat.getPaddingStart(this.mEditText), 0, ViewCompat.getPaddingEnd(this.mEditText), this.mEditText.getPaddingBottom());
            }
            updateLabelVisibility(false);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private LinearLayout.LayoutParams updateEditTextMargin(LayoutParams layoutParams) {
        LinearLayout.LayoutParams layoutParams2 = layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams : new LinearLayout.LayoutParams(layoutParams);
        if (this.mTmpPaint == null) {
            this.mTmpPaint = new Paint();
        }
        this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getTypeface());
        this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
        layoutParams2.topMargin = (int) (-this.mTmpPaint.ascent());
        return layoutParams2;
    }

    /* access modifiers changed from: private */
    public void updateLabelVisibility(boolean z) {
        EditText editText = this.mEditText;
        boolean z2 = editText != null && !TextUtils.isEmpty(editText.getText());
        boolean arrayContains = arrayContains(getDrawableState(), 16842908);
        ColorStateList colorStateList = this.mDefaultTextColor;
        if (!(colorStateList == null || this.mFocusedTextColor == null)) {
            this.mCollapsingTextHelper.setExpandedTextColor(colorStateList.getDefaultColor());
            this.mCollapsingTextHelper.setCollapsedTextColor((arrayContains ? this.mFocusedTextColor : this.mDefaultTextColor).getDefaultColor());
        }
        if (z2 || arrayContains) {
            collapseHint(z);
        } else {
            expandHint(z);
        }
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    public void setHint(CharSequence charSequence) {
        this.mHint = charSequence;
        this.mCollapsingTextHelper.setText(charSequence);
        sendAccessibilityEvent(2048);
    }

    public CharSequence getHint() {
        return this.mHint;
    }

    public void setHintTextAppearance(int i) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(i);
        this.mFocusedTextColor = ColorStateList.valueOf(this.mCollapsingTextHelper.getCollapsedTextColor());
        if (this.mEditText != null) {
            updateLabelVisibility(false);
            this.mEditText.setLayoutParams(updateEditTextMargin(this.mEditText.getLayoutParams()));
            this.mEditText.requestLayout();
        }
    }

    public void setErrorEnabled(boolean z) {
        if (this.mErrorEnabled != z) {
            TextView textView = this.mErrorView;
            if (textView != null) {
                ViewCompat.animate(textView).cancel();
            }
            if (z) {
                this.mErrorView = new TextView(getContext());
                this.mErrorView.setTextAppearance(getContext(), this.mErrorTextAppearance);
                this.mErrorView.setVisibility(4);
                addView(this.mErrorView);
                EditText editText = this.mEditText;
                if (editText != null) {
                    ViewCompat.setPaddingRelative(this.mErrorView, ViewCompat.getPaddingStart(editText), 0, ViewCompat.getPaddingEnd(this.mEditText), this.mEditText.getPaddingBottom());
                }
            } else {
                removeView(this.mErrorView);
                this.mErrorView = null;
            }
            this.mErrorEnabled = z;
        }
    }

    public boolean isErrorEnabled() {
        return this.mErrorEnabled;
    }

    public void setError(CharSequence charSequence) {
        if (!this.mErrorEnabled) {
            if (!TextUtils.isEmpty(charSequence)) {
                setErrorEnabled(true);
            } else {
                return;
            }
        }
        if (!TextUtils.isEmpty(charSequence)) {
            ViewCompat.setAlpha(this.mErrorView, 0.0f);
            this.mErrorView.setText(charSequence);
            ViewCompat.animate(this.mErrorView).alpha(1.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new ViewPropertyAnimatorListenerAdapter() {
                public void onAnimationStart(View view) {
                    view.setVisibility(0);
                }
            }).start();
            ViewCompat.setBackgroundTintList(this.mEditText, ColorStateList.valueOf(this.mErrorView.getCurrentTextColor()));
        } else if (this.mErrorView.getVisibility() == 0) {
            ViewCompat.animate(this.mErrorView).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new ViewPropertyAnimatorListenerAdapter() {
                public void onAnimationEnd(View view) {
                    view.setVisibility(4);
                }
            }).start();
            ViewCompat.setBackgroundTintList(this.mEditText, TintManager.get(getContext()).getTintList(C0010R.C0011drawable.abc_edit_text_material));
        }
        sendAccessibilityEvent(2048);
    }

    public CharSequence getError() {
        if (this.mErrorEnabled) {
            TextView textView = this.mErrorView;
            if (textView != null && textView.getVisibility() == 0) {
                return this.mErrorView.getText();
            }
        }
        return null;
    }

    public boolean isHintAnimationEnabled() {
        return this.mHintAnimationEnabled;
    }

    public void setHintAnimationEnabled(boolean z) {
        this.mHintAnimationEnabled = z;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mCollapsingTextHelper.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        EditText editText = this.mEditText;
        if (editText != null) {
            int left = editText.getLeft() + this.mEditText.getCompoundPaddingLeft();
            int right = this.mEditText.getRight() - this.mEditText.getCompoundPaddingRight();
            this.mCollapsingTextHelper.setExpandedBounds(left, this.mEditText.getTop() + this.mEditText.getCompoundPaddingTop(), right, this.mEditText.getBottom() - this.mEditText.getCompoundPaddingBottom());
            this.mCollapsingTextHelper.setCollapsedBounds(left, getPaddingTop(), right, (i4 - i2) - getPaddingBottom());
            this.mCollapsingTextHelper.recalculate();
        }
    }

    public void refreshDrawableState() {
        super.refreshDrawableState();
        updateLabelVisibility(ViewCompat.isLaidOut(this));
    }

    private void collapseHint(boolean z) {
        ValueAnimatorCompat valueAnimatorCompat = this.mAnimator;
        if (valueAnimatorCompat != null && valueAnimatorCompat.isRunning()) {
            this.mAnimator.cancel();
        }
        if (!z || !this.mHintAnimationEnabled) {
            this.mCollapsingTextHelper.setExpansionFraction(1.0f);
        } else {
            animateToExpansionFraction(1.0f);
        }
    }

    private void expandHint(boolean z) {
        ValueAnimatorCompat valueAnimatorCompat = this.mAnimator;
        if (valueAnimatorCompat != null && valueAnimatorCompat.isRunning()) {
            this.mAnimator.cancel();
        }
        if (!z || !this.mHintAnimationEnabled) {
            this.mCollapsingTextHelper.setExpansionFraction(0.0f);
        } else {
            animateToExpansionFraction(0.0f);
        }
    }

    private void animateToExpansionFraction(float f) {
        if (this.mCollapsingTextHelper.getExpansionFraction() != f) {
            if (this.mAnimator == null) {
                this.mAnimator = ViewUtils.createAnimator();
                this.mAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
                this.mAnimator.setDuration(200);
                this.mAnimator.setUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction(valueAnimatorCompat.getAnimatedFloatValue());
                    }
                });
            }
            this.mAnimator.setFloatValues(this.mCollapsingTextHelper.getExpansionFraction(), f);
            this.mAnimator.start();
        }
    }

    private int getThemeAttrColor(int i) {
        TypedValue typedValue = new TypedValue();
        if (getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.data;
        }
        return -65281;
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }
}
