package com.p004ti.ble.p005ti.profiles;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.ti.ble.simplelinkstarter.R;
import org.apache.http.protocol.HTTP;

/* renamed from: com.ti.ble.ti.profiles.TILampControlDialogFragment */
public class TILampControlDialogFragment extends DialogFragment {
    public static final String ACTION_LAMP_HSI_COLOR_CHANGED = "org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED";
    public static final String EXTRA_LAMP_HSI_COLOR_H = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_H";
    public static final String EXTRA_LAMP_HSI_COLOR_I = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_I";
    public static final String EXTRA_LAMP_HSI_COLOR_S = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_S";

    /* renamed from: H */
    double f52H;

    /* renamed from: I */
    double f53I;

    /* renamed from: S */
    double f54S;
    float downx = 0.0f;
    float downy = 0.0f;
    SeekBar intensityBar;
    float upx = 0.0f;
    float upy = 0.0f;

    /* renamed from: v */
    View f55v;

    public static TILampControlDialogFragment newInstance() {
        TILampControlDialogFragment tILampControlDialogFragment = new TILampControlDialogFragment();
        new Bundle();
        return tILampControlDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Builder positiveButton = new Builder(getActivity()).setTitle("Set color").setPositiveButton(HTTP.CONN_CLOSE, null);
        this.f55v = getActivity().getLayoutInflater().inflate(R.layout.lamp_control, null);
        ImageView imageView = (ImageView) this.f55v.findViewById(R.id.colorPicker);
        this.intensityBar = (SeekBar) this.f55v.findViewById(R.id.colorIntensitySeekBar);
        this.intensityBar.setMax(1000);
        if (imageView != null) {
            SeekBar seekBar = this.intensityBar;
            if (seekBar != null) {
                seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                        TILampControlDialogFragment tILampControlDialogFragment = TILampControlDialogFragment.this;
                        tILampControlDialogFragment.f53I = ((double) i) / 1000.0d;
                        tILampControlDialogFragment.broadCastLightValue();
                    }
                });
                imageView.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        if (action == 0) {
                            TILampControlDialogFragment.this.downx = motionEvent.getX();
                            TILampControlDialogFragment.this.downy = motionEvent.getY();
                        } else if (action == 1) {
                            TILampControlDialogFragment.this.upx = motionEvent.getX();
                            TILampControlDialogFragment.this.upy = motionEvent.getY();
                        } else if (action == 2) {
                            TILampControlDialogFragment.this.upx = motionEvent.getX();
                            TILampControlDialogFragment.this.upy = motionEvent.getY();
                            TILampControlDialogFragment tILampControlDialogFragment = TILampControlDialogFragment.this;
                            tILampControlDialogFragment.downx = tILampControlDialogFragment.upx;
                            TILampControlDialogFragment tILampControlDialogFragment2 = TILampControlDialogFragment.this;
                            tILampControlDialogFragment2.downy = tILampControlDialogFragment2.upy;
                        }
                        double width = (double) (TILampControlDialogFragment.this.upx - ((float) (view.getWidth() / 2)));
                        double height = (double) (TILampControlDialogFragment.this.upy - ((float) (view.getHeight() / 2)));
                        TILampControlDialogFragment.this.f54S = Math.sqrt(Math.pow(width, 2.0d) + Math.pow(height, 2.0d)) / ((double) (view.getWidth() / 2));
                        TILampControlDialogFragment.this.f52H = ((Math.atan2(height, width) * 180.0d) / 3.141592653589793d) - 180.0d;
                        TILampControlDialogFragment tILampControlDialogFragment3 = TILampControlDialogFragment.this;
                        tILampControlDialogFragment3.f53I = ((double) tILampControlDialogFragment3.intensityBar.getProgress()) / 1000.0d;
                        TILampControlDialogFragment.this.f52H += 180.0d;
                        if (TILampControlDialogFragment.this.f52H > 360.0d) {
                            TILampControlDialogFragment.this.f52H -= 360.0d;
                        }
                        if (TILampControlDialogFragment.this.f52H < 0.0d) {
                            TILampControlDialogFragment.this.f52H += 360.0d;
                        }
                        if (TILampControlDialogFragment.this.f54S > 1.0d) {
                            TILampControlDialogFragment.this.f54S = 1.0d;
                        }
                        if (TILampControlDialogFragment.this.f54S < 0.0d) {
                            TILampControlDialogFragment.this.f54S = 0.0d;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("S: ");
                        sb.append(TILampControlDialogFragment.this.f54S);
                        sb.append(" H:");
                        sb.append(TILampControlDialogFragment.this.f52H);
                        sb.append(" I:");
                        sb.append(TILampControlDialogFragment.this.f53I);
                        Log.d("TILampControlDialogFragment", sb.toString());
                        TILampControlDialogFragment.this.broadCastLightValue();
                        return true;
                    }
                });
            }
        }
        positiveButton.setView(this.f55v);
        return positiveButton.create();
    }

    /* access modifiers changed from: 0000 */
    public void broadCastLightValue() {
        Intent intent = new Intent(ACTION_LAMP_HSI_COLOR_CHANGED);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_H, this.f52H);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_I, this.f53I);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_S, this.f54S);
        getActivity().sendBroadcast(intent);
    }
}
