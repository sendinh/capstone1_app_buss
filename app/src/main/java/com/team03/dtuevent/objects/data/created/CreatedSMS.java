package com.team03.dtuevent.objects.data.created;

import androidx.annotation.NonNull;

public class CreatedSMS implements com.team03.dtuevent.objects.data.created.ICreatedData {
    private final String recipient;
    private final String contents;


    public CreatedSMS(String to, String contents) {
        this.recipient = to;
        this.contents = contents;
    }

    @NonNull
    @Override
    public String getQRData() {
        return "SMSTO:" + recipient + ":" + contents;
    }

    @Override
    public boolean isEmpty() {
        return (recipient == null || recipient.isEmpty()) || (contents == null || contents.isEmpty());
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContents() {
        return contents;
    }
}
