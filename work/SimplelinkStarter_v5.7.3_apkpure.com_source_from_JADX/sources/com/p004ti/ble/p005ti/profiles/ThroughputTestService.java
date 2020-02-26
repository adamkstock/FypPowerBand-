package com.p004ti.ble.p005ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.p004ti.ble.common.GenericBluetoothProfile;
import com.p004ti.util.TrippleSparkLineView;
import com.p004ti.util.bleUtility;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.ble.ti.profiles.ThroughputTestService */
public class ThroughputTestService extends GenericBluetoothProfile {
    public static final String TAG = ThroughputTestService.class.getSimpleName();
    public static final String throughputServiceToggleThroughputTest_UUID = "f0001237-0451-4000-b000-000000000000";
    public static final String throughputServiceUpdatePDU_UUID = "f0001235-0451-4000-b000-000000000000";
    public static final String throughputServiceUpdatePHY_UUID = "f0001236-0451-4000-b000-000000000000";
    public static final String throughputService_UUID = "f0001234-0451-4000-b000-000000000000";
    /* access modifiers changed from: private */
    public int bytesReceivedInInterval;
    private long bytesReceivedTotal;
    /* access modifiers changed from: private */
    public ToggleButton codingButton;
    private TextView currentPHYText;
    /* access modifiers changed from: private */
    public TextView currentPacketsPerSecondText;
    /* access modifiers changed from: private */
    public TextView currentSpeedText;
    /* access modifiers changed from: private */
    public Switch enableThroughputTest;
    private long lastSequenceNumber;
    /* access modifiers changed from: private */
    public ToggleButton oneMbsButton;
    /* access modifiers changed from: private */
    public int packetsReceivedInInterval;
    private SeekBar pduLengthSeekBar;
    /* access modifiers changed from: private */
    public TrippleSparkLineView speedGraph;
    public ThroughputTestServiceTableRow tRow;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic toggleThroughputTestChar;
    /* access modifiers changed from: private */
    public ToggleButton twoMbsButton;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic updatePDUChar;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic updatePHYChar;

    public void disableService() {
    }

    public void periodWasUpdated(int i) {
    }

