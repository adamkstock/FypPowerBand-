package com.p004ti.ble.audio;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.p000v4.content.LocalBroadcastManager;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;
import java.util.ArrayList;

/* renamed from: com.ti.ble.audio.AudioActivity */
public class AudioActivity extends AppCompatActivity {
    public static final String BT_ADDRESS_EXTRA = "AudioActivity.BTADDRESS_EXTRA";
    private BroadcastReceiver AdvancedRemoteBLEAudioMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("device");
            BluetoothDevice bluetoothDevice2 = (BluetoothDevice) intent.getParcelableExtra("paired");
            final String stringExtra = intent.getStringExtra("statusText");
            String stringExtra2 = intent.getStringExtra("fileInfoText");
            intent.getStringExtra("searchTerm");
            byte byteExtra = intent.getByteExtra("conInterval", -1);
            int intExtra = intent.getIntExtra("peakVU", 0);
            String str = "AudioActivity";
            if (stringExtra2 != null) {
                Log.d(str, stringExtra2);
            }
            String str2 = "activity";
            if (stringExtra != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Got status :");
                sb.append(stringExtra);
                Log.d(str, sb.toString());
                AudioActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        AudioActivity.this.conStatus.setText(stringExtra);
                    }
                });
                if (stringExtra.equals("Enabling audio")) {
                    Log.d(str2, "Enabling notification");
                } else {
                    String str3 = "Changing notification";
                    if (stringExtra.equals("Disconnected")) {
                        Log.d(str2, str3);
                    } else if (stringExtra.equals("Connected")) {
                        Log.d(str2, str3);
                    } else if (stringExtra.equals("Failed to set connection interval")) {
                        Builder builder = new Builder(AudioActivity.this.mThis);
                        builder.setPositiveButton("OK", new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.setTitle("Unable to set correct connection interval");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Tried to set correct connection interval (10ms), but got (");
                        sb2.append(AudioActivity.this.conInterval.getText());
                        sb2.append(") Sound transfer will be degraded...");
                        builder.setMessage(sb2.toString());
                        builder.create().show();
                    }
                }
            } else {
                String str4 = " name: ";
                if (bluetoothDevice != null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Found device :");
                    sb3.append(bluetoothDevice.getAddress());
                    sb3.append(str4);
                    sb3.append(bluetoothDevice.getName());
                    Log.d(str2, sb3.toString());
                } else if (bluetoothDevice2 != null) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Found paired :");
                    sb4.append(bluetoothDevice2.getAddress());
                    sb4.append(str4);
                    sb4.append(bluetoothDevice2.getName());
                    Log.d(str2, sb4.toString());
                }
            }
            if (intExtra != 0) {
                AudioActivity.this.calcAndDisplayVUFromMaxSampleValue(intExtra);
            }
            if (byteExtra != -1) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Received Connection interval ");
                double d = ((double) ((float) byteExtra)) * 1.25d;
                sb5.append(d);
                String str5 = " ms";
                sb5.append(str5);
                Log.d(str, sb5.toString());
                TextView textView = AudioActivity.this.conInterval;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(d);
                sb6.append(str5);
                textView.setText(sb6.toString());
            }
        }
    };
    AdvancedRemoteBLEAudioService aService;
    Intent audioServiceIntent;
    TextView conInterval;
    TextView conStatus;
    ImageView icon;
    ArrayList<ImageView> ledArray;
    String mBtAddr;
    BroadcastReceiver mReceiver;
    AudioActivity mThis = this;

    public void onCreate(Bundle bundle) {
        this.mBtAddr = getIntent().getStringExtra(BT_ADDRESS_EXTRA);
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_audio);
        this.conStatus = (TextView) findViewById(R.id.aa_con_status);
        TextView textView = this.conStatus;
        if (textView != null) {
            textView.setText("Starting Service ...");
        }
        this.ledArray = new ArrayList<>();
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_0));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_1));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_2));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_3));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_4));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_5));
        this.ledArray.add((ImageView) findViewById(R.id.aa_led_6));
        this.conInterval = (TextView) findViewById(R.id.aa_con_interval);
        this.icon = (ImageView) findViewById(R.id.aa_icon);
        this.icon.setImageResource(R.mipmap.cc2650_rc_audio);
    }

    public void onResume() {
        super.onResume();
        this.mReceiver = this.AdvancedRemoteBLEAudioMessageReceiver;
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter("ARCBLEAudio-From-Service-Events"));
        this.audioServiceIntent = new Intent(this, AdvancedRemoteBLEAudioService.class);
        startService(this.audioServiceIntent);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("Connecting to ");
                sb.append(AudioActivity.this.mBtAddr);
                Log.d("AudioActivity", sb.toString());
                Intent intent = new Intent("ARCBLEAudio-To-Service-Events");
                intent.putExtra("btaddr", AudioActivity.this.mBtAddr);
                LocalBroadcastManager.getInstance(AudioActivity.this.mThis).sendBroadcast(intent);
            }
        }, 1000);
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
    }

    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("ARCBLEAudio-To-Service-Events");
        intent.putExtra("message", 3);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        stopService(this.audioServiceIntent);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
    }

    public void calcAndDisplayVUFromMaxSampleValue(int i) {
        float log10 = ((float) Math.log10((double) (((float) i) / 32768.0f))) * 20.0f;
        int i2 = log10 >= 0.0f ? 7 : log10 >= -6.0f ? 6 : log10 >= -12.0f ? 5 : log10 >= -18.0f ? 4 : log10 >= -24.0f ? 3 : log10 >= -30.0f ? 2 : log10 >= -36.0f ? 1 : 0;
        for (int i3 = 0; i3 < i2; i3++) {
            ((ImageView) this.ledArray.get(i3)).setImageResource(R.mipmap.led_on);
        }
        while (i2 < 7) {
            ((ImageView) this.ledArray.get(i2)).setImageResource(R.mipmap.led_off);
            i2++;
        }
    }
}
