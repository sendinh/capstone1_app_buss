package com.team03.dtuevent.objects.actions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.SMS;

public class CopySMSRecipientAction extends Action {
    private static Action INSTANCE;
    private CopySMSRecipientAction() {
        super(App.getStringGlobal(R.string.copy_sms_recipient, "Copy recipient"), null);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopySMSRecipientAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.recipient),
                ((SMS)data).getRecipient()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
