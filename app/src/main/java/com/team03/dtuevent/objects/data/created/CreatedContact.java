package com.team03.dtuevent.objects.data.created;

import androidx.annotation.NonNull;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;

public class CreatedContact implements com.team03.dtuevent.objects.data.created.ICreatedData {
    public final String firstName;
    public final String lastName;
    public final String prefix;
    public final String suffix;
    public final String company;
    public final String job;
    public final String phoneNo;
    public final String email;
    public final String street;
    public final String zipCode;
    public final String region;
    public final String country;
    public final String url;
    public final String additionalNotes;

    public CreatedContact(String firstName, String lastName, String prefix, String suffix, String company, String job, String phoneNo, String email, String street, String zipCode, String region, String country, String url, String additionalNotes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.company = company;
        this.job = job;
        this.phoneNo = phoneNo;
        this.email = email;
        this.street = street;
        this.zipCode = zipCode;
        this.region = region;
        this.country = country;
        this.url = url;
        this.additionalNotes = additionalNotes;
    }

    @NonNull
    @Override
    public String getQRData() {

        VCard vCard = new VCard();
        StructuredName name = new StructuredName();
        name.setGiven(firstName);
        name.setFamily(lastName);
        name.getPrefixes().add(prefix);
        name.getPrefixes().add(suffix);
        vCard.setStructuredName(name);


        Organization org = new Organization();
        org.getValues().add(job);
        org.getValues().add(company);
        vCard.addOrganization(org);

        vCard.addTelephoneNumber(phoneNo);
        vCard.addEmail(email);

        Address address = new Address();
        address.setStreetAddress(street);
        address.setRegion(region);
        address.setCountry(country);
        address.setPostalCode(zipCode);
        vCard.addAddress(address);

        vCard.addUrl(url);
        vCard.addNote(additionalNotes);
        return vCard.write();
    }

    @Override
    public boolean isEmpty() {
        return
                firstName.isEmpty() && lastName.isEmpty() && prefix.isEmpty()
                        && suffix.isEmpty() && company.isEmpty() && job.isEmpty()
                        && phoneNo.isEmpty() && email.isEmpty() && street.isEmpty()
                        && zipCode.isEmpty() && region.isEmpty() && country.isEmpty()
                        && url.isEmpty() && additionalNotes.isEmpty();
    }

    public static CreatedContact EMPTY = new CreatedContact("", "", "", "", "", "", "", "", "", "", "", "", "", "");

}
