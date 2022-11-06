package com.team03.dtuevent.objects.data;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;

public class GeoLocation extends Data {


    /*
    Since lat and lng are simple values, no need for using accessors, can get directly.
     */
    public final double lat;
    public final double lng;

    public GeoLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return App.getStringGlobalWithArgs(R.string.geo_template, lat, lng);
    }

    @Override
    public boolean isEmpty() {
        // double cannot be null/empty.
        return false;
    }

    public Uri getGeoUri() {
        return Uri.parse("geo:" + lat + "," + lng);
    }
}
