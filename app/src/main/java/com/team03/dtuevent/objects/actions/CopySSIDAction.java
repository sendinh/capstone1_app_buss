package com.team03.dtuevent.objects.actions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.WiFi;

public class CopySSIDAction extends Action {
    private static Action INSTANCE;
    private CopySSIDAction() {
        super(App.getStringGlobal(R.string.copy_ssid, "Copy SSID"), null);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopySSIDAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.ssid),
                ((WiFi)data).getSsid()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
