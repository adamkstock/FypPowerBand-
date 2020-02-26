package com.example.fyppowerbandv002.things;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import java.util.UUID;

/* renamed from: com.ti.ble.launchpad.ProjectZeroLEDProfile */
public class LEDProfile {

    public enum eLED {NULL, LED0, LED1 };

    public static final UUID PRZ_LED0_STATE_CHARACTERISIC_UUID = UUID.fromString("f0001111-0451-4000-b000-000000000000");
    public static final UUID PRZ_LED1_STATE_CHARACTERISIC_UUID = UUID.fromString("f0001112-0451-4000-b000-000000000000");
    public static final UUID PRZ_LED_SERVICE_UUID = UUID.fromString("f0001110-0451-4000-b000-000000000000");

    private BluetoothGattCharacteristic led0C;
    private BluetoothGattCharacteristic led1C;
    private boolean led0On = false;
    private boolean led1On = false;

    public BluetoothGattService createLEDService() {
        BluetoothGattService service = new BluetoothGattService(PRZ_LED_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        led0C = new BluetoothGattCharacteristic(PRZ_LED0_STATE_CHARACTERISIC_UUID
                ,BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY
                , BluetoothGattCharacteristic.PERMISSION_READ);
        // Descriptor?

        led1C = new BluetoothGattCharacteristic(PRZ_LED1_STATE_CHARACTERISIC_UUID
                ,BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY
                , BluetoothGattCharacteristic.PERMISSION_READ);
        // Descriptor?

        service.addCharacteristic(led0C);
        service.addCharacteristic(led1C);
        return service;
    }

    public LEDProfile() {
    }

    public byte[] getLED(eLED ledsel)
    {
        boolean ledval = false;
        switch(ledsel)
        {
            case NULL:
                break;
            case LED0:
                ledval = led0On;
                break;
            case LED1:
                ledval = led1On;
            break;
        }
        byte[] bytes = new byte[1];
        bytes[0] = (byte)(ledval ? 1 : 0);
        return bytes;
    }
    public void toggleLED(eLED ledsel)
    {
        byte[] bytes = new byte[1];
        switch(ledsel)
        {
            case NULL:
                break;
            case LED0:
                led0On = !led0On;
                bytes[0] = (byte)(led0On ? 1 : 0);
                led0C.setValue(bytes);
                break;
            case LED1:
                led1On = !led1On;
                bytes[0] = (byte)(led1On ? 1 : 0);
                led1C.setValue(bytes);
                break;
        }
    }
}
