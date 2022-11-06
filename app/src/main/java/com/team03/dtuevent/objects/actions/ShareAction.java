package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.content.Intent;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;

public class ShareAction extends Action {
    private static Action INSTANCE;
    private ShareAction() {
        super(App.getStringGlobal(R.string.share, "Share"), R.drawable.ic_baseline_share_24);
    }
    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShareAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        Intent intent = data.constructShareIntent();
        context.startActivity(intent);
    }
}
