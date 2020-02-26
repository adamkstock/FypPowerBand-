package com.p004ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TableRow;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.simplelinkstarter.R;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ti.ble.sensortag.SensorTagSimpleKeysProfile */
public class SensorTagSimpleKeysProfile extends GenericBluetoothProfile {
    SensorTagSimpleKeysTableRow myRow;

    public SensorTagSimpleKeysProfile(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.myRow = new SensorTagSimpleKeysTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(SensorTagGatt.UUID_KEY_DATA.toString())) {
                this.dataC = bluetoothGattCharacteristic;
            }
        }
        ImageView imageView = (ImageView) this.myRow.findViewById(R.id.sktr_icon);
        this.myRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "simplekeys");
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_KEY_SERV.toString()) == 0;
    }

    public void enableService() {
        this.isEnabled = true;
    }

    public void disableService() {
        this.isEnabled = false;
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.equals(this.dataC)) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            switch (value[0]) {
                case 1:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_on);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_off);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_off);
                    break;
                case 2:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_off);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_on);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_off);
                    break;
                case 3:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_on);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_on);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_off);
                    break;
                case 4:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_off);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_off);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_on);
                    break;
                case 5:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_on);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_off);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_on);
                    break;
                case 6:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_off);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_on);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_on);
                    break;
                case 7:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_on);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_on);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_on);
                    break;
                default:
                    this.myRow.leftKeyPressStateImage.setImageResource(R.mipmap.left_key_off);
                    this.myRow.rightKeyPressStateImage.setImageResource(R.mipmap.right_key_off);
                    this.myRow.reedStateImage.setImageResource(R.mipmap.reed_relay_off);
                    break;
            }
            this.myRow.lastKeys = value[0];
        }
    }

    public Map<String, String> getMQTTMap() {
        byte[] value = this.dataC.getValue();
        HashMap hashMap = new HashMap();
        String str = "reed_relay";
        String str2 = "key_2";
        String str3 = "key_1";
        if (value != null) {
            String str4 = "%d";
            hashMap.put(str3, String.format(str4, new Object[]{Integer.valueOf(value[0] & 1)}));
            hashMap.put(str2, String.format(str4, new Object[]{Integer.valueOf(value[0] & 2)}));
            hashMap.put(str, String.format(str4, new Object[]{Integer.valueOf(value[0] & 4)}));
        } else {
            String str5 = "0";
            hashMap.put(str3, str5);
            hashMap.put(str2, str5);
            hashMap.put(str, str5);
        }
        return hashMap;
    }

    public TableRow getTableRow() {
        return this.myRow;
    }
}
