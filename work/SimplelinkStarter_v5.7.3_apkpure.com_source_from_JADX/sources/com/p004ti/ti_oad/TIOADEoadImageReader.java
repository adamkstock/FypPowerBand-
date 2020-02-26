package com.p004ti.ti_oad;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.p004ti.ti_oad.TIOADEoadHeader.TIOADEoadSegmentInformation;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* renamed from: com.ti.ti_oad.TIOADEoadImageReader */
public class TIOADEoadImageReader {
    private final String TAG = TIOADEoadImageReader.class.getSimpleName();
    private Context context;
    public TIOADEoadHeader imageHeader;
    private ArrayList<TIOADEoadSegmentInformation> imageSegments = new ArrayList<>();
    private byte[] rawImageData;

    public TIOADEoadImageReader(Uri uri, Context context2) {
        this.context = context2;
        TIOADToadLoadImageFromDevice(uri);
    }

    public TIOADEoadImageReader(String str, Context context2) {
        this.context = context2;
        TIOADToadLoadImage(str);
    }

    public void TIOADToadLoadImage(String str) {
        try {
            InputStream open = this.context.getAssets().open(str);
            this.rawImageData = new byte[open.available()];
            int read = open.read(this.rawImageData);
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Read ");
            sb.append(read);
            sb.append(" bytes from asset file");
            Log.d(str2, sb.toString());
            this.imageHeader = new TIOADEoadHeader(this.rawImageData);
            this.imageHeader.validateImage();
        } catch (IOException unused) {
            Log.d(this.TAG, "Could not read input file");
        }
    }

    public void TIOADToadLoadImageFromDevice(Uri uri) {
        try {
            InputStream openInputStream = this.context.getContentResolver().openInputStream(uri);
            this.rawImageData = new byte[openInputStream.available()];
            int read = openInputStream.read(this.rawImageData);
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Read ");
            sb.append(read);
            sb.append(" bytes from file");
            Log.d(str, sb.toString());
            this.imageHeader = new TIOADEoadHeader(this.rawImageData);
            this.imageHeader.validateImage();
        } catch (IOException unused) {
            Log.d(this.TAG, "Could not read input file");
        }
    }

    public byte[] getRawImageData() {
        return this.rawImageData;
    }

    public byte[] getHeaderForImageNotify() {
        byte[] bArr = new byte[22];
        System.arraycopy(this.imageHeader.TIOADEoadImageIdentificationValue, 0, bArr, 0, this.imageHeader.TIOADEoadImageIdentificationValue.length);
        int length = this.imageHeader.TIOADEoadImageIdentificationValue.length + 0;
        int i = length + 1;
        bArr[length] = this.imageHeader.TIOADEoadBIMVersion;
        int i2 = i + 1;
        bArr[i] = this.imageHeader.TIOADEoadImageHeaderVersion;
        System.arraycopy(this.imageHeader.TIOADEoadImageInformation, 0, bArr, i2, this.imageHeader.TIOADEoadImageInformation.length);
        int length2 = i2 + this.imageHeader.TIOADEoadImageInformation.length;
        int i3 = 0;
        while (i3 < 4) {
            int i4 = length2 + 1;
            bArr[length2] = TIOADEoadDefinitions.GET_BYTE_FROM_UINT32(this.imageHeader.TIOADEoadImageLength, i3);
            i3++;
            length2 = i4;
        }
        System.arraycopy(this.imageHeader.TIOADEoadImageSoftwareVersion, 0, bArr, length2, this.imageHeader.TIOADEoadImageSoftwareVersion.length);
        int length3 = this.imageHeader.TIOADEoadImageSoftwareVersion.length;
        return bArr;
    }
}
