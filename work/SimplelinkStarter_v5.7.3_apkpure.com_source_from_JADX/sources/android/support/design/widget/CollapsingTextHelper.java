package android.support.design.widget;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.design.C0010R;
import android.support.p000v4.text.TextDirectionHeuristicsCompat;
import android.support.p000v4.view.GravityCompat;
import android.support.p000v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT = null;
    private static final boolean USE_SCALING_TEXTURE = (VERSION.SDK_INT < 18);
    private boolean mBoundsChanged;
    private final Rect mCollapsedBounds;
    private float mCollapsedDrawX;
    private float mCollapsedDrawY;
    private int mCollapsedTextColor;
    private int mCollapsedTextGravity = 16;
    private float mCollapsedTextSize = 15.0f;
    private final RectF mCurrentBounds;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private float mCurrentTextSize;
    private boolean mDrawTitle;
    private final Rect mExpandedBounds;
    private float mExpandedDrawX;
    private float mExpandedDrawY;
    private float mExpandedFraction;
    private int mExpandedTextColor;
    private int mExpandedTextGravity = 16;
    private float mExpandedTextSize = 15.0f;
    private Bitmap mExpandedTitleTexture;
    private boolean mIsRtl;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private float mTextureAscent;
    private float mTextureDescent;
    private Paint mTexturePaint;
    private boolean mUseTexture;
    private final View mView;

    static {
        Paint paint = DEBUG_DRAW_PAINT;
        if (paint != null) {
            paint.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }

    public CollapsingTextHelper(View view) {
        this.mView = view;
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
    }

    /* access modifiers changed from: 0000 */
    public void setTextSizeInterpolator(Interpolator interpolator) {
        this.mTextSizeInterpolator = interpolator;
        recalculate();
    }

    /* access modifiers changed from: 0000 */
    public void setPositionInterpolator(Interpolator interpolator) {
        this.mPositionInterpolator = interpolator;
        recalculate();
    }

    /* access modifiers changed from: 0000 */
    public void setExpandedTextSize(float f) {
        if (this.mExpandedTextSize != f) {
            this.mExpandedTextSize = f;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setCollapsedTextSize(float f) {
        if (this.mCollapsedTextSize != f) {
            this.mCollapsedTextSize = f;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setCollapsedTextColor(int i) {
        if (this.mCollapsedTextColor != i) {
            this.mCollapsedTextColor = i;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setExpandedTextColor(int i) {
        if (this.mExpandedTextColor != i) {
            this.mExpandedTextColor = i;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setExpandedBounds(int i, int i2, int i3, int i4) {
        if (!rectEquals(this.mExpandedBounds, i, i2, i3, i4)) {
            this.mExpandedBounds.set(i, i2, i3, i4);
            this.mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setCollapsedBounds(int i, int i2, int i3, int i4) {
        if (!rectEquals(this.mCollapsedBounds, i, i2, i3, i4)) {
            this.mCollapsedBounds.set(i, i2, i3, i4);
            this.mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    /* access modifiers changed from: 0000 */
    public void onBoundsChanged() {
        this.mDrawTitle = this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0;
    }

    /* access modifiers changed from: 0000 */
    public void setExpandedTextGravity(int i) {
        if (this.mExpandedTextGravity != i) {
            this.mExpandedTextGravity = i;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public int getExpandedTextGravity() {
        return this.mExpandedTextGravity;
    }

    /* access modifiers changed from: 0000 */
    public void setCollapsedTextGravity(int i) {
        if (this.mCollapsedTextGravity != i) {
            this.mCollapsedTextGravity = i;
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public int getCollapsedTextGravity() {
        return this.mCollapsedTextGravity;
    }

    /* access modifiers changed from: 0000 */
    public void setCollapsedTextAppearance(int i) {
        TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(i, C0010R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = obtainStyledAttributes.getColor(C0010R.styleable.TextAppearance_android_textColor, this.mCollapsedTextColor);
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TextAppearance_android_textSize, (int) this.mCollapsedTextSize);
        }
        obtainStyledAttributes.recycle();
        recalculate();
    }

    /* access modifiers changed from: 0000 */
    public void setExpandedTextAppearance(int i) {
        TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(i, C0010R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = obtainStyledAttributes.getColor(C0010R.styleable.TextAppearance_android_textColor, this.mExpandedTextColor);
        }
        if (obtainStyledAttributes.hasValue(C0010R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(C0010R.styleable.TextAppearance_android_textSize, (int) this.mExpandedTextSize);
        }
        obtainStyledAttributes.recycle();
        recalculate();
    }

    /* access modifiers changed from: 0000 */
    public void setTypeface(Typeface typeface) {
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        if (this.mTextPaint.getTypeface() != typeface) {
            this.mTextPaint.setTypeface(typeface);
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    /* access modifiers changed from: 0000 */
    public void setExpansionFraction(float f) {
        float constrain = MathUtils.constrain(f, 0.0f, 1.0f);
        if (constrain != this.mExpandedFraction) {
            this.mExpandedFraction = constrain;
            calculateCurrentOffsets();
        }
    }

    /* access modifiers changed from: 0000 */
    public float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    /* access modifiers changed from: 0000 */
    public float getCollapsedTextSize() {
        return this.mCollapsedTextSize;
    }

    /* access modifiers changed from: 0000 */
    public float getExpandedTextSize() {
        return this.mExpandedTextSize;
    }

    private void calculateCurrentOffsets() {
        float f = this.mExpandedFraction;
        interpolateBounds(f);
        this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, f, this.mPositionInterpolator);
        this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, f, this.mTextSizeInterpolator));
        int i = this.mCollapsedTextColor;
        int i2 = this.mExpandedTextColor;
        if (i != i2) {
            this.mTextPaint.setColor(blendColors(i2, i, f));
        } else {
            this.mTextPaint.setColor(i);
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    private void calculateBaseOffsets() {
        this.mTextPaint.setTextSize(this.mCollapsedTextSize);
        CharSequence charSequence = this.mTextToDraw;
        float f = 0.0f;
        float measureText = charSequence != null ? this.mTextPaint.measureText(charSequence, 0, charSequence.length()) : 0.0f;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(this.mCollapsedTextGravity, this.mIsRtl ? 1 : 0);
        int i = absoluteGravity & 112;
        if (i == 48) {
            this.mCollapsedDrawY = ((float) this.mCollapsedBounds.top) - this.mTextPaint.ascent();
        } else if (i != 80) {
            this.mCollapsedDrawY = ((float) this.mCollapsedBounds.centerY()) + (((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent());
        } else {
            this.mCollapsedDrawY = (float) this.mCollapsedBounds.bottom;
        }
        int i2 = absoluteGravity & 7;
        if (i2 == 1) {
            this.mCollapsedDrawX = ((float) this.mCollapsedBounds.centerX()) - (measureText / 2.0f);
        } else if (i2 != 5) {
            this.mCollapsedDrawX = (float) this.mCollapsedBounds.left;
        } else {
            this.mCollapsedDrawX = ((float) this.mCollapsedBounds.right) - measureText;
        }
        this.mTextPaint.setTextSize(this.mExpandedTextSize);
        CharSequence charSequence2 = this.mTextToDraw;
        if (charSequence2 != null) {
            f = this.mTextPaint.measureText(charSequence2, 0, charSequence2.length());
        }
        int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.mExpandedTextGravity, this.mIsRtl ? 1 : 0);
        int i3 = absoluteGravity2 & 112;
        if (i3 == 48) {
            this.mExpandedDrawY = ((float) this.mExpandedBounds.top) - this.mTextPaint.ascent();
        } else if (i3 != 80) {
            this.mExpandedDrawY = ((float) this.mExpandedBounds.centerY()) + (((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent());
        } else {
            this.mExpandedDrawY = (float) this.mExpandedBounds.bottom;
        }
        int i4 = absoluteGravity2 & 7;
        if (i4 == 1) {
            this.mExpandedDrawX = ((float) this.mExpandedBounds.centerX()) - (f / 2.0f);
        } else if (i4 != 5) {
            this.mExpandedDrawX = (float) this.mExpandedBounds.left;
        } else {
            this.mExpandedDrawX = ((float) this.mExpandedBounds.right) - f;
        }
        clearTexture();
    }

    private void interpolateBounds(float f) {
        this.mCurrentBounds.left = lerp((float) this.mExpandedBounds.left, (float) this.mCollapsedBounds.left, f, this.mPositionInterpolator);
        this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        this.mCurrentBounds.right = lerp((float) this.mExpandedBounds.right, (float) this.mCollapsedBounds.right, f, this.mPositionInterpolator);
        this.mCurrentBounds.bottom = lerp((float) this.mExpandedBounds.bottom, (float) this.mCollapsedBounds.bottom, f, this.mPositionInterpolator);
    }

    public void draw(Canvas canvas) {
        float f;
        int save = canvas.save();
        if (this.mTextToDraw != null && this.mDrawTitle) {
            float f2 = this.mCurrentDrawX;
            float f3 = this.mCurrentDrawY;
            boolean z = this.mUseTexture && this.mExpandedTitleTexture != null;
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            if (z) {
                f = this.mTextureAscent * this.mScale;
                float f4 = this.mTextureDescent;
            } else {
                f = this.mTextPaint.ascent() * this.mScale;
                this.mTextPaint.descent();
                float f5 = this.mScale;
            }
            if (z) {
                f3 += f;
            }
            float f6 = f3;
            float f7 = this.mScale;
            if (f7 != 1.0f) {
                canvas.scale(f7, f7, f2, f6);
            }
            if (z) {
                canvas.drawBitmap(this.mExpandedTitleTexture, f2, f6, this.mTexturePaint);
            } else {
                CharSequence charSequence = this.mTextToDraw;
                canvas.drawText(charSequence, 0, charSequence.length(), f2, f6, this.mTextPaint);
            }
        }
        canvas.restoreToCount(save);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this.mView) != 1) {
            z = false;
        }
        return (z ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(charSequence, 0, charSequence.length());
    }

    private void setInterpolatedTextSize(float f) {
        float f2;
        float f3;
        boolean z;
        if (this.mText != null) {
            if (isClose(f, this.mCollapsedTextSize)) {
                f2 = (float) this.mCollapsedBounds.width();
                f3 = this.mCollapsedTextSize;
                this.mScale = 1.0f;
            } else {
                float width = (float) this.mExpandedBounds.width();
                float f4 = this.mExpandedTextSize;
                if (isClose(f, f4)) {
                    this.mScale = 1.0f;
                } else {
                    this.mScale = f / this.mExpandedTextSize;
                }
                f2 = width;
                f3 = f4;
            }
            boolean z2 = true;
            if (f2 > 0.0f) {
                z = this.mCurrentTextSize != f3 || this.mBoundsChanged;
                this.mCurrentTextSize = f3;
                this.mBoundsChanged = false;
            } else {
                z = false;
            }
            if (this.mTextToDraw == null || z) {
                this.mTextPaint.setTextSize(this.mCurrentTextSize);
                CharSequence ellipsize = TextUtils.ellipsize(this.mText, this.mTextPaint, f2, TruncateAt.END);
                CharSequence charSequence = this.mTextToDraw;
                if (charSequence == null || !charSequence.equals(ellipsize)) {
                    this.mTextToDraw = ellipsize;
                }
                this.mIsRtl = calculateIsRtl(this.mTextToDraw);
            }
            if (!USE_SCALING_TEXTURE || this.mScale == 1.0f) {
                z2 = false;
            }
            this.mUseTexture = z2;
            if (this.mUseTexture) {
                ensureExpandedTexture();
            }
            ViewCompat.postInvalidateOnAnimation(this.mView);
        }
    }

    private void ensureExpandedTexture() {
        if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty() && !TextUtils.isEmpty(this.mTextToDraw)) {
            this.mTextPaint.setTextSize(this.mExpandedTextSize);
            this.mTextPaint.setColor(this.mExpandedTextColor);
            this.mTextureAscent = this.mTextPaint.ascent();
            this.mTextureDescent = this.mTextPaint.descent();
            TextPaint textPaint = this.mTextPaint;
            CharSequence charSequence = this.mTextToDraw;
            int round = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int round2 = Math.round(this.mTextureDescent - this.mTextureAscent);
            if (round > 0 || round2 > 0) {
                this.mExpandedTitleTexture = Bitmap.createBitmap(round, round2, Config.ARGB_8888);
                Canvas canvas = new Canvas(this.mExpandedTitleTexture);
                CharSequence charSequence2 = this.mTextToDraw;
                canvas.drawText(charSequence2, 0, charSequence2.length(), 0.0f, ((float) round2) - this.mTextPaint.descent(), this.mTextPaint);
                if (this.mTexturePaint == null) {
                    this.mTexturePaint = new Paint(3);
                }
            }
        }
    }

    public void recalculate() {
        if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setText(CharSequence charSequence) {
        if (charSequence == null || !charSequence.equals(this.mText)) {
            this.mText = charSequence;
            this.mTextToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    /* access modifiers changed from: 0000 */
    public CharSequence getText() {
        return this.mText;
    }

    private void clearTexture() {
        Bitmap bitmap = this.mExpandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.mExpandedTitleTexture = null;
        }
    }

    private static boolean isClose(float f, float f2) {
        return Math.abs(f - f2) < 0.001f;
    }

    /* access modifiers changed from: 0000 */
    public int getExpandedTextColor() {
        return this.mExpandedTextColor;
    }

    /* access modifiers changed from: 0000 */
    public int getCollapsedTextColor() {
        return this.mCollapsedTextColor;
    }

    private static int blendColors(int i, int i2, float f) {
        float f2 = 1.0f - f;
        return Color.argb((int) ((((float) Color.alpha(i)) * f2) + (((float) Color.alpha(i2)) * f)), (int) ((((float) Color.red(i)) * f2) + (((float) Color.red(i2)) * f)), (int) ((((float) Color.green(i)) * f2) + (((float) Color.green(i2)) * f)), (int) ((((float) Color.blue(i)) * f2) + (((float) Color.blue(i2)) * f)));
    }

    private static float lerp(float f, float f2, float f3, Interpolator interpolator) {
        if (interpolator != null) {
            f3 = interpolator.getInterpolation(f3);
        }
        return AnimationUtils.lerp(f, f2, f3);
    }

    private static boolean rectEquals(Rect rect, int i, int i2, int i3, int i4) {
        return rect.left == i && rect.top == i2 && rect.right == i3 && rect.bottom == i4;
    }
}
