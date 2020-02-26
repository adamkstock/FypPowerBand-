package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.p000v4.view.TintableBackgroundView;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.appcompat.C0251R;
import android.support.p003v7.internal.widget.TintManager;
import android.support.p003v7.internal.widget.ViewUtils;
import android.support.p003v7.widget.ListPopupWindow.ForwardingListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;

/* renamed from: android.support.v7.widget.AppCompatSpinner */
public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
    private static final int[] ATTRS_ANDROID_SPINNERMODE = {16843505};
    private static final boolean IS_AT_LEAST_JB = (VERSION.SDK_INT >= 16);
    /* access modifiers changed from: private */
    public static final boolean IS_AT_LEAST_M = (VERSION.SDK_INT >= 23);
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private AppCompatBackgroundHelper mBackgroundTintHelper;
    /* access modifiers changed from: private */
    public int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    /* access modifiers changed from: private */
    public DropdownPopup mPopup;
    private Context mPopupContext;
    private boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    /* access modifiers changed from: private */
    public final Rect mTempRect;
    private TintManager mTintManager;

    /* renamed from: android.support.v7.widget.AppCompatSpinner$DropDownAdapter */
    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public DropDownAdapter(SpinnerAdapter spinnerAdapter, Theme theme) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) spinnerAdapter;
            }
            if (theme == null) {
                return;
            }
            if (AppCompatSpinner.IS_AT_LEAST_M && (spinnerAdapter instanceof ThemedSpinnerAdapter)) {
                ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter.getDropDownViewTheme() != theme) {
                    themedSpinnerAdapter.setDropDownViewTheme(theme);
                }
            } else if (spinnerAdapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedSpinnerAdapter2 = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                    themedSpinnerAdapter2.setDropDownViewTheme(theme);
                }
            }
        }

        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return 0;
            }
            return spinnerAdapter.getCount();
        }

        public Object getItem(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getItem(i);
        }

        public long getItemId(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return -1;
            }
            return spinnerAdapter.getItemId(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return getDropDownView(i, view, viewGroup);
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(i, view, viewGroup);
        }

        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter != null && spinnerAdapter.hasStableIds();
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        public boolean isEnabled(int i) {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.isEnabled(i);
            }
            return true;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    /* renamed from: android.support.v7.widget.AppCompatSpinner$DropdownPopup */
    private class DropdownPopup extends ListPopupWindow {
        /* access modifiers changed from: private */
        public ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect = new Rect();

        public DropdownPopup(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            setAnchorView(AppCompatSpinner.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new OnItemClickListener(AppCompatSpinner.this) {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    AppCompatSpinner.this.setSelection(i);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        AppCompatSpinner.this.performItemClick(view, i, DropdownPopup.this.mAdapter.getItemId(i));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        /* access modifiers changed from: 0000 */
        public void computeContentWidth() {
            Drawable background = getBackground();
            int i = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                i = ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? AppCompatSpinner.this.mTempRect.right : -AppCompatSpinner.this.mTempRect.left;
            } else {
                Rect access$300 = AppCompatSpinner.this.mTempRect;
                AppCompatSpinner.this.mTempRect.right = 0;
                access$300.left = 0;
            }
            int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            int paddingRight = AppCompatSpinner.this.getPaddingRight();
            int width = AppCompatSpinner.this.getWidth();
            if (AppCompatSpinner.this.mDropDownWidth == -2) {
                int access$500 = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                int i2 = (AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels - AppCompatSpinner.this.mTempRect.left) - AppCompatSpinner.this.mTempRect.right;
                if (access$500 > i2) {
                    access$500 = i2;
                }
                setContentWidth(Math.max(access$500, (width - paddingLeft) - paddingRight));
            } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
                setContentWidth((width - paddingLeft) - paddingRight);
            } else {
                setContentWidth(AppCompatSpinner.this.mDropDownWidth);
            }
            setHorizontalOffset(ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? i + ((width - paddingRight) - getWidth()) : i + paddingLeft);
        }

        public void show() {
            boolean isShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            getListView().setChoiceMode(1);
            setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (!isShowing) {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    final C02832 r1 = new OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            DropdownPopup dropdownPopup = DropdownPopup.this;
                            if (!dropdownPopup.isVisibleToUser(AppCompatSpinner.this)) {
                                DropdownPopup.this.dismiss();
                                return;
                            }
                            DropdownPopup.this.computeContentWidth();
                            DropdownPopup.super.show();
                        }
                    };
                    viewTreeObserver.addOnGlobalLayoutListener(r1);
                    setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                            if (viewTreeObserver != null) {
                                viewTreeObserver.removeGlobalOnLayoutListener(r1);
                            }
                        }
                    });
                }
            }
        }

        /* access modifiers changed from: private */
        public boolean isVisibleToUser(View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
    }

    public AppCompatSpinner(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppCompatSpinner(Context context, int i) {
        this(context, null, C0251R.attr.spinnerStyle, i);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0251R.attr.spinnerStyle);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0062, code lost:
        if (r7 != null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0064, code lost:
        r7.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0076, code lost:
        if (r7 != null) goto L_0x0064;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AppCompatSpinner(android.content.Context r7, android.util.AttributeSet r8, int r9, int r10, android.content.res.Resources.Theme r11) {
        /*
            r6 = this;
            r6.<init>(r7, r8, r9)
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r6.mTempRect = r0
            int[] r0 = android.support.p003v7.appcompat.C0251R.styleable.Spinner
            r1 = 0
            android.support.v7.internal.widget.TintTypedArray r0 = android.support.p003v7.internal.widget.TintTypedArray.obtainStyledAttributes(r7, r8, r0, r9, r1)
            android.support.v7.internal.widget.TintManager r2 = r0.getTintManager()
            r6.mTintManager = r2
            android.support.v7.widget.AppCompatBackgroundHelper r2 = new android.support.v7.widget.AppCompatBackgroundHelper
            android.support.v7.internal.widget.TintManager r3 = r6.mTintManager
            r2.<init>(r6, r3)
            r6.mBackgroundTintHelper = r2
            r2 = 0
            if (r11 == 0) goto L_0x002b
            android.support.v7.internal.view.ContextThemeWrapper r3 = new android.support.v7.internal.view.ContextThemeWrapper
            r3.<init>(r7, r11)
            r6.mPopupContext = r3
            goto L_0x0044
        L_0x002b:
            int r11 = android.support.p003v7.appcompat.C0251R.styleable.Spinner_popupTheme
            int r11 = r0.getResourceId(r11, r1)
            if (r11 == 0) goto L_0x003b
            android.support.v7.internal.view.ContextThemeWrapper r3 = new android.support.v7.internal.view.ContextThemeWrapper
            r3.<init>(r7, r11)
            r6.mPopupContext = r3
            goto L_0x0044
        L_0x003b:
            boolean r11 = IS_AT_LEAST_M
            if (r11 != 0) goto L_0x0041
            r11 = r7
            goto L_0x0042
        L_0x0041:
            r11 = r2
        L_0x0042:
            r6.mPopupContext = r11
        L_0x0044:
            android.content.Context r11 = r6.mPopupContext
            r3 = 1
            if (r11 == 0) goto L_0x00b9
            r11 = -1
            if (r10 != r11) goto L_0x0081
            int r11 = android.os.Build.VERSION.SDK_INT
            r4 = 11
            if (r11 < r4) goto L_0x0080
            int[] r11 = ATTRS_ANDROID_SPINNERMODE     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            android.content.res.TypedArray r7 = r7.obtainStyledAttributes(r8, r11, r9, r1)     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            boolean r11 = r7.hasValue(r1)     // Catch:{ Exception -> 0x0068 }
            if (r11 == 0) goto L_0x0062
            int r10 = r7.getInt(r1, r1)     // Catch:{ Exception -> 0x0068 }
        L_0x0062:
            if (r7 == 0) goto L_0x0081
        L_0x0064:
            r7.recycle()
            goto L_0x0081
        L_0x0068:
            r11 = move-exception
            goto L_0x006f
        L_0x006a:
            r8 = move-exception
            r7 = r2
            goto L_0x007a
        L_0x006d:
            r11 = move-exception
            r7 = r2
        L_0x006f:
            java.lang.String r4 = "AppCompatSpinner"
            java.lang.String r5 = "Could not read android:spinnerMode"
            android.util.Log.i(r4, r5, r11)     // Catch:{ all -> 0x0079 }
            if (r7 == 0) goto L_0x0081
            goto L_0x0064
        L_0x0079:
            r8 = move-exception
        L_0x007a:
            if (r7 == 0) goto L_0x007f
            r7.recycle()
        L_0x007f:
            throw r8
        L_0x0080:
            r10 = 1
        L_0x0081:
            if (r10 != r3) goto L_0x00b9
            android.support.v7.widget.AppCompatSpinner$DropdownPopup r7 = new android.support.v7.widget.AppCompatSpinner$DropdownPopup
            android.content.Context r10 = r6.mPopupContext
            r7.<init>(r10, r8, r9)
            android.content.Context r10 = r6.mPopupContext
            int[] r11 = android.support.p003v7.appcompat.C0251R.styleable.Spinner
            android.support.v7.internal.widget.TintTypedArray r10 = android.support.p003v7.internal.widget.TintTypedArray.obtainStyledAttributes(r10, r8, r11, r9, r1)
            int r11 = android.support.p003v7.appcompat.C0251R.styleable.Spinner_android_dropDownWidth
            r1 = -2
            int r11 = r10.getLayoutDimension(r11, r1)
            r6.mDropDownWidth = r11
            int r11 = android.support.p003v7.appcompat.C0251R.styleable.Spinner_android_popupBackground
            android.graphics.drawable.Drawable r11 = r10.getDrawable(r11)
            r7.setBackgroundDrawable(r11)
            int r11 = android.support.p003v7.appcompat.C0251R.styleable.Spinner_android_prompt
            java.lang.String r11 = r0.getString(r11)
            r7.setPromptText(r11)
            r10.recycle()
            r6.mPopup = r7
            android.support.v7.widget.AppCompatSpinner$1 r10 = new android.support.v7.widget.AppCompatSpinner$1
            r10.<init>(r6, r7)
            r6.mForwardingListener = r10
        L_0x00b9:
            r0.recycle()
            r6.mPopupSet = r3
            android.widget.SpinnerAdapter r7 = r6.mTempAdapter
            if (r7 == 0) goto L_0x00c7
            r6.setAdapter(r7)
            r6.mTempAdapter = r2
        L_0x00c7:
            android.support.v7.widget.AppCompatBackgroundHelper r7 = r6.mBackgroundTintHelper
            r7.loadFromAttributes(r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.widget.AppCompatSpinner.<init>(android.content.Context, android.util.AttributeSet, int, int, android.content.res.Resources$Theme):void");
    }

    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        if (IS_AT_LEAST_M) {
            return super.getPopupContext();
        }
        return null;
    }

    public void setPopupBackgroundDrawable(Drawable drawable) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setBackgroundDrawable(drawable);
        } else if (IS_AT_LEAST_JB) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    public void setPopupBackgroundResource(int i) {
        setPopupBackgroundDrawable(getPopupContext().getDrawable(i));
    }

    public Drawable getPopupBackground() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getBackground();
        }
        if (IS_AT_LEAST_JB) {
            return super.getPopupBackground();
        }
        return null;
    }

    public void setDropDownVerticalOffset(int i) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setVerticalOffset(i);
        } else if (IS_AT_LEAST_JB) {
            super.setDropDownVerticalOffset(i);
        }
    }

    public int getDropDownVerticalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getVerticalOffset();
        }
        if (IS_AT_LEAST_JB) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }

    public void setDropDownHorizontalOffset(int i) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setHorizontalOffset(i);
        } else if (IS_AT_LEAST_JB) {
            super.setDropDownHorizontalOffset(i);
        }
    }

    public int getDropDownHorizontalOffset() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            return dropdownPopup.getHorizontalOffset();
        }
        if (IS_AT_LEAST_JB) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }

    public void setDropDownWidth(int i) {
        if (this.mPopup != null) {
            this.mDropDownWidth = i;
        } else if (IS_AT_LEAST_JB) {
            super.setDropDownWidth(i);
        }
    }

    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (IS_AT_LEAST_JB) {
            return super.getDropDownWidth();
        }
        return 0;
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context = this.mPopupContext;
            if (context == null) {
                context = getContext();
            }
            this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null && dropdownPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener = this.mForwardingListener;
        if (forwardingListener == null || !forwardingListener.onTouch(this, motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mPopup != null && MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), MeasureSpec.getSize(i)), getMeasuredHeight());
        }
    }

    public boolean performClick() {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup == null || dropdownPopup.isShowing()) {
            return super.performClick();
        }
        this.mPopup.show();
        return true;
    }

    public void setPrompt(CharSequence charSequence) {
        DropdownPopup dropdownPopup = this.mPopup;
        if (dropdownPopup != null) {
            dropdownPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    public CharSequence getPrompt() {
        DropdownPopup dropdownPopup = this.mPopup;
        return dropdownPopup != null ? dropdownPopup.getHintText() : super.getPrompt();
    }

    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable);
        }
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintList();
        }
        return null;
    }

    public void setSupportBackgroundTintMode(Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    public Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintMode();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    /* access modifiers changed from: private */
    public int compatMeasureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        int i = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int max = Math.max(0, getSelectedItemPosition());
        int min = Math.min(spinnerAdapter.getCount(), max + 15);
        View view = null;
        int i2 = 0;
        for (int max2 = Math.max(0, max - (15 - (min - max))); max2 < min; max2++) {
            int itemViewType = spinnerAdapter.getItemViewType(max2);
            if (itemViewType != i) {
                view = null;
                i = itemViewType;
            }
            view = spinnerAdapter.getView(max2, view, this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i2 = Math.max(i2, view.getMeasuredWidth());
        }
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            i2 += this.mTempRect.left + this.mTempRect.right;
        }
        return i2;
    }
}