    public ThroughputTestService(Context context, BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService) {
        super(context, bluetoothDevice, bluetoothGattService);
        this.tRow = new ThroughputTestServiceTableRow(context);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : this.mBTService.getCharacteristics()) {
            if (bluetoothGattCharacteristic.getUuid().toString().equals(throughputServiceUpdatePDU_UUID)) {
                this.updatePDUChar = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(throughputServiceUpdatePHY_UUID)) {
                this.updatePHYChar = bluetoothGattCharacteristic;
            }
            if (bluetoothGattCharacteristic.getUuid().toString().equals(throughputServiceToggleThroughputTest_UUID)) {
                this.toggleThroughputTestChar = bluetoothGattCharacteristic;
            }
        }
        this.oneMbsButton = (ToggleButton) this.tRow.findViewById(R.id.tpttr_one_mbs_button);
        this.oneMbsButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    ThroughputTestService.this.twoMbsButton.setChecked(false);
                    ThroughputTestService.this.codingButton.setChecked(false);
                    ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, 0);
                    ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
                    String str = ThroughputTestService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Set PHY to ");
                    sb.append(0);
                    Log.d(str, sb.toString());
                }
            }
        });
        this.twoMbsButton = (ToggleButton) this.tRow.findViewById(R.id.tpttr_two_mbs_button);
        this.twoMbsButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    ThroughputTestService.this.oneMbsButton.setChecked(false);
                    ThroughputTestService.this.codingButton.setChecked(false);
                    ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, 1);
                    ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
                    String str = ThroughputTestService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Set PHY to ");
                    sb.append(1);
                    Log.d(str, sb.toString());
                }
            }
        });
        this.codingButton = (ToggleButton) this.tRow.findViewById(R.id.tpttr_coded_button);
        this.codingButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    ThroughputTestService.this.oneMbsButton.setChecked(false);
                    ThroughputTestService.this.twoMbsButton.setChecked(false);
                    ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, 2);
                    ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
                    String str = ThroughputTestService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Set PHY to ");
                    sb.append(2);
                    Log.d(str, sb.toString());
                }
            }
        });
        this.enableThroughputTest = (Switch) this.tRow.findViewById(R.id.tpttr_enable_throughput_test_switch);
        this.enableThroughputTest.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.toggleThroughputTestChar, new byte[]{2});
                String str = ThroughputTestService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Throughput Test :");
                sb.append(z ? "Enabled" : "Disabled");
                Log.d(str, sb.toString());
                new Thread(new Runnable() {
                    public void run() {
                        while (ThroughputTestService.this.enableThroughputTest.isChecked()) {
                            try {
                                Thread.sleep(1000, 0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            final int access$1300 = ThroughputTestService.this.bytesReceivedInInterval;
                            final int access$1400 = ThroughputTestService.this.packetsReceivedInInterval;
                            ThroughputTestService.this.bytesReceivedInInterval = 0;
                            ThroughputTestService.this.packetsReceivedInInterval = 0;
                            ThroughputTestService.this.speedGraph.post(new Runnable() {
                                public void run() {
                                    ThroughputTestService.this.speedGraph.addValue((float) access$1300);
                                }
                            });
                            ThroughputTestService.this.currentSpeedText.post(new Runnable() {
                                public void run() {
                                    ThroughputTestService.this.currentSpeedText.setText(String.format("%d Bps", new Object[]{Integer.valueOf(access$1300)}));
                                }
                            });
                            ThroughputTestService.this.currentPacketsPerSecondText.post(new Runnable() {
                                public void run() {
                                    ThroughputTestService.this.currentPacketsPerSecondText.setText(String.format("%d p/s", new Object[]{Integer.valueOf(access$1400)}));
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        this.pduLengthSeekBar = (SeekBar) this.tRow.findViewById(R.id.tpttr_pduLengthSeekBar);
        this.pduLengthSeekBar.setProgress(0);
        this.pduLengthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePDUChar, (byte) (seekBar.getProgress() + 20));
                String str = ThroughputTestService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Set PDU size to ");
                sb.append(seekBar.getProgress() + 20);
                sb.append("bytes");
                Log.d(str, sb.toString());
            }
        });
        this.currentPHYText = (TextView) this.tRow.findViewById(R.id.tpttr_current_phy_textview);
        this.currentPHYText.setText("---");
        this.currentSpeedText = (TextView) this.tRow.findViewById(R.id.tpttr_current_speed_label);
        this.currentPHYText.setText("0 2131558680");
        this.currentPacketsPerSecondText = (TextView) this.tRow.findViewById(R.id.tpttr_packets_per_second_text);
        this.currentPacketsPerSecondText.setText("0 2131558698");
        this.speedGraph = (TrippleSparkLineView) this.tRow.findViewById(R.id.tpttr_throughputGraph);
        this.speedGraph.setColor(0, 0, 0, 0, 1);
        this.speedGraph.setColor(0, 0, 0, 0, 2);
        this.speedGraph.setColor(255, 255, 0, 0);
        TrippleSparkLineView trippleSparkLineView = this.speedGraph;
        trippleSparkLineView.autoScale = true;
        trippleSparkLineView.autoScaleBounceBack = true;
        this.bytesReceivedInInterval = 0;
        this.bytesReceivedTotal = 0;
    }

    public static boolean isCorrectService(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString().compareTo(throughputService_UUID) == 0;
    }

    public void didReadValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super.didReadValueForCharacteristic(bluetoothGattCharacteristic);
        if (bluetoothGattCharacteristic.getUuid().toString().equals(this.updatePDUChar.getUuid().toString())) {
            this.pduLengthSeekBar.setProgress(bluetoothGattCharacteristic.getValue()[0] - 20);
        } else if (bluetoothGattCharacteristic.getUuid().toString().equals(this.updatePHYChar.getUuid().toString())) {
            byte b = bluetoothGattCharacteristic.getValue()[0];
            if (b == 0) {
                this.oneMbsButton.setChecked(true);
                this.twoMbsButton.setChecked(false);
                this.codingButton.setChecked(false);
                this.currentPHYText.setText("1 Mbps");
            } else if (b == 1) {
                this.oneMbsButton.setChecked(false);
                this.twoMbsButton.setChecked(true);
                this.codingButton.setChecked(false);
                this.currentPHYText.setText("2 Mbps");
            } else if (b == 2) {
                this.oneMbsButton.setChecked(false);
                this.twoMbsButton.setChecked(false);
                this.codingButton.setChecked(true);
                this.currentPHYText.setText("Coded");
            }
        }
    }

    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic.getUuid().toString().equals(this.toggleThroughputTestChar.getUuid().toString())) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (bluetoothGattCharacteristic.getValue().length > 4) {
                long BUILD_UINT32 = bleUtility.BUILD_UINT32(value[0], value[1], value[2], value[3]);
                if (BUILD_UINT32 > this.lastSequenceNumber) {
                    this.bytesReceivedInInterval += bluetoothGattCharacteristic.getValue().length;
                    this.packetsReceivedInInterval++;
                    this.lastSequenceNumber = BUILD_UINT32;
                    return;
                }
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("RX with length ");
                sb.append(bluetoothGattCharacteristic.getValue().length);
                sb.append(" SEQ:");
                sb.append(String.format("0x%08x", new Object[]{Long.valueOf(BUILD_UINT32)}));
                Log.d(str, sb.toString());
                this.lastSequenceNumber = BUILD_UINT32;
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void enableService() {
        this.dev.readCharacteristicSync(this.updatePDUChar);
        this.dev.readCharacteristicSync(this.updatePHYChar);
        this.dev.readCharacteristicSync(this.toggleThroughputTestChar);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThroughputTestService.this.dev.requestMTUChange(245);
            }
        }).start();
    }

    public void configureService() {
        this.dev.setCharacteristicNotificationSync(this.toggleThroughputTestChar, true);
        this.dev.setCurrentConnectionPriority(1);
    }

    public void deConfigureService() {
        this.dev.setCharacteristicNotificationSync(this.toggleThroughputTestChar, false);
    }

    public TableRow getTableRow() {
        return this.tRow;
    }
}
