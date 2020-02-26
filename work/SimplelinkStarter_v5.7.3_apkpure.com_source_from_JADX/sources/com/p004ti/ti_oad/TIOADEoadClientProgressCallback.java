package com.p004ti.ti_oad;

import com.p004ti.ti_oad.TIOADEoadDefinitions.oadStatusEnumeration;

/* renamed from: com.ti.ti_oad.TIOADEoadClientProgressCallback */
public interface TIOADEoadClientProgressCallback {
    void oadProgressUpdate(float f, int i);

    void oadStatusUpdate(oadStatusEnumeration oadstatusenumeration);
}
