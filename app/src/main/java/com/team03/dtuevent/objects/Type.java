package com.team03.dtuevent.objects;

import androidx.room.Entity;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.actions.Action;
import com.team03.dtuevent.objects.actions.AddContactAction;
import com.team03.dtuevent.objects.actions.AddWiFiAction;
import com.team03.dtuevent.objects.actions.CallPhoneAction;
import com.team03.dtuevent.objects.actions.CopyAction;
import com.team03.dtuevent.objects.actions.CopyPasskeyAction;
import com.team03.dtuevent.objects.actions.CopyPhoneAction;
import com.team03.dtuevent.objects.actions.CopySMSContentsAction;
import com.team03.dtuevent.objects.actions.CopySMSRecipientAction;
import com.team03.dtuevent.objects.actions.CopySSIDAction;
import com.team03.dtuevent.objects.actions.EmailAction;
import com.team03.dtuevent.objects.actions.SMSAction;
import com.team03.dtuevent.objects.actions.URLAction;
import com.team03.dtuevent.objects.actions.ViewLocationAction;
import com.team03.dtuevent.objects.data.Phone;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Type {
    private final List<Action> actions;
    private final String typeName;
    private final int typeInt;

    public static Type EMAIL;
    public static Type URL;
    public static Type CONTACT;
    public static Type UNKNOWN_OR_TEXT;
    public static Type WIFI;
    public static Type SMS;
    public static Type PHONE;
    public static Type GEOLOCATION;

    private Type(List<Action> actions, String typeName, int typeInt) {
        actions.add(0, CopyAction.getInstance());
        //actions.add(1, ShareAction.getInstance());
        this.actions = actions;
        this.typeName = typeName;
        this.typeInt = typeInt;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public static Type getTypeFromCode(int barcodeValueType) {

        // TODO if add more types here, also add @ Data.java
        switch (barcodeValueType) {
            case Barcode.TYPE_EMAIL:
                return Type.EMAIL;
            case Barcode.TYPE_CONTACT_INFO:
                return Type.CONTACT;
            case Barcode.TYPE_URL:
                return Type.URL;
            case Barcode.TYPE_WIFI:
                return Type.WIFI;
            case Barcode.TYPE_SMS:
                return Type.SMS;
            case Barcode.TYPE_PHONE:
                return Type.PHONE;
            case Barcode.TYPE_GEO:
                return Type.GEOLOCATION;
            default:
                return Type.UNKNOWN_OR_TEXT;
        }
    }


    // Different predefined types:
    static {

        // Creating actions
        // Email
        ArrayList<Action> emailActions = new ArrayList<>();
        emailActions.add(EmailAction.getInstance());
        EMAIL = new Type(emailActions, App.getStringGlobal(R.string.email, "Email"), Barcode.TYPE_EMAIL);


        // Contacts
        ArrayList<Action> contactActions = new ArrayList<>();
        contactActions.add(AddContactAction.getInstance());
        CONTACT = new Type(contactActions, App.getStringGlobal(R.string.contact, "Contact"), Barcode.TYPE_CONTACT_INFO);


        // URL
        ArrayList<Action> urlActions = new ArrayList<>();
        urlActions.add(URLAction.getInstance());
        URL = new Type(urlActions, App.getStringGlobal(R.string.url, "URL"), Barcode.TYPE_URL);

        // WiFi
        ArrayList<Action> wifiActions = new ArrayList<>();
        wifiActions.add(CopySSIDAction.getInstance());
        wifiActions.add(CopyPasskeyAction.getInstance());
        wifiActions.add(AddWiFiAction.getInstance());
        WIFI = new Type(wifiActions, App.getStringGlobal(R.string.wifi, "Wi-Fi"), Barcode.TYPE_WIFI);


        // SMS
        ArrayList<Action> smsActions = new ArrayList<>();
        smsActions.add(CopySMSRecipientAction.getInstance());
        smsActions.add(CopySMSContentsAction.getInstance());
        smsActions.add(SMSAction.getInstance());
        SMS = new Type(smsActions, App.getStringGlobal(R.string.sms, "SMS"), Barcode.TYPE_SMS);

        // Phone
        ArrayList<Action> phoneActions = new ArrayList<>();
        phoneActions.add(new CopyPhoneAction(data -> (Phone) data));
        phoneActions.add(new CallPhoneAction(data -> (Phone) data));
        PHONE = new Type(phoneActions, App.getStringGlobal(R.string.phone_number, "Phone number"), Barcode.TYPE_PHONE);

        // Geolocation
        ArrayList<Action> locationActions = new ArrayList<>();
        locationActions.add(ViewLocationAction.getInstance());
        GEOLOCATION = new Type(locationActions, App.getStringGlobal(R.string.geolocation, "Geolocation"), Barcode.TYPE_GEO);


        //unknown, same as text
        UNKNOWN_OR_TEXT = new Type(new ArrayList<>(), App.getStringGlobal(R.string.text, "Text"), Barcode.TYPE_TEXT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeInt == type.typeInt &&
                typeName.equals(type.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeInt);
    }
}
