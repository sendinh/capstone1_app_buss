package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.net.Uri;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.URL;

public class URLAction extends com.team03.dtuevent.objects.actions.Action {
    private static com.team03.dtuevent.objects.actions.Action INSTANCE;

    private URLAction() {
        super(App.getStringGlobal(R.string.open_url, "Open URL"), R.drawable.ic_baseline_open_in_new_24);
    }

    public static com.team03.dtuevent.objects.actions.Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new URLAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        //data is a url object
        URL url = (URL) data;
        Uri rawWebAddress = Uri.parse(url.getUrlAddress());

        // Prepend a http schema in front if url comes without schema.
        // See https://github.com/czlucius/code-scanner/issues/20
        Uri.Builder builder = rawWebAddress.buildUpon();
        if (rawWebAddress.getScheme() == null || rawWebAddress.getScheme().isEmpty()) {
            builder.scheme("http");
        }
        Uri webpage = builder.build();

        Utils.launchWebPageExternally(context, webpage);
    }


}
