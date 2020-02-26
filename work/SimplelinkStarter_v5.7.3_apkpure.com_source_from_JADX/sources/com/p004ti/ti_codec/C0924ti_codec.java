package com.p004ti.ti_codec;

/* renamed from: com.ti.ti_codec.ti_codec */
public class C0924ti_codec {
    public short PV_Dec = 0;
    public short SI_Dec = 0;

    /* renamed from: com.ti.ti_codec.ti_codec$TIcodecConstants */
    private static class TIcodecConstants {
        /* access modifiers changed from: private */
        public static final byte[] codec_IndexLut = {-1, -1, -1, -1, 2, 4, 6, 8, -1, -1, -1, -1, 2, 4, 6, 8};
        /* access modifiers changed from: private */
        public static final short[] codec_stepsize_Lut = {7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143, 157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, Short.MAX_VALUE};

        private TIcodecConstants() {
        }
    }

    public short codec_DecodeSingle(int i) {
        short[] access$000 = TIcodecConstants.codec_stepsize_Lut;
        short s = this.SI_Dec;
        short s2 = access$000[s];
        short s3 = (short) (s2 >> 3);
        this.SI_Dec = (short) (s + TIcodecConstants.codec_IndexLut[i]);
        short s4 = this.SI_Dec;
        if (s4 < 0) {
            this.SI_Dec = 0;
        } else if (s4 > 88) {
            this.SI_Dec = 88;
        }
        if ((i & 4) != 0) {
            s3 = (short) (s3 + s2);
        }
        if ((i & 2) != 0) {
            s3 = (short) (s3 + (s2 >> 1));
        }
        if ((i & 1) != 0) {
            s3 = (short) (s3 + (s2 >> 2));
        }
        if ((i & 8) != 0) {
            short s5 = this.PV_Dec;
            if (s5 < s3 - Short.MAX_VALUE) {
                this.PV_Dec = -32767;
            } else {
                this.PV_Dec = (short) (s5 - s3);
            }
        } else {
            short s6 = this.PV_Dec;
            if (s6 > Short.MAX_VALUE - s3) {
                this.PV_Dec = Short.MAX_VALUE;
            } else {
                this.PV_Dec = (short) (s6 + s3);
            }
        }
        return this.PV_Dec;
    }
}
