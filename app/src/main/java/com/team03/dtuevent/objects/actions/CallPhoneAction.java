package com.team03.dtuevent.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.arch.core.util.Function;

import com.team03.dtuevent.App;
import com.team03.dtuevent.R;
import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.Phone;

public class CallPhoneAction extends com.team03.dtuevent.objects.actions.PhoneAction {
    public CallPhoneAction(Function<Data, Phone> phoneRetrievalMethod) {
        this(phoneRetrievalMethod, App.getStringGlobal(R.string.call_number, "Call number"));
    }

    public CallPhoneAction(Function<Data, Phone> phoneRetrievalMethod, String chipText) {
        super(chipText, R.drawable.ic_baseline_call_24, phoneRetrievalMethod);
    }




    @Override
    public void performAction(Context context, Data data) {
        // May be type Contact or type Phone, so we use the callback function to get our phone.
        Phone phone = getPhone(data);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String telProtocol = "tel:" + phone.getNumber();
        intent.setData(Uri.parse(telProtocol));
        context.startActivity(intent);
    }
}
