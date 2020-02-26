package com.p004ti.wifi.utils;

/* renamed from: com.ti.wifi.utils.DidShowMobileDataDialogSingleton */
public class DidShowMobileDataDialogSingleton {
    private static DidShowMobileDataDialogSingleton instance;
    public boolean didShow = false;

    public static DidShowMobileDataDialogSingleton getInstance() {
        if (instance == null) {
            instance = new DidShowMobileDataDialogSingleton();
        }
        return instance;
    }

    private DidShowMobileDataDialogSingleton() {
    }
}
