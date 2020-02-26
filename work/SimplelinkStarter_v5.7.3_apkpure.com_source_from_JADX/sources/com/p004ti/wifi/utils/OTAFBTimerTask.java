package com.p004ti.wifi.utils;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.p004ti.device_selector.TopLevel;
import com.p004ti.wifi.wifiOTAActivity;
import com.p004ti.wifi.wifiOTAActivity.OTAStates;
import com.ti.ble.simplelinkstarter.R;
import java.util.TimerTask;

/* renamed from: com.ti.wifi.utils.OTAFBTimerTask */
public class OTAFBTimerTask extends TimerTask {
    /* access modifiers changed from: private */
    public Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public ProgressDialog otaFailProgDialog;

    public OTAFBTimerTask(Context context2, ProgressDialog progressDialog) {
        this.context = context2;
        this.otaFailProgDialog = progressDialog;
    }

    public void run() {
        this.handler.post(new Runnable() {
            public void run() {
                String str = "OTA task failed dialog - state: ";
                String str2 = "wifiOTAActivity";
                Log.i(str2, "OTA task failed dialog - start OTAFBTimerTask");
                Builder builder = new Builder(OTAFBTimerTask.this.context);
                builder.setCancelable(false).setTitle(OTAFBTimerTask.this.context.getResources().getString(R.string.connection_failed_title)).setMessage(OTAFBTimerTask.this.context.getResources().getString(R.string.connection_failed_message)).setPositiveButton(OTAFBTimerTask.this.context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(OTAFBTimerTask.this.context, TopLevel.class);
                        intent.setFlags(67108864);
                        OTAFBTimerTask.this.context.startActivity(intent);
                    }
                });
                try {
                    OTAFBTimerTask.this.otaFailProgDialog.dismiss();
                    builder.create().show();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(wifiOTAActivity.state);
                    Log.i(str2, sb.toString());
                    wifiOTAActivity.state = OTAStates.FINISHED;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(wifiOTAActivity.state);
                    Log.i(str2, sb2.toString());
                    Log.i(str2, "OTA task failed dialog - OTAFBTimerTask dialog show");
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Exception: ");
                        sb3.append(e.getMessage());
                        Log.i(str2, sb3.toString());
                        Log.i(str2, "Exception");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }
}
