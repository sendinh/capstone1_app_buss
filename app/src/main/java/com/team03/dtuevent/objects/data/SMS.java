package com.team03.dtuevent.objects.data;

import androidx.annotation.NonNull;
import com.google.mlkit.vision.barcode.Barcode;

public class SMS extends com.team03.dtuevent.objects.data.Data {
    private final String recipient;
    private final String contents;


    public SMS(String to, String contents) {
        this.recipient = to;
        this.contents = contents;
    }

    public SMS(Barcode.Sms sms) {
        this.recipient = sms.getPhoneNumber();
        this.contents = sms.getMessage();
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return "To: " + recipient + "\nContents:\n" + contents;
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
