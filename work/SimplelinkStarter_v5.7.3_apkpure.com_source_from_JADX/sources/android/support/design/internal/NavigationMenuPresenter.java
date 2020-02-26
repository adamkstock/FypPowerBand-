package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.C0010R;
import android.support.p003v7.internal.view.menu.MenuBuilder;
import android.support.p003v7.internal.view.menu.MenuItemImpl;
import android.support.p003v7.internal.view.menu.MenuPresenter;
import android.support.p003v7.internal.view.menu.MenuPresenter.Callback;
import android.support.p003v7.internal.view.menu.MenuView;
import android.support.p003v7.internal.view.menu.SubMenuBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

public class NavigationMenuPresenter implements MenuPresenter, OnItemClickListener {
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HIERARCHY = "android:menu:list";
    private NavigationMenuAdapter mAdapter;
    private Callback mCallback;
    private LinearLayout mHeader;
    /* access modifiers changed from: private */
    public ColorStateList mIconTintList;
    private int mId;
    /* access modifiers changed from: private */
    public Drawable mItemBackground;
    /* access modifiers changed from: private */
    public LayoutInflater mLayoutInflater;
    /* access modifiers changed from: private */
    public MenuBuilder mMenu;
    private NavigationMenuView mMenuView;
    /* access modifiers changed from: private */
    public int mPaddingSeparator;
    private int mPaddingTopDefault;
    /* access modifiers changed from: private */
    public int mTextAppearance;
    /* access modifiers changed from: private */
    public boolean mTextAppearanceSet;
    /* access modifiers changed from: private */
    public ColorStateList mTextColor;

    private class NavigationMenuAdapter extends BaseAdapter {
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl mCheckedItem;
        private final ArrayList<NavigationMenuItem> mItems = new ArrayList<>();
        private ColorDrawable mTransparentIcon;
        private boolean mUpdateSuspended;

        public boolean areAllItemsEnabled() {
            return false;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 3;
        }

        NavigationMenuAdapter() {
            prepareMenuItems();
        }

        public int getCount() {
            return this.mItems.size();
        }

        public NavigationMenuItem getItem(int i) {
            return (NavigationMenuItem) this.mItems.get(i);
        }

