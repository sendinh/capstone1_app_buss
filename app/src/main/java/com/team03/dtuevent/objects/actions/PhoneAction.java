package com.team03.dtuevent.objects.actions;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import com.team03.dtuevent.objects.data.Data;
import com.team03.dtuevent.objects.data.Phone;

public abstract class PhoneAction extends Action {
    private final Function<Data, Phone> phoneRetrievalMethod;
    public PhoneAction(String actionText, @DrawableRes @Nullable Integer icon, Function<Data, Phone> phoneRetrievalMethod) {
        super(actionText, icon);
        this.phoneRetrievalMethod = phoneRetrievalMethod;
    }

    protected Phone getPhone(Data data) {
        return phoneRetrievalMethod.apply(data);
    }
}
