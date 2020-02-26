package com.p004ti.device_selector;

import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.thirdPartyLicenseActivity */
public class thirdPartyLicenseActivity extends AppCompatActivity {
    private WebView apacheLicense;
    private WebView eclipseLicense;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_third_party_licenses);
        this.eclipseLicense = (WebView) findViewById(R.id.atpl_eclipse_license);
        this.apacheLicense = (WebView) findViewById(R.id.atpl_apache_license);
        this.eclipseLicense.loadUrl("https://www.eclipse.org/legal/epl-v10.html");
        this.apacheLicense.loadUrl("http://www.apache.org/licenses/LICENSE-2.0.txt");
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }
}
