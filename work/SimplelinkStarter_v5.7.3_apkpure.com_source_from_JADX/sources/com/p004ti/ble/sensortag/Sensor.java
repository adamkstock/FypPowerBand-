package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothGattCharacteristic;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.util.Point3D;
import java.util.List;
import java.util.UUID;

/* renamed from: com.ti.ble.sensortag.Sensor */
public enum Sensor {
    IR_TEMPERATURE(SensorTagGatt.UUID_IRT_SERV, SensorTagGatt.UUID_IRT_DATA, SensorTagGatt.UUID_IRT_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            double extractAmbientTemperature = extractAmbientTemperature(bArr);
            Point3D point3D2 = new Point3D(extractAmbientTemperature, extractTargetTemperature(bArr, extractAmbientTemperature), extractTargetTemperatureTMP007(bArr));
            return point3D2;
        }

        private double extractAmbientTemperature(byte[] bArr) {
            return ((double) Sensor.shortUnsignedAtOffset(bArr, 2).intValue()) / 128.0d;
        }

        private double extractTargetTemperature(byte[] bArr, double d) {
            double d2 = d + 273.15d;
            double d3 = d2 - 298.15d;
            double doubleValue = (Sensor.shortSignedAtOffset(bArr, 0).doubleValue() * 1.5625E-7d) - (((-5.7E-7d * d3) - 140671.9229952d) + (Math.pow(d3, 2.0d) * 4.63E-9d));
            return Math.pow(Math.pow(d2, 4.0d) + ((doubleValue + (Math.pow(doubleValue, 2.0d) * 13.4d)) / ((((0.00175d * d3) + 1.0d) + (Math.pow(d3, 2.0d) * -1.678E-5d)) * 5.593E-14d)), 0.25d) - 273.15d;
        }

