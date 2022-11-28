package com.team03.dtuevent.objects.data.created;

import androidx.annotation.NonNull;

public interface ICreatedData {
    @NonNull
    String getQRData();


    default boolean isEmpty() {
        return getQRData().isEmpty();
    }
}
