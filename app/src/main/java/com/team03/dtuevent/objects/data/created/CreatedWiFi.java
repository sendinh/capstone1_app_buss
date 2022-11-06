package com.team03.dtuevent.objects.data.created;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;

public class CreatedWiFi implements com.team03.dtuevent.objects.data.created.ICreatedData {
    private final String ssid;
    private final String password;
    private final EncryptionType encryptionType;

    public CreatedWiFi(String ssid, String password, EncryptionType encryptionType) {
        this.ssid = ssid;
        this.password = password;
        this.encryptionType = encryptionType;
    }

    @NonNull
    @Override
    public String getQRData() {
        return "WIFI:"
                + "T:"
                + encryptionType.value
                + ";S:"
                + ssid
                + ";P:"
                + password
                + ";;";
    }


    public enum EncryptionType {
        Open(0,"nopass", R.string.open_network), WEP(1,"WEP", R.string.wep), WPA(2,"WPA", R.string.wpa);

        public final int index;
        private final String value;
        private final @StringRes int displayName;
        EncryptionType(int index, String value, @StringRes int displayName) {
            this.index = index;
            this.value = value;
            this.displayName = displayName;
        }

        @NonNull
        @Override
        public String toString() {
            return App.getStringGlobal(displayName, name());
        }
    }

    @Override
    public boolean isEmpty() {
        return ssid.isEmpty() && password.isEmpty();
    }

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }


}