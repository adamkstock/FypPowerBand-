package com.p004ti.device_selector;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ti.ble.simplelinkstarter.R;

/* renamed from: com.ti.device_selector.aboutActivity */
public class aboutActivity extends AppCompatActivity {
    private Button license;
    /* access modifiers changed from: private */
    public aboutActivity mThis;
    private Button thirdPartyLicensesButton;
    private TextView version;

    public void onCreate(Bundle bundle) {
        this.mThis = this;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about);
        this.thirdPartyLicensesButton = (Button) findViewById(R.id.aa_thirdparty_licenses);
        this.thirdPartyLicensesButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                aboutActivity.this.startActivity(new Intent(aboutActivity.this.mThis, thirdPartyLicenseActivity.class));
            }
        });
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            this.version = (TextView) findViewById(R.id.aa_version_number);
            TextView textView = this.version;
            StringBuilder sb = new StringBuilder();
            sb.append("SimpleLink Starter v");
            sb.append(packageInfo.versionName);
            sb.append(" (Build ");
            sb.append(packageInfo.versionCode);
            sb.append(")");
            textView.setText(sb.toString());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.license = (Button) findViewById(R.id.aa_license_button);
        this.license.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                aboutActivity.this.startActivity(new Intent(aboutActivity.this.mThis, licenseActivity.class));
            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }
}
