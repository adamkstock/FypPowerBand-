package com.p004ti.wifi.utils;

import android.os.Environment;
import java.io.File;

/* renamed from: com.ti.wifi.utils.Constants */
interface Constants {
    public static final int ATTEMPTS_TO_GET_CGF_RESULTS = 5;
    public static final int ATTEMPTS_TO_GET_CGF_RESULTS_DELAY = 1500;
    public static final String BASE_URL = "http://mysimplelink.net";
    public static final String CAMERA_NOT_AVAILABLE = "Camera unavailable or scan canceled by user";
    public static final int DELAY_AFTER_RESCAN_NETWORKS_BEFORE_FETCHING_NETWORKS = 20000;
    public static final int DELAY_BEFORE_ASKING_CFG_RESULTS = 1000;
    public static final int DELAY_BEFORE_FETCHING_SSIDS_FROM_DEVICE = 1000;
    public static final int DELAY_BETWEEN_CFG_RESULTS_REQUESTS = 3000;
    public static final int DELAY_BETWEEN_CONNECTING_TO_DEVICE_TO_PULLING_NETWORKS = 5000;
    public static final String DEVICE_DOES_NOT_SUPPORT_QR = "This device doesn't support this feature";
    public static final String DEVICE_LIST_CFG_CONFIRMATION_FAILED = "Confirmation failed";
    public static final String DEVICE_LIST_FAILED_ADDING_PROFILE = "Failed adding the profile";
    public static final String DEVICE_LIST_FAILED_CONFIRMATION_VIA_DEVICE = "Failed confirmation via device";
    public static final String DEVICE_LIST_FAILED_CONFIRMATION_VIA_WIFI = "Failed confirmation via wifi";
    public static final String DEVICE_LIST_FAILED_TO_GET_RESULTS = "Application cannot configure the selected device. Either you have selected a non-SimpleLink device or  you have selected a SimpleLink device which supports only legacy provisioning sequence. Please choose another SimpleLink device";
    public static final String DEVICE_LIST_FAILED_TO_RESCAN = "Failed to perform rescan on device";
    public static final String DEVICE_LIST_MUST_SUPPLY_PASSWORD = "You must supply password for this network";
    public static final String DEVICE_LIST_MUST_SUPPLY_SSID = "You must choose a legit network";
    public static final String DEVICE_PREFIX = "mysimplelink";
    public static final String DEVICE_UNKNOWN_TOKEN_STRING = "Unknown Token";
    public static final int DEVICE_WAITING_TIME_BEFORE_STARTING_MDNS_SCAN = 3000;
    public static final String LOG_PATH;
    public static final String MDNS_SCAN_MESSAGE = "Searching for devices";
    public static final int PERIODIC_SCAN_TIME = 25000;
    public static final String PROVISIONING_NOT_READY = "Not ready to start";
    public static final String QR_DEVICE_NOT_IN_RANGE_BODY = "Your mobile can't see this device";
    public static final String QR_DEVICE_NOT_IN_RANGE_TITLE = "Sorry";
    public static final String QR_NOT_VALID = "The QR code is not valid";
    public static final String QUESTION_CHOOSE_DEVICE = "More than one device was detected, select the device you want to configure";
    public static final String QUESTION_CHOOSE_ROUTER = "Please choose the network that the device will connect to";
    public static final String QUESTION_CONFIGURATION_INFO = "QR button will activate the camera and capture QR code located on the device/package. The QR code contains unique information about the device, which will be used to identify the device during the provisioning process.";
    public static final String QUESTION_CONFIGURATION_KEY = "Enter the password used for secure transmission of network information to the device";
    public static final String QUESTION_DEVICE_NAME = "Set the \"Device name\" to a friendly name, for example - Kitchen Temp Sensor. If not set, device name shall be factory default name.";
    public static final String QUESTION_DEVICE_PASSWORD = "Set the device's password, used for connecting to devices working in secured AP mode";
    public static final String QUESTION_NETWORK_NAME = "Set the name of the Network you would like to connect your device to. For example, your home router name. The device will connect to this network at the end of configuration sequence.";
    public static final String QUESTION_NETWORK_PASSWORD = "In case network is secured, set its password";
    public static final String QUESTION_PASSWORD = "Please enter your WiFi password";
    public static final String QUESTION_SET_IOT = "Please set your iotLink UUID (Unique User ID)";
    public static final String SMART_CONFIG_CFG_RESULT_NOT_STARTED = "Simplelink confirmation not started";
    public static final String SMART_CONFIG_MUST_CHOOSE_NETWORK_TO_BEGIN = "Please choose network first";
    public static final int SMART_CONFIG_TRANSMIT_TIME = 40000;
    public static final int SMART_CONFIG_WAITING_BEFORE_MANUAL_CONFIRMATION = 15000;
    public static final int TRY_NUMBER_FOR_GETTING_SSIDS_FROM_DEVICE = 3;
    public static final int WIFI_3G_FAILURE = 57;
    public static final int WIFI_CONNECTION_TIMEOUT = 10000;
    public static final int WIFI_FIRST_STEP_CONNECTION_FAILURE = 44;
    public static final int WIFI_SCAN_TIMEOUT = 10000;
    public static final int WIFI_SETTINGS_INTENT_RESULTS = 4;
    public static final int WIFI_TIMEOUT_FAILURE = 47;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(File.separator);
        sb.append("SmartConfig");
        sb.append(File.separator);
        sb.append("logs");
        sb.append(File.separator);
        sb.append("log4j.txt");
        LOG_PATH = sb.toString();
    }
}
