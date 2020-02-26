package com.p004ti.device_selector;

/* renamed from: com.ti.device_selector.ServiceExplorerCharacteristicAction */
public interface ServiceExplorerCharacteristicAction {
    void shallReadCharacteristic();

    void shallSetIndication();

    void shallSetNotification();

    void shallWriteCharacteristic(byte[] bArr);
}
