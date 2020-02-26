package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.design.C0010R;
import android.support.p000v4.graphics.drawable.DrawableCompat;
import android.support.p000v4.widget.TextViewCompat;
import android.support.p003v7.internal.view.menu.MenuItemImpl;
import android.support.p003v7.internal.view.menu.MenuView.ItemView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class NavigationMenuItemView extends TextView implements ItemView {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private int mIconSize;
    private ColorStateList mIconTintList;
    private MenuItemImpl mItemData;

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setShortcut(boolean z, char c) {
    }

    public boolean showsIcon() {
        return true;
    }

    public NavigationMenuItemView(Context context) {
        this(context, null);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIconSize = context.getResources().getDimensionPixelSize(C0010R.dimen.design_navigation_icon_size);
    }

    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        if (getBackground() == null) {
            setBackgroundDrawable(createDefaultBackground());
        }
        setCheckable(menuItemImpl.isCheckable());
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        setTitle(menuItemImpl.getTitle());
        setIcon(menuItemImpl.getIcon());
    }

    private StateListDrawable createDefaultBackground() {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(C0010R.attr.colorControlHighlight, typedValue, true)) {
            return null;
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(CHECKED_STATE_SET, new ColorDrawable(typedValue.data));
        stateListDrawable.addState(EMPTY_STATE_SET, new ColorDrawable(0));
        return stateListDrawable;
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setTitle(CharSequence charSequence) {
        setText(charSequence);
    }

    public void setCheckable(boolean z) {
        refreshDrawableState();
    }

    public void setChecked(boolean z) {
        refreshDrawableState();
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.getConstantState().newDrawable()).mutate();
            int i = this.mIconSize;
            drawable.setBounds(0, 0, i, i);
            DrawableCompat.setTintList(drawable, this.mIconTintList);
        }
        TextViewCompat.setCompoundDrawablesRelative(this, drawable, null, null, null);
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.mItemData.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    /* access modifiers changed from: 0000 */
    public void setIconTintList(ColorStateList colorStateList) {
        this.mIconTintList = colorStateList;
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null) {
            setIcon(menuItemImpl.getIcon());
        }
    }
}
