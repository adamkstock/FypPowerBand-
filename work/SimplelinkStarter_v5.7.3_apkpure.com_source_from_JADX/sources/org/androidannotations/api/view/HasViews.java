package org.androidannotations.api.view;

import android.view.View;

public interface HasViews {
    <T extends View> T internalFindViewById(int i);
}
