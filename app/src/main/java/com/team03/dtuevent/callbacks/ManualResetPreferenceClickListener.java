package com.team03.dtuevent.callbacks;

import androidx.preference.Preference;

public abstract class ManualResetPreferenceClickListener implements Preference.OnPreferenceClickListener {
    private boolean enabled = true;


    @Override
    public final boolean onPreferenceClick(Preference preference) {
        if (!enabled) {
            return true;
        }
        enabled = false;

        return onSingleClick(preference);
    }

    public abstract boolean onSingleClick(Preference p);

    public final void resetListener() {
        enabled = true;
    }

    public final com.team03.dtuevent.callbacks.Callback getResetCallback() {
        return this::resetListener;
    }
}
