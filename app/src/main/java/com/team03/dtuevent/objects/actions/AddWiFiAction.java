package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.exceptions.NetworkInvalidException;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.WiFi;

public class AddWiFiAction extends com.team03.dtuevent.objects.actions.Action {
    private static com.team03.dtuevent.objects.actions.Action INSTANCE;
    private AddWiFiAction() {
        super(App.getStringGlobal(R.string.connect_wifi, "Connect"), R.drawable.ic_baseline_signal_wifi_4_bar_24);
    }

    public static com.team03.dtuevent.objects.actions.Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddWiFiAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {

        WiFi wifi = (WiFi) data;
        try {
            wifi.connect(context);
        } catch (NetworkInvalidException e) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                errorMsg(context, R.string.connection_error_simple);
            } else {
                switch (e.getType()) {
                    case NetworkInvalidException.INTERNAL_ERR:
                        errorMsg(context, R.string.connection_internal_error);
                    case NetworkInvalidException.APP_DISALLOWED:
                        errorMsg(context, R.string.connection_app_disallowed);
                        break;
                    case NetworkInvalidException.DUPLICATE:
                        errorMsg(context, R.string.connection_duplicate);
                        break;
                    case NetworkInvalidException.EXCEED_MAX_LIMIT:
                        errorMsg(context, R.string.connection_app_exceed_limit);
                        break;
                    case NetworkInvalidException.NETWORK_INVALID:
                        errorMsg(context, R.string.connection_ntwk_invalid);
                        break;
                    case NetworkInvalidException.NETWORK_NOT_ALLOWED:
                        errorMsg(context, R.string.connection_ntwk_disallowed);
                        break;
                    case NetworkInvalidException.SIMPLE_ERROR:
                        e.printStackTrace();
                        break;
                    default:
                        e.printStackTrace();
                        break;

                }
            }
        }
    }

    private void errorMsg(Context context, @StringRes int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }

}
