package com.p004ti.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.ArrayList;

/* renamed from: com.ti.util.SparkLineView */
public class SparkLineView extends View {
    public boolean autoScale;
    public boolean autoScaleBounceBack;
    private ArrayList<Float> dataPoints;
    public float displayWidth;
    public float maxVal;
    public float minVal;
    private final int numberOfPoints;
    private final Paint pointFillPaint;
    private final Paint pointStrokePaint;
    private final Paint sparkLinePaint;

    public SparkLineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.numberOfPoints = 15;
        this.autoScale = false;
        this.autoScaleBounceBack = false;
        this.sparkLinePaint = new Paint() {
            {
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(5.0f);
                setAntiAlias(true);
                setARGB(255, 255, 0, 0);
            }
        };
        this.pointStrokePaint = new Paint() {
            {
                setARGB(255, 255, 255, 255);
                setStyle(Style.FILL_AND_STROKE);
                setAntiAlias(true);
            }
        };
        this.pointFillPaint = new Paint() {
            {
                setARGB(255, 255, 0, 0);
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        };
        init();
    }

    public SparkLineView(Context context) {
        super(context);
        this.numberOfPoints = 15;
        this.autoScale = false;
        this.autoScaleBounceBack = false;
        this.sparkLinePaint = new Paint() {
            {
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(5.0f);
                setAntiAlias(true);
                setARGB(255, 255, 0, 0);
            }
        };
        this.pointStrokePaint = new Paint() {
            {
                setARGB(255, 255, 255, 255);
                setStyle(Style.FILL_AND_STROKE);
                setAntiAlias(true);
            }
        };
        this.pointFillPaint = new Paint() {
            {
                setARGB(255, 255, 0, 0);
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        };
        init();
    }

    public void init() {
        this.dataPoints = new ArrayList<>();
        this.maxVal = 1.0f;
        this.minVal = 0.0f;
        for (int i = 0; i < 15; i++) {
            this.dataPoints.add(Float.valueOf(0.0f));
        }
        setWillNotDraw(false);
        setBackgroundColor(0);
        this.displayWidth = 200.0f;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        float width = (float) getWidth();
        float height = (float) getHeight();
        super.onDraw(canvas);
        Path path = new Path();
        int i = 15;
        if ((this.dataPoints.size() - 1) - 15 < 0) {
            i = this.dataPoints.size() - 1;
        }
        ArrayList<Float> arrayList = this.dataPoints;
        ArrayList arrayList2 = new ArrayList(arrayList.subList((arrayList.size() - 1) - i, this.dataPoints.size() - 1));
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (int i2 = 0; i2 < i; i2++) {
            Float f4 = (Float) arrayList2.get(i2);
            if (f4.floatValue() > f2) {
                f2 = f4.floatValue();
            }
            if (f4.floatValue() < f3) {
                f3 = f4.floatValue();
            }
            if (this.autoScale) {
                if (f4.floatValue() > this.maxVal) {
                    this.maxVal = f4.floatValue() + 0.01f;
                    if (((double) this.minVal) < -0.001d) {
                        this.minVal = (-f4.floatValue()) - 0.01f;
                    } else {
                        this.minVal = 0.0f;
                    }
                } else if (f4.floatValue() < this.minVal) {
                    this.minVal = f4.floatValue() - 0.01f;
                    this.maxVal = (-f4.floatValue()) + 0.01f;
                }
            }
        }
        if (this.autoScaleBounceBack) {
            float max = Math.max(f2, Math.abs(f3));
            this.maxVal = max;
            if (this.minVal < -0.1f) {
                this.minVal = -max;
            } else {
                this.minVal = 0.0f;
            }
        }
        int i3 = 0;
        while (i3 < i) {
            if (i3 == 0) {
                path.moveTo(f, height - ((height / (this.maxVal - this.minVal)) * (((Float) arrayList2.get(i3)).floatValue() - this.minVal)));
            } else {
                int i4 = i3 - 1;
                Float f5 = (Float) arrayList2.get(i4);
                Float f6 = (Float) arrayList2.get(i3);
                PointF pointF = new PointF();
                PointF pointF2 = new PointF();
                float f7 = (width - 30.0f) / ((float) i);
                pointF.x = (((float) i4) * f7) + 15.0f;
                float f8 = height - 15.0f;
                float f9 = height - 30.0f;
                float f10 = f9 / (this.maxVal - this.minVal);
                float floatValue = f5.floatValue();
                float f11 = this.minVal;
                pointF.y = f8 - (f10 * (floatValue - f11));
                pointF2.x = (f7 * ((float) i3)) + 15.0f;
                pointF2.y = f8 - ((f9 / (this.maxVal - f11)) * (f6.floatValue() - this.minVal));
                PointF midPointForPoints = midPointForPoints(pointF, pointF2);
                PointF controlPointForPoints = controlPointForPoints(midPointForPoints, pointF);
                path.quadTo(controlPointForPoints.x, controlPointForPoints.y, midPointForPoints.x, midPointForPoints.y);
                PointF controlPointForPoints2 = controlPointForPoints(midPointForPoints, pointF2);
                path.quadTo(controlPointForPoints2.x, controlPointForPoints2.y, pointF2.x, pointF2.y);
            }
            i3++;
            f = 0.0f;
        }
        canvas2.drawPath(path, this.sparkLinePaint);
        for (int i5 = 0; i5 < i; i5++) {
            Float f12 = (Float) arrayList2.get(i5);
            Float valueOf = Float.valueOf((((width - 30.0f) / ((float) i)) * ((float) i5)) + 15.0f);
            Float valueOf2 = Float.valueOf((height - 15.0f) - (((height - 30.0f) / (this.maxVal - this.minVal)) * (f12.floatValue() - this.minVal)));
            canvas2.drawCircle(valueOf.floatValue(), valueOf2.floatValue(), 10.0f, this.pointStrokePaint);
            canvas2.drawCircle(valueOf.floatValue(), valueOf2.floatValue(), 7.0f, this.pointFillPaint);
        }
    }

    /* access modifiers changed from: 0000 */
    public PointF controlPointForPoints(PointF pointF, PointF pointF2) {
        PointF midPointForPoints = midPointForPoints(pointF, pointF2);
        Float valueOf = Float.valueOf(Math.abs(pointF2.y - midPointForPoints.y));
        if (pointF.y < pointF2.y) {
            midPointForPoints.y += valueOf.floatValue();
        } else if (pointF.y > pointF2.y) {
            midPointForPoints.y -= valueOf.floatValue();
        }
        return midPointForPoints;
    }

    /* access modifiers changed from: 0000 */
    public PointF midPointForPoints(PointF pointF, PointF pointF2) {
        PointF pointF3 = new PointF();
        pointF3.x = (pointF.x + pointF2.x) / 2.0f;
        pointF3.y = (pointF.y + pointF2.y) / 2.0f;
        return pointF3;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
        super.onMeasure(i, i2);
    }

    public void addValue(float f) {
        this.dataPoints.add(Float.valueOf(f));
        invalidate();
    }

    public void setColor(int i, int i2, int i3, int i4) {
        this.pointFillPaint.setARGB(i, i2, i3, i4);
        this.sparkLinePaint.setARGB(i, i2, i3, i4);
    }
}
