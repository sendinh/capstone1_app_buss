package com.team03.dtuevent.objects.actions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.WiFi;

public class CopyPasskeyAction extends Action {
    private static Action INSTANCE;
    private CopyPasskeyAction() {
        super(App.getStringGlobal(R.string.copy_password, "Copy password"), null);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopyPasskeyAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.password),
                ((WiFi)data).getPassword()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
