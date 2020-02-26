package com.p004ti.device_selector;

import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.licenseActivity */
public class licenseActivity extends AppCompatActivity {
    WebView licenseText;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_license);
        this.licenseText = (WebView) findViewById(R.id.al_license_text);
        this.licenseText.loadUrl("file:///android_asset/License.htm");
    }
}