        private double extractTargetTemperatureTMP007(byte[] bArr) {
            return ((double) Sensor.shortUnsignedAtOffset(bArr, 0).intValue()) / 128.0d;
        }
    },
    MOVEMENT_ACC(SensorTagGatt.UUID_MOV_SERV, SensorTagGatt.UUID_MOV_DATA, SensorTagGatt.UUID_MOV_CONF, 3) {
        public Point3D convert(byte[] bArr) {
            if (bArr != null) {
                Point3D point3D = new Point3D((double) ((((float) ((bArr[7] << 8) + bArr[6])) / 4096.0f) * -1.0f), (double) (((float) ((bArr[9] << 8) + bArr[8])) / 4096.0f), (double) ((((float) ((bArr[11] << 8) + bArr[10])) / 4096.0f) * -1.0f));
                return point3D;
            }
            Point3D point3D2 = new Point3D(0.0d, 0.0d, 0.0d);
            return point3D2;
        }
    },
    MOVEMENT_GYRO(SensorTagGatt.UUID_MOV_SERV, SensorTagGatt.UUID_MOV_DATA, SensorTagGatt.UUID_MOV_CONF, 3) {
        public Point3D convert(byte[] bArr) {
            if (bArr != null) {
                Point3D point3D = new Point3D((double) (((float) ((bArr[1] << 8) + bArr[0])) / 128.0f), (double) (((float) ((bArr[3] << 8) + bArr[2])) / 128.0f), (double) (((float) ((bArr[5] << 8) + bArr[4])) / 128.0f));
                return point3D;
            }
            Point3D point3D2 = new Point3D(0.0d, 0.0d, 0.0d);
            return point3D2;
        }
    },
    MOVEMENT_MAG(SensorTagGatt.UUID_MOV_SERV, SensorTagGatt.UUID_MOV_DATA, SensorTagGatt.UUID_MOV_CONF, 3) {
        public Point3D convert(byte[] bArr) {
            byte[] bArr2 = bArr;
            if (bArr2 == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            } else if (bArr2.length >= 18) {
                Point3D point3D2 = new Point3D((double) (((float) ((bArr2[13] << 8) + bArr2[12])) / 6.0f), (double) (((float) ((bArr2[15] << 8) + bArr2[14])) / 6.0f), (double) (((float) ((bArr2[17] << 8) + bArr2[16])) / 6.0f));
                return point3D2;
            } else {
                Point3D point3D3 = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D3;
            }
        }
    },
    ACCELEROMETER(SensorTagGatt.UUID_ACC_SERV, SensorTagGatt.UUID_ACC_DATA, SensorTagGatt.UUID_ACC_CONF, 3) {
        public Point3D convert(byte[] bArr) {
            Point3D point3D;
            DeviceActivity instance = DeviceActivity.getInstance();
            if (bArr == null) {
                Point3D point3D2 = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D2;
            } else if (instance.isSensorTag2()) {
                Point3D point3D3 = new Point3D((double) (((float) ((bArr[0] << 8) + bArr[1])) / 4096.0f), (double) (((float) ((bArr[2] << 8) + bArr[3])) / 4096.0f), (double) (((float) ((bArr[4] << 8) + bArr[5])) / 4096.0f));
                return point3D3;
            } else {
                Integer valueOf = Integer.valueOf(bArr[0]);
                Integer valueOf2 = Integer.valueOf(bArr[1]);
                Integer valueOf3 = Integer.valueOf(bArr[2] * -1);
                if (instance.firmwareRevision().contains("1.5")) {
                    point3D = new Point3D((double) (((float) valueOf.intValue()) / 64.0f), (double) (((float) valueOf2.intValue()) / 64.0f), (double) (((float) valueOf3.intValue()) / 64.0f));
                } else {
                    point3D = new Point3D((double) (((float) valueOf.intValue()) / 16.0f), (double) (((float) valueOf2.intValue()) / 16.0f), (double) (((float) valueOf3.intValue()) / 16.0f));
                }
                return point3D;
            }
        }
    },
    HUMIDITY(SensorTagGatt.UUID_HUM_SERV, SensorTagGatt.UUID_HUM_DATA, SensorTagGatt.UUID_HUM_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            int intValue = Sensor.shortUnsignedAtOffset(bArr, 2).intValue();
            Point3D point3D2 = new Point3D((double) (((((float) (intValue - (intValue % 4))) / 65535.0f) * 125.0f) - 0.75f), 0.0d, 0.0d);
            return point3D2;
        }
    },
    HUMIDITY2(SensorTagGatt.UUID_HUM_SERV, SensorTagGatt.UUID_HUM_DATA, SensorTagGatt.UUID_HUM_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            Point3D point3D2 = new Point3D((double) ((((float) Sensor.shortUnsignedAtOffset(bArr, 2).intValue()) / 65535.0f) * 100.0f), 0.0d, 0.0d);
            return point3D2;
        }
    },
    TEMPHUM(SensorTagGatt.UUID_HUM_SERV, SensorTagGatt.UUID_HUM_DATA, SensorTagGatt.UUID_HUM_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            Point3D point3D2 = new Point3D((double) (((((float) Sensor.shortUnsignedAtOffset(bArr, 0).intValue()) / 65535.0f) * 165.0f) - 40.0f), 0.0d, 0.0d);
            return point3D2;
        }
    },
    MAGNETOMETER(SensorTagGatt.UUID_MAG_SERV, SensorTagGatt.UUID_MAG_DATA, SensorTagGatt.UUID_MAG_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            Point3D point3D2 = MagnetometerCalibrationCoefficients.INSTANCE.val;
            Point3D point3D3 = new Point3D(((double) ((((float) Sensor.shortSignedAtOffset(bArr, 0).intValue()) * 0.030517578f) * -1.0f)) - point3D2.f71x, ((double) ((((float) Sensor.shortSignedAtOffset(bArr, 2).intValue()) * 0.030517578f) * -1.0f)) - point3D2.f72y, ((double) (((float) Sensor.shortSignedAtOffset(bArr, 4).intValue()) * 0.030517578f)) - point3D2.f73z);
            return point3D3;
        }
    },
    LUXOMETER(SensorTagGatt.UUID_OPT_SERV, SensorTagGatt.UUID_OPT_DATA, SensorTagGatt.UUID_OPT_CONF) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            Integer access$100 = Sensor.shortUnsignedAtOffset(bArr, 0);
            int intValue = access$100.intValue() & 4095;
            Point3D point3D2 = new Point3D((((double) intValue) * Math.pow(2.0d, (double) ((access$100.intValue() >> 12) & 255))) / 100.0d, 0.0d, 0.0d);
            return point3D2;
        }
    },
    GYROSCOPE(SensorTagGatt.UUID_GYR_SERV, SensorTagGatt.UUID_GYR_DATA, SensorTagGatt.UUID_GYR_CONF, 7) {
        public Point3D convert(byte[] bArr) {
            if (bArr == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            }
            float intValue = ((float) Sensor.shortSignedAtOffset(bArr, 2).intValue()) * 0.0076293945f;
            Point3D point3D2 = new Point3D((double) intValue, (double) (((float) Sensor.shortSignedAtOffset(bArr, 0).intValue()) * 0.0076293945f * -1.0f), (double) (((float) Sensor.shortSignedAtOffset(bArr, 4).intValue()) * 0.0076293945f));
            return point3D2;
        }
    },
    BAROMETER(SensorTagGatt.UUID_BAR_SERV, SensorTagGatt.UUID_BAR_DATA, SensorTagGatt.UUID_BAR_CONF) {
        public Point3D convert(byte[] bArr) {
            byte[] bArr2 = bArr;
            if (bArr2 == null) {
                Point3D point3D = new Point3D(0.0d, 0.0d, 0.0d);
                return point3D;
            } else if (!DeviceActivity.getInstance().isSensorTag2()) {
                List<Integer> list = BarometerCalibrationCoefficients.INSTANCE.barometerCalibrationCoefficients;
                if (list == null) {
                    Point3D point3D2 = new Point3D(0.0d, 0.0d, 0.0d);
                    return point3D2;
                }
                int[] iArr = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    iArr[i] = ((Integer) list.get(i)).intValue();
                }
                Integer access$200 = Sensor.shortSignedAtOffset(bArr2, 0);
                Integer access$100 = Sensor.shortUnsignedAtOffset(bArr2, 2);
                Point3D point3D3 = new Point3D(Double.valueOf(((Double.valueOf((((double) iArr[2]) + (((double) (iArr[3] * access$200.intValue())) / Math.pow(2.0d, 17.0d))) + (((((double) (iArr[4] * access$200.intValue())) / Math.pow(2.0d, 15.0d)) * ((double) access$200.intValue())) / Math.pow(2.0d, 19.0d))).doubleValue() * ((double) access$100.intValue())) + Double.valueOf(((((double) iArr[5]) * Math.pow(2.0d, 14.0d)) + (((double) (iArr[6] * access$200.intValue())) / Math.pow(2.0d, 3.0d))) + (((((double) (iArr[7] * access$200.intValue())) / Math.pow(2.0d, 15.0d)) * ((double) access$200.intValue())) / Math.pow(2.0d, 4.0d))).doubleValue()) / Math.pow(2.0d, 14.0d)).doubleValue(), 0.0d, 0.0d);
                return point3D3;
            } else if (bArr2.length > 4) {
                Point3D point3D4 = new Point3D(((double) Sensor.twentyFourBitUnsignedAtOffset(bArr2, 2).intValue()) / 100.0d, 0.0d, 0.0d);
                return point3D4;
            } else {
                Integer access$1002 = Sensor.shortUnsignedAtOffset(bArr2, 2);
                Point3D point3D5 = new Point3D((((double) (access$1002.intValue() & 4095)) * Math.pow(2.0d, (double) ((access$1002.intValue() >> 12) & 255))) / 100.0d, 0.0d, 0.0d);
                return point3D5;
            }
        }
    };
    
    public static final byte CALIBRATE_SENSOR_CODE = 2;
    public static final byte DISABLE_SENSOR_CODE = 0;
    public static final byte ENABLE_SENSOR_CODE = 1;
    public static final Sensor[] SENSOR_LIST = null;
    private final UUID config;
    private final UUID data;
    private byte enableCode;
    private final UUID service;

    static {
        Sensor sensor;
        Sensor sensor2;
        Sensor sensor3;
        Sensor sensor4;
        Sensor sensor5;
        Sensor sensor6;
        Sensor sensor7;
        SENSOR_LIST = new Sensor[]{sensor, sensor2, sensor4, sensor5, sensor6, sensor3, sensor7};
    }

    /* access modifiers changed from: private */
    public static Integer shortSignedAtOffset(byte[] bArr, int i) {
        if (bArr.length < i + 2) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf((Integer.valueOf(bArr[i + 1]).intValue() << 8) + Integer.valueOf(bArr[i] & 255).intValue());
    }

    /* access modifiers changed from: private */
    public static Integer shortUnsignedAtOffset(byte[] bArr, int i) {
        if (bArr.length < i + 2) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf((Integer.valueOf(bArr[i + 1] & 255).intValue() << 8) + Integer.valueOf(bArr[i] & 255).intValue());
    }

    /* access modifiers changed from: private */
    public static Integer twentyFourBitUnsignedAtOffset(byte[] bArr, int i) {
        if (bArr.length < i + 3) {
            return Integer.valueOf(0);
        }
        Integer valueOf = Integer.valueOf(bArr[i] & 255);
        return Integer.valueOf((Integer.valueOf(bArr[i + 2] & 255).intValue() << 16) + (Integer.valueOf(bArr[i + 1] & 255).intValue() << 8) + valueOf.intValue());
    }

    public void onCharacteristicChanged(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        throw new UnsupportedOperationException("Error: the individual enum classes are supposed to override this method.");
    }

    public Point3D convert(byte[] bArr) {
        throw new UnsupportedOperationException("Error: the individual enum classes are supposed to override this method.");
    }

    private Sensor(UUID uuid, UUID uuid2, UUID uuid3, byte b) {
        this.service = uuid;
        this.data = uuid2;
        this.config = uuid3;
        this.enableCode = b;
    }

    private Sensor(UUID uuid, UUID uuid2, UUID uuid3) {
        this.service = uuid;
        this.data = uuid2;
        this.config = uuid3;
        this.enableCode = 1;
    }

    public byte getEnableSensorCode() {
        return this.enableCode;
    }

    public UUID getService() {
        return this.service;
    }

    public UUID getData() {
        return this.data;
    }

    public UUID getConfig() {
        return this.config;
    }

    public static Sensor getFromDataUuid(UUID uuid) {
        Sensor[] values;
        for (Sensor sensor : values()) {
            if (sensor.getData().equals(uuid)) {
                return sensor;
            }
        }
        throw new RuntimeException("unable to find UUID.");
    }
}
