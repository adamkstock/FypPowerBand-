package com.p004ti.ble.sensortag;

import java.util.List;

/* renamed from: com.ti.ble.sensortag.BarometerCalibrationCoefficients */
public enum BarometerCalibrationCoefficients {
    INSTANCE;
    
    public volatile List<Integer> barometerCalibrationCoefficients;
    public volatile double heightCalibration;
}
