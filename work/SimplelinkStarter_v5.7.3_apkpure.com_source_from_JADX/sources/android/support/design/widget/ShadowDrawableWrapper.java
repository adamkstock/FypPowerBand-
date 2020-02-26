package android.support.design.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.design.C0010R;
import android.support.p003v7.graphics.drawable.DrawableWrapper;

class ShadowDrawableWrapper extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean mAddPaddingForCorners = true;
    final RectF mContentBounds;
    float mCornerRadius;
    final Paint mCornerShadowPaint;
    Path mCornerShadowPath;
    private boolean mDirty = true;
    final Paint mEdgeShadowPaint;
    float mMaxShadowSize;
    private boolean mPrintedShadowClipWarning = false;
    float mRawMaxShadowSize;
    float mRawShadowSize;
    private final int mShadowEndColor;
    private final int mShadowMiddleColor;
    float mShadowSize;
    private final int mShadowStartColor;

    public int getOpacity() {
        return -3;
    }

    public ShadowDrawableWrapper(Resources resources, Drawable drawable, float f, float f2, float f3) {
        super(drawable);
        this.mShadowStartColor = resources.getColor(C0010R.color.design_fab_shadow_start_color);
        this.mShadowMiddleColor = resources.getColor(C0010R.color.design_fab_shadow_mid_color);
        this.mShadowEndColor = resources.getColor(C0010R.color.design_fab_shadow_end_color);
        this.mCornerShadowPaint = new Paint(5);
        this.mCornerShadowPaint.setStyle(Style.FILL);
        this.mCornerRadius = (float) Math.round(f);
        this.mContentBounds = new RectF();
        this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
        this.mEdgeShadowPaint.setAntiAlias(false);
        setShadowSize(f2, f3);
    }

    private static int toEven(float f) {
        int round = Math.round(f);
        return round % 2 == 1 ? round - 1 : round;
    }

    public void setAddPaddingForCorners(boolean z) {
        this.mAddPaddingForCorners = z;
        invalidateSelf();
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mCornerShadowPaint.setAlpha(i);
        this.mEdgeShadowPaint.setAlpha(i);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mDirty = true;
    }

    /* access modifiers changed from: 0000 */
    public void setShadowSize(float f, float f2) {
        if (f < 0.0f || f2 < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        float even = (float) toEven(f);
        float even2 = (float) toEven(f2);
        if (even > even2) {
            if (!this.mPrintedShadowClipWarning) {
                this.mPrintedShadowClipWarning = true;
            }
            even = even2;
        }
        if (this.mRawShadowSize != even || this.mRawMaxShadowSize != even2) {
            this.mRawShadowSize = even;
            this.mRawMaxShadowSize = even2;
            this.mShadowSize = (float) Math.round(even * SHADOW_MULTIPLIER);
            this.mMaxShadowSize = even2;
            this.mDirty = true;
            invalidateSelf();
        }
    }

    public boolean getPadding(Rect rect) {
        int ceil = (int) Math.ceil((double) calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        int ceil2 = (int) Math.ceil((double) calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(ceil2, ceil, ceil2, ceil);
        return true;
    }

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) (f * SHADOW_MULTIPLIER)) + ((1.0d - COS_45) * ((double) f2))) : f * SHADOW_MULTIPLIER;
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) f) + ((1.0d - COS_45) * ((double) f2))) : f;
    }

    public void setCornerRadius(float f) {
        float round = (float) Math.round(f);
        if (this.mCornerRadius != round) {
            this.mCornerRadius = round;
            this.mDirty = true;
            invalidateSelf();
        }
    }

    public void draw(Canvas canvas) {
        if (this.mDirty) {
            buildComponents(getBounds());
            this.mDirty = false;
        }
        drawShadow(canvas);
        super.draw(canvas);
    }

    private void drawShadow(Canvas canvas) {
        float f;
        float f2;
        int i;
        Canvas canvas2 = canvas;
        float f3 = this.mCornerRadius;
        float f4 = (-f3) - this.mShadowSize;
        float f5 = f3 * 2.0f;
        boolean z = this.mContentBounds.width() - f5 > 0.0f;
        boolean z2 = this.mContentBounds.height() - f5 > 0.0f;
        float f6 = this.mRawShadowSize;
        float f7 = f3 / ((f6 - (SHADOW_HORIZ_SCALE * f6)) + f3);
        float f8 = f3 / ((f6 - (SHADOW_TOP_SCALE * f6)) + f3);
        float f9 = f3 / ((f6 - (f6 * SHADOW_BOTTOM_SCALE)) + f3);
        int save = canvas.save();
        canvas2.translate(this.mContentBounds.left + f3, this.mContentBounds.top + f3);
        canvas2.scale(f7, f8);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z) {
            canvas2.scale(SHADOW_BOTTOM_SCALE / f7, SHADOW_BOTTOM_SCALE);
            i = save;
            f = f8;
            f2 = f9;
            canvas.drawRect(0.0f, f4, this.mContentBounds.width() - f5, -this.mCornerRadius, this.mEdgeShadowPaint);
        } else {
            i = save;
            f = f8;
            f2 = f9;
        }
        canvas2.restoreToCount(i);
        int save2 = canvas.save();
        canvas2.translate(this.mContentBounds.right - f3, this.mContentBounds.bottom - f3);
        canvas2.scale(f7, f2);
        canvas2.rotate(180.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z) {
            canvas2.scale(SHADOW_BOTTOM_SCALE / f7, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f4, this.mContentBounds.width() - f5, (-this.mCornerRadius) + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(save2);
        int save3 = canvas.save();
        canvas2.translate(this.mContentBounds.left + f3, this.mContentBounds.bottom - f3);
        canvas2.scale(f7, f2);
        canvas2.rotate(270.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas2.scale(SHADOW_BOTTOM_SCALE / f2, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f4, this.mContentBounds.height() - f5, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(save3);
        int save4 = canvas.save();
        canvas2.translate(this.mContentBounds.right - f3, this.mContentBounds.top + f3);
        float f10 = f;
        canvas2.scale(f7, f10);
        canvas2.rotate(90.0f);
        canvas2.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas2.scale(SHADOW_BOTTOM_SCALE / f10, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f4, this.mContentBounds.height() - f5, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas2.restoreToCount(save4);
    }

    private void buildShadowCorners() {
        float f = this.mCornerRadius;
        RectF rectF = new RectF(-f, -f, f, f);
        RectF rectF2 = new RectF(rectF);
        float f2 = this.mShadowSize;
        rectF2.inset(-f2, -f2);
        Path path = this.mCornerShadowPath;
        if (path == null) {
            this.mCornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.mCornerShadowPath.setFillType(FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        float f3 = -rectF2.top;
        if (f3 > 0.0f) {
            float f4 = this.mCornerRadius / f3;
            float f5 = ((SHADOW_BOTTOM_SCALE - f4) / 2.0f) + f4;
            Paint paint = this.mCornerShadowPaint;
            RadialGradient radialGradient = r8;
            RadialGradient radialGradient2 = new RadialGradient(0.0f, 0.0f, f3, new int[]{0, this.mShadowStartColor, this.mShadowMiddleColor, this.mShadowEndColor}, new float[]{0.0f, f4, f5, 1.0f}, TileMode.CLAMP);
            paint.setShader(radialGradient);
        }
        Paint paint2 = this.mEdgeShadowPaint;
        LinearGradient linearGradient = new LinearGradient(0.0f, rectF.top, 0.0f, rectF2.top, new int[]{this.mShadowStartColor, this.mShadowMiddleColor, this.mShadowEndColor}, new float[]{0.0f, SHADOW_HORIZ_SCALE, SHADOW_BOTTOM_SCALE}, TileMode.CLAMP);
        paint2.setShader(linearGradient);
        this.mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect rect) {
        float f = this.mRawMaxShadowSize * SHADOW_MULTIPLIER;
        this.mContentBounds.set(((float) rect.left) + this.mRawMaxShadowSize, ((float) rect.top) + f, ((float) rect.right) - this.mRawMaxShadowSize, ((float) rect.bottom) - f);
        getWrappedDrawable().setBounds((int) this.mContentBounds.left, (int) this.mContentBounds.top, (int) this.mContentBounds.right, (int) this.mContentBounds.bottom);
        buildShadowCorners();
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public void setShadowSize(float f) {
        setShadowSize(f, this.mRawMaxShadowSize);
    }

    public void setMaxShadowSize(float f) {
        setShadowSize(this.mRawShadowSize, f);
    }

    public float getShadowSize() {
        return this.mRawShadowSize;
    }

    public float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }

    public float getMinWidth() {
        float f = this.mRawMaxShadowSize;
        return (Math.max(f, this.mCornerRadius + (f / 2.0f)) * 2.0f) + (this.mRawMaxShadowSize * 2.0f);
    }

    public float getMinHeight() {
        float f = this.mRawMaxShadowSize;
        return (Math.max(f, this.mCornerRadius + ((f * SHADOW_MULTIPLIER) / 2.0f)) * 2.0f) + (this.mRawMaxShadowSize * SHADOW_MULTIPLIER * 2.0f);
    }
}
