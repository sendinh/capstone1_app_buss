package com.team03.dtuevent.objects.data;

import androidx.annotation.NonNull;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

public class Email extends com.team03.dtuevent.objects.data.Data {
    private final String address;
    private final String body;
    private final String subject;
    private final int type;

    @NonNull
    @Override
    public String getStringRepresentation() {
        return App.getStringGlobal(R.string.email, "Email")
                + ": \n"
                + App.getStringGlobal(R.string.to, "To")
                + " <"
                + address + ">\n"
                + App.getStringGlobal(R.string.subject, "Subject")
                + ": \""
                + subject
                + "\"\n"
                + App.getStringGlobal(R.string.contents, "Contents")
                + ":\n"
                + body;
    }


    @Override
    public boolean isEmpty() {
        return address.isEmpty() && body.isEmpty() && subject.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Email email = (Email) o;
        return type == email.type &&
                Objects.equals(address, email.address) &&
                Objects.equals(body, email.body) &&
                Objects.equals(subject, email.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, body, subject, type);
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public int getType() {
        return type;
    }


    public Email(Barcode.Email email) {
        address = email.getAddress();
        body = email.getBody();
        subject = email.getSubject();
        type = email.getType();

    }


}
