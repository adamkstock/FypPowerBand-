package android.support.p003v7.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.graphics.ColorUtils;
import android.support.p000v4.util.LruCache;
import android.support.p003v7.appcompat.C0251R;
import android.util.SparseArray;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/* renamed from: android.support.v7.internal.widget.TintManager */
public final class TintManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {C0251R.C0252drawable.abc_popup_background_mtrl_mult, C0251R.C0252drawable.abc_cab_background_internal_bg, C0251R.C0252drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {C0251R.C0252drawable.abc_textfield_activated_mtrl_alpha, C0251R.C0252drawable.abc_textfield_search_activated_mtrl_alpha, C0251R.C0252drawable.abc_cab_background_top_mtrl_alpha, C0251R.C0252drawable.abc_text_cursor_material};
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {C0251R.C0252drawable.abc_textfield_search_default_mtrl_alpha, C0251R.C0252drawable.abc_textfield_default_mtrl_alpha, C0251R.C0252drawable.abc_ab_share_pack_mtrl_alpha};
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final boolean DEBUG = false;
    private static final Mode DEFAULT_MODE = Mode.SRC_IN;
    private static final WeakHashMap<Context, TintManager> INSTANCE_CACHE = new WeakHashMap<>();
    public static final boolean SHOULD_BE_USED = (VERSION.SDK_INT < 21);
    private static final String TAG = "TintManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = {C0251R.C0252drawable.abc_btn_check_material, C0251R.C0252drawable.abc_btn_radio_material};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = {C0251R.C0252drawable.abc_ic_ab_back_mtrl_am_alpha, C0251R.C0252drawable.abc_ic_go_search_api_mtrl_alpha, C0251R.C0252drawable.abc_ic_search_api_mtrl_alpha, C0251R.C0252drawable.abc_ic_commit_search_api_mtrl_alpha, C0251R.C0252drawable.abc_ic_clear_mtrl_alpha, C0251R.C0252drawable.abc_ic_menu_share_mtrl_alpha, C0251R.C0252drawable.abc_ic_menu_copy_mtrl_am_alpha, C0251R.C0252drawable.abc_ic_menu_cut_mtrl_alpha, C0251R.C0252drawable.abc_ic_menu_selectall_mtrl_alpha, C0251R.C0252drawable.abc_ic_menu_paste_mtrl_am_alpha, C0251R.C0252drawable.abc_ic_menu_moreoverflow_mtrl_alpha, C0251R.C0252drawable.abc_ic_voice_search_api_mtrl_alpha};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = {C0251R.C0252drawable.abc_edit_text_material, C0251R.C0252drawable.abc_tab_indicator_material, C0251R.C0252drawable.abc_textfield_search_material, C0251R.C0252drawable.abc_spinner_mtrl_am_alpha, C0251R.C0252drawable.abc_spinner_textfield_background_material, C0251R.C0252drawable.abc_ratingbar_full_material, C0251R.C0252drawable.abc_switch_track_mtrl_alpha, C0251R.C0252drawable.abc_switch_thumb_material, C0251R.C0252drawable.abc_btn_default_mtrl_shape, C0251R.C0252drawable.abc_btn_borderless_material};
    private final WeakReference<Context> mContextRef;
    private ColorStateList mDefaultColorStateList;
    private SparseArray<ColorStateList> mTintLists;

