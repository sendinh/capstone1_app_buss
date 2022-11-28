package com.team03.dtuevent.callbacks;

import android.os.SystemClock;
import android.view.View;

/**
 * A View.OnClickListener replacement that is safe from double clicks.
 */
public abstract class SafeViewClickListener implements View.OnClickListener{
    private long lastTimeClicked = SystemClock.elapsedRealtime();
    private long interval;

    public SafeViewClickListener(long interval) {
        this.interval = interval;
    }



    @Override
    public void onClick(View v) {
        // Set a threshold of 1000ms to prevent double clicks of buttons
        if ((SystemClock.elapsedRealtime() - lastTimeClicked ) < interval) {
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
