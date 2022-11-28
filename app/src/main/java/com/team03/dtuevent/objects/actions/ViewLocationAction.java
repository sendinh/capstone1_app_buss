package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.content.Intent;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.GeoLocation;


public class ViewLocationAction extends Action {
    private static Action INSTANCE;
    private ViewLocationAction() {
        super(App.getStringGlobal(R.string.view_location, "View location in maps application"), R.drawable.ic_baseline_location_on_24);
    }
    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewLocationAction();
        }
        return INSTANCE;
    }


    @Override
    public void performAction(Context context, Data data) {
        GeoLocation geo = (GeoLocation) data;
        Intent intent = new Intent(Intent.ACTION_VIEW, geo.getGeoUri());
        context.startActivity(intent);
    }
}
