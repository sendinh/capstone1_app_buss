package com.team03.dtuevent.objects.data;

import androidx.annotation.NonNull;

public class Text extends Data {
    private final String data;

    public Text(String data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data.length() == 0;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return data;
    }


}
