package com.p004ti.ble.audio;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.ti.ble.simplelinkstarter.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.ti.ble.audio.Audiofiles */
public class Audiofiles extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BroadcastReceiver AdvancedRemoteBLEAudioMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("newFile") != null) {
                Log.d("Audiofiles", "New File available !");
                Audiofiles.this.reloadListView();
            }
        }
    };
    Button clearFiles;
    ListView fileList;
    ArrayList<String> list;

    /* renamed from: com.ti.ble.audio.Audiofiles$fileListArrayAdapter */
    private class fileListArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();

        public boolean hasStableIds() {
            return true;
        }

        public fileListArrayAdapter(Context context, int i, List<String> list) {
            super(context, i, list);
            for (int i2 = 0; i2 < list.size(); i2++) {
                this.mIdMap.put(list.get(i2), Integer.valueOf(i2));
            }
        }

        public long getItemId(int i) {
            return (long) ((Integer) this.mIdMap.get((String) getItem(i))).intValue();
        }
    }

    public static Audiofiles newInstance(int i) {
        Audiofiles audiofiles = new Audiofiles();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, i);
        audiofiles.setArguments(bundle);
        return audiofiles;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        File[] listFiles;
        View inflate = layoutInflater.inflate(R.layout.audiofiles_main, viewGroup, false);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.AdvancedRemoteBLEAudioMessageReceiver, new IntentFilter("ARCBLEAudio-From-Service-Events"));
        if (Environment.getExternalStorageState().equals("mounted")) {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getPath());
            sb.append("/BLEAudioFiles");
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            for (File file2 : file.listFiles()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("File : ");
                sb2.append(file2.getAbsolutePath());
                Log.d("Audiofiles", sb2.toString());
            }
        }
        this.fileList = (ListView) inflate.findViewById(R.id.fileList);
        this.fileList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    Audiofiles.this.playRecording((String) Audiofiles.this.list.get(i));
                } catch (IOException unused) {
                }
            }
        });
        reloadListView();
        this.clearFiles = (Button) inflate.findViewById(R.id.storeAPIKeyButton);
        this.clearFiles.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                File[] listFiles;
                Log.d("info", "Clear files button clicked !");
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory().getPath());
                sb.append("/BLEAudioFiles");
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                for (File file2 : file.listFiles()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("File : ");
                    sb2.append(file2.getAbsolutePath());
                    Log.d("Audiofiles", sb2.toString());
                    file2.delete();
                }
                Audiofiles.this.reloadListView();
            }
        });
        return inflate;
    }

    public void reloadListView() {
        File[] listFiles;
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/BLEAudioFiles");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        this.list = new ArrayList<>();
        for (File file2 : file.listFiles()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("File : ");
            sb2.append(file2.getAbsolutePath());
            Log.d("Audiofiles", sb2.toString());
            this.list.add(file2.getName());
        }
        this.fileList.setAdapter(new fileListArrayAdapter(getActivity(), 17367043, this.list));
    }

    public void playRecording(String str) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("file:///");
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/BLEAudioFiles/");
        sb.append(str);
        Uri parse = Uri.parse(sb.toString());
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(3);
        mediaPlayer.setDataSource(getActivity(), parse);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
}
