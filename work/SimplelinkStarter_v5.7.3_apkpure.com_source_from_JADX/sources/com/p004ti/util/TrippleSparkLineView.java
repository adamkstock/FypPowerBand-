package com.p004ti.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.ArrayList;

/* renamed from: com.ti.util.TrippleSparkLineView */
public class TrippleSparkLineView extends View {
    public boolean autoScale = false;
    public boolean autoScaleBounceBack = false;
    private ArrayList<ArrayList<Float>> dataPoints;
    public float displayWidth;
    public ArrayList<Float> mnVal;
    public ArrayList<Float> mxVal;
    private final int numberOfPoints = 15;
    private ArrayList<Paint> pointFillPaint;
    private ArrayList<Paint> pointStrokePaint;
    private ArrayList<Paint> sparkLinePaint;

    public TrippleSparkLineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public TrippleSparkLineView(Context context) {
        super(context);
        init();
    }

    public void init() {
        this.sparkLinePaint = new ArrayList<>();
        this.pointFillPaint = new ArrayList<>();
        this.pointStrokePaint = new ArrayList<>();
        this.sparkLinePaint.add(new Paint() {
            {
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(5.0f);
                setAntiAlias(true);
                setARGB(255, 255, 0, 0);
            }
        });
        this.sparkLinePaint.add(new Paint() {
            {
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(5.0f);
                setAntiAlias(true);
                setARGB(255, 0, 150, 125);
            }
        });
        this.sparkLinePaint.add(new Paint() {
            {
                setStyle(Style.STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(5.0f);
                setAntiAlias(true);
                setARGB(255, 0, 0, 0);
            }
        });
        C09654 r0 = new Paint() {
            {
                setARGB(255, 255, 255, 255);
                setStyle(Style.FILL_AND_STROKE);
                setAntiAlias(true);
            }
        };
        this.pointStrokePaint.add(r0);
        this.pointStrokePaint.add(r0);
        this.pointStrokePaint.add(r0);
        this.pointFillPaint.add(new Paint() {
            {
                setARGB(255, 255, 0, 0);
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        });
        this.pointFillPaint.add(new Paint() {
            {
                setARGB(255, 0, 150, 125);
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        });
        this.pointFillPaint.add(new Paint() {
            {
                setARGB(255, 0, 0, 0);
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        });
        this.dataPoints = new ArrayList<>();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 15; i++) {
            arrayList.add(Float.valueOf(0.0f));
        }
        this.dataPoints.add(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < 15; i2++) {
            arrayList2.add(Float.valueOf(0.0f));
        }
        this.dataPoints.add(arrayList2);
        ArrayList arrayList3 = new ArrayList();
        for (int i3 = 0; i3 < 15; i3++) {
            arrayList3.add(Float.valueOf(0.0f));
        }
        this.dataPoints.add(arrayList3);
        this.mxVal = new ArrayList<>();
        Float valueOf = Float.valueOf(1.0f);
        Float valueOf2 = Float.valueOf(1.0f);
        Float valueOf3 = Float.valueOf(1.0f);
        this.mxVal.add(valueOf);
        this.mxVal.add(valueOf2);
        this.mxVal.add(valueOf3);
        this.mnVal = new ArrayList<>();
        Float valueOf4 = Float.valueOf(0.0f);
        Float valueOf5 = Float.valueOf(0.0f);
        Float valueOf6 = Float.valueOf(0.0f);
        this.mnVal.add(valueOf4);
        this.mnVal.add(valueOf5);
        this.mnVal.add(valueOf6);
        setWillNotDraw(false);
        setBackgroundColor(0);
        this.displayWidth = 200.0f;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        float f;
        Paint paint;
        Paint paint2;
        float f2;
        float f3;
        Canvas canvas2 = canvas;
        float width = (float) getWidth();
        float height = (float) getHeight();
        super.onDraw(canvas);
        Float valueOf = Float.valueOf(0.0f);
        int i2 = 0;
        int i3 = 15;
        float f4 = 0.0f;
        float f5 = 0.0f;
        while (i2 < this.dataPoints.size()) {
            Path path = new Path();
            ArrayList arrayList = (ArrayList) this.dataPoints.get(i2);
            Paint paint3 = (Paint) this.sparkLinePaint.get(i2);
            Paint paint4 = (Paint) this.pointFillPaint.get(i2);
            Paint paint5 = (Paint) this.pointStrokePaint.get(i2);
            Float f6 = (Float) this.mxVal.get(i2);
            Float f7 = (Float) this.mnVal.get(i2);
            if ((arrayList.size() - 1) - 15 < 0) {
                i3 = arrayList.size() - 1;
            }
            Float f8 = f7;
            Float f9 = f6;
            Float f10 = valueOf;
            ArrayList arrayList2 = new ArrayList(arrayList.subList((arrayList.size() - 1) - i3, arrayList.size() - 1));
            Float f11 = f8;
            int i4 = 0;
            while (i4 < i3) {
                Float f12 = (Float) arrayList2.get(i4);
                if (f12.floatValue() > f4) {
                    f4 = f12.floatValue();
                }
                if (f12.floatValue() < f5) {
                    f5 = f12.floatValue();
                }
                float f13 = f4;
                if (!this.autoScale) {
                    f3 = f5;
                } else if (f12.floatValue() > f9.floatValue()) {
                    f9 = Float.valueOf(f12.floatValue() + 0.01f);
                    f3 = f5;
                    f11 = ((double) f11.floatValue()) < -0.001d ? Float.valueOf((-f12.floatValue()) - 0.01f) : f10;
                } else {
                    f3 = f5;
                    if (f12.floatValue() < f11.floatValue()) {
                        f11 = Float.valueOf(f12.floatValue() - 0.01f);
                        f9 = Float.valueOf((-f12.floatValue()) + 0.01f);
                    }
                }
                i4++;
                f4 = f13;
                f5 = f3;
            }
            if (this.autoScaleBounceBack) {
                float max = Math.max(f4, Math.abs(f5));
                f9 = Float.valueOf(max);
                if (f11.floatValue() < -0.1f) {
                    f11 = Float.valueOf(-max);
                    f4 = max;
                } else {
                    f4 = max;
                    f11 = f10;
                }
            }
            int i5 = 0;
            while (i5 < i3) {
                if (i5 == 0) {
                    f2 = f4;
                    path.moveTo(0.0f, height - ((height / (f9.floatValue() - f11.floatValue())) * (((Float) arrayList2.get(i5)).floatValue() - f11.floatValue())));
                    paint = paint5;
                    i = i2;
                    f = f5;
                    paint2 = paint4;
                } else {
                    f2 = f4;
                    int i6 = i5 - 1;
                    Float f14 = (Float) arrayList2.get(i6);
                    Float f15 = (Float) arrayList2.get(i5);
                    PointF pointF = new PointF();
                    f = f5;
                    PointF pointF2 = new PointF();
                    i = i2;
                    float f16 = (width - 30.0f) / ((float) i3);
                    pointF.x = (((float) i6) * f16) + 15.0f;
                    float f17 = height - 15.0f;
                    float f18 = height - 30.0f;
                    float floatValue = (f18 / (f9.floatValue() - f11.floatValue())) * (f14.floatValue() - f11.floatValue());
                    paint2 = paint4;
                    pointF.y = f17 - floatValue;
                    pointF2.x = (f16 * ((float) i5)) + 15.0f;
                    pointF2.y = f17 - ((f18 / (f9.floatValue() - f11.floatValue())) * (f15.floatValue() - f11.floatValue()));
                    PointF midPointForPoints = midPointForPoints(pointF, pointF2);
                    PointF controlPointForPoints = controlPointForPoints(midPointForPoints, pointF);
                    paint = paint5;
                    path.quadTo(controlPointForPoints.x, controlPointForPoints.y, midPointForPoints.x, midPointForPoints.y);
                    PointF controlPointForPoints2 = controlPointForPoints(midPointForPoints, pointF2);
                    path.quadTo(controlPointForPoints2.x, controlPointForPoints2.y, pointF2.x, pointF2.y);
                }
                i5++;
                f4 = f2;
                paint4 = paint2;
                paint5 = paint;
                f5 = f;
                i2 = i;
            }
            Paint paint6 = paint5;
            int i7 = i2;
            float f19 = f4;
            float f20 = f5;
            Paint paint7 = paint4;
            canvas2.drawPath(path, paint3);
            for (int i8 = 0; i8 < i3; i8++) {
                Float f21 = (Float) arrayList2.get(i8);
                Float valueOf2 = Float.valueOf((((width - 30.0f) / ((float) i3)) * ((float) i8)) + 15.0f);
                Float valueOf3 = Float.valueOf((height - 15.0f) - (((height - 30.0f) / (f9.floatValue() - f11.floatValue())) * (f21.floatValue() - f11.floatValue())));
                canvas2.drawCircle(valueOf2.floatValue(), valueOf3.floatValue(), 10.0f, paint6);
                canvas2.drawCircle(valueOf2.floatValue(), valueOf3.floatValue(), 7.0f, paint7);
            }
            i2 = i7 + 1;
            f4 = f19;
            valueOf = f10;
            f5 = f20;
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
        ((ArrayList) this.dataPoints.get(0)).add(Float.valueOf(f));
        invalidate();
    }

    public void addValue(float f, int i) {
        Float valueOf = Float.valueOf(f);
        if (i > this.dataPoints.size() - 1) {
            Log.d("TrippleSparkLineView", "Try to set data for line that does not exist, ignoring !");
            return;
        }
        ((ArrayList) this.dataPoints.get(i)).add(valueOf);
        invalidate();
    }

    public void setColor(int i, int i2, int i3, int i4) {
        Paint paint = (Paint) this.sparkLinePaint.get(0);
        ((Paint) this.pointFillPaint.get(0)).setARGB(i, i2, i3, i4);
        paint.setARGB(i, i2, i3, i4);
    }

    public void setColor(int i, int i2, int i3, int i4, int i5) {
        if (i5 > this.pointFillPaint.size() - 1) {
            Log.d("TrippleSparkLineView", "Try to set color for line that does not exist, ignoring !");
            return;
        }
        Paint paint = (Paint) this.sparkLinePaint.get(i5);
        ((Paint) this.pointFillPaint.get(i5)).setARGB(i, i2, i3, i4);
        paint.setARGB(i, i2, i3, i4);
        if (i == 0) {
            ((Paint) this.pointStrokePaint.get(i5)).setARGB(i, i2, i3, i4);
        }
    }

    public void maxVal(Float f) {
        for (int i = 0; i < this.mxVal.size(); i++) {
            Float f2 = (Float) this.mxVal.get(i);
            this.mxVal.set(i, f);
        }
    }

    public void minVal(Float f) {
        for (int i = 0; i < this.mnVal.size(); i++) {
            Float f2 = (Float) this.mnVal.get(i);
            this.mnVal.set(i, f);
        }
    }
}
