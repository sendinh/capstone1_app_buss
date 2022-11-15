package com.team03.dtuevent.ui.fragment;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.IntDef;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team03.dtuevent.objects.QR;
import com.team03.dtuevent.objects.data.created.ICreatedData;
import com.google.zxing.WriterException;

import java.util.Objects;


public class CreateViewModel extends AndroidViewModel {


    private final MutableLiveData<Integer> currentState; // since current Java version doesn't allow for annotated type arguments, we just use Integer.
    private final MutableLiveData<QR> qr;


    public CreateViewModel(Application app) {
        super(app);
        // Initialization
        currentState = new MutableLiveData<>();
        currentState.setValue( EditState.NONE);

        qr = new MutableLiveData<>();
        qr.setValue(new QR(null, Color.BLACK, Color.WHITE));
        getQrNullSafe().registerObserver(this.qr::setValue);

    }

    public QR getQrNullSafe() {
        return Objects.requireNonNull(qr.getValue());
    }

    public LiveData<QR> getQr() {
        return qr;
    }

    public Bitmap generateImage() throws WriterException {
        return getQrNullSafe().generateBitmap();
    }

    public ICreatedData getContents() {
        return getQrNullSafe().getContents();
    }

    public int getForegroundColor() {
        return getQrNullSafe().getColor();
    }

    public int getBackgroundColor() {
        return getQrNullSafe().getBackground();
    }

    public LiveData<Integer> getCurrentState() {
        return currentState;
    }


    public void setContents(ICreatedData contents) {
        getQrNullSafe().setContents(contents);
    }

    public void setForegroundColor(int foregroundColor) {
        getQrNullSafe().setColor(foregroundColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        getQrNullSafe().setBackground(backgroundColor);
    }


    public void setCurrentState(@EditState int currentState) {
        this.currentState.setValue(currentState);
    }


    public QR getQrValue() {
        return qr.getValue();
    }




    @IntDef({EditState.CONTENTS, EditState.FOREGROUND, EditState.BACKGROUND, EditState.NONE})
    public @interface EditState {
        int CONTENTS = 0;
        int FOREGROUND = 1;
        int BACKGROUND = 2;
        int NONE = 3;
    }



}
