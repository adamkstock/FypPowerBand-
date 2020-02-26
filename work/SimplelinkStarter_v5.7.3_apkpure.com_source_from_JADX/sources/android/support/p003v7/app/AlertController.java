package android.support.p003v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.appcompat.C0251R;
import android.support.p003v7.internal.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.lang.ref.WeakReference;

/* renamed from: android.support.v7.app.AlertController */
class AlertController {
    /* access modifiers changed from: private */
    public ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final OnClickListener mButtonHandler = new OnClickListener() {
        public void onClick(View view) {
            Message message = (view != AlertController.this.mButtonPositive || AlertController.this.mButtonPositiveMessage == null) ? (view != AlertController.this.mButtonNegative || AlertController.this.mButtonNegativeMessage == null) ? (view != AlertController.this.mButtonNeutral || AlertController.this.mButtonNeutralMessage == null) ? null : Message.obtain(AlertController.this.mButtonNeutralMessage) : Message.obtain(AlertController.this.mButtonNegativeMessage) : Message.obtain(AlertController.this.mButtonPositiveMessage);
            if (message != null) {
                message.sendToTarget();
            }
            AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
        }
    };
    /* access modifiers changed from: private */
    public Button mButtonNegative;
    /* access modifiers changed from: private */
    public Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    /* access modifiers changed from: private */
    public Button mButtonNeutral;
    /* access modifiers changed from: private */
    public Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelLayoutHint = 0;
    private int mButtonPanelSideLayout;
    /* access modifiers changed from: private */
    public Button mButtonPositive;
    /* access modifiers changed from: private */
    public Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    /* access modifiers changed from: private */
    public int mCheckedItem = -1;
    private final Context mContext;
    private View mCustomTitleView;
    /* access modifiers changed from: private */
    public final AppCompatDialog mDialog;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Drawable mIcon;
    private int mIconId = 0;
    private ImageView mIconView;
    /* access modifiers changed from: private */
    public int mListItemLayout;
    /* access modifiers changed from: private */
    public int mListLayout;
    /* access modifiers changed from: private */
    public ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    /* access modifiers changed from: private */
    public int mMultiChoiceItemLayout;
    private ScrollView mScrollView;
    /* access modifiers changed from: private */
    public int mSingleChoiceItemLayout;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewLayoutResId;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private boolean mViewSpacingSpecified = false;
    private int mViewSpacingTop;
    private final Window mWindow;

    /* renamed from: android.support.v7.app.AlertController$AlertParams */
    public static class AlertParams {
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem = -1;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId = 0;
        public int mIconId = 0;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public OnCancelListener mOnCancelListener;
        public OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public OnDismissListener mOnDismissListener;
        public OnItemSelectedListener mOnItemSelectedListener;
        public OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure = true;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified = false;
        public int mViewSpacingTop;

        /* renamed from: android.support.v7.app.AlertController$AlertParams$OnPrepareListViewListener */
        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void apply(AlertController alertController) {
            View view = this.mCustomTitleView;
            if (view != null) {
                alertController.setCustomTitle(view);
            } else {
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    alertController.setTitle(charSequence);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    alertController.setIcon(drawable);
                }
                int i = this.mIconId;
                if (i != 0) {
                    alertController.setIcon(i);
                }
                int i2 = this.mIconAttrId;
                if (i2 != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(i2));
                }
            }
            CharSequence charSequence2 = this.mMessage;
            if (charSequence2 != null) {
                alertController.setMessage(charSequence2);
            }
            CharSequence charSequence3 = this.mPositiveButtonText;
            if (charSequence3 != null) {
                alertController.setButton(-1, charSequence3, this.mPositiveButtonListener, null);
            }
            CharSequence charSequence4 = this.mNegativeButtonText;
            if (charSequence4 != null) {
                alertController.setButton(-2, charSequence4, this.mNegativeButtonListener, null);
            }
            CharSequence charSequence5 = this.mNeutralButtonText;
            if (charSequence5 != null) {
                alertController.setButton(-3, charSequence5, this.mNeutralButtonListener, null);
            }
            if (!(this.mItems == null && this.mCursor == null && this.mAdapter == null)) {
                createListView(alertController);
            }
            View view2 = this.mView;
            if (view2 == null) {
                int i3 = this.mViewLayoutResId;
                if (i3 != 0) {
                    alertController.setView(i3);
                }
            } else if (this.mViewSpacingSpecified) {
                alertController.setView(view2, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            } else {
                alertController.setView(view2);
            }
        }

