package com.team03.dtuevent.objects.actions;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.SMS;

public class CopySMSContentsAction extends Action {
    private static Action INSTANCE;
    private CopySMSContentsAction() {
        super(App.getStringGlobal(R.string.copy_sms_contents, "Copy SMS contents"), null);
    }


    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopySMSContentsAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.contents),
                ((SMS)data).getContents()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
