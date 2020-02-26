package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.design.C0010R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.internal.view.SupportMenuInflater;
import android.support.p003v7.internal.view.menu.MenuBuilder;
import android.support.p003v7.internal.view.menu.MenuBuilder.Callback;
import android.support.p003v7.internal.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;

public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    /* access modifiers changed from: private */
    public OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;
    private final NavigationMenu mMenu;
    private MenuInflater mMenuInflater;
    private final NavigationMenuPresenter mPresenter;

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Bundle menuState;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.menuState = parcel.readBundle();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuState);
        }
    }

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationView(Context context, AttributeSet attributeSet, int i) {
        ColorStateList colorStateList;
        int i2;
        boolean z;
        super(context, attributeSet, i);
        this.mPresenter = new NavigationMenuPresenter();
        this.mMenu = new NavigationMenu(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0010R.styleable.NavigationView, i, C0010R.style.Widget_Design_NavigationView);
        setBackgroundDrawable(obtainStyledAttributes.getDrawable(C0010R.styleable.NavigationView_android_background));
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows(this, obtainStyledAttributes.getBoolean(C0010R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.NavigationView_android_maxWidth, 0);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_itemIconTint)) {
            colorStateList = obtainStyledAttributes.getColorStateList(C0010R.styleable.NavigationView_itemIconTint);
        } else {
            colorStateList = createDefaultColorStateList(16842808);
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_itemTextAppearance)) {
            i2 = obtainStyledAttributes.getResourceId(C0010R.styleable.NavigationView_itemTextAppearance, 0);
            z = true;
        } else {
            z = false;
            i2 = 0;
        }
        ColorStateList colorStateList2 = null;
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_itemTextColor)) {
            colorStateList2 = obtainStyledAttributes.getColorStateList(C0010R.styleable.NavigationView_itemTextColor);
        }
        if (!z && colorStateList2 == null) {
            colorStateList2 = createDefaultColorStateList(16842806);
        }
        Drawable drawable = obtainStyledAttributes.getDrawable(C0010R.styleable.NavigationView_itemBackground);
        this.mMenu.setCallback(new Callback() {
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }

            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                return NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(menuItem);
            }
        });
        this.mPresenter.setId(1);
        this.mPresenter.initForMenu(context, this.mMenu);
        this.mPresenter.setItemIconTintList(colorStateList);
        if (z) {
            this.mPresenter.setItemTextAppearance(i2);
        }
        this.mPresenter.setItemTextColor(colorStateList2);
        this.mPresenter.setItemBackground(drawable);
        this.mMenu.addMenuPresenter(this.mPresenter);
        addView((View) this.mPresenter.getMenuView(this));
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_menu)) {
            inflateMenu(obtainStyledAttributes.getResourceId(C0010R.styleable.NavigationView_menu, 0));
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.NavigationView_headerLayout)) {
            inflateHeaderView(obtainStyledAttributes.getResourceId(C0010R.styleable.NavigationView_headerLayout, 0));
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mMenu.restorePresenterStates(savedState.menuState);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.mListener = onNavigationItemSelectedListener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            i = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(i), this.mMaxWidth), 1073741824);
        } else if (mode == 0) {
            i = MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
        }
        super.onMeasure(i, i2);
    }

    public void inflateMenu(int i) {
        this.mPresenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.mMenu);
        this.mPresenter.setUpdateSuspended(false);
        this.mPresenter.updateMenuView(false);
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public View inflateHeaderView(int i) {
        return this.mPresenter.inflateHeaderView(i);
    }

    public void addHeaderView(View view) {
        this.mPresenter.addHeaderView(view);
    }

    public void removeHeaderView(View view) {
        this.mPresenter.removeHeaderView(view);
    }

    public ColorStateList getItemIconTintList() {
        return this.mPresenter.getItemTintList();
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.mPresenter.setItemIconTintList(colorStateList);
    }

    public ColorStateList getItemTextColor() {
        return this.mPresenter.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.mPresenter.setItemTextColor(colorStateList);
    }

    public Drawable getItemBackground() {
        return this.mPresenter.getItemBackground();
    }

    public void setItemBackgroundResource(int i) {
        setItemBackground(ContextCompat.getDrawable(getContext(), i));
    }

    public void setItemBackground(Drawable drawable) {
        this.mPresenter.setItemBackground(drawable);
    }

    public void setCheckedItem(int i) {
        MenuItem findItem = this.mMenu.findItem(i);
        if (findItem != null) {
            this.mPresenter.setCheckedItem((MenuItemImpl) findItem);
        }
    }

    public void setItemTextAppearance(int i) {
        this.mPresenter.setItemTextAppearance(i);
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private ColorStateList createDefaultColorStateList(int i) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = getResources().getColorStateList(typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0010R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(DISABLED_STATE_SET, defaultColor), i2, defaultColor});
    }
}
