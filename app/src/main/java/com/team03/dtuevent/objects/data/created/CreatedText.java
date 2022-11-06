package com.team03.dtuevent.objects.data.created;

import androidx.annotation.NonNull;

public class CreatedText implements com.team03.dtuevent.objects.data.created.ICreatedData {
    protected final String text;

    public CreatedText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String getQRData() {
        return text;
    }

    public String getText() {
        return text;
    }
}
