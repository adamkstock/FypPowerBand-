package android.support.p003v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.p003v7.app.AlertController.AlertParams;
import android.support.p003v7.appcompat.C0251R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

/* renamed from: android.support.v7.app.AlertDialog */
public class AlertDialog extends AppCompatDialog implements DialogInterface {
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;
    /* access modifiers changed from: private */
    public AlertController mAlert;

    /* renamed from: android.support.v7.app.AlertDialog$Builder */
    public static class Builder {

        /* renamed from: P */
        private final AlertParams f16P;
        private int mTheme;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int i) {
            this.f16P = new AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, i)));
            this.mTheme = i;
        }

        public Context getContext() {
            return this.f16P.mContext;
        }

        public Builder setTitle(int i) {
            AlertParams alertParams = this.f16P;
            alertParams.mTitle = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.f16P.mTitle = charSequence;
            return this;
        }

        public Builder setCustomTitle(View view) {
            this.f16P.mCustomTitleView = view;
            return this;
        }

        public Builder setMessage(int i) {
            AlertParams alertParams = this.f16P;
            alertParams.mMessage = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.f16P.mMessage = charSequence;
            return this;
        }

        public Builder setIcon(int i) {
            this.f16P.mIconId = i;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.f16P.mIcon = drawable;
            return this;
        }

        public Builder setIconAttribute(int i) {
            TypedValue typedValue = new TypedValue();
            this.f16P.mContext.getTheme().resolveAttribute(i, typedValue, true);
            this.f16P.mIconId = typedValue.resourceId;
            return this;
        }

        public Builder setPositiveButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mPositiveButtonText = alertParams.mContext.getText(i);
            this.f16P.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mPositiveButtonText = charSequence;
            alertParams.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(i);
            this.f16P.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mNegativeButtonText = charSequence;
            alertParams.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mNeutralButtonText = alertParams.mContext.getText(i);
            this.f16P.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mNeutralButtonText = charSequence;
            alertParams.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.f16P.mCancelable = z;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.f16P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.f16P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.f16P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            this.f16P.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setAdapter(ListAdapter listAdapter, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setCursor(Cursor cursor, OnClickListener onClickListener, String str) {
            AlertParams alertParams = this.f16P;
            alertParams.mCursor = cursor;
            alertParams.mLabelColumn = str;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setMultiChoiceItems(int i, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            AlertParams alertParams2 = this.f16P;
            alertParams2.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams2.mCheckedItems = zArr;
            alertParams2.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams.mCheckedItems = zArr;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String str, String str2, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mCursor = cursor;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams.mIsCheckedColumn = str;
            alertParams.mLabelColumn = str2;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(int i, int i2, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            AlertParams alertParams2 = this.f16P;
            alertParams2.mOnClickListener = onClickListener;
            alertParams2.mCheckedItem = i2;
            alertParams2.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int i, String str, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mCursor = cursor;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mLabelColumn = str;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter listAdapter, int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.f16P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            this.f16P.mOnItemSelectedListener = onItemSelectedListener;
            return this;
        }

        public Builder setView(int i) {
            AlertParams alertParams = this.f16P;
            alertParams.mView = null;
            alertParams.mViewLayoutResId = i;
            alertParams.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view) {
            AlertParams alertParams = this.f16P;
            alertParams.mView = view;
            alertParams.mViewLayoutResId = 0;
            alertParams.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view, int i, int i2, int i3, int i4) {
            AlertParams alertParams = this.f16P;
            alertParams.mView = view;
            alertParams.mViewLayoutResId = 0;
            alertParams.mViewSpacingSpecified = true;
            alertParams.mViewSpacingLeft = i;
            alertParams.mViewSpacingTop = i2;
            alertParams.mViewSpacingRight = i3;
            alertParams.mViewSpacingBottom = i4;
            return this;
        }

        public Builder setInverseBackgroundForced(boolean z) {
            this.f16P.mForceInverseBackground = z;
            return this;
        }

        public Builder setRecycleOnMeasureEnabled(boolean z) {
            this.f16P.mRecycleOnMeasure = z;
            return this;
        }

        public AlertDialog create() {
            AlertDialog alertDialog = new AlertDialog(this.f16P.mContext, this.mTheme, false);
            this.f16P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.f16P.mCancelable);
            if (this.f16P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.f16P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.f16P.mOnDismissListener);
            if (this.f16P.mOnKeyListener != null) {
                alertDialog.setOnKeyListener(this.f16P.mOnKeyListener);
            }
            return alertDialog;
        }

        public AlertDialog show() {
            AlertDialog create = create();
            create.show();
            return create;
        }
    }

    protected AlertDialog(Context context) {
        this(context, resolveDialogTheme(context, 0), true);
    }

    protected AlertDialog(Context context, int i) {
        this(context, i, true);
    }

    AlertDialog(Context context, int i, boolean z) {
        super(context, resolveDialogTheme(context, i));
        this.mAlert = new AlertController(getContext(), this, getWindow());
    }

    protected AlertDialog(Context context, boolean z, OnCancelListener onCancelListener) {
        super(context, resolveDialogTheme(context, 0));
        setCancelable(z);
        setOnCancelListener(onCancelListener);
        this.mAlert = new AlertController(context, this, getWindow());
    }

    static int resolveDialogTheme(Context context, int i) {
        if (i >= 16777216) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(C0251R.attr.alertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    public Button getButton(int i) {
        return this.mAlert.getButton(i);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mAlert.setTitle(charSequence);
    }

    public void setCustomTitle(View view) {
        this.mAlert.setCustomTitle(view);
    }

    public void setMessage(CharSequence charSequence) {
        this.mAlert.setMessage(charSequence);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int i, int i2, int i3, int i4) {
        this.mAlert.setView(view, i, i2, i3, i4);
    }

    /* access modifiers changed from: 0000 */
    public void setButtonPanelLayoutHint(int i) {
        this.mAlert.setButtonPanelLayoutHint(i);
    }

    public void setButton(int i, CharSequence charSequence, Message message) {
        this.mAlert.setButton(i, charSequence, null, message);
    }

    public void setButton(int i, CharSequence charSequence, OnClickListener onClickListener) {
        this.mAlert.setButton(i, charSequence, onClickListener, null);
    }

    public void setIcon(int i) {
        this.mAlert.setIcon(i);
    }

    public void setIcon(Drawable drawable) {
        this.mAlert.setIcon(drawable);
    }

    public void setIconAttribute(int i) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(i, typedValue, true);
        this.mAlert.setIcon(typedValue.resourceId);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyDown(i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyUp(i, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }
}
