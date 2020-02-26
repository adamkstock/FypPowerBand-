package com.p004ti.util;

/* renamed from: com.ti.util.Point3D */
public class Point3D {

    /* renamed from: x */
    public double f71x;

    /* renamed from: y */
    public double f72y;

    /* renamed from: z */
    public double f73z;

    public Point3D(double d, double d2, double d3) {
        this.f71x = d;
        this.f72y = d2;
        this.f73z = d3;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.f71x);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.f72y);
        int i2 = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        long doubleToLongBits3 = Double.doubleToLongBits(this.f73z);
        return (i2 * 31) + ((int) (doubleToLongBits3 ^ (doubleToLongBits3 >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point3D point3D = (Point3D) obj;
        return Double.doubleToLongBits(this.f71x) == Double.doubleToLongBits(point3D.f71x) && Double.doubleToLongBits(this.f72y) == Double.doubleToLongBits(point3D.f72y) && Double.doubleToLongBits(this.f73z) == Double.doubleToLongBits(point3D.f73z);
    }
}
