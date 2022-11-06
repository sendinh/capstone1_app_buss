package com.team03.dtuevent.objects.data;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

public abstract class Data {
    @NonNull
    public abstract String getStringRepresentation();

    public abstract boolean isEmpty();

    public Intent constructShareIntent()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getStringRepresentation());
        sendIntent.setType("text/plain");

        return sendIntent;
    }


    public String getSummary() {
        return getStringRepresentation();
    }




    /**
     * Override to provide a more efficient equals method.
     * Regular method requires generating human-readable description, which may be expensive.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Data)) return false;
        Data that = (Data) obj;
        return getStringRepresentation().equals(that.getStringRepresentation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStringRepresentation());
    }

    @NonNull
    @Override
    public String toString() {
        return getStringRepresentation();
    }

    public static class Factory {
        private static final Factory INSTANCE = new Factory();

        private Factory() {
        }

        public static Factory getInstance() {
            return INSTANCE;
        }

        // TODO
        // If you add here, also add in Type.java
        public Data create(Barcode barcode) {
            switch (barcode.getValueType()) {
                case Barcode.TYPE_EMAIL:
                    assert barcode.getEmail() != null;
                    return new Email(barcode.getEmail());
                case Barcode.TYPE_CONTACT_INFO:
                    assert barcode.getContactInfo() != null;
                    return new com.team03.dtuevent.objects.data.Contact (barcode.getContactInfo());
                case Barcode.TYPE_URL:
                    assert barcode.getUrl() != null;
                    return new com.team03.dtuevent.objects.data.URL (barcode.getUrl());
                case Barcode.TYPE_WIFI:
                    assert barcode.getWifi() != null;
                    return new com.team03.dtuevent.objects.data.WiFi (barcode.getWifi());
                case Barcode.TYPE_SMS:
                    return new SMS(Objects.requireNonNull(barcode.getSms()));
                case Barcode.TYPE_PHONE:
                    return new Phone(Objects.requireNonNull(barcode.getPhone()));
                case Barcode.TYPE_GEO: {
                    Barcode.GeoPoint geoPoint = Objects.requireNonNull(barcode.getGeoPoint());
                    return new com.team03.dtuevent.objects.data.GeoLocation (geoPoint.getLat(), geoPoint.getLng());
                }
                default:
                    return new com.team03.dtuevent.objects.data.Text (barcode.getDisplayValue());
            }
        }
    }



}
