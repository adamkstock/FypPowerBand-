package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.p000v4.graphics.ColorUtils;

class CircularBorderDrawable extends Drawable {
    private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333f;
    float mBorderWidth;
    private int mBottomInnerStrokeColor;
    private int mBottomOuterStrokeColor;
    private boolean mInvalidateShader = true;
    final Paint mPaint = new Paint(1);
    final Rect mRect = new Rect();
    final RectF mRectF = new RectF();
    private int mTintColor;
    private int mTopInnerStrokeColor;
    private int mTopOuterStrokeColor;

    public CircularBorderDrawable() {
        this.mPaint.setStyle(Style.STROKE);
    }

    /* access modifiers changed from: 0000 */
    public void setGradientColors(int i, int i2, int i3, int i4) {
        this.mTopOuterStrokeColor = i;
        this.mTopInnerStrokeColor = i2;
        this.mBottomOuterStrokeColor = i3;
        this.mBottomInnerStrokeColor = i4;
    }

    /* access modifiers changed from: 0000 */
    public void setBorderWidth(float f) {
        if (this.mBorderWidth != f) {
            this.mBorderWidth = f;
            this.mPaint.setStrokeWidth(f * DRAW_STROKE_WIDTH_MULTIPLE);
            this.mInvalidateShader = true;
            invalidateSelf();
        }
    }

    public void draw(Canvas canvas) {
        if (this.mInvalidateShader) {
            this.mPaint.setShader(createGradientShader());
            this.mInvalidateShader = false;
        }
        float strokeWidth = this.mPaint.getStrokeWidth() / 2.0f;
        RectF rectF = this.mRectF;
        copyBounds(this.mRect);
        rectF.set(this.mRect);
        rectF.left += strokeWidth;
        rectF.top += strokeWidth;
        rectF.right -= strokeWidth;
        rectF.bottom -= strokeWidth;
        canvas.drawOval(rectF, this.mPaint);
    }

    public boolean getPadding(Rect rect) {
        int round = Math.round(this.mBorderWidth);
        rect.set(round, round, round, round);
        return true;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        invalidateSelf();
    }

    /* access modifiers changed from: 0000 */
    public void setTintColor(int i) {
        this.mTintColor = i;
        this.mInvalidateShader = true;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getOpacity() {
        return this.mBorderWidth > 0.0f ? -3 : -2;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mInvalidateShader = true;
    }

    private Shader createGradientShader() {
        Rect rect = this.mRect;
        copyBounds(rect);
        float height = this.mBorderWidth / ((float) rect.height());
        LinearGradient linearGradient = new LinearGradient(0.0f, (float) rect.top, 0.0f, (float) rect.bottom, new int[]{ColorUtils.compositeColors(this.mTopOuterStrokeColor, this.mTintColor), ColorUtils.compositeColors(this.mTopInnerStrokeColor, this.mTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mTopInnerStrokeColor, 0), this.mTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mBottomInnerStrokeColor, 0), this.mTintColor), ColorUtils.compositeColors(this.mBottomInnerStrokeColor, this.mTintColor), ColorUtils.compositeColors(this.mBottomOuterStrokeColor, this.mTintColor)}, new float[]{0.0f, height, 0.5f, 0.5f, 1.0f - height, 1.0f}, TileMode.CLAMP);
        return linearGradient;
    }
}
