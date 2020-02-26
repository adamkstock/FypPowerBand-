package com.p004ti.ble.common.oad;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.p004ti.ble.btsig.DeviceInformationServiceProfile;
import com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadCallBack;
import com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadImageStatus;
import com.p004ti.ble.common.oad.FWUpdateNGProgrammingHandler.oadParameters.oadMode;
import com.p004ti.ble.p005ti.profiles.TIConnectionControlService;
import com.p004ti.ble.p005ti.profiles.TIOADProfile;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.PreferenceWR;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.util.bleUtility;
import com.p004ti.util.bleUtility.boardType;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* renamed from: com.ti.ble.common.oad.FWUpdateNGActivity */
public class FWUpdateNGActivity extends AppCompatActivity {
    public static final float BT_GATT_CON_INT_IDEAL = 30.0f;
    public static final int BT_GATT_DELAY = 500;
    public static final String TAG = "FWUpdateNGActivity";
    public static final String TI_BT_OAD_IMAGE_BLOCK_REQ = "F000FFC2-0451-4000-B000-000000000000";
    public static final String TI_BT_OAD_IMAGE_NOTIFY = "F000FFC1-0451-4000-B000-000000000000";
    ToggleButton advancedButton;
    public BluetoothGattCallback bTCB = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onConnectionStateChange(bluetoothGatt, i, i2);
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionStateChange: ");
            sb.append(bleUtility.ConnectionStateStringFromState(i2));
            sb.append(" FW Upgrade State : ");
            FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
            sb.append(fWUpdateNGActivity.printState(fWUpdateNGActivity.state));
            sb.append(" (");
            sb.append(FWUpdateNGActivity.this.state);
            sb.append(")");
            String sb2 = sb.toString();
            String str = FWUpdateNGActivity.TAG;
            Log.d(str, sb2);
            if (i2 == 0) {
                FWUpdateNGActivity fWUpdateNGActivity2 = FWUpdateNGActivity.this;
                fWUpdateNGActivity2.isConnected = false;
                if (fWUpdateNGActivity2.state == fwUpdateProcessStages.EXIT) {
                    FWUpdateNGActivity.this.mBtGatt.close();
                }
                if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.DISCONNECTED) {
                    Log.d(str, "Device disconnected, start of OAD dialog");
                } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.CONNECTED || FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfFWREVISION || FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfCONINTERVAL) {
                    Log.d(str, "Disconnected before OAD got started !");
                    FWUpdateNGActivity fWUpdateNGActivity3 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity3.mBtGatt = fWUpdateNGActivity3.mBtDevice.connectGatt(FWUpdateNGActivity.this.mThis, false, FWUpdateNGActivity.this.bTCB);
                } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfDisconnect) {
                    String str2 = "Present finished dialog";
                    if (FWUpdateNGActivity.this.sensorTagMultiStage != fwUpdateMultistageStage.SINGLE_STAGE) {
                        Log.d(str, "Device disconnected in multistage mode, check stage is complete");
                        if (FWUpdateNGActivity.this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_3) {
                            Log.d(str, str2);
                            if (FWUpdateNGActivity.this.waitForDisconnectDialog != null && FWUpdateNGActivity.this.waitForDisconnectDialog.isShowing()) {
                                FWUpdateNGActivity.this.waitForDisconnectDialog.dismiss();
                            }
                            FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    new Builder(FWUpdateNGActivity.this.mThis) {
                                        {
                                            setTitle("Finished Successfully !");
                                            setMessage("Device disconnected and restarted, OAD complete");
                                            setPositiveButton("OK", new OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FWUpdateNGActivity.this.mThis.finish();
                                                }
                                            });
                                        }
                                    }.create().show();
                                }
                            });
                            FWUpdateNGActivity.this.mBtGatt.close();
                            return;
                        }
                        return;
                    }
                    Log.d(str, str2);
                    if (FWUpdateNGActivity.this.waitForDisconnectDialog != null && FWUpdateNGActivity.this.waitForDisconnectDialog.isShowing()) {
                        FWUpdateNGActivity.this.waitForDisconnectDialog.dismiss();
                    }
                    FWUpdateNGActivity.this.mBtGatt.close();
                } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfBLUETOOTHRESET) {
                    FWUpdateNGActivity fWUpdateNGActivity4 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity4.restartBluetooth(fWUpdateNGActivity4.mThis);
                } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfBLUETOOTHRESETCOMPLETE) {
                    Log.d(str, "Device disconnected in stage wfBLUETOOTHRESETCOMPLETE");
                } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                    Log.d(str, "Device disconnected while in programming !");
                    FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!FWUpdateNGActivity.this.isFinishing()) {
                                new Builder(FWUpdateNGActivity.this.mThis) {
                                    {
                                        setTitle("Device Disconnect !");
                                        setMessage("Device disconnected while programming was not finished ! Please exit to main screen and remove and insert battery in device and try again !");
                                        setPositiveButton("OK", new OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                FWUpdateNGActivity.this.mThis.mFWUpdateNGProgrammingHandler.abort = true;
                                            }
                                        });
                                    }
                                }.create().show();
                            }
                        }
                    });
                    FWUpdateNGActivity.this.state = fwUpdateProcessStages.ERROR;
                }
            } else if (i2 == 2) {
                FWUpdateNGActivity fWUpdateNGActivity5 = FWUpdateNGActivity.this;
                fWUpdateNGActivity5.isConnected = true;
                if (fWUpdateNGActivity5.state == fwUpdateProcessStages.DISCONNECTED || FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfFWREVISION || FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfCONINTERVAL) {
                    Log.d(str, "Device connected, start device scan");
                    FWUpdateNGActivity fWUpdateNGActivity6 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity6.mBtBlocked = true;
                    fWUpdateNGActivity6.state = fwUpdateProcessStages.wfFWREVISION;
                    FWUpdateNGActivity fWUpdateNGActivity7 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity7.mBtGatt = bluetoothGatt;
                    fWUpdateNGActivity7.mBtGatt.discoverServices();
                    FWUpdateNGActivity.this.mBtGatt.requestConnectionPriority(1);
                }
                if ((FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfDisconnect || FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfBLUETOOTHRESET) && FWUpdateNGActivity.this.sensorTagMultiStage != fwUpdateMultistageStage.SINGLE_STAGE) {
                    FWUpdateNGActivity fWUpdateNGActivity8 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity8.mBtBlocked = true;
                    fWUpdateNGActivity8.state = fwUpdateProcessStages.wfFWREVISION;
                    FWUpdateNGActivity fWUpdateNGActivity9 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity9.mBtGatt = bluetoothGatt;
                    fWUpdateNGActivity9.mBtGatt.discoverServices();
                    FWUpdateNGActivity.this.mBtGatt.requestConnectionPriority(1);
                }
                if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADFINISHED) {
                    Log.d(str, "Finished, pop to root activity !");
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            super.onServicesDiscovered(bluetoothGatt, i);
            StringBuilder sb = new StringBuilder();
            sb.append("onServicesDiscovered ");
            sb.append(bleUtility.ServiceStateStringFromStatus(i));
            sb.append(" State: ");
            sb.append(FWUpdateNGActivity.this.state.ordinal());
            Log.d(FWUpdateNGActivity.TAG, sb.toString());
            new Thread(new Runnable() {
                public void run() {
                    FWUpdateNGActivity.this.readVersionFromDeviceInfo();
                }
            }).start();
        }

        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
            FWUpdateNGActivity.this.mBtBlocked = false;
            StringBuilder sb = new StringBuilder();
            sb.append("onCharacteristicRead ");
            sb.append(bleUtility.getStringFromByteVector(bluetoothGattCharacteristic.getValue()));
            sb.append(" State: ");
            sb.append(FWUpdateNGActivity.this.state.ordinal());
            Log.d(FWUpdateNGActivity.TAG, sb.toString());
            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString(DeviceInformationServiceProfile.dISFirmwareREV_UUID))) {
                final byte[] value = bluetoothGattCharacteristic.getValue();
                try {
                    String str = new String(value);
                    String substring = str.substring(0, str.indexOf(" "));
                    FWUpdateNGActivity.this.currentVersion = Float.parseFloat(substring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (FWUpdateNGActivity.this.state != fwUpdateProcessStages.OADINPROGRESS) {
                    FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfCONINTERVAL;
                }
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.fwRevisionLabel.setText(new String(value));
                    }
                });
                new Thread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.readCurConParams();
                    }
                }).start();
                FWUpdateNGActivity.this.runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.fwProgressConIntLabel.setText("Reading...");
                    }
                }));
            }
            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString("f000ccc1-0451-4000-b000-000000000000"))) {
                final byte[] value2 = bluetoothGattCharacteristic.getValue();
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.currentConParams = TIConnectionControlService.getValuesFromBytes(value2);
                        TextView textView = FWUpdateNGActivity.this.fwProgressConIntLabel;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(FWUpdateNGActivity.this.currentConParams[0]);
                        sb.append(" ms");
                        textView.setText(sb.toString());
                        FWUpdateNGActivity.this.getIdealConParams(FWUpdateNGActivity.this.currentConParams[0]);
                    }
                });
            }
            if (bluetoothGattCharacteristic.getService().getUuid().equals(UUID.fromString(TIOADProfile.oadService_UUID))) {
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
            FWUpdateNGActivity.this.mBtBlocked = false;
            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString("f000ccc2-0451-4000-b000-000000000000"))) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            FWUpdateNGActivity.this.readCurConParams();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            if (bluetoothGattCharacteristic.getService().getUuid().equals(UUID.fromString(TIOADProfile.oadService_UUID))) {
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            if (bluetoothGattCharacteristic.getService().getUuid().equals(UUID.fromString(TIOADProfile.oadService_UUID))) {
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            }
        }

        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
            Log.d(FWUpdateNGActivity.TAG, "onDescriptorRead");
            if (bluetoothGattDescriptor.getCharacteristic().getService().getUuid().equals(UUID.fromString(TIOADProfile.oadService_UUID))) {
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
            }
            FWUpdateNGActivity.this.mBtBlocked = false;
        }

        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
            if (bluetoothGattDescriptor.getCharacteristic().getService().getUuid().equals(UUID.fromString(TIOADProfile.oadService_UUID))) {
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
            }
            FWUpdateNGActivity.this.mBtBlocked = false;
        }

        public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
            super.onReliableWriteCompleted(bluetoothGatt, i);
            Log.d(FWUpdateNGActivity.TAG, "onReliableWriteCompleted");
            FWUpdateNGActivity.this.mBtBlocked = false;
        }

        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onReadRemoteRssi(bluetoothGatt, i, i2);
            Log.d(FWUpdateNGActivity.TAG, "onReadRemoteRssi");
        }

        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onMtuChanged(bluetoothGatt, i, i2);
            Log.d(FWUpdateNGActivity.TAG, "onMtuChanged");
        }
    };
    float[] currentConParams;
    float currentVersion;
    boolean finishedReset = false;
    List<FWUpdateTIFirmwareEntry> fwEntries;
    FWUpdateNGBINFileHandler fwFile;
    ProgressBar fwProgressBar;
    TextView fwProgressBlockLabel;
    TextView fwProgressBytesLabel;
    TextView fwProgressConIntLabel;
    TextView fwProgressLabel;
    TextView fwProgressSpeedLabel;
    TextView fwRevisionLabel;
    ToggleButton highSpeedButton;
    AlertDialog imageStatusDialog;
    boolean isConnected;
    int lastByteCount;
    long lastTime;
    boolean mBtBlocked = false;
    BluetoothDevice mBtDevice;
    BluetoothGatt mBtGatt;
    BluetoothGattCharacteristic mDeviceInformationFirmwareRevision;
    FWUpdateNGProgrammingHandler mFWUpdateNGProgrammingHandler;
    FWUpdateNGActivity mThis = this;
    public oadCallBack myOADCB = new oadCallBack() {
        public void oadImageNotifyRejectedImage(short s, short s2, byte[] bArr) {
        }

        public void oadStartedWithParameters(oadMode oadmode, boolean z, int i, int i2) {
        }

        public void oadProgressWasUpdated(int i, int i2, int i3, int i4) {
            FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
            final int i5 = i;
            final int i6 = i3;
            final int i7 = i2;
            final int i8 = i4;
            C07111 r1 = new Runnable() {
                public void run() {
                    float f = (((float) i5) / ((float) i6)) * 100.0f;
                    FWUpdateNGActivity.this.fwProgressBar.setProgress((int) f);
                    TextView textView = FWUpdateNGActivity.this.fwProgressBlockLabel;
                    StringBuilder sb = new StringBuilder();
                    sb.append(i7);
                    String str = MqttTopic.TOPIC_LEVEL_SEPARATOR;
                    sb.append(str);
                    sb.append(i8);
                    textView.setText(sb.toString());
                    TextView textView2 = FWUpdateNGActivity.this.fwProgressBytesLabel;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(i5);
                    sb2.append(str);
                    sb2.append(i6);
                    textView2.setText(sb2.toString());
                    FWUpdateNGActivity.this.fwProgressLabel.setText(String.format("%.2f%%", new Object[]{Float.valueOf(f)}));
                    int i = i5 - FWUpdateNGActivity.this.lastByteCount;
                    long currentTimeMillis = System.currentTimeMillis() - FWUpdateNGActivity.this.lastTime;
                    if (currentTimeMillis > 250) {
                        FWUpdateNGActivity.this.lastByteCount = i5;
                        FWUpdateNGActivity.this.lastTime = System.currentTimeMillis();
                        TextView textView3 = FWUpdateNGActivity.this.fwProgressSpeedLabel;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(String.format("%.1f", new Object[]{Float.valueOf((((float) i) / ((float) currentTimeMillis)) * 1000.0f)}));
                        sb3.append(" B/s");
                        textView3.setText(sb3.toString());
                    }
                }
            };
            fWUpdateNGActivity.runOnUiThread(r1);
        }

        public void oadImageStatusWasUpdated(final oadImageStatus oadimagestatus) {
            if (FWUpdateNGActivity.this.statusMessageDialogRunnable == null) {
                FWUpdateNGActivity.this.statusMessageDialogRunnable = new Runnable() {
                    public void run() {
                        new Builder(FWUpdateNGActivity.this.mThis) {
                            {
                                setTitle("Status update !");
                                Object[] objArr = new Object[1];
                                String str = oadimagestatus == oadImageStatus.OAD_SUCCESS ? "SUCCESS" : oadimagestatus == oadImageStatus.OAD_BUFFER_OFL ? "BUFFER OVERFLOW" : oadimagestatus == oadImageStatus.OAD_CRC_ERR ? "CRC ERROR" : oadimagestatus == oadImageStatus.OAD_FLASH_ERR ? "FLASH ERROR" : "UNKOWN";
                                objArr[0] = str;
                                setMessage(String.format("Device reports status : %s", objArr));
                                setPositiveButton("OK", new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FWUpdateNGActivity.this.mThis.finish();
                                    }
                                });
                            }
                        }.create().show();
                    }
                };
                FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                fWUpdateNGActivity.runOnUiThread(fWUpdateNGActivity.statusMessageDialogRunnable);
                return;
            }
            Log.d(FWUpdateNGActivity.TAG, "Already showing status message !");
        }

        public void oadTimedoutWhileProgramming() {
            Log.d(FWUpdateNGActivity.TAG, "OAD timeout occured !");
            if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.ERROR;
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        new Builder(FWUpdateNGActivity.this.mThis) {
                            {
                                setTitle("OAD Programming timed out !");
                                setMessage("Got timeout while programming, cannot continue !");
                                setPositiveButton("OK", new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FWUpdateNGActivity.this.mThis.finish();
                                    }
                                });
                            }
                        }.create().show();
                    }
                });
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.disableNotifications();
                FWUpdateNGActivity.this.mFWUpdateNGProgrammingHandler.abort = true;
            }
        }

        public void oadProgrammingFininshed() {
            Log.d(FWUpdateNGActivity.TAG, "Programmed finished !");
            if (FWUpdateNGActivity.this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_1 && FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfBLUETOOTHRESET;
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        new Builder(FWUpdateNGActivity.this.mThis) {
                            {
                                setTitle("Stage 1 finished !");
                                setMessage("Stage one complete, bluetooth will now be reset to continue");
                                setPositiveButton("Continue", new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FWUpdateNGActivity.this.startRestartBluetooth(FWUpdateNGActivity.this.mThis);
                                    }
                                });
                            }
                        }.create().show();
                    }
                });
            } else if (FWUpdateNGActivity.this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_2 && FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.OADFINISHED;
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        new Builder(FWUpdateNGActivity.this.mThis) {
                            {
                                setTitle("Stage 2 finished !");
                                setMessage("Will now continue to stage 3");
                                setPositiveButton("Continue", new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfFILENAME;
                                        FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_3;
                                        FWUpdateNGActivity.this.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                    }
                                });
                            }
                        }.create().show();
                    }
                });
            } else if (FWUpdateNGActivity.this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_3 && FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfBLUETOOTHRESET;
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        new Builder(FWUpdateNGActivity.this.mThis) {
                            {
                                setTitle("Stage 3 finished !");
                                setMessage("Stage three complete, bluetooth will now be reset to finnish");
                                setPositiveButton("Continue", new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FWUpdateNGActivity.this.startRestartBluetooth(FWUpdateNGActivity.this.mThis);
                                    }
                                });
                            }
                        }.create().show();
                    }
                });
            } else if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.OADINPROGRESS) {
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.OADFINISHED;
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.waitForDisconnectDialog = new ProgressDialog(FWUpdateNGActivity.this.mThis);
                        FWUpdateNGActivity.this.waitForDisconnectDialog.setTitle("Waiting for device to disconnect");
                        FWUpdateNGActivity.this.waitForDisconnectDialog.setMessage("Note: If you have programmed an image with different services than the previous, remember to turn off and on bluetooth in the settings of the device to make device force an update to service cache or device will not function properly !");
                        FWUpdateNGActivity.this.waitForDisconnectDialog.setIndeterminate(false);
                        FWUpdateNGActivity.this.waitForDisconnectDialog.setProgressStyle(1);
                    }
                });
                new Thread(new Runnable() {
                    public void run() {
                        final int i = 0;
                        for (final int i2 = 0; i2 < 300; i2++) {
                            try {
                                Thread.sleep(100);
                                if (i2 % 3 == 0) {
                                    i++;
                                }
                                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        FWUpdateNGActivity.this.waitForDisconnectDialog.setProgress(i);
                                        ProgressDialog progressDialog = FWUpdateNGActivity.this.waitForDisconnectDialog;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Timeout in ( ");
                                        sb.append((300 - i2) / 10);
                                        sb.append(" )\n\nNote: If you have programmed an image with different services than the previous, remember to turn off and on bluetooth in the settings of the device to make device force an update to service cache or device will not function properly !");
                                        progressDialog.setMessage(sb.toString());
                                        if (!FWUpdateNGActivity.this.waitForDisconnectDialog.isShowing()) {
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!FWUpdateNGActivity.this.isConnected) {
                                Log.d(FWUpdateNGActivity.TAG, "Device disconnected, everything is peachy !");
                                if (FWUpdateNGActivity.this.waitForDisconnectDialog != null) {
                                    FWUpdateNGActivity.this.waitForDisconnectDialog.dismiss();
                                }
                                Intent intent = new Intent(FWUpdateNGActivity.this.mThis, TopLevel.class);
                                intent.addFlags(67108864);
                                FWUpdateNGActivity.this.startActivity(intent);
                                return;
                            }
                        }
                        FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                FWUpdateNGActivity.this.waitForDisconnectDialog.dismiss();
                                new Builder(FWUpdateNGActivity.this.mThis) {
                                    {
                                        setTitle("Upgrade failed !");
                                        setMessage("Device did not disconnect in time !");
                                        setPositiveButton("OK", null);
                                    }
                                }.create().show();
                            }
                        });
                    }
                }).start();
                FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateNGActivity.this.waitForDisconnectDialog.show();
                    }
                });
                FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfDisconnect;
            }
        }
    };
    int packetsPerInterval;
    TextView packetsPerTimer;
    Button packetsPerTimerMinusButton;
    Button packetsPerTimerPlusButton;
    BroadcastReceiver reciever;
    ProgressDialog resetBTDialog;
    public ScanCallback sCB = new ScanCallback() {
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            StringBuilder sb = new StringBuilder();
            sb.append("Found new device : ");
            sb.append(scanResult.getDevice());
            String sb2 = sb.toString();
            String str = FWUpdateNGActivity.TAG;
            Log.d(str, sb2);
            if (scanResult.getDevice().getAddress().toString().equals(((BluetoothDevice) FWUpdateNGActivity.this.getIntent().getParcelableExtra(DeviceActivity.EXTRA_DEVICE)).getAddress().toString())) {
                Log.d(str, "Found device I need !");
                FWUpdateNGActivity.this.mBtDevice = scanResult.getDevice();
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                fWUpdateNGActivity.mBtGatt = fWUpdateNGActivity.mBtDevice.connectGatt(FWUpdateNGActivity.this.mThis, false, FWUpdateNGActivity.this.bTCB);
            }
        }
    };
    ToggleButton safeModeButton;
    Button selFwButton;
    fwUpdateMultistageStage sensorTagMultiStage = fwUpdateMultistageStage.SINGLE_STAGE;
    boolean startedAutomaticMultistage = false;
    fwUpdateProcessStages state = fwUpdateProcessStages.DISCONNECTED;
    Runnable statusMessageDialogRunnable;
    boolean timeoutStop = false;
    int timerInterval;
    Button timerMinusButton;
    Button timerPlusButton;
    TextView timerTime;
    boolean triedConUpdate = false;
    Switch useNotificationsSwitch;
    ProgressDialog waitForDisconnectDialog;

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGActivity$26 */
    static /* synthetic */ class C073326 {

        /* renamed from: $SwitchMap$com$ti$ble$common$oad$FWUpdateNGActivity$fwUpdateMultistageStage */
        static final /* synthetic */ int[] f39x7ebe413 = new int[fwUpdateMultistageStage.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(31:0|(2:1|2)|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(32:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0072 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x007d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0089 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0095 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00a1 */
        static {
            /*
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateMultistageStage[] r0 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateMultistageStage.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f39x7ebe413 = r0
                r0 = 1
                int[] r1 = f39x7ebe413     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateMultistageStage r2 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateMultistageStage.MULTISTAGE_STAGE_1     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = f39x7ebe413     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateMultistageStage r3 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateMultistageStage.MULTISTAGE_STAGE_2     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = f39x7ebe413     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateMultistageStage r4 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateMultistageStage.MULTISTAGE_STAGE_3     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages[] r3 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                f40xd90db27e = r3
                int[] r3 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x003d }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r4 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.DISCONNECTED     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0047 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r3 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.CONNECTED     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0051 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfBLUETOOTHRESET     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x005c }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfBLUETOOTHRESETCOMPLETE     // Catch:{ NoSuchFieldError -> 0x005c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x005c }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x005c }
            L_0x005c:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0067 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfCONINTERVAL     // Catch:{ NoSuchFieldError -> 0x0067 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0067 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0067 }
            L_0x0067:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0072 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfDisconnect     // Catch:{ NoSuchFieldError -> 0x0072 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0072 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0072 }
            L_0x0072:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x007d }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfFILENAME     // Catch:{ NoSuchFieldError -> 0x007d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x007d }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x007d }
            L_0x007d:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0089 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.wfFWREVISION     // Catch:{ NoSuchFieldError -> 0x0089 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0089 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0089 }
            L_0x0089:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x0095 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.OADFINISHED     // Catch:{ NoSuchFieldError -> 0x0095 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0095 }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0095 }
            L_0x0095:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x00a1 }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.OADINPROGRESS     // Catch:{ NoSuchFieldError -> 0x00a1 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a1 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00a1 }
            L_0x00a1:
                int[] r0 = f40xd90db27e     // Catch:{ NoSuchFieldError -> 0x00ad }
                com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages r1 = com.p004ti.ble.common.oad.FWUpdateNGActivity.fwUpdateProcessStages.ERROR     // Catch:{ NoSuchFieldError -> 0x00ad }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ad }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00ad }
            L_0x00ad:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.p004ti.ble.common.oad.FWUpdateNGActivity.C073326.<clinit>():void");
        }
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateMultistageStage */
    public enum fwUpdateMultistageStage {
        SINGLE_STAGE,
        MULTISTAGE_STAGE_1,
        MULTISTAGE_STAGE_2,
        MULTISTAGE_STAGE_3
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGActivity$fwUpdateProcessStages */
    public enum fwUpdateProcessStages {
        DISCONNECTED,
        CONNECTED,
        wfFWREVISION,
        wfCONINTERVAL,
        wfFILENAME,
        OADINPROGRESS,
        OADFINISHED,
        wfDisconnect,
        wfBLUETOOTHRESET,
        wfBLUETOOTHRESETCOMPLETE,
        ERROR,
        EXIT
    }

    /* renamed from: com.ti.ble.common.oad.FWUpdateNGActivity$timeoutThread */
    public class timeoutThread extends Thread {
        static final int timeoutLen = 20;
        public int timeout;

        public timeoutThread() {
        }

        public void run() {
            super.run();
            String str = FWUpdateNGActivity.TAG;
            Log.d(str, "Timeout thread started ...");
            while (!FWUpdateNGActivity.this.timeoutStop) {
                while (!FWUpdateNGActivity.this.mBtBlocked) {
                    try {
                        Thread.sleep(250, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.timeout = 0;
                while (true) {
                    if (FWUpdateNGActivity.this.mBtBlocked) {
                        this.timeout++;
                        try {
                            Thread.sleep(250, 0);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Timeout : ");
                            sb.append(this.timeout);
                            sb.append(" of ");
                            sb.append(20);
                            Log.d(str, sb.toString());
                            if (this.timeout >= 20) {
                                if (FWUpdateNGActivity.this.state == fwUpdateProcessStages.wfFWREVISION) {
                                    FWUpdateNGActivity.this.mBtBlocked = false;
                                    FWUpdateNGActivity.this.readVersionFromDeviceInfo();
                                }
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("TIMEOUT ! What to do ? state = ");
                                sb2.append(FWUpdateNGActivity.this.state.ordinal());
                                Log.d(str, sb2.toString());
                                FWUpdateNGActivity.this.mBtGatt.disconnect();
                                return;
                            }
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void onWindowFocusChanged(boolean z) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fw_update_ng);
        this.fwRevisionLabel = (TextView) findViewById(R.id.afun_version_label);
        this.fwProgressLabel = (TextView) findViewById(R.id.afun_progress_label);
        this.fwProgressBlockLabel = (TextView) findViewById(R.id.afun_block_label);
        this.fwProgressBytesLabel = (TextView) findViewById(R.id.afun_bytes_label);
        this.fwProgressSpeedLabel = (TextView) findViewById(R.id.afun_bytes_per_sec_label);
        this.fwProgressConIntLabel = (TextView) findViewById(R.id.afun_con_interval);
        this.selFwButton = (Button) findViewById(R.id.afun_sel_fw_file_button);
        this.selFwButton.setEnabled(false);
        this.selFwButton.setText("Setting up ...");
        this.safeModeButton = (ToggleButton) findViewById(R.id.afun_safe_mode_button);
        this.highSpeedButton = (ToggleButton) findViewById(R.id.afun_high_speed_mode_button);
        this.advancedButton = (ToggleButton) findViewById(R.id.afun_advanced_mode_button);
        this.timerPlusButton = (Button) findViewById(R.id.afun_timer_plus_button);
        this.timerMinusButton = (Button) findViewById(R.id.afun_timer_minus_button);
        this.packetsPerTimerPlusButton = (Button) findViewById(R.id.afun_packets_per_timer_plus_button);
        this.packetsPerTimerMinusButton = (Button) findViewById(R.id.afun_packets_per_timer_minus_button);
        this.useNotificationsSwitch = (Switch) findViewById(R.id.afun_use_notifications_switch);
        this.useNotificationsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!FWUpdateNGActivity.this.advancedButton.isChecked() || z) {
                    FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(true);
                    FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(true);
                    TextView textView = FWUpdateNGActivity.this.packetsPerTimer;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(FWUpdateNGActivity.this.packetsPerInterval);
                    textView.setText(sb.toString());
                    return;
                }
                FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                FWUpdateNGActivity.this.packetsPerTimer.setText("1");
            }
        });
        this.fwProgressBar = (ProgressBar) findViewById(R.id.afun_progress_bar);
        this.fwProgressBar.setProgress(0);
        this.fwProgressBar.setMax(100);
        this.safeModeButton.setChecked(true);
        this.safeModeButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    FWUpdateNGActivity.this.highSpeedButton.setChecked(false);
                    FWUpdateNGActivity.this.advancedButton.setChecked(false);
                    FWUpdateNGActivity.this.timerPlusButton.setEnabled(false);
                    FWUpdateNGActivity.this.timerMinusButton.setEnabled(false);
                    FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                    FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                    FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                    fWUpdateNGActivity.packetsPerInterval = 1;
                    fWUpdateNGActivity.packetsPerTimer.setText("1");
                    FWUpdateNGActivity.this.useNotificationsSwitch.setChecked(true);
                    FWUpdateNGActivity.this.useNotificationsSwitch.setEnabled(false);
                    FWUpdateNGActivity fWUpdateNGActivity2 = FWUpdateNGActivity.this;
                    fWUpdateNGActivity2.timerInterval = 1;
                    fWUpdateNGActivity2.timerTime.setText("Notifications");
                }
            }
        });
        this.highSpeedButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    FWUpdateNGActivity.this.safeModeButton.setChecked(false);
                    FWUpdateNGActivity.this.advancedButton.setChecked(false);
                    FWUpdateNGActivity.this.timerPlusButton.setEnabled(false);
                    FWUpdateNGActivity.this.timerMinusButton.setEnabled(false);
                    FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                    FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                    FWUpdateNGActivity.this.useNotificationsSwitch.setChecked(true);
                    FWUpdateNGActivity.this.useNotificationsSwitch.setEnabled(false);
                    FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                    fWUpdateNGActivity.timerInterval = 8;
                    fWUpdateNGActivity.timerTime.setText("8 ms");
                    FWUpdateNGActivity.this.packetsPerTimer.setText("4");
                    FWUpdateNGActivity.this.packetsPerInterval = 4;
                }
            }
        });
        this.advancedButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    FWUpdateNGActivity.this.safeModeButton.setChecked(false);
                    FWUpdateNGActivity.this.highSpeedButton.setChecked(false);
                    FWUpdateNGActivity.this.timerPlusButton.setEnabled(true);
                    FWUpdateNGActivity.this.timerMinusButton.setEnabled(true);
                    FWUpdateNGActivity.this.useNotificationsSwitch.setEnabled(true);
                    if (FWUpdateNGActivity.this.useNotificationsSwitch.isChecked()) {
                        FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(true);
                        FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(true);
                        FWUpdateNGActivity.this.packetsPerTimer.setText("4");
                        FWUpdateNGActivity.this.packetsPerInterval = 4;
                    } else {
                        FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                        FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                        FWUpdateNGActivity.this.packetsPerTimer.setText("1");
                        FWUpdateNGActivity.this.packetsPerInterval = 1;
                    }
                    FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                    fWUpdateNGActivity.timerInterval = 8;
                    fWUpdateNGActivity.timerTime.setText("8 ms");
                }
            }
        });
        this.timerTime = (TextView) findViewById(R.id.afun_timer_delay_label);
        this.packetsPerTimer = (TextView) findViewById(R.id.afun_packets_per_timer);
        this.timerInterval = 8;
        this.timerTime.setText("8 ms");
        this.packetsPerTimer.setText("8");
        this.packetsPerInterval = 8;
        this.timerMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                fWUpdateNGActivity.timerInterval = (int) (((float) fWUpdateNGActivity.timerInterval) - 1.0f);
                if (FWUpdateNGActivity.this.timerInterval < 1) {
                    FWUpdateNGActivity.this.timerInterval = 1;
                }
                TextView textView = FWUpdateNGActivity.this.timerTime;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(FWUpdateNGActivity.this.timerInterval);
                sb.append(" ms");
                textView.setText(sb.toString());
            }
        });
        this.timerPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FWUpdateNGActivity.this.timerInterval++;
                if (FWUpdateNGActivity.this.timerInterval < 1) {
                    FWUpdateNGActivity.this.timerInterval = 1;
                }
                TextView textView = FWUpdateNGActivity.this.timerTime;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(FWUpdateNGActivity.this.timerInterval);
                sb.append(" ms");
                textView.setText(sb.toString());
            }
        });
        this.packetsPerTimerMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FWUpdateNGActivity.this.packetsPerInterval--;
                if (FWUpdateNGActivity.this.packetsPerInterval < 1) {
                    FWUpdateNGActivity.this.packetsPerInterval = 1;
                }
                TextView textView = FWUpdateNGActivity.this.packetsPerTimer;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(FWUpdateNGActivity.this.packetsPerInterval);
                textView.setText(sb.toString());
            }
        });
        this.packetsPerTimerPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FWUpdateNGActivity.this.packetsPerInterval++;
                TextView textView = FWUpdateNGActivity.this.packetsPerTimer;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(FWUpdateNGActivity.this.packetsPerInterval);
                textView.setText(sb.toString());
            }
        });
        moveDeviceToOAD((BluetoothDevice) getIntent().getParcelableExtra(DeviceActivity.EXTRA_DEVICE));
        try {
            this.fwEntries = new FWUpdateBINFileEntriesParser().parse(getAssets().open("firmware_list.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selFwButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FWUpdateNGSelectorDialogFragment.newInstance(FWUpdateNGActivity.this.fwEntries, FWUpdateNGActivity.this.currentVersion, FWUpdateNGActivity.this.mBtDevice.getName()).show(FWUpdateNGActivity.this.mThis.getFragmentManager(), "FirmwareFileSelector");
            }
        });
    }

    public void onPause() {
        super.onPause();
        this.state = fwUpdateProcessStages.EXIT;
        unregisterReceiver(this.reciever);
        BluetoothGatt bluetoothGatt = this.mBtGatt;
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
        String str = TAG;
        Log.d(str, "Stopping scanner");
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(this.sCB);
        Log.d(str, "Scanner Stopped");
        new PreferenceWR(this.mBtGatt.getDevice().getAddress(), this.mThis).setBooleanPreference("refresh", true);
        this.timeoutStop = true;
    }

    public void onResume() {
        super.onResume();
        new timeoutThread().start();
        this.reciever = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (FWUpdateNGSelectorDialogFragment.ACTION_FW_WAS_SELECTED.equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra(FWUpdateNGSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX, -1);
                    if (intExtra < FWUpdateNGActivity.this.fwEntries.size()) {
                        final FWUpdateTIFirmwareEntry fWUpdateTIFirmwareEntry = (FWUpdateTIFirmwareEntry) FWUpdateNGActivity.this.fwEntries.get(intExtra);
                        if (!fWUpdateTIFirmwareEntry.compatible) {
                            Builder builder = new Builder(context);
                            String str = "OK";
                            builder.setPositiveButton(str, null);
                            builder.setTitle("Warning !");
                            builder.setMessage("Selected image is not compatible with your current firmware, you should not update with this image !");
                            builder.setPositiveButton(str, new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FWUpdateNGActivity.this.startOADWithFile(fWUpdateTIFirmwareEntry.Filename);
                                }
                            });
                            builder.setNegativeButton("Abort", new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder.create().show();
                        } else if (((int) (fWUpdateTIFirmwareEntry.Version * 100.0f)) < 130 || !fWUpdateTIFirmwareEntry.BoardType.equals("CC2650 SensorTag") || ((double) FWUpdateNGActivity.this.currentVersion) > 1.25d) {
                            FWUpdateNGActivity.this.startOADWithFile(fWUpdateTIFirmwareEntry.Filename);
                        } else {
                            FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    FWUpdateNGActivity.this.safeModeButton.setChecked(true);
                                    FWUpdateNGActivity.this.safeModeButton.callOnClick();
                                    new Builder(FWUpdateNGActivity.this.mThis) {
                                        {
                                            setTitle("3 Stage upgrade");
                                            setMessage("When upgrading from below v1.30 CC2650 SensorTag the upgrade process has three stages, this is an automatic process that starts with this first step");
                                            setPositiveButton("Upgrade", new OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_1;
                                                    FWUpdateNGActivity.this.mThis.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                                }
                                            });
                                            setNegativeButton("Cancel", null);
                                        }
                                    }.create().show();
                                }
                            });
                        }
                    }
                }
            }
        };
        registerReceiver(this.reciever, new IntentFilter(FWUpdateNGSelectorDialogFragment.ACTION_FW_WAS_SELECTED));
    }

    public void startOADWithFile(String str) {
        oadMode oadmode;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.fwFile = new FWUpdateNGBINFileHandler(str, true, this.mThis);
        this.mFWUpdateNGProgrammingHandler = new FWUpdateNGProgrammingHandler(this.fwFile, this.mBtGatt, this.myOADCB);
        if (this.highSpeedButton.isChecked()) {
            oadmode = oadMode.HIGH_SPEED;
        } else if (this.advancedButton.isChecked()) {
            oadmode = oadMode.ADVANCED;
        } else {
            oadmode = oadMode.SAFE_MODE;
        }
        this.mFWUpdateNGProgrammingHandler.setupOAD(oadmode, this.currentConParams[0], this.useNotificationsSwitch.isChecked(), this.timerInterval, this.packetsPerInterval);
        this.mFWUpdateNGProgrammingHandler.startOADProcess();
        this.state = fwUpdateProcessStages.OADINPROGRESS;
        this.statusMessageDialogRunnable = null;
    }

    public void moveDeviceToOAD(BluetoothDevice bluetoothDevice) {
        this.mBtDevice = bluetoothDevice;
        this.mBtGatt = this.mBtDevice.connectGatt(this.mThis, false, this.bTCB);
    }

    public boolean getIdealConParams(float f) {
        if (f < 30.0f) {
            this.packetsPerInterval = 4;
            this.packetsPerTimer.setText("4");
            this.timerInterval = 30;
            this.timerTime.setText("30 ms");
        }
        String str = "SELECT FW FILE";
        if (f < 32.0f && f > 25.0f) {
            this.selFwButton.setEnabled(true);
            this.selFwButton.setText(str);
            this.safeModeButton.setEnabled(true);
            this.highSpeedButton.setEnabled(true);
            this.advancedButton.setEnabled(true);
            this.useNotificationsSwitch.setEnabled(true);
            if (this.state == fwUpdateProcessStages.wfCONINTERVAL) {
                if (bleUtility.boardTypeFromBluetoothDevice(this.mBtDevice) == boardType.CC2650FlashProgrammer && this.sensorTagMultiStage == fwUpdateMultistageStage.SINGLE_STAGE) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d(FWUpdateNGActivity.TAG, "Detected CC2650 SensorTag stuck in multi-stage Stage II, display dialog and continue");
                            new Builder(FWUpdateNGActivity.this.mThis) {
                                {
                                    setTitle("SensorTag mid-upgrade found !");
                                    setMessage("The currently connected SensorTag has been aborted mid upgrade,\n continue multistage upgrade from stage 2 ?");
                                    setPositiveButton("OK", new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d(FWUpdateNGActivity.TAG, "Starting multi-stage Stage 2");
                                            FWUpdateNGActivity.this.safeModeButton.setChecked(true);
                                            FWUpdateNGActivity.this.safeModeButton.callOnClick();
                                            FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_2;
                                            FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfFILENAME;
                                            FWUpdateNGActivity.this.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                        }
                                    });
                                    setNegativeButton("Cancel", null);
                                }
                            }.create().show();
                            FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfFILENAME;
                        }
                    });
                } else if (this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_1) {
                    if (((int) (this.currentVersion * 100.0f)) == 121) {
                        this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_2;
                        startMultiStageProgrammingOnStage(this.sensorTagMultiStage);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Builder(FWUpdateNGActivity.this.mThis) {
                                    {
                                        setTitle("Upgrade stage 1 failed");
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Version is still ");
                                        sb.append(FWUpdateNGActivity.this.currentVersion);
                                        sb.append(" , this means the upgrade failed, will try safe mode upgrade instead");
                                        setMessage(sb.toString());
                                        setPositiveButton("Try again", new OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_1;
                                                FWUpdateNGActivity.this.highSpeedButton.setChecked(false);
                                                FWUpdateNGActivity.this.advancedButton.setChecked(false);
                                                FWUpdateNGActivity.this.timerPlusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.timerMinusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.safeModeButton.setChecked(true);
                                                FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.packetsPerInterval = 1;
                                                FWUpdateNGActivity.this.packetsPerTimer.setText("1");
                                                FWUpdateNGActivity.this.useNotificationsSwitch.setChecked(true);
                                                FWUpdateNGActivity.this.useNotificationsSwitch.setEnabled(false);
                                                FWUpdateNGActivity.this.timerInterval = 1;
                                                FWUpdateNGActivity.this.timerTime.setText("Notifications");
                                                FWUpdateNGActivity.this.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                            }
                                        });
                                    }
                                }.create().show();
                            }
                        });
                    }
                } else if (this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_3 && ((int) (this.currentVersion * 100.0f)) >= 130) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new Builder(FWUpdateNGActivity.this.mThis) {
                                {
                                    setTitle("Stage 3 complete !");
                                    setMessage("Stage 3 completed successfull ! Upgrade complete");
                                    setPositiveButton("OK", new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FWUpdateNGActivity.this.mThis.finish();
                                        }
                                    });
                                }
                            }.create().show();
                        }
                    });
                }
            }
        } else if (this.triedConUpdate) {
            this.selFwButton.setEnabled(true);
            this.selFwButton.setText(str);
            this.safeModeButton.setEnabled(true);
            this.highSpeedButton.setEnabled(true);
            this.advancedButton.setEnabled(true);
            this.useNotificationsSwitch.setEnabled(true);
            if (this.state == fwUpdateProcessStages.wfCONINTERVAL) {
                if (bleUtility.boardTypeFromBluetoothDevice(this.mBtDevice) == boardType.CC2650FlashProgrammer && this.sensorTagMultiStage == fwUpdateMultistageStage.SINGLE_STAGE) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d(FWUpdateNGActivity.TAG, "Detected CC2650 SensorTag stuck in multi-stage Stage II, display dialog and continue");
                            new Builder(FWUpdateNGActivity.this.mThis) {
                                {
                                    setTitle("SensorTag mid-upgrade found !");
                                    setMessage("The currently connected SensorTag has been aborted mid upgrade,\n continue multistage upgrade from stage 2 ?");
                                    setPositiveButton("OK", new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d(FWUpdateNGActivity.TAG, "Starting multi-stage Stage 2");
                                            FWUpdateNGActivity.this.safeModeButton.setChecked(true);
                                            FWUpdateNGActivity.this.safeModeButton.callOnClick();
                                            FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_2;
                                            FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfFILENAME;
                                            FWUpdateNGActivity.this.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                        }
                                    });
                                    setNegativeButton("Cancel", null);
                                }
                            }.create().show();
                            FWUpdateNGActivity.this.state = fwUpdateProcessStages.wfFILENAME;
                        }
                    });
                } else if (this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_1) {
                    if (((int) (this.currentVersion * 100.0f)) == 121) {
                        this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_2;
                        startMultiStageProgrammingOnStage(this.sensorTagMultiStage);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Builder(FWUpdateNGActivity.this.mThis) {
                                    {
                                        setTitle("Upgrade stage 1 failed");
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Version is still ");
                                        sb.append(FWUpdateNGActivity.this.currentVersion);
                                        sb.append(" , this means the upgrade failed, will try safe mode upgrade instead");
                                        setMessage(sb.toString());
                                        setPositiveButton("Try again", new OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                FWUpdateNGActivity.this.sensorTagMultiStage = fwUpdateMultistageStage.MULTISTAGE_STAGE_1;
                                                FWUpdateNGActivity.this.highSpeedButton.setChecked(false);
                                                FWUpdateNGActivity.this.advancedButton.setChecked(false);
                                                FWUpdateNGActivity.this.timerPlusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.timerMinusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.safeModeButton.setChecked(true);
                                                FWUpdateNGActivity.this.packetsPerTimerMinusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.packetsPerTimerPlusButton.setEnabled(false);
                                                FWUpdateNGActivity.this.packetsPerInterval = 1;
                                                FWUpdateNGActivity.this.packetsPerTimer.setText("1");
                                                FWUpdateNGActivity.this.useNotificationsSwitch.setChecked(true);
                                                FWUpdateNGActivity.this.useNotificationsSwitch.setEnabled(false);
                                                FWUpdateNGActivity.this.timerInterval = 1;
                                                FWUpdateNGActivity.this.timerTime.setText("Notifications");
                                                FWUpdateNGActivity.this.startMultiStageProgrammingOnStage(FWUpdateNGActivity.this.sensorTagMultiStage);
                                            }
                                        });
                                    }
                                }.create().show();
                            }
                        });
                    }
                } else if (this.sensorTagMultiStage == fwUpdateMultistageStage.MULTISTAGE_STAGE_3 && ((int) (this.currentVersion * 100.0f)) >= 130) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new Builder(FWUpdateNGActivity.this.mThis) {
                                {
                                    setTitle("Stage 3 complete !");
                                    setMessage("Stage 3 completed successfull ! Upgrade complete");
                                    setPositiveButton("OK", new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                }
                            }.create().show();
                        }
                    });
                }
            }
        } else {
            this.triedConUpdate = true;
            byte b = (byte) 24;
            byte b2 = (byte) 0;
            final byte[] bArr = {b, b2, b, b2, 0, 0, (byte) 96, (byte) 0};
            new Thread(new Runnable() {
                public void run() {
                    BluetoothGattService service = FWUpdateNGActivity.this.mBtGatt.getService(UUID.fromString("f000ccc0-0451-4000-b000-000000000000"));
                    if (service != null) {
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("f000ccc2-0451-4000-b000-000000000000"));
                        if (characteristic != null) {
                            FWUpdateNGActivity.this.mBtBlocked = true;
                            characteristic.setValue(bArr);
                            FWUpdateNGActivity.this.mBtGatt.writeCharacteristic(characteristic);
                        }
                    }
                }
            }).start();
        }
        return true;
    }

    public boolean readCurConParams() {
        this.mBtBlocked = true;
        new Thread(new Runnable() {
            public void run() {
                BluetoothGattService service = FWUpdateNGActivity.this.mBtGatt.getService(UUID.fromString("f000ccc0-0451-4000-b000-000000000000"));
                if (service != null) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("f000ccc1-0451-4000-b000-000000000000"));
                    if (characteristic != null) {
                        FWUpdateNGActivity.this.mBtGatt.readCharacteristic(characteristic);
                    }
                }
            }
        }).start();
        return true;
    }

    public boolean readVersionFromDeviceInfo() {
        this.mBtBlocked = true;
        new Thread(new Runnable() {
            public void run() {
                BluetoothGattService service = FWUpdateNGActivity.this.mBtGatt.getService(UUID.fromString(DeviceInformationServiceProfile.dISService_UUID));
                if (service != null) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(DeviceInformationServiceProfile.dISFirmwareREV_UUID));
                    if (characteristic != null) {
                        FWUpdateNGActivity.this.mBtGatt.readCharacteristic(characteristic);
                    }
                }
            }
        }).start();
        return true;
    }

    public String printState(fwUpdateProcessStages fwupdateprocessstages) {
        switch (fwupdateprocessstages) {
            case DISCONNECTED:
                return "Disconnected";
            case CONNECTED:
                return "Connected";
            case wfBLUETOOTHRESET:
                return "Waiting for Bluetooth reset";
            case wfBLUETOOTHRESETCOMPLETE:
                return "Waiting for Bluetooth reset complete";
            case wfCONINTERVAL:
                return "Waiting for Connection Interval Setting";
            case wfDisconnect:
                return "Waiting for Disconnect";
            case wfFILENAME:
                return "Waiting for filename of firmware file";
            case wfFWREVISION:
                return "Waiting for RX of Firmware Revision from DIS";
            case OADFINISHED:
                return "OAD Finished";
            case OADINPROGRESS:
                return "OAD In Progress";
            case ERROR:
                return "Error State";
            default:
                return "";
        }
    }

    public void startMultiStageProgrammingOnStage(fwUpdateMultistageStage fwupdatemultistagestage) {
        oadMode oadmode;
        int i = C073326.f39x7ebe413[fwupdatemultistagestage.ordinal()];
        if (i == 1) {
            this.fwFile = new FWUpdateNGBINFileHandler("CC2650SensorTag_FlashProgrammer_v1.21.bin", true, this.mThis);
        } else if (i == 2) {
            this.fwFile = new FWUpdateNGBINFileHandler("CC2650SensorTag_RamFlashLoader_v1.21.bin", true, this.mThis);
        } else if (i == 3) {
            this.fwFile = new FWUpdateNGBINFileHandler("CC2650SensorTag_BLE_All_v1.40.bin", true, this.mThis);
        }
        this.mFWUpdateNGProgrammingHandler = new FWUpdateNGProgrammingHandler(this.fwFile, this.mBtGatt, this.myOADCB);
        if (this.highSpeedButton.isChecked()) {
            oadmode = oadMode.HIGH_SPEED;
        } else if (this.advancedButton.isChecked()) {
            oadmode = oadMode.ADVANCED;
        } else {
            oadmode = oadMode.SAFE_MODE;
        }
        this.mFWUpdateNGProgrammingHandler.setupOAD(oadmode, this.currentConParams[0], this.useNotificationsSwitch.isChecked(), this.timerInterval, this.packetsPerInterval);
        this.mFWUpdateNGProgrammingHandler.startOADProcess();
        this.state = fwUpdateProcessStages.OADINPROGRESS;
    }

    public void startRestartBluetooth(Context context) {
        BluetoothGatt bluetoothGatt = this.mBtGatt;
        String str = TAG;
        if (bluetoothGatt != null) {
            Log.d(str, "Starting restart now ...");
            this.mBtGatt.disconnect();
            return;
        }
        Log.d(str, "mBtGatt not valid !");
        restartBluetooth(this.mThis);
    }

    public void restartBluetooth(Context context) {
        runOnUiThread(new Runnable() {
            public void run() {
                FWUpdateNGActivity fWUpdateNGActivity = FWUpdateNGActivity.this;
                fWUpdateNGActivity.resetBTDialog = new ProgressDialog(fWUpdateNGActivity.mThis);
                FWUpdateNGActivity.this.resetBTDialog.setTitle("Restarting Bluetooth");
                FWUpdateNGActivity.this.resetBTDialog.setMax(100);
                FWUpdateNGActivity.this.resetBTDialog.setProgressStyle(1);
                FWUpdateNGActivity.this.resetBTDialog.show();
            }
        });
        final BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (defaultAdapter.isEnabled()) {
                        defaultAdapter.cancelDiscovery();
                        defaultAdapter.disable();
                        for (final int i = 0; i < 10; i++) {
                            Thread.sleep(600, 0);
                            FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    FWUpdateNGActivity.this.resetBTDialog.setProgress(i * 5);
                                }
                            });
                        }
                    }
                    defaultAdapter.enable();
                    for (final int i2 = 0; i2 < 10; i2++) {
                        Thread.sleep(400, 0);
                        FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                FWUpdateNGActivity.this.resetBTDialog.setProgress((i2 * 5) + 50);
                            }
                        });
                    }
                    FWUpdateNGActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                    defaultAdapter.getBluetoothLeScanner().startScan(FWUpdateNGActivity.this.sCB);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FWUpdateNGActivity.this.finishedReset = true;
            }
        }).start();
        while (!this.finishedReset) {
            Log.d(TAG, "Waiting for Bluetooth Reset");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                FWUpdateNGActivity.this.resetBTDialog.dismiss();
            }
        });
        this.mBtGatt = this.mBtDevice.connectGatt(this.mThis, false, this.bTCB);
    }
}
