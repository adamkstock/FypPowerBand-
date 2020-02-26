package android.support.design.internal;

import android.content.Context;
import android.support.p003v7.internal.view.menu.MenuBuilder;
import android.support.p003v7.internal.view.menu.MenuItemImpl;
import android.support.p003v7.internal.view.menu.SubMenuBuilder;
import android.view.MenuItem;

public class NavigationSubMenu extends SubMenuBuilder {
    public NavigationSubMenu(Context context, NavigationMenu navigationMenu, MenuItemImpl menuItemImpl) {
        super(context, navigationMenu, menuItemImpl);
    }

    public MenuItem add(CharSequence charSequence) {
        MenuItem add = super.add(charSequence);
        notifyParent();
        return add;
    }

    public MenuItem add(int i) {
        MenuItem add = super.add(i);
        notifyParent();
        return add;
    }

    public MenuItem add(int i, int i2, int i3, CharSequence charSequence) {
        MenuItem add = super.add(i, i2, i3, charSequence);
        notifyParent();
        return add;
    }

    public MenuItem add(int i, int i2, int i3, int i4) {
        MenuItem add = super.add(i, i2, i3, i4);
        notifyParent();
        return add;
    }

    private void notifyParent() {
        ((MenuBuilder) getParentMenu()).onItemsChanged(true);
    }
}
