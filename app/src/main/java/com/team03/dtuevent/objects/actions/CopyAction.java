package com.team03.dtuevent.objects.actions;

import android.content.Context;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.objects.data.Data;

public class CopyAction extends Action {
    private static Action INSTANCE;
    private CopyAction() {
        super(App.getStringGlobal(R.string.copy, "Copy"), null);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopyAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        Utils.copyToClipboard(context, data.getStringRepresentation());
    }


}
