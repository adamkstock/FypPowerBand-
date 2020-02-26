package com.p004ti.device_selector;

import android.os.Bundle;
import android.view.MenuItem;

/* renamed from: com.ti.device_selector.PreferencesActivity */
public class PreferencesActivity extends AppCompatPreferenceActivity {
    public boolean isValidFragment(String str) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onBackPressed() {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