    /* renamed from: android.support.v7.internal.widget.TintManager$ColorFilterLruCache */
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int i) {
            super(i);
        }

        /* access modifiers changed from: 0000 */
        public PorterDuffColorFilter get(int i, Mode mode) {
            return (PorterDuffColorFilter) get(Integer.valueOf(generateCacheKey(i, mode)));
        }

        /* access modifiers changed from: 0000 */
        public PorterDuffColorFilter put(int i, Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return (PorterDuffColorFilter) put(Integer.valueOf(generateCacheKey(i, mode)), porterDuffColorFilter);
        }

        private static int generateCacheKey(int i, Mode mode) {
            return ((i + 31) * 31) + mode.hashCode();
        }
    }

    public static Drawable getDrawable(Context context, int i) {
        if (isInTintList(i)) {
            return get(context).getDrawable(i);
        }
        return ContextCompat.getDrawable(context, i);
    }

    public static TintManager get(Context context) {
        TintManager tintManager = (TintManager) INSTANCE_CACHE.get(context);
        if (tintManager != null) {
            return tintManager;
        }
        TintManager tintManager2 = new TintManager(context);
        INSTANCE_CACHE.put(context, tintManager2);
        return tintManager2;
    }

    private TintManager(Context context) {
        this.mContextRef = new WeakReference<>(context);
    }

    public Drawable getDrawable(int i) {
        return getDrawable(i, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0059, code lost:
        if (r6 != false) goto L_0x0032;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable getDrawable(int r5, boolean r6) {
        /*
            r4 = this;
            java.lang.ref.WeakReference<android.content.Context> r0 = r4.mContextRef
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            android.graphics.drawable.Drawable r0 = android.support.p000v4.content.ContextCompat.getDrawable(r0, r5)
            if (r0 == 0) goto L_0x005c
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 8
            if (r2 < r3) goto L_0x001c
            android.graphics.drawable.Drawable r0 = r0.mutate()
        L_0x001c:
            android.content.res.ColorStateList r2 = r4.getTintList(r5)
            if (r2 == 0) goto L_0x0034
            android.graphics.drawable.Drawable r1 = android.support.p000v4.graphics.drawable.DrawableCompat.wrap(r0)
            android.support.p000v4.graphics.drawable.DrawableCompat.setTintList(r1, r2)
            android.graphics.PorterDuff$Mode r5 = r4.getTintMode(r5)
            if (r5 == 0) goto L_0x0032
            android.support.p000v4.graphics.drawable.DrawableCompat.setTintMode(r1, r5)
        L_0x0032:
            r0 = r1
            goto L_0x005c
        L_0x0034:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_cab_background_top_material
            if (r5 != r2) goto L_0x0053
            android.graphics.drawable.LayerDrawable r5 = new android.graphics.drawable.LayerDrawable
            r6 = 2
            android.graphics.drawable.Drawable[] r6 = new android.graphics.drawable.Drawable[r6]
            r0 = 0
            int r1 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_cab_background_internal_bg
            android.graphics.drawable.Drawable r1 = r4.getDrawable(r1)
            r6[r0] = r1
            r0 = 1
            int r1 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_cab_background_top_mtrl_alpha
            android.graphics.drawable.Drawable r1 = r4.getDrawable(r1)
            r6[r0] = r1
            r5.<init>(r6)
            return r5
        L_0x0053:
            boolean r5 = r4.tintDrawableUsingColorFilter(r5, r0)
            if (r5 != 0) goto L_0x005c
            if (r6 == 0) goto L_0x005c
            goto L_0x0032
        L_0x005c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.internal.widget.TintManager.getDrawable(int, boolean):android.graphics.drawable.Drawable");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0069 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean tintDrawableUsingColorFilter(int r8, android.graphics.drawable.Drawable r9) {
        /*
            r7 = this;
            java.lang.ref.WeakReference<android.content.Context> r0 = r7.mContextRef
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            android.graphics.PorterDuff$Mode r2 = DEFAULT_MODE
            int[] r3 = COLORFILTER_TINT_COLOR_CONTROL_NORMAL
            boolean r3 = arrayContains(r3, r8)
            r4 = -1
            r5 = 1
            if (r3 == 0) goto L_0x001f
            int r8 = android.support.p003v7.appcompat.C0251R.attr.colorControlNormal
        L_0x001a:
            r3 = r2
            r6 = -1
            r2 = r8
            r8 = 1
            goto L_0x0056
        L_0x001f:
            int[] r3 = COLORFILTER_COLOR_CONTROL_ACTIVATED
            boolean r3 = arrayContains(r3, r8)
            if (r3 == 0) goto L_0x002a
            int r8 = android.support.p003v7.appcompat.C0251R.attr.colorControlActivated
            goto L_0x001a
        L_0x002a:
            int[] r3 = COLORFILTER_COLOR_BACKGROUND_MULTIPLY
            boolean r3 = arrayContains(r3, r8)
            if (r3 == 0) goto L_0x003d
            r8 = 16842801(0x1010031, float:2.3693695E-38)
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.MULTIPLY
            r3 = r2
            r8 = 1
            r2 = 16842801(0x1010031, float:2.3693695E-38)
            goto L_0x0055
        L_0x003d:
            int r3 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_list_divider_mtrl_alpha
            if (r8 != r3) goto L_0x0052
            r8 = 16842800(0x1010030, float:2.3693693E-38)
            r3 = 1109603123(0x42233333, float:40.8)
            int r3 = java.lang.Math.round(r3)
            r6 = r3
            r8 = 1
            r3 = r2
            r2 = 16842800(0x1010030, float:2.3693693E-38)
            goto L_0x0056
        L_0x0052:
            r3 = r2
            r8 = 0
            r2 = 0
        L_0x0055:
            r6 = -1
        L_0x0056:
            if (r8 == 0) goto L_0x0069
            int r8 = android.support.p003v7.internal.widget.ThemeUtils.getThemeAttrColor(r0, r2)
            android.graphics.PorterDuffColorFilter r8 = getPorterDuffColorFilter(r8, r3)
            r9.setColorFilter(r8)
            if (r6 == r4) goto L_0x0068
            r9.setAlpha(r6)
        L_0x0068:
            return r5
        L_0x0069:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.internal.widget.TintManager.tintDrawableUsingColorFilter(int, android.graphics.drawable.Drawable):boolean");
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInTintList(int i) {
        return arrayContains(TINT_COLOR_CONTROL_NORMAL, i) || arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, i) || arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, i) || arrayContains(TINT_COLOR_CONTROL_STATE_LIST, i) || arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, i) || arrayContains(TINT_CHECKABLE_BUTTON_LIST, i) || i == C0251R.C0252drawable.abc_cab_background_top_material;
    }

    /* access modifiers changed from: 0000 */
    public final Mode getTintMode(int i) {
        if (i == C0251R.C0252drawable.abc_switch_thumb_material) {
            return Mode.MULTIPLY;
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.content.res.ColorStateList getTintList(int r4) {
        /*
            r3 = this;
            java.lang.ref.WeakReference<android.content.Context> r0 = r3.mContextRef
            java.lang.Object r0 = r0.get()
            android.content.Context r0 = (android.content.Context) r0
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            android.util.SparseArray<android.content.res.ColorStateList> r2 = r3.mTintLists
            if (r2 == 0) goto L_0x0016
            java.lang.Object r1 = r2.get(r4)
            android.content.res.ColorStateList r1 = (android.content.res.ColorStateList) r1
        L_0x0016:
            if (r1 != 0) goto L_0x0095
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_edit_text_material
            if (r4 != r2) goto L_0x0023
            android.content.res.ColorStateList r0 = r3.createEditTextColorStateList(r0)
        L_0x0020:
            r1 = r0
            goto L_0x0083
        L_0x0023:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_switch_track_mtrl_alpha
            if (r4 != r2) goto L_0x002c
            android.content.res.ColorStateList r0 = r3.createSwitchTrackColorStateList(r0)
            goto L_0x0020
        L_0x002c:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_switch_thumb_material
            if (r4 != r2) goto L_0x0035
            android.content.res.ColorStateList r0 = r3.createSwitchThumbColorStateList(r0)
            goto L_0x0020
        L_0x0035:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_btn_default_mtrl_shape
            if (r4 == r2) goto L_0x007e
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_btn_borderless_material
            if (r4 != r2) goto L_0x003e
            goto L_0x007e
        L_0x003e:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_btn_colored_material
            if (r4 != r2) goto L_0x0047
            android.content.res.ColorStateList r0 = r3.createColoredButtonColorStateList(r0)
            goto L_0x0020
        L_0x0047:
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_spinner_mtrl_am_alpha
            if (r4 == r2) goto L_0x0079
            int r2 = android.support.p003v7.appcompat.C0251R.C0252drawable.abc_spinner_textfield_background_material
            if (r4 != r2) goto L_0x0050
            goto L_0x0079
        L_0x0050:
            int[] r2 = TINT_COLOR_CONTROL_NORMAL
            boolean r2 = arrayContains(r2, r4)
            if (r2 == 0) goto L_0x005f
            int r1 = android.support.p003v7.appcompat.C0251R.attr.colorControlNormal
            android.content.res.ColorStateList r0 = android.support.p003v7.internal.widget.ThemeUtils.getThemeAttrColorStateList(r0, r1)
            goto L_0x0020
        L_0x005f:
            int[] r2 = TINT_COLOR_CONTROL_STATE_LIST
            boolean r2 = arrayContains(r2, r4)
            if (r2 == 0) goto L_0x006c
            android.content.res.ColorStateList r0 = r3.getDefaultColorStateList(r0)
            goto L_0x0020
        L_0x006c:
            int[] r2 = TINT_CHECKABLE_BUTTON_LIST
            boolean r2 = arrayContains(r2, r4)
            if (r2 == 0) goto L_0x0083
            android.content.res.ColorStateList r0 = r3.createCheckableButtonColorStateList(r0)
            goto L_0x0020
        L_0x0079:
            android.content.res.ColorStateList r0 = r3.createSpinnerColorStateList(r0)
            goto L_0x0020
        L_0x007e:
            android.content.res.ColorStateList r0 = r3.createDefaultButtonColorStateList(r0)
            goto L_0x0020
        L_0x0083:
            if (r1 == 0) goto L_0x0095
            android.util.SparseArray<android.content.res.ColorStateList> r0 = r3.mTintLists
            if (r0 != 0) goto L_0x0090
            android.util.SparseArray r0 = new android.util.SparseArray
            r0.<init>()
            r3.mTintLists = r0
        L_0x0090:
            android.util.SparseArray<android.content.res.ColorStateList> r0 = r3.mTintLists
            r0.append(r4, r1)
        L_0x0095:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.internal.widget.TintManager.getTintList(int):android.content.res.ColorStateList");
    }

    private ColorStateList getDefaultColorStateList(Context context) {
        if (this.mDefaultColorStateList == null) {
            int themeAttrColor = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlNormal);
            int themeAttrColor2 = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated);
            this.mDefaultColorStateList = new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.ACTIVATED_STATE_SET, ThemeUtils.PRESSED_STATE_SET, ThemeUtils.CHECKED_STATE_SET, ThemeUtils.SELECTED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorControlNormal), themeAttrColor2, themeAttrColor2, themeAttrColor2, themeAttrColor2, themeAttrColor2, themeAttrColor});
        }
        return this.mDefaultColorStateList;
    }

    private ColorStateList createCheckableButtonColorStateList(Context context) {
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.CHECKED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorControlNormal), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlNormal)});
    }

    private ColorStateList createSwitchTrackColorStateList(Context context) {
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.CHECKED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getThemeAttrColor(context, 16842800, 0.1f), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated, 0.3f), ThemeUtils.getThemeAttrColor(context, 16842800, 0.3f)});
    }

    private ColorStateList createSwitchThumbColorStateList(Context context) {
        int[][] iArr = new int[3][];
        int[] iArr2 = new int[3];
        ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, C0251R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList == null || !themeAttrColorStateList.isStateful()) {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorSwitchThumbNormal);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorSwitchThumbNormal);
        } else {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = themeAttrColorStateList.getColorForState(iArr[0], 0);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = themeAttrColorStateList.getDefaultColor();
        }
        return new ColorStateList(iArr, iArr2);
    }

    private ColorStateList createEditTextColorStateList(Context context) {
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorControlNormal), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlNormal), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated)});
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return createButtonColorStateList(context, C0251R.attr.colorButtonNormal);
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return createButtonColorStateList(context, C0251R.attr.colorAccent);
    }

    private ColorStateList createButtonColorStateList(Context context, int i) {
        int themeAttrColor = ThemeUtils.getThemeAttrColor(context, i);
        int themeAttrColor2 = ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlHighlight);
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.PRESSED_STATE_SET, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorButtonNormal), ColorUtils.compositeColors(themeAttrColor2, themeAttrColor), ColorUtils.compositeColors(themeAttrColor2, themeAttrColor), themeAttrColor});
    }

    private ColorStateList createSpinnerColorStateList(Context context) {
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0251R.attr.colorControlNormal), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlNormal), ThemeUtils.getThemeAttrColor(context, C0251R.attr.colorControlActivated)});
    }

    public static void tintViewBackground(View view, TintInfo tintInfo) {
        Drawable background = view.getBackground();
        if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
            background.setColorFilter(createTintFilter(tintInfo.mHasTintList ? tintInfo.mTintList : null, tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE, view.getDrawableState()));
        } else {
            background.clearColorFilter();
        }
        if (VERSION.SDK_INT <= 10) {
            view.invalidate();
        }
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return getPorterDuffColorFilter(colorStateList.getColorForState(iArr, 0), mode);
    }

    private static PorterDuffColorFilter getPorterDuffColorFilter(int i, Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter = COLOR_FILTER_CACHE.get(i, mode);
        if (porterDuffColorFilter != null) {
            return porterDuffColorFilter;
        }
        PorterDuffColorFilter porterDuffColorFilter2 = new PorterDuffColorFilter(i, mode);
        COLOR_FILTER_CACHE.put(i, mode, porterDuffColorFilter2);
        return porterDuffColorFilter2;
    }
}
