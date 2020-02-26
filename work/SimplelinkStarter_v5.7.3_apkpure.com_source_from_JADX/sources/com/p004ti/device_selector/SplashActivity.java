package com.p004ti.device_selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;

/* renamed from: com.ti.device_selector.SplashActivity */
public class SplashActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startActivity(new Intent(this, TopLevel_.class));
        finish();
    }
}
