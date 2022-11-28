package com.team03.dtuevent.objects.actions;

import android.content.Context;

import androidx.arch.core.util.Function;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.Utils;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.Phone;

public class CopyPhoneAction extends com.team03.dtuevent.objects.actions.PhoneAction {
    public CopyPhoneAction(Function<Data, Phone> phoneRetrievalMethod) {
        this(phoneRetrievalMethod, App.getStringGlobal(R.string.copy_phone_number, " Copy phone number"));
    }

    public CopyPhoneAction(Function<Data, Phone> phoneRetrievalMethod, String chipText) {
        super(chipText, null, phoneRetrievalMethod);
    }

    @Override
    public void performAction(Context context, Data data) {
        Phone phone = getPhone(data);
        Utils.copyToClipboard(context, phone.getStringRepresentation());
    }
}
