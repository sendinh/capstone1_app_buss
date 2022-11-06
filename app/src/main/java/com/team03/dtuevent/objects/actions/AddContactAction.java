package com.team03.dtuevent.objects.actions;

import static android.provider.ContactsContract.Intents.Insert.DATA;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.objects.data.Address;
import com.team03.dtuevent.objects.data.Contact;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.Email;
import com.team03.dtuevent.objects.data.Name;
import com.team03.dtuevent.objects.data.Phone;

import java.util.ArrayList;

public class AddContactAction extends com.team03.dtuevent.objects.actions.Action {
    private static AddContactAction INSTANCE;
    private AddContactAction() {
        super(App.getStringGlobal(R.string.add_contact, "Add Contact"), R.drawable.ic_baseline_add_circle_24);
    }

    public static AddContactAction getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new AddContactAction();
        }
        return INSTANCE;
    }


    @Override
    public void performAction(Context context, Data data) {
        Contact contactInfo = (Contact) data;


        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        ArrayList<ContentValues> dataList = new ArrayList<>();

        // Name
        ContentValues names = new ContentValues();

        Name name = contactInfo.getName();
        // Prefixes and Suffixes
       names.put(ContactsContract.CommonDataKinds.StructuredName.PREFIX, name.getPrefix());
       names.put(ContactsContract.CommonDataKinds.StructuredName.SUFFIX, name.getSuffix());
        // Add First Name (Given Name)
       names.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name.getFirstName());
        // Middle Name
       names.put(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, name.getMiddleName());
        // Family Name (Last Name)
       names.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name.getLastName());

       names.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name.getFormattedName());

       dataList.add(names);

        // Add Phone numbers
        Phone[] phoneList = contactInfo.getPhones();

        for (Phone phone : phoneList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getNumber());
            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, Utils.convertPhoneType(phone.getType()));
            dataList.add(row);
        }


        // Org & Job title
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contactInfo.getTitle());
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contactInfo.getOrganization());


        Email[] emailList = contactInfo.getEmails();


        for (Email email : emailList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email.getAddress());
            row.put(ContactsContract.CommonDataKinds.Email.TYPE, Utils.convertEmailType(email.getType()));
            dataList.add(row);
        }



        String[] urlList = contactInfo.getUrls();


        for (String url: urlList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Website.URL, url);
            dataList.add(row);

        }



        Address[] addresses = contactInfo.getAddresses();


        for (Address address: addresses) {
            for (String formattedAddr: address.getFormattedAddresses()) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, formattedAddr);
            }


        }

        intent.putParcelableArrayListExtra(DATA, dataList);
        context.startActivity(intent);

    }
}
