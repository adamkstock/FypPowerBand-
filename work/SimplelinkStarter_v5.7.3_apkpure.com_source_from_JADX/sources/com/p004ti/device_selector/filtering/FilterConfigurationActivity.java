package com.p004ti.device_selector.filtering;

import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.ti.ble.simplelinkstarter.R;
import java.util.Iterator;

/* renamed from: com.ti.device_selector.filtering.FilterConfigurationActivity */
public class FilterConfigurationActivity extends AppCompatActivity {
    final String TAG = FilterConfigurationActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public BTDeviceFilterGlobal globFilt;
    private Switch globalEnableSW;
    private FilterConfigurationActivity mThis;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.globFilt = new BTDeviceFilterGlobal(this);
        setContentView((int) R.layout.activity_main_filter_activity);
        this.globalEnableSW = (Switch) findViewById(R.id.trfcws_switch);
        this.globalEnableSW.setChecked(this.globFilt.globalEnable);
        this.globalEnableSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                BTDeviceFilterGlobal access$000 = FilterConfigurationActivity.this.globFilt;
                if (z) {
                    access$000.enableGlobalFilterBank();
                    Log.d(FilterConfigurationActivity.this.TAG, "Enabled global filter bank");
                    return;
                }
                access$000.disableGlobalFilterBank();
                Log.d(FilterConfigurationActivity.this.TAG, "Disabled global filter bank");
            }
        });
        Iterator it = this.globFilt.getFilterBank().iterator();
        while (it.hasNext()) {
            ((LinearLayout) findViewById(R.id.amfa_insert_view)).addView(((BTDeviceFilter) it.next()).getConfigView());
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        ((LinearLayout) findViewById(R.id.amfa_insert_view)).removeAllViews();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
