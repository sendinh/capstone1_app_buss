package com.team03.dtuevent.misc.monetization;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.arch.core.util.Function;

import com.team03.dtuevent.callbacks.Callback;

public class AdStrategy2 {

    private AdStrategy2() { /* stub */ }

    public static AdStrategy2 getInstance(Context appCtx) {
        return new AdStrategy2();
    }

    public void initialize() { /* stub */ }

    public void loadAdView(Function<Integer, View> findViewByIdProducer) { /* stub */ }

    public void addAdViewTo(ViewGroup parent) {}

    public void loadRewardedAdVideo(Activity activity, View root, Callback resetCallback) { /* stub */ }

    @Deprecated
    public void initialiseRewardedAds(Activity activity, View root) { /* stub */ }




}