        /* JADX WARNING: type inference failed for: r9v0, types: [android.widget.ListAdapter] */
        /* JADX WARNING: type inference failed for: r2v2, types: [android.widget.SimpleCursorAdapter] */
        /* JADX WARNING: type inference failed for: r2v3, types: [android.widget.ListAdapter] */
        /* JADX WARNING: type inference failed for: r2v4 */
        /* JADX WARNING: type inference failed for: r9v2 */
        /* JADX WARNING: type inference failed for: r2v5, types: [android.support.v7.app.AlertController$CheckedItemAdapter] */
        /* JADX WARNING: type inference failed for: r1v17, types: [android.support.v7.app.AlertController$AlertParams$2] */
        /* JADX WARNING: type inference failed for: r1v18, types: [android.support.v7.app.AlertController$AlertParams$1] */
        /* JADX WARNING: type inference failed for: r2v8, types: [android.widget.SimpleCursorAdapter] */
        /* JADX WARNING: type inference failed for: r2v9 */
        /* JADX WARNING: type inference failed for: r1v19, types: [android.support.v7.app.AlertController$AlertParams$2] */
        /* JADX WARNING: type inference failed for: r1v20, types: [android.support.v7.app.AlertController$AlertParams$1] */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r9v2
          assigns: [?[OBJECT, ARRAY], android.widget.SimpleCursorAdapter, android.support.v7.app.AlertController$AlertParams$2, android.support.v7.app.AlertController$AlertParams$1]
          uses: [android.widget.ListAdapter, android.widget.SimpleCursorAdapter, android.support.v7.app.AlertController$AlertParams$2, android.support.v7.app.AlertController$AlertParams$1]
          mth insns count: 69
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 6 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void createListView(final android.support.p003v7.app.AlertController r12) {
            /*
                r11 = this;
                android.view.LayoutInflater r0 = r11.mInflater
                int r1 = r12.mListLayout
                r2 = 0
                android.view.View r0 = r0.inflate(r1, r2)
                android.widget.ListView r0 = (android.widget.ListView) r0
                boolean r1 = r11.mIsMultiChoice
                r8 = 1
                if (r1 == 0) goto L_0x0037
                android.database.Cursor r4 = r11.mCursor
                if (r4 != 0) goto L_0x002a
                android.support.v7.app.AlertController$AlertParams$1 r9 = new android.support.v7.app.AlertController$AlertParams$1
                android.content.Context r3 = r11.mContext
                int r4 = r12.mMultiChoiceItemLayout
                r5 = 16908308(0x1020014, float:2.3877285E-38)
                java.lang.CharSequence[] r6 = r11.mItems
                r1 = r9
                r2 = r11
                r7 = r0
                r1.<init>(r3, r4, r5, r6, r7)
                goto L_0x0070
            L_0x002a:
                android.support.v7.app.AlertController$AlertParams$2 r9 = new android.support.v7.app.AlertController$AlertParams$2
                android.content.Context r3 = r11.mContext
                r5 = 0
                r1 = r9
                r2 = r11
                r6 = r0
                r7 = r12
                r1.<init>(r3, r4, r5, r6, r7)
                goto L_0x0070
            L_0x0037:
                boolean r1 = r11.mIsSingleChoice
                if (r1 == 0) goto L_0x0040
                int r1 = r12.mSingleChoiceItemLayout
                goto L_0x0044
            L_0x0040:
                int r1 = r12.mListItemLayout
            L_0x0044:
                r4 = r1
                android.database.Cursor r5 = r11.mCursor
                r1 = 16908308(0x1020014, float:2.3877285E-38)
                if (r5 != 0) goto L_0x005c
                android.widget.ListAdapter r2 = r11.mAdapter
                if (r2 == 0) goto L_0x0051
                goto L_0x005a
            L_0x0051:
                android.support.v7.app.AlertController$CheckedItemAdapter r2 = new android.support.v7.app.AlertController$CheckedItemAdapter
                android.content.Context r3 = r11.mContext
                java.lang.CharSequence[] r5 = r11.mItems
                r2.<init>(r3, r4, r1, r5)
            L_0x005a:
                r9 = r2
                goto L_0x0070
            L_0x005c:
                android.widget.SimpleCursorAdapter r9 = new android.widget.SimpleCursorAdapter
                android.content.Context r3 = r11.mContext
                java.lang.String[] r6 = new java.lang.String[r8]
                java.lang.String r2 = r11.mLabelColumn
                r7 = 0
                r6[r7] = r2
                int[] r10 = new int[r8]
                r10[r7] = r1
                r2 = r9
                r7 = r10
                r2.<init>(r3, r4, r5, r6, r7)
            L_0x0070:
                android.support.v7.app.AlertController$AlertParams$OnPrepareListViewListener r1 = r11.mOnPrepareListViewListener
                if (r1 == 0) goto L_0x0077
                r1.onPrepareListView(r0)
            L_0x0077:
                r12.mAdapter = r9
                int r1 = r11.mCheckedItem
                r12.mCheckedItem = r1
                android.content.DialogInterface$OnClickListener r1 = r11.mOnClickListener
                if (r1 == 0) goto L_0x008c
                android.support.v7.app.AlertController$AlertParams$3 r1 = new android.support.v7.app.AlertController$AlertParams$3
                r1.<init>(r12)
                r0.setOnItemClickListener(r1)
                goto L_0x0098
            L_0x008c:
                android.content.DialogInterface$OnMultiChoiceClickListener r1 = r11.mOnCheckboxClickListener
                if (r1 == 0) goto L_0x0098
                android.support.v7.app.AlertController$AlertParams$4 r1 = new android.support.v7.app.AlertController$AlertParams$4
                r1.<init>(r0, r12)
                r0.setOnItemClickListener(r1)
            L_0x0098:
                android.widget.AdapterView$OnItemSelectedListener r1 = r11.mOnItemSelectedListener
                if (r1 == 0) goto L_0x009f
                r0.setOnItemSelectedListener(r1)
            L_0x009f:
                boolean r1 = r11.mIsSingleChoice
                if (r1 == 0) goto L_0x00a7
                r0.setChoiceMode(r8)
                goto L_0x00af
            L_0x00a7:
                boolean r1 = r11.mIsMultiChoice
                if (r1 == 0) goto L_0x00af
                r1 = 2
                r0.setChoiceMode(r1)
            L_0x00af:
                r12.mListView = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.p003v7.app.AlertController.AlertParams.createListView(android.support.v7.app.AlertController):void");
        }
    }

    /* renamed from: android.support.v7.app.AlertController$ButtonHandler */
    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick((DialogInterface) this.mDialog.get(), message.what);
            } else if (i == 1) {
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    /* renamed from: android.support.v7.app.AlertController$CheckedItemAdapter */
    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return true;
        }

