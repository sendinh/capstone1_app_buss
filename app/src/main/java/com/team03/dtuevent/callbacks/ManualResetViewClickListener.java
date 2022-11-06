package com.team03.dtuevent.callbacks;

import android.view.View;

public abstract class ManualResetViewClickListener implements View.OnClickListener {
    private boolean enabled = true;

    @Override
    public final void onClick(View v) {
        // Manual enabled switch to prevent double clicks until reset
        if (!enabled) {
            return;
        }
        enabled = false;

        onSingleClick(v);
    }

    public abstract void onSingleClick(View v);

    public final void resetListener() {
        enabled = true;
    }

    public final com.team03.dtuevent.callbacks.Callback getResetCallback() {
        return this::resetListener;
    }
}
