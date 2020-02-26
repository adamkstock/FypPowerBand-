package com.p004ti.ble.common.oad;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.p004ti.device_selector.DeviceActivity;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.ti_oad.TIOADEoadClient;
import com.p004ti.ti_oad.TIOADEoadClientProgressCallback;
import com.p004ti.ti_oad.TIOADEoadDefinitions;
import com.p004ti.ti_oad.TIOADEoadDefinitions.oadStatusEnumeration;
import com.p004ti.ti_oad.TIOADEoadDefinitions.tiOADImageType;
import com.p004ti.ti_oad.TIOADEoadImageReader;
import com.p004ti.util.SparkLineView;
import com.ti.ble.simplelinkstarter.R;
import java.util.List;

/* renamed from: com.ti.ble.common.oad.FWUpdateEOADActivity */
public class FWUpdateEOADActivity extends AppCompatActivity {
    static final String DEVICE_ID_EXTRA = "com.ti.ble.common.oad.DEVICE_ID_EXTRA";
    private static final int READ_REQUEST_CODE = 42;
    static final String SECURITY_EXTRA = "com.ti.ble.common.oad.SECURITY_EXTRA";
    static final String SELECTED_IMAGE_FILENAME_EXTRA = "com.ti.ble.common.oad.SELECTED_IMAGE_FILENAME_EXTRA";
    private static final int SELECT_FILE_REQUEST_CODE = 11;
    static final String TAG = FWUpdateEOADActivity.class.getSimpleName();
    public Button customFileButton;
    private OnClickListener customFileButtonClicked = new OnClickListener() {
        public void onClick(View view) {
            FWUpdateEOADActivity.this.showFileSelector();
        }
    };
    public TextView eoadBlockSize;
    public TextView eoadChipType;
    public TextView eoadCurrentBlock;
    public TextView eoadCurrentSpeed;
    public FWUpdateEOADNewFileSelectorStageOne eoadFileSelector;
    public TextView eoadImageInfo;
    public TextView eoadMTU;
    public ProgressBar eoadProgress;
    public TextView eoadProgressText;
    public SparkLineView eoadSpeedHistory;
    public TextView eoadStatus;
    public TextView eoadTotalBlocks;
    Uri fileURL;
    List<FWUpdateTIFirmwareEntry> fwEntries = null;
    public long lastPacketRXTime;
    private BluetoothDevice mBtDevice;
    private BluetoothGatt mBtGatt;
    /* access modifiers changed from: private */
    public TIOADEoadClient mOADClient;
    /* access modifiers changed from: private */
    public FWUpdateEOADActivity mThis = this;
    public String oadImageFileNamePath = null;
    BroadcastReceiver reciever;
    public Button selectFileButton;
    private OnClickListener selectFileButtonClicked = new OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(FWUpdateEOADActivity.this.mThis, FWUpdateEOADNewFileSelectorStageOne.class);
            intent.putExtra(FWUpdateEOADActivity.DEVICE_ID_EXTRA, FWUpdateEOADActivity.this.mOADClient.getTIOADEoadDeviceID());
            intent.putExtra(FWUpdateEOADActivity.SECURITY_EXTRA, FWUpdateEOADActivity.this.mOADClient.detectedImageType == tiOADImageType.tiOADImageSecure);
            FWUpdateEOADActivity.this.mThis.startActivityForResult(intent, 11);
        }
    };
    private OnClickListener stopOADButtonClicked = new OnClickListener() {
        public void onClick(View view) {
            new Builder(FWUpdateEOADActivity.this.mThis).setTitle("Abort programming ?").setMessage("Are you sure you want to abort an ongoing programming ?\n").setNegativeButton("NO", null).setIcon(17301543).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FWUpdateEOADActivity.this.mOADClient.abortProgramming();
                    Intent intent = new Intent(FWUpdateEOADActivity.this.mThis, TopLevel.class);
                    intent.setFlags(67108864);
                    FWUpdateEOADActivity.this.startActivity(intent);
                }
            }).create().show();
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fw_update_eoad);
        setTitle(R.string.eoad_dialog_title);
        moveDeviceToOAD((BluetoothDevice) getIntent().getParcelableExtra(DeviceActivity.EXTRA_DEVICE));
        this.selectFileButton = (Button) findViewById(R.id.afue_select_file_button);
        this.selectFileButton.setText(R.string.eoad_select_file_button_preparing);
        this.selectFileButton.setOnClickListener(this.selectFileButtonClicked);
        this.customFileButton = (Button) findViewById(R.id.afue_select_custom_button);
        this.customFileButton.setText("Preparing...");
        this.customFileButton.setOnClickListener(this.customFileButtonClicked);
        this.eoadProgress = (ProgressBar) findViewById(R.id.afue_oad_progress_bar);
        this.eoadProgress.setIndeterminate(true);
        this.eoadStatus = (TextView) findViewById(R.id.afue_status_label);
        this.eoadCurrentBlock = (TextView) findViewById(R.id.afue_oad_current_block);
        this.eoadTotalBlocks = (TextView) findViewById(R.id.afue_oad_total_blocks);
        this.eoadMTU = (TextView) findViewById(R.id.afue_oad_mtu);
        this.eoadBlockSize = (TextView) findViewById(R.id.afue_oad_block_size);
        this.eoadProgressText = (TextView) findViewById(R.id.afue_oad_progress);
        this.eoadCurrentSpeed = (TextView) findViewById(R.id.afue_current_speed);
        this.eoadChipType = (TextView) findViewById(R.id.afue_chip_type);
        this.eoadSpeedHistory = (SparkLineView) findViewById(R.id.afue_speed_history_graph);
        SparkLineView sparkLineView = this.eoadSpeedHistory;
        sparkLineView.autoScale = true;
        sparkLineView.minVal = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        String str = "Abort Programming";
        if (i == 42 && i2 == -1 && intent != null) {
            this.fileURL = intent.getData();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("File selected: ");
            sb.append(this.fileURL.toString());
            Log.d(str2, sb.toString());
            String path = this.fileURL.getPath();
            int lastIndexOf = path.lastIndexOf(47);
            if (lastIndexOf != -1) {
                path.substring(lastIndexOf + 1);
            }
            this.mOADClient.start(this.fileURL);
            TIOADEoadImageReader tIOADEoadImageReader = new TIOADEoadImageReader(this.fileURL, (Context) this);
            this.eoadImageInfo = (TextView) findViewById(R.id.afue_image_info);
            this.eoadImageInfo.setText(tIOADEoadImageReader.imageHeader.getHeaderInfo(tIOADEoadImageReader.imageHeader));
            this.customFileButton.setOnClickListener(this.stopOADButtonClicked);
            this.customFileButton.setText(str);
            this.lastPacketRXTime = System.currentTimeMillis();
            this.selectFileButton.setEnabled(false);
        }
        if (i == 11 && i2 == -1 && intent != null) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("File URL: ");
            String str4 = SELECTED_IMAGE_FILENAME_EXTRA;
            sb2.append(intent.getStringExtra(str4));
            Log.d(str3, sb2.toString());
            this.mOADClient.start(intent.getStringExtra(str4));
            TIOADEoadImageReader tIOADEoadImageReader2 = new TIOADEoadImageReader(intent.getStringExtra(str4), (Context) this);
            this.eoadImageInfo = (TextView) findViewById(R.id.afue_image_info);
            this.eoadImageInfo.setText(tIOADEoadImageReader2.imageHeader.getHeaderInfo(tIOADEoadImageReader2.imageHeader));
            this.customFileButton.setOnClickListener(this.stopOADButtonClicked);
            this.customFileButton.setText(str);
            this.lastPacketRXTime = System.currentTimeMillis();
            this.selectFileButton.setEnabled(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public void moveDeviceToOAD(BluetoothDevice bluetoothDevice) {
        this.mBtDevice = bluetoothDevice;
        this.mOADClient = new TIOADEoadClient(this.mThis);
        this.mOADClient.initializeTIOADEoadProgrammingOnDevice(bluetoothDevice, new TIOADEoadClientProgressCallback() {
            public void oadProgressUpdate(final float f, final int i) {
                FWUpdateEOADActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateEOADActivity.this.eoadProgress.setProgress((int) f);
                        FWUpdateEOADActivity.this.eoadProgressText.setText(String.format("%.2f%%", new Object[]{Float.valueOf(f)}));
                        String str = "%d";
                        FWUpdateEOADActivity.this.eoadCurrentBlock.setText(String.format(str, new Object[]{Integer.valueOf(i)}));
                        FWUpdateEOADActivity.this.eoadTotalBlocks.setText(String.format(str, new Object[]{Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadTotalBlocks())}));
                        float tIOADEoadBlockSize = ((float) FWUpdateEOADActivity.this.mOADClient.getTIOADEoadBlockSize()) / (((float) (System.currentTimeMillis() - FWUpdateEOADActivity.this.lastPacketRXTime)) / 1000.0f);
                        FWUpdateEOADActivity.this.eoadCurrentSpeed.setText(String.format("%.0fb/s", new Object[]{Float.valueOf(tIOADEoadBlockSize)}));
                        FWUpdateEOADActivity.this.eoadSpeedHistory.addValue(tIOADEoadBlockSize);
                        FWUpdateEOADActivity.this.lastPacketRXTime = System.currentTimeMillis();
                    }
                });
            }

            public void oadStatusUpdate(final oadStatusEnumeration oadstatusenumeration) {
                String str = FWUpdateEOADActivity.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("TIOADEoad Status Update: ");
                sb.append(TIOADEoadDefinitions.oadStatusEnumerationGetDescriptiveString(oadstatusenumeration));
                Log.d(str, sb.toString());
                FWUpdateEOADActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FWUpdateEOADActivity.this.eoadStatus.setText(TIOADEoadDefinitions.oadStatusEnumerationGetDescriptiveString(oadstatusenumeration));
                        if (oadstatusenumeration == oadStatusEnumeration.tiOADClientReady) {
                            FWUpdateEOADActivity.this.eoadProgress.setIndeterminate(false);
                            FWUpdateEOADActivity.this.selectFileButton.setText(R.string.eoad_select_file_button_select_file);
                            FWUpdateEOADActivity.this.selectFileButton.setEnabled(true);
                            FWUpdateEOADActivity.this.customFileButton.setText(R.string.eoad_custom_file_button_select_file);
                            FWUpdateEOADActivity.this.customFileButton.setEnabled(true);
                            FWUpdateEOADActivity.this.eoadProgressText.setText("0.00%");
                            String str = "%d";
                            FWUpdateEOADActivity.this.eoadBlockSize.setText(String.format(str, new Object[]{Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadBlockSize())}));
                            FWUpdateEOADActivity.this.eoadMTU.setText(String.format(str, new Object[]{Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getMTU())}));
                            FWUpdateEOADActivity.this.eoadChipType.setText(TIOADEoadDefinitions.oadChipTypePrettyPrint(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadDeviceID()));
                        }
                        if (oadstatusenumeration == oadStatusEnumeration.tiOADClientCompleteFeedbackOK) {
                            FWUpdateEOADActivity.this.eoadProgress.setIndeterminate(true);
                            FWUpdateEOADActivity.this.eoadProgressText.setText("Waiting for disconnect...");
                            FWUpdateEOADActivity.this.selectFileButton.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadStatus.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadBlockSize.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadMTU.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadCurrentSpeed.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadCurrentBlock.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadTotalBlocks.setEnabled(false);
                            FWUpdateEOADActivity.this.eoadChipType.setEnabled(false);
                        }
                        String str2 = "OK";
                        if (oadstatusenumeration == oadStatusEnumeration.tiOADClientCompleteDeviceDisconnectedPositive) {
                            new Builder(FWUpdateEOADActivity.this.mThis).setTitle("Success").setMessage("OAD Programming complete !\n\nNote: If you have programmed an image with different services than the previous, remember to turn off and on bluetooth in the settings of the device to make device force an update to service cache or device will not function properly !").setIcon(17301659).setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(FWUpdateEOADActivity.this.mThis, TopLevel.class);
                                    intent.setFlags(67108864);
                                    FWUpdateEOADActivity.this.startActivity(intent);
                                }
                            }).create().show();
                        }
                        if (oadstatusenumeration == oadStatusEnumeration.tiOADClientChipIsCC1352PShowWarningAboutLayouts) {
                            new Builder(FWUpdateEOADActivity.this.mThis).setTitle("Warning !").setMessage("Chip type of this board is CC1352P\n\nThere are two different types of RF layouts for the CC1352P, be sure to select an image with the correct layout for your board !\n\nOn the TI Launchpads the marking on the left side of the board near the left button shows which version it is (either P1 or P2)\n\n").setIcon(17301659).setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                        }
                    }
                });
            }
        });
    }

    public void showFileSelector() {
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(FWUpdateEOADActivity.this.mThis);
                builder.setTitle((CharSequence) "Select Image");
                builder.setMessage((CharSequence) "Device is now ready to program, please select file in the next dialog shown after this dialog.");
                builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                        intent.setType("application/octet-stream");
                        FWUpdateEOADActivity.this.startActivityForResult(intent, 42);
                    }
                }).create().show();
            }
        });
    }
}