        public CheckedItemAdapter(Context context, int i, int i2, CharSequence[] charSequenceArr) {
            super(context, i, i2, charSequenceArr);
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(C0251R.attr.alertDialogCenterButtons, typedValue, true);
        if (typedValue.data != 0) {
            return true;
        }
        return false;
    }

    public AlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        this.mContext = context;
        this.mDialog = appCompatDialog;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(appCompatDialog);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, C0251R.styleable.AlertDialog, C0251R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = obtainStyledAttributes.getResourceId(C0251R.styleable.AlertDialog_listItemLayout, 0);
        obtainStyledAttributes.recycle();
    }

    static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    public void installContent() {
        this.mDialog.supportRequestWindowFeature(1);
        this.mDialog.setContentView(selectContentView());
        setupView();
    }

    private int selectContentView() {
        int i = this.mButtonPanelSideLayout;
        if (i == 0) {
            return this.mAlertDialogLayout;
        }
        if (this.mButtonPanelLayoutHint == 1) {
            return i;
        }
        return this.mAlertDialogLayout;
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setCustomTitle(View view) {
        this.mCustomTitleView = view;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setView(int i) {
        this.mView = null;
        this.mViewLayoutResId = i;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view, int i, int i2, int i3, int i4) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = i;
        this.mViewSpacingTop = i2;
        this.mViewSpacingRight = i3;
        this.mViewSpacingBottom = i4;
    }

    public void setButtonPanelLayoutHint(int i) {
        this.mButtonPanelLayoutHint = i;
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message) {
        if (message == null && onClickListener != null) {
            message = this.mHandler.obtainMessage(i, onClickListener);
        }
        if (i == -3) {
            this.mButtonNeutralText = charSequence;
            this.mButtonNeutralMessage = message;
        } else if (i == -2) {
            this.mButtonNegativeText = charSequence;
            this.mButtonNegativeMessage = message;
        } else if (i == -1) {
            this.mButtonPositiveText = charSequence;
            this.mButtonPositiveMessage = message;
        } else {
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setIcon(int i) {
        this.mIcon = null;
        this.mIconId = i;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (i != 0) {
            imageView.setImageResource(this.mIconId);
        } else {
            imageView.setVisibility(8);
        }
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(8);
        }
    }

    public int getIconAttributeResId(int i) {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public Button getButton(int i) {
        if (i == -3) {
            return this.mButtonNeutral;
        }
        if (i == -2) {
            return this.mButtonNegative;
        }
        if (i != -1) {
            return null;
        }
        return this.mButtonPositive;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        ScrollView scrollView = this.mScrollView;
        return scrollView != null && scrollView.executeKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        ScrollView scrollView = this.mScrollView;
        return scrollView != null && scrollView.executeKeyEvent(keyEvent);
    }

    private void setupView() {
        setupContent((ViewGroup) this.mWindow.findViewById(C0251R.C0253id.contentPanel));
        boolean z = setupButtons();
        ViewGroup viewGroup = (ViewGroup) this.mWindow.findViewById(C0251R.C0253id.topPanel);
        View view = null;
        boolean z2 = false;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mContext, null, C0251R.styleable.AlertDialog, C0251R.attr.alertDialogStyle, 0);
        setupTitle(viewGroup);
        View findViewById = this.mWindow.findViewById(C0251R.C0253id.buttonPanel);
        if (!z) {
            findViewById.setVisibility(8);
            View findViewById2 = this.mWindow.findViewById(C0251R.C0253id.textSpacerNoButtons);
            if (findViewById2 != null) {
                findViewById2.setVisibility(0);
            }
        }
        FrameLayout frameLayout = (FrameLayout) this.mWindow.findViewById(C0251R.C0253id.customPanel);
        View view2 = this.mView;
        if (view2 != null) {
            view = view2;
        } else if (this.mViewLayoutResId != 0) {
            view = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, frameLayout, false);
        }
        if (view != null) {
            z2 = true;
        }
        if (!z2 || !canTextInput(view)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (z2) {
            FrameLayout frameLayout2 = (FrameLayout) this.mWindow.findViewById(C0251R.C0253id.custom);
            frameLayout2.addView(view, new LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                frameLayout2.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView != null) {
                ((LinearLayout.LayoutParams) frameLayout.getLayoutParams()).weight = 0.0f;
            }
        } else {
            frameLayout.setVisibility(8);
        }
        ListView listView = this.mListView;
        if (listView != null) {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listView.setAdapter(listAdapter);
                int i = this.mCheckedItem;
                if (i > -1) {
                    listView.setItemChecked(i, true);
                    listView.setSelection(i);
                }
            }
        }
        obtainStyledAttributes.recycle();
    }

    private boolean setupTitle(ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new LayoutParams(-1, -2));
            this.mWindow.findViewById(C0251R.C0253id.title_template).setVisibility(8);
        } else {
            this.mIconView = (ImageView) this.mWindow.findViewById(16908294);
            if (!TextUtils.isEmpty(this.mTitle)) {
                this.mTitleView = (TextView) this.mWindow.findViewById(C0251R.C0253id.alertTitle);
                this.mTitleView.setText(this.mTitle);
                int i = this.mIconId;
                if (i != 0) {
                    this.mIconView.setImageResource(i);
                } else {
                    Drawable drawable = this.mIcon;
                    if (drawable != null) {
                        this.mIconView.setImageDrawable(drawable);
                    } else {
                        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                        this.mIconView.setVisibility(8);
                    }
                }
            } else {
                this.mWindow.findViewById(C0251R.C0253id.title_template).setVisibility(8);
                this.mIconView.setVisibility(8);
                viewGroup.setVisibility(8);
                return false;
            }
        }
        return true;
    }

    private void setupContent(ViewGroup viewGroup) {
        this.mScrollView = (ScrollView) this.mWindow.findViewById(C0251R.C0253id.scrollView);
        this.mScrollView.setFocusable(false);
        this.mMessageView = (TextView) this.mWindow.findViewById(16908299);
        TextView textView = this.mMessageView;
        if (textView != null) {
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
            } else {
                textView.setVisibility(8);
                this.mScrollView.removeView(this.mMessageView);
                if (this.mListView != null) {
                    ViewGroup viewGroup2 = (ViewGroup) this.mScrollView.getParent();
                    int indexOfChild = viewGroup2.indexOfChild(this.mScrollView);
                    viewGroup2.removeViewAt(indexOfChild);
                    viewGroup2.addView(this.mListView, indexOfChild, new LayoutParams(-1, -1));
                } else {
                    viewGroup.setVisibility(8);
                }
            }
        }
    }

    private boolean setupButtons() {
        boolean z;
        this.mButtonPositive = (Button) this.mWindow.findViewById(16908313);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
            z = false;
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            z = true;
        }
        this.mButtonNegative = (Button) this.mWindow.findViewById(16908314);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            z |= true;
        }
        this.mButtonNeutral = (Button) this.mWindow.findViewById(16908315);
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            z |= true;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (z) {
                centerButton(this.mButtonPositive);
            } else if (z) {
                centerButton(this.mButtonNegative);
            } else if (z) {
                centerButton(this.mButtonNeutral);
            }
        }
        if (z) {
            return true;
        }
        return false;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }
}
