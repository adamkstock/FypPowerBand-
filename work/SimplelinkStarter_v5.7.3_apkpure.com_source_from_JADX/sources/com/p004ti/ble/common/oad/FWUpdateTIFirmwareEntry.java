package com.p004ti.ble.common.oad;

/* renamed from: com.ti.ble.common.oad.FWUpdateTIFirmwareEntry */
public class FWUpdateTIFirmwareEntry {
    public final String BoardType;
    public final boolean Custom;
    public final String Description;
    public final String DevPack;
    public final String Filename;
    public final String MCU;
    public final int OADAlgo;
    public final float RequiredVersionRev;
    public final boolean SafeMode;
    public boolean Security;
    public final String Title;
    public final String Type;
    public final float Version;
    public final String WirelessStandard;
    public boolean compatible = true;
    public boolean onChip;

    public FWUpdateTIFirmwareEntry(String str, boolean z, String str2, String str3, int i, String str4, float f, boolean z2, float f2, String str5, String str6, String str7, boolean z3, String str8, boolean z4) {
        this.Filename = str;
        this.Custom = z;
        this.WirelessStandard = str2;
        this.Type = str3;
        this.OADAlgo = i;
        this.BoardType = str4;
        this.RequiredVersionRev = f;
        this.SafeMode = z2;
        this.Version = f2;
        this.DevPack = str5;
        this.Description = str6;
        this.MCU = str7;
        this.onChip = z3;
        this.Title = str8;
        this.Security = z4;
    }
}
