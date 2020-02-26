package com.p004ti.wifi.utils;

/* renamed from: com.ti.wifi.utils.CFG_Result_Enum */
public enum CFG_Result_Enum {
    Success,
    Failure,
    Unknown_Token,
    Not_Started,
    Wrong_Password,
    Ap_Not_Found,
    Ip_Add_Fail,
    Time_Out;
    
    public static String AP_NOT_FOUND_STRING;
    public static String FAILURE_STRING;
    public static String IP_ADD_FAIL_STRING;
    public static String NOT_STARTED_STRING;
    public static String SUCCESS_STRING;
    public static String TIME_OUT_STRING;
    public static String UNKNOWN_STRING;
    public static String WRONG_PASSWORD_STRING;

    static {
        String str;
        String str2;
        SUCCESS_STRING = "Provisioning Successful";
        FAILURE_STRING = "Please try to restart the device and the configuration application and try again";
        UNKNOWN_STRING = str;
        NOT_STARTED_STRING = "The provisioning sequence has not started yet. Device is waiting for configuration to be sent";
        WRONG_PASSWORD_STRING = "Connection to selected AP has failed. Please try one of the following:\nCheck your password is entered correctly and try again\nVerify your AP is working\nRestart your AP.";
        AP_NOT_FOUND_STRING = "Could not find the selected WiFi network; it is either turned off or out of range. When the WiFi network is available please restart the device in order to connect.";
        IP_ADD_FAIL_STRING = "Failed to acquire IP address from the selected AP.\nPlease try one of the following:\nConnect a new device to the AP to verify it is functional\n,Or restart the AP.";
        TIME_OUT_STRING = str2;
    }
}
