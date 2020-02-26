package com.p004ti.ble.dmm;

import org.apache.http.HttpStatus;

/* renamed from: com.ti.ble.dmm.TIDMMDefines */
public class TIDMMDefines {
    public static int TI_DMM_FIFTEEN_FOUR_PROVISIONING_PAN_CONNECT = 170;
    public static int TI_DMM_FIFTEEN_FOUR_PROVISIONING_PAN_DISCONNECT = 221;
    public static int TI_DMM_FIFTEEN_FOUR_PROVISIONING_PAN_NETWORK_CLOSE = HttpStatus.SC_NO_CONTENT;
    public static int TI_DMM_FIFTEEN_FOUR_PROVISIONING_PAN_NETWORK_OPEN = 85;
    public static String[] TI_DMM_PROVISIONING_STATE_STRINGS = {"Initialized on hold", "Initialized no connection", "Discovering ...", "Joining a PAN", "Rejoined scanning for end devices", "Orphaned from network", "Authenticated End Device", "Authenticated Router", "Coordinator Starting", "Coordinator Started", "Lost information about parent", "Sending Keep Alive to parent", "Waiting before trying to rejoin", "Unkonown State"};
    public static int TI_DMM_PROVISIONING_STATE_STRINGS_COUNT = 14;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_END_DEVICE_AUTH = 6;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_FORMATION_DONE = 9;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_HOLD = 0;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_INIT = 1;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_JOINED_SEC_CURR_CHANNEL = 4;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_JOINING = 3;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_KA_SENT = 11;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_NETWORK_DISC = 2;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_NETWORK_FORMATION_STARTING = 8;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_ORPHAN = 10;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_ORPHANED_FROM_NETWORK = 5;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_REJOIN_BACKOFF = 12;
    public static int TI_DMM_ZIGBEE_PROVISIONING_STATE_ROUTER_AUTH = 7;
}