        public int getItemViewType(int i) {
            NavigationMenuItem item = getItem(i);
            if (item.isSeparator()) {
                return 2;
            }
            return item.getMenuItem().hasSubMenu() ? 1 : 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            NavigationMenuItem item = getItem(i);
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                if (view == null) {
                    view = NavigationMenuPresenter.this.mLayoutInflater.inflate(C0010R.layout.design_navigation_item, viewGroup, false);
                }
                NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) view;
                navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
                if (NavigationMenuPresenter.this.mTextAppearanceSet) {
                    navigationMenuItemView.setTextAppearance(navigationMenuItemView.getContext(), NavigationMenuPresenter.this.mTextAppearance);
                }
                if (NavigationMenuPresenter.this.mTextColor != null) {
                    navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor);
                }
                navigationMenuItemView.setBackgroundDrawable(NavigationMenuPresenter.this.mItemBackground != null ? NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable() : null);
                navigationMenuItemView.initialize(item.getMenuItem(), 0);
            } else if (itemViewType == 1) {
                if (view == null) {
                    view = NavigationMenuPresenter.this.mLayoutInflater.inflate(C0010R.layout.design_navigation_item_subheader, viewGroup, false);
                }
                ((TextView) view).setText(item.getMenuItem().getTitle());
            } else if (itemViewType == 2) {
                if (view == null) {
                    view = NavigationMenuPresenter.this.mLayoutInflater.inflate(C0010R.layout.design_navigation_item_separator, viewGroup, false);
                }
                view.setPadding(0, item.getPaddingTop(), 0, item.getPaddingBottom());
            }
            return view;
        }

        public boolean isEnabled(int i) {
            return getItem(i).isEnabled();
        }

        public void notifyDataSetChanged() {
            prepareMenuItems();
            super.notifyDataSetChanged();
        }

        private void prepareMenuItems() {
            if (!this.mUpdateSuspended) {
                this.mUpdateSuspended = true;
                this.mItems.clear();
                int size = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();
                int i = -1;
                boolean z = false;
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    MenuItemImpl menuItemImpl = (MenuItemImpl) NavigationMenuPresenter.this.mMenu.getVisibleItems().get(i3);
                    if (menuItemImpl.isChecked()) {
                        setCheckedItem(menuItemImpl);
                    }
                    if (menuItemImpl.isCheckable()) {
                        menuItemImpl.setExclusiveCheckable(false);
                    }
                    if (menuItemImpl.hasSubMenu()) {
                        SubMenu subMenu = menuItemImpl.getSubMenu();
                        if (subMenu.hasVisibleItems()) {
                            if (i3 != 0) {
                                this.mItems.add(NavigationMenuItem.separator(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                            }
                            this.mItems.add(NavigationMenuItem.m2of(menuItemImpl));
                            int size2 = this.mItems.size();
                            int size3 = subMenu.size();
                            boolean z2 = false;
                            for (int i4 = 0; i4 < size3; i4++) {
                                MenuItemImpl menuItemImpl2 = (MenuItemImpl) subMenu.getItem(i4);
                                if (menuItemImpl2.isVisible()) {
                                    if (!z2 && menuItemImpl2.getIcon() != null) {
                                        z2 = true;
                                    }
                                    if (menuItemImpl2.isCheckable()) {
                                        menuItemImpl2.setExclusiveCheckable(false);
                                    }
                                    if (menuItemImpl.isChecked()) {
                                        setCheckedItem(menuItemImpl);
                                    }
                                    this.mItems.add(NavigationMenuItem.m2of(menuItemImpl2));
                                }
                            }
                            if (z2) {
                                appendTransparentIconIfMissing(size2, this.mItems.size());
                            }
                        }
                    } else {
                        int groupId = menuItemImpl.getGroupId();
                        if (groupId != i) {
                            i2 = this.mItems.size();
                            boolean z3 = menuItemImpl.getIcon() != null;
                            if (i3 != 0) {
                                i2++;
                                this.mItems.add(NavigationMenuItem.separator(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                            }
                            z = z3;
                        } else if (!z && menuItemImpl.getIcon() != null) {
                            appendTransparentIconIfMissing(i2, this.mItems.size());
                            z = true;
                        }
                        if (z && menuItemImpl.getIcon() == null) {
                            menuItemImpl.setIcon(17170445);
                        }
                        this.mItems.add(NavigationMenuItem.m2of(menuItemImpl));
                        i = groupId;
                    }
                }
                this.mUpdateSuspended = false;
            }
        }

        private void appendTransparentIconIfMissing(int i, int i2) {
            while (i < i2) {
                MenuItemImpl menuItem = ((NavigationMenuItem) this.mItems.get(i)).getMenuItem();
                if (menuItem.getIcon() == null) {
                    if (this.mTransparentIcon == null) {
                        this.mTransparentIcon = new ColorDrawable(17170445);
                    }
                    menuItem.setIcon(this.mTransparentIcon);
                }
                i++;
            }
        }

        public void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.mCheckedItem != menuItemImpl && menuItemImpl.isCheckable()) {
                MenuItemImpl menuItemImpl2 = this.mCheckedItem;
                if (menuItemImpl2 != null) {
                    menuItemImpl2.setChecked(false);
                }
                this.mCheckedItem = menuItemImpl;
                menuItemImpl.setChecked(true);
            }
        }

        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            MenuItemImpl menuItemImpl = this.mCheckedItem;
            if (menuItemImpl != null) {
                bundle.putInt(STATE_CHECKED_ITEM, menuItemImpl.getItemId());
            }
            return bundle;
        }

        public void restoreInstanceState(Bundle bundle) {
            int i = bundle.getInt(STATE_CHECKED_ITEM, 0);
            if (i != 0) {
                this.mUpdateSuspended = true;
                Iterator it = this.mItems.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    MenuItemImpl menuItem = ((NavigationMenuItem) it.next()).getMenuItem();
                    if (menuItem != null && menuItem.getItemId() == i) {
                        setCheckedItem(menuItem);
                        break;
                    }
                }
                this.mUpdateSuspended = false;
                prepareMenuItems();
            }
        }

        public void setUpdateSuspended(boolean z) {
            this.mUpdateSuspended = z;
        }
    }

    private static class NavigationMenuItem {
        private final MenuItemImpl mMenuItem;
        private final int mPaddingBottom;
        private final int mPaddingTop;

        private NavigationMenuItem(MenuItemImpl menuItemImpl, int i, int i2) {
            this.mMenuItem = menuItemImpl;
            this.mPaddingTop = i;
            this.mPaddingBottom = i2;
        }

        /* renamed from: of */
        public static NavigationMenuItem m2of(MenuItemImpl menuItemImpl) {
            return new NavigationMenuItem(menuItemImpl, 0, 0);
        }

        public static NavigationMenuItem separator(int i, int i2) {
            return new NavigationMenuItem(null, i, i2);
        }

        public boolean isSeparator() {
            return this.mMenuItem == null;
        }

        public int getPaddingTop() {
            return this.mPaddingTop;
        }

        public int getPaddingBottom() {
            return this.mPaddingBottom;
        }

        public MenuItemImpl getMenuItem() {
            return this.mMenuItem;
        }

        public boolean isEnabled() {
            MenuItemImpl menuItemImpl = this.mMenuItem;
            return menuItemImpl != null && !menuItemImpl.hasSubMenu() && this.mMenuItem.isEnabled();
        }
    }

    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean flagActionItems() {
        return false;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
        Resources resources = context.getResources();
        this.mPaddingTopDefault = resources.getDimensionPixelOffset(C0010R.dimen.design_navigation_padding_top_default);
        this.mPaddingSeparator = resources.getDimensionPixelOffset(C0010R.dimen.design_navigation_separator_vertical_padding);
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (NavigationMenuView) this.mLayoutInflater.inflate(C0010R.layout.design_navigation_menu, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new NavigationMenuAdapter();
            }
            this.mHeader = (LinearLayout) this.mLayoutInflater.inflate(C0010R.layout.design_navigation_item_header, this.mMenuView, false);
            this.mMenuView.addHeaderView(this.mHeader, null, false);
            this.mMenuView.setAdapter(this.mAdapter);
            this.mMenuView.setOnItemClickListener(this);
        }
        return this.mMenuView;
    }

    public void updateMenuView(boolean z) {
        NavigationMenuAdapter navigationMenuAdapter = this.mAdapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.notifyDataSetChanged();
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (this.mMenuView != null) {
            SparseArray sparseArray = new SparseArray();
            this.mMenuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        NavigationMenuAdapter navigationMenuAdapter = this.mAdapter;
        if (navigationMenuAdapter != null) {
            bundle.putBundle(STATE_ADAPTER, navigationMenuAdapter.createInstanceState());
        }
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
        Bundle bundle2 = bundle.getBundle(STATE_ADAPTER);
        if (bundle2 != null) {
            this.mAdapter.restoreInstanceState(bundle2);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        int headerViewsCount = i - this.mMenuView.getHeaderViewsCount();
        if (headerViewsCount >= 0) {
            setUpdateSuspended(true);
            MenuItemImpl menuItem = this.mAdapter.getItem(headerViewsCount).getMenuItem();
            if (menuItem != null && menuItem.isCheckable()) {
                this.mAdapter.setCheckedItem(menuItem);
            }
            this.mMenu.performItemAction(menuItem, this, 0);
            setUpdateSuspended(false);
            updateMenuView(false);
        }
    }

    public void setCheckedItem(MenuItemImpl menuItemImpl) {
        this.mAdapter.setCheckedItem(menuItemImpl);
    }

    public View inflateHeaderView(int i) {
        View inflate = this.mLayoutInflater.inflate(i, this.mHeader, false);
        addHeaderView(inflate);
        return inflate;
    }

    public void addHeaderView(View view) {
        this.mHeader.addView(view);
        NavigationMenuView navigationMenuView = this.mMenuView;
        navigationMenuView.setPadding(0, 0, 0, navigationMenuView.getPaddingBottom());
    }

    public void removeHeaderView(View view) {
        this.mHeader.removeView(view);
        if (this.mHeader.getChildCount() == 0) {
            NavigationMenuView navigationMenuView = this.mMenuView;
            navigationMenuView.setPadding(0, this.mPaddingTopDefault, 0, navigationMenuView.getPaddingBottom());
        }
    }

    public ColorStateList getItemTintList() {
        return this.mIconTintList;
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.mIconTintList = colorStateList;
        updateMenuView(false);
    }

    public ColorStateList getItemTextColor() {
        return this.mTextColor;
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        updateMenuView(false);
    }

    public void setItemTextAppearance(int i) {
        this.mTextAppearance = i;
        this.mTextAppearanceSet = true;
        updateMenuView(false);
    }

    public Drawable getItemBackground() {
        return this.mItemBackground;
    }

    public void setItemBackground(Drawable drawable) {
        this.mItemBackground = drawable;
    }

    public void setUpdateSuspended(boolean z) {
        NavigationMenuAdapter navigationMenuAdapter = this.mAdapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.setUpdateSuspended(z);
        }
    }
}
