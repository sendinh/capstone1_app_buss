package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.SMS;

public class SMSAction extends com.team03.dtuevent.objects.actions.Action {
    private static com.team03.dtuevent.objects.actions.Action INSTANCE;
    public SMSAction() {
        super(App.getStringGlobal(R.string.start_sms, "Start message"), R.drawable.ic_baseline_sms_24);
    }


    public static com.team03.dtuevent.objects.actions.Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SMSAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        SMS sms = (SMS) data;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + sms.getRecipient()));
        intent.putExtra("sms_body", sms.getContents());
        if (!Utils.launchIntentCheckAvailable(intent, context)) {
            Toast.makeText(context, R.string.no_msg_app_found, Toast.LENGTH_SHORT).show();
        }
    }


}
