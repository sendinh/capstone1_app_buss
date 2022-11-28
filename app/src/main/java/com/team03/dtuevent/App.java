package com.team03.dtuevent;


import android.app.Application;
import android.content.res.Resources;
import android.util.Log;
import androidx.annotation.StringRes;

import com.androidnetworking.AndroidNetworking;
import com.team03.dtuevent.misc.monetization.AdStrategy2;
import com.google.android.material.color.DynamicColors;
import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.config.Configuration;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfiguration;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**f
 * Application
 */
public class App extends Application {
    public static final String TAG = "App";
    private static WeakReference<Resources> res;

    public static ExecutorService globalExService;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        globalExService = Executors.newFixedThreadPool(processors);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

        AndroidNetworking.initialize(getApplicationContext());

        // Initialize Advertising (only for Google
        AdStrategy2.getInstance(this)
                .initialize();


        // ACRA Crash Reporting
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder();
        //core configuration:
        builder
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON);

        // Plugin configs
        ArrayList<Configuration> acraConfigs = new ArrayList<>();
        String emailBody = "Crash report for Code Scanner\n\n" +
                "Code Scanner just crashed. Send this email with the log files attached to the developers for diagnosis.\n" +
                "This will help us improve the app and make it better!\n" +
                "Sending the log files is entirely at your discretion, if you don't feel comfortable sharing them, you can choose to discard this email draft.\n" +
                "If you don't wish to use your email address, you can email this to yourself, and upload the attached file here: https://forms.gle/2oHZ17SNaYWmzRXTA\n" +
                "Alternatively, if you know how to use GitHub to report issues, you may file an issue here: https://github.com/czlucius/code-scanner/issues (with the log file). \n" +
                "Thanks!\n";

        MailSenderConfiguration mailSenderConfig = new MailSenderConfigurationBuilder()
                .withMailTo(getString(R.string.contact_email))
                .withSubject("Bug report for Code Scanner")
                .withBody(emailBody)
                .withEnabled(true)
                .build();
        acraConfigs.add(mailSenderConfig);
        builder.setPluginConfigurations(acraConfigs);

        ACRA.init(this, builder);

        res = new WeakReference<>(getResources());
    }


    // Get Strings Globally while app is running, but if re
    public static String getStringGlobal(@StringRes int resId, String fallback) {
        if (res != null && res.get() != null) {
            return res.get().getString(resId);
        } else {
            Log.e(TAG, "Warning: Resources invalid. String resources could not be obtained. Defaulting to fallback string");
            return fallback;
        }
    }

    public static String getStringGlobalWithArgs(@StringRes int resId, Object... formatArgs) {
        if (res != null && res.get() != null) {
            return res.get().getString(resId, formatArgs);
        } else {
            throw new IllegalAccessError("App context not available!!!!!");
        }
    }
}


